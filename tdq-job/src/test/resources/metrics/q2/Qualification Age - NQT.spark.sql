select
  e.session_start_dt,
  e.expt_flow_type,
  case
    when e.soj_ec = 1 then 'Web'
    when e.soj_ec = 2 then 'Mobile Web'
    when e.soj_ec = 4 then 'Android'
    when e.soj_ec = 5 then 'iOS'
    when e.soj_ec = 6 then 'Email'
    else 'Unknown'
  end as Channel,
  case
    when e.expt_flow_type in ('Native Client Side') then '< 12 hours'
    else '< 10 second'
  end as qual_age_target,
  sum(
    case
      when e.expt_flow_type in ('Native Client Side')
      and qual_age <= 3600 * 12 then 1
      when e.expt_flow_type in ('Native web, MWeb, DWeb', 'Native Server Side')
      and qual_age <= 10 then 1
      else 0
    end
  ) as qualified_events_cnt,
  count(*) total_event,
  cast(
    100.00 * qualified_events_cnt / total_event as decimal(18, 2)
  ) as Meeting_Target_Rate
from
  (
    select
      e.session_start_dt,
      e.EVENT_TIMESTAMP,
      cast(sojlib.soj_nvl(e.soj, 'ec') AS varchar(20)) soj_ec,
      cast(sojlib.soj_nvl(e.soj, 'eprlogid') AS varchar(20)) soj_eprlogid,
      to_timestamp(
        sojlib.soj_parse_rlogid(soj_eprlogid, 'timestamp')
      ) qual_timestamp,
      unix_timestamp(e.EVENT_TIMESTAMP) event_unix_timestamp,
      unix_timestamp(qual_timestamp) qual_unit_timestamp,
      event_unix_timestamp - qual_unit_timestamp qual_age,
      case
        1 = 1
        when qual_age < 0 then 'Group: Error'
        when qual_age <= 1 then 'Group  1: 1 sec'
        when qual_age <= 2 then 'Group  2: 2 sec'
        when qual_age <= 10 then 'Group  3: 10 sec'
        when qual_age <= 60 then 'Group  4: 1 min'
        when qual_age <= 900 then 'Group  5: 15 min'
        when qual_age <= 3600 then 'Group  6: 1 hr'
        when qual_age <= 3600 * 12 then 'Group  7: 12 hr'
        when qual_age <= 86400 then 'Group  8: 1 day'
        when qual_age <= 86400 * 2 then 'Group  9: 2 days'
        when qual_age <= 86400 * 10 then 'Group 10: 10 days'
        else 'Group 11: > 10 days'
      end qual_age_group,
      cast(
        sojlib.soj_nvl(e.client_data, 'TPool') AS varchar(20)
      ) TPool,
      case
        1 = 1
        when cast(
          sojlib.soj_nvl(e.client_data, 'TPool') AS varchar(20)
        ) in ('r1rover', 'r1pulsgwy', 'r1edgetrksvc') THEN 'T'
        else 'D'
      end pool_type,
      case
        1 = 1
        when pool_type = 'T'
        and soj_ec in ('4', '5') then 'Native Client Side'
        when soj_ec in ('4', '5') then 'Native Server Side'
        else 'Native web, MWeb, DWeb'
      end expt_flow_type
    from
      UBI_V.ubi_event_noskew_1pct e
  ) e
WHERE
  e.session_start_dt = '2021-04-14'
  and e.qual_timestamp is not null
  group by 1, 2, 3, 4
  order by 1, 2, 3, 4