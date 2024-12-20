# Shaders and GLSL

The following is a quick overview of shaders, and how they fit into the process of rendering game graphics.

> This section contains simplifications, approximations, hand waving, and downright lies. The aim is to quickly give you some intuition, not to turn you into a rendering pipeline expert.

## What is a shader?

In the current age of computer graphics, whether you are doing 2D or 3D, your work is most likely rendered using 3D graphics hardware, and part of that process employs a 'Shader program' that tells the hardware how to render parts of your image. Shader programs are made up of two or more smaller programs. In WebGL, the only two available types of these programs are called the vertex and fragment shaders - a pair makes your complete shader program - but in other platforms you might also be able to define additional shader types, like compute shaders.

### Graphics Hardware

Computer graphics are usually processed on dedicated hardware, called a GPU (Graphics Processing Unit).

GPUs differ from CPUs (Central Processing Unit), because they are a type of vector processor, meaning they are designed to do huge amounts of one kind of work at a time. In the case of graphics rendering, that roughly translates to "rendering millions of triangles".

A good analogy is a factory production line, which is set up to mass produce yellow rubber ducks. If you wanted to switch to producing green frogs instead of yellow ducks, then you need to go through the expensive process of halting production, cleaning everything down, reconfiguration the hardware, and restarting production.

The process of rendering an array of triangles goes something like this:

1. Prepare the data in the graphics cards memory, including:
   1. Textures
   2. Triangle data (arrays of coordinates)
   3. Shader programs (for WebGL, one shader program is a pair of programs called the vertex and fragment shaders respectively)
   4. Constants, variables, and other reference data
   5. etc.
2. Run a program (the vertex shader) to correctly position the triangle on the screen while accounting for, among other things, scale, perspective, and camera location.
3. Run another program (the fragment shader) to draw the triangle with the right textures and lighting and effects.
4. Return the image.

Vertex shaders are run once per vertex (corner) of a triangle. Fragment shaders are run once per 'fragment' which though not technically accurate, for our purposes we can think of pixels. Both programs are written in a language called GLSL (for WebGL) and run with 'embarrassing parallelism', meaning that in the case of the fragment shader for example, each execution of the program renders a single 'pixel', in parallel, with no knowledge of the surrounding pixels. In Scala pseudo code, it's a bit like this:

```scala
val pixels: List[Pixel]    = ???
val render: Pixel => Color = ???

pixels.par.foreach(render)
```

As with the factory in the analogy, this process suffers and slows down if there is a 'context switch', in which any of the data or conditions are changed, because the parallel processing production line must be interrupted and reset.

## Shaders in 2D graphics

The process specifically for 2D graphics is roughly as follows:

1. Start with a high level description of some on-screen entities. Where each entity has properties like position, size, and rotation.
2. Sort the scene by depth, and draw from furthest away to nearest, to ensure the transparency will be handled correctly.* 
3. Convert the on-screen entities into mesh data. Since this is 2D, each entity is a rectangle. The rectangle is known as a 'quad'. A quad is made of 2 triangles that share 4 vertices (a, b, c, and d), as follows:

```
a___d
|\  |
| \ |
|__\|
b   c
```

4. Upload the mesh and other information, like textures and shader programs, to the graphics hardware.
5. For each vertex (a, b, c, or d), run a ***Vertex shader*** to tell the graphics hardware, where the vertices should be rendered.
6. The graphics hardware then interpolates between the values between the vertices, and for each render-able position between two vertexes, it runs the ***fragment shader*** supplying interpolated values, notably UV coordinates. For example, halfway between vertices (a) and (d) on the diagram, will have a UV coordinate of (0.0f, 0.5f). The fragment shader tells the graphics hardware what color that fragment (i.e. pixel, more or less. Kinda.) should be.
7. The rendered entity is blended into whatever else has been rendered so far by some process.

(* This is known as a painters algorithm. The process is important because, perhaps surprisingly, 3D graphics hardware is not good at handling transparency, and game developers employ lots of tricks to work around this limitation.)

## What is GLSL?

GLSL is a very small C-like language that is compiled and run on the GPU. There are other GLSL-like languages and formats (such as HLSL), but Ultraviolet only cares about GLSL since that is the language of WebGL (versions 100 and 300, specifically). 

***GLSL is*** for doing maths on arrays of simple numeric data types.

***GLSL is not*** a general purpose language like Scala.

The implications of that are that while it is possible to represent most of GLSL in Scala, the opposite is not true. For example, GLSL has no notion of strings.

## Entities, layers, and blending

Most of the description above applies fairly directly to rendering what Indigo calls 'entities' - things on the screen. However, the description alludes to 'some process' of blending the entity into whatever has already been drawn so far.

Blending is the process of combining one image with another, and they may not necessarily be the same size or in the same position. There are two aspects to blending:

1. Blend Mode - this is a hardware mode where essentially you are telling the hardware how to combine numbers. Colors are represented as `vec4`s (`Vector4`, i.e. x,y,z,w or r,g,b,a) so to blend red (`vec4(1.0f, 0.0f, 0.0f, 1.0f)`) and blue (`vec4(0.0f, 0.0f, 1.0f, 1.0f)`) together, you need to decide how the maths should be done, e.g. add, multiply, subtract, interpolate, etc.
2. Blend shaders - very similar to entity shaders, they both have a vertex and fragment component, but a few of the values differ. Blend shaders have the source and destination color values, for example.

Indigo blends entities onto a layer, and then the layer onto the final scene rendered so far (called a buffer).

# Further reading on Shaders

Nothing created here with regard to shaders is original. Here are a few links to shader related goodness.

- [https://thebookofshaders.com/](https://thebookofshaders.com/)
- [https://iquilezles.org/articles/](https://iquilezles.org/articles/)
- [https://github.com/ashima/webgl-noise](https://github.com/ashima/webgl-noise)
- [https://www.shadertoy.com/](https://www.shadertoy.com/)
- [https://www.khronos.org/opengl/wiki/Core_Language_(GLSL)](https://www.khronos.org/opengl/wiki/Core_Language_(GLSL))
