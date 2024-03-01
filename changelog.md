# Changelog
All notable changes to `cnj-tracing-backend-micro` will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [3.3.0] - 2024-03-01
### Added
- commit-stage builds produce Docker images for linux/amd64 and linux/arm64/v8 platforms now
- added JSON-B configuration class
### Changed
- upgraded Payara to version 6.2024.2
- simplified POM
- consolidated common dependencies
- consolidated common cloudtrain dependencies
- upgraded Java to version 21
- upgraded Maven plugins and dependencies
- consolidated POM with other showcases
- consolidated system tests with other showcases
- Docker images use Generational Z garbage collector by default
### Fixed
- improved test coverage measurement with Jacoco to include all coverage data in reports
- log messages of test runs are displayed correctly now after upgrade to SLF4J 2.0.9 and using ServiceLoader for log provider lookup
- application logs are actually written to console after (re-)adding dependency slf4j-jdk14

## [3.2.0] - 2023-11-14
### Added
- Tagging of git branch
### Changed
- Upgraded to helm-maven-plugin version 5.0.0
- Now a helm chart is packaged and pushed as an artifact during the commit-stage build
- Now the helm chart is pulled before deploying during the integration-test-stage build
- removed dependency on cnj-common-test-jakarta by switching to model based system tests
- added missing dependency on assertj for testing
- upgraded Payara version to 6.2023.10
- consolidated dependencies

## [3.1.0] - 2023-06-09
- upgraded postgres to version 15
- made persistence work with local hack
- added docker-compose.yml to run the showcase locally

## [3.0.0] - 2023-05-31
### Changed
- upgraded to Java 17
- upgraded to Jakarta EE 10
- upgraded to Payara 6.2023.5
- switched from OpenTracing to OpenTelemetry/MicroProfile Telemetry
- solved JPA mapping issue in EclipseLink with a local hack (not using cnj-common-persistence-jpa) anymore

## [2.1.1] - 2022-11-14
### Added
### Changed
- fixed broken helm configuration of postgres secret

## [2.1.0] - 2022-08-26
### Added
### Changed
- consolidated with other showcases

## [2.0.1] - 2022-03-04
### Added
### Changed
- fixed configuration of readiness and liveness probes in Helm chart
- fixed application startup failures due to racing condition during OpenTracing Tracer registration
- synchronized Helm Chart version with project revision

## [2.0.0] - 2022-03-04
### Added
### Changed
- first re-release after repository split
