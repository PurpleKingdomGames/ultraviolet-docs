import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object ColourInterpolators extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = None
  val channel1: Option[AssetPath] = None
  val channel2: Option[AssetPath] = None
  val channel3: Option[AssetPath] = None

  val shader: Shader =
    CustomShader.shader

object CustomShader:

  val shader: Shader =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  import ultraviolet.syntax.*

  /** In this example, we extract a vec3 of red (1, 0, 0) and blue (0, 0, 1) using a 'swizzle' (the
    * `.xyz`) and add them together to create magenta (1, 0, 1).
    */
  // ```scala
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      import ultraviolet.colors.*

      def fragment(color: vec4): vec4 =
        val magenta = Red.xyz + Blue.xyz
        vec4(magenta, 1.0f)
    }
  // ```
