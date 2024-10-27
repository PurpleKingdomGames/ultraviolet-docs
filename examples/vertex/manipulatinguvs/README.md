# Manipulating UVs

UVs are the (x, y) coordinates within an on screen entity. The range from 0.0 to 1.0 in both the x and y axis. In Indigo, those coordinates go from top left to bottom right.

In the majority of cases the standard provided UVs are perfectly adequate for you to accomplish your rendering tasks.

However from time to time you may have a requirement that involves, say, rendering a subset of a large texture within a smaller entity, e.g, a 64 x 64 pixel sprite rendering animation frames from a texture sheet that is 256 x 256 (i.e. containing 16 frames of animation). You can achieve that inside a fragment shader, but there are two things to bear in mind:

1. Doing these calculations in the fragment shader is relatively expensive, since you will do the calculation exactly 4 times in a vertex shader, and once PER FRAGMENT (i.e. PIXEL, more or less) in the fragment shader. Fragment shaders are _extremely_ fast, so don't _worry_ about doing calculations there - particularly while prototyping, it's just a consideration.
2. If you need to do UV manipulation in the fragment shader, then you need a custom fragment shader. If you do the work in the vertex shader, yes you need a _small_ custom vertex shader, but after that you can use any existing fragment shader to do the rendering, with all the built in bells and whistles they come with.


