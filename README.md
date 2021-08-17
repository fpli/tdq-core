# TDQ
Tracking Data Quality

#### startup-mode config
```
"startup-mode": "LATEST",
"startup-mode": "TIMESTAMP", "from-timestamp": 1628067300000,
```
#### exception config
```
{
  "expr": "total_cnt",
  "config": {
    "sampling": false,
    "sampling-fraction": 0.005
  },
  "metric-name": "exception",
  "transformations": [
    {
      "expr": "count(1)",
      "alias": "total_cnt"
    }
  ]
},
```


#### sink config
* normal-metric
```
{
  "name": "pronto_sojevent_tdq_normal_metric",
  "type": "realtime.pronto",
  "config": {
    "tdq-type": "normal-metric",
    "index-pattern": "tdq.${flink.app.profile}.metric.normal."
  }
},
{
  "name": "hdfs_sojevent_tdq_normal_metric",
  "type": "realtime.hdfs",
  "config": {
    "tdq-type": "normal-metric",
    "hdfs-path": "hdfs://apollo-rno/user/b_bis/tdq/${flink.app.profile}/metric/normal"
  }
},
{
  "name": "console_sojevent_tdq_normal_metric",
  "type": "realtime.console",
  "config": {
    "tdq-type": "normal-metric",
    "std-name": "normal"
  }
},
```

#### dump pathfinder
```
{
  "name": "hdfs_pathfinder_dump",
  "type": "realtime.hdfs",
  "config": {
    "tdq-type": "dump-pathfinder",
    "hdfs-path": "hdfs://apollo-rno/user/b_bis/tdq/raw-data"
  }
}
```
#### dump sojevent
```
{
  "name": "hdfs_sojevent_dump",
  "type": "realtime.hdfs",
  "config": {
    "tdq-type": "dump-sojevent",
    "hdfs-path": "hdfs://apollo-rno/user/b_bis/tdq/raw-data"
  }
}
```

## profile config
--tdq-profile test|pre-prod|prod
--flink.app.profile test|pre-prod|prod

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
git clone git@github.corp.ebay.com:juntzhang/tdq-core.git
cd tdq-parent
mvn clean package
```