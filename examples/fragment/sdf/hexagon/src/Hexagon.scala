import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object Hexagon extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = None
  val channel1: Option[AssetPath] = None
  val channel2: Option[AssetPath] = None
  val channel3: Option[AssetPath] = None

  val shader: Shader =
    HexagonShader.shader

object HexagonShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("hexagon shader"),
      EntityShader.fragment(fragment, FragmentEnv.reference)
    )

  import ultraviolet.syntax.*

  /** The sdf function is imported, but we then need to set up a proxy function. This is because the
    * macro system relies on inlining, and we want to create a function definition, not just inline
    * the body at the point of use.
    * 
    * This shader also uses an imported 'fill' function to calculate a nice colour gradient.
    */
  // ```scala
  inline def fragment =
    Shader[FragmentEnv] { env =>
      import ultraviolet.sdf.*
      import FillColorHelper.*

      def proxy: (vec2, Float) => Float =
        (p, r) => hexagon(p, r)

      def calculateColour: (vec2, Float) => vec4 = (uv, sdf) => fill(uv, sdf)

      def fragment(color: vec4): vec4 =
        calculateColour(env.UV, proxy(env.UV - 0.5f, 0.4f))
    }
  // ```

object FillColorHelper:

  import ultraviolet.syntax.*
  import ultraviolet.colors.*

  inline def fill(uv: vec2, sdf: Float): vec4 =
    val fill       = vec4(uv, Blue.z, 1.0f)
    val fillAmount = (1.0f - step(0.0f, sdf)) * fill.w
    vec4(fill.xyz * fillAmount, fillAmount)