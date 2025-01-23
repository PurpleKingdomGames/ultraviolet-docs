import indigo.*

import scala.scalajs.js.annotation.*
import generated.*
import ultraviolet.syntax.*

@JSExportTopLevel("IndigoGame")
object Grid extends IndigoShader:

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

  val shader: ShaderProgram =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      def fragment(color: vec4): vec4 =
        val col: vec3 = 0.5f + 0.5f * cos(env.TIME + env.UV.xyx + vec3(0.0f, 2.0f, 4.0f))

        val count   = 10.0f
        val size    = 1.0f / count
        val coords  = env.UV - (size / 2.0f)
        val closest = round(coords / size)

        val coloredGrid = col * (vec3(closest, 0.0f) / count)

        val blur    = distance(coords, closest / count) * count
        val outline = step(0.95f, mod(env.UV, size) * count)
        val lines   = max(outline.x, outline.y)

        val yellow         = vec3(1.0f, 1.0f, 0.0f)
        val combinedColour = mix(coloredGrid * (1.0f - blur), yellow, lines)

        vec4(combinedColour, 1.0f)

    }
