#!/bin/bash
#${artifactory_contextUrl}
sed "s|artifactory_contextUrl|test|g" build.gradle
cat build.gradle