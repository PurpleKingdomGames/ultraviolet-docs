import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.shadertoy.*
import ultraviolet.syntax.*

@JSExportTopLevel("IndigoGame")
object Default extends IndigoSandbox[Unit, Unit]:

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
  * Note the custom `ShaderToyEnv` that is passed to the shader. This is a custom environment that
  * contains read-only information like the running time, mouse position, resolution, which are
  * supplied by shadertoy.
  */
// ```scala
object ShaderToyExample:

  inline def image =
    Shader[ShaderToyEnv, Unit] { env =>
      def mainImage(fragColor: vec4, fragCoord: vec2): vec4 = {
        // Normalized pixel coordinates (from 0 to 1)
        val uv: vec2 = fragCoord / env.iResolution.xy

        // Time varying pixel color
        val col: vec3 = 0.5f + 0.5f * cos(env.iTime + uv.xyx + vec3(0.0f, 2.0f, 4.0f))

        // Output to screen
        vec4(col, 1.0f)
      }
    }
// ```

/** Then to get the code as a String so that we might print it or output it to a file or whatever,
  * we can do the following:
  */
// ```scala
object Output:

  val imageCode: String =
    ShaderToyExample.image.toGLSL[ShaderToy].toOutput.code
// ```
