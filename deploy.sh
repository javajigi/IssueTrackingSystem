#!/bin/bash

./mvnw clean package docker:build -DdockerImageTags=1.0.2

docker-compose up --build -d
