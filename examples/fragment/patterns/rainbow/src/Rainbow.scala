import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

@JSExportTopLevel("IndigoGame")
object Rainbow extends IndigoShader:

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

      def rainbow(p: Float): vec4 =
        val interval = 1.0f / 7.0f

        val redAmount    = 1.0f - step(interval * 1.0f, p)
        val orangeAmount = 1.0f - step(interval * 2.0f, p)
        val yellowAmount = 1.0f - step(interval * 3.0f, p)
        val greenAmount  = 1.0f - step(interval * 4.0f, p)
        val blueAmount   = 1.0f - step(interval * 5.0f, p)
        val cyanAmount   = 1.0f - step(interval * 6.0f, p)

        val red    = vec4(1.0f, 0.0f, 0.0f, 1.0f)
        val orange = vec4(1.0f, 0.5f, 0.0f, 1.0f)
        val yellow = vec4(1.0f, 1.0f, 0.0f, 1.0f)
        val green  = vec4(0.0f, 1.0f, 0.0f, 1.0f)
        val blue   = vec4(0.0f, 0.0f, 1.0f, 1.0f)
        val cyan   = vec4(0.0f, 1.0f, 1.0f, 1.0f)
        val purple = vec4(0.75f, 0.0f, 1.0f, 1.0f)

        mix(
          purple,
          mix(
            cyan,
            mix(
              blue,
              mix(
                green,
                mix(
                  yellow,
                  mix(orange, red, redAmount),
                  orangeAmount
                ),
                yellowAmount
              ),
              greenAmount
            ),
            blueAmount
          ),
          cyanAmount
        )

      def fragment(color: vec4): vec4 =
        val col = rainbow(env.UV.x)
        vec4(0.3f) + (col * 0.7f)

    }
