    select
      session_start_dt,
      hourly,
      sum(user_cnt),
      sum(ttl_cnt)
    from
      (
        select
          session_start_dt,
          hour(EVENT_TIMESTAMP) as hourly,
          sum(
            case
              when sojlib.soj_nvl(soj, 'u') is not null then 1
              else 0
            end
          ) as user_cnt,
          count(*) as ttl_cnt
        from
          ubi_v.ubi_event a
          join p_alfu_t.bbwoa_pages_with_itm b on a.page_id = b.page_id
        where
          a.session_start_dt in (
            '2021-07-20'
          )
          and sojlib.soj_nvl(CLIENT_DATA, 'RemoteIP') not like '10\.%'
          group by 1, 2
        UNION
        select
          session_start_dt,
          hour(EVENT_TIMESTAMP) as hourly,
          sum(
            case
              when sojlib.soj_nvl(soj, 'u') is not null then 1
              else 0
            end
          ) as user_cnt,
          count(*) as ttl_cnt
        from
          ubi_v.ubi_event_skew a
          join p_alfu_t.bbwoa_pages_with_itm b on a.page_id = b.page_id
        where
          a.session_start_dt in (
            '2021-07-20'
          )
          and sojlib.soj_nvl(CLIENT_DATA, 'RemoteIP') not like '10\.%'
          group by 1, 2
      )
      group by 1, 2
      order by 1, 2
    ;

,

