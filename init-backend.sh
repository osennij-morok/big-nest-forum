#!/usr/bin/env bash

cp .env ./backend/.env
cd ./backend
./gradlew clean build
cd -

