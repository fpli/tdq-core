#!/usr/bin/env bash

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <MODULE_NAME>"
  exit 1
fi

MODULE=$1

if [[ -e "${MODULE}/pom.xml" ]]; then
    echo "==================== Uploading jar to Rheos Portal ===================="
#    mvn -f ${MODULE}/pom.xml job-uploader:upload -Dusername=9c60ad5782194b0e8f91ac8607470985 -Dpassword=J51qQChoO6gQXTyM4a05phJuskjkyzXRkM5Dem4kuaCQVPmIVIzGwE1q71MJqPF5 -Dnamespace=sojourner-ubd
    mvn -f ${MODULE}/pom.xml job-uploader:upload -Dusername=cc9def8e931a4eb7be3e1c98a2fb9fea -Dpassword=3lpCS36s3VROAkAe4vOSCbCkLc7tKVZqlJOQ70uxvJTydCZ4LjAzpzo1gWzvuetb -Dnamespace=tdq
else
  echo "Cannot find module ${MODULE}"
  exit 1
fi