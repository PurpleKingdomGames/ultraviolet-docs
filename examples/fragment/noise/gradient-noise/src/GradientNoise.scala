import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object GradientNoise extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = None
  val channel1: Option[AssetPath] = None
  val channel2: Option[AssetPath] = None
  val channel3: Option[AssetPath] = None

  val shader: Shader =
    GradientNoiseShader.shader

object GradientNoiseShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("gradient noise shader"),
      EntityShader.fragment(fragment, FragmentEnv.reference)
    )

  import ultraviolet.syntax.*

  /** The noise function is imported, but we then need to set up a proxy function. This is because the
    * macro system relies on inlining, and we want to create a function definition, not just inline
    * the noise code at the point of use.
    */
  // ```scala
  inline def fragment =
    Shader[FragmentEnv] { env =>
      import ultraviolet.noise.*

      def proxy: vec2 => vec3 =
        p => gradient(p)

      def fragment(color: vec4): vec4 =
        vec4(proxy(env.UV * 8.0f), 1.0f)
    }
  // ```