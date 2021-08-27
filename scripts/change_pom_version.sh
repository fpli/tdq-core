#!/usr/bin/env bash

if [ "$#" -ne 1 ]; then
  echo "Missing arguments"
  echo "Usage: $0 <VERSION>"
  exit 1
fi

NEW_VERSION="$1-SNAPSHOT"
#NEW_VERSION="$1-RELEASE"

echo "Using maven version: $(mvn -v)"

mvn versions:set -DnewVersion=${NEW_VERSION} -q
