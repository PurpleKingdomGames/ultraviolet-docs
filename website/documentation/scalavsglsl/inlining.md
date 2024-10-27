## Things to know about inlining

Ultraviolet allows you to share / reuse code, as long as it is inlined following Scala 3's standard inlining rules. However there are things to know about how this will affect your GLSL!

Here, 'external' means 'not inside the body of your shader'.

- You cannot inline external `val`s.
- You can inline external `def`s into your code, but:
  - A def that is essentially a call by reference val such as `inline def x = 1.0f` will have it's value inlined.
  - A def that is a function, laid out like a method e.g. `inline def foo(c: Int): Int = c + 1` will be inlined.
  - A def that is an anonymous function will be embedded with a new name and will work exactly as you'd expect, i.e. `inline def foo: Int => Int = c => c + 1`

## Using Ultraviolet with Scala > 3.3.0

Ultraviolet (up to 0.1.1) was built against Scala 3.2.x, but does work with Scala 3.3.0 with one minor caveat: The thing to know is that calling external inlined functions doesn't work the way it used to anymore, and will produce errors that complain about illegal forward references.

Being able to call external inlined functions is important, because it's one of the main ways the Ultraviolet achieves code reuse.

Here is a simple example. Given an object called `Importable` like this:

```scala
object Importable:
  inline def addOne = (i: Int) => i + 1
```

## In Scala 3.2.x

This used to work:

```scala
import Importable.*

inline def fragment: Shader[FragEnv, Int] =
  Shader { _ =>
    val value = 10
    addOne(value)
  }
```

## Scala 3.3.x

Unfortunately the way inlines are represented in the AST has now changed, and the old method does not work anymore. Luckily there is a simple workaround. All we have to do is create a local proxy with the same signature that delegates to the original function:

```scala
import Importable.*

inline def fragment: Shader[FragEnv, Int] =
  Shader { _ =>
    val proxy: Int => Int = addOne
    val value = 10
    proxy(value)
  }
```

With this small change, we can continue to reuse code between shaders as before. Indeed the compiled output will be identical to how it was before.