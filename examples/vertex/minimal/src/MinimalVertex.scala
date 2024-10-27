import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

/** ## How to set up a minimal vertex shader
  */
@JSExportTopLevel("IndigoGame")
object MinimalVertex extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = None
  val channel1: Option[AssetPath] = None
  val channel2: Option[AssetPath] = None
  val channel3: Option[AssetPath] = None

  val shader: Shader =
    CustomShader.shader

/** Inside a custom shader object we define an ultraviolet shader. To do that, we need to supply a
  * ShaderId, that we'll need to register in Indigo's boot data, and also the vertex and fragment
  * shaders themselves.
  *
  * We import the ultraviolet syntax inside the object in order to avoid import collisions.
  *
  * The vertex shader presented here is a 'no-op', it simply returns the vertex position as is,
  * without modification. However we could use this function to affect the position of the vertex or
  * values such as the UV coordinates. We could also do calculations and pass the results across to
  * the fragment shader via 'varying' values.
  *
  * The fragment shader is a simple function that maps the UV coordinates to the red and green
  * channels, just so that we can see the effect of the shader.
  */
// ```scala
object CustomShader:

  val shader: Shader =
    UltravioletShader(
      ShaderId("shader"),
      EntityShader.vertex[VertexEnv](vertex, VertexEnv.reference),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  import ultraviolet.syntax.*

  inline def vertex: Shader[VertexEnv, Unit] =
    Shader[VertexEnv] { env =>
      def vertex(v: vec4): vec4 =
        v
    }

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      def fragment(color: vec4): vec4 =
        vec4(env.UV, 0.0f, 1.0f)
    }
// ```
