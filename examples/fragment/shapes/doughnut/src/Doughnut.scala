import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

@JSExportTopLevel("IndigoGame")
object Doughnut extends IndigoShader:

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

object CustomShader:

  val shader: ShaderProgram =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      def heightValue(d: Float, m: Float): Float =
        ((cos(d * env.PI * m) * 0.5f) + 0.5f) * min(step(-1.0f / m, d), 1.0f - step(1.0f / m, d))

      def sdCircle(p: vec2, r: Float): Float =
        length(p) - r

      def fragment(color: vec4): vec4 =

        val sdf    = sdCircle(env.UV - 0.5f, 0.25f)
        val height = heightValue(sdf, 10.0f)

        vec4(vec3(height), 1.0f)
    }
