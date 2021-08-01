CREATE TABLE `zhangjt_metrics` (
    `metricId` STRING,
    `metricName` STRING,
    `eventTime` BIGINT,
    `processTime` BIGINT,
    `tags` MAP<STRING, STRING>,
    `values` MAP<STRING, DOUBLE>,
    `dt` STRING,
    `hr` STRING
)
USING parquet
OPTIONS (
  `compression` 'snappy',
  `serialization.format` '1',
  path 'viewfs://apollo-rno/user/b_bis/tdq/metric/'
)
PARTITIONED BY (dt, hr);




MSCK REPAIR TABLE zhangjt_metrics;
refresh table zhangjt_metrics;

SELECT
  TO_TIMESTAMP(processTime / 1000) AS processTime,
  TO_TIMESTAMP(eventTime / 1000) AS eventTime,
  cast(
    VALUES.p1 AS long
  ) AS total_cnt
FROM
  zhangjt_metrics
WHERE
  metricName = 'global_cnt_by_1min'
  ORDER BY processTime DESC
;