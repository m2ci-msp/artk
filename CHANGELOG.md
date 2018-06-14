ArTK Changelog
==============

[Unreleased]
------------

### Changed

- build with Gradle 4.8
- migrate testing from JUnit 4 with FEST-Assert to JUnit 5 Jupiter with AssertJ
- [all changes since v0.5]

### Removed

- support for Java 7

### Fixed

- Euler angle extraction

[v0.5] - 2016-11-23
-------------------

### Added

- optional head movement normalization
- JSON output format
- support for EST format (i.e., MOCHA-TIMIT, mngu0 datasets)
- snapshot artifacts hosted on OJO

### Changed

- migrate build from Maven to Gradle 3.1
- [all changes since v0.4.1]

### Removed

- support for Java 6

[v0.4.1] - 2015-01-22
---------------------

### Added

- optional smoothing for noisy data
- testing hosted on Travis CI

### Changed

- [all changes since v0.4.1]

[v0.4] - 2014-07-11
-------------------

### Added

- release artifacts hosted on Bintray

### Changed

- build with Maven 3.0.3, updated plugins
- [all changes since v0.3]

[v0.3] - 2014-03-31
-------------------

### Added

- support for Carstens AG501 files
- channels can be extracted individually

### Changed

- refactored classes
- [all changes since v0.2]

[v0.2] - 2014-03-23
-------------------

Initial release

[Unreleased]: https://github.com/m2ci-msp/artk
[all changes since v0.5]: https://github.com/m2ci-msp/artk/compare/v0.5...master
[v0.5]: https://github.com/m2ci-msp/artk/releases/tag/v0.5
[all changes since v0.4.1]: https://github.com/m2ci-msp/artk/compare/v0.4.1...v0.5
[v0.4.1]: https://github.com/m2ci-msp/artk/releases/tag/v0.4.1
[all changes since v0.4.1]: https://github.com/m2ci-msp/artk/compare/v0.4...v0.4.1
[v0.4]: https://github.com/m2ci-msp/artk/releases/tag/v0.4
[all changes since v0.3]: https://github.com/m2ci-msp/artk/compare/v0.3...v0.4
[v0.3]: https://github.com/m2ci-msp/artk/releases/tag/v0.3
[all changes since v0.2]: https://github.com/m2ci-msp/artk/compare/v0.2...v0.3
[v0.2]: https://github.com/m2ci-msp/artk/releases/tag/v0.2
