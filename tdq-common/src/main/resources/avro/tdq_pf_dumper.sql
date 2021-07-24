CREATE TABLE `zhangjt_tdq_pf_dumper` (
    `ingestTime` BIGINT,
    `sojTimestamp` BIGINT,
    `eventTimestamp` BIGINT,
    `processTimestamp` BIGINT,
    `sojA` MAP<STRING, STRING>,
    `sojK` MAP<STRING, STRING>,
    `sojC` MAP<STRING, STRING>,
    `clientData` MAP<STRING, STRING>,
    `dt` STRING,
    `hr` STRING
)
USING parquet
OPTIONS (
  `compression` 'snappy',
  `serialization.format` '1',
  path 'viewfs://apollo-rno/user/b_bis/tdq/pre-prod/raw-data/'
)
PARTITIONED BY (dt, hr);

MSCK REPAIR TABLE zhangjt_tdq_pf_dumper;
refresh table zhangjt_tdq_pf_dumper;
