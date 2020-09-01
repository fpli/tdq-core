#!/usr/bin/env bash

CURRENT_VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec)

BUILD_NUMBER=${BUILD_NUMBER:-0}

NEW_VERSION=${CURRENT_VERSION/%-SNAPSHOT/.${BUILD_NUMBER}}

mvn versions:set -DnewVersion=${NEW_VERSION}