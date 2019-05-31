package com.ebay.sojourner.ubd.common.sharedlib.parser;


import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJGetUrlParams;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AppIdParser implements FieldParser<RawEvent, UbiEvent> {
    private static final Logger log = Logger.getLogger(AppIdParser.class);
    public static final String APPID = "app";
    public static final String REFERER = "Referer";
    public static final String TRACKING_SRC_APPID = "trackingSrcAppId";
    public static final String AGENT = "Agent";
    private static final String PREFIX = "http://www.ebay.com";
    public static final String SRCAPPID="srcAppId";


    Pattern queryPattern7 = Pattern.compile(".*srcAppId.*eBayInc80-8977-4f05-a933-3daa1311213.*", Pattern.CASE_INSENSITIVE);
    private static ArrayList<String> appidExclude = new ArrayList<String>();
    Pattern pattern = Pattern.compile("ebayUserAgent/eBayIOS;.*;iPad.*", Pattern.CASE_INSENSITIVE);
    Pattern pattern1 = Pattern.compile("ebayUserAgent/eBayIOS;.*", Pattern.CASE_INSENSITIVE);
    Pattern pattern2 = Pattern.compile("ebayUserAgent/eBayAndroid;.*", Pattern.CASE_INSENSITIVE);
    Pattern pattern3 = Pattern.compile("eBayiPad/.*", Pattern.CASE_INSENSITIVE);
    Pattern pattern4 = Pattern.compile("eBayiPhone/.*", Pattern.CASE_INSENSITIVE);
    Pattern pattern5 = Pattern.compile("eBayAndroid/.*", Pattern.CASE_INSENSITIVE);
    Pattern pattern6 = Pattern.compile("iphone/5.*", Pattern.CASE_INSENSITIVE);
    Pattern pattern7 = Pattern.compile("Android/5.*", Pattern.CASE_INSENSITIVE);
    private static  ArrayList<Pattern> patternList = new ArrayList<Pattern>();
    private static  ArrayList<String> idList = new ArrayList<String>();
    private static  ArrayList<String> idList2 = new ArrayList<String>();
    Pattern anyPattern = Pattern.compile(".*&UUID=.*|.*?UUID=.*", Pattern.CASE_INSENSITIVE);
    private static  ArrayList<Pattern> patternList2 = new ArrayList<Pattern>();
    static {

        Pattern queryPattern = Pattern.compile(".*srcAppId.*eBayInc80-8977-4f05-a933-3daa1311213.*", Pattern.CASE_INSENSITIVE);
        Pattern queryPattern2 = Pattern.compile(".*srcAppId.*eBayInc64-7662-48ae-8d31-2b168593ee5.*", Pattern.CASE_INSENSITIVE);
        Pattern queryPattern3 = Pattern.compile(".*srcAppId.*eBayInc52-907e-4b8a-ba0c-707469bb4d5.*", Pattern.CASE_INSENSITIVE);
        Pattern queryPattern4 = Pattern.compile(".*eBayInc80-8977-4f05-a933-3daa1311213.*", Pattern.CASE_INSENSITIVE);
        Pattern queryPattern5 = Pattern.compile(".*eBayInc64-7662-48ae-8d31-2b168593ee5.*", Pattern.CASE_INSENSITIVE);
        Pattern queryPattern6 = Pattern.compile(".*eBayInc52-907e-4b8a-ba0c-707469bb4d5.*", Pattern.CASE_INSENSITIVE);

        Pattern agentPattern = Pattern.compile(".*iPad.*", Pattern.CASE_INSENSITIVE);
        Pattern agentPattern2 = Pattern.compile(".*iPhone.*", Pattern.CASE_INSENSITIVE);
        Pattern agentPattern3 = Pattern.compile(".*Android.*", Pattern.CASE_INSENSITIVE);


        patternList.add(queryPattern);
        patternList.add(queryPattern2);
        patternList.add(queryPattern3);
        patternList.add(queryPattern4);
        patternList.add(queryPattern5);
        patternList.add(queryPattern6);

        patternList2.add(agentPattern);
        patternList2.add(agentPattern2);
        patternList2.add(agentPattern3);

        idList.add("1462");
        idList.add("2878");
        idList.add("2571");
        idList.add("1462");
        idList.add("2878");
        idList.add("2571");
        idList2.add("2878");
        idList2.add("1462");
        idList2.add("2571");
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

        String urlQueryStr = rawEvent.getClientData().getUrlQueryString()==null?"": rawEvent.getClientData().getUrlQueryString();
        String referrer =rawEvent.getClientData().getReferrer()==null?"":rawEvent.getClientData().getReferrer();
        String agentInfo =rawEvent.getClientData().getAgent()==null?"":rawEvent.getClientData().getAgent();
        String urlQUeryStrprefix = (urlQueryStr.startsWith("/") ? urlQueryStr : "/" + urlQueryStr);
        String urlQUeryStrprefix2 = (urlQueryStr.startsWith("/") ? urlQueryStr : (urlQueryStr.contains("/") ? "/" + urlQueryStr : "/?" + urlQueryStr));
       // String applicationPayload = rawEvent.getApplicationPayload();
        if (isInteger(SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(PREFIX + urlQUeryStrprefix), TRACKING_SRC_APPID)) == 1) {
            appid = SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(PREFIX + urlQUeryStrprefix), TRACKING_SRC_APPID);
        } else if (isInteger(SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(referrer), TRACKING_SRC_APPID)) == 1) {
            appid = SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(referrer), TRACKING_SRC_APPID);
        } else if (pattern.matcher(agentInfo).matches()) {
            appid = "2878";
        } else if (pattern1.matcher(agentInfo).matches()) {
            appid = "1462";
        } else if (pattern2.matcher(agentInfo).matches()) {
            appid = "2571";
        } else if (pattern3.matcher(agentInfo).matches()) {
            appid = "2878";
        } else if (pattern4.matcher(agentInfo).matches()) {
            appid = "1462";
        } else if (pattern5.matcher(agentInfo).matches()) {
            appid = "2571";
        } else if (pattern6.matcher(agentInfo).matches()) {
            appid = "1462";
        } else if (pattern7.matcher(agentInfo).matches()) {
            appid = "2571";
        } else if (isInteger(SOJNVL.getTagValue(applicationPayload, SRCAPPID)) == 1 && !appidExclude.contains(SOJNVL.getTagValue(applicationPayload, SRCAPPID))) {
            appid = SOJNVL.getTagValue(applicationPayload, SRCAPPID);
        } else if (isInteger(SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(PREFIX + urlQUeryStrprefix), SRCAPPID)) == 1) {
            appid = SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(PREFIX + urlQUeryStrprefix2), SRCAPPID);
        } else if (isInteger(SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(referrer), SRCAPPID)) == 1) {
            appid = SOJNVL.getTagValue(SOJGetUrlParams.getUrlParams(referrer), SRCAPPID);
        } else {
            for (int i = 0; i < patternList.size(); i++) {
                if (patternList.get(i).matcher(urlQueryStr).matches()) {
                    appid = idList.get(i);
                    return;
                }
            }
            String appIdStr = SOJNVL.getTagValue(applicationPayload, APPID);
            if (appIdStr != null && isInteger(appIdStr) == 1 && !appidExclude.contains(appIdStr)) {
                appid = appIdStr;
                return;
            }
            if(anyPattern.matcher(urlQueryStr).matches())
            {
                for(int j=0;j<patternList2.size();j++)
                {
                    if (patternList2.get(j).matcher(agentInfo).matches()) {
                        appid = idList2.get(j);
                        return;
                    }
                }
            }
        }
        try {
            if (StringUtils.isNotBlank(appid)) {
                ubiEvent.setAppId(Integer.parseInt(appid));
            }
        }catch (NumberFormatException e) {
            log.debug("Parsing appid failed, format incorrect: " + appid);
        }

    }
    private static Integer isInteger(String str)
    {
        if (str == null) {
            return 0;
        }

        try {
            int value = Integer.valueOf(str);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void init() throws Exception {

    }
}
