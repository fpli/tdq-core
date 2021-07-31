CREATE TABLE `zhangjt_tdq_pathfinder` (
    `ingestTime` BIGINT,
    `sojTimestamp` BIGINT,
    `eventTimestamp` BIGINT,
    `processTimestamp` BIGINT,
    `sojA` MAP<STRING, STRING>,
    `sojK` MAP<STRING, STRING>,
    `sojC` MAP<STRING, STRING>,
    `clientData` MAP<STRING, STRING>,
    `source` STRING,
    `dt` STRING,
    `hr` STRING
)
USING parquet
OPTIONS (
  `compression` 'snappy',
  `serialization.format` '1',
  path 'viewfs://apollo-rno/user/b_bis/tdq/raw-data/tdq.pre_prod.dump.pathfinder'
)
PARTITIONED BY (source, dt, hr);

MSCK REPAIR TABLE zhangjt_tdq_pathfinder;
refresh table zhangjt_tdq_pathfinder;
