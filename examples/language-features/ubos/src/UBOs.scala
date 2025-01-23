import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*


/** ## How to use Uniform Buffer Objects (UBOs) to supply data to a shader
  */
@JSExportTopLevel("IndigoGame")
object UBOs extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = None
  val channel1: Option[AssetPath] = None
  val channel2: Option[AssetPath] = None
  val channel3: Option[AssetPath] = None

  val uniformBlocks: Batch[UniformBlock] =
    Batch(
      CustomData(vec4(1.0f, 0.0f, 1.0f, 1.0f))
    )

  val shader: ShaderProgram =
    CustomShader.shader

/** */
// ```scala
object CustomShader:

  class Env extends CustomData(vec4(0.0f)) with FragmentEnvReference

  val shader: ShaderProgram =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[Env](fragment, new Env)
    )

  inline def fragment: Shader[Env, Unit] =
    Shader[Env] { env =>
      ubo[CustomData]

      def fragment(color: vec4): vec4 =
        env.CUSTOM_COLOR
    }
// ```

case class CustomData(CUSTOM_COLOR: vec4) derives ToUniformBlock