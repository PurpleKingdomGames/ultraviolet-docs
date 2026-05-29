package examples

import indigo.*

import generated.*
import ultraviolet.syntax.*

final class Stripes() extends Game.ShaderPlayground:

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
    CustomShader.shader

object CustomShader:

  val shader: ShaderProgram =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      def fragment(color: vec4): vec4 =
        val amount: Float = step(0.5f, 0.5f + (sin(env.UV.y * env.SIZE.x / 4.0f) * 0.5f))
        val c: vec3       = vec3(1.0f, 0.75f, 0.4f) * (0.8f + amount * 0.2f)

        vec4(c, 1.0f)

    }
