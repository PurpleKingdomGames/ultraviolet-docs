# Specifying Texture Coordinates

Normally you read the pixel value of your first texture by simply referencing `env.CHANNEL_0`, and you are done.

In Indigo proper, `CHANNEL_0` is carefully constructed to ensure that the texture coordinate read adheres to the fill type specified in Indigo, based on the UV coordinates passed to the fragment shader from the vertex shader.

There are times, though, when you want to be able to control the texture coordinates directly. Maybe you only want to render a sub region of a texture, for example. Let's explore an example.
