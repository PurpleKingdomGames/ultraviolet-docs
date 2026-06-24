package examples

import indigo.*

import generated.*
import ultraviolet.syntax.*

final class ClassicPerlinNoise() extends Game.ShaderPlayground:

  val gameId: GameId =
    GameId("game")

  val config: EngineConfig =
    Config.config

  val assets: Set[AssetType]             = Assets.assets.assetSetRelative
  val channel0: Option[AssetPath]        = None
  val channel1: Option[AssetPath]        = None
  val channel2: Option[AssetPath]        = None
  val channel3: Option[AssetPath]        = None
  val uniformBlocks: Batch[UniformBlock] = Batch.empty

  val shader: ShaderProgram =
    PerlinNoiseShader.shader

object PerlinNoiseShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("perlin noise shader"),
      EntityShader.fragment(fragment, FragmentEnv.reference)
    )

  /** The noise function is imported, but we then need to set up a proxy function. This is because
    * the macro system relies on inlining, and we want to create a function definition, not just
    * inline the noise code at the point of use.
    */
  // ```scala
  inline def fragment =
    Shader[FragmentEnv] { env =>
      import ultraviolet.noise.*

      def proxy: vec2 => Float =
        p => perlin(p)

      def fragment(color: vec4): vec4 =
        vec4(vec3(proxy(env.UV * 8.0f)), 1.0f)
    }
  // ```
