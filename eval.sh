#!/bin/bash

sed "\${artifactory_contextUrl|${artifactory_contextUrl}|g" build.gradle