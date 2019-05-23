package com.ebay.sojourner.ubd.common.util;

/**
 * The resources location constants
 * @author kofeng
 */
public class Resources {
    // Application Configuration 
    public static final String UBI_CONFIG = "/ubi.properties";
    public static final String LKP_CONFIG = "/lkp.properties";
    // AVRO Schema
    public static final String EVENT_KEY_SCHEMA = "/avro/EventKey.avsc";
    public static final String OPEN_SESSION_INFO_SCHEMA = "/avro/OpenSessionInfo.avsc";
    public static final String UBI_EVENT_SCHEMA = "/avro/UbiEvent.avsc";
    public static final String RAW_UBI_EVENT_SCHEMA = "/avro/RawUbiEvent.avsc";
    public static final String SESSION_KEY_SCHEMA = "/avro/SessionKey.avsc";
    public static final String UBI_SESSION_SCHEMA = "/avro/UbiSession.avsc";
    public static final String EVENT_SHUFFLE_SCHEMA = "/avro/EventShuffleKey.avsc";
    public static final String SOJ_EVENT_SCHEMA = "/avro/SojEvent.avsc";
    public static final String EVENT_GROUP_KEY_SCHEMA = "/avro/EventGroupKey.avsc";
    public static final String SOJ_SESSION_SCHEMA = "/avro/SojSession.avsc";
    public static final String BOT_SESSION_SCHEMA = "/avro/BotSession.avsc";
    public static final String BOT_SHUFFLE_SESSION_SCHEMA = "/avro/BotShuffleSession.avsc";
    public static final String AGENT_GROUP_KEY_SCHEMA = "/avro/AgentKey.avsc";
    public static final String AGENT_SHUFFLE_KEY_SCHEMA = "/avro/AgentShuffleKey.avsc";
    public static final String IP_GROUP_KEY_SCHEMA = "/avro/IPKey.avsc";
    public static final String IP_SHUFFLE_KEY_SCHEMA = "/avro/IpShuffleKey.avsc";
    public static final String GUID_GROUP_KEY_SCHEMA = "/avro/GuidKey.avsc";
    public static final String AGENT_IP_GROUP_SCHEMA = "/avro/AgentIPKey.avsc";
    public static final String GUID_SHUFFLE_KEY_SCHEMA = "/avro/GuidShuffleKey.avsc";
    public static final String AGENT_IP_SHUFFLE_KEY_SCHEMA = "/avro/AgentIPShuffleKey.avsc";
    public static final String SESSION_EVENT_KEY_SCHEMA = "/avro/SessionEventKey.avsc";
    public static final String SESSION_X_UID_SCHEMA = "/avro/SessionXUid.avsc";
    public static final String PARTIAL_EVENT_S_SCHEMA = "/avro/PartialEventS.avsc";
    public static final String SESSION_X_UID_KEY_SCHEMA = "/avro/SessionXUidKey.avsc";
    public static final String PARTIAL_SESSION_S_SCHEMA = "/avro/PartialSessionS.avsc";
    public static final String GUID_X_UID_KEY_SCHEMA = "/avro/GuidXUidKey.avsc";
    public static final String GUID_X_UID_SCHEMA = "/avro/GuidXUid.avsc";
    public static final String GUID_KEY_SCHEMA = "/avro/GuidKey.avsc";
    public static final String PARTIAL_EVENT_G_SCHEMA = "/avro/PartialEventG.avsc";
    public static final String EVENT_X_UID_SCHEMA = "/avro/EventXUid.avsc";
    public static final String PARTIAL_EVENT_E_SCHEMA = "/avro/PartialEventE.avsc";
    public static final String PARTIAL_SESSION_X_UID_SCHEMA = "/avro/PartialSessionXUid.avsc";
    public static final String PARTIAL_UID_SESSION_SCHEMA = "/avro/PartialUidSession.avsc";
    public static final String LARGE_SESSION_INFO_SCHEMA = "/avro/LargeSessionInfo.avsc";
    // AVRO schema for new BOTS
    public static final String BOT_AI_SESSION_SCHEMA = "/avro/BotAISession.avsc";
    public static final String AGENT_STRING_SHULLFE_KEY_SCHEMA = "/avro/AgentStringShuffleKey.avsc";
    public static final String AGENT_SESSION_SCHEMA = "/avro/AgentSession.avsc";
    public static final String IP_AGENT_METRICS_SCHEMA = "/avro/IpAgentMetrics.avsc";
    public static final String IP_METRICS_SCHEMA = "/avro/IpMetrics.avsc";
    public static final String IP_AGENT_SESSION_SCHEMA = "/avro/IpAgentSession.avsc";
    public static final String IP_SESSION_START_DT_SCHEMA = "/avro/IpSessionStartDt.avsc";
    public static final String AGENT_STRING_GROUP_SCHEMA = "/avro/AgentString.avsc";
    public static final String AGENT_STRING_EX_INTERNAL_IP_SHUFFLE_KEY = "/avro/AgentStringExInternalIPShuffleKey.avsc";
    public static final String AGENT_STRING_IP_SCHEMA = "/avro/AgentStringIpPartitionKey.avsc";
    // New Bot 6
    public static final String AGENT_SESSION_BOT6_SCHEMA = "/avro/AgentSession4Bot6.avsc";
    // Lookup Table Source: for loading source from locally
    public static final String IFRAME_PAGE_SOURCE = "/lkp/iframePageIds";
    public static final String FINDING_FLAG_SOURCE = "/lkp/findingFlag";
    public static final String VT_NEWID_SOURCE = "/lkp/vtNewIdSource";
    public static final String IAB_AGENT_SOURCE = "/lkp/iabAgentRex";
    public static final String APP_ID_SOURCE = "/lkp/appid";
    public static final String TEST_USER_SOURCE = "/lkp/testUserIds";
    public static final String LARGE_SESSION_SOURCE = "/lkp/largeSessionGuid";
    public static final String PAGE_FMLY_NAME = "/lkp/pageFmly";
    public static final String MPX_ROTATION_SOURCE = "/lkp/mpx";
    public static final String SELECTED_IPS = "/lkp/selectedIps";
    public static final String SELECTED_AGENTS = "/lkp/selectedAgents";
}
