package examples

import indigo.*

import generated.*
import ultraviolet.syntax.*

final class Star() extends Game.ShaderPlayground:

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
    StarShader.shader

object StarShader:

  val shader: UltravioletShader =
    UltravioletShader.entityFragment(
      ShaderId("star shader"),
      EntityShader.fragment(fragment, FragmentEnv.reference)
    )

  inline def fragment =
    Shader[FragmentEnv] { env =>
      import ultraviolet.sdf.*
      import FillColorHelper.*

      def proxy: (vec2, Float, Float) => Float =
        (p, rt, r) => star(p, rt, r)

      def calculateColour: (vec2, Float) => vec4 = (uv, sdf) => fill(uv, sdf)

      def fragment(color: vec4): vec4 =
        calculateColour(env.UV, proxy(env.UV - 0.5f, 0.3f, 0.6f))
    }

object FillColorHelper:
  import ultraviolet.colors.*

  inline def fill(uv: vec2, sdf: Float): vec4 =
    val fill       = vec4(uv, Blue.z, 1.0f)
    val fillAmount = (1.0f - step(0.0f, sdf)) * fill.w
    vec4(fill.xyz * fillAmount, fillAmount)
