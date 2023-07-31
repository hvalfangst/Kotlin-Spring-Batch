#!/bin/sh

# Exits immediately if a command exits with a non-zero status
set -e

# Run docker-compose down for wiping source database
docker-compose -f db/source/docker-compose.yml down

# Run docker-compose down for wiping target database
docker-compose -f db/target/docker-compose.yml down
