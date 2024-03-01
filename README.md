# cnj-tracing-backend-micro

Cloud native Java backend using OpenTelemetry via Jaeger based on Eclipse Microprofile.

The application is packaged as a multi-architecture docker image which supports the following platforms:
* linux/amd64
* linux/arm64/v8

## Synopsis

This showcase demonstrates
* how to enable exposure of telemetry data in OpenTelemetry format
* how to connect your application to an OpenTelemetry backend

Since OpenTelemetry superseded OpenTracing as a standard, 
MicroProfile does not provide an extra feature for OpenTelemetry support anymore.
All OpenTelemetry configuration is provided via MicroProfile Config compliant approaches
like properties files, system properties or environment variables.

For this showcase, `Jaeger` is used as an OpenTelemetry backend.

### Enable exposure of telemetry data

The OpenTelemetry feature must be enabled by setting configuration property `otel.sdk.disabled` to __false__.

### Connect your application to an OpenTelemetry backend

After enabling the OpenTelemetry feature, the OpenTelemetry endpoint the tracing data should be sent to must be configured
via configuration property `otel.exporter.otlp.endpoint`.

## Status

![Build status](https://codebuild.eu-west-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoid1A2M0cvdUg4YndHb0JQM1dkRUVCZmtZRnhIZ3VXYTg0TjBCRlFoUDkwUmtIY2J2aTltUDdCQW50RDl3MGFrVW90c1NiaG10M2pQTlFsL2hhWk9oUWJrPSIsIml2UGFyYW1ldGVyU3BlYyI6IldMUHpFTGsxZjNsOVFLNnEiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=main)

## Release information

Check [changelog](changelog.md) for latest version and release information.

## Docker Pull Command

`docker pull docker.cloudtrain.aws.msgoat.eu/cloudtrain/cnj-tracing-backend-micro`

## Helm Pull Command

`helm pull oci://docker.cloudtrain.aws.msgoat.eu/cloudtrain-charts/cnj-tracing-backend-micro`

## HOW-TO build this application locally

If all prerequisites are met, just run the following Maven command in the project folder:

```shell 
mvn clean verify -P pre-commit-stage
```

Build results: a Docker image containing the showcase application.

## HOW-TO run this showcase locally

In order to run the whole showcase locally, just run the following docker commands in the project folder:

```shell 
docker compose up -d
docker compose logs -f 
```
The showcase application will be accessible via `http://localhost:38080`.

The Jaeger UI will be available at `http://localhost:37080`.

Press `Ctlr+c` to stop tailing the container logs and run the following docker command to stop the show case:

```shell 
docker compose down
```
