# Supplying Array Data via UBOs in Indigo

At the time of writing, sending array data to your shaders via UBOs in Indigo is a bit of a rough experience. There are open tickets to try and improve the situation, but this is an attempt to document a path that works and to call out a few points worth knowing. The goal is to give people a way forward while acknowledging that it's less than ideal.

## Notes

### Always send arrays in UBOs, separate from other data

For the sake of your sanity. Example:

Do:

```scala
case class UBO1(COUNT: Int)
case class UBO2(MAP: Array[Float])
```

Do NOT:

```scala
case class UBO1(COUNT: Int, MAP: Array[Float])
```

### Array sizes must be known

Arrays in GLSL are a fixed size. If you need to send variable amounts of data, what you need to do is set an array large enough for all your possible data, and then supply another value indicating the count.

### Beware of large arrays (Performance bottleneck)

By default, Ultraviolet supports auto conversion of array sizes up to `4096`.

You _can_ add support for arbitrarily sized arrays, up to the UBO max (..max supported by Indigo, in practice this is hardware dependent), like this:

```scala
given ultraviolet.macros.ShaderTypeOf[Array[Float]] with
  def typeOf: String = s"float [65536]"
```

***HOWEVER.***

Because Indigo sets UBOs whenever it needs them, you pay the data transfer cost every time (unless you're using the same shader many times sequentially). The transfer cost is surprisingly high, and you may quickly find yourself running into performance problems.
