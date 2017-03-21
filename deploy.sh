#!/bin/bash

APP_NAME=almondchocolate

./mvnw clean package docker:build -DdockerImageTags=$APP_NAME-1.0.0

docker-compose up --build -d
