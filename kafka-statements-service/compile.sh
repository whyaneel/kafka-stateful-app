#!/bin/sh

cd kafka-statements-service
mvn clean install -DskipTests jib:dockerBuild
cd ..
