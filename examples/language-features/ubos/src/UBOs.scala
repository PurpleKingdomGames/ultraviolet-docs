import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

/** ## How to use Uniform Buffer Objects (UBOs) to supply data to a shader
  *
  * The process of setting up UBOs is a little more involved than setting up a simple shader, and
  * there are a few steps to follow. It's an imperfect arrangement, but works fairly well. Hopefully
  * it can be improved in the future.
  *
  * This example is built on top of Indigo's IndigoShader entry point, which allows us to send data
  * to the shader by setting the `uniformBlocks`. Please note that the fields exposed by this entry
  * point are the fields on Indigo's `ShaderData` type, so when doing this in a real game, you'd
  * fill in the details there instead.
  */

/** ### Creating the data structure
  *
  * First we need to define a structure that will be used to define the data that will be passed to
  * the shader. In this case, it's a case class called `CustomData`.
  *
  * Technically, there is _no_ link between the case class used in the shader data, and the case
  * class used in the shader itself. However, it's a good idea to use the same one if you can. It
  * may not always be convenient as we'll see below.
  *
  * In order to use one case class to define both the structure sent and how it is defined in the
  * shader, we need a case class that is _not_ finalised, so that we can extend it later.
  *
  * Notice that the case class derives from `ToUniformBlock`. This is a type class that allows use
  * to automagically convert the case class into a `UniformBlock`. You can mannually set up uniform
  * block data if you prefer, but you'll still need a case class for the representation in the
  * shader code.
  *
  * The `CUSTOM_COLOR` field is a `vec4` that will represent the RGBA color (a common use of vec4
  * types in shaders). The field name has been capitalised since environment fields in Indigo are
  * all capitalised by convention, but it isn't necessary.
  */
// ```scala
case class CustomData(CUSTOM_COLOR: vec4) derives ToUniformBlock
// ```

@JSExportTopLevel("IndigoGame")
object UBOs extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = None
  val channel1: Option[AssetPath] = None
  val channel2: Option[AssetPath] = None
  val channel3: Option[AssetPath] = None

  /** ### Setting up the uniform block data
    *
    * We need to instantiate the data, which is no more complicated that creating an instance of
    * `CustomData`. This works, because our case class is derived from `ToUniformBlock`, and the
    * IndigoShader entry point will perform an implicit conversion for us. The same conversion is
    * applied when you add data to an Indigo `ShaderData` instance.
    *
    * Here we've set the RGBA color to magenta.
    */
  // ```scala
  val uniformBlocks: Batch[UniformBlock] =
    Batch(
      CustomData(vec4(1.0f, 0.0f, 1.0f, 1.0f))
    )
  // ```

  val shader: ShaderProgram =
    CustomShader.shader

object CustomShader:

  /** ### Accessing the values via the environment
    *
    * The `CustomData` case class is used to define the data that will be passed to the shader, but
    * we need to somehow include that in the shader's environment so that we can access the data in
    * the shader.
    *
    * The default environment for an Indigo fragment shader is `FragmentEnv`, which provides access
    * to fields like `env.TIME`. We need all the fields from `FragmentEnv`, but we also need to
    * include the fields of our `CustomData` case class.
    *
    * To do that, we make a new class (called `Env` here) that extends `CustomData` and a stub
    * version of `FragmentEnv` called `FragmentEnvReference`.
    *
    * Note that the data used to instantiate `CustomData` here _does not matter_, these are just
    * placeholder values. The actual data will be supplied by the `UniformBlock` we set up earlier.
    */
  // ```scala
  class Env extends CustomData(vec4(0.0f)) with FragmentEnvReference
  // ```

  /** ### Supplying the environment
    *
    * The reason we have to set up the `Env` class is that under the current set up, we need to
    * supply an instance of the environment to the shader, as reference data. Here we just give a
    * `new Env` and we're done.
    */
  // ```scala
  val shader: ShaderProgram =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[Env](fragment, new Env)
    )
  // ```

  /** ### Using the data
    *
    * We're almost ready to use our ubo data in the shader, but first we need to tell Ultraviolet to
    * define the UBO structure in the final shader code. We do that by calling `ubo[CustomData]` at
    * the top of the shader body.
    *
    * We can now access the `CUSTOM_COLOR` field from the shader environment, and return it as the
    * fragment color.
    */
  // ```scala
  inline def fragment: Shader[Env, Unit] =
    Shader[Env] { env =>
      ubo[CustomData]

      def fragment(color: vec4): vec4 =
        env.CUSTOM_COLOR
    }
  // ```
