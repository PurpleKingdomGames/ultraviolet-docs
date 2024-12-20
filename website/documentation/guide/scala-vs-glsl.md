# Scala 3 vs GLSL

Ultraviolet models _most_ of GLSL in Scala, but only a subset of Scala in GLSL. Here is a quick run down of what is supported where.

Note that these docs are likely to fall out of date a little as Scala 3 and Ultraviolet evolve, but it should still give you some idea of what to expect.

## Comparison table

Only included are the differences or note worthy features. If they're the same in both languages they are omitted.

| Feature                           | Scala | GLSL | Ultraviolet | Notes                                                                                            |
| --------------------------------- | ----- | ---- | ----------- | ------------------------------------------------------------------------------------------------ |
| Recursion                         | ✅     | ❌    | ❌           |
| A stack!                          | ✅     | ❌    | ❌           |
| `String` and `Char`               | ✅     | ❌    | ❌           |
| `uint` / `uvec`                   | ❌     | ✅    | ❌           |
| `Double` / `dvec`                 | ✅     | ❌    | ❌           |
| `struct`                          | ❌     | ✅    | 💡           | You can define structs by declaring classes.                                                     |
| for loops                         | ❌     | ✅    | 💡           | In Scala, use the `cfor` or `_for` methods provided to simulate for-loops.                       |
| Imports                           | ✅     | ❌    | ✅           | Imported methods and values must be inlined.                                                     |
| Switch statements                 | ❌     | ✅    | 💡           | Scala does not have switch statements, but they can be expressed using pattern matching instead. |
| If statements can return values   | ✅     | ❌    | ✅           |
| Pattern matches can return values | ✅     | ❌    | ✅           |
| `#define`                         | ❌     | ✅    | ✅           | Use the `@define` annotation. (see note below)                                                   |
| `const`                           | ❌     | ✅    | ✅           | `@const`                                                                                         |
| `uniform`                         | ❌     | ✅    | ✅           | `@uniform`                                                                                       |
| `varying`, `in`, `out`            | ❌     | ✅    | ✅           | `@in`, `@out`                                                                                    |
| `%` (modulus op)                  | ✅     | ❌    | ✅           |
| Lambda/Anonymous functions        | ✅     | ❌    | ✅           |
| `compose`                         | ✅     | ❌    | ✅           |
| `andThen`                         | ✅     | ❌    | ✅           |


Other comments:

- Although Ultraviolet is based primarily on GLSL 300, I've kept `texture2D` and `textureCube` from WebGL 1.0 for clarity, and these are automatically rewritten to `texture` for WebGL 2.0. 
- Preprocessor directives largely don't exist, but `#define` is supported for special cases where you need to define a global value based on a non-constant value.
- GLSL headers can be provided via `PrinterHeader`s.

## Floats. Everywhere.

In GLSL 300 there are no double types. It was very tempting to make Ultraviolet accept `Double`s and write `Float`s for a nicer experience, but in later GLSL versions and other shader languages `Double` and `Float` are separate, so Ultraviolet does 'the right thing' and the cost of some user experience.

***Warning!*** Scala is very good at massaging Floats and Doubles, and so you don't always get helpful warnings when you accidentally use a double. Long story short: If you see an mysterious errors about constructors, you probably just wrote `1.0` instead of `1.0f`.

## Built in functions

GLSL comes LOADED with useful functions for doing all kinds of maths.

Since all of the implementations are well specified, Ultraviolet has fully implemented about 95% of them in Scala in order to help you write unit tests for your code.

The other 5% that cannot be implemented or usefully stubbed, and simply return fixed values. `texture2D` for example will always return `vec4(0.0f)`.
