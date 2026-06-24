# Supplying Data with UBOs

At some point, you're going to want to be able to supply data to your shader for some reason, for example, perhaps your shader renders planets and you need to give it the color scheme to change water into lava.

To send data to shaders we use Uniform Buffer Objects (UBOs).

Ultraviolet allows you to declare UBOs as case classes. It isn't a perfect set up, but it works.

## UBO Data Packing Rules

Take heed! When you send data to a shader what you're really doing is sending a series of floats which will be read as a C-like struct at the other end. There are rules about how these floats get packed so that they are correctly read at the other end.

Full rules follow, but to give you some idea:

Say you want to send this case class:

```scala
final case class MyData(angle: Float, position: vec3, multiplier: Float)
```

And for simplicity, lets say the values are:

```scala
MyData(1.0f, vec3(2.0f, 3.0f, 4.0f), 5.0f)
```

This will be very inefficiently packed into this:

```
1.0,0.0,0.0,0.0,
2.0,3.0,4.0,0.0,
5.0,0.0,0.0,0.0
```

Look at all that waste! So many `0.0`s! Might not be a problem for simple cases, but if you're trying to send a lot of data within the limits, then you could run into trouble.

Why does it do this? Well, `angle` seems good, and we might expect that we can fit position into the remaining three floats after it (i.e. 1.0,<2.0,3.0,4.0>,5.0,0.0,0.0,0.0), but the stride is 16 bytes, and a `vec3` is treated as a 16 bytes of data, so by default they cannot be merged.

Better strategies...

#### 1. Reorder the fields

Move the multiplier:

```scala
final case class MyData(angle: Float, multiplier: Float, position: vec3)
MyData(1.0f, 5.0f, vec3(2.0f, 3.0f, 4.0f))
```

Result:

```
1.0,5.0,0.0,0.0,
2.0,3.0,4.0,0.0
```

#### 2. Manually pack the data

```scala
final case class MyData(angleAndPosition: vec4, multiplier: Float)
MyData(vec4(1.0f, 2.0f, 3.0f, 4.0f), 5.0f)
```

Result:

```
1.0,2.0,3.0,4.0,
5.0,0.0,0.0,0.0
```

### The Rules

These rules have saved me many times and originally came from here: [https://youtu.be/bdIZ2ZloXEA?t=113](https://youtu.be/bdIZ2ZloXEA?t=113)

```
"UBO - Uniform Buffer Object"

Uses a struct as a way to defined the data in the buffer.
Struct data based on STD140 layout requires data to exist in 16 byte chunks.

Float, Int and Bools are treated as 4 Bytes of Data.

Arrays, no matter the type, each element is 16 Bytes.

vec2, Contains 2 floats so 4*2 bytes of data (8 Bytes)
vec4, Contains 4 floats so 4*4 bytes of data (16 Bytes)
vec3, Must be treated as 16 bytes of data (i.e. a vec4), last 4 bytes are buffer space

mat3, Contains 3 sets of Vec3 BUT each vec3 is treated as vec4, 3*16 Bytes of data
mat4, Contains 4 sets of Vec4, so 4 * 16 Bytes

For EXAMPLE
Float - Float - Vec3 - Float

FF00 VVVV F000

You also can't straddle byte boundries. So if you're trying to pack this: Float-Vec2
This is valid: F0VV
But this isn't: FVV0
```
