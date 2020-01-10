package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJGetUrlParams;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AppIdParser implements FieldParser<RawEvent, UbiEvent> {

    public static final String APPID = "app";
    public static final String REFERER = "Referer";
    public static final String TRACKING_SRC_APPID = "trackingSrcAppId";
    public static final String AGENT = "Agent";
    private static final String PREFIX = "http://www.ebay.com";
    public static final String SRCAPPID = "srcAppId";

    private static final String prefix0 = "ebayUserAgent/eBayIOS;";
    private static final String matchStr0 = ";iPad";
    private static final String prefix1 = "ebayUserAgent/eBayIOS;";
    private static final String prefix2 = "ebayUserAgent/eBayAndroid;";
    private static final String prefix3 = "eBayiPad/";
    private static final String prefix4 = "eBayiPhone/";
    private static final String prefix5 = "eBayAndroid/";
    private static final String prefix6 = "iphone/5";
    private static final String prefix7 = "Android/5";

    private static ArrayList<String> appidExclude = new ArrayList<>();
    private static Map<String, String> appIdStrMap = new HashMap<>();
    private static Map<String, String> appIdAgentMap = new HashMap<>();

    static {
        appIdStrMap.put("1462", "eBayInc80-8977-4f05-a933-3daa1311213");
        appIdStrMap.put("2878", "eBayInc64-7662-48ae-8d31-2b168593ee5");
        appIdStrMap.put("2571", "eBayInc52-907e-4b8a-ba0c-707469bb4d5");

        appIdAgentMap.put("2878", "iPad");
        appIdAgentMap.put("1462", "iPhone");
        appIdAgentMap.put("2571", "Android");

        appidExclude.add("3564");
        appidExclude.add("1622");
        appidExclude.add("4290");
    }

    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws RuntimeException {
        String appid = null;
        Map<String, String> map = new HashMap<>();
        map.putAll(rawEvent.getSojA());
        map.putAll(rawEvent.getSojK());
        map.putAll(rawEvent.getSojC());
        String mARecString = PropertyUtils.mapToString(rawEvent.getSojA());
        String mKRecString = PropertyUtils.mapToString(rawEvent.getSojK());
        String mCRecString = PropertyUtils.mapToString(rawEvent.getSojC());
        String applicationPayload = null;
        if (mARecString != null) {
            applicationPayload = mARecString;
        }
        if ((applicationPayload != null) && (mKRecString != null)) {
            applicationPayload = applicationPayload + "&" + mKRecString;
        }

        // else set C record
        if (applicationPayload == null)
            applicationPayload = mCRecString;

        String urlQueryStr = rawEvent.getClientData().getUrlQueryString() == null ? "" : rawEvent.getClientData().getUrlQueryString();
        String referrer = rawEvent.getClientData().getReferrer() == null ? "" : rawEvent.getClientData().getReferrer();
        String agentInfo = rawEvent.getClientData().getAgent() == null ? "" : rawEvent.getClientData().getAgent();
        String urlQueryStrPrefix = (urlQueryStr.startsWith("/") ? urlQueryStr : "/" + urlQueryStr);
        String urlQueryStrPrefix2 = (urlQueryStr.startsWith("/") ? urlQueryStr : (urlQueryStr.contains("/") ? "/" + urlQueryStr : "/?" + urlQueryStr));
        // String applicationPayload = rawEvent.getApplicationPayload();
        if (isInteger(SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(PREFIX + urlQueryStrPrefix), TRACKING_SRC_APPID))) {
            appid = SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(PREFIX + urlQueryStrPrefix), TRACKING_SRC_APPID);
        } else if (isInteger(SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(referrer), TRACKING_SRC_APPID))) {
            appid = SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(referrer), TRACKING_SRC_APPID);
        } else if (StringUtils.startsWithIgnoreCase(agentInfo, prefix0) && StringUtils.containsIgnoreCase(agentInfo, matchStr0)) {
            appid = "2878";
        } else if (StringUtils.startsWithIgnoreCase(agentInfo, prefix1)) {
            appid = "1462";
        } else if (StringUtils.startsWithIgnoreCase(agentInfo, prefix2)) {
            appid = "2571";
        } else if (StringUtils.startsWithIgnoreCase(agentInfo, prefix3)) {
            appid = "2878";
        } else if (StringUtils.startsWithIgnoreCase(agentInfo, prefix4)) {
            appid = "1462";
        } else if (StringUtils.startsWithIgnoreCase(agentInfo, prefix5)) {
            appid = "2571";
        } else if (StringUtils.startsWithIgnoreCase(agentInfo, prefix6)) {
            appid = "1462";
        } else if (StringUtils.startsWithIgnoreCase(agentInfo, prefix7)) {
            appid = "2571";
        } else if (isInteger(SOJNVL.getTagValue(applicationPayload, SRCAPPID)) && !appidExclude.contains(SOJNVL.getTagValue(applicationPayload, SRCAPPID))) {
            appid = SOJNVL.getTagValue(applicationPayload, SRCAPPID);
        } else if (isInteger(SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(PREFIX + urlQueryStrPrefix2), SRCAPPID))) {
            appid = SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(PREFIX + urlQueryStrPrefix2), SRCAPPID);
        } else if (isInteger(SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(referrer), SRCAPPID))) {
            appid = SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(referrer), SRCAPPID);
        } else {
            for (Map.Entry<String, String> entry : appIdStrMap.entrySet()) {
                if (StringUtils.containsIgnoreCase(urlQueryStr, entry.getValue())) {
                    appid = entry.getKey();
                    ubiEvent.setAppId(Integer.parseInt(appid));
                    return;
                }
            }
            String appIdStr = SOJNVL.getTagValue(applicationPayload, APPID);
            if (appIdStr != null && isInteger(appIdStr) && !appidExclude.contains(appIdStr)) {
                appid = appIdStr;
                ubiEvent.setAppId(Integer.parseInt(appid));
                return;
            }

            if (StringUtils.containsIgnoreCase(urlQueryStr, "&UUID=")
                    || StringUtils.containsIgnoreCase(urlQueryStr, "?UUID=")) {

                for (Map.Entry<String, String> entry : appIdAgentMap.entrySet()) {
                    if (StringUtils.containsIgnoreCase(agentInfo, entry.getValue())) {
                        appid = entry.getKey();
                        ubiEvent.setAppId(Integer.parseInt(appid));
                        return;
                    }
                }
            }
        }

        try {
            if (StringUtils.isNotBlank(appid)) {
                ubiEvent.setAppId(Integer.parseInt(appid));
            }
        } catch (NumberFormatException e) {
            log.warn("Parsing appId failed, format incorrect: " + appid);
        }

    }

    private Boolean isInteger(String str) {
        if (str == null) {
            return false;
        }

        try {
            Integer.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void init() throws Exception {

    }
}
