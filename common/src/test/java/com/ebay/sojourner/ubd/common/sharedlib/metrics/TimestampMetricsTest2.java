package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJParseClientInfo;
import com.ebay.sojourner.ubd.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimestampMetricsTest2 {

  private List<UbiEvent> ubiEventList;
  private TimestampMetrics timestampMetrics;
  private UbiSession ubiSession;
  private SessionAccumulator sessionAccumulator;
  private SessionMetrics sessionMetrics;

  @Before
  public void setUp() throws Exception {
    ubiEventList = new ArrayList<>();
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("970d97841710a12ceed7b9cbffdbb59d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-20 03:06:22.764")));
    ubiEvent.setRemoteIP("10.175.140.126");
    ubiEvent.setRequestCorrelationId("a7ab98ea61111c32");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*gvnsh%28rbpv6710-171970d982d-0x113");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("66.249.90.50");
    ubiEvent
        .setAgentInfo("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.4"
            + "6 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; AdsBot-Google-M"
            + "obile; +http://www.google.com/mobile/adsbot.html),GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(510282012685L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent
        .setApplicationPayload("nqc=AA**&c=1&g=970d97841710a12ceed7b9cbffdbb59d&h=84&px=4249&chn"
            + "l=9&uc=1&nqt=AA**&p=2317508&uaid=970d982d1710aaecf7673113d22a29d2S0&bs=0&t=2&cflgs=wA**&u"
            + "l=en-US&plmt=HQQAAB%252BLCAAAAAAAAAC1U8uu0zAQ%252FRevQ%252BJX0jRrEIIFEiqXDWLhOE5jyMOynT6o"
            + "%252Bu%252FMpE3vZXGRWLDyPI%252FnnLEvxO1JxYXgcksT4iypvl2IbUjFKN1wmRANIU7ZhnJWMM5zATEPMVKyL"
            + "eVUtjJXudQl3ypT8I2o20ZvVVO0JCEmAtx3ONUNFtrGue8TohAgz3PKCihThzU%252BQJzBcSIVTDPpxVNh8dS0eA"
            + "Fmu5BglNfd59n4MyCRKxS3eNcVbtNxKdRQSK%252FJg00h6YNNQWFUJjmTxZ1N0QgmcwoEylq2lClat9uN4Ju2LH"
            + "Iqyr%252BxmW4BBzBN6z6paA%252FmrQ2uV2doAxKEI83ocHKFZTutxkb1mFV31BlYEiYgNEW0wHAeWylYJ7da48"
            + "Oa%252F21q67ATG63W%252Fm7uG%252Befglld1w8R1SI3uXA1Gsc3p4gSu4VAsL8Mnp2x%252Bw7AeQ66Hm0TO1"
            + "IJCoqT0cTj5H9%252BQCRRSqSimqfRxiVianVOtUoP1mXD0dR471z3NnRfzg6QOez4Js%252FH3XLf3QlfjQ92Gp"
            + "F%252FylLGUQPfg5tl1qcIGyJor1M9DZn22SHTLNutUG9uTelgx%252FRHQDpIJCybe5XKc%252Bplhv%252BRAa"
            + "1eS%252FHyRUoU%252BDhteL%252FbvRtV3RsQI%252FrZwIpG%252BACw7amZdUSGOFs0qBa%252FYtN%252F%2"
            + "52Fy75%252Bl3Ar%252B3y%252Fkpy%252FQ0IRoUeHQQAAA%253D%253D&pn=2&rq=a7ab98ea61111c32&page"
            + "name=cos__mfe&po=%5B%28pg%3A2332490+pid%3A100724%29%28pg%3A2332490+pid%3A100640%29%5D&ci"
            + "id=DZgtz3Y*");
    ubiEvent.setClientData(constructClientData(
        "nqc=AA**&c=1&g=970d97841710a12ceed7b9cbffdbb59d&h=84&px=4249&chnl=9&uc=1&nqt=AA*"
            + "*&p=2317508&uaid=970d982d1710aaecf7673113d22a29d2S0&bs=0&t=2&cflgs=wA**&ul=en-US&plm"
            + "t=HQQAAB%252BLCAAAAAAAAAC1U8uu0zAQ%252FRevQ%252BJX0jRrEIIFEiqXDWLhOE5jyMOynT6o%252Bu"
            + "%252FMpE3vZXGRWLDyPI%252FnnLEvxO1JxYXgcksT4iypvl2IbUjFKN1wmRANIU7ZhnJWMM5zATEPMVKyLe"
            + "VUtjJXudQl3ypT8I2o20ZvVVO0JCEmAtx3ONUNFtrGue8TohAgz3PKCihThzU%252BQJzBcSIVTDPpxVNh8d"
            + "S0eAFmu5BglNfd59n4MyCRKxS3eNcVbtNxKdRQSK%252FJg00h6YNNQWFUJjmTxZ1N0QgmcwoEylq2lClat9"
            + "uN4Ju2LHIqyr%252BxmW4BBzBN6z6paA%252FmrQ2uV2doAxKEI83ocHKFZTutxkb1mFV31BlYEiYgNEW0wH"
            + "AeWylYJ7da48Oa%252F21q67ATG63W%252Fm7uG%252Befglld1w8R1SI3uXA1Gsc3p4gSu4VAsL8Mnp2x%2"
            + "52Bw7AeQ66Hm0TO1IJCoqT0cTj5H9%252BQCRRSqSimqfRxiVianVOtUoP1mXD0dR471z3NnRfzg6QOez4J"
            + "s%252FH3XLf3QlfjQ92GpF%252FylLGUQPfg5tl1qcIGyJor1M9DZn22SHTLNutUG9uTelgx%252FRHQDpI"
            + "JCybe5XKc%252Bplhv%252BRAa1eS%252FHyRUoU%252BDhteL%252FbvRtV3RsQI%252FrZwIpG%252BAC"
            + "w7amZdUSGOFs0qBa%252FYtN%252F%252Fy75%252Bl3Ar%252B3y%252Fkpy%252FQ0IRoUeHQQAAA%253"
            + "D%253D&pn=2&rq=a7ab98ea61111c32&pagename=cos__mfe&po=%5B%28pg%3A2332490+pid%3A10072"
            + "4%29%28pg%3A2332490+pid%3A100640%29%5D&ciid=DZgtz3Y*"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2332490/9?site=2&trknvp=plmt%3DHQQAAB%252BLCAAAAAAAAAC1U8uu0zAQ%252FRevQ%252BJ"
            + "X0jRrEIIFEiqXDWLhOE5jyMOynT6o%252Bu%252FMpE3vZXGRWLDyPI%252FnnLEvxO1JxYXgcksT4iypvl2"
            + "IbUjFKN1wmRANIU7ZhnJWMM5zATEPMVKyLeVUtjJXudQl3ypT8I2o20ZvVVO0JCEmAtx3ONUNFtrGue8TohA"
            + "gz3PKCihThzU%252BQJzBcSIVTDPpxVNh8dS0eAFmu5BglNfd59n4MyCRKxS3eNcVbtNxKdRQSK%252FJg0"
            + "0h6YNNQWFUJjmTxZ1N0QgmcwoEylq2lClat9uN4Ju2LHIqyr%252BxmW4BBzBN6z6paA%252FmrQ2uV2doAx"
            + "KEI83ocHKFZTutxkb1mFV31BlYEiYgNEW0wHAeWylYJ7da48Oa%252F21q67ATG63W%252Fm7uG%252Befg"
            + "lld1w8R1SI3uXA1Gsc3p4gSu4VAsL8Mnp2x%252Bw7AeQ66Hm0TO1IJCoqT0cTj5H9%252BQCRRSqSimqfRx"
            + "iVianVOtUoP1mXD0dR471z3NnRfzg6QOez4Js%252FH3XLf3QlfjQ92GpF%252FylLGUQPfg5tl1qcIGyJor"
            + "1M9DZn22SHTLNutUG9uTelgx%252FRHQDpIJCybe5XKc%252Bplhv%252BRAa1eS%252FHyRUoU%252BDhte"
            + "L%252FbvRtV3RsQI%252FrZwIpG%252BACw7amZdUSGOFs0qBa%252FYtN%252F%252Fy75%252Bl3Ar%252"
            + "B3y%252Fkpy%252FQ0IRoUeHQQAAA%253D%253D%26po%3D%5B%28pg%3A2332490+pid%3A100724%29%28"
            + "pg%3A2332490+pid%3A100640%29%5D&trknvpsvc=%3Ca%3Enqc%3DAA**%26nqt%3DAA**%3C%2Fa%3E&t"
            + "guid=970d97841710a12ceed7b9cbffdbb59d&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(2);
    ubiEvent.setClickId(1);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setItemId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("970d97841710a12ceed7b9cbffdbb59d");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2509164);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-20 03:06:23.031")));
    ubiEvent.setRemoteIP("66.249.90.50");
    ubiEvent.setRequestCorrelationId("a7ab98ea61111c32");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%28507426%3A%2Busqdrrp%2Bjqp"
        + "%2Bceb%7C%28dlh-171970d9938-0x1e2");
    ubiEvent.setEventFamily("SEOERR");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("66.249.90.50");
    ubiEvent
        .setAgentInfo(
            "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (K"
                + "HTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; AdsBot-Google-M"
                + "obile; +http://www.google.com/mobile/adsbot.html)");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(391926618125L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.ca");
    ubiEvent.setApplicationPayload(
        "nqc=AA**&flgs=AA**&uc=1&nqt=AA**&mos=iOS&bs=0&osv=4&!_mid=5277&ul=en-CA&callingpagename=seo"
            + "errorexpsvc__error_recovery_v1_error_page_GET&ec=2&pagename=seoerrorexpsvc__error_rec"
            + "overy_v1_error_page_GET&app=3564&res=480x320&efam=SEOERR&c=1&g=970d97841710a12ceed7b9"
            + "cbffdbb59d&h=84&mobile=true&cp=2509164&es=2&p=2509164&t=2&cflgs=QA**&eactn=EXPC&rq=a7"
            + "ab98ea61111c32&ciid=DZilQFs*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D553b6482d17b84b9%26node_id%3Dbadf2129aa168142%26REQUEST_GUID"
            + "%3D171970d9-9330-acc4-05b3-ec92d20781b8%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse3"
            + "1%2528507426%253A%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171970d9938-0x1e2%26cal_mo"
            + "d%3Dfalse&TPool=r1seoerrorexpsvc&TDuration=1&TStatus=0&TType=URL&ContentLength=980&Fo"
            + "rwardedFor=2.22.225.102;66.249.90.50&Script=/trk20svc/TrackingResource/v1&Server=www."
            + "ebay.ca&TMachine=10.204.64.91&TStamp=03:06:23.03&TName=Ginger.CollectionSvc.track&Age"
            + "nt=Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML,"
            + " like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; AdsBot-Google-Mobile"
            + "; +http://www.google.com/mobile/adsbot.html)&RemoteIP=66.249.90.50&Encoding=gzip"));
    ubiEvent.setUrlQueryString("/itm/Royal-Doulton-Howzat-Bunnykins-DB490-Australian-exclusive-ltd"
        + "-edn-box-cert-/264587672892?oid=254421769276");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(2);
    ubiEvent.setClickId(1);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setItemId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("970d97841710a12ceed7b9cbffdbb59d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2344945);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-20 03:06:22.997")));
    ubiEvent.setRemoteIP("66.249.90.50");
    ubiEvent.setRequestCorrelationId("a7ab98ea61111c32");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285044203%2Busqdrrp%2Bjqp%2Bceb%7C%28dl"
        + "h-171970d9916-0xeb");
    ubiEvent.setEventFamily("DEAL");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("66.249.90.50");
    ubiEvent
        .setAgentInfo("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 ("
            + "KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; AdsBot-Google"
            + "-Mobile; +http://www.google.com/mobile/adsbot.html)");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(4242413246477L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.ca");
    ubiEvent.setApplicationPayload(
        "!pgi=2344945%3A0&nqc=AA**&flgs=AA**&uc=1&dt=SMALL&nqt=AA**&mos=iOS&bs=0&osv=4&ul=en-CA&call"
            + "ingpagename=seoerrorexpsvc__error_recovery_v1_error_page_GET&pagename=rppces__module_"
            + "provider&app=3564&rpp_cat_name=Featured&res=480x320&efam=DEAL&epec=7%2C6%2C7%2C6&g=97"
            + "0d97841710a12ceed7b9cbffdbb59d&h=84&mobile=true&cp=2509164&p=2344945&t=2&cflgs=AA**&"
            + "pagedtl=page%253AFeatured%257Crpp_cat_id%253A0&!mi=4323|3872%3A1GBGUPPP%252C2US8H2IF"
            + "%252C3D22AC5K%252C2GRIYG3W%252C3DFAAD5D|1573%3A1&eactn=EXPM&rq=a7ab98ea61111c32&ciid"
            + "=DZj0cDg*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D8facb360d247c432%26node_id%3D647a9795b3796bd6%26REQUEST_GUI"
            + "D%3D171970d9-9120-acc7-0380-fcfff472bb34%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse"
            + "31%25285044203%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171970d9916-0xeb%26cal_mod%"
            + "3Dfalse&TPool=r1rppces&TDuration=1&TStatus=0&TType=URL&ContentLength=1131&Forwarded"
            + "For=2.22.225.102;66.249.90.50&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.c"
            + "a&TMachine=10.204.112.56&TStamp=03:06:22.99&TName=Ginger.CollectionSvc.track&Agent=M"
            + "ozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, li"
            + "ke Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; AdsBot-Google-Mobile; "
            + "+http://www.google.com/mobile/adsbot.html)&RemoteIP=66.249.90.50&Encoding=gzip"));
    ubiEvent
        .setUrlQueryString("/itm/Royal-Doulton-Howzat-Bunnykins-DB490-Australian-exclusive-ltd-e"
            + "dn-box-cert-/264587672892?oid=254421769276");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(2);
    ubiEvent.setClickId(0);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setItemId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("970d97841710a12ceed7b9cbffdbb59d");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2349624);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-20 03:06:22.860")));
    ubiEvent.setRemoteIP("66.249.90.50");
    ubiEvent.setRequestCorrelationId("a7ab98ea61111c32");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dqkisqn47pse31%287323464%2Busqdrrp%2Btil%2Bceb%7"
        + "C%28dlh-171970d988d-0x12d");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("66.249.90.50");
    ubiEvent
        .setAgentInfo("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46"
            + " (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; AdsBot-Goog"
            + "le-Mobile; +http://www.google.com/mobile/adsbot.html)");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(392383600397L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEA*");
    ubiEvent.setWebServer("www.ebay.ca");
    ubiEvent.setApplicationPayload(
        "nqc=AA**&flgs=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEA*&obfs_listing_is_elig"
            + "ible=true&viStreamId=970d97841710a12ceed7b9cbffdbb59d&!_OBFS_LINK_COUNT=0&!_OBFS_BUYE"
            + "R_VIEWING_SITEID=2&obfs_sid_uid_same=false&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INF"
            + "O_DETECTED&uc=1&nqt=AA**&mos=iOS&bs=0&OBFS_ITEMID=264587672892&osv=4&obfs_desc_has_co"
            + "ntactInfo=false&ul=en-CA&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=0&sdecn"
            + "=SALE_NOT_FOUND&!_epec=7,6,7,6&ec=2&pagename=viexpsvc__VIEWLISTING&app=3564&res=480x3"
            + "20&efam=ITM&c=1&!_OBFS_SELLERID=2638457&epec=7%2C6%2C7%2C6&g=970d97841710a12ceed7b9cb"
            + "ffdbb59d&h=84&mobile=true&!_OBFS_BUYERID=0&cp=2349624&OBFS_EP_STATE=NOT_IN_EXPERIMENT"
            + "&es=2&!_OBFS_PHONE_COUNT=0&p=2349624&!_callingpageid=2332490&t=2&cflgs=QA**&OBFS_STAT"
            + "US=NOT_REQUIRED&viPageType=0&eactn=EXPC&!_OBFS_EMAIL_COUNT=0&rq=a7ab98ea61111c32&cii"
            + "d=DZfiW1s*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Df24022c3f44574f8%26node_id%3Dea9a6254c1161b14%26REQUEST_GUID"
            + "%3D171970d9-8870-a6e5-b5b6-3ff3fdb741ce%26logid%3Dt6wwm53vpd77%253C%253Dqkisqn47pse31"
            + "%25287323464%252Busqdrrp%252Btil%252Bceb%257C%2528dlh-171970d988d-0x12d%26cal_mod%3Df"
            + "alse&TPool=r1viexpsvc&TDuration=2&TStatus=0&TType=URL&ContentLength=1652&ForwardedFor"
            + "=2.22.225.102;66.249.90.50&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.ca&TM"
            + "achine=10.110.91.91&TStamp=03:06:22.86&TName=Ginger.CollectionSvc.track&Agent=Mozilla"
            + "/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Geck"
            + "o) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; AdsBot-Google-Mobile; +http://"
            + "www.google.com/mobile/adsbot.html)&RemoteIP=66.249.90.50&Encoding=gzip"));
    ubiEvent.setUrlQueryString("/itm/Royal-Doulton-Howzat-Bunnykins-DB490-Australian-exclusive-ltd"
        + "-edn-box-cert-/264587672892?oid=254421769276");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(2);
    ubiEvent.setClickId(1);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setItemId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("970d97841710a12ceed7b9cbffdbb59d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-20 03:06:22.903")));
    ubiEvent.setRemoteIP("10.164.103.82");
    ubiEvent.setRequestCorrelationId("a7ab98ea61111c32");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*02506%3D1%29pqtfwpu%29osu%29fgg%7E-fi"
        + "j-171970d98b9-0x118");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("66.249.90.50");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gec"
            + "ko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; AdsBot-Google-Mobile; +http:/"
            + "/www.google.com/mobile/adsbot.html),GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(167918475277L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AA**&c=1&g=970d97841710a12ceed7b9cbffdbb59d&h=84&px=4249&chnl=9&uc=1&nqt=AA**&p=2317508"
            + "&uaid=970d98b81710a8618270c45ad448d9d9S0&bs=0&t=2&cflgs=wA**&ul=en-US&plmt=5gAAAB%25"
            + "2BLCAAAAAAAAAA1js0OgjAQhN9lzj1suy1peQvPxkMpRUlQCD9GQ3h3F4inyX6ZnZkVwx2lYTY2kMLQoryua"
            + "GuUmiiQVUiCDGlPmhwF551XGIWBq9AIYQpFYx0XIZuqDjZ5x9kyZSjkWeJuovGMlbfX0nUKcQ%252FQWjq02"
            + "OL7z5%252FCWeSDUtb06ZA4ndLLKIVJtq2YchzT47Lk8StJ2DZpSfNhSGKg%252Fa7aeW%252Fx2H5kwkh45"
            + "gAAAA%253D%253D&pn=2&rq=a7ab98ea61111c32&pagename=cos__mfe&po=%5B%28pg%3A2332490+pid"
            + "%3A100904%29%5D&ciid=DZi4GCc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Da7ab98ea61111c32%26node_id%3D8b448219cc17421e%26REQUEST_GUID"
            + "%3D171970d9-8b70-a861-8271-d5cde8edafff%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A025"
            + "06%253D1%2529pqtfwpu%2529osu%2529fgg%257E-fij-171970d98b9-0x118%26cal_mod%3Dfalse&TPo"
            + "ol=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=66.249.90.50"
            + "&Script=/roverimp/0/2332490/9&Server=internal.rover.vip.ebay.com&TMachine=10.134.24.3"
            + "9&TStamp=03:06:22.90&TName=roverimp&Agent=Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like"
            + " Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/"
            + "601.1 (compatible; AdsBot-Google-Mobile; +http://www.google.com/mobile/adsbot.html),"
            + "GingerClient/2.9.7-RELEASE&RemoteIP=10.164.103.82"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2332490/9?site=2&trknvp=plmt%3D5gAAAB%252BLCAAAAAAAAAA1js0OgjAQhN9lzj1suy1peQvP"
            + "xkMpRUlQCD9GQ3h3F4inyX6ZnZkVwx2lYTY2kMLQoryuaGuUmiiQVUiCDGlPmhwF551XGIWBq9AIYQpFYx0XI"
            + "ZuqDjZ5x9kyZSjkWeJuovGMlbfX0nUKcQ%252FQWjq02OL7z5%252FCWeSDUtb06ZA4ndLLKIVJtq2YchzT47"
            + "Lk8StJ2DZpSfNhSGKg%252Fa7aeW%252Fx2H5kwkh45gAAAA%253D%253D%26po%3D%5B%28pg%3A2332490+"
            + "pid%3A100904%29%5D&trknvpsvc=%3Ca%3Enqc%3DAA**%26nqt%3DAA**%3C%2Fa%3E&tguid=970d97841"
            + "710a12ceed7b9cbffdbb59d&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(2);
    ubiEvent.setClickId(1);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setItemId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    /*
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent.setEventTimestamp(3795064810731000L);
    ubiEvent.setRemoteIP("66.249.79.37");
    ubiEvent.setRequestCorrelationId("f8e7978d9a523089");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Fuk%60vgig%285226%3E00-1714affce6a-0x103c0");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("66.249.79.37");
    ubiEvent
        .setAgentInfo("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(424856899327L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "saucxgdpry=true&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid=ef2f416dfb084fd3a2"
            + "b1bf76aa2daef2&g=4affcde01710ac734f70f1afff5eb980&saebaypid=100564&sapcxkw=1pcs+Use"
            + "d+Cermate+10.4-inch+PanelMaster+Touch+Screen+PL104-TST3A-F2R1&h=e0&schemaversion=3&"
            + "salv=5&ciid=%2F85x62I*&p=2367355&sapcxcat=12576%2C42892%2C57516%2C181709&t=3&saiid="
            + "7e02ee3c-aed0-4e9d-b635-6e0fc865167d&cflgs=gA**&samslid=&eactn=AUCT&pn=2&rq=f8e7978"
            + "d9a523089&pagename=SandPage&ciid=%2F85x62I*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Df8e7978d9a523089%26node_id%3D85644d56e7645d3f%26REQUEST_GU"
            + "ID%3D1714affc-e6a0-a16e-b627-dc42eec06e87%26logid%3Dt6pdhc9%253Fuk%2560vgig%25285226"
            + "%253E00-1714affce6a-0x103c0&TPool=r1sand&TDuration=6&ContentLength=1113&ForwardedFor"
            + "=10.202.81.132&Script=sand&Server=sand.stratus.ebay.com&TMachine=10.22.235.98&TStamp"
            + "=08:40:10.73&TName=sand.v1&Agent=Mozilla/5.0 (compatible; Googlebot/2.1; +http://www"
            + ".google.com/bot.html)&RemoteIP=66.249.79.37"));
    ubiEvent.setUrlQueryString("/itm/293306863243?campid=5338358731&customid={gclid}");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(0);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setItemId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(3795064810721000L);
    ubiEvent.setRemoteIP("10.15.11.200");
    ubiEvent.setRequestCorrelationId("57d752ae069e619c");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E27%28twwgsvv%28umj%28bad%7F%29%60jk-1714affce63-0x19f");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("10.15.11.200");
    ubiEvent.setAgentInfo("eBayNioHttpClient 1.0,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(661733428991L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAIAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAIQABAAABACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAEAAQAQAABAE&c=1&g=4affcde01710ac734f70f1afff5eb980&h=e0&px=4249&chnl="
            + "9&uc=1&es=3&nqt=AAAAIAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAIQABAAABACAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAREQAQAQAABAEAg**&p=2317508&uaid=4affce621710a4d129a0"
            + "0c88dc627a70S0&bs=0&t=3&cflgs=QA**&ul=en-US&plmt=lwgAAB%252BLCAAAAAAAAAC1VdtuG0cM%2"
            + "52FZd5rSLMZWdvQR9k2WmLuqlsxeiDbQij3dFqmr1p9mI7hv695OzKVmLESNFagGWSwyEPD4fUI6kzEnPqB"
            + "X4gJ6Q2JL5%252BJCYlMaPU52JCEoPnzKeCC0a5L7wJsWAjOthoutZapSz01iJZKym9NY%252FStR%252FR"
            + "KAjJhOgWwnEuaSBvQVND8DEHhBC7nU2o6MG1IHHZ5fmENNVBqknscRFGE7IBOBNiGhI%252FkgjusZAFNCL"
            + "7PUQ1tX2KRwlaquME1gYbbocEpKxKTb5JEYgIMuQuw3EeCv7vvClnYSTCkAeSeb6HRTE4uH68AZJuSEwnNy"
            + "RRRa1MVv6GBsY8GrAwoIzB0dqkC20TXbYq03AaTOn%252BFmJwLOLZEywC06HgYR1TSimXqEr0lKEvuPSgC"
            + "ZSCzUeX91fLU5ADrBIdw%252BFePP6B5YgpQI31oB9D%252FLOL2dkMPme%252F%252Bz%252BdZrNmNvMK"
            + "%252FvHy86dFuLvvZj%252BjH3eEgiAOAkKLaEBjQX2JBjnkDFBGIHLKwve%252FnCxQdyBjRxgCESiEDoN"
            + "rm8L2SCkpQxfVg7Y0RZ3P%252Fjjv5UIZe2ca%252FZdeY%252BeMa0txj3STKnGaapymKqc1KbYsUXWt04"
            + "U1iXas0YGHQt3Pq97ouwPFKv2gIbbJtq0rgGMxTVJZU2YfKlt0ObxUcl7OynSu6iEeOGidYiFiSqNIAAeUx"
            + "lH8jsUDxzHjMvCdn7LJ9qLT9gHcwZAjZHK1dG8O9Ln6FXQPJKwaNZgoXShTQv5PptAnXZrh6AxodQkAttqe"
            + "q1aXCcZkSHhbtSpfmi96jOX0S52o%252FIX1qtyYvNVWp08neyASXvs1gaIzU0IkKF87bnptW%252BDeNqA"
            + "9P21QTk7Oeg7%252Fl%252FM%252FL88csPQUMIGUNTVezfOm6uC1401TKPheFRu9SlQLYm2romp16hjKc2"
            + "1dzAZKzvVFp8rWtA9guFNtsp1XXYl3ks7WI%252FsqvRxy7Z59YbowU1tskOUGtNv95D8vGPovF0y5EQ7h9"
            + "xaMF44Lhn83z7BUfmAo%252BTjEwk3U3lU7oPjcfqm3r6Dg3ohCvCGK3UOzfWhfQcGEDAcY3tvC%252BLve"
            + "vQbDO8CQb9kTXhf9ay9D%252BCMK%252F%252F9HcVixjMEPOWXjjh1Sv1yo8quFyg8LtahSnX9UBe6M3tS"
            + "rXm5X9d3KlLBMepWvGF2l%252FqqVL7fe03452gBHY%252F%252F1zALWpHWQEkgLv5D7fwD5YbDSlwgAAA"
            + "%253D%253D&!xt=225075&ec=1&pn=2&rq=57d752ae069e619c&pagename=cos__mfe&po=%5B%28pg%3"
            + "A2047675+pid%3A100623+pl%3A1+pladvids%3A%5B0%5D%29%5D&ciid=%2F85iEpo*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D57d752ae069e619c%26node_id%3D795ad2a67aceef66%26REQUEST_GU"
            + "ID%3D1714affc-e610-a4d1-29a3-f8b6ec6f2780%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A"
            + "517%253E27%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-1714affce63-0x19f%26cal_mod%"
            + "3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor="
            + "10.199.52.247&Script=/roverimp/0/2047675/9&Server=internal.rover.vip.ebay.com&TMachi"
            + "ne=10.77.18.154&TStamp=08:40:10.72&TName=roverimp&Agent=eBayNioHttpClient 1.0,Ginger"
            + "Client/2.9.7-RELEASE&RemoteIP=10.15.11.200"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2047675/9?site=3&trknvp=plmt%3DlwgAAB%252BLCAAAAAAAAAC1VdtuG0cM%252FZd5rSLMZWd"
            + "vQR9k2WmLuqlsxeiDbQij3dFqmr1p9mI7hv695OzKVmLESNFagGWSwyEPD4fUI6kzEnPqBX4gJ6Q2JL5%252"
            + "BJCYlMaPU52JCEoPnzKeCC0a5L7wJsWAjOthoutZapSz01iJZKym9NY%252FStR%252FRKAjJhOgWwnEuaSB"
            + "vQVND8DEHhBC7nU2o6MG1IHHZ5fmENNVBqknscRFGE7IBOBNiGhI%252FkgjusZAFNCL7PUQ1tX2KRwlaquM"
            + "E1gYbbocEpKxKTb5JEYgIMuQuw3EeCv7vvClnYSTCkAeSeb6HRTE4uH68AZJuSEwnNyRRRa1MVv6GBsY8GrA"
            + "woIzB0dqkC20TXbYq03AaTOn%252BFmJwLOLZEywC06HgYR1TSimXqEr0lKEvuPSgCZSCzUeX91fLU5ADrBI"
            + "dw%252BFePP6B5YgpQI31oB9D%252FLOL2dkMPme%252F%252Bz%252BdZrNmNvMK%252FvHy86dFuLvvZj%"
            + "252BjH3eEgiAOAkKLaEBjQX2JBjnkDFBGIHLKwve%252FnCxQdyBjRxgCESiEDoNrm8L2SCkpQxfVg7Y0RZ3"
            + "P%252Fjjv5UIZe2ca%252FZdeY%252BeMa0txj3STKnGaapymKqc1KbYsUXWt04U1iXas0YGHQt3Pq97ouwP"
            + "FKv2gIbbJtq0rgGMxTVJZU2YfKlt0ObxUcl7OynSu6iEeOGidYiFiSqNIAAeUxlH8jsUDxzHjMvCdn7LJ9qL"
            + "T9gHcwZAjZHK1dG8O9Ln6FXQPJKwaNZgoXShTQv5PptAnXZrh6AxodQkAttqeq1aXCcZkSHhbtSpfmi96jOX"
            + "0S52o%252FIX1qtyYvNVWp08neyASXvs1gaIzU0IkKF87bnptW%252BDeNqA9P21QTk7Oeg7%252Fl%252FM"
            + "%252FL88csPQUMIGUNTVezfOm6uC1401TKPheFRu9SlQLYm2romp16hjKc21dzAZKzvVFp8rWtA9guFNtsp1"
            + "XXYl3ks7WI%252FsqvRxy7Z59YbowU1tskOUGtNv95D8vGPovF0y5EQ7h9xaMF44Lhn83z7BUfmAo%252BTj"
            + "Ewk3U3lU7oPjcfqm3r6Dg3ohCvCGK3UOzfWhfQcGEDAcY3tvC%252BLvevQbDO8CQb9kTXhf9ay9D%252BCM"
            + "K%252F%252F9HcVixjMEPOWXjjh1Sv1yo8quFyg8LtahSnX9UBe6M3tSrXm5X9d3KlLBMepWvGF2l%252Fqq"
            + "VL7fe03452gBHY%252F%252F1zALWpHWQEkgLv5D7fwD5YbDSlwgAAA%253D%253D%26po%3D%5B%28pg%3A"
            + "2047675+pid%3A100623+pl%3A1+pladvids%3A%5B0%5D%29%5D&trknvpsvc=%3Ca%3Enqc%3DAAAAIAAA"
            + "AAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAIQABAAABACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAEAAQAQAABAE%26nqt%3DAAAAIAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAIQABAAABACAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAREQAQAQAABAEAg**%26es%3D3%26ec%3D1%26%2"
            + "1xt%3D225075%3C%2Fa%3E&tguid=4affcde01710ac734f70f1afff5eb980&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(1);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setItemId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(3795064810830000L);
    ubiEvent.setRemoteIP("10.156.77.203");
    ubiEvent.setRequestCorrelationId("57d752ae069e619c");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0603442%29pqtfwpu%29pie%29fgg%7E-fij-1714affced0-0x12d");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("10.156.77.203");
    ubiEvent.setAgentInfo("eBayNioHttpClient 1.0,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(661733428991L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAIAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAIQABAAABACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAEAAQAQAABAE&c=1&g=4affcde01710ac734f70f1afff5eb980&h=e0&px=4249&chnl="
            + "9&uc=1&es=3&nqt=AAAAIAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAIQABAAABACAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAREQAQAQAABAEAg**&p=2317508&uaid=4affcecf1710ac3d0204"
            + "821ce800d5b8S0&bs=0&t=3&cflgs=QA**&ul=en-US&plmt=cw4AAB%252BLCAAAAAAAAADVl1tvmzAUgP%"
            + "252BLX4eQbQwGqj2wtZqq3dJm20taVS4Yao1bjJO1ifLfd0ySJtIkVPYGD9g%252BPj7H5%252FN9i9oCxRQ"
            + "zHnDfQa1C8WKLVIZiguELHZQqW08xJiQgnIQ%252ByDTIkMhTziiTPMcey3Mc0iBimAX5I2T8MEUOkgbMUep"
            + "j7t9DSeyNH3yACW%252B51Cn21qBaobhelaWDuuaYa1HMqBdGDsqhOw5SHYq3iEI7EhKOI7TbgVXV6ld7GFl"
            + "Jc%252B5Aa2aI2jtAdVNL9I8LPwIXZe%252Fi3BGGBovtHcC4QzF27lAqqlaoor62AkIYBhocsEDVo8pmUqe"
            + "yNqKQUMtdvLsHT8R29qQJkrPuO8izBUgZpMlNcpXAd%252FU5eHdZJF2SsIp%252Bu%252F39YxYun1fJe1D"
            + "z%252BxAdFEBKIzekF58%252BzKDMrfzi5%252FwS8uHBZGSjB0LOCQRf4j8DICJ64ECnxCFwfTISQ071AAb"
            + "uHTB4U8LguyQciSHLHoeWxREDmxIGz8UjMfis3AxggL0B456DPyUOjLiRNxJEofMBEN5xlwymxIG6%252Fth"
            + "NcvMkBjCQ6ICBTwkDcdnIyZClxVsmQzglCv8xGaLV0IlJj3tkNCUMxA3GHhVtEQxhIMcLFJ4WB3%252FkquB"
            + "kM3hkhkcOk7pJei4beVTwMlu%252FZXcgk7pJvnF7gAeGUH0n7AuJQo1YQ3Gmm6oxMpvLspS6%252B26epL4"
            + "2sup%252BWZVK9dfq6tnGjZp0z0Z0fVE0PaMus4y0TEVZztVGxl%252BlER%252BFmdv3DKIczJxVfpEiP1X"
            + "a5dlJodOnm5XULyACgWmMKG9fm%252ByN7MA9jMkCiWwttVEddBZ0TyMBhYcqlw%252BpMJBtD2H19m1kvUK"
            + "n6qKUNytRG2VeQACjBf%252Bia%252BGvTGUvEmUHsnuLKzV9fCnEB2O4%252BwvI5Hvbcw4AAA%253D%253"
            + "D&!xt=225075&ec=1&pn=2&rq=57d752ae069e619c&pagename=cos__mfe&po=%5B%28pg%3A2047675+p"
            + "id%3A100008+pl%3A1+pladvids%3A%5B0%5D%29%5D&ciid=%2F87P0CA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D57d752ae069e619c%26node_id%3D746c95052b9f2f34%26REQUEST_GU"
            + "ID%3D1714affc-ece0-ac3d-0203-2087f35bcce9%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t%2"
            + "A0603442%2529pqtfwpu%2529pie%2529fgg%257E-fij-1714affced0-0x12d%26cal_mod%3Dfalse&T"
            + "Pool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=10.199.5"
            + "2.247&Script=/roverimp/0/2047675/9&Server=internal.rover.vip.ebay.com&TMachine=10.1"
            + "95.208.32&TStamp=08:40:10.83&TName=roverimp&Agent=eBayNioHttpClient 1.0,GingerClien"
            + "t/2.9.7-RELEASE&RemoteIP=10.156.77.203"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2047675/9?site=3&trknvp=plmt%3Dcw4AAB%252BLCAAAAAAAAADVl1tvmzAUgP%252BLX4eQbQ"
            + "wGqj2wtZqq3dJm20taVS4Yao1bjJO1ifLfd0ySJtIkVPYGD9g%252BPj7H5%252FN9i9oCxRQzHnDfQa1C8"
            + "WKLVIZiguELHZQqW08xJiQgnIQ%252ByDTIkMhTziiTPMcey3Mc0iBimAX5I2T8MEUOkgbMUepj7t9DSeyN"
            + "H3yACW%252B51Cn21qBaobhelaWDuuaYa1HMqBdGDsqhOw5SHYq3iEI7EhKOI7TbgVXV6ld7GFlJc%252B5"
            + "Aa2aI2jtAdVNL9I8LPwIXZe%252Fi3BGGBovtHcC4QzF27lAqqlaoor62AkIYBhocsEDVo8pmUqeyNqKQUM"
            + "tdvLsHT8R29qQJkrPuO8izBUgZpMlNcpXAd%252FU5eHdZJF2SsIp%252Bu%252F39YxYun1fJe1Dz%252B"
            + "xAdFEBKIzekF58%252BzKDMrfzi5%252FwS8uHBZGSjB0LOCQRf4j8DICJ64ECnxCFwfTISQ071AAbuHTB4"
            + "U8LguyQciSHLHoeWxREDmxIGz8UjMfis3AxggL0B456DPyUOjLiRNxJEofMBEN5xlwymxIG6%252FthNcvM"
            + "kBjCQ6ICBTwkDcdnIyZClxVsmQzglCv8xGaLV0IlJj3tkNCUMxA3GHhVtEQxhIMcLFJ4WB3%252FkquBkM3"
            + "hkhkcOk7pJei4beVTwMlu%252FZXcgk7pJvnF7gAeGUH0n7AuJQo1YQ3Gmm6oxMpvLspS6%252B26epL42s"
            + "up%252BWZVK9dfq6tnGjZp0z0Z0fVE0PaMus4y0TEVZztVGxl%252BlER%252BFmdv3DKIczJxVfpEiP1Xa"
            + "5dlJodOnm5XULyACgWmMKG9fm%252ByN7MA9jMkCiWwttVEddBZ0TyMBhYcqlw%252BpMJBtD2H19m1kvUK"
            + "n6qKUNytRG2VeQACjBf%252Bia%252BGvTGUvEmUHsnuLKzV9fCnEB2O4%252BwvI5Hvbcw4AAA%253D%25"
            + "3D%26po%3D%5B%28pg%3A2047675+pid%3A100008+pl%3A1+pladvids%3A%5B0%5D%29%5D&trknvpsvc"
            + "=%3Ca%3Enqc%3DAAAAIAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAIQABAAABACAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAQAQAABAE%26nqt%3DAAAAIAAAAAAAAAAAAAAAAMAAAAAAAAA"
            + "AAAAAAAAIQABAAABACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAREQAQAQAABAEAg"
            + "**%26es%3D3%26ec%3D1%26%21xt%3D225075%3C%2Fa%3E&tguid=4affcde01710ac734f70f1afff5eb"
            + "980&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(1);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setItemId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2047675);
    ubiEvent.setEventTimestamp(3795064810583000L);
    ubiEvent.setRemoteIP("66.249.79.37");
    ubiEvent.setRequestCorrelationId("57d752ae069e619c");
    ubiEvent.setSid(null);
    ubiEvent
        .setRlogid("t6ulcpjqcj9%3Fjqpsobtlrbn%28046700-vrubqst-ipt-%60dfz%2Behn-1714affcde4-0x176");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("66.249.79.37");
    ubiEvent
        .setAgentInfo("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(1061744070143L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("QIAxIKAAAAAAAAACYQEEABAAgIghgIAAAABwAAABAAAEAEAAAAAAEA**");
    ubiEvent.setWebServer("www.ebay.co.uk");
    ubiEvent.setApplicationPayload(
        "nqc=AA**&flgs=QIAxIKAAAAAAAAACYQEEABAAgIghgIAAAABwAAABAAAEAEAAAAAAEA**&ssc=36121583011&!sh"
            + "2srv=GBP%3A18.2%5E-1%5E-1%5E2020-04-24T03%3A00%3A00-07%3A00%5E2020-06-19T03%3A00%3A0"
            + "0-07%3A00%5EECONOMY%5EUK_IntlEconomyShippingFromGC%5E0%5E-1%5E3%5E-1%5EUS%5Enull%5E2"
            + "93306863243%5E-1%5E14%5E53&nlpp=10&obfs_sid_uid_same=false&nqt=AAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABA**&"
            + "!SHIPPINGSERVICE=SSP%3A0%5EDSP%3A0&swcenabled=true&noep=4&vibisdm=600X800&iwsv=0&!vi"
            + "mc=1%5E118%5E100623,1%5E225%5E100008&!_OBFS_SELLERID=1881488101&mtpvi=0&bdrs=0&OBFS_"
            + "EP_STATE=NOT_IN_EXPERIMENT&fbscore=109&!_callingpageid=2047675&swccount=1&meta=12576"
            + "&slr=1881488101&OBFS_STATUS=NOT_REQUIRED&curprice=423.89&attrct=5&virvwcnt=0&fssmdc="
            + "38&rq=57d752ae069e619c&bc=0&visbetyp=2&l1=42892&l2=57516&!_itmhs=1036|941|947&qtys=0"
            + "&itmcond=3000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=0&OBFS_ITEMID=293306863243&obfs_desc_"
            + "has_contactInfo=false&vibisb=800&vibisd=600X800&qtya=5&st=9&c=1&vibisbm=800&g=4affcd"
            + "e01710ac734f70f1afff5eb980&h=e0&leaf=181709&!_OBFS_PHONE_COUNT=0&p=2047675&fdp=99&t="
            + "3&nofp=4&tr=2204467&dc=1&visplt=100562%2C100567%2C100938%2C100727%2C100565%2C100916%"
            + "2C100917%2C100918%2C100919%2C&!ampid=3P_IMP&nozp=3&bsnippets=true&sdes=1&!_OBFS_LINK"
            + "_COUNT=0&uc=1&fssmd=27&shsvrcnt=2&mbsa=500&uaid=4affcde01710ac734f70f1afff5eb97fS0&u"
            + "l=en-GB&pymntVersion=1&ec=1&itm=293306863243&promol=0&iver=2412270174019&!_OBFS_BUYE"
            + "RID=0&es=3&fistime=16&vimr=100623%2C100008&itmtitle=1pcs+Used+Cermate+10.4-inch+Pane"
            + "lMaster+Touch+Screen+PL104-TST3A-F2R1&cflgs=wA**&gxoe=vine&pymntMethods=PPL%7CMSC%7C"
            + "VSA%7CMAE%7CAMX&vwc=0&shipsiteid=3&virvwavg=0.0&obfs_listing_is_eligible=true&nw=0&v"
            + "ibis=300&cscw=2&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&ppfoid=0&pagena"
            + "me=ViewItemPageRaptor&!sh1srv=GBP%3A27.0%5E-1%5E-1%5E2020-04-20T03%3A00%3A00-07%3A00"
            + "%5E2020-05-13T03%3A00%3A00-07%3A00%5ESTANDARD%5EUK_IntlStandardShippingFromGC%5E0%5E"
            + "-1%5E3%5E-1%5EUS%5Enull%5E293306863243%5E-1%5E10%5E27&rpg=2047675&fimbsa=500&iimp=10"
            + "&cp_usd=519.6467&swcembg=true&srv=0&!_OBFS_EMAIL_COUNT=0&pn=2&qtrmn=5&ciid=%2F83gNP"
            + "c*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D57d752ae069e619c%26node_id%3Dad1a2eec52fd56dc%26REQUEST_GUI"
            + "D%3D1714affc-dd70-ac73-4f77-eb91ff7db83a%26logid%3Dt6ulcpjqcj9%253Fjqpsobtlrbn%25280"
            + "46700-vrubqst-ipt-%2560dfz%252Behn-1714affcde4-0x176&TPool=r1viewitem&TDuration=395&"
            + "TStatus=0&TType=URL&ContentLength=0&ForwardedFor=66.249.79.37,104.123.69.196&Script="
            + "/itm&Server=www.ebay.co.uk&TMachine=10.199.52.247&TStamp=08:40:10.58&TName=ViewItemP"
            + "ageRaptor&Agent=Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.h"
            + "tml)&RemoteIP=66.249.79.37&Encoding=gzip"));
    ubiEvent.setUrlQueryString("/itm/293306863243?customid=&campid=");
    ubiEvent.setPageName("ViewItemPageRaptor");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(293306863243L);

    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(3);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    timestampMetrics = new TimestampMetrics();
    */
    ubiSession = new UbiSession();
    ubiSession.setStartTimestamp(3795064810583000L);
    ubiSession.setEndTimestamp(3795064810583000L);
    ubiSession.setAbsStartTimestamp(3795064810583000L);
    ubiSession.setAbsEndTimestamp(3795064810830000L);
    ubiSession.setSessionSkey(37950648105830L);
    sessionAccumulator = new SessionAccumulator();
    sessionMetrics = SessionMetrics.getInstance();

  }

  @Test
  public void test1() throws Exception {
    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent ubiEvent : ubiEventList) {
      sessionMetrics.feed(ubiEvent, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(ubiSession.getAbsStartTimestamp(),
        sessionAccumulator.getUbiSession().getAbsStartTimestamp());
    Assert.assertEquals(ubiSession.getAbsEndTimestamp(),
        sessionAccumulator.getUbiSession().getAbsEndTimestamp());
    Assert.assertEquals(ubiSession.getStartTimestamp(),
        sessionAccumulator.getUbiSession().getStartTimestamp());
    Assert.assertEquals(ubiSession.getEndTimestamp(),
        sessionAccumulator.getUbiSession().getEndTimestamp());
    Assert.assertEquals(ubiSession.getSessionSkey(),
        sessionAccumulator.getUbiSession().getSessionSkey());
  }

  private ClientData constructClientData(String clientDatastr) {
    ClientData clientData = new ClientData();
    clientData.getClass().getDeclaredFields();
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TPayload") != null) {
      clientData.setTPayload(SOJParseClientInfo.getClientInfo(clientDatastr, "TPayload"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TPool") != null) {
      clientData.setTPool(SOJParseClientInfo.getClientInfo(clientDatastr, "TPool"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TDuration") != null) {
      clientData.setTDuration(SOJParseClientInfo.getClientInfo(clientDatastr, "TDuration"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TStatus") != null) {
      clientData.setTStatus(SOJParseClientInfo.getClientInfo(clientDatastr, "TStatus"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TType") != null) {
      clientData.setTType(SOJParseClientInfo.getClientInfo(clientDatastr, "TType"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "ContentLength") != null) {
      clientData.setContentLength(SOJParseClientInfo
          .getClientInfo(clientDatastr, "ContentLength"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "ForwardedFor") != null) {
      clientData.setForwardFor(SOJParseClientInfo.getClientInfo(clientDatastr, "ForwardedFor"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Script") != null) {
      clientData.setScript(SOJParseClientInfo.getClientInfo(clientDatastr, "Script"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Server") != null) {
      clientData.setServer(SOJParseClientInfo.getClientInfo(clientDatastr, "Server"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TMachine") != null) {
      clientData.setTMachine(SOJParseClientInfo.getClientInfo(clientDatastr, "TMachine"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TStamp") != null) {
      clientData.setTStamp(SOJParseClientInfo.getClientInfo(clientDatastr, "TStamp"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TName") != null) {
      clientData.setTName(SOJParseClientInfo.getClientInfo(clientDatastr, "TName"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Agent") != null) {
      clientData.setAgent(SOJParseClientInfo.getClientInfo(clientDatastr, "Agent"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "RemoteIP") != null) {
      clientData.setRemoteIP(SOJParseClientInfo.getClientInfo(clientDatastr, "RemoteIP"));
    }

    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Encoding") != null) {
      clientData.setEncoding(SOJParseClientInfo.getClientInfo(clientDatastr, "Encoding"));
    }

    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Referer") != null) {
      clientData.setReferrer(SOJParseClientInfo.getClientInfo(clientDatastr, "Referer"));
    }

    return clientData;
  }

}
