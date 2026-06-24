package examples

import indigo.*
import indigo.shaders.*
import ultraviolet.syntax.*

import generated.*
import indigo.shaders.ShaderPrimitive

/** ## UniformBlock derivation limitations
  *
  * Ultraviolet is particular about what you can derive to ToUniformBlock:
  *
  * ```
  * Unsupported shader uniform type. Supported types From Scala (Int, Long, Float, Double), Indigo [RGBA, RGB, Point, Size, Vertex, Vector2, Vector3, Vector4, Rectangle, Matrix4, Radians, Millis, Seconds, Array[Float], js.Array[Float]], and UltraViolet [vec2, vec3, vec4, mat4]. However, if you intend to use the same case class for both Indigo and UltraViolet, you should stick to Float + the UltraViolet types.
  * ```
  *
  * Here out first UBO with some simple data in it.
  */
// ```scala
case class CustomData(CUSTOM_COLOR: vec4) derives ToUniformBlock
// ```

/** ## Avoid `derives` for arrays
  *
  * Technically, derives is supported, but if we're treating this example as a recipe, then in this
  * recipe we'll opt to avoid it and follow a known good path.
  */
// ```scala
case class ArrayData(ANOTHER_COLOR: array[4, Float])
// ```

/** ## Setting up the shader Environment
  *
  * This is a bit unfortunate. There are a number of ways to declare the Environment in the shader
  * itself, i.e. `Shader[Env]`. For example, you can do things like `Env & CustomData`.
  *
  * In this case (..and again we're doing a happy path for arrays specifically) we're just going to
  * make a new type with the same fields.
  */
// ```scala
case class Env(
    CUSTOM_COLOR: vec4,
    ANOTHER_COLOR: array[4, Float]
) extends FragmentEnvReference
object Env:
  def ref: Env =
    Env(vec4(0.0f), array[4, Float]())
// ```

final class UBOsWithArrays() extends Game[Unit, Unit, Model]:

  val gameId: GameId =
    GameId("game")

  def boot(flags: Map[String, String]): Outcome[BootResult[Unit, Model]] =
    Outcome(
      BootResult
        .noData(Config.config)
        .withAssets(Assets.assets.assetSetRelative)
        .withShaders(CustomShader.shader)
    )

  def eventFilters: EventFilters =
    EventFilters.Permissive

  def initialScene(bootData: Unit): Option[SceneName] =
    None

  def scenes(bootData: Unit): NonEmptyBatch[Scene[Model]] =
    NonEmptyBatch(Scene.empty)

  def setup(
      bootData: Unit,
      assetCollection: AssetCollection,
      dice: Dice
  ): Outcome[Startup[Unit]] =
    println(CustomShader.fragment.toGLSL300.code)
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[Model] =
    Outcome(Model(Size(1)))

  def updateModel(context: Context, model: Model): GlobalEvent => Outcome[Model] =
    case ViewportResize(size) =>
      Outcome(model.copy(viewport = size))

    case KeyboardEvent.KeyUp(Key.KEY_F) =>
      Outcome(model).addGlobalEvents(FullScreenEvent.Toggle)

    case _ =>
      Outcome(model)

  /** ## Sending the array data
    *
    * More repetition as we send over our data to the shader via the shader data.
    */
  // ```scala
  def present(context: Context, model: Model): Outcome[SceneUpdateFragment] =
    Outcome(
      SceneUpdateFragment(
        Layer(
          BlankEntity(
            model.viewport,
            ShaderData(CustomShader.shader.id)
              .withUniformBlocks(
                Batch(
                  UniformBlock(
                    UniformBlockName("ArrayData"),
                    Batch(
                      Uniform("ANOTHER_COLOR") -> ShaderPrimitive
                        .array(4, Array(0.0f, 1.0f, 1.0f, 1.0f))
                    )
                  )
                )
              )
              .addUniformData(CustomData(vec4(1.0f, 0.0f, 1.0f, 1.0f)))
          )
        )
      )
    )
  // ```

final case class Model(
    viewport: Size
)

object CustomShader:

  val shader: ShaderProgram =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[Env](fragment, Env.ref)
    )

  /** ## Using the array data
    *
    * Once we've gone through the steps to get our array data set-up, things get back to normal. In
    * this example we're accessing the components by index, but you could also loop over the arrays
    * with something like a while loop if you prefered.
    */
  // ```scala
  inline def fragment: Shader[Env, Unit] =
    Shader[Env] { env =>
      ubo[CustomData]
      ubo[ArrayData]

      def fragment(color: vec4): vec4 =
        val magenta = env.CUSTOM_COLOR
        val cyan = vec4(
          env.ANOTHER_COLOR(0),
          env.ANOTHER_COLOR(1),
          env.ANOTHER_COLOR(2),
          env.ANOTHER_COLOR(3)
        )

        val mByX = vec4(magenta.xyz * env.UV.x, env.UV.x)
        val cByY = vec4(cyan.xyz * env.UV.y, env.UV.y)

        mix(mByX, cByY, mByX.a)
    }
  // ```
