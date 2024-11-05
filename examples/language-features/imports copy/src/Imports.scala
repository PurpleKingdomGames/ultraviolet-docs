import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

/** ## How to import shared code
  */

/** ### Shared code
  *
  * First we'll declare some contrived code that we're pretending we'd like to use in multiple
  * shaders.
  *
  * There are some important things to note about this small section of code.
  *
  *   1. We've imported `ultraviolet.syntax.*`. Recall that, unfortunately, both Indigo and
  *      Ultraviolet both have types called things like `vec2`, `vec4`, etc. This import is to
  *      ensure that we're using the Ultraviolet types. In this specific example the import is done
  *      inside the object because indigo is imported at the top, but if the code was in a separate
  *      class then you could just put the import at the top as usual.
  *
  * 2. All the fields and functions are declared as `inline`. This is because Ultraviolet is
  * implemented with inline macros, and if the code isn't inlined, then Ultraviolet can't see it.
  *
  * 3. `redAmount` looks like it should be a `val`... and indeed normally it would be. However,
  * `val` declarations do not work, so we have to use `inline def` instead.
  */
// ```scala
object SharedCode:
  import ultraviolet.syntax.*

  inline def redAmount: Float       = 0.5
  inline def grabX(uv: vec2): Float = uv.x
// ```

@JSExportTopLevel("IndigoGame")
object Imports extends IndigoShader:

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

  /** Within our custom shader object, we can import the shared code and use it in our shader, but
    * we can't do things like `SharedCode.redAmount` at the present time.
    */
  // ```scala
  import SharedCode.* // This is okay.
  // SharedCode.redAmount // This does not work.
  // ```

  val shader: Shader =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  import ultraviolet.syntax.*

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      /** Another unfortunate limitation is that we cannot use our grabX function directly. The code
        * will compile if you try to use it, but your shader will be black, and if you check your
        * browser console you'll see a mysterious error, like this:
        *
        * `[Indigo] ERROR: 0:72: 'constructor' : too many arguments`
        *
        * In order to use the grabX function, we need to assign it to a proxy/delegate function
        * within the scope of the shader.
        */
      // ```scala
      val proxyGrabX: vec2 => Float = uv => grabX(uv)
      // ```

      /** Finally, we can see the shared code in use.
        */
      // ```scala
      def fragment(color: vec4): vec4 =
        vec4(redAmount, proxyGrabX(env.UV), 0.0f, 1.0f)
      // ```
    }
