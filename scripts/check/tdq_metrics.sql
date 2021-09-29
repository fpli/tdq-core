CREATE TABLE `tdq_metric_pre_prod` (
    `metricId` STRING,
    `metricName` STRING,
    `eventTime` BIGINT,
    `processTime` BIGINT,
    `tags` MAP<STRING, STRING>,
    `values` MAP<STRING, DOUBLE>,
    `cfg` STRING,
    `winId` STRING,
    `dt` STRING,
    `hr` STRING
)
USING parquet
OPTIONS (
  `compression` 'snappy',
  `serialization.format` '1',
  path 'hdfs://apollo-rno/user/b_bis/tdq/pre-prod/metric/normal'
)
PARTITIONED BY (cfg, winId, dt, hr);

MSCK REPAIR TABLE tdq_metric_pre_prod;
REFRESH     TABLE tdq_metric_pre_prod;
CACHE       TABLE tdq_metric_pre_prod;

-- Global Mandatory Tag - Item Rate
-- summary
SELECT
  1 as metricId,
  date_trunc('hour', to_timestamp(eventTime/1000)) AS eventTime,
  sum(VALUES.gmt_itm_cnt) / sum(VALUES.gmt_itm_total_cnt)
FROM
  tdq_metric_pre_prod
WHERE
  metricName = 'common_metric' and VALUES.gmt_itm_total_cnt > 0
    and to_timestamp(eventTime/1000) >= '2021-09-28 00:00:00'
    and to_timestamp(eventTime/1000) < '2021-09-28 12:00:00'
GROUP BY 1,2
ORDER BY 2 DESC
;
-- details
SELECT
  1 as metricId,
  date_trunc('hour', to_timestamp(eventTime/1000)) AS eventTime,
  tags.domain as domain,
  tags.site_id as site_id,
  sum(VALUES.gmt_itm_cnt)/sum(VALUES.gmt_itm_total_cnt)
FROM
  tdq_metric_pre_prod
WHERE
  metricName = 'common_metric' and VALUES.gmt_itm_total_cnt > 0
    and to_timestamp(eventTime/1000) >= '2021-09-28 00:00:00'
    and to_timestamp(eventTime/1000) < '2021-09-28 12:00:00'
GROUP BY 1,2,3,4
ORDER BY 2 DESC
;

-- Search Tag Rate - prof
-- summary
SELECT
  15 as metricId,
  date_trunc('hour', to_timestamp(eventTime/1000)) AS eventTime,
  sum(VALUES.st_prof_cnt)/sum(VALUES.st_total_cnt)
FROM
  tdq_metric_pre_prod
WHERE
  metricName = 'sojevent_common_metric' and VALUES.st_total_cnt > 0
    and to_timestamp(eventTime/1000) >= '2021-09-28 00:00:00'
    and to_timestamp(eventTime/1000) < '2021-09-28 12:00:00'
GROUP BY 1, 2
ORDER BY 2 DESC
;
-- details
SELECT
  15 as metricId,
  date_trunc('hour', to_timestamp(eventTime/1000)) AS eventTime,
  tags.page_id as page_id,
  tags.site_id as site_id,
  sum(VALUES.st_prof_cnt)/sum(VALUES.st_total_cnt)
FROM
  tdq_metric_pre_prod
WHERE
  metricName = 'sojevent_common_metric' and VALUES.st_total_cnt > 0
    and to_timestamp(eventTime/1000) >= '2021-09-28 00:00:00'
    and to_timestamp(eventTime/1000) < '2021-09-28 12:00:00'
GROUP BY 1, 2, 3, 4
ORDER BY 2 DESC
;