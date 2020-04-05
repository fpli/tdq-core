CREATE TABLE `ubi_event_rt`(
    `guid` string, `sessionId` string, `sessionSkey` bigint, `seqnum` int, `sessionstartdt` string, `sojdatadt` string, `clickid` int, `siteid` int, `version` int, `pageid` int, `pagename` string, `refererhash` bigint,
    `eventtimestamp` string, `urlquerystring` string, `clientdata` string, `cookies` string, `applicationpayload` string, `webserver` string, `referrer` string, `userid` string, `itemid` bigint, `flags` string, `rdt` int,
    `regu` int, `sqr` string, `staticpagetype` int, `reservedforfuture` int, `eventattr` string, `currentimprid` bigint, `sourceimprid` bigint, `cobrand` int, `iframe` int, `agentinfo` string, `forwardfor` string,
    `clieantIP` string, `bitVal` int, `appid` int, `oldsessionskey` bigint, `hashcode` int, `partialValidPage` boolean, `sessionstarttime` bigint, `sessionendtime` bigint, `botFlags` array<int>, `icfBinary` bigint,
    `ingestTime` bigint, `generateTime` bigint, `eventCnt` bigint, `dataCenter` string, `sid` string, `eventCaptureTime` bigint, `requestCorrelationId` string, `pageFamily` string, `remoteIP` string, `appVersion` string,
    `eventFamily` string, `eventAction` string, `trafficSource` string, `osVersion` string, `deviceFamily` string, `deviceType` string, `browserVersion` string, `browserFamily` string, `osFamily` string, `enrichedOsVersion` string,
    `rlogid` string, `dt` string, `hr` string)
USING parquet
OPTIONS (
  `compression` 'snappy',
  `serialization.format` '1',
  path 'viewfs://apollo-rno/sys/soj/ubd/events'
)
PARTITIONED BY (dt, hr)
;