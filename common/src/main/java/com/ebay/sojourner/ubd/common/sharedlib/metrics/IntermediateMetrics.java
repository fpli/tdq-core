package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.IsValidIPv4;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJCollapseWhiteSpace;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJGetUrlDomain;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJGetUrlParams;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJGetUrlPath;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJListGetValueByIndex;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJListLastElement;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNameValueParser;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJReplaceChar;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJURLDecodeEscape;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

// FIXME: refactor this class
@Data
public class IntermediateMetrics implements Serializable {

  private static final String CHANNEL = "chnl";
  private static final String EUID = "euid";
  private static final String MPPID = "mppid";
  private static final Integer ONE = 1;
  private static final String PREV = "prev";
  private static final String QUERY = "query";
  private static final String ROTID = "rotid";
  private static final String RURL = "rurl";
  private static final Integer SEVEN = 7;
  private static final String SID = "sid";
  private static final String SWD = "swd";
  private Set<Integer> agentExcludePageSet = null;
  private String imgMpxChnlSet1 = null;
  private String imgMpxChnlSet6 = null;
  private Set<Integer> mobilePageSet = null;
  //    private static Map<Long, String> mpxMap; // mpx channel id map
  private static Map<String, String> mpxMap;
  private Set<Integer> notifyCLickPageSet = null;
  private Set<Integer> notifyViewPageSet = null;
  private Set<Integer> roverPageSet = null;
  private Set<Integer> scPageSet1 = null;
  private Set<Integer> scPageSet2 = null;
  private StringBuilder stingBuilder = new StringBuilder();
  private Collection<String> tags = null;
  private String actualKeyword;
  private String boughtKeyword;
  private Integer channel;
  // para on each rover
  private Integer channelSequence;
  private String curAdme;
  private Long eventTS;
  // rover & mp
  private Integer finalMppId;
  private boolean findAgentString;
  private boolean findNotifyClick;
  private boolean findNotifyView;
  private boolean findRover3084;
  private boolean findRover3085;
  private boolean findRover3962;
  private boolean findRovercClick;
  private boolean findScEvent;
  private Integer firstMppId;
  private String futureAdme;
  private String imgMpxChannelId;
  private Map<String, String> kvMap;
  private Integer landPageID;
  /*
   * mobile pages 2051248,2051249,2050535,2057087,2054081,2059087,2056372,
   * 2052310,2054060,2053277,2058946,2054180,2050494,2050495,
   * 2058483,2050605,2050606,1673581,1698105,2034596,2041594,1677709
   */
  private Map<String, Integer> mppIdsFromMobileEvent;
  private Map<String, Integer> mppIdsFromRoverEvent;
  private String mpxChannelId;
  // Notify click
  private Long notifyClickTs;
  // notify view
  private Long notifyViewTs;
  private String prevAdme;
  private String refDomain;
  // first Sc event para
  private String referrer;
  private String refKeyword;
  private Long rotId;
  // first rover click para
  private String roverClickE;
  private String roverEntryEuid;
  private Long roverEntryTs;
  // page 3085
  private Long roverNsTs;
  private String roverOpenEuid;
  // page 3962
  private Long roverOpenTs;
  private String scEventE;
  private Integer searchAgentTypeId;
  // first not null agent string from valid event
  private Integer socialAgentTypeId;
  private Integer swd;
  // page 3084
  private Integer trackingPartner;
  private String url2Parse;

  public IntermediateMetrics() {
    roverPageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.ROVER_PAGES), Property.PROPERTY_DELIMITER);
    scPageSet1 =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.SCEVENT_EXCLUDE_PAGES1), Property.PROPERTY_DELIMITER);
    scPageSet2 =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.SCEVENT_EXCLUDE_PAGES2), Property.PROPERTY_DELIMITER);
    agentExcludePageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.AGENT_EXCLUDE_PAGES), Property.PROPERTY_DELIMITER);
    notifyCLickPageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.NOTIFY_CLICK_PAGES), Property.PROPERTY_DELIMITER);
    notifyViewPageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.NOTIFY_VIEW_PAGES), Property.PROPERTY_DELIMITER);
    mobilePageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.MOBILE_PAGES), Property.PROPERTY_DELIMITER);
    PropertyUtils.getIntegerSet(
        UBIConfig.getString(Property.NOTIFY_VIEW_PAGES), Property.PROPERTY_DELIMITER);
    tags =
        PropertyUtils.parseProperty(
            UBIConfig.getString(Property.PRELOAD_PAYLOAD_TAGS), Property.PROPERTY_DELIMITER);
    initLkp();
    initMetrics();
    stingBuilder.setLength(0);
    stingBuilder
        .append(".*/0/(")
        .append("4908/89969|")
        .append("710/(82157|63920|89230)|")
        .append("711/(87621|88535)|")
        .append("709/(18254|4989)|")
        .append("707/(52222|1174)|")
        .append("706/(83004|87751)|")
        .append("1185/18912|")
        .append("5221/29898|")
        .append("5222/36186")
        .append("){1}/.*");
    imgMpxChnlSet1 = stingBuilder.toString();

    stingBuilder.setLength(0);
    stingBuilder
        .append(".*/0/(")
        .append("705/53470|")
        .append("706/53473|")
        .append("707/53477|")
        .append("709/53476|")
        .append("710/(53481|5232)|")
        .append("711/(53200|1751)|")
        .append("724/53478|")
        .append("1185/53479|")
        .append("1346/(53482|53482)|")
        .append("1553/53471|")
        .append("4686/53472|")
        .append("5221/53469|")
        .append("5222/53480|")
        .append("5282/53468|")
        .append("){1}/.*");
    imgMpxChnlSet6 = stingBuilder.toString();

  }

  public void end(SessionAccumulator sessionAccumulator) {
    Integer mppIdFromMobile =
        this.mppIdsFromMobileEvent.get(sessionAccumulator.getUbiSession().getFirstAppId());
    if (mppIdFromMobile != null) {
      finalMppId = mppIdFromMobile;
      return;
    }

    Integer mppIdFromRover =
        this.mppIdsFromRoverEvent.get(sessionAccumulator.getUbiSession().getFirstAppId());
    if (mppIdFromRover != null) {
      finalMppId = mppIdFromRover;
      return;
    }
    finalMppId = -1;
  }

  public void feed(UbiEvent event) throws InterruptedException {
    SOJNameValueParser.getTagValues(event.getApplicationPayload(), tags, kvMap);

    // count every rover click
    if (isRoverClick(event)) {
      setMaxMppIds(event);
      if (!findRover3084 || !findRover3085 || !findRover3962) {
        setChannelSequence(parseChannelFromEvent(event));
        setRoverClickUrl2Parse(event);
      }
    }

    // check first sc click
    if (!findScEvent && isScEvent(event)) {
      setFindScEvent(true);
      setFirstScEventMetrics(event);
    }

    // check first rover click
    if (!findRovercClick && isRoverClick(event)) {
      setFindRovercClick(true);
      setFirstRoverClickMetrics(event);
    }

    // check first 3084/3085 click
    if ((isRover3084Click(event) || isRover3085Click(event)) && !findRover3084 && !findRover3085) {
      setFirstRoverClickRotId(event);
    }

    // check first 3084 click only
    if (!findRover3084 && isRover3084Click(event)) {
      setFindRover3084(true);
      setFirstRover3084Metrics(event);
      setFirstMppIdFromEvent(event);
    }

    // check first 3085 click only
    if (!findRover3085 && isRover3085Click(event)) {
      setFindRover3085(true);
      setFirstRover3085Metrics(event);
    }

    // check first 3962 click only
    if (!findRover3962 && isRover3962Click(event)) {
      setFindRover3962(true);
      setFirstRover3962Metrics(event);
    }

    // check first notify click
    if (!findNotifyClick && notifyCLickPageSet.contains(event.getPageId())) {
      setFindNotifyClick(true);
      this.setNotifyClickTS(event);
    }

    // check first notify view
    if (!findNotifyView && notifyViewPageSet.contains(event.getPageId())) {
      setFindNotifyView(true);
      this.setNotifyViewTS(event);
    }

    // check first not null agent_info from valid click
    if (!findAgentString
        && !event.isRdt()
        && !event.isIframe()
        && !agentExcludePageSet.contains(event.getPageId())
        && event.getAgentInfo() != null
        && !event.getAgentInfo().equals("Shockwave Flash")
        && !IsValidIPv4.isValidIP(event.getAgentInfo())) {
      setFindAgentString(true);
      setFirstScSocialAgentTypeId(event);
      setFirstScSearchAgentTypeId(event);
    }

    // get the mppid from mobile event
    if (mobilePageSet.contains(event.getPageId()) && event.getCobrand() == 6) {
      setMinMppIds(event);
    }
  }

  public void setActualKeyword(String actualKeyword) {
    if (actualKeyword != null) {
      this.actualKeyword = actualKeyword.toLowerCase();
    } else {
      this.actualKeyword = "";
    }
  }

  public String getBoughtKeyword() {
    return boughtKeyword;
  }

  public void setBoughtKeyword(String boughtKeyword) {
    this.boughtKeyword = boughtKeyword;
  }

  public Integer getChannel() {
    return channel;
  }

  public void setChannel(Integer channel) {
    this.channel = channel;
  }

  public Integer getChannelSequence() {
    return channelSequence;
  }

  public void setChannelSequence(Integer channelSequence) {
    this.channelSequence = channelSequence;
  }

  public String getCurAdme() {
    return curAdme;
  }

  public void setCurAdme(String curAdme) {
    if (curAdme == null) {
      curAdme = "";
    }
    this.curAdme = curAdme;
  }

  public Long getEventTS() {
    return eventTS;
  }

  public void setEventTS(Long scEventTS) {
    this.eventTS = scEventTS;
  }

  public Integer getFinalMppId() {
    return finalMppId;
  }

  public void setFinalMppId(Integer finalMppId) {
    this.finalMppId = finalMppId;
  }

  public Integer getFirstMppId() {
    return firstMppId;
  }

  public void setFirstMppId(Integer firstMppId) {
    this.firstMppId = firstMppId;
  }

  public Long getFirstNotifyTs() {
    if (this.notifyClickTs != null) {
      return this.notifyClickTs;
    } else {
      return this.notifyViewTs;
    }
  }

  public String getFutureAdme() {
    return futureAdme;
  }

  public void setFutureAdme(String futureAdme) {
    this.futureAdme = futureAdme;
  }

  public String getImgMpxChannelId() {
    return imgMpxChannelId;
  }

  public void setImgMpxChannelId(String imgMpxChannelId) {
    if (imgMpxChannelId != null) {
      this.imgMpxChannelId = imgMpxChannelId.toLowerCase();
    }
  }

  public Map<String, String> getKvMap() {
    return kvMap;
  }

  public void setKvMap(Map<String, String> kvMap) {
    this.kvMap = kvMap;
  }

  public Integer getLandPageID() {
    return landPageID;
  }

  public void setLandPageID(Integer landPageID) {
    if (landPageID != null) {
      this.landPageID = landPageID;
    }
  }

  public Map<String, Integer> getMppIdsFromMobileEvent() {
    return mppIdsFromMobileEvent;
  }

  public void setMppIdsFromMobileEvent(Map<String, Integer> mppIdsFromMobileEvent) {
    this.mppIdsFromMobileEvent = mppIdsFromMobileEvent;
  }

  public Map<String, Integer> getMppIdsFromRoverEvent() {
    return mppIdsFromRoverEvent;
  }

  public void setMppIdsFromRoverEvent(Map<String, Integer> mppIdsFromRoverEvent) {
    this.mppIdsFromRoverEvent = mppIdsFromRoverEvent;
  }

  public String getMpxChannelId() {
    return mpxChannelId;
  }

  public void setMpxChannelId(String mpxChannelId) {
    if (mpxChannelId != null) {
      this.mpxChannelId = mpxChannelId.toLowerCase();
    }
  }

  public Long getNotifyClickTs() {
    return notifyClickTs;
  }

  public void setNotifyClickTs(Long notifyClickTs) {
    this.notifyClickTs = notifyClickTs;
  }

  public Long getNotifyViewTs() {
    return notifyViewTs;
  }

  public void setNotifyViewTs(Long notifyViewTs) {
    this.notifyViewTs = notifyViewTs;
  }

  public String getPrevAdme() {
    return prevAdme;
  }

  public void setPrevAdme(String prevAdme) {
    this.prevAdme = prevAdme;
  }

  public String getRefDomain() {
    return refDomain;
  }

  public void setRefDomain(String refDomain) {
    if (refDomain != null) {
      this.refDomain = refDomain.toLowerCase();
    }
  }

  public String getReferrer() {
    return referrer;
  }

  public void setReferrer(String firstReferrer) {
    this.referrer = firstReferrer == null ? "" : firstReferrer;
  }

  public String getRefKeyword() {
    return refKeyword;
  }

  public void setRefKeyword(String refKeyword) {
    if (refKeyword != null) {
      this.refKeyword = refKeyword.toLowerCase();
    }
  }

  public Long getRotId() {
    return rotId;
  }

  public void setRotId(Long rot) {
    this.rotId = rot;
  }

  public String getRoverClickE() {
    return roverClickE;
  }

  public void setRoverClickE(String e) {
    if (e != null) {
      this.roverClickE = e.toLowerCase();
    }
  }

  public String getRoverEntryEuid() {
    return roverEntryEuid;
  }

  public void setRoverEntryEuid(String euid) {
    if (euid != null) {
      this.roverEntryEuid = euid.toLowerCase();
    }
  }

  public Long getRoverEntryTs() {
    return roverEntryTs;
  }

  public void setRoverEntryTs(Long roverTs) {
    this.roverEntryTs = roverTs;
  }

  public Long getRoverNsTs() {
    return roverNsTs;
  }

  public void setRoverNsTs(Long roverNsTs) {
    this.roverNsTs = roverNsTs;
  }

  public String getRoverOpenEuid() {
    return roverOpenEuid;
  }

  public void setRoverOpenEuid(String euid) {
    if (euid != null) {
      this.roverOpenEuid = euid.toLowerCase();
    }
  }

  public Long getRoverOpenTs() {
    return roverOpenTs;
  }

  public void setRoverOpenTs(Long roverOpenTs) {
    this.roverOpenTs = roverOpenTs;
  }

  public String getScEventE() {
    return scEventE;
  }

  public void setScEventE(String scEventE) {
    this.scEventE = scEventE;
  }

  public Integer getSearchAgentTypeId() {
    return searchAgentTypeId;
  }

  public void setSearchAgentTypeId(Integer searchAgentTypeId) {
    this.searchAgentTypeId = searchAgentTypeId;
  }

  public Integer getSocialAgentTypeId() {
    return socialAgentTypeId;
  }

  public void setSocialAgentTypeId(Integer socialAgentTypeId) {
    this.socialAgentTypeId = socialAgentTypeId;
  }

  public Integer getSwd() {
    return swd;
  }

  public void setSwd(Integer swd) {
    this.swd = swd;
  }

  public Integer getTrackingPartner() {
    return trackingPartner;
  }

  public void setTrackingPartner(Integer trackingPartner) {
    this.trackingPartner = trackingPartner;
  }

  public String getUrl2Parse() {
    return url2Parse;
  }

  public void setUrl2Parse(String url2Parse) {
    if (url2Parse != null) {
      // this.url2Parse = url2Parse.toLowerCase();
      this.url2Parse = url2Parse;
    }
  }

  public void initLkp() {
        if (mpxMap == null || mpxMap.size() < 1) {
          mpxMap = LkpManager.getInstance().getMpxMap();
        }
  }

  public void initMetrics() {
    this.findScEvent = false;
    this.findRovercClick = false;
    this.findRover3084 = false;
    this.findRover3085 = false;
    this.findRover3962 = false;
    this.findAgentString = false;
    this.findNotifyClick = false;
    this.findNotifyView = false;

    this.trackingPartner = null;
    this.referrer = "";
    this.rotId = null;
    this.url2Parse = "";
    this.refDomain = "";
    this.actualKeyword = "";
    this.boughtKeyword = "";
    this.roverClickE = "";
    this.scEventE = "";
    this.channel = null;
    this.roverEntryEuid = "";
    this.roverOpenEuid = "";
    this.mpxChannelId = "";
    this.imgMpxChannelId = "";
    this.refKeyword = "";
    this.landPageID = null;
    this.swd = null;
    this.socialAgentTypeId = null;
    this.searchAgentTypeId = null;
    this.curAdme = "";
    this.prevAdme = "";
    this.futureAdme = "";
    this.roverEntryTs = null;
    this.roverNsTs = null;
    this.roverOpenTs = null;
    this.notifyClickTs = null;
    this.notifyViewTs = null;

    if (kvMap == null) {
      kvMap = new HashMap<String, String>();
    } else if (kvMap.size() > 1) {
      kvMap.clear();
    }

    this.firstMppId = null;
    this.finalMppId = null;
    this.eventTS = null;
    this.channelSequence = null;

    if (this.mppIdsFromRoverEvent == null) {
      this.mppIdsFromRoverEvent = new HashMap<String, Integer>();
    } else {
      this.mppIdsFromRoverEvent.clear();
    }

    if (this.mppIdsFromMobileEvent == null) {
      this.mppIdsFromMobileEvent = new HashMap<String, Integer>();
    } else {
      this.mppIdsFromMobileEvent.clear();
    }
  }

  public boolean isRover3084Click(UbiEvent event) {
    if (event.getPageId() == Integer.MIN_VALUE) {
      return false;
    }

    return 3084 == event.getPageId();
  }

  public boolean isRover3085Click(UbiEvent event) {
    if (event.getPageId() == Integer.MIN_VALUE) {
      return false;
    }

    return event.getPageId() == 3085;
  }

  public boolean isRover3962Click(UbiEvent event) {
    if (event.getPageId() == Integer.MIN_VALUE) {
      return false;
    }
    return event.getPageId() == 3962;
  }

  public boolean isRoverClick(UbiEvent event) {
    return roverPageSet.contains(event.getPageId());
  }

  public boolean isScEvent(UbiEvent event) {
    Integer pageId = event.getPageId() == Integer.MIN_VALUE ? -99 : event.getPageId();

    return !event.isRdt()
        && !event.isIframe()
        // || urlQueryString.matches("(/roverimp|.*SojPageView).*")
        && !scPageSet1.contains(pageId)
        && !scPageSet2.contains(pageId);
  }

  public Integer parseChannelFromEvent(UbiEvent event) {
    Integer pageId = event.getPageId() == Integer.MIN_VALUE ? -99 : event.getPageId();
    Integer channel = null;

    try {
      channel = Integer.valueOf(kvMap.get(CHANNEL));
      return channel;
    } catch (NumberFormatException e) {
      // go next step if non-number
    } catch (NullPointerException e) {
      // go next step if no channel in app_payload
    }

    // if not in app_payload.chnl, then check rover urlstring
    if (roverPageSet.contains(pageId)) {
      try {
        channel =
            Integer.parseInt(
                SOJListGetValueByIndex.getValueByIndex(
                    SOJGetUrlPath.getUrlPath(("http://x.ebay.com" + event.getUrlQueryString())),
                    "/",
                    5));
        return channel;
      } catch (NumberFormatException e) {
        // go next step if non-number
      } catch (NullPointerException e) {
        // go next step if no channel in app_payload
      }

      if (channel == null) {
        try {
          channel =
              Integer.parseInt(
                  SOJListGetValueByIndex.getValueByIndex(
                      SOJGetUrlPath.getUrlPath(
                          ("http://x.ebay.com"
                              + SOJURLDecodeEscape.javaNetUrlDecode(
                              event.getUrlQueryString(), "UTF-8"))),
                      "/",
                      5));
          return channel;
        } catch (NumberFormatException e) {
          // go next step if non-number
        } catch (NullPointerException e) {
          // go next step if no channel in app_payload
        }
      }
    }

    return channel;
  }

  private void setFirstMppIdFromEvent(UbiEvent event) {
    try {
      this.firstMppId = Integer.valueOf(kvMap.get(MPPID));
    } catch (NumberFormatException e) {
      this.firstMppId = null;
    }
  }

  public void setFirstRover3084Metrics(UbiEvent event) throws InterruptedException {
    setFirstRoverEntryTs(event);
    // pls keep the order
    setRoverClickChannel(event);
    setFirstRoverClickSwd(event);
    setFirstRoverEntryEuid(event);
    setFirstRoverClickE(event);
    setFirstRoverClickMpxChannelId(event);
    setFirstRoverClickBoughtKeyword(event);
    setFirstRoverClickActualKeyword(event);
  }

  public void setFirstRover3085Metrics(UbiEvent event) {
    setFirstRoverNsTs(event);
  }

  public void setFirstRover3962Metrics(UbiEvent event) {
    setFirstRoverOpenTs(event);
    setFirstRoverOpenEuid(event);
  }

  // page 3084 only
  public void setFirstRoverClickActualKeyword(UbiEvent event) {
    String actualKeyword = null;
    String urlQueryString = event.getUrlQueryString();
    String url2Parse = this.getUrl2Parse();
    String refDomain = SOJGetUrlDomain.getUrlDomain(url2Parse);

    // ignore the non Latin char check since utf8 is used in String
    if (url2Parse == null || StringUtils.isBlank(urlQueryString)) {
      actualKeyword = "";
    } else if (refDomain.matches(".*\\.yahoo\\..*") && url2Parse.matches(".*/\\?p=us")) {
      actualKeyword = "";
    } else if (refDomain.matches(".*toolbar\\.google\\..*")) {
      actualKeyword = "ebay";
    } else if (refDomain.matches(".*aolsearch.*")) {
      actualKeyword =
          SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), QUERY);
    } else if (refDomain.matches(".*google.*")
        && url2Parse.matches(".*/imgres\\?.*")
        && url2Parse.matches(".*prev=.*")) {
      actualKeyword =
          SOJNameValueParser.getTagValue(
              SOJGetUrlParams.getUrlParams(
                  "http://www.gooogle.com"
                      + SOJURLDecodeEscape.javaNetUrlDecode(
                      SOJNameValueParser.getTagValue(
                          SOJGetUrlParams.getUrlParams(url2Parse), PREV),
                      "UTF-8")),
              "q");
    } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "q")
        != null) {
      actualKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "q");
    } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "p")
        != null) {
      actualKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "p");
    } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), QUERY)
        != null) {
      actualKeyword =
          SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), QUERY);
    } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "su")
        != null) {
      actualKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "su");
    } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "rdata")
        != null) {
      actualKeyword =
          SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "rdata");
    } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "searchfor")
        != null) {
      actualKeyword =
          SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "searchfor");
    } else if (refDomain.matches(".*www\\.ciao\\..*")
        && SOJGetUrlPath.getUrlPath(url2Parse).startsWith("/sr/q-")) {
      actualKeyword = SOJGetUrlPath.getUrlPath(url2Parse).substring(6);
    } else if (url2Parse.matches("http://.*\\.shopping\\.com/istlo.*")) {
      actualKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "mn");
    } else if (url2Parse.matches("http://.*\\.shopping\\.com/xFS.*")) {
      actualKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "KW");
    } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "term")
        != null) {
      actualKeyword =
          SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(url2Parse), "term");
    } else {
      actualKeyword = "";
    }

    if (StringUtils.isNotBlank(actualKeyword) && actualKeyword.contains("%")) {
      actualKeyword = SOJURLDecodeEscape.javaNetUrlDecode(actualKeyword, "UTF-8");
    }

    if (StringUtils.isNotBlank(actualKeyword) && actualKeyword.contains("+")) {
      actualKeyword = actualKeyword.replace("+", " ");
    }

    if (StringUtils.isNotBlank(actualKeyword) && actualKeyword.contains("  ")) {
      actualKeyword = SOJCollapseWhiteSpace.getString(actualKeyword);
    }

    if (StringUtils.isNotBlank(actualKeyword)) {
      setActualKeyword(actualKeyword);
    } else {
      setActualKeyword(getBoughtKeyword());
    }
  }

  // page 3084 only
  public void setFirstRoverClickBoughtKeyword(UbiEvent event) {
    String boughtKeyword = "";
    String urlQueryString = event.getUrlQueryString();

    if (urlQueryString == null) {
      return;
    }

    if (urlQueryString.contains("keyword=")) {
      boughtKeyword =
          SOJCollapseWhiteSpace.getString(
              SOJReplaceChar.replaceChar(
                  SOJURLDecodeEscape.javaNetUrlDecode(
                      SOJNameValueParser.getTagValue(
                          SOJGetUrlParams.getUrlParams("http://rover.ebay.com" + urlQueryString),
                          "keyword"),
                      "UTF-8"),
                  "+",
                  ' '));
    } else if (urlQueryString.contains("rawquery=")) {
      boughtKeyword =
          SOJCollapseWhiteSpace.getString(
              SOJReplaceChar.replaceChar(
                  SOJURLDecodeEscape.javaNetUrlDecode(
                      SOJNameValueParser.getTagValue(
                          SOJGetUrlParams.getUrlParams("http://rover.ebay.com" + urlQueryString),
                          "rawquery"),
                      "UTF-8"),
                  "+",
                  ' '));
    }

    boughtKeyword = boughtKeyword == null ? "" : boughtKeyword;

    if (boughtKeyword.length() > 100) {
      setBoughtKeyword(boughtKeyword.substring(0, 100));
    } else {
      setBoughtKeyword(boughtKeyword);
    }
  }

  public void setFirstRoverClickE(UbiEvent event) {
    Integer pageId = event.getPageId() == Integer.MIN_VALUE ? -99 : event.getPageId();
    String e = "";

    if (!pageId.equals(3962) && !(pageId.equals(3084) && SEVEN.equals(getChannelSequence()))) {
      return;
    }

    String sid = kvMap.get(SID);

    if (pageId.equals(3962)) {
      if (sid != null && sid.matches("(e|E).*")) {
        e = sid.substring(1);

        if (e.length() <= 18) {
          setRoverClickE(e.toLowerCase());
        } else {
          setRoverClickE(e.substring(0, 18).toLowerCase());
        }
      }
    } else {
      if (sid == null) {
        try {
          String urlQueryString = event.getUrlQueryString();
          sid = SOJListGetValueByIndex.getValueByIndex(urlQueryString, "/", 4);
        } catch (NumberFormatException ex) {
          // go next step if non-number
        } catch (NullPointerException ex) {
          // go next step if urlQueryString is null
        }
      }

      if (sid == null) {
        return;
      }

      if (sid.matches("(e|E).*\\..*")) {
        String tmp = SOJListGetValueByIndex.getValueByIndex(sid, "\\.", 1);
        e = (tmp == null) ? "" : tmp.substring(1);
      } else if (sid.matches("(e|E).*")) {
        e = sid.substring(1);
      }

      if (e.length() <= 9999) {
        setRoverClickE(e.toLowerCase());
      } else {
        setRoverClickE(e.substring(0, 9999).toLowerCase());
      }
    }
  }

  public void setFirstRoverClickMetrics(UbiEvent event) {
    // CAUTION: keep order for below metrics
    setFirstRoverClickTrackingPartner(event);
  }

  // page 3084 only
  public void setFirstRoverClickMpxChannelId(UbiEvent event) throws InterruptedException {
    Integer pageId = event.getPageId() == Integer.MIN_VALUE ? -99 : event.getPageId();
    mpxMap=LkpManager.getInstance().getMpxMap();
    String mpxChannelId = null;
    String[] channelIds = null;

    if (pageId != null && (pageId.equals(3085) || pageId.equals(3084))) {
      String rotationString = kvMap.get(ROTID);
      if (rotationString == null) {
        if (ONE.equals(getTrackingPartner())) {
          rotationString =
              SOJListGetValueByIndex.getValueByIndex(
                  SOJListGetValueByIndex.getValueByIndex(event.getUrlQueryString(), "/", 4),
                  "\\?",
                  1);
        }
      }

      try {
        Long rotationId = Long.parseLong(rotationString.replace("-", ""));
        channelIds = mpxMap.get(rotationId).split(":", 2);
        if ("137245".equals(channelIds[0])) {
          mpxChannelId = "97";
        } else {
          mpxChannelId = channelIds[1];
        }
      } catch (NullPointerException e) {
        // e.printStackTrace();
        mpxChannelId = null;
      } catch (NumberFormatException e) {
        // e.printStackTrace();
        mpxChannelId = null;
      } catch (ArrayIndexOutOfBoundsException e) {
        mpxChannelId = null;
      }
    }
    this.setMpxChannelId(mpxChannelId);
  }

  public void setFirstRoverClickRotId(UbiEvent event) {
    Integer pageId = event.getPageId() == Integer.MIN_VALUE ? -99 : event.getPageId();
    String rotId = null;
    String webServer = event.getWebServer();

    if ((pageId.equals(3084) && webServer != null && webServer.startsWith("r.ebay."))
        || ((pageId.equals(3085) || pageId.equals(3084)) && ONE.equals(getTrackingPartner()))) {
      rotId = kvMap.get(ROTID);

      if (rotId == null) {
        rotId = SOJListGetValueByIndex.getValueByIndex(event.getUrlQueryString(), "/", 4);
      }

      if (StringUtils.isNotBlank(rotId) && rotId.contains("-")) {
        rotId = rotId.replace("-", "");
      }
    }

    if (rotId != null) {
      try {
        this.setRotId(Long.valueOf(rotId));
      } catch (NumberFormatException e) {
        // skip
      }
    }
  }

  // page 3084 only
  public void setFirstRoverClickSwd(UbiEvent event) {
    String swd = null;
    try {
      if (event.getWebServer() == null) {
        return;
      }
      if (event.getWebServer().startsWith("r.ebay.")) {
        swd = kvMap.get(SWD);
        if (swd == null) {
          setSwd(null);
          return;
        }

        if (swd.length() > 20) {
          swd = swd.substring(0, 20);
        }

        setSwd(Integer.valueOf(swd));
      }
    } catch (NullPointerException e) {
      // skip exception if no swd or not number
      // e.printStackTrace();
    } catch (NumberFormatException e) {
      // skip exception if no swd or not number
      // e.printStackTrace();
    }
  }

  public void setFirstRoverClickTrackingPartner(UbiEvent event) {
    try {
      Integer trackingPartner =
          Integer.valueOf(
              SOJListGetValueByIndex.getValueByIndex(event.getUrlQueryString(), "/", 3));

      setTrackingPartner(trackingPartner);
    } catch (NullPointerException e1) {
      // skip
    } catch (NumberFormatException e2) {
      // skip
    } catch (ArrayIndexOutOfBoundsException e3) {
      // skip
    }
  }

  // page 3084 only
  public void setFirstRoverEntryEuid(UbiEvent event) {
    String euid = null;

    if (getChannel() == null || getChannel() != 7) {
      this.setRoverEntryEuid("");
      return;
    }

    // get EUID from app_payload
    euid = kvMap.get(EUID);
    if (StringUtils.isBlank(euid) || euid.length() != 32) {
      // if not found, check urlquerystring
      euid =
          SOJNameValueParser.getTagValue(
              SOJGetUrlParams.getUrlParams("http://x.ebay.com" + event.getUrlQueryString()), EUID);
      if (StringUtils.isBlank(euid) || euid.length() != 32) {
        euid = "";
      }
    }

    this.setRoverEntryEuid(euid);
  }

  public void setFirstRoverEntryTs(UbiEvent event) {
    setRoverEntryTs(event.getEventTimestamp());
  }

  public void setFirstRoverNsTs(UbiEvent event) {
    this.roverNsTs = event.getEventTimestamp();
  }

  // page 3962 only
  public void setFirstRoverOpenEuid(UbiEvent event) {
    String euid = null;

    euid = kvMap.get(EUID);
    if (StringUtils.isBlank(euid) || euid.length() != 32) {
      euid = "";
    }

    this.setRoverOpenEuid(euid);
  }

  public void setFirstRoverOpenTs(UbiEvent event) {
    this.roverOpenTs = event.getEventTimestamp();
  }

  public void setFirstScEventCurAdme(UbiEvent event) {
    String curAdme = null;
    String sspagename = null;
    String urlQueryString = event.getUrlQueryString();

    if (urlQueryString != null && urlQueryString.contains("_W0QQ")) {
      try {
        sspagename =
            SOJNameValueParser.getTagValue(
                SOJGetUrlParams.getUrlParams(
                    "http://x.ebay.com"
                        + SOJURLDecodeEscape.javaNetUrlDecode(
                        urlQueryString
                            .replace("_W0QQ", "?")
                            .replace("QQ", "&")
                            .replace('Z', '=')
                            .replace('Q', '%'),
                        "UTF-8")
                        .toLowerCase()),
                "sspagename")
                .toUpperCase();
      } catch (NullPointerException e) {
        sspagename = "";
      }
    } else {
      try {
        sspagename =
            SOJNameValueParser.getTagValue(
                SOJGetUrlParams.getUrlParams(
                    "http://x.ebay.com" + urlQueryString.toLowerCase()),
                "sspagename")
                .toUpperCase();
      } catch (NullPointerException e) {
        sspagename = "";
      }
    }

    if (sspagename.matches(".*ADME:.*:.*:.*")) {
      curAdme = SOJListGetValueByIndex.getValueByIndex(sspagename, ":", 3);
    }
    setCurAdme(curAdme);
  }

  public void setFirstScEventE(UbiEvent event) {
    // if event e is set first in rover click, it still can be reset in 1st sc click
    String e = "";

    String sid = kvMap.get(SID);

    if (sid != null && sid.matches("(e|E).*\\..*")) {
      String tmp = SOJListGetValueByIndex.getValueByIndex(sid, "\\.", 1);
      e = (tmp == null) ? "" : tmp.substring(1);
    } else if (sid != null && sid.matches("(e|E).*")) {
      e = sid.substring(1);
    }

    if (e.length() <= 9999) {
      setScEventE(e.toLowerCase());
    } else {
      setScEventE(e.substring(0, 9999).toLowerCase());
    }
  }

  public void setFirstScEventImgMpxChannelId(UbiEvent event) throws InterruptedException {
    mpxMap=LkpManager.getInstance().getMpxMap();
    String imgMpxChannelId = "";
    String referrer = getReferrer();

    if (referrer == null) {
      this.setImgMpxChannelId(referrer);
      return;
    }

    if (referrer.matches(".*img.*\\.mediaplex\\.com/.*")) {
      if (SOJGetUrlPath.getUrlPath(referrer).matches(imgMpxChnlSet1)) {
        imgMpxChannelId = "1";
      } else if (SOJGetUrlPath.getUrlPath(referrer).matches(imgMpxChnlSet6)) {
        imgMpxChannelId = "6";
      }
    }

    // check from mpx table by
    Long imgRotationId = null;
    String tmp = null;

    if (referrer.contains(".mediaplex.com")) {
      tmp =
          SOJListLastElement.getLastElement(
              SOJGetUrlPath.getUrlPath(referrer.replace('_', '?').replace('&', '?')), "/");

      if (tmp != null && tmp.matches(".*-.*-.*-.*")) {
        try {
          imgRotationId = Long.parseLong(tmp.replace("-", ""));
          imgMpxChannelId = mpxMap.get(imgRotationId).split(":", 2)[1];
        } catch (NullPointerException e) {
          // e.printStackTrace();
        } catch (NumberFormatException e) {
          // e.printStackTrace();
        }
      }
    }

    this.setImgMpxChannelId(imgMpxChannelId);
  }

  public void setFirstScEventLandPageID(UbiEvent event) {
    setLandPageID(event.getPageId());
  }

  public void setFirstScEventMetrics(UbiEvent event) throws InterruptedException {
    Long eventTS = event.getEventTimestamp();
    // pls keep the order
    setEventTS(eventTS);
    setFirstScEventLandPageID(event);
    setFirstScEventReferrer(event);
    setFirstScEventRefDomain(event);
    setFirstScEventImgMpxChannelId(event);
    setFirstScEventE(event);
    setFirstScEventRefKeyword(event);
    setFirstScEventCurAdme(event);
  }

  public void setFirstScEventRefDomain(UbiEvent event) {
    String refDomain = SOJGetUrlDomain.getUrlDomain(this.getReferrer());
    this.setRefDomain(refDomain);
  }

  public void setFirstScEventReferrer(UbiEvent event) {
    // check web_server first
    String webServer = event.getWebServer() == null ? "" : event.getWebServer();
    String urlQueryString = event.getUrlQueryString() == null ? "" : event.getUrlQueryString();
    String rurl = kvMap.get(RURL);
    String rurlDecode = null;

    if (webServer.matches("(.*rover\\.ebay\\.|sofe\\.ebay\\.).*")
        && urlQueryString.matches(".*(SojPageView|roverimp).*")) {
      if (StringUtils.isBlank(rurl)) {
        setReferrer(rurl);
        return;
      }
      // decode once
      rurl = SOJURLDecodeEscape.javaNetUrlDecode(rurl, "UTF-8");
      rurlDecode = rurl;
      if (rurlDecode != null && rurlDecode.matches("http(|s)://.*")) {
        setReferrer(rurlDecode);
        return;
      }
      // decode twice
      rurlDecode = SOJURLDecodeEscape.javaNetUrlDecode(rurlDecode, "UTF-8");
      if (rurlDecode != null && rurlDecode.matches("http(|s)://.*")) {
        setReferrer(rurlDecode);
        return;
      }
      // decode 3 times
      rurlDecode = SOJURLDecodeEscape.javaNetUrlDecode(rurlDecode, "UTF-8");
      if (rurlDecode != null && rurlDecode.matches("http(|s)://.*")) {
        setReferrer(rurlDecode);
        return;
      }

      setReferrer(rurl);
      return;
    }

    // set event.referrer
    String referrer = event.getReferrer();
    if (StringUtils.isBlank(referrer)) {
      setReferrer(referrer);
      return;
    }

    String referrerDecode;

    if (referrer == null || referrer.matches("http(|s)://.*")) {
      setReferrer(referrer);
      return;
    }

    referrerDecode = SOJURLDecodeEscape.javaNetUrlDecode(referrer, "UTF-8");
    if (referrerDecode != null && referrerDecode.matches("http(|s)://.*")) {
      setReferrer(referrerDecode);
    } else {
      setReferrer(referrer);
    }
  }

  public void setFirstScEventRefKeyword(UbiEvent event) {
    String refKeyword = null;
    String referrer = this.getReferrer();
    String refDomain = this.getRefDomain();

    if (referrer != null) {
      // ignore the non Latin char check since utf8 is used in String
      if (refDomain.matches(".*toolbar\\.google\\..*")) {
        refKeyword = "ebay";
      } else if (refDomain.matches("(.*aolsearch.*)|(.*search\\.aol\\..*)")
          && SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), QUERY)
          != null) {
        refKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), QUERY);
      } else if (referrer.matches(".*google.*/imgres\\?.*prev=.*")) {
        refKeyword =
            SOJNameValueParser.getTagValue(
                SOJGetUrlParams.getUrlParams(
                    "http://www.gooogle.com"
                        + SOJURLDecodeEscape.javaNetUrlDecode(
                        SOJNameValueParser.getTagValue(
                            SOJGetUrlParams.getUrlParams(referrer), PREV),
                        "UTF-8")),
                "q");
      } else if (referrer.endsWith("yahoo.com/?p=us")) {
        refKeyword = "";
      } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "q")
          != null) {
        refKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "q");
      } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "p")
          != null) {
        refKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "p");
      } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), QUERY)
          != null) {
        refKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), QUERY);
      } else if (referrer.contains("baidu")
          && SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "wd") != null) {
        refKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "wd");
      } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "su")
          != null) {
        refKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "su");
      } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "rdata")
          != null) {
        refKeyword =
            SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "rdata");
      } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "searchfor")
          != null) {
        refKeyword =
            SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "searchfor");
      } else if (referrer.matches(".*www\\.ciao\\..*/sr/q-.*")) {
        refKeyword = SOJGetUrlPath.getUrlPath(referrer).substring(6);
      } else if (referrer.matches(".*\\.shopping\\.com/istlo.*")
          && SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "mn") != null) {
        refKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "mn");
      } else if (referrer.matches(".*\\.shopping\\.com/xFS.*")
          && SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "KW") != null) {
        refKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "KW");
      } else if (SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "term")
          != null) {
        refKeyword = SOJNameValueParser.getTagValue(SOJGetUrlParams.getUrlParams(referrer), "term");
      } else {
        refKeyword = "";
      }

      if (refKeyword != null && refKeyword.contains("%")) {
        refKeyword = SOJURLDecodeEscape.javaNetUrlDecode(refKeyword, "UTF-8");
      }

      if (refKeyword != null && refKeyword.contains("+")) {
        refKeyword = refKeyword.replace("+", " ");
      }

      if (refKeyword != null && refKeyword.contains("  ")) {
        refKeyword = SOJCollapseWhiteSpace.getString(refKeyword);
      }
    }

    this.setRefKeyword(refKeyword);
  }

  public void setFirstScSearchAgentTypeId(UbiEvent event) {
    String agentInfo = event.getAgentInfo();
    if (agentInfo != null && agentInfo.contains("Quick Search Box")) {
      setSearchAgentTypeId(1);
    } else {
      setSearchAgentTypeId(null);
    }
  }

  public void setFirstScSocialAgentTypeId(UbiEvent event) {
    Integer id = null;
    String agentInfo = event.getAgentInfo();

    if (agentInfo.contains("FBAN") || agentInfo.matches("(Mozilla/5.0 .*\\[FBAN/|.*Instagram).*")) {
      id = 2;
    } else if (agentInfo.contains("Twitter")) {
      id = 3;
    } else if (agentInfo.contains("GooglePlus/")) {
      id = 10;
    }
    setSocialAgentTypeId(id);
  }

  private void setMaxMppIds(UbiEvent event) {
    Integer mppId = null;
    try {
      mppId =
          Integer.valueOf(SOJNameValueParser.getTagValue(event.getApplicationPayload(), "mppid"));
    } catch (NumberFormatException e) {
      try {
        mppId =
            Integer.valueOf(
                SOJNameValueParser.getTagValue(
                    SOJGetUrlParams.getUrlParams(
                        "http://rover.ebay.com" + event.getUrlQueryString()),
                    "mppid"));
      } catch (NumberFormatException e1) {
        return;
      }
    }
    Integer oldmppId = mppIdsFromRoverEvent.get(event.getAppId().toString());
    if (oldmppId == null || (mppId != null && mppId.compareTo(oldmppId) > 0)) {
      mppIdsFromRoverEvent.put(event.getAppId().toString(), mppId);
    }
  }

  private void setMinMppIds(UbiEvent event) {
    Integer mppId = null;
    try {
      mppId =
          Integer.valueOf(SOJNameValueParser.getTagValue(event.getApplicationPayload(), "mppid"));
    } catch (NumberFormatException e) {
      return;
    }
    Integer oldmppId = mppIdsFromMobileEvent.get(event.getAppId().toString());
    if (oldmppId == null || (mppId != null && mppId.compareTo(oldmppId) < 0)) {
      mppIdsFromMobileEvent.put(event.getAppId().toString(), mppId);
    }
  }

  public void setNotifyClickTS(UbiEvent event) {
    this.notifyClickTs = event.getEventTimestamp();
  }

  public void setNotifyViewTS(UbiEvent event) {
    this.notifyViewTs = event.getEventTimestamp();
  }

  // rover 3084 channel
  public void setRoverClickChannel(UbiEvent event) {
    this.setChannel(parseChannelFromEvent(event));
  }

  public void setRoverClickUrl2Parse(UbiEvent event) {
    Integer pageId = event.getPageId() == Integer.MIN_VALUE ? -99 : event.getPageId();
    String url2Parse = "";
    String urlQueryString = event.getUrlQueryString();

    if (pageId.equals(3084)) {
      url2Parse = event.getReferrer();
    } else if (pageId.equals(3085) && urlQueryString != null) {
      if (urlQueryString.indexOf("&mpvl=") >= 0) {
        url2Parse = urlQueryString.substring(urlQueryString.indexOf("&mpvl=") + 6);
      }
    }

    this.setUrl2Parse(url2Parse);
  }

  public void start(UbiEvent event) {
    // null
  }


}
