SELECT pageFamilies
     , TUMBLE_START(eventTimestamp, INTERVAL '1' HOUR)       as START_TIME
     , sum(case when tags.itm is null then 1 else 0 end)     as TAG_ITM_MISS_COUNT
     , sum(case when tags.itmid is null then 1 else 0 end)   as TAG_ITMID_MISS_COUNT
     , sum(case when tags.itm_id is null then 1 else 0 end)  as TAG_ITM_ID_MISS_COUNT
     , sum(case when tags.itmlist is null then 1 else 0 end) as TAG_ITMLIST_MISS_COUNT
     , sum(case when tags.litm is null then 1 else 0 end)    as TAG_LITM_MISS_COUNT
     , sum(case when tags.u is null then 1 else 0 end)       as TAG_U_MISS_COUNT
FROM source1
WHERE pageFamilies IN ('ASQ', 'BID', 'BIDFLOW', 'BIN', 'BINFLOW', 'CART', 'OFFER', 'UNWTCH', 'VI', 'WTCH', 'XO')
group by TUMBLE(eventTimestamp, INTERVAL '1' HOUR), pageFamilies
;

SELECT TUMBLE_START(eventTimestamp, INTERVAL '1' HOUR) as START_TIME
     , sum(cast(tags.TDuration as DOUBLE)) as EVENT_CAPTURE_PUBLISH_LATENCY
FROM source1
group by TUMBLE(eventTimestamp, INTERVAL '1' HOUR)
;

SELECT pageIds
     , TUMBLE_START(eventTimestamp, INTERVAL '1' HOUR) as START_TIME
     , count(*) as MARKETING_EVENT_VOLUME
FROM source1
where pageIds in (2547208, 2483445)
group by TUMBLE(eventTimestamp, INTERVAL '1' HOUR), pageIds
;

SELECT TUMBLE_START(eventTimestamp, INTERVAL '1' HOUR) as START_TIME
     , sum(case when isInteger(tags.u) then 0 else 1 end) as TRANSFORMATION_ERROR_COUNT
FROM source1
group by TUMBLE(eventTimestamp, INTERVAL '1' HOUR)
;

/*
here are some problem:
  source1 需要重复消费4次, if the topic is very large like soj, there will be some waste of rheos resources
  数据量太大可能需要预聚合需要先通过GUID聚合 for data skew
  用户需要掌握flink sql 语法
  pre define some UDF like isInteger
*/

create view v1 as
select TUMBLE_START(eventTimestamp, INTERVAL '1' HOUR) as START_TIME

     ,sum(case when isInteger(tags.u) then 0 else 1 end) as TRANSFORMATION_ERROR_COUNT

     ,count(1) as map(pageIds,MARKETING_EVENT_VOLUME) where pageIds in (2547208, 2483445) group by pageIds

     ,sum(cast(tags.TDuration as DOUBLE)) as EVENT_CAPTURE_PUBLISH_LATENCY

         ,sum(case when tags.itm is null then 1 else 0 end)     as map(pageFamilies,TAG_ITM_MISS_COUNT)     WHERE pageFamilies IN ('ASQ', 'BID', 'BIDFLOW', 'BIN', 'BINFLOW', 'CART', 'OFFER', 'UNWTCH', 'VI', 'WTCH', 'XO') GROUP BY pageFamilies
         ,sum(case when tags.itmid is null then 1 else 0 end)   as map(pageFamilies,TAG_ITMID_MISS_COUNT)   WHERE pageFamilies IN ('ASQ', 'BID', 'BIDFLOW', 'BIN', 'BINFLOW', 'CART', 'OFFER', 'UNWTCH', 'VI', 'WTCH', 'XO') GROUP BY pageFamilies
         ,sum(case when tags.itm_id is null then 1 else 0 end)  as map(pageFamilies,TAG_ITM_ID_MISS_COUNT)  WHERE pageFamilies IN ('ASQ', 'BID', 'BIDFLOW', 'BIN', 'BINFLOW', 'CART', 'OFFER', 'UNWTCH', 'VI', 'WTCH', 'XO') GROUP BY pageFamilies
         ,sum(case when tags.itmlist is null then 1 else 0 end) as map(pageFamilies,TAG_ITMLIST_MISS_COUNT) WHERE pageFamilies IN ('ASQ', 'BID', 'BIDFLOW', 'BIN', 'BINFLOW', 'CART', 'OFFER', 'UNWTCH', 'VI', 'WTCH', 'XO') GROUP BY pageFamilies
         ,sum(case when tags.litm is null then 1 else 0 end)    as map(pageFamilies,TAG_LITM_MISS_COUNT)    WHERE pageFamilies IN ('ASQ', 'BID', 'BIDFLOW', 'BIN', 'BINFLOW', 'CART', 'OFFER', 'UNWTCH', 'VI', 'WTCH', 'XO') GROUP BY pageFamilies
         ,sum(case when tags.u is null then 1 else 0 end)       as map(pageFamilies,TAG_U_MISS_COUNT)       WHERE pageFamilies IN ('ASQ', 'BID', 'BIDFLOW', 'BIN', 'BINFLOW', 'CART', 'OFFER', 'UNWTCH', 'VI', 'WTCH', 'XO') GROUP BY pageFamilies

FROM source1
group by TUMBLE(eventTimestamp, INTERVAL '5' MINUTE), guid
;

create view v2 as
select
      TUMBLE_START(eventTimestamp, INTERVAL '1' HOUR) as START_TIME
    , sum(TRANSFORMATION_ERROR_COUNT)
    , sum(map(pageIds,MARKETING_EVENT_VOLUME))
    , sum(EVENT_CAPTURE_PUBLISH_LATENCY)
    , sum(map(pageFamilies,TAG_ITM_MISS_COUNT))
    , sum(map(pageFamilies,TAG_ITMID_MISS_COUNT))
    , sum(map(pageFamilies,TAG_ITM_ID_MISS_COUNT))
    , sum(map(pageFamilies,TAG_ITMLIST_MISS_COUNT))
    , sum(map(pageFamilies,TAG_LITM_MISS_COUNT))
    , sum(map(pageFamilies,TAG_U_MISS_COUNT))
from v1
group by TUMBLE(eventTimestamp, INTERVAL '1' HOUR)
;

select TUMBLE_START(eventTimestamp, INTERVAL '1' DAY) as START_TIME
     , sum(TRANSFORMATION_ERROR_COUNT)
from v2
group by TUMBLE(eventTimestamp, INTERVAL '1' DAY)




select count(1) as itm_missing_cnt
from src
where TAG_EXTRACT('itm|itmid|itm_id|itmlist|litm') IS NULL
group by page_family

select count(1) as itm_valid_cnt
from src
where TAG_EXTRACT('itm|itmid|itm_id|itmlist|litm') RLIKE '^(\d+(%2C)?)+$'
group by page_family

itm_valid_cnt must be AggregateFunction
itm_missing_cnt must be AggregateFunction

SUM(field) field can be UnaryExpression
COUNT

select itm_valid_cnt/itm_missing_cnt as global_mandatory_tag_item_rate
from src2
group by page_family, TUMBLE(eventTimestamp, INTERVAL '1' HOUR)

select sum(CAST(TAG_EXTRACT('TDuration') AS DOUBLE)) as event_capture_publish_latency
from src
where page_id in ('2547208', '2483445')
group by page_id, TUMBLE(eventTimestamp, INTERVAL '5' MINUTE)

final_metric:
event_capture_publish_latency{page_id='2547208',timestamp='2021-03-30 18:05'} 1231.1
event_capture_publish_latency{page_id='2483445',timestamp='2021-03-30 18:05'} 231.3
global_mandatory_tag_item_rate{page_family='BID',timestamp='2021-03-30 18:00'} 0.712
global_mandatory_tag_item_rate{page_family='BIDFLOW',timestamp='2021-03-30 18:00'} 0.812
global_mandatory_tag_item_rate{page_family='OFFER',timestamp='2021-03-30 18:00'} 0.812

middle_expression:
global_mandatory_tag_item_rate{page_family='BID',timestamp='2021-03-30 18:00', expr_id='agg1'} 712
global_mandatory_tag_item_rate{page_family='BID',timestamp='2021-03-30 18:00', expr_id='agg2'} 1000
global_mandatory_tag_item_rate{page_family='BID',timestamp='2021-03-30 18:00', expr_id='agg3'} 1000


