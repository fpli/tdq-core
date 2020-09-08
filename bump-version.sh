#!/usr/bin/env bash

get_current_version() {
  CURRENT_VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec)
  echo "Current version is: ${CURRENT_VERSION}"
}

case "$1" in
  major)
    get_current_version
    version=${CURRENT_VERSION%-SNAPSHOT}
    arr=(${version//./ })
    major=${arr[0]}
    updated_major=$((${major}+1))
    new_version="${updated_major}.0.0-SNAPSHOT"
    mvn versions:set -q -DnewVersion=${new_version} > /dev/null
    echo "New version is: ${new_version}"
    ;;

  minor)
    get_current_version
    version=${CURRENT_VERSION%-SNAPSHOT}
    arr=(${version//./ })
    minor=${arr[1]}
    updated_minor=$((${minor}+1))
    new_version="${arr[0]}.${updated_minor}.0-SNAPSHOT"
    mvn versions:set -q -DnewVersion=${new_version} > /dev/null
    echo "New version is: ${new_version}"
    ;;

  patch)
    get_current_version
    version=${CURRENT_VERSION%-SNAPSHOT}
    arr=(${version//./ })
    patch=${arr[2]}
    updated_patch=$((${patch}+1))
    new_version="${arr[0]}.${arr[1]}.${updated_patch}-SNAPSHOT"
    mvn versions:set -q -DnewVersion=${new_version} > /dev/null
    echo "New version is: ${new_version}"
    ;;

  *)
    echo "Usage: $0 {major|minor|patch}"
    ;;
esac

# commit and push
# git commit -am "Bump version to ${new_version} [skip-ci]" > /dev/null