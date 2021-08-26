select e.SESSION_START_DT,
 page_id,
e.site_id,
count(1) as tot_cnt
from  ubi_v.ubi_event e
where
(    --session_start_dt = '2021-08-16'
    --and hour(EVENT_TIMESTAMP) = '11'
     page_id = 4634 -- stores pages
    or ( page_id = 2499619 and upper(sojlib.soj_nvl(soj,'eactn')) = 'EXPC')  -- stores pages
    or  page_id in(2545226, 2048320, 2056805) -- user_profile
    or  page_id in(3911,2050449,2046732)  -- SOIPageview

    -- search soi
    or ( page_id IN (2351460, 2053742, 2047936, 2381081)
        AND sojlib.soj_url_decode_escapes(lower(sojlib.soj_nvl(e.soj, 'gf')), '%') like '%seller:specific%' )

    --20210702 added new
    or  page_id in(3418065, 3658866)
    or ( page_id in (3238419, 3238420) and (sojlib.soj_nvl(e.soj,'sid')='p3533390' or sojlib.soj_nvl(e.soj,'cp')='3418065'))
    --or ( page_id = 3658866 and sojlib.soj_nvl(e.soj,'sid')='p3533390')
    --or ( page_id = 2349624 and sojlib.soj_nvl(e.soj,'sid')='p3418065.m1686.l7400') --VI page excluded
    or ( page_id = 2356359 and sojlib.soj_nvl(e.soj,'cp')='3418065' and sojlib.soj_url_decode_escapes(sojlib.soj_nvl(lower(e.soj),'moduledtl'), '%') in ('mi:54476','82052'))
)
and e.rdt=0
and e.session_start_dt between '2021-08-15' and '2021-08-16'
group by 1,2,3;





(
    page_id = 4634
    or (page_id = 2499619 and upper(applicationPayload['eactn']) = 'EXPC')
    or page_id in(2545226, 2048320, 2056805)
    or page_id in(3911,2050449,2046732)

    or (page_id IN (2351460, 2053742, 2047936, 2381081)
        AND soj_url_decode_escapes(lower(applicationPayload['gf']), '%') like '%seller:specific%' )

    or page_id in(3418065, 3658866)
    or (page_id in (3238419, 3238420) and (applicationPayload['sid']='p3533390' or applicationPayload['cp']='3418065'))

    or (page_id = 2356359 and applicationPayload['cp']='3418065' and lower(soj_url_decode_escapes(applicationPayload('moduledtl'), '%')) in ('mi:54476','82052'))
) and rdt=0

----------------------------------------


select
e.SESSION_START_DT, page_id,e.site_id
,sum(case when default.is_decimal(sojlib.soj_nvl(lower(e.soj), 'soid'),18) then 1 else 0 end) as valid_store_owner_id_cnt
from  ubi_v.ubi_event e
where
e.session_start_dt between '2021-08-15' and '2021-08-16'
and  page_id in (4634, 2046732)
and e.rdt=0
group by 1,2,3;

sum(case when is_decimal(applicationPayload['soid'],18) then 1 else 0 end)
page_id in (4634, 2046732) and rdt=0


----------------------------------------
select								
e.session_start_dt,  page_id,e.site_id								
,sum(case when sojlib.soj_nvl(lower(e.soj), 'soid') is not null then 1 else 0 end) as valid_store_owner_id_cnt								
from  ubi_v.ubi_event e								
where								
-- page_id in (2499619, 3418065, 2356359)								
(								
	( page_id = 2499619 and upper(sojlib.soj_nvl(soj,'eactn')) = 'EXPC')  -- stores pages							
	or ( page_id = 2356359 and sojlib.soj_nvl(e.soj,'cp')='3418065' and sojlib.soj_url_decode_escapes(sojlib.soj_nvl(lower(e.soj),'moduledtl'), '%') in ('mi:54476','82052'))							
	or  page_id = 3418065							
)								
and e.rdt=0								
and session_start_dt between '2021-08-15' and '2021-08-16'								
group by 1,2,3;								



( (page_id = 2499619 and upper(applicationPayload['eactn']) = 'EXPC') or (page_id = 2356359 and applicationPayload['cp']='3418065' and  lower(soj_url_decode_escapes(applicationPayload('moduledtl'), '%')) in ('mi:54476','82052')) or page_id = 3418065 ) and rdt=0


----------------------------------------
Metric Name	URL Query String - Store Name Volume
Metric Defination	monitor valid store name volume from URL Query String
Metric Formula	num of valid store name volume from URL Query String
Dimension	page_id/site_id
Threshold
Metric Stage	4 - Post Processing
Metric Type	Completeness

Logic
with tb1 as (
select
e.session_start_dt, page_id,e.site_id
-- store name
,case when e.URL_QUERY_STRING like '/str/%'
then sojlib.soj_list_get_val_by_idx( sojlib.soj_get_url_path(concat('http://www.ebay.com', e.URL_QUERY_STRING)) , '/', 3)
when e.URL_QUERY_STRING like '/experience/shopping/%'
then sojlib.soj_url_extract_nvp(e.URL_QUERY_STRING, 'store_name',0)
when e.WEB_SERVER like '%stores%'
then sojlib.soj_list_get_val_by_idx( sojlib.soj_get_url_path(concat('http://www.ebay.com', e.URL_QUERY_STRING)) , '/', 2)
end as store_name
from  ubi_v.ubi_event e
where
-- page_id in (4634, 2499619, 3418065)
(  page_id in (4634, 3418065)
	or ( page_id = 2499619 and upper(sojlib.soj_nvl(soj,'eactn')) = 'EXPC')  -- stores pages
)
and e.rdt=0
and session_start_dt between '2021-08-15' and '2021-08-16'
)
select a.session_start_dt,a.page_id, a.site_id
,sum(case when nvl(a.store_name, '')<> '' then 1 else 0 end) as valid_store_name_cnt
from tb1 a
group by 1,2,3;



----------------------------------------

with tb1 as (
select
e.session_start_dt, page_id,e.site_id
,case when  page_id = 2048320 then COALESCE(sojlib.soj_url_decode_escapes(sojlib.soj_nvl(e.SOJ, 'sn'), '%'), sojlib.soj_url_extract_nvp( sojlib.soj_url_decode_escapes(sojlib.soj_get_url_params( concat('http://www.ebay.com',  URL_QUERY_STRING)), '%') ,  'sn',0))  -- native user profile
when  page_id = 2056805 then sojlib.soj_url_extract_nvp( sojlib.soj_url_decode_escapes(sojlib.soj_get_url_params( concat('http://www.ebay.com',  URL_QUERY_STRING)), '%') ,  'sid',0) -- mweb user profile
when  page_id IN (2351460, 2053742, 2047936, 2381081)
then coalesce(sojlib.soj_url_extract_nvp( sojlib.soj_url_decode_escapes(sojlib.soj_get_url_params( concat('http://www.ebay.com',  URL_QUERY_STRING)), '%') ,  'sid',0) ,
sojlib.soj_url_extract_nvp( sojlib.soj_url_decode_escapes(sojlib.soj_get_url_params( concat('http://www.ebay.com',  URL_QUERY_STRING)), '%') , '_ssn',0) ,
sojlib.soj_nvl(lower(soj), 'sn'))
when  page_id in (3238419, 3238420) then substr(sojlib.soj_url_decode_escapes(sojlib.soj_nvl(lower(e.soj),'folent'), '%'), 2) --remove '~' at 1st character
when  page_id = 3658866 then sojlib.soj_url_extract_nvp( sojlib.soj_url_decode_escapes(sojlib.soj_get_url_params( concat('http://www.ebay.com',  URL_QUERY_STRING)), '%') ,  '_ssn',0)
when e.URL_QUERY_STRING like '/sch/%' or
e.URL_QUERY_STRING like '/usr/%'
then sojlib.soj_list_get_val_by_idx( sojlib.soj_get_url_path(concat('http://www.ebay.com', e.URL_QUERY_STRING)), '/', 3) -- usr profile page
when  page_id = 1236 then sojlib.soj_str_between_str( sojlib.soj_get_url_params( concat('http://www.ebay.com', e.URL_QUERY_STRING)), 'userid=', '&')  -- soi
end as usr_slctd_id
from  ubi_v.ubi_event e
where
(
 page_id in(2545226, 2048320, 2046732, 3658866)
or ( page_id IN (2351460, 2047936, 2381081) AND sojlib.soj_url_decode_escapes(lower(sojlib.soj_nvl(e.soj, 'gf')), '%') like '%seller:specific%' )
or ( page_id in (3238419, 3238420) and (sojlib.soj_nvl(e.soj,'sid')='p3533390' or sojlib.soj_nvl(e.soj,'cp')='3418065'))
)
and e.rdt=0
and session_start_dt between '2021-08-15' and '2021-08-16'
)
select a.session_start_dt,a.page_id, a.site_id, sum(case when nvl(a.usr_slctd_id, '')<> '' then 1 else 0 end) as valid_usr_slctd_id_cnt
from tb1 a
group by 1,2,3;



case
    when page_id = 2048320 then COALESCE( soj_url_decode_escapes( applicationPayload['sn'], '%'),  soj_url_extract_nvp(  soj_url_decode_escapes( soj_get_url_params( concat('http://www.ebay.com',  urlQueryString)), '%') ,  'sn',0))
    when page_id = 2056805 then soj_url_extract_nvp(  soj_url_decode_escapes( soj_get_url_params(concat('http://www.ebay.com',  urlQueryString)), '%') ,  'sid',0)
    when page_id in (2351460, 2053742, 2047936, 2381081) then coalesce(soj_url_extract_nvp(soj_url_decode_escapes(soj_get_url_params(concat('http://www.ebay.com',  urlQueryString)), '%') ,  'sid',0) , soj_url_extract_nvp(  soj_url_decode_escapes( soj_get_url_params( concat('http://www.ebay.com',  urlQueryString)), '%') , '_ssn',0) , applicationPayload['sn'])
    when page_id in (3238419, 3238420) then substr(soj_url_decode_escapes( applicationPayload['folent'], '%'), 2)
    when page_id = 3658866 then soj_url_extract_nvp(soj_url_decode_escapes(soj_get_url_params( concat('http://www.ebay.com',  urlQueryString)), '%') ,  '_ssn',0)
    when urlQueryString like '/sch/%' or urlQueryString like '/usr/%' then soj_list_get_val_by_idx(soj_get_url_path(concat('http://www.ebay.com',  urlQueryString)), '/', 3)
    when page_id = 1236 then soj_str_between_str(  soj_get_url_params(concat('http://www.ebay.com',  urlQueryString)), 'userid=', '&')
end

sum(case when length(usr_slctd_id)>0 then 1 else 0 end)


----------------------------------------
with tb1 as (
select
e.session_start_dt,e.page_id,e.site_id
,sojlib.soj_list_last_element(sojlib.soj_get_url_path(e.REFERRER),'/') as item_id_in_referrer
from  ubi_v.ubi_event e
where
(    e.page_id in(4634, 2545226, 2056805, 2046732, 3418065, 3658866)
or (e.page_id = 2499619 and upper(sojlib.soj_nvl(soj,'eactn')) = 'EXPC')  -- stores pages
or (E.page_id = 2351460 AND sojlib.soj_url_decode_escapes(lower(sojlib.soj_nvl(e.soj, 'gf')), '%') like '%seller:specific%' )
)
and e.rdt=0
and e.session_start_dt between '2021-08-15' and '2021-08-16'
)
select a.session_start_dt, a.page_id, a.site_id
,sum(case when default.is_decimal(a.item_id_in_referrer,18) then 1 else 0 end) as valid_item_id_in_referrer_cnt
from tb1 a
group by 1,2,3;



----------------------------------------

with tb1 as (
select
e.session_start_dt,e.page_id,e.site_id
, sojlib.soj_list_get_val_by_idx( sojlib.SOJ_DECODE_BASE36_VEC(sojlib.soj_nvl(e.SOJ, 'itm')),',', 1) as first_item_id_in_soj
from  ubi_v.ubi_event e
where
(     e.page_id in(4634, 2046732, 3658866)
or (e.page_id = 2499619 and upper(sojlib.soj_nvl(soj,'eactn')) = 'EXPC')
or (E.PAGE_ID IN (2351460, 2053742, 2047936, 2381081) AND sojlib.soj_url_decode_escapes(lower(sojlib.soj_nvl(e.soj, 'gf')), '%') like '%seller:specific%' )
)
and e.rdt=0
and e.session_start_dt between '2021-08-15' and '2021-08-16'
)
select a.session_start_dt,a.page_id,a.site_id
,sum(case when default.is_decimal(a.first_item_id_in_soj,18) then 1 else 0 end) as valid_first_item_id_in_soj_cnt
from tb1 a
group by 1,2,3;






----------------------------------------





