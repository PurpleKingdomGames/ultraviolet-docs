# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this repo is

Living documentation for **Ultraviolet**, a Scala 3 → GLSL inline transpiler that is part of the [Indigo](https://indigoengine.io/) engine ecosystem. The repo holds small Scala 3 example shaders that are compiled with Scala.js, presented in the browser via Indigo, and assembled into the published docs site at https://ultraviolet.indigoengine.io/. New examples are encouraged — the project explicitly treats this as documentation-by-demo.

## Build & dev commands

- `./mill` — Mill 1.1.5 wrapper. Use it for all Mill targets (do not assume a system `mill` matches).
- `./mill __.compile` — compile every module. CI uses this.
- `./mill __.fastLinkJS` — link all examples to JS. CI uses this.
- `./mill __.test` — run mUnit Scala.js tests across modules. Each example has a `test` sub-module declared in the build, though most have no test sources yet.
- `./mill examples.fragment.sdf.circle.fastLinkJS` — link a single example. The module path mirrors the directory path under `examples/`.
- `bash ci.sh` — the exact CI invocation (compile + fastLinkJS, `set -e`).
- `purpledoc -i .` — regenerate the docs site from `website/` + example READMEs into `docs/`. Slow first time; cached after that. Requires `purpledoc` on PATH. Configured by `purpledoc.yaml`.
- `nix develop` (or `direnv allow` via `.envrc` + `flake.nix`) — dev shell providing JDK 17, Mill, sbt, Node, Yarn, and `http-server`.
- Formatting: scalafmt 3.8.5, Scala 3 dialect, 100-column max (`.scalafmt.conf`).

## Build status caveat — read before editing the build

The repo is mid-migration from Mill `build.sc` (deleted) to Mill 1.x `build.mill`. The new `build.mill` currently has the `ShaderModule` trait and almost every example module **commented out** — only the empty `examples` shell compiles. The commented template at the bottom of `build.mill` is the source of truth for what each shader module should look like once restored. The compiled artifacts under `out/`, `.bloop/`, and `.metals/` are stale from before the migration; do not rely on them as evidence that the current build works.

## Repository layout (non-obvious bits)

- `examples/<kind>/<category>/<name>/` — each example is its own Mill module.
  - `kind` ∈ `fragment`, `vertex`, `language-features`.
  - Under `fragment`, `category` ∈ `basics`, `demos`, `noise`, `patterns`, `sdf`, `shapes`, `textures`.
  - Each module has `src/<Name>.scala` and a `README.md`. The README is the narrative rendered on the docs site; the `.scala` source is embedded into it using `// ` + triple-backtick-`scala` markers in the source to delimit the visible block.
- `shadertoy/<name>/` — same module shape as `examples`, but uses `IndigoSandbox` plus `ultraviolet.shadertoy.*`. Shader is declared in a `ShaderToyExample` object with a `mainImage(fragColor, fragCoord)` function matching the Shadertoy convention.
- `website/` — markdown source (`documentation/`, `examples/`, `directory.conf` files) that purpledoc consumes alongside the example READMEs.
- `docs/` — generated site, committed and served via GitHub Pages. Do not hand-edit.
- `assets/` — shared static assets bundled into examples.
- `.purpledoc/` — purpledoc's working cache (`compiled-sources`, `generated-docs`, `livedemos`).

## Example module conventions

Each shader example follows a fixed shape — match it when adding a new one:

- Entry point object extends `IndigoShader` (or `IndigoSandbox` for shadertoy) and is annotated `@JSExportTopLevel("IndigoGame")`. Indigo's JS bootstrap looks up that exact name.
- Imports `generated.*` for `Config` and `Assets`. These are produced by `IndigoGenerators` in `build.mill` (`generateConfig("Config", indigoOptions)` and `listAssets("Assets", indigoOptions.assets)`); do not hand-write them.
- The shader itself lives in a **separate inner `object CustomShader`** that imports `ultraviolet.syntax.*` inside the object body. This is deliberate, not stylistic: `indigo.*` and `ultraviolet.syntax.*` both define `Shader`, `vec4`, etc., and importing them at the same scope collides. See `website/documentation/guide/gotchas.md` for the canonical writeup.
- Shader bodies are `inline def`s returning `Shader[FragmentEnv, Unit]` (or `Shader[ShaderToyEnv, Unit]`). The `inline` is required so Ultraviolet can see the AST at transpile time.
- Float literals use the `f` suffix (`1.0f`, not `1.0`). GLSL is float-typed and Ultraviolet does not silently widen `Double`.

## Per-example build template

When restoring or adding modules, each `ShaderModule extends MillIndigo` provides:

- `scalaVersion = "3.8.4-M3"`, `scalaJSVersion = "1.21.0"`.
- `scalacOptions` that filter out `-Wunused:locals` and `-Wnonunit-statement` — Ultraviolet shader code triggers spurious warnings on both, so leaving them in makes `-Xfatal-warnings` unusable.
- `indigoOptions` built via `makeIndigoOptions(title)`: window size (default 400×400), `assets/` directory, black background, exclusions for `.gitkeep` and `.DS_Store`.
- `indigoGenerators` that emits `Config` and `Assets` into the `generated` package — this is what `import generated.*` resolves to.
- Dependencies on `ultraviolet`, `indigo`, `indigo-extras`, `indigo-json-circe`, all pinned to `indigoEngineVersion` (currently `0.30.0-M1-PREVIEW`).
- A `test` inner module using mUnit with `ModuleKind.CommonJSModule`.

## Site config

`purpledoc.yaml` controls site-wide settings: title, `latestRelease`, top-nav order (`documentation`, `examples`, `shadertoy`), repo edit-link bases, Discord link, demo iframe size. Edit it for site-wide changes rather than touching anything under `docs/`.

## CI

`.github/workflows/ci.yml` runs on push, PR, and `workflow_dispatch`, and invokes `bash ci.sh` on Ubuntu with JDK 11 (adopt@1.11). Note the version mismatch: CI uses **JDK 11** but the Nix dev shell pins **JDK 17**.
