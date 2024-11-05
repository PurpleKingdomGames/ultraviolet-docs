# Using Imports & Shared Code

Imports and shared code are not entirely straight forward to use with Ultraviolet.

Ultraviolet would never have come into being without Scala 3's powerful inline macro system, but this is where you really start to see the limits of the current setup, and it's all down to how inlining works. No doubt all these problems are solvable, we only await someone with the time and inclination to come and fix them!

In any case, while sharing code in Ultraviolet is undoubtedly particular and peculiar to use, it is still a massive improvement over building shaders with strings as you would in JavaScript.

Please see [the main docs on inlining](/documentation/scalavsglsl/inlining.md) for more information.


