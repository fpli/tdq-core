package com.ebay.sojourner.ubd.rt.operators.sessionizer;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJTS2Date;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.queryablestate.client.QueryableStateClient;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;


public class UbiSessionWindowProcessFunction
        extends ProcessWindowFunction<SessionAccumulator, UbiEvent, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(UbiSessionWindowProcessFunction.class);
    private static SessionMetrics sessionMetrics;
    private OutputTag outputTag =null;
    private JobID jobID=null;
    private String proxyHost = "127.0.0.1";
    private int proxyPort = 9069;
    private QueryableStateClient client = null;
    public UbiSessionWindowProcessFunction(OutputTag outputTag,JobID jobID) throws UnknownHostException {
        this.outputTag=outputTag;
        this.jobID=jobID;
        client =new QueryableStateClient(proxyHost, proxyPort);
    }
    @Override
    public void process(Tuple tuple, Context context, Iterable<SessionAccumulator> elements,
                        Collector<UbiEvent> out) throws Exception {
        if(sessionMetrics==null)
        {
            sessionMetrics=  new SessionMetrics();
        }
        SessionAccumulator sessionAccumulator = elements.iterator().next();

        if(sessionAccumulator.getUbiEvent()!=null) {
            out.collect(sessionAccumulator.getUbiEvent());

            if (context.currentWatermark() > context.window().maxTimestamp()) {
                endSessionEvent(sessionAccumulator);
                UbiSession ubiSession = new UbiSession();
                ubiSession.setGuid(sessionAccumulator.getUbiEvent().getGuid());
                ubiSession.setAgentString(sessionAccumulator.getUbiSession().getAgentString());
                ubiSession.setIp(sessionAccumulator.getUbiSession().getIp());
                ubiSession.setUserAgent(sessionAccumulator.getUbiSession().getUserAgent());
                ubiSession.setExInternalIp(sessionAccumulator.getUbiSession().getExInternalIp());
                ubiSession.setAgentCnt(sessionAccumulator.getUbiSession().getAgentCnt());
                ubiSession.setSingleClickSessionFlag(sessionAccumulator.getUbiSession().getSingleClickSessionFlag());
                CompletableFuture<ValueState<IpSignature>> completableFuture = queryState(ubiSession.getClientIp(),jobID,client);
                if(completableFuture!=null)
                {
                    IpSignature ipSignature = completableFuture.get().value();
                    ubiSession.setBotFlag(ipSignature.getBotFlag());
                }
                context.output(outputTag, ubiSession);
            }
        }
        else
        {
            logger.error("ubiEvent is null pls check");
        }
    }
    private CompletableFuture<ValueState<IpSignature>> queryState(String key,JobID jobId, QueryableStateClient client)
    {
        return client.getKvState(
                jobId,
                "bot7",
                key,
                Types.STRING,
                new ValueStateDescriptor<IpSignature>("", TypeInformation.of(new TypeHint<IpSignature>() {})));
    }
    private void endSessionEvent(SessionAccumulator sessionAccumulator) throws Exception {
        if(sessionAccumulator.getUbiEvent().getEventTimestamp()!=null) {
            sessionAccumulator.getUbiSession().setEndTimestamp(sessionAccumulator.getUbiEvent().getEventTimestamp());
        }
        else
        {
            logger.error(sessionAccumulator.getUbiEvent());
        }
        sessionMetrics.end(sessionAccumulator);

    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
        File configFile = getRuntimeContext().getDistributedCache().getFile("configFile");
        UBIConfig ubiConfig = UBIConfig.getInstance();
        if(!ubiConfig.isInitialized()) {


            ubiConfig.initAppConfiguration(configFile);
            initConfiguration(conf, false, ubiConfig);
            setConfiguration(conf, ubiConfig);
        }

    }
    public static void initConfiguration(Configuration conf, boolean enableTest,UBIConfig ubiConfig) throws Exception {


        if (ubiConfig.getUBIProperty(Property.LOG_LEVEL) != null) {
            ubiConfig.setString(Property.LOG_LEVEL, ubiConfig.getUBIProperty(Property.LOG_LEVEL));
        }

        if (enableTest) {
            ubiConfig.setBoolean(Property.IS_TEST_ENABLE, true);
            loadLookupTableLocally(conf,ubiConfig);
        } else {
            ubiConfig.setString(Property.IFRAME_PAGE_IDS, ubiConfig.getUBIProperty(Property.IFRAME_PAGE_IDS));

            ubiConfig.setString(Property.FINDING_FLAGS, ubiConfig.getUBIProperty(Property.FINDING_FLAGS));
            ubiConfig.setString(Property.VTNEW_IDS, ubiConfig.getUBIProperty(Property.VTNEW_IDS));
            ubiConfig.setString(Property.IAB_AGENT, ubiConfig.getUBIProperty(Property.IAB_AGENT ));
            ubiConfig.setString(Property.APP_ID, ubiConfig.getUBIProperty(Property.APP_ID));
            ubiConfig.setString(Property.TEST_USER_IDS, ubiConfig.getUBIProperty(Property.TEST_USER_IDS));
            ubiConfig.setString(Property.LARGE_SESSION_GUID, ubiConfig.getUBIProperty(Property.LARGE_SESSION_GUID));
            ubiConfig.setString(Property.PAGE_FMLY, ubiConfig.getUBIProperty(Property.PAGE_FMLY));
            ubiConfig.setString(Property.MPX_ROTATION, ubiConfig.getUBIProperty(Property.MPX_ROTATION));

//            conf.setString(Property.IFRAME_PAGE_IDS4Bot12, UBIConfig.getUBIProperty(Property.IFRAME_PAGE_IDS4Bot12));
//            conf.setString(Property.SELECTED_IPS, UBIConfig.getUBIProperty(Property.SELECTED_IPS));
//            conf.setString(Property.SELECTED_AGENTS, UBIConfig.getUBIProperty(Property.SELECTED_AGENTS));

        }
    }
    protected static void loadLookupTableLocally(Configuration conf,UBIConfig ubiConfig) throws Exception {
        LkpFetcher fetcher = new LkpFetcher();
        fetcher.loadLocally();
        for (String key : fetcher.getResult().keySet()) {
            if (null == ubiConfig.getString(key)) {
                String value = fetcher.getResult().get(key);
                ubiConfig.setString(key, value);
            }
        }
    }
    public void setConfiguration(Configuration conf,UBIConfig ubiConfig) throws Exception {

        // Set session properties
        ubiConfig.setLong(Property.EVENT_WAITING_PERIOD, Long.valueOf(ubiConfig.getUBIProperty(Property.EVENT_WAITING_PERIOD)) * SOJTS2Date.MILLI2MICRO);
        ubiConfig.setLong(Property.SESSION_IDLE_TIMEOUT, Long.valueOf(ubiConfig.getUBIProperty(Property.SESSION_IDLE_TIMEOUT)) * SOJTS2Date.MILLI2MICRO);
        ubiConfig.setLong(Property.SESSION_MAX_DURATION, Long.valueOf(ubiConfig.getUBIProperty(Property.SESSION_MAX_DURATION)) * SOJTS2Date.MILLI2MICRO);
        ubiConfig.setLong(Property.EVENT_DELAY_THRESHOLD, Long.valueOf(ubiConfig.getUBIProperty(Property.EVENT_DELAY_THRESHOLD)) * SOJTS2Date.MILLI2MICRO);


        // Set event properties
        ubiConfig.setLong(Property.EVENT_POOL_THRESHOLD, Long.valueOf(ubiConfig.getUBIProperty(Property.EVENT_POOL_THRESHOLD)));
        // Set Cobrand properties
        ubiConfig.setString(Property.EXPRESS_SITE, ubiConfig.getUBIProperty(Property.EXPRESS_SITE));
        ubiConfig.setString(Property.HALF_SITE, ubiConfig.getUBIProperty(Property.HALF_SITE));
        ubiConfig.setString(Property.EXPRESS_PARTNER, ubiConfig.getUBIProperty(Property.EXPRESS_PARTNER));
        ubiConfig.setString(Property.SHOPPING_PARTNER, ubiConfig.getUBIProperty(Property.SHOPPING_PARTNER));
        ubiConfig.setString(Property.HALF_PARTNER, ubiConfig.getUBIProperty(Property.HALF_PARTNER));
        ubiConfig.setString(Property.ARTISAN_PARTNER, ubiConfig.getUBIProperty(Property.ARTISAN_PARTNER));
        ubiConfig.setString(Property.MOBILE_AGENT_START, ubiConfig.getUBIProperty(Property.MOBILE_AGENT_START));
        ubiConfig.setString(Property.MOBILE_AGENT_INDEX, ubiConfig.getUBIProperty(Property.MOBILE_AGENT_INDEX));
        ubiConfig.setString(Property.MOBILE_AGENT_OTHER, ubiConfig.getUBIProperty(Property.MOBILE_AGENT_OTHER));
        // Set page indicators
        ubiConfig.setString(Property.SEARCH_VIEW_PAGES, ubiConfig.getUBIProperty(Property.SEARCH_VIEW_PAGES));
        ubiConfig.setString(Property.VIEW_ITEM_PAGES, ubiConfig.getUBIProperty(Property.VIEW_ITEM_PAGES));
        ubiConfig.setString(Property.BID_PAGES, ubiConfig.getUBIProperty(Property.BID_PAGES));
        ubiConfig.setString(Property.BIN_PAGES, ubiConfig.getUBIProperty(Property.BIN_PAGES));
        ubiConfig.setString(Property.CAPTCHA_PAGES, ubiConfig.getUBIProperty(Property.CAPTCHA_PAGES));
        ubiConfig.setString(Property.HALF_PAGES, ubiConfig.getUBIProperty(Property.HALF_PAGES));
        ubiConfig.setString(Property.CORESITE_PAGES, ubiConfig.getUBIProperty(Property.CORESITE_PAGES));
        ubiConfig.setString(Property.CLASSIFIED_PAGES, ubiConfig.getUBIProperty(Property.CLASSIFIED_PAGES));
        // New metrics need page list
        ubiConfig.setString(Property.ROVER_PAGES, ubiConfig.getUBIProperty(Property.ROVER_PAGES));
        ubiConfig.setString(Property.LAND_PAGES1, ubiConfig.getUBIProperty(Property.LAND_PAGES1));
        ubiConfig.setString(Property.LAND_PAGES2, ubiConfig.getUBIProperty(Property.LAND_PAGES2));
        ubiConfig.setString(Property.SCEVENT_EXCLUDE_PAGES1, ubiConfig.getUBIProperty(Property.SCEVENT_EXCLUDE_PAGES1));
        ubiConfig.setString(Property.SCEVENT_EXCLUDE_PAGES2, ubiConfig.getUBIProperty(Property.SCEVENT_EXCLUDE_PAGES2));
        ubiConfig.setString(Property.AGENT_EXCLUDE_PAGES, ubiConfig.getUBIProperty(Property.AGENT_EXCLUDE_PAGES));
        ubiConfig.setString(Property.NOTIFY_CLICK_PAGES, ubiConfig.getUBIProperty(Property.NOTIFY_CLICK_PAGES));
        ubiConfig.setString(Property.NOTIFY_VIEW_PAGES, ubiConfig.getUBIProperty(Property.NOTIFY_VIEW_PAGES));
        ubiConfig.setString(Property.LNDG_PAGE_IDS, ubiConfig.getUBIProperty(Property.LNDG_PAGE_IDS));
        ubiConfig.setString(Property.IP_EXCLUDE_PAGES, ubiConfig.getUBIProperty(Property.IP_EXCLUDE_PAGES));
        ubiConfig.setString(Property.EXCLUDE_IP_PATTERN, ubiConfig.getUBIProperty(Property.EXCLUDE_IP_PATTERN));
        ubiConfig.setString(Property.EBAY_SITE_COBRAND, ubiConfig.getUBIProperty(Property.EBAY_SITE_COBRAND));
        ubiConfig.setString(Property.INVALID_BOT_FILTER, ubiConfig.getUBIProperty(Property.INVALID_BOT_FILTER));
        // Extra metrics for page list
        ubiConfig.setString(Property.INVALID_PAGE_IDS, ubiConfig.getUBIProperty(Property.INVALID_PAGE_IDS));
        // Set APP PAYLOAD KV Property
        ubiConfig.setString(Property.SWD_VALUES, ubiConfig.getUBIProperty(Property.SWD_VALUES));
        ubiConfig.setString(Property.ROT_VALUES, ubiConfig.getUBIProperty(Property.ROT_VALUES));
        ubiConfig.setString(Property.VI_EVENT_VALUES, ubiConfig.getUBIProperty(Property.VI_EVENT_VALUES));
        // Set APP ID property
        ubiConfig.setString(Property.MOBILE_APP, ubiConfig.getUBIProperty(Property.MOBILE_APP));
        ubiConfig.setString(Property.DESKTOP_APP, ubiConfig.getUBIProperty(Property.DESKTOP_APP));
        ubiConfig.setString(Property.EIM_APP, ubiConfig.getUBIProperty(Property.EIM_APP));
        // Set BOT rules
        ubiConfig.setString(Property.BOT_RULE_PACKAGE, ubiConfig.getUBIProperty(Property.BOT_RULE_PACKAGE));
        ubiConfig.setString(Property.BOT_RULE_CLASSES, ubiConfig.getUBIProperty(Property.BOT_RULE_CLASSES));
        // Set large session properties
        ubiConfig.setLong(Property.LARGE_SESSION_EVENT_NUMBER, Long.valueOf(ubiConfig.getUBIProperty(Property.LARGE_SESSION_EVENT_NUMBER)));
        ubiConfig.setLong(Property.LARGE_SESSION_TIMES_OF_BOT15, Long.valueOf(ubiConfig.getUBIProperty(Property.LARGE_SESSION_TIMES_OF_BOT15)));
        // Set disabled filters
        String disabledFilterNames = ubiConfig.getUBIProperty(Property.DISABLED_FILTER_NAMES);
        if (StringUtils.isNotBlank(disabledFilterNames)) {
            ubiConfig.setString(Property.DISABLED_FILTER_NAMES, disabledFilterNames);
        }
        if(!ubiConfig.isInitialized())
        {
            ubiConfig.setInitialized(true);
        }
    }
}
