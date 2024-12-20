# Ultraviolet

[Ultraviolet](https://github.com/PurpleKingdomGames/ultraviolet) is a Scala 3 to GLSL 100/300 'inline' transpiler library.

You can use Ultraviolet to generate GLSL shader code for any WebGL use case, such as with [ShaderToy](https://www.shadertoy.com/), but is primarily designed to support [Indigo](https://github.com/PurpleKingdomGames/indigo).

All the shader examples and demos in these docs are written in [Ultraviolet](https://github.com/PurpleKingdomGames/ultraviolet) and presented using [Indigo](https://indigoengine.io/).

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
