#!/bin/bash
a="$(type -p java)"
if [ -z "$a" ]; then
    sudo apt-get install default-jdk
    sudo apt-get install default-jre
fi
a="$(type -p mvn)"
if [ -z "$a" ]; then
    sudo apt-get install maven
fi
mvn package
