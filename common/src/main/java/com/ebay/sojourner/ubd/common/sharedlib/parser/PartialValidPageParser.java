package com.ebay.sojourner.ubd.common.sharedlib.parser;


import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJExtractFlag;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;


public class PartialValidPageParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {

	//	private long startTimestamp = 1282147200000000L + 2208963600000000L;  2010-08-19 sojTimestamp
	private Integer[] CLFGPageIds = { 2588, 3030, 3907, 4939, 5108 };
	// add more pages on 2018-02-09 2047675,2054574,2057587,2052197,2049334,2052122,2051865,4853
	private Integer[] WSPageIds = { 1702440, 2043183, 2043216, 2047524, 2051322, 2051319, 2052193, 2051542, 2052317, 3693,2047675,2054574,2057587,2052197,2049334,2052122,2051865,4853 };
	private Integer[] STATEPageIds = { 2765, 2771, 2685, 3306, 2769, 4034, 4026 };
	//add 5713,2053584,6024,2053898,6053,2054900 on 2018-02-09
	private Integer[] XOProcessorPageIds = { 5713,2053584,6024,2053898,6053,2054900 };
	private String[] IpLists = { "10.2.137.50", "10.2.182.150", "10.2.137.51", "10.2.182.151" };
	//add "MainCheckoutPage" on 2018-02-09
	private String[] pageLists = { "ryprender", "cyp", "success", "pots", "error", "rypexpress","MainCheckoutPage" };
	private String[] checkoutCleanList={"MainCheckoutPage", "CheckoutPaymentSuccess","CheckoutPayPalWeb","PaymentSent","CheckoutPaymentMethod","Autopay","CheckoutPayPalError1","CheckoutPaymentFailed"};

	@Override
	public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
		Integer pageId = ubiEvent.getPageId();
		String urlQueryString = ubiEvent.getUrlQueryString();
		ClientData clientData = ubiEvent.getClientData();
		String webServer = ubiEvent.getWebServer();
		String sqr = ubiEvent.getSqr();
		Integer siteId = ubiEvent.getSiteId();
		String pageName = ubiEvent.getPageName();
		String sojPage=ubiEvent.getApplicationPayload()==null?null: SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "page");
		String urlQueryPage=StringUtils.isNotBlank(urlQueryString)?SOJNVL.getTagValue(urlQueryString, "page"):null;
		String pfn=ubiEvent.getApplicationPayload()==null?null:SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "pfn");
		String agentString=clientData==null?null: clientData.getAgent();
		Integer appId = ubiEvent.getAppId();
        String cflags=ubiEvent.getApplicationPayload()==null?null:SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "cflgs");

		if (ubiEvent.getRdt()!=null&&ubiEvent.getRdt() == 1) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (pageId != null && pageId == 3686 && StringUtils.isNotBlank(urlQueryString) && urlQueryString.contains("Portlet")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (pageId != null && pageId == 451 && StringUtils.isNotBlank(urlQueryString) && urlQueryString.contains("LogBuyerRegistrationJSEvent")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (StringUtils.isNotBlank(webServer) && webServer.contains("sandbox.ebay.")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (isCorrespondingPageId(pageId, CLFGPageIds) && StringUtils.isNotBlank(cflags)&& SOJExtractFlag.extractFlag(cflags, 4) == 1) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (StringUtils.isNotBlank(cflags)&& SOJExtractFlag.extractFlag(cflags, 14) == 1) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (StringUtils.isNotBlank(sqr)
				&& ("null".equals(sqr)|| "undefined".equals(sqr)  || sqr.endsWith(".htm") || sqr.endsWith(".asp") || sqr.endsWith(".jsp") || sqr.endsWith(".gif")
				|| sqr.endsWith(".png") || sqr.endsWith(".pdf") || sqr.endsWith(".html") || sqr.endsWith(".php") || sqr.endsWith(".cgi")
				|| sqr.endsWith(".jpeg") || sqr.endsWith(".swf") || sqr.endsWith(".txt") || sqr.endsWith(".wav") || sqr.endsWith(".zip")
				|| sqr.endsWith(".flv") || sqr.endsWith(".dll") || sqr.endsWith(".ico") || sqr.endsWith(".jpg")||sqr.contains("hideoutput"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// ignore the condition sessionStartDt >= '2010-08-19' as not all ubiEvent have the field and the condition is not necessary
		if (pageId != null && pageId == 1468660 && siteId != null && siteId == 0
				&& StringUtils.isNotBlank(webServer) && webServer.equals("rover.ebay.com")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (pageId!=null&&isCorrespondingPageId(pageId, WSPageIds) && StringUtils.isNotBlank(webServer) && webServer.startsWith("rover.ebay.")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "an") != null && SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "av") != null) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "in") != null) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (pageId != null && pageId == 5360 && StringUtils.isNotBlank(urlQueryString) && urlQueryString.contains("_xhr=2")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (StringUtils.isNotBlank(urlQueryString) && (urlQueryString.startsWith("/_vti_bin") || urlQueryString.startsWith("/MSOffice/cltreq.asp"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if ("1".equals(SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "mr")) || StringUtils.isNotBlank(urlQueryString)
				&& (urlQueryString.contains("?redirect=mobile") || urlQueryString.contains("&redirect=mobile"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (pageId != null && pageId == 2043141 && StringUtils.isNotBlank(urlQueryString)
				&& (urlQueryString.contains("jsf.js") || urlQueryString.startsWith("/intercept.jsf"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		//change SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "state") != null to SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "state") == null on 2018-02-09
		//if (isCorrespondingPageId(pageId, STATEPageIds) && SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "state") != null) {
		if (pageId!=null&&isCorrespondingPageId(pageId, STATEPageIds) && SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "state") == null) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (StringUtils.isNotBlank(urlQueryString) && urlQueryString.contains("_showdiag=1")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (clientData!=null&&clientData.getRemoteIP()!=null&&isCorrespondingString(clientData.getRemoteIP(), IpLists)) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (StringUtils.isNotBlank(urlQueryString)&&("/&nbsp;".equals(urlQueryString) || "/&nbsb;".equals(urlQueryString))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (pageId != null && pageId == 1677950 && StringUtils.isNotBlank(sqr) && sqr.equals("postalCodeTestQuery")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// change pageId=5713 to isCorrespondingPageId(pageId, XOProcessorPageIds) on 2018-02-09
		if (pageId != null
				&& isCorrespondingPageId(pageId, XOProcessorPageIds)
				&& (sojPage == null || !isCorrespondingString(
				sojPage, pageLists))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if ((pageId == null || pageId != 2050757)&& StringUtils.isNotBlank(agentString)
				&& agentString.startsWith("eBayNioHttpClient")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (pageId != null && pageId == 2050867 && StringUtils.isNotBlank(urlQueryString)
				&& (urlQueryString.contains("json") || urlQueryString.startsWith("/local/availability"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// add the logic according to caleb's on 2018-02-09
		if (pageId != null && (pageId == 2052122||pageId==2050519) && StringUtils.isNotBlank(urlQueryString)
				&& urlQueryString.contains("json")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		if (StringUtils.isNotBlank(urlQueryString)
				&& ("null".equals(urlQueryString)  || "undefined".equals(urlQueryString)  || urlQueryString.endsWith(".gif") || urlQueryString.endsWith(".png")
				|| urlQueryString.endsWith(".pdf") || urlQueryString.endsWith(".jpeg") || urlQueryString.endsWith(".swf")
				|| urlQueryString.endsWith(".txt") || urlQueryString.endsWith(".wav") || urlQueryString.endsWith(".zip")
				|| urlQueryString.endsWith(".flv") || urlQueryString.endsWith(".ico") || urlQueryString.endsWith(".jpg"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// add on 2018-02-09
		if (pageId != null && pageId == 2050601 && (!StringUtils.isNotBlank(pageName) || !pageName.startsWith("FeedHome"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}

		if (pageId != null && pageId == 2054095 && (!StringUtils.isNotBlank(urlQueryString) || !urlQueryString.startsWith("/survey"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// add on 2018-02-09
		if (pageId != null && pageId == 2056116 && StringUtils.isNotBlank(urlQueryString) && (urlQueryString.startsWith("/itm/watchInline")||urlQueryString.startsWith("/itm/ajaxSmartAppBanner"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// add on 2018-02-09
		if (pageId != null && pageId == 2059707 && StringUtils.isNotBlank(urlQueryString) && urlQueryString.startsWith("/itm/delivery")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}

		// add on 2018-02-09
		if (pageId != null && pageId == 2052197 && StringUtils.isNotBlank(urlQueryString) &&(urlQueryString.contains("ImportHubItemDescription")||urlQueryString.contains("ImportHubCreateListing"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}

		// add on 2018-02-09
		if (pageId != null && (pageId == 2047935||pageId==2053898) && StringUtils.isNotBlank(webServer) && webServer.startsWith("reco.ebay.")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// add on 2018-02-09
		if (pageId != null && pageId == 2067339 && StringUtils.isNotBlank(urlQueryString) && urlQueryString.startsWith("/roverimp/0/0/9?")) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// add on 2018-02-09
		if (pageId != null && pageId == 2053898 && (!StringUtils.isNotBlank(urlQueryPage) || !isCorrespondingString(urlQueryPage,checkoutCleanList)||sojPage==null)) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// add on 2018-02-09
		if (pageId != null && pageId == 2056812 && (sojPage==null || (!"ryprender".equals(sojPage)&&!"cyprender".equals(sojPage)))){
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// add on 2018-02-09
		if (pageId != null && pageId == 2056116 && (!StringUtils.isNotBlank(pfn) || !"VI".equals(pfn))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		// add on 2018-02-09
		if (pageId != null && pageId == 2481888&&appId!=null&&appId==3564 && StringUtils.isNotBlank(agentString) && (agentString.startsWith("ebayUserAgent/eBayIOS")||agentString.startsWith("ebayUserAgent/eBayAndroid"))) {
			ubiEvent.setPartialValidPage(0);
			return;
		}
		ubiEvent.setPartialValidPage(1);
	}

	private boolean isCorrespondingPageId(Integer id, Integer[] pageIdList) {
		for (Integer pageId : pageIdList) {
			if (pageId.equals(id)) {
				return true;
			}
		}
		return false;
	}

	private boolean isCorrespondingString(String source, String[] matchStr) {
		for (String str : matchStr) {
			if (str.equals(source)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
		// nothing to do
	}
}
