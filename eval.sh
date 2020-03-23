#!/bin/bash
cat build.gradle
sed "s|\${artifactory_contextUrl}|${artifactory_contextUrl}|g" build.gradle
cat build.gradle