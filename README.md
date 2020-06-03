# sojourner
Sojourner Unified Bot Detection

## Features
* A common library for core business and bot logic
* A Flink application to do real time ETL and bot detection
* A Spark application to do post processing
* A rule management tool
* SQL-based DSL for defining rules

## Development
Requirements:

* Java 8
* IDE (Intellij IDEA recommended)
* IDEA plugins (Scala, Lombok)

Build from source:

```
git clone git@github.corp.ebay.com:sojourner/sojourner.git
cd sojourner
mvn clean package -DskipTests
```


You can run or debug applications in IDE. E.g. to run real time pipeline, you run
`com.ebay.sojourner.rt.pipeline.SojournerRTJob`

## Run on a Local Flink Cluster
Start a local Flink cluster:

```
<FLINK_HOME>/bin/start-cluster.sh
```

Run `sojourner-rt-pipeline`:

```
<FLINK_HOME>/bin/flink run -c com.ebay.sojourner.rt.pipeline.SojournerRTJobForQA <sojourner>/rt-pipeline/target/sojourner-rt-pipeline-0.1-SNAPSHOT.jar
```
