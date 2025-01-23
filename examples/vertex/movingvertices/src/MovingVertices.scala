import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

/** ## How to move vertices in a vertex shader
  */
@JSExportTopLevel("IndigoGame")
object MovingVertices extends IndigoShader:

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

  /** We need to define a shader that takes both the usual fragment shader, and also a vertex
    * shader. In this case we pass in the default environment to both.
    */
  // ```scala
  val shader: ShaderProgram =
    UltravioletShader(
      ShaderId("shader"),
      EntityShader.vertex[VertexEnv](vertex, VertexEnv.reference),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )
  // ```

  /** A vertex shader is just a function that takes a vec4 of the current vertex position, and
    * returns a new vec4 of the manipulated vertex position.
    *
    * In this example, we calculate an angle based on the current time, and then shift the current
    * vertex by that angle.
    */
  // ```scala
  inline def vertex: Shader[VertexEnv, Unit] =
    Shader[VertexEnv] { env =>
      def vertex(v: vec4): vec4 =
        val angle = env.TAU * (env.TIME % 1.0f)

        v + vec4(
          vec2(sin(angle) * 0.2f, cos(angle) * 0.2f),
          vec2(0.0f)
        )
    }
  // ```

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      def fragment(color: vec4): vec4 =
        vec4(env.UV, 0.0f, 1.0f)
    }
