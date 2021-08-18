select
	e.session_start_dt
	,e.expt_flow_type
	,e.soj_ec
	,e.mdbref_age_group
	,count(*) total_event
from (
	select
		e.session_start_dt
		,e.EVENT_TIMESTAMP
		,cast(sojlib.soj_nvl(e.soj,'ec') AS varchar(20)) soj_ec
		,cast(sojlib.soj_nvl(e.soj,'eprlogid') AS varchar(20)) soj_eprlogid
		,to_timestamp(sojlib.soj_parse_rlogid(soj_eprlogid, 'timestamp')) qual_timestamp --qualification timestamp
		,unix_timestamp(qual_timestamp) qual_unix_timestamp
		,cast(sojlib.soj_nvl(e.soj,'mdbreftime') AS varchar(20)) soj_mdbreftime --EP Metadata Last Refresh Timestamp in ms since epoch time,
		,round(soj_mdbreftime/1000) mdbref_timestamp
		,qual_unix_timestamp - mdbref_timestamp mdbref_age
		,case 1=1
			when mdbref_age < 0 then 'Group: Error'
			when mdbref_age <= 910 then '<= 15 min 10 sec'
			else 'Not meet target'
			--else 'Group 13: > 10 days'
			end mdbref_age_group
		,cast(sojlib.soj_nvl(e.client_data,'TPool') AS varchar(20)) TPool
		,case 1=1
			when cast(sojlib.soj_nvl(e.client_data,'TPool') AS varchar(20)) in ('r1rover', 'r1pulsgwy', 'r1edgetrksvc')
				THEN 'T' --tracking pool
			else 'D' --domain pool
			end pool_type
		,case 1=1
			when pool_type = 'T' and soj_ec in ('4', '5') -- experiment channel: 4: Android 5: iOS
				then 'Native Client Side' -- native app client side
			when soj_ec in ('4', '5')
				then 'Native Server Side' -- native app server side
			else 'Native Web, DWeb' -- native web, mobile web, desktop web
			end expt_flow_type --experiment flow type
	from UBI_V.ubi_event_noskew_1pct e
UNION
	select
		e.session_start_dt
		,e.EVENT_TIMESTAMP
		,cast(sojlib.soj_nvl(e.soj,'ec') AS varchar(20)) soj_ec
		,cast(sojlib.soj_nvl(e.soj,'eprlogid') AS varchar(20)) soj_eprlogid
		,to_timestamp(sojlib.soj_parse_rlogid(soj_eprlogid, 'timestamp')) qual_timestamp --qualification timestamp
		,unix_timestamp(qual_timestamp) qual_unix_timestamp
		,cast(sojlib.soj_nvl(e.soj,'mdbreftime') AS varchar(20)) soj_mdbreftime --EP Metadata Last Refresh Timestamp in ms since epoch time,
		,round(soj_mdbreftime/1000) mdbref_timestamp
		,qual_unix_timestamp - mdbref_timestamp mdbref_age
		,case 1=1
			when mdbref_age < 0 then 'Group: Error'
			when mdbref_age <= 910 then '<= 15 min 10 sec'
			else 'Not meet target'
			--else 'Group 13: > 10 days'
			end mdbref_age_group
		,cast(sojlib.soj_nvl(e.client_data,'TPool') AS varchar(20)) TPool
		,case 1=1
			when cast(sojlib.soj_nvl(e.client_data,'TPool') AS varchar(20)) in ('r1rover', 'r1pulsgwy', 'r1edgetrksvc')
				THEN 'T' --tracking pool
			else 'D' --domain pool
			end pool_type
		,case 1=1
			when pool_type = 'T' and soj_ec in ('4', '5') -- experiment channel: 4: Android 5: iOS
				then 'Native Client Side' -- native app client side
			when soj_ec in ('4', '5')
				then 'Native Server Side' -- native app server side
			else 'Native Web, DWeb' -- native web, mobile web, desktop web
			end expt_flow_type --experiment flow type
	from UBI_V.ubi_event_skew_1pct e
) e
WHERE
	e.session_start_dt = '2021-07-30'
	and qual_timestamp is not null
	and mdbref_timestamp is not null
group by
	e.session_start_dt
	,e.soj_ec
	,e.expt_flow_type
	,e.mdbref_age_group
order by
	e.session_start_dt
	,e.expt_flow_type
	,e.soj_ec
	,e.mdbref_age_group
