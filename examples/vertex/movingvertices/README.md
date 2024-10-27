# Moving Vertices

3D graphics hardware works by rendering triangles to the screen. In 2D graphics, a rectangular region on the screen is made up of two triangles, known as a quad. At the corners of those triangles are the vertices. A quad has four vertices (a, b, c, and d) that the two triangles share, as follows:

```
a___d
|\  |
| \ |
|__\|
b   c
```

Generally speaking, it isn't necessary to manually shift these vertices into the right place. Indigo will take care of this for you, based on the settings you provide for your entity, like its position, scale and rotation.

That said, sometimes it is necessary or interesting to do so. Perhaps to make an entity wobble or skew.

To do that, we need to provide a vertex shader program that tells Indigo how to adjust the position of each vertex.
