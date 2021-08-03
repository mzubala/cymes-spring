#!/bin/bash

./mvnw package
docker-compose up --build --remove-orphans