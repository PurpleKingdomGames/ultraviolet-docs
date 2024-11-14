import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

/** ## How to composite one texture over another
  */
@JSExportTopLevel("IndigoGame")
object BlendingTextures extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType] = Assets.assets.assetSet

  /** First we need to assign our two textures to two channels.
    *
    * The first texture is a solid background, but the second is some foreground layer with
    * transparency, i.e. an alpha value that varies between 0.0 and 1.0.
    */
  // ```scala
  val channel0: Option[AssetPath] = Option(AssetPath("assets/fire-background.png"))
  val channel1: Option[AssetPath] = Option(AssetPath("assets/campfire.png"))
  // ```
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

  /** We can access the color of each texture by using the channel reference.
    *
    * The `mix` function is then used to blend the two textures together, putting the foreground on
    * top of the background, based on the alpha value of the foreground.
    *
    * All quite straightforward, but what is this `mix` function? `mix` is a GLSL term that is also
    * known as lerp, or linear interpolation. The arguments are: a) The value to interpolated from,
    * b) The value to interpolate to, c) The interpolation factor.
    *
    * In this example we've used `mix` to blend the two textures together based on the alpha value
    * of the foreground texture, for example when the alpha value is 0.0 the result will be the
    * background texture, when the alpha value is 1.0 the result will be the foreground texture.
    *
    * It is important to note that `mix` is just a mathematical function that will work with many
    * numeric and vector types, we're using it for textures here, but you can use it to interpolate
    * between any values you like.
    */

  // ```scala
  inline def fragment: Shader[FragmentEnv, Unit] =
  Shader[FragmentEnv] { env =>
    def fragment(color: vec4): vec4 =
      val background: vec4 = env.CHANNEL_0
      val foreground: vec4 = env.CHANNEL_1

      mix(background, foreground, foreground.a)
  }
  // ```
