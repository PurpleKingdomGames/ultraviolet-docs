package shadertoy

import indigo.*

import generated.*
import ultraviolet.predef.shadertoy.*
import ultraviolet.syntax.*

final class Default() extends Game[Unit, Unit, Unit]:

  val gameId: GameId =
    GameId("game")

  def scenes(bootData: Unit): NonEmptyBatch[Scene[Unit, Unit]] =
    NonEmptyBatch(Scene.empty)

  def initialScene(bootData: Unit): Option[SceneName] =
    None

  def eventFilters: EventFilters =
    EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[Unit, Unit]] =
    Outcome(BootResult.noData(Config.config))

  def setup(bootData: Unit, assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[Unit] =
    Outcome(())

  def updateModel(context: Context, model: Unit): GlobalEvent => Outcome[Unit] =
    _ => Outcome(model)

  def present(context: Context, model: Unit): Outcome[SceneUpdateFragment] =
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
    ShaderToyExample.image.toGLSL(ShaderToyProgram).code
// ```
