import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object SimplexNoise extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = None
  val channel1: Option[AssetPath] = None
  val channel2: Option[AssetPath] = None
  val channel3: Option[AssetPath] = None

  val shader: Shader =
    SimplexNoiseShader.shader

object SimplexNoiseShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("simplex noise shader"),
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

      def proxy: vec2 => Float =
        pt => simplex(pt)

      def fragment(color: vec4): vec4 =
        val n = (proxy(env.UV) * 0.25f) +
          (proxy(env.UV * 2.0f) * 0.25f) +
          (proxy(env.UV * 8.0f) * 0.25f) +
          (proxy(env.UV * 32.0f) * 0.25f)

        vec4(vec3(n), 1.0f)
    }
  // ```