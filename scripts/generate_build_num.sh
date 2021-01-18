#!/usr/bin/env bash

CURRENT_VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec)

if [[ "${CURRENT_VERSION}" =~ .*"-SNAPSHOT"$ ]]; then
  BUILD_NUMBER=${BUILD_NUMBER:-0}
  NEW_VERSION=${CURRENT_VERSION/%-SNAPSHOT/.${BUILD_NUMBER}}
else
  NEW_VERSION=${CURRENT_VERSION}
fi

echo "Build version is ${NEW_VERSION}"
mvn versions:set -DnewVersion=${NEW_VERSION}
echo ${NEW_VERSION} > build_version_tmp.txt