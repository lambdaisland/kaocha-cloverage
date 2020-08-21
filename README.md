# kaocha-cloverage

<!-- badges -->
[![CircleCI](https://circleci.com/gh/lambdaisland/kaocha-cloverage.svg?style=svg)](https://circleci.com/gh/lambdaisland/kaocha-cloverage) [![cljdoc badge](https://cljdoc.org/badge/lambdaisland/kaocha-cloverage)](https://cljdoc.org/d/lambdaisland/kaocha-cloverage) [![Clojars Project](https://img.shields.io/clojars/v/lambdaisland/kaocha-cloverage.svg)](https://clojars.org/lambdaisland/kaocha-cloverage) [![codecov](https://codecov.io/gh/lambdaisland/kaocha-cloverage/branch/master/graph/badge.svg)](https://codecov.io/gh/lambdaisland/kaocha-cloverage)
<!-- /badges -->

Kaocha plugin to get code coverage reports through [Cloverage](https://github.com/cloverage/cloverage).

<!-- opencollective -->
### Support Lambda Island Open Source

If you find value in our work please consider [becoming a backer on Open Collective](http://opencollective.com/lambda-island#section-contribute)
<!-- /opencollective -->

## Installation

deps.edn

```
lambdaisland/kaocha-cloverage {:mvn/version "1.0.56"}
```

project.clj

```
[lambdaisland/kaocha-cloverage "1.0.56"]
```

## Usage

Add the dependency

``` clojure
;; deps.edn
{:paths [...]
 :deps {...}
 :aliases
 {:test
  {:extra-deps {lambdaisland/kaocha {...}
                lambdaisland/kaocha-cloverage {...}}}}}
```

Enable the plugin

- Command line

```
bin/kaocha --plugin cloverage
```

- Configuration file

``` clojure
;; tests.edn
#kaocha/v1
{:plugins [:kaocha.plugin/cloverage]}
```

This plugin adds a large amount of command line options to Kaocha. Run `bin/kaocha --plugin cloverage --help` to see them.

Alternatively Cloverage can be configured through `tests.edn`. Source paths specified in your tests suites will be automatically instrumented. Other Cloverage options can be specified under a top level `:cloverage/opts` key. Run `bin/kaocha --plugin cloverage --print-config` to see the current default values.

```
;; tests.edn
#kaocha/v1
{:plugins [kaocha.plugin/cloverage]
 :cloverage/opts
 {:ns-exclude-regex [],
  :text? false,
  :lcov? false,
  :high-watermark 80,
  :fail-threshold 0,
  :output "target/coverage",
  :low-watermark 50,
  :ns-regex [],
  :summary? true,
  :coveralls? false,
  :emma-xml? false,
  :html? true,
  :nop? false,
  :codecov? false}}
```

<!-- contributing -->
### Contributing

Everyone has a right to submit patches to this projects, and thus become a contributor.

Contributors MUST

- adhere to the [LambdaIsland Clojure Style Guide](https://nextjournal.com/lambdaisland/clojure-style-guide)
- write patches that solve a problem. Start by stating the problem, then supply a minimal solution. `*`
- agree to license their contributions as MPLv2.
- not break the contract with downstream consumers. `**`
- not break the tests.

Contributors SHOULD

- update the CHANGELOG and README.
- add tests for new functionality.

If you submit a pull request that adheres to these rules, then it will almost
certainly be merged immediately. However some things may require more
consideration. If you add new dependencies, or significantly increase the API
surface, then we need to decide if these changes are in line with the project's
goals. In this case you can start by [writing a
pitch](https://nextjournal.com/lambdaisland/pitch-template), and collecting
feedback on it.

`*` This goes for features too, a feature needs to solve a problem. State the problem it solves, then supply a minimal solution.

`**` As long as this project has not seen a public release (i.e. is not on Clojars)
we may still consider making breaking changes, if there is consensus that the
changes are justified.
<!-- /contributing -->

<!-- license -->
## License
&nbsp;
Copyright &copy; 2018-2020 Arne Brasseur and Contributors
&nbsp;
Available under the terms of the Eclipse Public License 1.0, see LICENSE.txt
<!-- /license -->
