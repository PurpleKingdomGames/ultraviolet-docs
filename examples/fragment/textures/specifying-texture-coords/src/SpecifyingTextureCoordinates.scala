import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

/** ## Stretching a texture to fill the entity.
  *
  * In this example we're going to mimic Indigo's 'stretch' `FillType`, to tell the shader to
  * stretch the texture to fill the entity (or in our case, the available space), by telling the
  * shader how to map the entities UV's onto the texture.
  *
  * Fundamentally, we're just going to look at using the `texture2D` function in a shader, and if
  * you are using Ultraviolet outside of Indigo, that's all you really need.
  *
  * Since we are in Indigo though, things are slightly more complex because we are required to know
  * a bit about how Indigo works with textures.
  *
  * ### How Indigo stores texture information
  *
  * Briefly: Indigo stores textures on a texture atlas. Which means that many texture are all stored
  * in the same image / texture buffer, which can then be accessed repeatedly without requiring a
  * context change by the graphics card.
  *
  * Indigo can have many atlases, which are limited to 4096x4096 pixels in size, but in pixel art
  * terms that's a lot of space! It's common for whole games to easily fit in one atlas.
  *
  * What does that mean in practice? Well it means that when you want to make use of a texture in
  * the `SRC_CHANNEL` - the enourmous texture atlas - you need to know where in that atlas your
  * texture is, and how big it is. For channel 0, those values are `CHANNEL_0_POSITION` and
  * `CHANNEL_0_SIZE`, respectively.
  *
  * ### Using `texture2D`
  *
  * > Trivia: The function `texture2D()` comes from WebGL 1.0, in WebGL 2.0 it's just called
  * `texture()`, but it was convenient to keep the old terminology to help disambiguate terms, and
  * Ultraviolet translates it to the right name for you based on the compilation target.
  */
@JSExportTopLevel("IndigoGame")
object SpecifyingTextureCoordinates extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType] = Assets.assets.assetSet

  /** As usual, we need to first assign a texture to a channel.
    */
  // ```scala
  val channel0: Option[AssetPath] = Option(AssetPath("assets/fire-background.png"))
  // ```
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
      /** Now we can render our stretched texture.
        */
      def fragment(color: vec4): vec4 =
        /** First we need to calculate the stretched UV coordinates. We do this by taking the
          * entity's UV value passed over by the vertex shader; multiply it by the texture size so
          * that we have translated from UV space (0.0 to 1.0) to pixels; then add the position of
          * the texture in the atlas to move to the correct location.
          */
        // ```scala
        val coords = env.CHANNEL_0_POSITION + (env.UV * env.CHANNEL_0_SIZE)
        // ```

        /** Finally, we can sample the texture at the calculated coordinates. `texture2D` returns a
          * `vec4` of the color of the texture at the given coordinates.
          */
        // ```scala
        texture2D(env.SRC_CHANNEL, coords)
        // ```
    }
