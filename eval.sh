#!/bin/bash

sed -i "s/\$\{artifactory_contextUrl\}/${artifactory_contextUrl}/g" build.gradle