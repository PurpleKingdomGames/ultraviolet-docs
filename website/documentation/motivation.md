# Motivation

This project is motivated from two needs:

1. The most pressing need is that standard (WebGL) GLSL tooling is not very enjoyable when writing games / shaders in Scala.js.
2. Indigo is currently locked into WebGL 2.0, and to move to other platforms or rendering technologies means having some way to abstract away from that. 

Ultimately the aim is to be able to write Shaders in FP friendly Scala that can target more than just GLSL 300, but that is not necessary for Ultraviolet to be useful and fun.

## Current Goals

Right now, the goal is an almost like-for-like experience of writing GLSL (for WebGL) in Scala 3, in all its specific, procedural glory. It includes a few quality of life improvements such as anonymous functions and function composition, but nothing fancy for now. You can even write unit tests!

The library may ultimately diverge from GLSL, and who knows what sort of problems have been caused by using GLSL as a starting point, but replacing GLSL with Scala GLSL-flavored-shader-experience is the current goal.

## Non-goals

It is _not_ a goal to be able to write arbitrary Scala and have it turned into GLSL. In other words this isn't a 'full' transpiler (like Scala.js), it's a useful cross-over subset of Scala and GLSL. As many GLSL language features as can sensibly be represented, and as much Scala as GLSL can be coerced into expressing.

No monads or typeclasses or other functional abstractions. They don't much sense here anyway! Try it, see for yourself.
