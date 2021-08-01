create or REPLACE TEMPORARY view tdq_pages as (
  select
    *
  from
    ACCESS_VIEWS.PAGES
  where
    PAGE_FMLY4_NAME in (
      'ASQ',
      'BID',
      'BIN',
      'WTCH',
      'OFFER',
      'BINFLOW',
      'BIDFLOW',
      'UNWTCH',
      'XO',
      'CART'
    )
    and FRAME_BASED_PAGE_YN_ID = 0
);

create or REPLACE TEMPORARY view tdq_pages_itm as (
  select
    DISTINCT a.*
  from
    tdq_pages a
    inner join BATCH_VIEWS.soj_page_tags b on a.page_id = b.page_id
    and b.soj_tag_name in ('itm_id', 'itm', 'itmid', 'litm', 'itmlist')
);
cache table tdq_pages_itm;


create or REPLACE TEMPORARY view tdq_result as (
    select
        t_minute,
        b.PAGE_FMLY4_NAME,
        a.site_id,
        count(*) as total_cnt,
        sum( case when applicationPayload.u is not null then 1 else 0 end ) as usr_cnt
    from (
        select
          minute(to_timestamp((eventTimestamp-2208963600000000)/1000000)) as t_minute,
          pageId as page_id,
          siteId as site_id,
          applicationPayload
        from ubi_w.stg_ubi_event_dump_w a
        where dt = '20210730' and hr in ('04') and clientData.RemoteIP not like '10\.%'
    ) a
    inner join tdq_pages_itm b on a.page_id = b.page_id
    group by 1, 2, 3
);

set spark.sql.shuffle.partitions = 1;
cache table tdq_result_cache select * from tdq_result cluster by t_minute;

select *,
    usr_cnt / total_cnt as usr_rate
from (
    select
        floor(t_minute/5)*5 as t_minute,
        sum(total_cnt) as total_cnt,
        sum(usr_cnt) as usr_cnt
    from tdq_result_cache
    group by 1
) t;