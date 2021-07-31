CREATE TABLE `zhangjt_tdq_sojevent` (
    `guid` STRING,
    `sessionId` STRING,
    `sessionSkey` BIGINT,
    `seqNum` STRING,
    `sessionStartDt` BIGINT, `sojDataDt` BIGINT, `version` INT, `staticPageType` INT, `reservedForFuture` INT,
    `eventAttr` STRING, `currentImprId` BIGINT, `sourceImprId` BIGINT, `eventTimestamp` BIGINT,
    `eventCaptureTime` BIGINT, `requestCorrelationId` STRING, `cguid` STRING, `sid` STRING,
    `pageId` INT, `pageName` STRING, `pageFamily` STRING, `eventFamily` STRING, `eventAction` STRING,
    `userId` STRING, `clickId` STRING, `siteId` STRING, `ciid` STRING, `siid` STRING, `oldSessionSkey` BIGINT,
    `partialValidPage` BOOLEAN, `botFlags` ARRAY<INT>, `icfBinary` BIGINT, `ingestTime` BIGINT,
    `eventCnt` BIGINT, `rdt` INT, `regu` INT, `iframe` BOOLEAN, `refererHash` STRING, `sqr` STRING,
    `itemId` STRING, `flags` STRING, `urlQueryString` STRING, `webServer` STRING, `cookies` STRING,
    `referrer` STRING, `bot` INT, `clientIP` STRING, `remoteIP` STRING, `agentInfo` STRING, `forwardedFor` STRING,
    `appId` STRING, `appVersion` STRING, `osVersion` STRING, `trafficSource` STRING, `cobrand` STRING,
    `deviceFamily` STRING, `deviceType` STRING, `browserVersion` STRING, `browserFamily` STRING, `osFamily` STRING,
    `enrichedOsVersion` STRING, `applicationPayload` MAP<STRING, STRING>, `rlogid` STRING,
    `clientData` MAP<STRING, STRING>,
    `source` STRING,
    `dt` STRING,
    `hr` STRING
)
STORED AS parquet
PARTITIONED BY (source, dt, hr)
LOCATION 'viewfs://apollo-rno/user/b_bis/tdq/raw-data/tdq.pre_prod.dump.sojevent_tdq'


MSCK REPAIR TABLE zhangjt_tdq_sojevent;
refresh table zhangjt_tdq_sojevent;
