#!/bin/bash
#insert ci variables in files before gradle build process
sed -i "s|\${artifactory_contextUrl}|${artifactory_contextUrl}|g" build.gradle
sed -i "s|\${artifactory_user}|${artifactory_user}|g" build.gradle
sed -i "s|\${artifactory_password}|${artifactory_password}|g" build.gradle

sed -i "s|\${artifactory_contextUrl}|${artifactory_contextUrl}|g" settings.gradle
sed -i "s|\${artifactory_user}|${artifactory_user}|g" settings.gradle
sed -i "s|\${artifactory_password}|${artifactory_password}|g" settings.gradle