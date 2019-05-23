package com.ebay.sojourner.ubd.common.sharedlib.metrics;


import com.ebay.sojourner.ubd.common.sharedlib.util.SOJListGetValueByIndex;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.regex.Pattern;

public class AgentIPMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

    private static Set<Integer> badIPPages;
    private static String invalidIPPattern;
    private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
    private static final Pattern pattern = Pattern.compile(".*cdn.ampproject.org.*", Pattern.CASE_INSENSITIVE);
    private static final Logger logger = Logger.getLogger(AgentIPMetrics.class);
    private static UBIConfig ubiConfig ;
    @Override
    public void init() throws Exception {
        ubiConfig = UBIConfig.getInstance();
        badIPPages = PropertyUtils.getIntegerSet(ubiConfig.getString(Property.IP_EXCLUDE_PAGES), Property.PROPERTY_DELIMITER);
        logger.info("UBIConfig.getString(Property.IP_EXCLUDE_PAGES):"+ubiConfig.getString(Property.IP_EXCLUDE_PAGES));
        invalidIPPattern = ubiConfig.getString(Property.EXCLUDE_IP_PATTERN);
    }
    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().setFindFirst(false);
        sessionAccumulator.getUbiSession().setInternalIp(null);
        sessionAccumulator.getUbiSession().setExternalIp(null);
        sessionAccumulator.getUbiSession().setExternalIp2(null);


//        feed(event, sessionAccumulator);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {

        if(!sessionAccumulator.getUbiSession().isFindFirst())
        {
            sessionAccumulator.getUbiSession().setAgentInfo(event.getAgentInfo());
            sessionAccumulator.getUbiSession().setClientIp( event.getClientIP());
        }
        if (event.getIframe() == 0 && event.getRdt() == 0) {
            if (!sessionAccumulator.getUbiSession().isFindFirst()) {
                sessionAccumulator.getUbiSession().setAgentInfo( event.getAgentInfo());
                sessionAccumulator.getUbiSession().setClientIp( event.getClientIP());
                sessionAccumulator.getUbiSession().setFindFirst(true);;
            }
        }
       // to avoid the cut off issue on 2018-02-09
        if(event.getPartialValidPage()==null||event.getPartialValidPage()!=0)
        {
            if(event.getIframe()==0)
            {
                if (event.getRdt()!=1)
                {
                    if ( sessionAccumulator.getUbiSession().getExternalIp() == null) {
                        String remoteIp =event.getClientData().getRemoteIP(); //SOJParseClientInfo.getClientInfo(event.getClientData(), "RemoteIP");
                        String forwardFor =event.getClientData().getForwardFor();// SOJParseClientInfo.getClientInfo(event.getClientData(), "ForwardedFor");
                        sessionAccumulator.getUbiSession().setExternalIp( getExternalIP(event, remoteIp, forwardFor));
                        if (sessionAccumulator.getUbiSession().getExternalIp() == null && sessionAccumulator.getUbiSession().getInternalIp() == null) {
                            sessionAccumulator.getUbiSession().setInternalIp(getInternalIP(remoteIp, forwardFor));
                        }
                    }

                }

            }
        }
        if(event.getIframe()==0) {
            if(sessionAccumulator.getUbiSession().getExternalIp2()==null)
            {
                String remoteIp =event.getClientData().getRemoteIP(); //SOJParseClientInfo.getClientInfo(event.getClientData(), "RemoteIP");
                String forwardFor =event.getClientData().getForwardFor();// SOJParseClientInfo.getClientInfo(event.getClientData(), "ForwardedFor");
                sessionAccumulator.getUbiSession().setExternalIp2(getExternalIP(event, remoteIp, forwardFor));
            }

        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {
        //change the logic to allign with caleb's on 2018-02-06
        //  exInternalIp = externalIp == null ? internalIp : externalIp;

        sessionAccumulator.getUbiSession().setUserAgent(sessionAccumulator.getUbiSession().getAgentInfo());
        sessionAccumulator.getUbiSession().setIp(sessionAccumulator.getUbiSession().getClientIp());
        sessionAccumulator.getUbiSession().setExInternalIp((sessionAccumulator.getUbiSession().getExternalIp() == null) ? (sessionAccumulator.getUbiSession().getExternalIp2()==null?sessionAccumulator.getUbiSession().getInternalIp():sessionAccumulator.getUbiSession().getExternalIp2()): sessionAccumulator.getUbiSession().getExternalIp());
    }

    public String getExternalIP(UbiEvent event, String remoteIp, String forwardFor) {
        Integer pageId = event.getPageId();
        String urlQueryString = event.getUrlQueryString();
        if (badIPPages.contains(pageId)) {
            return null;
        }
        if (pageId != null && pageId == 3686 && urlQueryString != null && urlQueryString.contains("Portlet")) {
            return null;
        }

        Pattern p = Pattern.compile(invalidIPPattern);
        if (remoteIp != null && !(p.matcher(remoteIp).matches())) {
            return remoteIp;
        }

        for (int i = 1; i < 4; i++) {
            String forwardValueByIndex = SOJListGetValueByIndex.getValueByIndex(forwardFor, ",", i);
            if (forwardValueByIndex != null && !(p.matcher(forwardValueByIndex).matches())) {
                return forwardValueByIndex;
            }
        }
        return null;
    }

    public String getInternalIP(String remoteIp, String forwardFor) {
        String forwardForValueIndex1 = SOJListGetValueByIndex.getValueByIndex(forwardFor, ",", 1);
        if (remoteIp != null) {
            return remoteIp;
        }
        if (forwardForValueIndex1 != null) {
            return forwardForValueIndex1;
        }
        return null;
    }

}
