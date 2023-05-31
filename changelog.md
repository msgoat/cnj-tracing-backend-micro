# Changelog
All notable changes to `cnj-tracing-backend-micro` will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
