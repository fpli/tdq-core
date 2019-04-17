package com.ebay.sojourner.ubd.operators.mertrics;


import com.ebay.sojourner.ubd.common.sojlib.SOJListGetValueByIndex;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.Property;
import com.ebay.sojourner.ubd.util.PropertyUtils;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.regex.Pattern;

public class AgentIPMetrics implements FieldMetrics<UbiEvent, UbiEvent> {
    private String agentInfo;
    private String clientIp;
    private String exInternalIp;
    private boolean findFirst = false;
    private String internalIp;
    private String externalIp;
    private String externalIp2;
    private Set<Integer> badIPPages;
    private String invalidIPPattern;
    private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
    private static final Pattern pattern = Pattern.compile(".*cdn.ampproject.org.*", Pattern.CASE_INSENSITIVE);



    @Override
    public void start(UbiEvent event, UbiEvent session) {
        findFirst = false;
        internalIp = null;
        externalIp = null;
        externalIp2=null;
        agentInfo = event.getAgentInfo();
        clientIp = event.getClientIP();
        badIPPages = PropertyUtils.getIntegerSet(event.getConfiguration().getString(Property.IP_EXCLUDE_PAGES,null), Property.PROPERTY_DELIMITER);
        invalidIPPattern = event.getConfiguration().getString(Property.EXCLUDE_IP_PATTERN,null);
        feed(event, session);
    }

    @Override
    public void feed(UbiEvent event, UbiEvent session) {

        if (event.getIframe() == 0 && event.getRdt() == 0) {
            if (!findFirst) {
                agentInfo = event.getAgentInfo();
                clientIp = event.getClientIP();
                findFirst = true;
            }
        }
       // to avoid the cut off issue on 2018-02-09
        if(event.getPartialValidPage()==null||event.getPartialValidPage()!=0)
        {
            if(event.getIframe()==0)
            {
                if (event.getRdt()!=1)
                {
                    if (externalIp == null) {
                        String remoteIp =event.getClientData().getRemoteIP(); //SOJParseClientInfo.getClientInfo(event.getClientData(), "RemoteIP");
                        String forwardFor =event.getClientData().getForwardFor();// SOJParseClientInfo.getClientInfo(event.getClientData(), "ForwardedFor");
                        externalIp = getExternalIP(event, remoteIp, forwardFor);
                        if (externalIp == null && internalIp == null) {
                            internalIp = getInternalIP(remoteIp, forwardFor);
                        }
                    }

                }

            }
        }
        if(event.getIframe()==0) {
            if(externalIp2==null)
            {
                String remoteIp =event.getClientData().getRemoteIP(); //SOJParseClientInfo.getClientInfo(event.getClientData(), "RemoteIP");
                String forwardFor =event.getClientData().getForwardFor();// SOJParseClientInfo.getClientInfo(event.getClientData(), "ForwardedFor");
                externalIp2 = getExternalIP(event, remoteIp, forwardFor);
            }

        }
    }

    @Override
    public void end(UbiEvent session) {
        //change the logic to allign with caleb's on 2018-02-06
        //  exInternalIp = externalIp == null ? internalIp : externalIp;
        exInternalIp = (externalIp == null) ? (externalIp2==null?internalIp:externalIp2): externalIp;
        session.getUbiSession().setUserAgent(agentInfo);
        session.getUbiSession().setIp(clientIp);
        session.getUbiSession().setExInternalIp(exInternalIp);
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
