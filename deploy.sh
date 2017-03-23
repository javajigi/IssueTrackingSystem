#!/bin/bash

APP_NAME=its
COMMIT_HASH="$(git show-ref --head | grep -h HEAD | cut -d':' -f2 | head -n 1 | head -c 10)"

./mvnw clean package docker:build -DdockerImageTags=$APP_NAME-$COMMIT_HASH

docker-compose up --build -d
