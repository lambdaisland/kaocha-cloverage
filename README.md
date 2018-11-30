# kaocha-cloverage

<!-- badges -->
[![CircleCI](https://circleci.com/gh/lambdaisland/kaocha-cloverage.svg?style=svg)](https://circleci.com/gh/lambdaisland/kaocha-cloverage) [![cljdoc badge](https://cljdoc.org/badge/lambdaisland/kaocha-cloverage)](https://cljdoc.org/d/lambdaisland/kaocha-cloverage) [![Clojars Project](https://img.shields.io/clojars/v/lambdaisland/kaocha-cloverage.svg)](https://clojars.org/lambdaisland/kaocha-cloverage) [![codecov](https://codecov.io/gh/lambdaisland/kaocha-cloverage/branch/master/graph/badge.svg)](https://codecov.io/gh/lambdaisland/kaocha-cloverage)
<!-- /badges -->

Kaocha plugin to get code coverage reports through [Cloverage](https://github.com/cloverage/cloverage).

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

<!-- license-epl -->
## License

Copyright &copy; 2018 Arne Brasseur

Available under the terms of the Eclipse Public License 1.0, see LICENSE.txt
<!-- /license-epl -->
