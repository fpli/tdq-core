# TDQ
Tracking Data Quality

## Wiki
- https://wiki.vip.corp.ebay.com/display/TDQ/TDQ+Self+define+Metric+Architecture
- https://wiki.vip.corp.ebay.com/display/TDQ/TDQ+Env+Info

## Modules
* common(soj common need deprecated)
* flink-lib(soj lib need deprecated)

* tdq-common
  * tdq config pojo
  * tdq utils
  * tdq entity model 
  
* tdq-planner
  * soj udf
  * tdq expressions(arithmetic,math,predicates,string)
  * physical plan
  
* tdq-job
  * flink pipeline 
  * [sinks/rule/sources](./tdq-job/conf.md)
  * flink functions
  * kafka schema
  * [sojevent config example](./tdq-job/src/test/resources/metrics/sojevent_tdq/tdq.pre_prod.sojevent_tdq.json)
  * [pathfinder config example](./tdq-job/src/test/resources/metrics/pathfinder/tdq.pre_prod.pathfinder.json)
  
* tdq-svc-lib
  * profiler query service
  
* tdq-example
  * example for tdq-svc-lib

## Features
* Support dynamic kafka source
* SQL-based DSL for defining rules
* Support sink types like kafka pronto

## Development
Requirements:

* Java 8
* IDE (Intellij IDEA recommended)
* IDEA plugins (Scala, Lombok)

Build from source
```
git clone git@github.corp.ebay.com:juntzhang/tdq-core.git
cd tdq-parent
mvn clean package
```
