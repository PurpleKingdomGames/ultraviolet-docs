import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

@JSExportTopLevel("IndigoGame")
object WavyStripes extends IndigoShader:

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

      def fragment(color: vec4): vec4 =
        val offset: Float = 0.25f + (sin(env.UV.x * env.SIZE.x / 8.0f) * 0.01f)
        val yPos: Float   = env.UV.y + offset
        val amount: Float = step(0.5f, 0.5f + (sin(yPos * env.SIZE.x / 4.0f) * 0.5f))
        val c: vec3       = vec3(1.0f, 0.75f, 0.4f) * (0.8f + amount * 0.2f)

        vec4(c, 1.0f)

    }
