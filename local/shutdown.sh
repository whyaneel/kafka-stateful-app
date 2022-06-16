#!/bin/sh

cd local

# Docker compose down
docker-compose down --remove-orphans --volumes
