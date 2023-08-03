# HTTP triggered Spring Batch ETL Job using Tasklet

## Overview

The main purpose of this application is to perform data migration and aggregation from a source database to a target database using Spring Batch. 
It follows the ETL pattern, where data is extracted from the source database, transformed, and loaded into the target database. 
The application provides an easy-to-use RESTful API to trigger the ETL job.

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


## Usage

The application exposes an HTTP endpoint for triggering the ETL job. 
You can use any REST client, like Postman, to make a POST request to the /batch/start endpoint with the target date as a parameter.

## Endpoint details

Endpoint: POST /batch/start

Parameters:

    targetDate: The date for which the ETL job should be executed. It should be in the format 'yyyy-MM-dd'.

Response: The endpoint will respond with a 200 status code if the job succeeded and a 400 status code if the job failed.

## Shutdown

The script "down" wipes the source and target databases executing the following:
```
1. docker-compose -f db/source/docker-compose.yml down
2. docker-compose -f db/target/docker-compose.yml down
```


## Postman Collection

The repository includes a Postman collection in the 'postman' directory.
