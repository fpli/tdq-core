package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJGetUrlPath;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yunjzhang
 * init version by Daniel:   logic from BI sql
 * 2014/4/9 by Daniel:   new traffic source id (30,31)
 * new landing page id (2056089,2057337,2059705)
 * *.yandex.ru to *.yandex.*
 * roverentry_src_string.mppid > 0 THEN 14
 * session_details.mppid > 0 THEN 14
 */
public class TrafficSourceIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    public static final Long INITIALSESSIONDATE = 3542227200000000L; // 2012-04-01
    public static final Long SESCOND1 = 1000000L;
    private static final Long SECOND3 = 3 * SESCOND1;
    public static final Long SECOND8 = 8 * SESCOND1;
    private static final Long SECOND10 = 10 * SESCOND1;
    private static final Long SECOND30 = 30 * SESCOND1;
    private static final Long SECOND180 = 180 * SESCOND1;
    private static UBIConfig ubiConfig;
    // since the init value need to add before a valid event appears,
    // minus 1 year for safe purpose
    public static final Long SOJMAXLONG = Long.MAX_VALUE - SESCOND1 * 365 * 24 * 3600;
    public static final Integer ONE = 1;
    public static final Integer FIVE = 5;
    public static final Integer SEVEN = 7;
    public static final Integer EIGHT = 8;
    public static final Integer NINE = 9;

    //TODO: move to configure file
    private static final String ebayStr =
            "(.*(ebay|e_bay|e__bay).*)|(e)|(eb)|(eba)|(eybe)|(bay)|(eby)|(eaby)|(eay)";
    private static final String kijijiStr =
            ".*(kijiji|e.*bay.*classified|kleinanzeigen|ebay klein).*";

    private static String regString4Id21 = null;
    private static String regString4Id22 = null;
    private static String regString4Id21_1 = null;
    private static String regString4Id23 = null;

    private static Set<Integer> landPageSet1 = new HashSet<Integer>();
    private static Set<Integer> landPageSet2 = new HashSet<Integer>();
    private static Set<Integer> swdSet = new HashSet<Integer>();
    private static Set<Long> rotSet = new HashSet<Long>();
    private static Set<Integer> socialAgentId22 = new HashSet<Integer>();
    private static Set<Integer> socialAgentId23 = new HashSet<Integer>();

    public static String getEbaystr() {
        return ebayStr;
    }

    public static String getKijijistr() {
        return kijijiStr;
    }

    static Set<Integer> getLandPageSet1() {
        return landPageSet1;
    }

    static Set<Integer> getLandPageSet2() {
        return landPageSet2;
    }

    public static String getRegString4Id21() {
        return regString4Id21;
    }

    public static String getRegString4Id21_1() {
        return regString4Id21_1;
    }

    public static String getRegString4Id22() {
        return regString4Id22;
    }

    public static String getRegString4Id23() {
        return regString4Id23;
    }

    static Set<Long> getRotSet() {
        return rotSet;
    }

    static Set<Integer> getSwdSet() {
        return swdSet;
    }

    static void setLandPageSet1(HashSet<Integer> pageIdSet1) {
        TrafficSourceIdMetrics.landPageSet1 = pageIdSet1;
    }

    static void setLandPageSet2(HashSet<Integer> pageIdSet2) {
        TrafficSourceIdMetrics.landPageSet2 = pageIdSet2;
    }

    public static void setRegString4Id21(String regString4Id21) {
        TrafficSourceIdMetrics.regString4Id21 = regString4Id21;
    }

    public static void setRegString4Id21_1(String regString4Id21_1) {
        TrafficSourceIdMetrics.regString4Id21_1 = regString4Id21_1;
    }

    public static void setRegString4Id22(String regString4Id22) {
        TrafficSourceIdMetrics.regString4Id22 = regString4Id22;
    }

    public static void setRegString4Id23(String regString4Id23) {
        TrafficSourceIdMetrics.regString4Id23 = regString4Id23;
    }

    static void setRotSet(HashSet<Long> rotSet) {
        TrafficSourceIdMetrics.rotSet = rotSet;
    }

    static void setSwdSet(HashSet<Integer> swdSet) {
        TrafficSourceIdMetrics.swdSet = swdSet;
    }

//    private Long startTSOnCurrentCobrandSite = null;
//
//    private IntermediateMetrics intermediateMetrics = null;
//
//    private Integer firstCobrand = null;

    private static StringBuilder stingBuilder = new StringBuilder();

    static {
        stingBuilder.append("(.*(").append("popular|compare|achat-vente|affari|")
                .append("einkaufstipps|top-themen|trucos|usato").append(")|")
                .append("preisvergleich)\\.ebay\\..*");
        regString4Id21 = stingBuilder.toString();

        stingBuilder.setLength(0);
        stingBuilder.append("t\\.co|").append("vk\\.com|").append(".*so\\.cl|").append(".*(facebook|")
                .append("twitter|").append("\\.myspace|").append("\\.linkedin|").append("\\.tencent|")
                .append("buzz\\.google|").append("vkontakte|").append("\\.orkut|").append("\\.bebo|")
                .append("plus\\..*google|").append("pinterest)\\..*");
        regString4Id22 = stingBuilder.toString();

        stingBuilder.setLength(0);
        stingBuilder.append("(search\\.(bt|icq)\\..*)|")
                .append("(.*(\\.tiscali\\.co\\.uk|\\.mywebsearch\\.com))|")
                .append("suche\\.web\\.de|").append("(.*(\\.google\\.|\\.yahoo\\.|search.*\\.live\\.|")
                .append("search.*\\.msn\\.|\\.bing\\.|\\.ask\\.|\\.baidu\\.|")
                .append("search\\.juno\\.|search\\.comcast\\.|suche\\.t-online\\.|")
                .append("search\\.virginmedia|search\\.orange\\.|search\\.aol\\.|")
                .append("search.*\\.sky\\.|suche\\.aol|search\\..*voila\\.|")
                .append("search\\.conduit\\.|\\.excite\\.|\\.ciao\\.|duckduckgo\\.|yandex\\.).*)");
        regString4Id21_1 = stingBuilder.toString();

        stingBuilder.setLength(0);
        stingBuilder.append("(.*\\.(youtube|blogspot|blogger|stumbleupon|tumblr|")
                .append("livejournal|wordpress|typepad)|").append("reddit)\\..*");
        regString4Id23 = stingBuilder.toString();
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) throws Exception {
        sessionAccumulator.getUbiSession().getIntermediateMetrics().end(sessionAccumulator);
        sessionAccumulator.getUbiSession().setTrafficSrcId(getTrafficSourceId(sessionAccumulator));
    }

    @Override
    public void feed(UbiEvent ubiEvent, SessionAccumulator sessionAccumulator) throws Exception {
        sessionAccumulator.getUbiSession().getIntermediateMetrics().feed(ubiEvent);
    }

    public String getCobrandSiteId(UbiEvent event) {
        if (event == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(event.getCobrand()).append("|").append(event.getSiteId());
        return stringBuilder.toString();
    }

    private int getTrafficSourceId(SessionAccumulator sessionAccumulator) {
        IntermediateMetrics intermediateMetrics=sessionAccumulator.getUbiSession().getIntermediateMetrics();
        Integer firstCobrand =sessionAccumulator.getUbiSession().getFirstCorbrand();
        Long startTSOnCurrentCobrandSite =
                intermediateMetrics.getEventTS() == null ? SOJMAXLONG : intermediateMetrics
                        .getEventTS();

        // calculate the traffic source id base on intermedia parameters
        if (("11030".equals(intermediateMetrics.getScEventE())
                || "11030".equals(intermediateMetrics.getRoverClickE()))
                && INITIALSESSIONDATE.compareTo(startTSOnCurrentCobrandSite) <= 0) {
            return 23;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && EIGHT.equals(intermediateMetrics.getChannel())) {
            return 19;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && SEVEN.equals(intermediateMetrics.getChannel())) {
            return 4;
        } else if (StringUtils.isNotBlank(intermediateMetrics.getScEventE())) {
            return 4;
        } else if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "25".equals(intermediateMetrics.getMpxChannelId())
                && intermediateMetrics.getActualKeyword().contains("half")) {
            return 6;
        } else if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "2".equals(intermediateMetrics.getMpxChannelId())
                && intermediateMetrics.getActualKeyword().contains("half")) {
            return 7;
        } else if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "26".equals(intermediateMetrics.getMpxChannelId())
                && intermediateMetrics.getActualKeyword().contains("half")) {
            return 9;
        } else if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "25".equals(intermediateMetrics.getMpxChannelId())
                && intermediateMetrics.getActualKeyword().matches(kijijiStr)) {
            return 6;
        } else if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "2".equals(intermediateMetrics.getMpxChannelId())
                && intermediateMetrics.getActualKeyword().matches(kijijiStr)) {
            return 7;
        } else if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "26".equals(intermediateMetrics.getMpxChannelId())
                && intermediateMetrics.getActualKeyword().matches(kijijiStr)) {
            return 9;
        } else if ((!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
                && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && ("2".equals(intermediateMetrics.getMpxChannelId()) || "14"
                .equals(intermediateMetrics.getMpxChannelId()))
                && intermediateMetrics.getActualKeyword().matches(ebayStr)
                && rotSet.contains(intermediateMetrics.getRotId())) {
            return 29;
        } else if ((!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
                && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "25".equals(intermediateMetrics.getMpxChannelId())
                && intermediateMetrics.getActualKeyword().matches(ebayStr)) {
            return 6;
        } else if ((!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
                && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "2".equals(intermediateMetrics.getMpxChannelId())
                && intermediateMetrics.getActualKeyword().matches(ebayStr)) {
            return 7;
        } else if ((!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
                && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "26".equals(intermediateMetrics.getMpxChannelId())
                && intermediateMetrics.getActualKeyword().matches(ebayStr)) {
            return 9;
        } else if ((!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
                && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && ("2".equals(intermediateMetrics.getMpxChannelId()) || "14"
                .equals(intermediateMetrics.getMpxChannelId()))
                && rotSet.contains(intermediateMetrics.getRotId())) {
            return 28;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "25".equals(intermediateMetrics.getMpxChannelId())) {
            return 18;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "2".equals(intermediateMetrics.getMpxChannelId())) {
            return 10;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "6".equals(intermediateMetrics.getMpxChannelId())) {
            return 12;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "1".equals(intermediateMetrics.getMpxChannelId())) {
            return 11;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "32".equals(intermediateMetrics.getMpxChannelId())) {
            return 30;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "9".equals(intermediateMetrics.getMpxChannelId())) {
            return 13;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "15".equals(intermediateMetrics.getMpxChannelId())) {
            return 14;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "16".equals(intermediateMetrics.getMpxChannelId())) {
            return 15;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "17".equals(intermediateMetrics.getMpxChannelId())) {
            return 16;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "23".equals(intermediateMetrics.getMpxChannelId())) {
            return 17;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "26".equals(intermediateMetrics.getMpxChannelId())) {
            return 20;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "27".equals(intermediateMetrics.getMpxChannelId())) {
            return 25;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "97".equals(intermediateMetrics.getMpxChannelId())) {
            return 26;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && "33".equals(intermediateMetrics.getMpxChannelId())) {
            return 31;
        } else if ("1".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 11;
        } else if ("32".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 30;
        } else if ("6".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 12;
        } else if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
                && "25".equals(intermediateMetrics.getImgMpxChannelId())
                && intermediateMetrics.getRefKeyword().contains("half")) {
            return 6;
        } else if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
                && "25".equals(intermediateMetrics.getImgMpxChannelId())
                && intermediateMetrics.getRefKeyword().matches(kijijiStr)) {
            return 6;
        } else if ((!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
                && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand))
                && "25".equals(intermediateMetrics.getImgMpxChannelId())
                && intermediateMetrics.getRefKeyword().matches(ebayStr)) {
            return 6;
        } else if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
                && "2".equals(intermediateMetrics.getImgMpxChannelId())
                && intermediateMetrics.getRefKeyword().contains("half")) {
            return 7;
        } else if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
                && "2".equals(intermediateMetrics.getImgMpxChannelId())
                && intermediateMetrics.getRefKeyword().matches(kijijiStr)) {
            return 7;
        } else if ((!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
                && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand))
                && "2".equals(intermediateMetrics.getImgMpxChannelId())
                && intermediateMetrics.getRefKeyword().matches(ebayStr)) {
            return 7;
        } else if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
                && "26".equals(intermediateMetrics.getImgMpxChannelId())
                && intermediateMetrics.getRefKeyword().contains("half")) {
            return 9;
        } else if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
                && "26".equals(intermediateMetrics.getImgMpxChannelId())
                && intermediateMetrics.getRefKeyword().matches(kijijiStr)) {
            return 9;
        } else if ((!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
                && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand))
                && "26".equals(intermediateMetrics.getImgMpxChannelId())
                && intermediateMetrics.getRefKeyword().matches(ebayStr)) {
            return 9;
        } else if ("25".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 18;
        } else if ("2".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 10;
        } else if ("9".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 13;
        } else if ("15".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 14;
        } else if ("16".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 15;
        } else if ("17".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 16;
        } else if ("23".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 17;
        } else if ("26".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 20;
        } else if ("27".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 25;
        } else if (intermediateMetrics.getRefDomain().startsWith("itemlistings.ebay.")) {
            return 21;
        } else if (intermediateMetrics.getRefDomain().matches(regString4Id21)) {
            return 21;
        } else if (intermediateMetrics.getRefDomain().contains(".craigslist.")) {
            return 24;
        } else if (intermediateMetrics.getRefDomain().matches(".*toolbar.*\\.google\\..*")
                && landPageSet1.contains(intermediateMetrics.getLandPageID())) {
            return 8;
        } else if (intermediateMetrics.getRefDomain().matches(".*toolbar.*\\.google\\..*")) {
            return 21;
        } else if (intermediateMetrics.getRefDomain().matches(regString4Id22)) {
            return 22;
        } else if ("13".equals(intermediateMetrics.getImgMpxChannelId())) {
            return 23;
        } else if (intermediateMetrics.getRefDomain().contains("email")) {
            return 5;
        } else if (intermediateMetrics.getRefDomain()
                .matches(".*\\.(google|googleusercontent)\\..*")
                && intermediateMetrics.getRefKeyword().equals("")
                && !SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer()).matches(
                "/url|/imgres|/search|/")) {
            return 24;
        } else if ((ONE.equals(firstCobrand) || EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRefDomain().contains(".google.")
                && SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer()).startsWith("/products")
                && intermediateMetrics.getRefKeyword().contains("half")) {
            return 9;
        } else if ((FIVE.equals(firstCobrand) || NINE.equals(firstCobrand))
                && intermediateMetrics.getRefDomain().contains(".google.")
                && SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer()).startsWith("/products")
                && intermediateMetrics.getRefKeyword().matches(".*(kijiji|e.*bay.*classified).*")) {
            return 9;
        } else if ((!FIVE.equals(firstCobrand) && !NINE.equals(firstCobrand)
                && !ONE.equals(firstCobrand) && !EIGHT.equals(firstCobrand))
                && intermediateMetrics.getRefDomain().contains(".google.")
                && SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer()).startsWith("/products")
                && intermediateMetrics.getRefKeyword().matches(ebayStr)) {
            return 9;
        } else if (intermediateMetrics.getRefDomain().contains(".google.")
                && SOJGetUrlPath.getUrlPath(intermediateMetrics.getReferrer()).startsWith("/products")
                && intermediateMetrics.getRefKeyword().matches(".*(kijiji|e.*bay.*classified).*")) {
            return 20;
        } else if (intermediateMetrics.getRefDomain().contains(".yahoo.")
                && !intermediateMetrics.getRefDomain().contains("search.yahoo.")) {
            return 24;
        } else if (intermediateMetrics.getRoverNsTs() != null
                && intermediateMetrics.getRoverNsTs() <= startTSOnCurrentCobrandSite + SECOND10
                && intermediateMetrics.getRoverNsTs() >= startTSOnCurrentCobrandSite + SECOND3
                && "3".equals(intermediateMetrics.getMpxChannelId())) {
            if (landPageSet1.contains(intermediateMetrics.getLandPageID())) {
                return 8;
            } else {
                return 21;
            }
        } else if (intermediateMetrics.getRefDomain().matches(regString4Id21_1)) {
            if (landPageSet1.contains(intermediateMetrics.getLandPageID())) {
                return 8;
            } else {
                return 21;
            }
        } else if (StringUtils.isBlank(intermediateMetrics.getRefDomain())
                && intermediateMetrics.getRoverOpenTs() != null
                && intermediateMetrics.getRoverOpenTs() <= startTSOnCurrentCobrandSite + SESCOND1
                && intermediateMetrics.getRoverOpenTs() >= startTSOnCurrentCobrandSite - SECOND30
                && StringUtils.isNotBlank(intermediateMetrics.getRoverOpenEuid())) {
            return 4;
        } else if (intermediateMetrics.getRefDomain().matches(".*(ebay|\\.kijiji\\.).*")) {
            return 2;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SECOND8
                && intermediateMetrics.getRoverEntryTs() >= startTSOnCurrentCobrandSite - SECOND30
                && "13".equals(intermediateMetrics.getMpxChannelId())
                && swdSet.contains(intermediateMetrics.getSwd())) {
            return 22;
        } else if (intermediateMetrics.getRefDomain().matches(regString4Id23)) {
            return 23;
        } else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SECOND8
                && intermediateMetrics.getRoverEntryTs() >= startTSOnCurrentCobrandSite - SECOND30
                && "13".equals(intermediateMetrics.getMpxChannelId())) {
            return 23;
        } else if (intermediateMetrics.getRoverNsTs() != null
                && intermediateMetrics.getRoverNsTs() <= startTSOnCurrentCobrandSite + SECOND10
                && intermediateMetrics.getRoverNsTs() >= startTSOnCurrentCobrandSite - SECOND3
                && "13".equals(intermediateMetrics.getMpxChannelId())) {
            return 23;
        } else if (socialAgentId22.contains(intermediateMetrics.getSocialAgentTypeId())) {
            return 22;
        } else if (socialAgentId23.contains(intermediateMetrics.getSocialAgentTypeId())) {
            return 23;
        } else if (intermediateMetrics.getSearchAgentTypeId() != null
                && landPageSet1.contains(intermediateMetrics.getLandPageID())) {
            return 8;
        } else if (intermediateMetrics.getSearchAgentTypeId() != null) {
            return 21;
        } else if (intermediateMetrics.getFirstNotifyTs() != null
                && intermediateMetrics.getFirstNotifyTs() <= startTSOnCurrentCobrandSite + SECOND3
                && intermediateMetrics.getFirstNotifyTs() >= startTSOnCurrentCobrandSite - SECOND180) {
            return 27;
        } /*check mppid from roverentry*/ else if (intermediateMetrics.getRoverEntryTs() != null
                && intermediateMetrics.getRoverEntryTs() <= startTSOnCurrentCobrandSite + SECOND10
                && intermediateMetrics.getRoverEntryTs() >= startTSOnCurrentCobrandSite - SECOND30
                && intermediateMetrics.getFirstMppId() != null
                && intermediateMetrics.getFirstMppId() > 0) {
            return 14;
        } /*check mppid from mobile pages*/ else if (intermediateMetrics.getFinalMppId() != null
                && intermediateMetrics.getFinalMppId() > 0) {
            return 14;
        } else if (StringUtils.isBlank(intermediateMetrics.getRefDomain())) {
            return 1;
        } else if (intermediateMetrics.getCurAdme().startsWith("TB")) {
            return 3;
        } else if (landPageSet2.contains(intermediateMetrics.getLandPageID())
                && intermediateMetrics.getPrevAdme().startsWith("TB")) {
            // not init on prevAdme
            // cobrandSiteIdTrafficSourceId.put(currentCobrandSite,3);
        } else if (StringUtils.isNotBlank(intermediateMetrics.getRefDomain())) {
            return 24;
        } else if (StringUtils.isNotBlank(intermediateMetrics.getCurAdme())
                && !intermediateMetrics.getCurAdme().startsWith("TB")) {
            /**
             * below branches are not reachable anyway
             */
            return 4;
        } else if (StringUtils.isNotBlank(intermediateMetrics.getPrevAdme())
                && !intermediateMetrics.getPrevAdme().startsWith("TB")) {
            // not init on prevAdme
            // return 4;
        } else if (landPageSet2.contains(intermediateMetrics.getLandPageID())
                && StringUtils.isNotBlank(intermediateMetrics.getPrevAdme())
                && !intermediateMetrics.getPrevAdme().startsWith("TB")) {
            // not init on prevAdme
            // return 4;
        } else if (landPageSet2.contains(intermediateMetrics.getLandPageID())
                && StringUtils.isNotBlank(intermediateMetrics.getFutureAdme())
                && !intermediateMetrics.getFutureAdme().startsWith("TB")) {
            // not init on futureAdme
            // return 4;
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public void init() throws Exception {
        // init constants from property
        InputStream resourceAsStream = TrafficSourceIdMetrics.class.getResourceAsStream("/ubi.properties");
        ubiConfig = UBIConfig.getInstance(resourceAsStream);
        landPageSet1 =
                PropertyUtils.getIntegerSet(ubiConfig.getString(Property.LAND_PAGES1),
                        Property.PROPERTY_DELIMITER);
        landPageSet2 =
                PropertyUtils.getIntegerSet(ubiConfig.getString(Property.LAND_PAGES2),
                        Property.PROPERTY_DELIMITER);
        swdSet =
                PropertyUtils
                        .getIntegerSet(ubiConfig.getString(Property.SWD_VALUES), Property.PROPERTY_DELIMITER);
        rotSet =
                PropertyUtils.getLongSet(ubiConfig.getString(Property.ROT_VALUES), Property.PROPERTY_DELIMITER);
        socialAgentId22 =
                PropertyUtils.getIntegerSet(ubiConfig.getString(Property.SOCIAL_AGENT_ID22),
                        Property.PROPERTY_DELIMITER);
        socialAgentId23 =
                PropertyUtils.getIntegerSet(ubiConfig.getString(Property.SOCIAL_AGENT_ID23),
                        Property.PROPERTY_DELIMITER);


    }

    @Override
    public void start(SessionAccumulator sessionAccumulator) throws Exception {
        IntermediateMetrics intermediateMetrics = new IntermediateMetrics();
        intermediateMetrics.initMetrics();
        sessionAccumulator.getUbiSession().setIntermediateMetrics(intermediateMetrics);
//        intermediateMetrics.start(source);
//        feed(source, target);
    }

    public static Set<Integer> getSocialAgentId22() {
        return socialAgentId22;
    }

    public static void setSocialAgentId22(Set<Integer> socialAgentId22) {
        TrafficSourceIdMetrics.socialAgentId22 = socialAgentId22;
    }

    public static Set<Integer> getSocialAgentId23() {
        return socialAgentId23;
    }

    public static void setSocialAgentId23(Set<Integer> socialAgentId23) {
        TrafficSourceIdMetrics.socialAgentId23 = socialAgentId23;
    }
}
