# Intro to Shader Programs

The following is a quick overview of shaders, and how the fit into the process of rendering game graphics.

## What are shaders and how are they used?

In the current age of computer graphics, whether you are doing 2D or 3D, your work is most likely rendered using 3D graphics hardware, and part of that process employs a 'Shader program' that tells the hardware how to render parts of your image.

Shader programs are actually made up of several smaller programs. In GLSL the only two types of these programs are call the vertex and fragment shaders - a pair makes your complete shader program - but in other platforms you might also be able to define additional shader types, like compute shaders.

The process for 2D graphics, where transparency happens at every level, is roughly as follows:

1. Start with a high level description of some on-screen entities. Where each entity has properties like position, size, and rotation.
2. Sort the scene by depth, and draw from furthest away to nearest, to ensure the transparency will be handled correctly.* 
3. Convert the on-screen entities into mesh data. Since this is 2D, each entity is a rectangle. The rectangle is known as a 'quad'. A quad is made of 2 triangle that share 4 vertices (a, b, c, and d), as follows:

```
a___d
|\  |
| \ |
|__\|
b   c
```

4. Upload the mesh and other information, like textures and shader programs, to the graphics hardware.
5. For each vertex (a, b, c, or d), run a ***Vertex shader*** to tell the graphics hardware, where the vertices should be rendered.
6. The graphics hardware then interpolates between the values between the vertices, and for each renderable position between two vertexes, it runs the ***fragment shader*** supplying interpolated values, notably UV coordinates. For example, halfway between vertices (a) and (d) on the diagram, will have a UV coordinate of (0.0f, 0.5f). The fragment shader tells the graphics hardware what color that fragment (i.e. pixel, more or less. Kinda.) should be.
7. The rendered entity is blended into whatever else has been rendered so far by some process.

(* This is known as a painters algorithm. The process is important because, perhaps surprisingly, 3D graphics hardware is not optimised for handling transparency, and game devs employ lots of tricks to work around this limitation.)

In summary:

- Vertex shaders position the entity on the screen.
- Fragment shaders decide what color the pixels of the entity will be.

## Entities, layers, and blending

Most of the description above applies fairly directly to rendering what Indigo calls 'entities'. However, the last point of the description alludes to 'some process' of blending the entity into whatever has already been drawn so far.

Blending is the process of combining one image with another, and they may not necessarily be the same size or in the same position. There are two aspects to blending:

1. Blend Mode - this is a hardware mode where essentially you are telling the hardware how to combine numbers. Colors are represented as `vec4`s (Vector4, i.e. x,y,z,w or r,g,b,a) so to blend red (`vec4(1.0f, 0.0f, 0.0f, 1.0f)`) and blue (`vec4(0.0f, 0.0f, 1.0f, 1.0f)`) together, you need to decide how the maths should be done.
2. Blend shaders.

Blend shaders are very similar to entity shaders, they both have a vertex and fragment component, but a few of the values differ. Blend shaders have the source and destination color values, for example.

Indigo blends entities onto a layer, and then the layer onto the final scene rendered so far (called a buffer).