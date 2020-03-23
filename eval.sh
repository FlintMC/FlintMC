#!/bin/bash
#insert ci variables in files before gradle build process
sed -i "s|\${artifactory_contextUrl}|${artifactory_contextUrl}|g" build.gradle
cat build.gradle