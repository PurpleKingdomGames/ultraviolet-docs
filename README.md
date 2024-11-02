# Ultraviolet Docs

This repository is meant to act as 'living documentation' for Ultraviolet, a Scala 3 to GLSL transpiler. Small, simple, reference examples of how to use Ultraviolet, and perhaps a few larger demos too.

No example is too small, no library feature unworthy of a demo. If you think something is missing, please feel free to contribute.

## Building the examples and documentation

This documentation site is built using [purpledoc](https://github.com/PurpleKingdomGames/purpledoc).

With an alias to `purpledoc` setup, you can build everything by running `purpledoc -i .` in the root of this repo.

This will be slow the first time you run it, but once the modules have all been built and cached by Mill, it should be much faster.