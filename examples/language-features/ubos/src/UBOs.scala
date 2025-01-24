import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

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

  val shader: Shader =
    CustomShader.Shader
    // TODO: I'm stuck until I can publish the next version of indigo...

/** 
  */
// ```scala
object CustomShader:

  val shader: Shader =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  import ultraviolet.syntax.*

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      def fragment(color: vec4): vec4 =
        vec4(1.0f, 0.0f, 0.0f, 1.0f)
    }
// ```
