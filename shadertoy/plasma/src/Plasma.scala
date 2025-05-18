import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.shadertoy.*
import ultraviolet.syntax.*

@JSExportTopLevel("IndigoGame")
object Plasma extends IndigoSandbox[Unit, Unit]:

  val config: GameConfig          = Config.config.noResize
  val assets: Set[AssetType]      = Set()
  val fonts: Set[FontInfo]        = Set()
  val animations: Set[Animation]  = Set()
  val shaders: Set[ShaderProgram] = Set()

  def setup(assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[Unit] =
    Outcome(())

  def updateModel(context: Context[Unit], model: Unit): GlobalEvent => Outcome[Unit] =
    _ => Outcome(model)

  def present(context: Context[Unit], model: Unit): Outcome[SceneUpdateFragment] =
    Outcome(SceneUpdateFragment.empty)

/** ## Ultraviolet Shadertoy code
  *
  * Reminder: The live demo will not work, below is the Shadertoy compatible code.
  *
  * Unlike the default example, this example produces two shaders:
  *   1. A shader that produces a buffer (bufferA)
  *   2. A shader that produces the final image (image)
  *
  * If you were to render Bufffer A, you'd see that it produces a noise image, which is then used as
  * a reference source in order to produce the final plasma effect.
  */
// ```scala
object ShaderToyExample:

  inline def bufferA =
    Shader[ShaderToyEnv, Unit] { env =>
      @const val pi: Float = 3.1415926435f

      def mainImage(fragColor: vec4, fragCoord: vec2): vec4 =
        val i: Float = fragCoord.x / env.iResolution.x
        val t: vec3  = (env.iTime + env.iMouse.y) / vec3(63.0f, 78.0f, 45.0f)
        val cs: vec3 = cos(i * pi * 2.0f + vec3(0.0f, 1.0f, -0.5f) * pi + t)

        vec4(0.5f + 0.5f * cs, 1.0f)
    }

  inline def image =
    Shader[ShaderToyEnv, Unit] { env =>
      @const val vp: vec2 = vec2(320.0, 200.0)

      def mainImage(fragColor: vec4, fragCoord: vec2): vec4 =
        val t: Float  = env.iTime * 10.0f + env.iMouse.x
        val uv: vec2  = fragCoord.xy / env.iResolution.xy
        val p0: vec2  = (uv - 0.5f) * vp
        val hvp: vec2 = vp * 0.5f
        val p1d: vec2 = vec2(cos(t / 98.0f), sin(t / 178.0f)) * hvp - p0
        val p2d: vec2 = vec2(sin(-t / 124.0f), cos(-t / 104.0f)) * hvp - p0
        val p3d: vec2 = vec2(cos(-t / 165.0f), cos(t / 45.0f)) * hvp - p0
        val sum: Float = 0.25f + 0.5f * (cos(length(p1d) / 30.0f) +
          cos(length(p2d) / 20.0f) +
          sin(length(p3d) / 25.0f) * sin(p3d.x / 20.0f) * sin(p3d.y / 15.0f))

        texture2D(env.iChannel0, vec2(fract(sum), 0))
    }
// ```

/** Then to get the code for the two shaders as a String so that we might print or output them
  * somewhere, we can do the following:
  */
// ```scala
object Output:

  val bufferACode: String =
    ShaderToyExample.bufferA.toGLSL[ShaderToy].toOutput.code

  val imageCode: String =
    ShaderToyExample.image.toGLSL[ShaderToy].toOutput.code
// ```
