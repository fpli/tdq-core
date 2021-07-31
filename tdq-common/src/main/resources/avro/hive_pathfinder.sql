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


select
    minute(to_timestamp(eventTimestamp/1000)) as t_minute, count(1) as cnt
from zhangjt_tdq_pathfinder
where dt=20210730 and hr='03'
group by 1;


select
    minute(to_timestamp((eventTimestamp-2208963600000000)/1000000)) as t_minute, count(1) as cnt
from ubi_w.stg_ubi_event_dump_w
where dt=20210730 and hr='03'
group by 1;

select
  cast(EVENT_TIMESTAMP as bigint) *1000,
  EVENT_TIMESTAMP,
  minute(EVENT_TIMESTAMP) as t_minute
from
  ubi_v.ubi_event a
where
  a.session_start_dt = '2021-07-30'
limit 10;

select
  t_minute,
  sum(p_cnt),
  sum(ttl_cnt)
from
(
    select
      minute(EVENT_TIMESTAMP) as t_minute,
      sum( case when a.page_id=2547208 then 1 else 0 end ) as p_cnt,
      count(1) as ttl_cnt
    from
      ubi_v.ubi_event a
    where
      a.session_start_dt = '2021-07-30'  and  hour(EVENT_TIMESTAMP)=3
      group by 1
    UNION
    select
      minute(EVENT_TIMESTAMP) as t_minute,
     sum( case when a.page_id=2547208 then 1 else 0 end ) as p_cnt,
      count(*) as ttl_cnt
    from
      ubi_v.ubi_event_skew a
    where
      a.session_start_dt = '2021-07-30'  and  hour(EVENT_TIMESTAMP)=3
      group by 1
)
group by 1
order by 1
;