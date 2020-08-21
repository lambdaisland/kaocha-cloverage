# Unreleased

## Added

## Fixed

## Changed

# 1.0.63 (2020-08-21 / 83f3706)

## Changed

- Bump Cloverage to 1.2.0

# 1.0.56 (2020-08-13 / 304162e)

## Changed

- Switch back to vanilla Cloverage, and bump it to 1.1.3. This version no longer
  contains an aot-compiled tools.reader.

# 1.0-45 (2020-03-29 / 8642129)

## Fixed

- Get rid of clojure.tools.reader warnings, by switching to a Cloverage version
  that does not include an AOT compiled clojure.tools.reader.

# 0.0-41 (2019-09-25 / 0f12673)

## Fixed

- Support code using tagged literals ([#6](https://github.com/lambdaisland/kaocha-cloverage/pull/6))

# 0.0-32 (2019-03-19 / 8da7e96)

## Added

- Allow passing excluded calls to Cloverage, see also [Cloverage#242](https://github.com/cloverage/cloverage/pull/242)

## Changed

- Upgrade Cloverage to version 1.1.1

# 0.0-22 (2018-12-10 / ee13a86)

## Fixed

- Don't rely on Cloverage to find namespaces, this fixes compatibility with Cucumber.

# 0.0-18 (2018-12-05 / 9c8c629)

## Fixed

- Allow configuring ns-regex and ns-exclude-regex from tests.edn ([#2](https://github.com/lambdaisland/kaocha-cloverage/pull/2))

# 0.0-6 (2018-11-15 / 8308b88)

## Fixed

- Correctly specify Cloverage as a dependency

# 0.0-3 (2018-11-15 / 80c323a)

## Changed

- Initial release