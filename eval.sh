#!/bin/bash
cat build.gradle
sed "'${artifactory_contextUrl}'|${artifactory_contextUrl}|g" build.gradle
cat build.gradle