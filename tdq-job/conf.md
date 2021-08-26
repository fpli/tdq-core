# TDQ config
## env
* --tdq-profile test|pre-prod|prod
* --flink.app.profile test|pre-prod|prod

## source
### kafka startup-mode
```
"startup-mode": "LATEST",
"startup-mode": "TIMESTAMP", "from-timestamp": 1628067300000,
```
## sink
### normal-metric
```
{
  "name": "pronto_sojevent_tdq_normal_metric",
  "type": "realtime.pronto",
  "config": {
    "sub-type": "normal-metric",
    "index-pattern": "tdq.${flink.app.profile}.metric.normal."
  }
},
{
  "name": "hdfs_sojevent_tdq_normal_metric",
  "type": "realtime.hdfs",
  "config": {
    "sub-type": "normal-metric",
    "hdfs-path": "hdfs://apollo-rno/user/b_bis/tdq/${flink.app.profile}/metric/normal"
  }
},
{
  "name": "console_sojevent_tdq_normal_metric",
  "type": "realtime.console",
  "config": {
    "sub-type": "normal-metric",
    "std-name": "normal"
  }
},
```
### dump pathfinder
```
{
  "name": "hdfs_pathfinder_dump",
  "type": "realtime.hdfs",
  "config": {
    "sub-type": "dump-pathfinder",
    "hdfs-path": "hdfs://apollo-rno/user/b_bis/tdq/raw-data"
  }
}
```
### dump sojevent
```
{
  "name": "hdfs_sojevent_dump",
  "type": "realtime.hdfs",
  "config": {
    "sub-type": "dump-sojevent",
    "hdfs-path": "hdfs://apollo-rno/user/b_bis/tdq/raw-data"
  }
}
```

## rule
### test exception
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


{
  "expr": "total_cnt",
  "metric-name": "exception",
  "transformations": [
    {
      "expr": "count(1)",
      "alias": "total_cnt"
    }
  ]
}
```