
[Q2 - Search Tags Doc](https://docs.google.com/spreadsheets/d/1-LNEktUJwglgC3kIbPUbavofLYhLjkqoFS7l8rWqdDo/edit#gid=1200924696)

```sql
select page_id, sum(cpnip_cnt)/sum(total_cnt)
from (
    select
        a.page_id,
        sum(case when sojlib.soj_nvl(soj,'cpnip') is not null then 1 else 0 end) as cpnip_cnt,
        count(*) as total_cnt
    from ubi_v.ubi_event a
    left join ACCESS_VIEWS.dw_soj_lkp_page b on a.page_id=b.page_id
    where session_start_dt = '2021-04-01'
    and ( page_id in (2047936,2054032,2053742,2045573) or ( page_id in (2351460,2381081) and sojlib.soj_nvl(soj,'eactn') is not null ) )
) group by 1
```



config
```json
{
  "id": "20", "name": "cfg_20", "rules": [
  {
    "name": "rule_20",
    "type": "realtime.rheos.profiler",
    "config": {"window": "1min"},
    "profilers": [
      {
        "metric-name": "sch_tags_cpnip_rate",
        "expression": {"operator": "Expr", "config": {"text": "cpnip_cnt / total_cnt"}},
        "dimensions": ["site_id", "page_id"],
        "filter": "page_id in (2047936,2054032,2053742,2045573) or (page_id in (2351460,2381081) and TAG_EXTRACT('eactn') is not null)",
        "transformations": [
          {
            "alias": "site_id",
            "expression": {"operator": "UDF", "config": {"text": "CAST(TAG_EXTRACT('t') AS INTEGER)"}}
          },
          {
            "alias": "page_id",
            "expression": {"operator": "UDF", "config": {"text": "CAST(TAG_EXTRACT('p') AS INTEGER)"}}
          },
          {"alias": "total_cnt", "expression": {"operator": "Count", "config": {"arg0": "1.0"}}},
          {
            "alias": "cpnip_cnt",
            "expression": {
              "operator": "Sum",
              "config": {"arg0": "case when TAG_EXTRACT('cpnip') is not null then 1.0 else 0.0 end"}
            }
          }
        ]
      }
    ]
  }
]
}
```

es timelion
```
.es(
index=tdq-metrics-prod-*,
timefield=event_time_fmt,
metric=sum:expr.cpnip_cnt,
q='metric_key.keyword :sch_tags_cpnip_rate').divide(.es(
index=tdq-metrics-prod-*,
timefield=event_time_fmt,
metric=sum:expr.total_cnt,
q='metric_key.keyword :sch_tags_cpnip_rate')).color(green).lines(fill=1,width=1).label('Mav Tag Rate').yaxis(units="custom::%",tickDecimals=2).precision(5).multiply(100)
```