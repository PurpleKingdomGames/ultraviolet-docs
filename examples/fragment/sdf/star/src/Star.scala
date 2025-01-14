import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object Star extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = None
  val channel1: Option[AssetPath] = None
  val channel2: Option[AssetPath] = None
  val channel3: Option[AssetPath] = None

  val shader: Shader =
    StarShader.shader

object StarShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("star shader"),
      EntityShader.fragment(fragment, FragmentEnv.reference)
    )

  import ultraviolet.syntax.*

  inline def fragment =
    Shader[FragmentEnv] { env =>
      import ultraviolet.sdf.*
      import FillColorHelper.*

      def proxy: (vec2, Float, Float) => Float =
        (p, rt, r) => star(p, rt, r)

      def calculateColour: (vec2, Float) => vec4 = (uv, sdf) => fill(uv, sdf)

      def fragment(color: vec4): vec4 =
        calculateColour(env.UV, proxy(env.UV - 0.5f, 0.3f, 0.6f))
    }

object FillColorHelper:

  import ultraviolet.syntax.*
  import ultraviolet.colors.*

  inline def fill(uv: vec2, sdf: Float): vec4 =
    val fill       = vec4(uv, Blue.z, 1.0f)
    val fillAmount = (1.0f - step(0.0f, sdf)) * fill.w
    vec4(fill.xyz * fillAmount, fillAmount)
