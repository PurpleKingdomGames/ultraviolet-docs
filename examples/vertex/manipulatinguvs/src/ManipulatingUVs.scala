import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

/** ## How to manipulate UVs in a vertex shader
  */
@JSExportTopLevel("IndigoGame")
object ManipulatingUVs extends IndigoShader:

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
    * returns a new vec4 of the manipulated vertex position, but the vertex isn't the only value we
    * can alter.
    *
    * In this example, simply return the provided vertex position. The interesting part is that in a
    * vertex shader the UV values are mutable, so we take the current UV and shift it based on an
    * angle manufactured from the current time.
    */
  // ```scala
  inline def vertex: Shader[VertexEnv, Unit] =
    Shader[VertexEnv] { env =>

      def vertex(v: vec4): vec4 =
        val angle = env.TAU * (env.TIME % 1.0f)

        env.UV = env.UV + vec2(sin(angle) * 0.2f, cos(angle) * 0.2f)

        v
    }
  // ```
  /** This means that the entity remains in place, but where a texture would be rendering within
    * it's space is shifed around.
    */

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      def fragment(color: vec4): vec4 =
        vec4(env.UV, 0.0f, 1.0f)
    }
