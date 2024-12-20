# Motivation

Writing shaders is a fun thing to do, and the primary aim of this project is to bring that programming joy to Scala.

Having said that, the project is also motivated by two real world needs:

1. We'd like a shader writing solution with great tooling, and a seamless integration with Scala based games and graphics projects.
2. Indigo specifically is currently locked into WebGL 2.0, and any future move to other platforms or rendering technologies predicated on being about to abstract away from that.

## Goals

The library may ultimately diverge from GLSL, and who knows what sort of problems have been caused by using GLSL as a starting point, but replacing GLSL with Scala GLSL-flavored-shader-experience is the current goal.

1. An almost like-for-like experience of writing GLSL (for WebGL) in Scala 3, in all its highly specific, procedural glory.
2. A few quality of life improvements over standard GLSL, that Scala developers take for granted, such as:
   1. Code re-use and sharing
   2. Anonymous functions
   3. Simple function composition
   4. Ability to write unit tests
   5. If statements that return
3. Seamless inline integration into Scala projects.

## Non-goals

It is _not_ a goal to be able to write arbitrary Scala and have it turned into GLSL. In other words, this isn't a 'full' transpiler (like Scala.js), it is a useful cross-over subset of Scala and GLSL. As many GLSL language features as can sensibly be represented, and as much Scala as GLSL can be coerced into expressing.

For now at least, there will be no monads or typeclasses or other functional abstractions.
