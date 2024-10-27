
## Gotcha's, foot guns, and weird stuff

### UX/DX/API rough edges

#### Indigo import collisions

If you're using Ultraviolet with Indigo, then you stand a chance of import collisions. The main culprits are that both Ultraviolet and Indigo contains instances of things like classes called `Shader` and `vec4`.

The simplest workaround here is to declare your UV shader code in an object in a separate file, or in a sub object as follows:

```scala
import indigo.* // <-- bring in problem imports

object MyCode:
  
  //...

  object MyShader:
    import ultraviolet.syntax.* // <-- problem import are effectively replaced

    inline def myShader = ???

```

This tactic is used in all the examples.

#### Environment definitions

If your shaders environment is simple, like the shadertoy one, and can be defined using a single case class that may or may not also be declared as a UBO, then all is well.

More typically, you have some environment and several UBO definitions, and you'd like your code to be able to access all of them, e.g.:

```scala
final case class UBO1(count: Int)
final case class UBO2(pos: vec2)

// Delcare all this fields that make up `env`
Shader[FragmentEnv & UBO1 & UBO2] { env =>
  ubo[UBO1] // Tells ultraviolet to define the UBO1 data structure
  ubo[UBO2] // Tells ultraviolet to define the UBO1 data structure

  env.count // can be a field on FragmentEnv, UBO1, or UBO2
}
```

So far, the above works. The problem is that at the point of converting to GLSL code, you have to provide a dummy, but real instance of that funky type `FragmentEnv & UBO1 & UBO2`

You can't avoid declaring `UBO1` and `UBO2` because UV needs them to create the correct definitions, but making `FragmentEnv & UBO1 & UBO2` isn't a fun exercise.

Unfortunately, the best solution I've found so far is to replace `FragmentEnv & UBO1 & UBO2` with something like this:

```scala
final case class Env(count: Int, pos: vec2) extends FragmentEnvReference
object Env:
  def reference: Env = Env(0, vec2(0.0f))
```

Note that `FragmentEnv` is a real thing in Indigo, it provides Indigo's standard Fragment environment (there is also `VertexEnv` and `BlendFragmentEnv`). `FragmentEnvReference` is a trait with the same fields as `FragmentEnv`, with all the values hard coded so that you can make an instance easily.

### Compile time and Runtime errors

> TL;DR: Some errors only happen at runtime, if you want to catch them early, write a simple test that exercises/run your shader.

Ultraviolet has a series of phases, some of which we can run at compile time, and some (currently) have to run at runtime:

1. (Compile) Generate shader AST from Scala code
2. (Compile) General program validation (e.g. forward reference checks)
3. (Runtime) Target specific validation (e.g. specific rules for targeting shadertoy)
4. (Runtime) Print the output

Compile time errors are the most common type of errors if you're using a standard output target like `.toGLSL[WebGL1]`/`.toGLSL[WebGL1]`.

If you are getting compile time validation errors and you want to see the output anyway, you can disable it with `.toGLSL[WebGL1](useValidation = false)`.

Runtime print errors are unusual, but the ones to look out for are from the **target specific validation phase**. An example of this kind of error would be that you've targeted shadertoy, but you haven't included a `mainImage` function, which is a requirement. There will be a runtime error to that effect, and the best why to find that ahead of time is just to write a simple test that forces the code to be evaluated. It could be as simple as:

`assert(myShaderToy.toGLSL[ShaderToy].toOutput.code.nonEmpty)`

### Did you accidentally use a Double?

Ultraviolet / GLSL only supports `Float`s! Scala is very good at massaging Floats and Doubles, and so you don't always get helpful warnings when you accidentally use a double. Long story short: If you see an mysterious errors about constructors, you probably just wrote `1.0` instead of `1.0f`.

### Strings? Where we're going, we don't need Strings.

GLSL is a C-like language for doing maths. There are no `Char` or `String` types.

### No functions as return types

Functions are not first class citizens in GLSL, and so it is not possible (currently) to have a function as a return type of a function. Simple function composition does work, and the `Shader` type forms a monad you can `map` and `flatMap` over.

### Limited support for product types

You cannot make or use arbitrary Product types. For example, it is tempting to just make a little tuple in order to return two values from a function... but you can't.

The closest thing you can do is make use of 'structs', which in Ultraviolet are represented by classes declared in the shader body - but it's usefulness is limited.

```scala
inline def fragment =
  Shader[Unit, Unit] { _ =>
    class Light(
        val eyePosOrDir: vec3,
        val isDirectional: Boolean,
        val intensity: vec3,
        val attenuation: Float
    )

    def makeLight(): Light =
      Light(vec3(1.0f), true, vec3(2.0f), 2.5f)

    def frag: Unit =
      val x = makeLight()
      val y = x.eyePosOrDir.y
  }
```

### No sum types

There is no way to represent anything like an `enum`, the closest you can get is using an `int` as a flag to switch on in a pattern match.

### No forward referencing

In Scala, you can call functions at the bottom of a program from code living at the top. This type of arrangement is called a forward reference, and is not allowed in GLSL.

There are compile time validation checks for this.

### No, your fancy library won't work here

Almost every language feature you have available via UltraViolet has required work to allow it to be converted to GLSL. Bringing in your favorite library that adds arbitrary functionality will not work.

### Nested functions and function purity

Because functions in Scala are first-class citizens, you can do all sorts of fancy things with them that we take for granted as Scala developers. One such thing is being able to arbitrarily nest functions.

In GLSL, functions are special, and can only exist at the top level of the program.

In general, this is manageable problem, but there are two rules to follow:

1. **'Named' functions e.g. `def foo(x: Float): vec2 = ???` _cannot_ be nested inside one another.** This is because Ultraviolet will preserve the order of your code including named functions, in order to avoid problems with forward references.
2. **Anonymous functions _can_ be nested, but _must be pure_.**. Ultraviolet will re-organise anonymous functions, this is what allows us to simulate things like function composition. The price is that anonymous functions must be pure, i.e. they can only produce a value based on their arguments, and cannot make reference to other outside entities that Scala would normally consider to be 'in scope'.

These rules should be enforced by compile time program validation for you.

### Just write a glsl as a String?

This is completely valid but only if it's the only contents of the block:

```scala
Shader {
  "int foo = 10;"
}
```

This is fine anywhere:


```scala
Shader {
  RawGLSL("int foo = 10;")
  // or
  raw("int bar = 11;")
}
```

But this will not work:

```scala
Shader {
  raw("int foo = 10;").trim
}
```

Because we can't do string-y things in GLSL, and trim is a string operation.

### Pattern matching weirdness

A pattern match is converted to a switch statement, and in GLSL you can only switch on an Int. So far that's limiting, but ok.

What is totally unintuitive is that on some graphics hardware, in some implementations, switch statements will process ***all*** branches irrespective of whether they're going to be used or not.

The problem with that, is that if you declare the same variable name in two branches, the GLSL compiler (***IN THE BROWSER AT RUNTIME***) will fail and tell you that you've redeclared it. Bonkers, I know, but the lesson is: Don't repeat variable names in pattern match branches...

### Unofficial reserved words

When writing shaders in Scala, Scala reserved words will be checked and errors shown by the compiler.

You shouldn't have too much trouble with GLSL reserved words because many of them have the same status in Scala, and Ultraviolets validation should catch all the others at compile time.

Naming conventions to avoid:

- Do not call a function something like `def xy(v: vec4): ???` because this will likely interfere with the Swizzle mechanisms (e.g. `vec3(1.0f).yx`). Not at the point of definition but at the point of use.
- Do not name anything `val0...N` or `def0...N`, as this is the naming scheme UltraViolet uses internally when it needs to create identifiers, and you'll end up in a mess. The `val` and `def` prefixes where picked in the hope that Scala people would naturally avoid them!
