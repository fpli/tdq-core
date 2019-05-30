# sojourner-ubd
Sojourner Unified Bot Detection

## Features
* A shared library for key logic
* A Flink application to do real time bot detection
* A Spark application to do post processing
* A rule management tool

## Development
Requirements:

* Java 8
* IDE (Intellij IDEA recommended)

Build from source:

```
git clone git@github.corp.ebay.com:DSS-COREINSIGHTS/sojourner-ubd.git
cd sojourner-ubd
mvn clean package -DskipTests
```

Prepare dirs and configs:

```
mkdir -p /opt/sojourner-ubd
mkdir /opt/sojourner-ubd/conf
mkdir /opt/sojourner-ubd/data
mkdir /opt/sojourner-ubd/logs
cp -R <sojourner-ubd>/common/src/main/resources/* /opt/sojourner-ubd/conf
```

Now you can run and debug applications in IDE. E.g. to run real time pipeline, you run
`com.ebay.sojourner.ubd.rt.pipeline.SojournerUBDRTJob`

## Run on a Local Flink Cluster
Start a local Flink cluster:

```
<FLINK_HOME>/bin/start-cluster.sh
```

Run `sojourner-ubd-rt-pipeline`:

```
<FLINK_HOME>/bin/flink run -c com.ebay.sojourner.ubd.rt.pipeline.SojournerUBDRTJob <sojourner-ubd>/rt-pipeline/target/sojourner-ubd-rt-pipeline-0.1-SNAPSHOT.jar
```
