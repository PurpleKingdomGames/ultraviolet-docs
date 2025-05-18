# Default

> This is a [Shadertoy](https://www.shadertoy.com/) example. Because of the way this docs are built, its housed in a minimal Indigo project for built purposes, but the shader code isn't run because it's incompatible with Indigo, so all you get is a black screen.

This example is the default shader you are given when you make a new shadertoy project.

## The GLSL Output

This is the shadertoy compatible GLSL code that Ultraviolet manufactures:

```glsl
void mainImage(out vec4 fragColor,in vec2 fragCoord){
  vec2 uv=fragCoord/iResolution.xy;
  vec3 col=0.5+(0.5*(cos((iTime+uv.xyx)+vec3(0.0,2.0,4.0))));
  fragColor=vec4(col,1.0);
}
```
