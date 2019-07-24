package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.common.datum.UbiEvent;
import com.ebay.sojourner.common.datum.UbiSession;
import com.ebay.sojourner.common.sojlib.ByteArrayToNum;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;

import java.util.HashSet;
import java.util.Set;

public class AttributeFlagMetrics implements FieldMetrics<UbiEvent, UbiSession, Configuration> {
    private static final Integer EXCEPTION_NULL_INTEGER_VALUE = -99;
    private static final Long DEFAULTDATE = 0L;
    private static final Integer NUMBER_ATTRIBUTE_FLAG = 13;
    private Set<String> userIdSet = new HashSet<String>();
    private Attributes attributes = new Attributes();

    /**
     * @return the userIdList
     */
    Set<String> getUserIdSet() {
        return userIdSet;
    }

    /**
     * @param userIdList the userIdList to set
     */
    void setUserIdList(Set<String> userIdSet) {
        this.userIdSet = userIdSet;
    }

    private Long sessionStartDate;

    // idx 0~12 used fr attribute flag
    // private BitSet attributeFlags = new BitSet();
    private byte[] attributeFlags = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public AttributeFlagMetrics() {
        init();
    }

    public void feed(UbiEvent event) {
        /*
         * is_abvar Application_Payload.contain('&abvar=' ) &&
         * !Application_Payload.contain('&abvar=0&' ) && !Application_Payload.contain('&abvar=-1&')
         * is_test Application_Payload.contain('&test=') is_tui_abtest
         * Application_Payload.contain('&tui_abtest=' ) is_epr Application_Payload.contain('&epr=')
         * is_pgV Application_Payload.contain('&pgV=') is_m2g
         * Application_Payload.contain('&Motors20Group='|'&m2g=' )
         */
        Long eventDate = event.getSojDataDt();
        if (eventDate == null) eventDate = DEFAULTDATE;
        String applicationPayload = event.getApplicationPayload();
        String webServer = event.getWebServer();
        String referrer = event.getReferrer();
        Integer pageId = event.getPageId();
        String userId = event.getUserId();

        // replace null with default value
        if (applicationPayload == null) applicationPayload = "";
        if (webServer == null) webServer = "";
        if (referrer == null) referrer = "";
        if (pageId == null) pageId = EXCEPTION_NULL_INTEGER_VALUE;
        // update user list
        if (StringUtils.isNotBlank(userId) && userIdSet.size() < 3 && !userIdSet.contains(userId)) userIdSet.add(userId);

        // attribute flag 10
        if (attributeFlags[10] == 0 && !eventDate.equals(sessionStartDate)) attributeFlags[10] = 1;

        if (applicationPayload.indexOf("&rule=") >= 0) attributes.isCustRule = true;

        if (webServer.indexOf("express.ebay") >= 0 || webServer.indexOf("ebayexpress") >= 0) attributes.isWeb_ee = true;

        if (webServer.indexOf("sofe") >= 0 && referrer.indexOf("pages.ebay.com/express") >= 0) attributes.isSofe = true;

        if (webServer.indexOf("half.") >= 0) attributes.isHalf = true;

        if (pageId == 2588 && (referrer.indexOf("http://www.express.ebay") >= 0 || referrer.indexOf("http://www.ebayexpress") >= 0))
            attributes.isEbxRef = true;

        if (applicationPayload.indexOf("&abvar=") >= 0 && applicationPayload.indexOf("&abvar=0&") <= 0
                && applicationPayload.indexOf("&abvar=-1&") <= 0) attributes.isAbvar = true;

        if (applicationPayload.indexOf("&test=") >= 0) attributes.isTest = true;

        if (applicationPayload.indexOf("&tui_abtest=") >= 0) attributes.isTuiAbtest = true;

        if (applicationPayload.indexOf("&epr=") >= 0) attributes.isEpr = true;

        if (applicationPayload.indexOf("&pgV=") >= 0) attributes.isPgV = true;

        if (applicationPayload.indexOf("&Motors20Group=") >= 0 || applicationPayload.indexOf("&m2g=") >= 0) attributes.isM2g = true;
        flagProcess(attributes);
    }

    void flagProcess(Attributes attributes) {
        // 13 Flags except 0 and 10
        if (attributes.isCustRule) attributeFlags[1] = 1;

        if (attributes.isWeb_ee || attributes.isSofe)
            attributeFlags[3] = 1;
        else if (attributes.isHalf)
            attributeFlags[2] = 1;
        else
            attributeFlags[4] = 1;

        if (attributes.isEbxRef) attributeFlags[5] = 1;

        if (attributes.isAbvar)
            attributeFlags[7] = 1;
        else if (attributes.isTest)
            attributeFlags[6] = 1;
        else if (attributes.isTuiAbtest)
            attributeFlags[8] = 1;
        else if (attributes.isEpr)
            attributeFlags[9] = 1;
        else if (attributes.isPgV)
            attributeFlags[11] = 1;
        else if (attributes.isM2g) attributeFlags[12] = 1;
    }

    public void endSessionProcess() {
        /*
         * ABIT_0 count(distinct user_id)>1 ABIT_1 is_cust_rule ABIT_2 if (is_web_ee = 1 or is_sofe
         * = 1) then 0 else if is_half = 1 then 1 else 0 ABIT_3 if ( is_web_ee = 1 or is_sofe = 1
         * then 1 else 0 ABIT_4 if(is_web_ee = 1 or is_sofe = 1 or is_half = 1) then 0 else 1 ABIT_5
         * is_ebx_ref ABIT_6 if(is_abvar = 1) then 0 else if (is_test = 1) then 1 else 0 ABIT_7
         * if(is_abvar = 1) then 1 else 0 ABIT_8 if(is_abvar = 1 or is_test = 1) then 0 else
         * if(is_tui_abtest = 1) then 1 else 0 ABIT_9 if(is_abvar = 1 or is_test = 1 or
         * is_tui_abtest = 1) then 0 else if(is_epr = 1) then 1 else 0 ABIT_10 if(session_start_dt
         * != null and event_dt <> session_start_dt OR COUNT( DISTINCT EVENT_DT) > 1) then 1 else 0
         * ABIT_11 if(is_abvar = 1 or is_test = 1 or is_tui_abtest = 1 or is_epr = 1) then 0 else
         * if(is_pgV = 1) then 1 else 0 ABIT_12 if(is_abvar = 1 or is_test = 1 or is_tui_abtest = 1
         * or is_epr = 1 or is_pgV = 1) then 0 else if (is_m2g = 1) then 1 else 0
         */

        if (userIdSet != null && userIdSet.size() > 1) attributeFlags[0] = 1;
    }

    public byte[] getAttributeFlag() {
        return attributeFlags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < NUMBER_ATTRIBUTE_FLAG - 1; i++)
            sb.append(attributeFlags[i]).append(",");
        sb.append(attributeFlags[NUMBER_ATTRIBUTE_FLAG - 1]).append("}");
        return sb.toString();
    }

    public void init() {
        sessionStartDate = DEFAULTDATE;

        userIdSet.clear();
        attributes.reset();
        for (int i = attributeFlags.length - 1; i >= 0; i--) {
            attributeFlags[i] = 0;
        }
    }

    @Override
    public void start(UbiEvent source, UbiSession target) {
        init();
        if (source.getSojDataDt() != null) {
            sessionStartDate = source.getSojDataDt();
        }
        feed(source);
    }

    @Override
    public void feed(UbiEvent source, UbiSession target) {
        feed(source);
    }

    @Override
    public void end(UbiSession target) {
        this.endSessionProcess();
        target.setAttrFlags(ByteArrayToNum.getInt(attributeFlags));
    }
    

    /**
     * @param attributes the attributeIntermediaFlag to set
     */
    void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public void init(Configuration conf) throws Exception {
        // nothing to do
    }
}


class Attributes {

    boolean isCustRule;
    boolean isWeb_ee;
    boolean isSofe;
    boolean isHalf;
    boolean isEbxRef;
    boolean isAbvar;
    boolean isTest;
    boolean isTuiAbtest;
    boolean isEpr;
    boolean isPgV;
    boolean isM2g;

    public void setVaules(boolean isCustRule, boolean isWeb_ee, boolean isSofe, boolean isHalf, boolean isEbxRef, boolean isAbvar, boolean isTest, boolean isTuiAbtest, boolean isEpr,
                   boolean isPgV, boolean isM2g) {
        this.isCustRule = isCustRule;
        this.isWeb_ee = isWeb_ee;
        this.isSofe = isSofe;
        this.isHalf = isHalf;
        this.isEbxRef = isEbxRef;
        this.isAbvar = isAbvar;
        this.isTest = isTest;
        this.isTuiAbtest = isTuiAbtest;
        this.isEpr = isEpr;
        this.isPgV = isPgV;
        this.isM2g = isM2g;
    }

    public void reset() {
        this.isCustRule = false;
        this.isWeb_ee = false;
        this.isSofe = false;
        this.isHalf = false;
        this.isEbxRef = false;
        this.isAbvar = false;
        this.isTest = false;
        this.isTuiAbtest = false;
        this.isEpr = false;
        this.isPgV = false;
        this.isM2g = false;
        
    }
}
