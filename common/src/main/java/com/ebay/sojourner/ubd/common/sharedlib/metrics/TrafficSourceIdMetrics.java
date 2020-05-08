package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJGetUrlPath;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class TrafficSourceIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {


  private static final Long INITIALSESSIONDATE = 3542227200000000L;
  private static final Long SESCOND1 = 1000000L;
  private static final Long SECOND3 = 3L * SESCOND1;
  private static final Long SECOND8 = 8L * SESCOND1;
  private static final Long SECOND10 = 10L * SESCOND1;
  private static final Long SECOND30 = 30L * SESCOND1;
  private static final Long SECOND180 = 180L * SESCOND1;
  private static final Long SOJMAXLONG = Long.MAX_VALUE - SESCOND1 * 365L * 24L * 3600L;
  private static final Integer ONE = 1;
  private static final Integer FIVE = 5;
  private static final Integer SEVEN = 7;
  private static final Integer EIGHT = 8;
  private static final Integer NINE = 9;
  private static final String ebayStr =
      "(.*(ebay|e_bay|e__bay).*)|(e)|(eb)|(eba)|(eybe)|(bay)|(eby)|(eaby)|(eay)";
  private static final String kijijiStr =
      ".*(kijiji|e.*bay.*classified|kleinanzeigen|ebay klein).*";
  private static String regString4Id21;
  private static String regString4Id22;
  private static String regString4Id21_1;
  private static String regString4Id23;
  private static Set<Integer> landPageSet1 = new HashSet<>();
  private static Set<Integer> landPageSet2 = new HashSet<>();
  private static Set<Integer> swdSet = new HashSet<>();
  private static Set<Long> rotSet = new HashSet<>();
  private static Set<Integer> socialAgentId22 = new HashSet<>();
  private static Set<Integer> socialAgentId23 = new HashSet<>();
  private static StringBuilder stingBuilder = new StringBuilder();

  static {
    stingBuilder
        .append("(.*(")
        .append("popular|compare|achat-vente|affari|")
        .append("einkaufstipps|top-themen|trucos|usato")
        .append(")|")
        .append("preisvergleich)\\.ebay\\..*");
    regString4Id21 = stingBuilder.toString();
    stingBuilder.setLength(0);
    stingBuilder
        .append("t\\.co|")
        .append("vk\\.com|")
        .append(".*so\\.cl|")
        .append(".*(facebook|")
        .append("twitter|")
        .append("\\.myspace|")
        .append("\\.linkedin|")
        .append("\\.tencent|")
        .append("buzz\\.google|")
        .append("vkontakte|")
        .append("\\.orkut|")
        .append("\\.bebo|")
        .append("plus\\..*google|")
        .append("pinterest)\\..*");
    regString4Id22 = stingBuilder.toString();

    stingBuilder.setLength(0);
    stingBuilder
        .append("(search\\.(bt|icq)\\..*)|")
        .append("(.*(\\.tiscali\\.co\\.uk|\\.mywebsearch\\.com))|")
        .append("suche\\.web\\.de|")
        .append("(.*(\\.google\\.|\\.yahoo\\.|search.*\\.live\\.|")
        .append("search.*\\.msn\\.|\\.bing\\.|\\.ask\\.|\\.baidu\\.|")
        .append("search\\.juno\\.|search\\.comcast\\.|suche\\.t-online\\.|")
        .append("search\\.virginmedia|search\\.orange\\.|search\\.aol\\.|")
        .append("search.*\\.sky\\.|suche\\.aol|search\\..*voila\\.|")
        .append("search\\.conduit\\.|\\.excite\\.|\\.ciao\\.|duckduckgo\\.|yandex\\.).*)");
    regString4Id21_1 = stingBuilder.toString();

    stingBuilder.setLength(0);
    stingBuilder
        .append("(.*\\.(youtube|blogspot|blogger|stumbleupon|tumblr|")
        .append("livejournal|wordpress|typepad)|")
        .append("reddit)\\..*");
    regString4Id23 = stingBuilder.toString();
  }

  private volatile IntermediateMetrics intermediateMetrics;

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().getIntermediateMetrics().end(sessionAccumulator);
    sessionAccumulator.getUbiSession().setTrafficSrcId(getTrafficSourceId(sessionAccumulator));
  }

  @Override
  public void feed(UbiEvent ubiEvent, SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().getIntermediateMetrics().feed(ubiEvent,sessionAccumulator);
  }

  private int getTrafficSourceId(SessionAccumulator sessionAccumulator) {
    IntermediateMetrics intermediateMetrics = sessionAccumulator.getUbiSession()
        .getIntermediateMetrics();

    Integer firstCobrand = sessionAccumulator.getUbiSession().getFirstCobrand();
    Long startTSOnCurrentCobrandSite =
        intermediateMetrics.getEventTS() == null ? SOJMAXLONG : intermediateMetrics.getEventTS();

    // calculate the traffic source id base on intermedia parameters
    if (("11030".equals(intermediateMetrics.getScEventE())
        || "11030".equals(intermediateMetrics.getRoverClickE()))
        && INITIALSESSIONDATE.compareTo(startTSOnCurrentCobrandSite) <= 0) {
      return 23;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && EIGHT.equals(intermediateMetrics.getChannel())) {
      return 19;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && SEVEN.equals(intermediateMetrics.getChannel())) {
      return 4;
    }
    if (StringUtils.isNotBlank(intermediateMetrics.getScEventE())) {
      return 4;
    }
    if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "25".equals(intermediateMetrics.getMpxChannelId())
        && intermediateMetrics.getActualKeyword().contains("half")) {
      return 6;
    }
    if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "2".equals(intermediateMetrics.getMpxChannelId())
        && intermediateMetrics.getActualKeyword().contains("half")) {
      return 7;
    }
    if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "26".equals(intermediateMetrics.getMpxChannelId())
        && intermediateMetrics.getActualKeyword().contains("half")) {
      return 9;
    }
    if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "25".equals(intermediateMetrics.getMpxChannelId())
        && intermediateMetrics.getActualKeyword().matches(kijijiStr)) {
      return 6;
    }
    if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "2".equals(intermediateMetrics.getMpxChannelId())
        && intermediateMetrics.getActualKeyword().matches(kijijiStr)) {
      return 7;
    }
    if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "26".equals(intermediateMetrics.getMpxChannelId())
        && intermediateMetrics.getActualKeyword().matches(kijijiStr)) {
      return 9;
    }
    if (!FIVE.equals(firstCobrand)
        && !NINE.equals(firstCobrand)
        && !ONE.equals(firstCobrand)
        && !EIGHT.equals(firstCobrand)
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && ("2".equals(intermediateMetrics.getMpxChannelId())
        || "14".equals(intermediateMetrics.getMpxChannelId()))
        && intermediateMetrics.getActualKeyword().matches(ebayStr)
        && rotSet.contains(intermediateMetrics.getRotId())) {
      return 29;
    }
    if (!FIVE.equals(firstCobrand)
        && !NINE.equals(firstCobrand)
        && !ONE.equals(firstCobrand)
        && !EIGHT.equals(firstCobrand)
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "25".equals(intermediateMetrics.getMpxChannelId())
        && intermediateMetrics.getActualKeyword().matches(ebayStr)) {
      return 6;
    }
    if (!FIVE.equals(firstCobrand)
        && !NINE.equals(firstCobrand)
        && !ONE.equals(firstCobrand)
        && !EIGHT.equals(firstCobrand)
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "2".equals(intermediateMetrics.getMpxChannelId())
        && intermediateMetrics.getActualKeyword().matches(ebayStr)) {
      return 7;
    }
    if (!FIVE.equals(firstCobrand)
        && !NINE.equals(firstCobrand)
        && !ONE.equals(firstCobrand)
        && !EIGHT.equals(firstCobrand)
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "26".equals(intermediateMetrics.getMpxChannelId())
        && intermediateMetrics.getActualKeyword().matches(ebayStr)) {
      return 9;
    }
    if (!FIVE.equals(firstCobrand)
        && !NINE.equals(firstCobrand)
        && !ONE.equals(firstCobrand)
        && !EIGHT.equals(firstCobrand)
        && intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && ("2".equals(intermediateMetrics.getMpxChannelId())
        || "14".equals(intermediateMetrics.getMpxChannelId()))
        && rotSet.contains(intermediateMetrics.getRotId())) {
      return 28;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "25".equals(intermediateMetrics.getMpxChannelId())) {
      return 18;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "2".equals(intermediateMetrics.getMpxChannelId())) {
      return 10;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "6".equals(intermediateMetrics.getMpxChannelId())) {
      return 12;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "1".equals(intermediateMetrics.getMpxChannelId())) {
      return 11;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "32".equals(intermediateMetrics.getMpxChannelId())) {
      return 30;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "9".equals(intermediateMetrics.getMpxChannelId())) {
      return 13;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "15".equals(intermediateMetrics.getMpxChannelId())) {
      return 14;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "16".equals(intermediateMetrics.getMpxChannelId())) {
      return 15;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "17".equals(intermediateMetrics.getMpxChannelId())) {
      return 16;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "23".equals(intermediateMetrics.getMpxChannelId())) {
      return 17;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "26".equals(intermediateMetrics.getMpxChannelId())) {
      return 20;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "27".equals(intermediateMetrics.getMpxChannelId())) {
      return 25;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "97".equals(intermediateMetrics.getMpxChannelId())) {
      return 26;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && "33".equals(intermediateMetrics.getMpxChannelId())) {
      return 31;
    }
    if ("1".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 11;
    }
    if ("32".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 30;
    }
    if ("6".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 12;
    }
    if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
        && "25".equals(intermediateMetrics.getImgMpxChannelId())
        && intermediateMetrics.getRefKeyword().contains("half")) {
      return 6;
    }
    if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
        && "25".equals(intermediateMetrics.getImgMpxChannelId())
        && intermediateMetrics.getRefKeyword().matches(kijijiStr)) {
      return 6;
    }
    if (!FIVE.equals(firstCobrand)
        && !NINE.equals(firstCobrand)
        && !ONE.equals(firstCobrand)
        && !EIGHT.equals(firstCobrand)
        && "25".equals(intermediateMetrics.getImgMpxChannelId())
        && intermediateMetrics.getRefKeyword().matches(ebayStr)) {
      return 6;
    }
    if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
        && "2".equals(intermediateMetrics.getImgMpxChannelId())
        && intermediateMetrics.getRefKeyword().contains("half")) {
      return 7;
    }
    if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
        && "2".equals(intermediateMetrics.getImgMpxChannelId())
        && intermediateMetrics.getRefKeyword().matches(kijijiStr)) {
      return 7;
    }
    if (!FIVE.equals(firstCobrand)
        && !NINE.equals(firstCobrand)
        && !ONE.equals(firstCobrand)
        && !EIGHT.equals(firstCobrand)
        && "2".equals(intermediateMetrics.getImgMpxChannelId())
        && intermediateMetrics.getRefKeyword().matches(ebayStr)) {
      return 7;
    }
    if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
        && "26".equals(intermediateMetrics.getImgMpxChannelId())
        && intermediateMetrics.getRefKeyword().contains("half")) {
      return 9;
    }
    if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
        && "26".equals(intermediateMetrics.getImgMpxChannelId())
        && intermediateMetrics.getRefKeyword().matches(kijijiStr)) {
      return 9;
    }
    if (!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
        && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand)
        && "26".equals(intermediateMetrics.getImgMpxChannelId())
        && intermediateMetrics.getRefKeyword().matches(ebayStr)) {
      return 9;
    }
    if ("25".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 18;
    }
    if ("2".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 10;
    }
    if ("9".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 13;
    }
    if ("15".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 14;
    }
    if ("16".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 15;
    }
    if ("17".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 16;
    }
    if ("23".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 17;
    }
    if ("26".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 20;
    }
    if ("27".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 25;
    }
    if (intermediateMetrics.getRefDomain().startsWith("itemlistings.ebay.")) {
      return 21;
    }
    if (intermediateMetrics.getRefDomain().matches(regString4Id21)) {
      return 21;
    }
    if (intermediateMetrics.getRefDomain().contains(".craigslist.")) {
      return 24;
    }
    if (intermediateMetrics.getRefDomain().matches(".*toolbar.*\\.google\\..*")
        && landPageSet1.contains(intermediateMetrics.getLandPageID())) {
      return 8;
    }
    if (intermediateMetrics.getRefDomain().matches(".*toolbar.*\\.google\\..*")) {
      return 21;
    }
    if (intermediateMetrics.getRefDomain().matches(regString4Id22)) {
      return 22;
    }
    if ("13".equals(intermediateMetrics.getImgMpxChannelId())) {
      return 23;
    }
    if (intermediateMetrics.getRefDomain().contains("email")) {
      return 5;
    }
    if (intermediateMetrics.getRefDomain().matches(".*\\.(google|googleusercontent)\\..*")
        && intermediateMetrics.getRefKeyword().equals("")
        && !SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer())
        .matches("/url|/imgres|/search|/")) {
      return 24;
    }
    if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
        && intermediateMetrics.getRefDomain().contains(".google.")
        && SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer()).startsWith("/products")
        && intermediateMetrics.getRefKeyword().contains("half")) {
      return 9;
    }
    if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
        && intermediateMetrics.getRefDomain().contains(".google.")
        && SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer()).startsWith("/products")
        && intermediateMetrics.getRefKeyword().matches(".*(kijiji|e.*bay.*classified).*")) {
      return 9;
    }
    if (!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
        && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand)
        && intermediateMetrics.getRefDomain().contains(".google.")
        && SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer()).startsWith("/products")
        && intermediateMetrics.getRefKeyword().matches(ebayStr)) {
      return 9;
    }
    if (intermediateMetrics.getRefDomain().contains(".google.")
        && SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer()).startsWith("/products")
        && intermediateMetrics.getRefKeyword().matches(".*(kijiji|e.*bay.*classified).*")) {
      return 20;
    }
    if (intermediateMetrics.getRefDomain().contains(".yahoo.") &&
        !intermediateMetrics.getRefDomain().contains("search.yahoo.")) {
      return 24;
    }
    if (intermediateMetrics.getRoverNsTs() != null
        && intermediateMetrics.getRoverNsTs() <= startTSOnCurrentCobrandSite + SECOND10
        && intermediateMetrics.getRoverNsTs() >= startTSOnCurrentCobrandSite + SECOND3
        && "3".equals(intermediateMetrics.getMpxChannelId())) {
      if (landPageSet1.contains(intermediateMetrics.getLandPageID())) {
        return 8;
      }
      return 21;
    }
    if (intermediateMetrics.getRefDomain().matches(regString4Id21_1)) {
      if (landPageSet1.contains(intermediateMetrics.getLandPageID())) {
        return 8;
      }
      return 21;
    }
    if (StringUtils.isBlank(intermediateMetrics.getRefDomain())
        && intermediateMetrics.getRoverOpenTs() != null
        && intermediateMetrics.getRoverOpenTs() <= startTSOnCurrentCobrandSite + SESCOND1
        && intermediateMetrics.getRoverOpenTs() >= startTSOnCurrentCobrandSite - SECOND30
        && StringUtils.isNotBlank(intermediateMetrics.getRoverOpenEuid())) {
      return 4;
    }
    if (intermediateMetrics.getRefDomain().matches(".*(ebay|\\.kijiji\\.).*")) {
      return 2;
    }
    if (intermediateMetrics.getRoverEntryTs() != null && intermediateMetrics
        .getRoverEntryTs() <= startTSOnCurrentCobrandSite + SECOND8
        && intermediateMetrics
        .getRoverEntryTs() >= startTSOnCurrentCobrandSite - SECOND30 && "13"
        .equals(intermediateMetrics.getMpxChannelId()) && swdSet
        .contains(intermediateMetrics.getSwd())) {
      return 22;
    }
    if (intermediateMetrics.getRefDomain().matches(regString4Id23)) {
      return 23;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SECOND8
        && intermediateMetrics.getRoverEntryTs() >= startTSOnCurrentCobrandSite - SECOND30
        && "13".equals(intermediateMetrics.getMpxChannelId())) {
      return 23;
    }
    if (intermediateMetrics.getRoverNsTs() != null
        && intermediateMetrics.getRoverNsTs() <= startTSOnCurrentCobrandSite + SECOND10
        && intermediateMetrics.getRoverNsTs() >= startTSOnCurrentCobrandSite - SECOND3
        && "13".equals(intermediateMetrics.getMpxChannelId())) {
      return 23;
    }
    if (socialAgentId22.contains(intermediateMetrics.getSocialAgentTypeId())) {
      return 22;
    }
    if (socialAgentId23.contains(intermediateMetrics.getSocialAgentTypeId())) {
      return 23;
    }
    if (intermediateMetrics.getSearchAgentTypeId() != null
        && landPageSet1.contains(intermediateMetrics.getLandPageID())) {
      return 8;
    }
    if (intermediateMetrics.getSearchAgentTypeId() != null) {
      return 21;
    }
    if (intermediateMetrics.getFirstNotifyTs() != null
        && intermediateMetrics.getFirstNotifyTs() <= startTSOnCurrentCobrandSite + SECOND3
        && intermediateMetrics.getFirstNotifyTs() >= startTSOnCurrentCobrandSite - SECOND180) {
      return 27;
    }
    if (intermediateMetrics.getRoverEntryTs() != null
        && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SECOND10
        && intermediateMetrics.getRoverEntryTs() >= startTSOnCurrentCobrandSite - SECOND30
        && intermediateMetrics.getFirstMppId() != null
        && intermediateMetrics.getFirstMppId() > 0) {
      return 14;
    }
    if (intermediateMetrics.getFinalMppId() != null
        && intermediateMetrics.getFinalMppId() > 0) {
      return 14;
    }
    if (StringUtils.isBlank(intermediateMetrics.getRefDomain())) {
      return 1;
    }
    if (intermediateMetrics.getCurAdme().startsWith("TB")) {
      return 3;
    }
    if (!landPageSet2.contains(intermediateMetrics.getLandPageID())
        || !intermediateMetrics.getPrevAdme().startsWith("TB")) {
      if (StringUtils.isNotBlank(intermediateMetrics.getRefDomain())) {
        return 24;
      }
      if (StringUtils.isNotBlank(intermediateMetrics.getCurAdme())
          && !intermediateMetrics.getCurAdme().startsWith("TB")) {
        return 4;
      }
      if (!StringUtils.isNotBlank(intermediateMetrics.getPrevAdme())
          || intermediateMetrics.getPrevAdme().startsWith("TB")) {
        if (!landPageSet2.contains(intermediateMetrics.getLandPageID())
            || !StringUtils.isNotBlank(intermediateMetrics.getPrevAdme())
            || intermediateMetrics.getPrevAdme().startsWith("TB")) {
          if (!landPageSet2.contains(intermediateMetrics.getLandPageID())
              || !StringUtils.isNotBlank(intermediateMetrics.getFutureAdme())
              || !intermediateMetrics.getFutureAdme().startsWith("TB")) {
            ;
          }
        }
      }
    }
    return Integer.MIN_VALUE;
  }

  @Override
  public void init() throws Exception {
    // init constants from property
    landPageSet1 = PropertyUtils.getIntegerSet(
        UBIConfig.getString(Property.LAND_PAGES1), Property.PROPERTY_DELIMITER);
    landPageSet2 = PropertyUtils.getIntegerSet(
        UBIConfig.getString(Property.LAND_PAGES2), Property.PROPERTY_DELIMITER);
    swdSet = PropertyUtils.getIntegerSet(
        UBIConfig.getString(Property.SWD_VALUES), Property.PROPERTY_DELIMITER);
    rotSet = PropertyUtils.getLongSet(
        UBIConfig.getString(Property.ROT_VALUES), Property.PROPERTY_DELIMITER);
    socialAgentId22 = PropertyUtils.getIntegerSet(
        UBIConfig.getString(Property.SOCIAL_AGENT_ID22), Property.PROPERTY_DELIMITER);
    socialAgentId23 = PropertyUtils.getIntegerSet(
        UBIConfig.getString(Property.SOCIAL_AGENT_ID23), Property.PROPERTY_DELIMITER);
  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    intermediateMetrics = new IntermediateMetrics();
    intermediateMetrics.initMetrics();
    sessionAccumulator.getUbiSession().setIntermediateMetrics(intermediateMetrics);

  }

}
