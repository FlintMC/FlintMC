#!/bin/bash
#${artifactory_contextUrl}
sed -i "s|\${artifactory_contextUrl}|${artifactory_contextUrl}|g" build.gradle
cat build.gradle