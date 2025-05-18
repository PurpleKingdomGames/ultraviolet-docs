import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $file.scripts.shadermodule

import indigoplugin._

object examples extends mill.Module {

  object fragment extends mill.Module {

    object basics extends mill.Module {

      object colours extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Colours")
      }

      object `colour-constants` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Colour Constants")
      }

      object `colour-interpolators` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Colour Interpolators")
      }

      object minimal extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Minimal Fragment Shader Setup")
      }

    }

    object demos extends mill.Module {

      object campfire extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Campfire")
            .withWindowSize(192, 192)
      }

      object pulsingbox extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("PulsingBox")
      }

      object `glowing-star` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Glowing Star")
      }

    }

    object noise extends mill.Module {

      object `cellular-noise` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Cellular Noise")
      }

      object `classic-perlin-noise` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Classic Perlin Noise")
      }

      object `gradient-noise` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Gradient Noise")
      }

      object `simplex-noise` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Simplex Noise")
      }

      object `white-noise` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("White Noise")
      }

    }

    object patterns extends mill.Module {

      object grid extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Grid")
      }

      object rainbow extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Rainbow")
      }

      object `simple-voronoi` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Simple Voronoi")
      }

      object stripes extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Stripes")
      }

      object `wavy-stripes` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("WavyStripes")
      }

    }

    object sdf extends mill.Module {

      object box extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Box SDF")
      }

      object circle extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Circle SDF")
      }

      object hexagon extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Hexagon SDF")
      }

      object segment extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Segment SDF")
      }

      object star extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Star")
      }

      object triangle extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Triangle")
      }

    }

    object shapes extends mill.Module {

      object doughnut extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Doughnut")
      }

      object metaballs extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Metaballs")
      }

    }

    object textures extends mill.Module {

      object `basic-texture-use` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Basic Texture Use")
      }

      object `blending-textures` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Blending Textures")
      }

      object `specifying-texture-coords` extends shadermodule.ShaderModule {
        val indigoOptions: IndigoOptions =
          makeIndigoOptions("Specifying Texture Coordinates")
      }

    }

  }

  object vertex extends mill.Module {

    object minimal extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Minimal Vertex Shader Setup")
    }

    object movingvertices extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Moving Vertices")
    }

    object manipulatinguvs extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Manipulating UVs")
    }

  }

  object `language-features` extends mill.Module {

    object imports extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Using Imports & Shared Code")
    }

    object ubos extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Supplying Data to a Shader with UBOs")
    }

  }

}

object shadertoy extends mill.Module {

  object default extends shadermodule.ShaderModule {
    val indigoOptions: IndigoOptions =
      makeIndigoOptions("Default Shadertoy Example")
  }

  object plasma extends shadermodule.ShaderModule {
    val indigoOptions: IndigoOptions =
      makeIndigoOptions("Plasma Shadertoy Example")
  }

  object seascape extends shadermodule.ShaderModule {
    val indigoOptions: IndigoOptions =
      makeIndigoOptions("Seascape Shadertoy Example")
  }

}
