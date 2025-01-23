import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

/** ## How to directly use a texture
  */
@JSExportTopLevel("IndigoGame")
object BasicTextureUse extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType] = Assets.assets.assetSet

  /** To use a texture, we need to assign it to a channel. The incantation to do this varies
    * slightly between this example, a simple `IndigoShader` and using it in a full Indigo game
    * where the channel must be assigned to the `ShaderData` referencing the `AssetName`.
    */
  // ```scala
  val channel0: Option[AssetPath] = Option(AssetPath("assets/fire-background.png"))
  // ```
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

  /** In the simplest use of an image/texture, you just need to use of the appropriate channel, in
    * this case `env.CHANNEL_0`. This type of channel reference provides the color of the pixel at
    * the current UV coordinate.
    *
    * Where did the UV coordinate come from? It is an interpolated value that is passed to the
    * fragment shader from the vertex shader. UV Coordinates in Indigo are in the range 0.0 to 1.0
    * from top-left to bottom-right.
    */
  // ```scala
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      def fragment(color: vec4): vec4 =
        env.CHANNEL_0
    }
  // ```
