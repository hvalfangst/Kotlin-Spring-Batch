# Spring Batch ETL Job with multiple Flyway datasources

Jobs are triggered by Spring Boot HTTP endpoints via RestController

## Requirements

* x86-64
* JDK 17
* Docker

## Startup

The script "up" starts the application by executing the following:
```
1. docker-compose -f db/source/docker-compose.yml up -d
2. docker-compose -f db/target/docker-compose.yml up -d
3. mvn clean install
4. mvn spring-boot:run
```


## Shutdown

The script "down" wipes the source and target databases executing the following:
```
1. docker-compose -f db/source/docker-compose.yml down
2. docker-compose -f db/target/docker-compose.yml down
```


## HTTP Endpoints


