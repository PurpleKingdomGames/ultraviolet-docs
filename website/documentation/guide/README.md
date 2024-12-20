## Writing Shaders

Ultraviolet functions within a couple of significant constraints that affect how it works and its usability.

The first constraint, as mentioned elsewhere, is that GLSL is NOT a general purpose programming language. While we can represent _most_ of GLSL in Scala, we _cannot_ represent (in any useful sense) all of Scala in GLSL. The code might look like Scala, but it isn't, and you may need to be a little forgiving / creative with your coding approach. There are some functional niceties in Ultraviolet, but first and foremost this is imperative, C-like Scala.

The second constraint is that shaders in Ultraviolet rely on Scala 3's inline macros in order to function. This has the benefit of allowing you to reuse and share code, and the drawback that you need to jump through a few hoops to make that work... and you just have to know about them.

We're going to look at the basics below, but in general it is best to [look at the examples](/examples/README.md) for real code that you can be sure is up to date and working. Code presented here will be kept to a minimum to hopefully avoid it falling out of date and being misleading.

### Getting started

The `Shader` block is the root of any Ultraviolet shader. Within this context, you write your vertex or fragment shader code, which will ultimately be converted to GLSL.

A small, contrived, Indigo flavored fragment shader looks like this:

```scala
import ultraviolet.syntax.*

object MyShader:

  inline def fragmentShader: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      def calculateBlue(uv: vec2): Float =
        uv.x

      def fragment(color: vec4): vec4 =
        // Here we're making a vec4 using the UV coordinates, a vec2, plugged into the
        // red and green values, followed by the UV.x that has been extracted by a function
        // as a Float for the blue value, and setting the alpha to full/max, i.e. 1.0f.
        vec4(env.UV, calculateBlue(env.UV), 1.0f)
    }
```

A shader is always written within a `Shader` 'block' that takes an environment type, and an initial function which is called `fragment` for fragment shaders and `vertex` for vertex shaders.

The `fragment` and `vertex` functions must return a `vec4`, representing an RGBA (i.e. `vec4(red, green, blue, alpha)`) color or a vertex position, respectively. In the case of vertex shaders you can also set additional fields, such as the `UV` value, or set `varying` values that will be sent across to the fragment shader.

Within a `Shader` block, given the constraints mentioned, we can write our code more or less as usual with variables and functions and so on.

### Sharing and Testing Code

Putting all your shader code in the shader block is the most straight forward way to write shaders, but it has one big problem: It's difficult to test and share / reuse.

However, thanks to the power of inlining and the fact that Ultraviolet is built on inline macros, there is a way around this limitation.

Here is the same code again, but this time with the `calculateBlue` function moved out of the shader block.

```scala
import ultraviolet.syntax.*

object MyShader:

  // MUST be marked as `inline`
  inline def calculateBlue(uv: vec2): Float =
    uv.x

  inline def fragmentShader: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      val proxy: vec2 => Float =
        uv => calculateBlue(uv)

      def fragment(color: vec4): vec4 =
        // Here we're making a vec4 using the UV coordinates, a vec2, plugged into the
        // red and green values, followed by the UV.x that has been extracted by a function
        // as a Float for the blue value, and setting the alpha to full/max, i.e. 1.0f.
        vec4(env.UV, proxy(env.UV), 1.0f)
    }
```

This ends up working exactly the same as the original, but the `calculateBlue` function is now outside the shader block, and can be unit tested. To make that work, it was _necessary_ to include a `proxy` function locally within the shader block.

If you wanted to use that function again in a different shader, you can use a particular formulation of `import`, like this.

```scala
object MyOtherShader:

  // Must import the function or *
  import MyShader.*

  inline def differentShader: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      val proxy: vec2 => Float =
        uv => calculateBlue(uv)

      def fragment(color: vec4): vec4 =
        vec4(0.0f, 0.0f, proxy(env.UV), 1.0f)
    }
```

If you do _not_ use the function `proxy`, the code from your function will be inlined at the call site. That might be ok if it is one line of simple logic, but more likely it will do something odd and produce errors at compile or runtime. 

If you want to reuse values outside your shader place, use `final val`s or `inline def`s, i.e.:

```scala
// These work
final val x  = 1.0f
inline def y = 2.0f

// These compile because Scala can see them, but do not work because they don't exist in the GLSL.
val z        = 3.0f
inline val w = 4.0f

inline def shader: Shader[FragmentEnv, Unit] =
  Shader[FragmentEnv] { env =>
    def fragment(color: vec4): vec4 =
      vec4(x, y, z, w)
  }
```

Produces a GLSL `vec4` of `vec4(1.0f,2.0f,z,w)` where `z` and `w` do not refer to anything in the final GLSL shader program, and will error.

### Best practices

'Best practices' seems a bit strong, but here are some 'good' practices to consider:

#### Unit tests that check the output

It is a good idea to check that the output of your code is what you expect, particularly if you've tried to run it and gotten an error or no output.

To do that, consider writing a simple acceptance test, like the ones found [here](https://github.com/PurpleKingdomGames/ultraviolet/blob/main/ultraviolet/shared/src/test/scala/ultraviolet/acceptance/GLSLExamplesTests.scala):

```scala
  test("can build a multi-statement function") {

    inline def shader: Shader[FragEnv, vec4] =
      Shader { env =>
        def calculateColour(uv: vec2, sdf: Float): vec4 =
          val fill       = vec4(uv, 0.0f, 1.0f)
          val fillAmount = (1.0f - step(0.0f, sdf)) * fill.w
          vec4(fill.xyz * fillAmount, fillAmount)

        calculateColour(env.UV, 3.0)
      }

    val actual =
      shader.toGLSL[WebGL2].toOutput.code

    assertEquals(
      actual,
      s"""
      |vec4 calculateColour(in vec2 uv,in float sdf){
      |  vec4 fill=vec4(uv,0.0,1.0);
      |  float fillAmount=(1.0-step(0.0,sdf))*fill.w;
      |  return vec4(fill.xyz*fillAmount,fillAmount);
      |}
      |calculateColour(UV,3.0);
      |""".stripMargin.trim
    )
  }
```

Note that if you're code is failing validation, perhaps with an illegal forward reference error, you can disable validation like this, so you can see what is going on.

```diff
-shader.toGLSL[WebGL2].toOutput.code
+shader.toGLSL[WebGL2](false).toOutput.code
```

#### Pure functions, always

Function definitions, particularly for imported or anonymous functions, are moved to the top of the code that gets output. This can lead to "illegal forward reference" errors if you try to make use of local variables you expect to be present.

Constant variables are available to functions, but in general you can avoid a lot of heartache by keeping your functions pure, i.e. they only operate on their arguments, not external state / values.
