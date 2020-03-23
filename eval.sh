#!/bin/bash
#${artifactory_contextUrl}
sed -i "s|artifactory_contextUrl|test|g" build.gradle
cat build.gradle