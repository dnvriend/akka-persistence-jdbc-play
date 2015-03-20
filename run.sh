#!/bin/bash
docker rm -f $(docker ps -aq)
activator clean universal:packageZipTarball && docker-compose build
docker-compose up -d
docker-compose logs
