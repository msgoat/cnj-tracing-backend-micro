# cnj-tracing-backend-micro

Cloud native Java backend using OpenTelemetry/MicroProfile Telemetry via Jaeger based on Eclipse Microprofile.

## Status

![Build status](https://codebuild.eu-west-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoid1A2M0cvdUg4YndHb0JQM1dkRUVCZmtZRnhIZ3VXYTg0TjBCRlFoUDkwUmtIY2J2aTltUDdCQW50RDl3MGFrVW90c1NiaG10M2pQTlFsL2hhWk9oUWJrPSIsIml2UGFyYW1ldGVyU3BlYyI6IldMUHpFTGsxZjNsOVFLNnEiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=main)

## Release information

Check [changelog](changelog.md) for latest version and release information.


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
