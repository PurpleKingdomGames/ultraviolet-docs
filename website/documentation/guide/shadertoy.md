# Shadertoy

Although Ultraviolet was designed to support [Indigo](https://indigoengine.io/), it will transpile Scala 3 to any flavor of GLSL for WebGL (i.e. versions 100 or 300).

However in the same way that it has special support for Indigo, it also has some special support for [Shadertoy](https://www.shadertoy.com/), the renowned website for sharing shader based creations.

You can see a couple of samples of how this is done in the examples section, but long story short: You use Ultraviolet to generate the code, and then output it somewhere (perhaps write it out to a file?), and copy+paste it into the right part of shadertoy.
