import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

/** ## How to set up a minimal fragment shader
  */
@JSExportTopLevel("IndigoGame")
object Minimal extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]             = Assets.assets.assetSet
  val channel0: Option[AssetPath]        = None
  val channel1: Option[AssetPath]        = None
  val channel2: Option[AssetPath]        = None
  val channel3: Option[AssetPath]        = None
  val uniformBlocks: Batch[UniformBlock] = Batch.empty

  val shader: ShaderProgram =
    CustomShader.shader

/** Inside a custom shader object, we define an ultraviolet shader using the `entityFragment` helper
  * method, since we're not interested in using a vertex shader at this time. To do that, we need to
  * supply a ShaderId, that we'll need to register in Indigo's boot data, and also the fragment
  * shader itself.
  *
  * We import the ultraviolet syntax inside the object in order to avoid import collisions.
  *
  * The fragment shader is a simple function that takes a color and returns a new color. In this
  * case, we're returning the colour red, by setting the red and alpha to 'full', i.e.:
  *
  * `vec4(red = 1.0f, green == 0.0f, blue = 0.0f, alpha = 1.0f)`
  */
// ```scala
object CustomShader:

  val shader: ShaderProgram =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      def fragment(color: vec4): vec4 =
        vec4(1.0f, 0.0f, 0.0f, 1.0f)
    }
// ```
