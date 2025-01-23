import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

@JSExportTopLevel("IndigoGame")
object Segment extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]             = Assets.assets.assetSet
  val channel0: Option[AssetPath]        = None
  val channel1: Option[AssetPath]        = None
  val channel2: Option[AssetPath]        = None
  val channel3: Option[AssetPath]        = None
  val uniformBlocks: Batch[UniformBlock] = Batch.empty

  val shader: ShaderProgram =
    SegmentShader.shader

object SegmentShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("segment shader"),
      EntityShader.fragment(fragment, FragmentEnv.reference)
    )

  /** The sdf function is imported, but we then need to set up a proxy function. This is because the
    * macro system relies on inlining, and we want to create a function definition, not just inline
    * the body at the point of use.
    *
    * This shader also uses an imported 'fill' function to calculate a nice colour gradient.
    *
    * Note that we can't just render the line segment, because at 0.0f the SDF is 0.0f, so we need
    * the annular, i.e. abs(sdf) - thickness-of-border.
    */
  // ```scala
  inline def fragment =
    Shader[FragmentEnv] { env =>
      import ultraviolet.sdf.*
      import FillColorHelper.*

      def proxy: (vec2, vec2, vec2) => Float =
        (p, a, b) => segment(p, a, b)

      def calculateColour: (vec2, Float) => vec4 = (uv, sdf) => fill(uv, sdf)

      def fragment(color: vec4): vec4 =
        val segSDF = proxy(env.UV, vec2(0.2f), vec2(0.8f))
        calculateColour(env.UV, abs(segSDF) - 0.1f)
    }
  // ```

object FillColorHelper:
  import ultraviolet.colors.*

  inline def fill(uv: vec2, sdf: Float): vec4 =
    val fill       = vec4(uv, Blue.z, 1.0f)
    val fillAmount = (1.0f - step(0.0f, sdf)) * fill.w
    vec4(fill.xyz * fillAmount, fillAmount)
