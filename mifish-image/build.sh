#!/usr/bin/env bash

cd $(dirname "$0")

image=mifish.ruanyi.com/storage/jdk
tag=8_1.8_8

docker build --tag $image:$tag .
docker push $image:$tag