# Onboard New Metric(Time Between EP Metadata Refresh and Qualification)


### [step 0: doc](https://docs.google.com/spreadsheets/d/1-LNEktUJwglgC3kIbPUbavofLYhLjkqoFS7l8rWqdDo/edit#gid=1244674125)
### step 1: add a metric in iDo metric_meta_info

http://phpmyadmin-svc-prod.ido-ns.svc.57.tess.io/tbl_sql.php?db=tdq&table=rhs_config

```sql
INSERT INTO `metric_meta_info` (`id`, `name`, `description`, `window_id`, `stage_id`, `operator_id`, `expressions`, `filters`, `threshold`, `threshold_type`, `unit`, `category_id`, `active_flag`, `status`, `tdq_config`, `detail_flag`, `mmd_flag`, `mmd_bound_flag`, `cre_user`, `cre_time`, `upd_user`, `upd_time`) VALUES (20,	'Time Between EP Metadata Refresh and Qualification',	'All services are expected to refresh the EP Metadata by querying the database every 15 minutes.  These services include domain services and EP Service.',	5,	2,	1,	'',	NULL,	96.00,	'<=',	'm',	4,	1,	2,	'{\"id\": \"ignore\", \"name\": \"ignore\", \"rules\": [{\"name\": \"ignore\", \"type\": \"realtime.rheos.profiler\", \"config\": {\"window\": \"1h\"}, \"profilers\": [{\"expr\": \"mrq_cnt\", \"config\": {\"pronto-filter\": \"expr.mrq_cnt > 0\", \"pronto-dropdown\": \"expr.mrq_cnt > 0\"}, \"filter\": \"mdbref_timestamp is not null\", \"dimensions\": [\"expt_flow_type\", \"soj_ec\", \"mdbref_age_group\"], \"metric-name\": \"ep_qa_nqt\", \"transformations\": [{\"expr\": \"soj_nvl(\'ec\')\", \"alias\": \"soj_ec\"}, {\"expr\": \"soj_nvl(\'eprlogid\')\", \"alias\": \"soj_eprlogid\"}, {\"expr\": \"soj_nvl(\'TPool\')\", \"alias\": \"tpool\"}, {\"expr\": \"to_timestamp(soj_parse_rlogid(soj_eprlogid, \'timestamp\'))\", \"alias\": \"qual_timestamp\"}, {\"expr\": \"case when tpool in (\'r1rover\', \'r1pulsgwy\', \'r1edgetrksvc\') THEN \'T\' else \'D\' end\", \"alias\": \"pool_type\"}, {\"expr\": \"case when pool_type = \'T\' and soj_ec in (\'4\', \'5\') then \'Native Client Side\' when soj_ec in (\'4\', \'5\') then \'Native Server Side\' else \'Native web, MWeb, DWeb\' end\", \"alias\": \"expt_flow_type\"}, {\"expr\": \"cast(cast(soj_nvl(\'mdbreftime\') AS LONG) / 1000 as long)\", \"alias\": \"mdbref_timestamp\"}, {\"expr\": \"unix_timestamp(qual_timestamp) - mdbref_timestamp\", \"alias\": \"mdbref_age\"}, {\"expr\": \"case when mdbref_age < 0 then \'Group: Error\' when mdbref_age <= 910 then \'<= 15 min 10 sec\' else \'Not meet target\' end\", \"alias\": \"mdbref_age_group\"}, {\"expr\": \"count(1)\", \"alias\": \"mrq_cnt\"}]}]}]}',	1,	0,	1,	'juntzhang',	'2021-08-18 20:20:28',	'juntzhang',	'2021-08-19 01:05:01');
```

### step 2: add metric config in job config

tdq.prod.pathfinder(new).json

### step3: reload

https://ido.dss.vip.ebay.com/tdq/dashboard


```
http://dssetl-svc-qa.ido-ns.svc.57.tess.io:8080/swagger-ui.html#/tdqv2

{
  "from": "20210801000000",
  "metricId": 31,
  "to": "20210827000000"
}
``` 

