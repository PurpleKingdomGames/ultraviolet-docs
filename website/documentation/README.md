# Ultraviolet

[Ultraviolet](https://github.com/PurpleKingdomGames/ultraviolet) is a Scala 3 to GLSL (versions 100 and 300) transpiler library built on top of Scala 3 inline macros.

You can use Ultraviolet to generate GLSL shader code for any WebGL use case, such as with [ShaderToy](https://www.shadertoy.com/), but is primarily designed to support [Indigo](https://github.com/PurpleKingdomGames/indigo).

All the shader examples and demos in these docs are written in [Ultraviolet](https://github.com/PurpleKingdomGames/ultraviolet) and presented using [Indigo](https://indigoengine.io/).

The golden rule when writing shaders in Ultraviolet is: ***Keep It Simple!***

GLSL is not a general purpose language like Scala is, and while it's possible to represent most of GLSL in Scala, the opposite is not true.

GLSL is for doing maths on arrays of simple numeric data types and is, as someone else described it, "a very limited programming model."

For all that, it is _very good fun_. Go forth and do maths and make pretty pictures!

## Give me the TL;DR

If you've stumbled across this project and have no idea what all this shader stuff is about:

1. A shader program is used to render graphics on a GPU.
2. ***Think of a tiny C-like program that runs for every pixel on the screen***.
3. Ultraviolet allows you to write those programs in Scala 3, where you would normally use GLSL targeting WebGL in a browser.

## Installation

Check the repo for the latest release number, and substitute the `x.y.z` below accordingly.

sbt

```
libraryDependencies += "io.indigoengine" %%% "ultraviolet" % "x.y.z"
```

Mill

```
def ivyDeps = Agg(ivy"io.indigoengine::ultraviolet::x.y.z")
```

Scala-CLI

```
//> using dep io.indigoengine::ultraviolet:x.y.z
```
