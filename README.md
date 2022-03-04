# cnj-tracing-backend-micro

Cloud native MicroProfile backend with support of cluster logging using an EFK stack:

* everything is logged to stdout (no file appenders)
* uses JSON logging format

## Enable JSON logging in Payara Micro

In order to switch Payara Micro to JSON logging output format, you will have to proceed through the following steps:

### Add a custom logging.properties to the Docker image

Add a custom logging.properties file to your Docker image by copying it to the Payara home directory at __/home/payara__.
This custom logging.properties file should be a copy
of the original logging.properties file with the following modification:

```
java.util.logging.ConsoleHandler.formatter=fish.payara.enterprise.server.logging.JSONLogFormatter
```

This line attaches the `JSONLogFormatter`  to the `ConsoleHander` which renders each log entry as a JSON document.


### Activate the custom logging.properties during application start

Add the command line argument `--logProperties` to the command line arguments passed to Payara Micro.
The easiest way to do that is to specify the following envvar `PAYARA_ARGUMENTS` on container start:

```
PAYARA_ARGUMENTS=${PAYARA_ARGUMENTS} --logProperties /home/payara/logging.properties
```

Command line argument `--logProperties` overrides the default logging.properties file location with the given one. 

> The Docker image used in this showcase supports an additional envvar `PAYARA_LOGGING_FORMAT` which is set to __JSON__
> to add the `--logProperties` command line arguments to the default Payara command line arguments.

## Build this application 

``` 
mvn clean verify -P pre-commit-stage
```

Build results: a Docker image containing an Payara MicroProfile application.

## Exposed REST endpoints

Simply call the OpenAPI REST endpoint at `/openapi` to get an OpenAPI compliant API specification.

## Exposed environment variables

| Name | Required | Description |
| --- | --- | --- |
| PAYARA_LOGGING_FORMAT |  | Activates the JSON logging format, if set to __JSON__; uses the default logging configuration otherwise (default: __JSON__) | 
| MP_JWT_VERIFY_PUBLICKEY_LOCATION | x | REST endpoint of an OpenID Connect authentication provider returning the JWT key set |
| MP_JWT_VERIFY_ISSUER | x | ID of the JWT's issuer |
| CLOUDTRAIN_SERVICES_GRANTEDPERMISSIONS_MP_REST_URL | x | Base URL of downstream service |
| POSTGRES_DB_USER | x | PostgreSQL database user | 
| POSTGRES_DB_PASSWORD | x | PostgreSQL database user |
| POSTGRES_DB_NAME | x | PostgreSQL database name |
| POSTGRES_DB_HOST | x | PostgreSQL hostname |
| POSTGRES_DB_PORT | x | PostgreSQL port number |

## Exposed Ports

| Port | Protocol | Description |
| --- | --- | --- |
| 8080 | HTTP | HTTP endpoint of this application | 
 
## Version / Tags

| Tag(s) | Remarks |
| --- | --- |
| latest, 4.0.0 | first release |
