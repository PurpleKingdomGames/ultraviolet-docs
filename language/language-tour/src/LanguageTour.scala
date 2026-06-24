package examples

import indigo.*

import generated.*
import ultraviolet.syntax.*

import scala.annotation.nowarn

final class LanguageTour() extends Game.ShaderPlayground:

  val gameId: GameId =
    GameId("game")

  val config: EngineConfig =
    Config.config

  val assets: Set[AssetType]             = Assets.assets.assetSetRelative
  val channel0: Option[AssetPath]        = None
  val channel1: Option[AssetPath]        = None
  val channel2: Option[AssetPath]        = None
  val channel3: Option[AssetPath]        = None
  val uniformBlocks: Batch[UniformBlock] = Batch.empty

  val shader: ShaderProgram =
    CustomShader.shader

object CustomShader:

  val shader: ShaderProgram =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  @nowarn
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      /** ## Primitive types */

      /** Shaders are numeric calculations, so all of the primitives are arrays of floats in
        * disguise.
        */
      // ```scala
      vec2(1.0f, 1.0f)
      vec3(1.0f, 1.0f, 1.0f)
      vec4(1.0f, 1.0f, 1.0f, 1.0f)
      mat2(1.0f, 1.0f, 1.0f, 1.0f)
      mat3(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
      mat4(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
        1.0f)
      array[2, Float](1.0f, 1.0f)
      // ```
      /** All of the types work with all of the opperations, so you can mod a vec2 if you like /
        * need. They are all pretty adaptable and come with very expressive constructors.
        */
      // ```scala
      vec2(1.0f, 1.0f)
      vec2(1.0f)
      vec3(1.0f, 1.0f, 1.0f)
      vec4(vec3(1.0f), 1.0f)
      vec4(1.0f)
      // ```

      /** ## Variables */

      /** Almost everything is a Float, and there is poor support for automative numeric conversion.
        * If you're seeing errors, you probably forgot to add the `f` to `10.0f`.
        */
      // ```scala
      val someValue: Float = 1.0f
      // ```

      /** `val`s work as normal, and continue to enjoy type inference.
        */
      // ```scala
      val position: vec2 = vec2(1.0f, 2.0f)
      val color          = vec4(1.0f, 2.0f, 3.0f, 4.0f)
      // ```

      /** You will need an occasional `var` too, there is only so far you can go with functional
        * programming here.
        */
      // ```scala
      var c: Float = 0.0f
      c = 4.0f
      // ```

      /** ## Functions */

      /** Functions can be declared in two ways, and they behave differently, so beware.
        *
        * You can declare a `def`, and ultraviolet will place it in the same relative position that
        * you did.
        *
        * **Rules: You cannot nest def's, and you cannot use name shadowing.**
        */
      // ```scala
      def addOne(i: Int): Int = i + 1
      // ```

      /** You can also declare functions as vals. This concept is not natively supported in GLSL,
        * and to make it work, Ultraviolet _assumes_ that functions declared this way are _pure_,
        * i.e. they do not refer, for example to any variables declared at the same level.
        *
        * This is important, because at compile time the body of this functions is moved to the top
        * of the output code. The avoids numerous problems, but if you have refered to something you
        * shouldn't have, you'll get a confusing 'forward reference' error.
        *
        * **Rules: Functions declared as vals must be pure.**
        */
      // ```scala
      val addTwo: Int => Int   = i => i + 2
      def addThree: Int => Int = i => i + 3
      // ```

      /** Unary functions can be composed with `compose` and `andThen`, as normal
        */
      // ```scala
      val e: Float => Float = v => v - 0.5f
      val f: Float => vec3  = r => vec3(r, 0.0f, 0.0f)
      val g: vec3 => vec4   = val3 => vec4(val3, 0.5f)
      val h: Float => vec4  = g compose f compose e

      h(1.0f)
      // ```

      /** ## If statements */
      // ```scala
      val red    = vec4(1.0, 0.0, 0.0, 1.0)
      val green  = vec4(0.0, 1.0, 0.0, 1.0)
      val blue   = vec4(0.0, 0.0, 1.0, 1.0)
      val w: Int = 1

      if w <= 0 then red
      else {
        val y = 10
        if w == 1 && y == 10 then blue
        else green
      }
      // ```

      /** ## Loops */

      /** Loops can either be while loops...
        */
      // ```scala
      var i = 0.0f
      while i < 4.0f do i += 1.0f
      // ```

      /** ...or for loops.
        */
      // ```scala
      val steps = 10
      cfor(0, _ < steps, _ + 1) { _ =>
        i += 1.0f
      }
      // ```

      /** ## Pattern matching / Switch statements */

      /** **Rules: Yes, there is pattern matching BUT it is converted to switch statements, and GLSL
        * isn't terrible clever about what it can switch on. Keep it simple!**
        */
      // ```scala
      val flag: Int = 2

      var res: Int = -1

      flag match
        case 0 => res = 10
        case 1 => res = 20
        case 2 => res = 30
        case _ => res = -100

      res
      // ```

      /** ## Casting */

      // ```scala
      val x  = 1.0f.toInt // Cast is inlined on literal
      val y  = 1.toFloat  // Cast is inlined on literal
      val z  = x.toFloat
      val zz = y.toInt
      val w1 = 2
      val w2 = (1 + w1).toFloat
      x + y
      // ```

      /** ## Swizzling */

      /** Swizzling is very important in shader programming. It allows you to access and reorganise
        * component values quickly as needed.
        *
        * A common example is that you are given an RGBA color, but you only want the rgb values.
        */
      // ```scala
      vec4(1.0f, 2.0f, 3.0f, 4.0f).wzyx
      vec3(1.0f, 2.0f, 3.0f).xxy

      val fill = vec3(1.0f, 2.0f, 3.0f)
      fill.zyx
      fill.x
      fill.yyy
      fill.yz

      vec4(1.0f, 2.0f, 3.0f, 4.0f).abgr
      // ```

      /** ## Structs */

      /** You can delare a struct with a normal class.
        */
      // ```scala
      class Light(
          val eyePosOrDir: vec3,
          val isDirectional: Boolean,
          val intensity: vec3,
          val attenuation: Float
      )
      // ```

      /** ## Arrays */

      // ```scala
      val arr: array[4, Float] = array[4, Float](0.0f, 2.0f, 3.0f, 4.0f)
      val valueAtOne           = arr(1)
      // ```

      /** ## Accessing the Environment */

      /** The environment comes loaded with lots of useful data points. If are animating your
        * shaders, for example, your environment might include the time:
        */
      // ```scala
      val t = env.TIME
      // ```
      /** Environments in general are customisable, however if you're using Indigo or Shadertoy,
        * there are premade environments for them that you can use or augment.
        */

      /** ## GLSL Annotations */

      /** Sometimes you need to annotate to give GLSL / UV more information.
        */
      // ```scala
      @flat @in var a1: vec2       = null
      @smooth @out val b1: vec4    = null
      @global var c1: vec2         = null
      @layout(7) @in val d1: Float = 0.0;
      @const var e1: vec2          = null
      @uniform var f1: Float       = 0.0f
      // ```

      /** ## Common Shader Operations and Values */

      /** Ultraviolet supports a comprehensive full suite of shader operations, and while they use
        * the real GPU implementation in your shaders, most of them also have Scala implementations
        * to help you with testing.
        *
        * Here are a few:
        */
      // ```scala
      sin(1.0f)
      mix(1.0f, 2.0f, 0.6f)
      step(0.5f, 0.1f)
      mod(1.0f, 10.0f)
      // ```

      /** ## Embedding Raw GLSL */

      /** You can embed RAW GLSL if you need to. It is up to you to ensure it is corect, no compiler
        * support!
        */
      // ```scala
      RawGLSL("float v = 1.0;")
      raw("COLOR = vec4(v, v, v, 0.5);")
      RawGLSL(
        """
//#vertex_start
vec4 vertex(vec4 v){
return v;
}
//#vertex_end
        """
      )
      // ```

      def fragment(color: vec4): vec4 =
        vec4(1.0f)
    }
