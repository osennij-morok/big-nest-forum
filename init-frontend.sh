#!/usr/bin/env bash

grep "\S" .env | awk '{print "VITE_" $0}' > ./frontend/.env
