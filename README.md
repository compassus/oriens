# Oriens

A Boot/Leiningen template for [Compassus](https://github.com/anmonteiro/compassus), a routing library for Om Next.

## Description

Oriens is a Boot / Leiningen template that provides the following features:

- A starter Compassus project with HTML5 history routing (via [Bidi](https://github.com/juxt/bidi)
and [Pushy](https://github.com/kibu-australia/pushy))
- Minimal [Less](http://lesscss.org/) setup
- [Boot](https://github.com/boot-clj/boot) as the generated project's build tool
- Boot tasks for
  - an [interactive programming](https://en.wikipedia.org/wiki/Interactive_programming),
hot code loading development workflow
  - a minimal ClojureScript testing setup with automatic test runs when source files change
  - compiling the Clojurescript project for production use

## Usage

### Boot

To generate a project based on the Oriens template, run the following Boot command:

    $ boot -d seancorfield/boot-new new -t oriens -n your-app


### Leiningen

To generate a project based on the Oriens template, run the following Lein command:

    $ lein new oriens your-app


## License

Copyright © 2016 António Nuno Monteiro

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
