#!/bin/sh

cd local

# Docker compose down
docker-compose down --remove-orphans --volumes

cd ../exchange-rate-service
echo "exchange-rate-service-mock is shutting down"
docker-compose down -v --remove-orphans
