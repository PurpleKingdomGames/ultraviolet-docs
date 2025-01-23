import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

@JSExportTopLevel("IndigoGame")
object CellularNoise extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]             = Assets.assets.assetSet
  val channel0: Option[AssetPath]        = None
  val channel1: Option[AssetPath]        = None
  val channel2: Option[AssetPath]        = None
  val channel3: Option[AssetPath]        = None
  val uniformBlocks: Batch[UniformBlock] = Batch.empty

  val shader: ShaderProgram =
    CellularNoiseShader.shader

object CellularNoiseShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("cellular noise shader"),
      EntityShader.fragment(fragment, FragmentEnv.reference)
    )

  /** The noise function is imported, but we then need to set up a proxy function. This is because
    * the macro system relies on inlining, and we want to create a function definition, not just
    * inline the noise code at the point of use.
    */
  // ```scala
  inline def fragment =
    Shader[FragmentEnv] { env =>
      import ultraviolet.noise.*

      def proxy: vec2 => vec2 =
        pt => cellular(pt)

      def fragment(color: vec4): vec4 =
        vec4(proxy(env.UV * 8.0f), 0.0f, 1.0f)
    }
  // ```
