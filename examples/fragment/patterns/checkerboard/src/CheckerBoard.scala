package examples

import indigo.*

import generated.*
import ultraviolet.syntax.*

final class CheckerBoard() extends Game.ShaderPlayground:

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
    CheckerBoardShader.shader

object CheckerBoardShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("checker board shader"),
      EntityShader.fragment(fragment, FragmentEnv.reference)
    )

  inline def fragment =
    Shader[FragmentEnv] { env =>
      def checkerBoard(pt: vec2, size: Float): vec4 =
        val pos = floor(pt / size)
        val pattern = mod(pos.x + mod(pos.y, 2.0f), 2.0f)
        
        pattern * vec4(1.0f)

      def fragment(color: vec4): vec4 =
        checkerBoard(env.SIZE * env.UV, 10.0f)
    }