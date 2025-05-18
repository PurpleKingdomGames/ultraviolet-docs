# Plasma

> This is a [Shadertoy](https://www.shadertoy.com/) example. Because of the way this docs are built, its housed in a minimal Indigo project for built purposes, but the shader code isn't run because it's incompatible with Indigo, so all you get is a black screen.

This example is a port of 'Plasma' by [Jolle](https://www.shadertoy.com/user/jolle), and you can view it here: https://www.shadertoy.com/view/XsVSDz

## The GLSL Output

This is the shadertoy compatible GLSL code that Ultraviolet manufactures, note that this shader is in two parts, Buffer A and Image:

### Buffer A

```glsl
const float pi=3.1415927;
void mainImage(out vec4 fragColor,in vec2 fragCoord){
  float i=fragCoord.x/iResolution.x;
  vec3 t=(iTime+iMouse.y)/vec3(63.0,78.0,45.0);
  vec3 cs=cos(((i*pi)*2.0)+((vec3(0.0,1.0,-0.5))*pi)+t);
  fragColor=vec4(0.5+(0.5*cs),1.0);
}
```
### Image

```glsl
const vec2 vp=vec2(320.0,200.0);
void mainImage(out vec4 fragColor,in vec2 fragCoord){
  float t=(iTime*10.0)+iMouse.x;
  vec2 uv=fragCoord.xy/iResolution.xy;
  vec2 p0=(uv-0.5)*vp;
  vec2 hvp=vp*0.5;
  vec2 p1d=((vec2(cos(t/98.0),sin(t/178.0)))*hvp)-p0;
  vec2 p2d=((vec2(sin((-t)/124.0),cos((-t)/104.0)))*hvp)-p0;
  vec2 p3d=((vec2(cos((-t)/165.0),cos(t/45.0)))*hvp)-p0;
  float sum=0.25+(0.5*(cos(length(p1d)/30.0))+(cos(length(p2d)/20.0))+(sin(length(p3d)/25.0))*(sin(p3d.x/20.0))*(sin(p3d.y/15.0)));
  fragColor=texture(iChannel0,vec2(fract(sum),0.0));
}
```