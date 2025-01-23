import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

@JSExportTopLevel("IndigoGame")
object ColourInterpolators extends IndigoShader:

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

  /** In this example, interpolate a hex colour value to a vec3 (hexa would produce a vec4), and a
    * blue color as a vec4 using `rgba` (rgb would produce a vec3). The blue colour is then
    * converted to a vec3 with a swizzle, and the two are added together to create magenta.
    */
  // ```scala
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      def fragment(color: vec4): vec4 =
        val red     = hex"#FF0000"
        val blue    = rgba"0,0,255,255"
        val magenta = red.xyz + blue.xyz

        vec4(magenta, 1.0f)
    }
  // ```
