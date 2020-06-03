package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.ClientData;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.IsValidIPv4;
import com.ebay.sojourner.common.util.SOJParseClientInfo;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetricsForSessionDQTest {

  private List<UbiEvent> ubiEventList;
  private SessionAccumulator sessionAccumulator;
  private SessionMetrics sessionMetrics;

  @BeforeEach
  public void setup() throws Exception {

    sessionAccumulator = new SessionAccumulator();
    sessionMetrics = SessionMetrics.getInstance();
    ubiEventList = new ArrayList<>();
    System.out.println(IsValidIPv4.isValidIP(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5"));

  }

  @Test
  public void test_TrafficSourceMetric1() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.380")));
    ubiEvent.setRemoteIP("10.164.146.154");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0%3C%3F%3E%3A%3D5-171e862b1cd-0x19a");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,"
            + "GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(449944858978L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&c=1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&px=4249&chnl=9&n=3a68651d1660abc2dbb5278fe7b14991&uc=1&es=0&nqt=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&p=2317508&uaid=e862b1cd1710aadc268190d6d851a3c7S0&bs=0&t=0&u=1496174693&cflgs=QA**&ul=en-US&plmt=2wAAAB%252BLCAAAAAAAAAA1jksOwjAMRO8y6yycKsRpbsEadRGSFCoVWvWDQFXujmnFaqTn8bM3jDf4yjjtnFMYO%252FjLhi7BayJHRiEKqkgzncgIs4YVJmGwKadcc8uc2Fx1qF02TJks21BTbKGQF9E1kuHQytpz7XuF8BNoLT4ttfD684dwLfGGJ4Uh7hHmI4Z9NstvG%252BYcpng%252Fr3n6iAmlyJW47IUoBSpN%252BQIKkKbg2wAAAA%253D%253D&ec=4&pn=2&rq=kmC8PxpF%2F9y3&pagename=cos__mfe&po=%5B%28pg%3A2481888+pid%3A100804%29%5D&ciid=YrHNwmg*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D1c2d989a5f78e81d%26REQUEST_GUID"
            + "%3D171e862b-1cc0-aadc-2680-1c05eaaa4309%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0"
            + "%253C%253F%253E%253A%253D5-171e862b1cd-0x19a%26cal_mod%3Dfalse&TPool=r1rover"
            + "&TDuration"
            + "=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48.43"
            + ".216&Script=/roverimp/0/2481888/9&Server=internal.rover.vip.ebay.com&TMachine=10.173"
            + ".194.104&TStamp=22:08:34.38&TName=roverimp&Agent=ebayUserAgent/eBayAndroid;6.0.1;"
            + "Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,GingerClient/2.9"
            + ".7-RELEASE&RemoteIP=10.164.146.154"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2481888/9?site=0&trknvp=n%3D3a68651d1660abc2dbb5278fe7b14991%26u"
            + "%3D1496174693%26plmt%3D2wAAAB"
            + "%252BLCAAAAAAAAAA1jksOwjAMRO8y6yycKsRpbsEadRGSFCoVWvWDQFXujmnFaqTn8bM3jDf4yjjtnFMYO"
            +
            "%252FjLhi7BayJHRiEKqkgzncgIs4YVJmGwKadcc8uc2Fx1qF02TJks21BTbKGQF9E1kuHQytpz7XuF8BNoLT4ttfD684dwLfGGJ4Uh7hHmI4Z9NstvG%252BYcpng%252Fr3n6iAmlyJW47IUoBSpN%252BQIKkKbg2wAAAA%253D%253D%26po%3D%5B%28pg%3A2481888+pid%3A100804%29%5D&trknvpsvc=%3Ca%3Enqc%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26nqt%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26es%3D0%26ec%3D4%3C%2Fa%3E&tguid=adedd9ac15d76d1dd9e1f3f001b26d8d&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2493970);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.295")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("763519d1c1a77e14");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dosusqn47pse31%285044103%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e862b177"
            + "-0xc6");
    ubiEvent.setEventFamily("CART");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(939441238370L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AEQ*");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&flgs=AEQ*&cartic=0&dm=HUAWEI&dn=HWPAR&mcc=false&uic=0&uc=56&!xe=23243&tic=0&nqt=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&mos=Android&bs=197&osv=9&nmq=0&ul=en-US&callingpagename=cartexsvc__DefaultPage&!xt=59962&ec=4&pagename=cartexsvc__DefaultPage&app=2571&res=0x0&uit=1588741714191&efam=CART&mav=6.0.1&crtsz=0&ebc=5212778693&g=adedd9ac15d76d1dd9e1f3f001b26d8d&rpg=2493970&h=ac&num_itm_unavbl=0&prof=ANDROID&nativeApp=true&cp=2493970&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&es=0&p=2493970&t=0&u=1496174693&cflgs=AA**&cmd=DefaultPage&xt=59962&issmeoic=false&eactn=EXPC&bic=0&rq=763519d1c1a77e14&ciid=YrEPu9o*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Df11926985e66045b%26node_id%3Dd2eec94149f0a26d%26REQUEST_GUID"
            + "%3D171e862b-1730-a68b-bda5-c166fa3d78c2%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%25285044103%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e862b177-0xc6%26cal_mod"
            + "%3Dfalse&TPool=r1cartexsvc&TDuration=1&TStatus=0&TType=URL&ContentLength=1654"
            + "&ForwardedFor=92.122.154.106; 2.23.97.28;37.48.43"
            + ".216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.104.187"
            + ".218&TStamp=22:08:34.29&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString("/experience/shopping_cart/v1/cart?mode=lite");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.446")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%285331%3E62%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b20f"
            + "-0xd4");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSPROV");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521017078370L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&modulectx=prov%3ARECENTLY_VIEWED_ITEMS_PROVIDER%7Cscen"
            + "%3Anative_recently_viewed_items_1%7Csver%3A%7Cuxcg%3A&dm=HUAWEI&dn=HWPAR&modulekey"
            + "=mi"
            + "%3A4236%7Ciid%3A1&uc=56&mos=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3"
            + "-74dbd1808f0a"
            + "&osv=9&ul=en-US&callingpagename"
            + "=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename"
            + "=ANSWERS_PLATFORM_PAGE&chunkcnt=2&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0"
            + ".1&ansdomain=HOMEPAGE&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp"
            + "=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u"
            + "=1496174693"
            + "&cflgs=AAE*&chunknum=2&eactn=ANSPROV&rq=kmC8PxpF%2F9y3&moduledata=c%3A5%7Citm"
            + "%3A324011747498%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC"
            + ".SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid"
            + "%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D5"
            + "%252526rkt%25253D7%252526mehot%25253Dpp%252526sd%25253D333335313556%252526itm"
            + "%25253D324011747498%252526lsid%25253D0%252526ci%25253D75708%252526itmf%25253D1"
            + "%252526sid%25253DAQAEAAAAEIj5Lt5whu8xW8zqJoulg1A%2525253D%252526pmt%25253D0"
            + "%252526noa"
            + "%25253D1%252526pg%25253D2481888%252526brand%25253DStar%252BWars%2Cc%3A6%7Citm"
            + "%3A123832811152%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC"
            + ".SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid"
            + "%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D6"
            + "%252526rkt%25253D7%252526mehot%25253Dnone%252526sd%25253D333335313556%252526itm"
            + "%25253D123832811152%252526lsid%25253D0%252526ci%25253D86207%252526plcampt%25253D0"
            + "%2525253A11687061018%252526itmf%25253D1%252526sid%25253DAQAEAAAAEBUcbl8vHmQ7v"
            + "%2525252BY9ryTfhUk%2525253D%252526pmt%25253D1%252526noa%25253D1%252526pg"
            + "%25253D2481888"
            + "%2Cc%3A7%7Citm%3A273611584619%7CclickTracking%3Aaid%25253D111001%252526algo"
            + "%25253DREC"
            + ".SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid"
            + "%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D7"
            + "%252526rkt%25253D7%252526mehot%25253Dpp%252526sd%25253D333335313556%252526itm"
            + "%25253D273611584619%252526lsid%25253D0%252526ci%25253D15687%252526plcampt%25253D0"
            + "%2525253A10553540014%252526itmf%25253D1%252526sid"
            + "%25253DAQAEAAAAELUD1mrbetF0XjYdhwRZLzg%2525253D%252526pmt%25253D1%252526noa%25253D1"
            + "%252526pg%25253D2481888%252526brand%25253DAffliction&ciid=YrIJT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D870b3b7e5ed47b92%26REQUEST_GUID"
            + "%3D171e862b-20a0-a9e4-f794-ef6bf808423e%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dpiesqn47pse31"
            + "%25285331%253E62%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b20f-0xd4"
            + "%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=2432&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43"
            + ".216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79"
            + ".121&TStamp=22:08:34.44&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components"
            + "=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,"
            + "FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,"
            + "NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2530290);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.285")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("763519d1c1a77e14");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285%3F1522%3B-171e862b16d-0xd8");
    ubiEvent.setEventFamily("MFE");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(826334163298L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&flgs=AA**&dm=HUAWEI&dn=HWPAR&uc=56&nqt=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&mos=Android&bs=197&osv=9&plmt=twAAAB%252BLCAAAAAAAAAA1jtsKgzAQRP9ln1NYYy5ufqX4sKSmCFqlNwqSf%252B9U6dOBYefsbLReKVknrUQ2tI6UzhuNF0oNs0RnKCM6iaE7SF1QKV4HmzvnNEQdcmi9iDpbuERPhoYnFD2ohwq122uaDOlPICLMDc70%252Fc9n5Hg9f3YseYc%252BDiwYUmHLsFoAw7j29Qu4GKSYtwAAAA%253D%253D&ul=en-US&callingpagename=cartexsvc__DefaultPage&ec=4&pagename=reco__experience_merchandising_v1_module_provider_GET&app=2571&res=0x0&uit=1588741714191&efam=MFE&mav=6.0.1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&rpg=2493970&h=ac&nativeApp=true&cp=2493970&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&es=0&p=2530290&t=0&u=1496174693&cflgs=AA**&eactn=EXPM&rq=763519d1c1a77e14&po=%5B%28pg%3A2493970+pid%3A100974%29%5D&ciid=YrFaZcA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D6895bb90afadf062%26node_id%3D11b082070a5fe676%26REQUEST_GUID"
            + "%3D171e862b-1690-aa46-5c02-382afbe15e16%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%25285%253F1522%253B-171e862b16d-0xd8%26cal_mod%3Dfalse&TPool=r1reco&TDuration=1"
            + "&TStatus=0&TType=URL&ContentLength=1438&ForwardedFor=92.122.154.106; 2.23.97.28;37"
            + ".48"
            + ".43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.164"
            + ".101"
            + ".192&TStamp=22:08:34.28&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping_cart/v1/cart?mode=lite");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2050454);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:33.828")));
    ubiEvent.setRemoteIP("10.158.42.70");
    ubiEvent.setRequestCorrelationId("f38380aebcb09fe1");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E37%28twwgsvv%28umj%28bad%7F%29%60jk-171e862b200-0x15c");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(790458577506L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=jakuvol2&tz=2.0&lv=1&dn=PAR-LX1&ist=0&rlutype=1&tzname=Europe%2FPrague&uc=1"
            + "&mos=Android&bs=0&uaid=e862b2001710a4d0bb825a4ce1423a0bS0&memsz=2048"
            + ".0&osv=9&ul=en-US&maup=1&mtsts=2020-05-05T22%3A08%3A33"
            + ".828&pagename=Install&app=2571&res=1080x2130&c=2&mav=6.0"
            + ".1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&reqts=1588741714424&cguidsrc=cookie&n"
            + "=3a68651d1660abc2dbb5278fe7b14991&ort=p&p=2050454&mnt=MOBILE&carrier=T-Mobile+CZ&t"
            + "=0&u"
            + "=1496174693&prefl=cs_CZ&cflgs=EA**&ids=MP%253Djakuvol2&designsystem=6&mrollp=43"
            + "&gadid"
            + "=ff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0&mppid=117&androidid=2b330951daa57eeb"
            + "&pcguid"
            + "=3a68651d1660abc2dbb5278fe7b14991&pn=2&rq=f38380aebcb09fe1&ciid=YrIAC7g*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Df38380aebcb09fe1%26node_id%3Dba5283e9f9bee29c%26REQUEST_GUID"
            + "%3D171e862b-1ff0-a4d0-bb86-da36ef453ee5%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E37%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e862b200-0x15c%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=28&TStatus=0&TType=URL&ContentLength=-1"
            + "&ForwardedFor"
            + "=37.48.43.216&Script=/roverimp/1/0/4&Server=localhost&TMachine=10.77.11"
            + ".184&TStamp=22:08:34.43&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.158.42"
            + ".70"));
    ubiEvent.setUrlQueryString(
        "/roverimp/1/0/4?res=1080x2130&memsz=2048"
            + ".0&designsystem=6&imp=2050454&lv=udid%3Dadedd9ac15d76d1dd9e1f3f001b26d8d%26ai%3D2571"
            + "%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AAmYSgD5SEqA"
            + "%252Bdj6x9nY%252BseQ**%26memsz%3D2048"
            + ".0%26res%3D1080x2130%26mrollp%3D43%26designsystem%3D6%26c%3D2%26osv%3D9%26ids%3DMP"
            + "%253Djakuvol2%26mnt%3DMOBILE%26ist%3D0%26prefl%3Dcs_CZ%26tzname%3DEurope%2FPrague"
            + "%26user_name%3Djakuvol2%26androidid%3D2b330951daa57eeb%26reqts%3D1588741714424%26tz"
            + "%3D2.0%26rlutype%3D1%26maup%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DT-Mobile+CZ"
            + "%26gadid%3Dff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0%26dn%3DPAR-LX1%26dpi%3D409"
            + ".432x409"
            + ".903%26mppid%3D117%26mtsts%3D2020-05-05T22%3A08%3A33"
            + ".828&mnt=MOBILE&prefl=cs_CZ&tzname=Europe%2FPrague&androidid=2b330951daa57eeb&maup=1"
            + "&ort=p&carrier=T-Mobile+CZ&dpi=409.432x409"
            + ".903&dn=PAR-LX1&site=0&mrollp=43&ou=nY%2BsHZ2PrBmdj6wVnY%2BsEZ2PrA2dj6AAmYSgD5SEqA"
            + "%2Bdj6x9nY%2BseQ**&c=2&mav=6.0"
            + ".1&osv=9&ids=MP%253Djakuvol2&udid=adedd9ac15d76d1dd9e1f3f001b26d8d&ist=0&rvrhostname"
            + "=rover.ebay.com&user_name=jakuvol2&ai=2571&rvrsite=0&reqts=1588741714424&tz=2"
            + ".0&rlutype=1&mos=Android&gadid=ff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0&mppid=117");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(2);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 6
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.241")));
    ubiEvent.setRemoteIP("10.173.210.218");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ckuvthu%60t*0217%3B%3C7%29pqtfwpu%29osu%29fgg%7E-fij-171e862b142-0x115");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,"
            + "GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(989088362850L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&c=1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&px=4249&chnl=9&n=3a68651d1660abc2dbb5278fe7b14991&uc=1&es=0&nqt=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&p=2317508&uaid=e862b1421710aa64ae61762dbfae42b1S0&bs=0&t=0&u=1496174693&cflgs=QA**&ul=en-US&plmt=8wAAAB%252BLCAAAAAAAAAAljrFuwzAMRP%252BFswbKUi3BX5EW2QIPMk03BpzIkJIiqeF%252F7zWZDjweH2%252Bj9Zu6xkcbYzS0ztSdNppH6ixzZGtIYDVsA3%252Bwh%252BddMFTgkTgdpknbyY3ieWij1VZ98hK8c6wNGdIbcD00vbE4u96XxVD6B4QQmC1i6QfTQUvN17TMvzoe8zpL%252FdIpyS0XJC7Io8vlQR0byvKSVN%252BSX7uKzhtVTUXOn3ctTzBp3%252FFd0KKBIMB7v%252F8BHMxBJPMAAAA%253D&ec=4&pn=2&rq=kmC8PxpF%2F9y3&pagename=cos__mfe&po=%5B%28pg%3A2481888+pid%3A100801%29%5D&ciid=YrFCSuY*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D4323d4bab869fd98%26REQUEST_GUID"
            + "%3D171e862b-1410-aa64-ae67-a4b5ddb929f8%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t"
            + "%2A0217"
            + "%253B%253C7%2529pqtfwpu%2529osu%2529fgg%257E-fij-171e862b142-0x115%26cal_mod%3Dfalse"
            + "&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48"
            + ".43"
            + ".216&Script=/roverimp/0/2481888/9&Server=internal.rover.vip.ebay.com&TMachine=10.166"
            + ".74.230&TStamp=22:08:34.24&TName=roverimp&Agent=ebayUserAgent/eBayAndroid;6.0.1;"
            + "Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,GingerClient/2.9"
            + ".7-RELEASE&RemoteIP=10.173.210.218"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2481888/9?site=0&trknvp=n%3D3a68651d1660abc2dbb5278fe7b14991%26u"
            + "%3D1496174693%26plmt%3D8wAAAB%252BLCAAAAAAAAAAljrFuwzAMRP"
            + "%252BFswbKUi3BX5EW2QIPMk03BpzIkJIiqeF%252F7zWZDjweH2"
            + "%252Bj9Zu6xkcbYzS0ztSdNppH6ixzZGtIYDVsA3%252Bwh"
            +
            "%252BddMFTgkTgdpknbyY3ieWij1VZ98hK8c6wNGdIbcD00vbE4u96XxVD6B4QQmC1i6QfTQUvN17TMvzoe8zpL"
            + "%252FdIpyS0XJC7Io8vlQR0byvKSVN%252BSX7uKzhtVTUXOn3ctTzBp3%252FFd0KKBIMB7v"
            + "%252F8BHMxBJPMAAAA%253D%26po%3D%5B%28pg%3A2481888+pid%3A100801%29%5D&trknvpsvc=%3Ca"
            + "%3Enqc"
            +
            "%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26nqt%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26es%3D0%26ec%3D4%3C%2Fa%3E&tguid=adedd9ac15d76d1dd9e1f3f001b26d8d&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 7
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.504")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%2854%3A%3C5%3E7%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh"
            + "-171e862b249-0xd6");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSTRIG");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521020813922L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&dm=HUAWEI&dn=HWPAR&uc=56&modulereq=prov%3AFEATURED_DEALS_PROVIDER%7Cmi%3A2615"
            + "%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A1%2Cprov%3APOPULAR_IN_INTERESTS_PROVIDER%7Cmi"
            + "%3A4497%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov"
            + "%3ARECENTLY_VIEWED_ITEMS_PROVIDER%7Cmi%3A4236%7Cenabled%3A1%7Ccandidate%3A1%7Ciid"
            + "%3A1"
            + "%2Cprov%3AWATCHING_PROVIDER%7Cmi%3A3607%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode"
            + "%3ANO_DATA%7Ciid%3A1%2Cprov%3AFULL_BLEED_BANNER_PROVIDER%7Cmi%3A4519%7Cenabled%3A1"
            + "%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov%3ALOYALTY_MODULE_PROVIDER"
            + "%7Cmi%3A4523%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov"
            + "%3AMULTI_CTA_BANNER_PROVIDER%7Cmi%3A43886%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode"
            + "%3ANO_DATA%7Ciid%3A1%2Cprov%3ANOTIFICATIONS_PROVIDER%7Cmi%3A4926%7Cenabled%3A1"
            + "%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1&mos=Android&bs=197&pageci=a2a23ecf"
            + "-8f57-11ea-a1d3-74dbd1808f0a&osv=9&ul=en-US&callingpagename"
            + "=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename"
            + "=ANSWERS_PLATFORM_PAGE&chunkcnt=1&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0"
            + ".1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an"
            + "=eBayAndroid&n"
            + "=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u=1496174693&cflgs=AAE*&moduletrig"
            + "=mi"
            + "%3A4236%7Ciid%3A1%7Cscen%3Anative_recently_viewed_items_1%7CpriorAlpha%3A1"
            + ".0%7CpriorBeta%3A1.0%7Calpha%3A0%7Cbeta%3A0%7Cscore%3A0"
            + ".9162149121594685%7Cforcedpos%3A2%7Cshown%3A1%7Crank%3A2%2Cmi%3A2615%7Ciid%3A1"
            + "%7Cscen"
            + "%3Anative_featured_deals_1%7CpriorAlpha%3A1.0%7CpriorBeta%3A1"
            + ".0%7Calpha%3A0%7Cbeta%3A0%7Cscore%3A0"
            + ".653219759768443%7Cforcedpos%3A7%7Cshown%3A1%7Crank%3A7&chunknum=1&eactn=ANSTRIG&rq"
            + "=kmC8PxpF%2F9y3&ciid=YrJCT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3Df2fd1a4124245abe%26REQUEST_GUID"
            + "%3D171e862b0b4.abd80a2.17e8.b1229534%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31"
            + "%252854%253A%253C5%253E7%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b249"
            + "-0xd6"
            + "%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=1851&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43"
            + ".216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79"
            + ".121&TStamp=22:08:34.50&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components"
            + "=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,"
            + "FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,"
            + "NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 8
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2481888);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.506")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%285354025%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b24b"
            + "-0x130");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521032544354L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&modulectx=prov%3AFEATURED_DEALS_PROVIDER%7Cscen%3Anative_featured_deals_1"
            + "%7Csver%3A%7Cuxcg%3A&dm=HUAWEI&dn=HWPAR&modulekey=mi%3A2615%7Ciid%3A1&uc=56&mos"
            + "=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9&ul=en-US"
            + "&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET"
            + "&pagename=ANSWERS_PLATFORM_PAGE&chunkcnt=1&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0"
            + ".1&ansdomain=HOMEPAGE&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp"
            + "=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u"
            + "=1496174693"
            + "&cflgs=AAE*&chunknum=1&eactn=ANSPROV&rq=kmC8PxpF%2F9y3&moduledata=c%3A1%7Citm"
            + "%3A264379674226%2Cc%3A2%7Citm%3A223649956355%2Cc%3A3%7Citm%3A272622872854%2Cc%3A4"
            + "%7Citm%3A143435259741%2Cc%3A5%7Citm%3A223750802816%2Cc%3A6%7Citm%3A254510009388%2Cc"
            + "%3A7%7Citm%3A183746331896%2Cc%3A8%7Citm%3A163271073369%2Cc%3A9%7Citm%3A264526512109"
            + "%2Cc%3A10%7Citm%3A372993341934%2Cc%3A11%7Citm%3A124048209325%2Cc%3A12%7Citm"
            + "%3A392384692424%2Cc%3A13%7Citm%3A151203678229%2Cc%3A14%7Citm%3A302397270994%2Cc%3A15"
            + "%7Citm%3A303455179376&ciid=YrEtT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D159900a3194a633e%26REQUEST_GUID"
            + "%3D171e862b-12d0-a9e4-f794-ef6bf8084240%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dpiesqn47pse31"
            + "%252851757%253E1-171e862b145-0x135%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2"
            + "&TStatus=0&TType=URL&ContentLength=1363&ForwardedFor=92.122.154.93; 2.23.97.28;37.48"
            + ".43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158"
            + ".79"
            + ".121&TStamp=22:08:34.24&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components"
            + "=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,"
            + "FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,"
            + "NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 9
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.244")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%2851757%3E1-171e862b145-0x135");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSPROV");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521019437410L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&flgs=AA**&dm=HUAWEI&dn=HWPAR&uc=56&nqt=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&mos=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9&ul=en-US&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&ec=4&pagename=hpservice__experience_search_v1_get_homepage_search_result_GET&app=2571&res=0x0&uit=1588741714165&efam=HOMEPAGE&mav=6.0.1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&es=0&p=2481888&t=0&u=1496174693&cflgs=AA**&lfcat=0&eactn=EXPC&rq=kmC8PxpF%2F9y3&ciid=YrD1T3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D74516a9107f63cf0%26node_id%3Dc66a3016573c6641%26REQUEST_GUID"
            + "%3D171e862b-2470-a9e4-f794-ef6bf808423b%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dpiesqn47pse31"
            + "%25285354025%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b24b-0x130"
            + "%26cal_mod"
            + "%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=1512"
            + "&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43"
            + ".216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79"
            + ".121&TStamp=22:08:34.50&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components"
            + "=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,"
            + "FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,"
            + "NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 10
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2051248);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:32.600")));
    ubiEvent.setRemoteIP("10.153.189.142");
    ubiEvent.setRequestCorrelationId("75df0990fa0c8827");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E53%28twwgsvv%28umj%28bad%7F%29%60jk-171e862b0ec-0x12c");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(696102203490L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "user_name=jakuvol2&tz=2.0&lv=1&dn=PAR-LX1&ist=0&tzname=Europe%2FPrague&uc=1&mos=Android"
            + "&bs=0&uaid=e862b0ec1710a4d12a20560698764b99S0&memsz=2048"
            + ".0&osv=9&ul=en-US&mtsts=2020-05-05T22%3A08%3A32"
            + ".600&pagename=Foreground&app=2571&res=1080x2130&c=1&mav=6.0"
            + ".1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&reqts=1588741714149&cguidsrc=cookie&n"
            + "=3a68651d1660abc2dbb5278fe7b14991&ort=p&p=2051248&ttp=Page&mnt=MOBILE&carrier=T"
            + "-Mobile"
            + "+CZ&t=0&u=1496174693&prefl=cs_CZ&cflgs=EA**&ids=MP%253Djakuvol2&designsystem=6"
            + "&mrollp"
            + "=43&gadid=ff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0&mppid=0&androidid"
            + "=2b330951daa57eeb"
            + "&pcguid=3a68651d1660abc2dbb5278fe7b14991&pn=2&rq=75df0990fa0c8827&ciid=YrDsEqI*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D75df0990fa0c8827%26node_id%3D6f9c55062df2226d%26REQUEST_GUID"
            + "%3D171e862b-0eb0-a4d1-2a20-f62dc76e7006%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E53%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e862b0ec-0x12c%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=37.48.43.216&Script=/roverimp/0/0/14&Server=rover.ebay.com&TMachine=10.77.18"
            + ".162&TStamp=22:08:34.15&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.153"
            + ".189"
            + ".142"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?imp=2051248&lv=udid%3Dadedd9ac15d76d1dd9e1f3f001b26d8d%26ai%3D2571"
            + "%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AAmYSgD5SEqA"
            + "%252Bdj6x9nY%252BseQ**%26memsz%3D2048"
            + ".0%26res%3D1080x2130%26mrollp%3D43%26designsystem%3D6%26c%3D1%26osv%3D9%26ids%3DMP"
            + "%253Djakuvol2%26mnt%3DMOBILE%26prefl%3Dcs_CZ%26ist%3D0%26tzname%3DEurope%2FPrague"
            + "%26user_name%3Djakuvol2%26androidid%3D2b330951daa57eeb%26reqts%3D1588741714149%26tz"
            + "%3D2.0%26mos%3DAndroid%26ort%3Dp%26carrier%3DT-Mobile+CZ%26gadid%3Dff41b8f3-a816"
            + "-4d74"
            + "-87b2-963e3fc137e9%2C0%26dn%3DPAR-LX1%26dpi%3D409.432x409"
            + ".903%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A08%3A32.600");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 11
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.446")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%28533110%3B%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b20f"
            + "-0xe6");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSPROV");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521017078370L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&modulectx=prov%3ARECENTLY_VIEWED_ITEMS_PROVIDER%7Cscen"
            + "%3Anative_recently_viewed_items_1%7Csver%3A%7Cuxcg%3A&dm=HUAWEI&dn=HWPAR&modulekey"
            + "=mi"
            + "%3A4236%7Ciid%3A1&uc=56&mos=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3"
            + "-74dbd1808f0a"
            + "&osv=9&ul=en-US&callingpagename"
            + "=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename"
            + "=ANSWERS_PLATFORM_PAGE&chunkcnt=2&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0"
            + ".1&ansdomain=HOMEPAGE&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp"
            + "=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u"
            + "=1496174693"
            + "&cflgs=AAE*&chunknum=1&eactn=ANSPROV&rq=kmC8PxpF%2F9y3&moduledata=c%3A1%7Citm"
            + "%3A333335313556%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC"
            + ".SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid"
            + "%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D1"
            + "%252526rkt%25253D7%252526mehot%25253Dpp%252526sd%25253D333335313556%252526itm"
            + "%25253D333335313556%252526lsid%25253D2%252526ci%25253D177791%252526plcampt%25253D0"
            + "%2525253A11714325015%252526itmf%25253D1%252526sid%25253DAQAEAAAAECXFDDamd9sYGVArhy9"
            + "%2525252FQUc%2525253D%252526pmt%25253D1%252526noa%25253D1%252526pg%25253D2481888%2Cc"
            + "%3A2%7Citm%3A123701322614%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC"
            + ".SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid"
            + "%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D2"
            + "%252526rkt%25253D7%252526mehot%25253Dnone%252526sd%25253D333335313556%252526itm"
            + "%25253D123701322614%252526lsid%25253D15%252526ci%25253D176997%252526itmf%25253D1"
            + "%252526sid%25253DAQAEAAAAEHAVzRKP1FzOi1wOwd8gGm0%2525253D%252526pmt%25253D0"
            + "%252526noa"
            + "%25253D1%252526pg%25253D2481888%2Cc%3A3%7Citm%3A124004802327%7CclickTracking%3Aaid"
            + "%25253D111001%252526algo%25253DREC"
            + ".SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid"
            + "%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D3"
            + "%252526rkt%25253D7%252526mehot%25253Dnone%252526sd%25253D333335313556%252526itm"
            + "%25253D124004802327%252526lsid%25253D15%252526ci%25253D20349%252526itmf%25253D1"
            + "%252526sid%25253DAQAEAAAAEJClEiElN79bR672jYOLeH0%2525253D%252526pmt%25253D0"
            + "%252526noa"
            + "%25253D1%252526pg%25253D2481888%252526brand%25253DUnbranded%2Cc%3A4%7Citm"
            + "%3A254465165203%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC"
            + ".SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid"
            + "%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D4"
            + "%252526rkt%25253D7%252526mehot%25253Dnone%252526sd%25253D333335313556%252526itm"
            + "%25253D254465165203%252526lsid%25253D0%252526ci%25253D63861%252526plcampt%25253D0"
            + "%2525253A11547188011%252526itmf%25253D1%252526sid"
            + "%25253DAQAEAAAAEOeGeSHL27PicQlZLvMUZSY%2525253D%252526pmt%25253D1%252526noa%25253D1"
            + "%252526pg%25253D2481888%252526brand%25253DUnbranded&ciid=YrIJT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3Df210854fd7742920%26REQUEST_GUID"
            + "%3D171e862b-20a0-a9e4-f794-ef6bf808423f%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dpiesqn47pse31"
            + "%2528533110%253B%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b20f-0xe6"
            + "%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=2852&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43"
            + ".216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79"
            + ".121&TStamp=22:08:34.44&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components"
            + "=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,"
            + "FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,"
            + "NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 12
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.414")));
    ubiEvent.setRemoteIP("10.69.226.234");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*063%3E4%3C%28twwgsvv%28umj%28bad%7F%29%60jk-171e862b1ef-0x19d");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,"
            + "GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(581866336610L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&c=1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&px=4249&chnl=9&n=3a68651d1660abc2dbb5278fe7b14991&uc=1&es=0&nqt=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&p=2317508&uaid=e862b1ef1710a4b79876eb3ad108d845S0&bs=0&t=0&u=1496174693&cflgs=QA**&ul=en-US&plmt=9gYAAB%252BLCAAAAAAAAACNlVtT2kAYhv%252FL3jYz3d3skRkvqKBoaRUtVnQcJyQbCCQk5gCCw3%252Fvl4CVUkLLzR6y%252B%252B6z75svvKFkhBqUKaKUslASoMbjGwo81CAYK2xbyIUpionEHDOYE1RYKIU5JMSQM6k85mnCjNQOc5R2mTLMMBcbH1nI5CD3BK2zkd2qw2amWaoiMoJFEWrMijC0UBa%252F95LyeEIs5EPHQkGGGm%252BIwDYipdQErdfWRoZjFWSrqV8rw7gQf8tQG39okGmeTWfG1GoISvkhFKG1%252FJCxVTJdhf5LrQzF%252BAAJtpneYVlOooJ64ogtR0TA6SBJf3sMd4SZuM50lCTooOlhJb97CC7VGoRIwmzKMeGwcScOC9FyAK0NbbPXbDfh1z69P2u1nMjT2eD8rpmOl%252Fpzr%252B%252BewDK2Xc6rCKEjthOyvE1dNGgWz8w%252B8iYcQKa1Ee1hdZp3q5uv1%252BRsdRWQxdXCU6PzCJ%252FsUcAddzCWudaFfwSjygUo7Lp09iAuT8N20A6%252FSz28EZJOBldd0zkKYauFt4pseQSCYi0qCnY4PihXKHPIeBufsJWoTe%252FKnJvbTpfK68DthQ%252Fd%252Bbf%252Bw%252B1gN71d0p16YqyYKuWb2rdMcq0rzANVJbnEah%252FlYsK7OV%252BMC%252FX6U61eLuMiHJHmSd0L9Edtz5fFIsyOmUa3NOKwaUJJLKAw1NY0JSiWdaZ96bvDUM07UU%252FOPw10uvzhj%252FvT%252FzDNHidE58Wq1jRqbzHlQUzMuc0ZxoS9lyYH8DrMbr9FonRo8jN8Pxl448XNQ3c1%252Bmdl4s03xqmcJfDKlw%252Bc%252BTtlFFRc0StqQDHEbsXqZNXIiatnmVcyZ8ZJ3XGvMOkSlNAaFsOVHlESuk6U5CD6HPnm2XXKbpLGUZwbrzTEhKFJL8ruLG7meRoMizyIZzAex%252FnMZFnLyR0YBXlUFmuYwR%252FZU4ns5tXxLhyP10%252FrX3QpBub2BgAA&ec=4&pn=2&rq=kmC8PxpF%2F9y3&pagename=cos__mfe&po=%5B%28pg%3A2481888+pid%3A100803+pladvids%3A%5B0%5D%29%5D&ciid=YrHveYc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3Dc5c3749c59c532cb%26REQUEST_GUID"
            + "%3D171e862b-1ee0-a4b7-9876-9de8e84e25ad%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A063"
            + "%253E4%253C%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e862b1ef-0x19d%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=37.48.43.216&Script=/roverimp/0/2481888/9&Server=internal.rover.vip.ebay"
            + ".com&TMachine=10.75.121.135&TStamp=22:08:34"
            + ".41&TName=roverimp&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;"
            + "T-Mobile CZ;1080x2130;2.5,GingerClient/2.9.7-RELEASE&RemoteIP=10.69.226.234"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2481888/9?site=0&trknvp=n%3D3a68651d1660abc2dbb5278fe7b14991%26u"
            + "%3D1496174693%26plmt%3D9gYAAB%252BLCAAAAAAAAACNlVtT2kAYhv"
            + "%252FL3jYz3d3skRkvqKBoaRUtVnQcJyQbCCQk5gCCw3%252Fvl4CVUkLLzR6y%252B"
            +
            "%252B6z75svvKFkhBqUKaKUslASoMbjGwo81CAYK2xbyIUpionEHDOYE1RYKIU5JMSQM6k85mnCjNQOc5R2mTLMMBcbH1nI5CD3BK2zkd2qw2amWaoiMoJFEWrMijC0UBa%252F95LyeEIs5EPHQkGGGm%252BIwDYipdQErdfWRoZjFWSrqV8rw7gQf8tQG39okGmeTWfG1GoISvkhFKG1%252FJCxVTJdhf5LrQzF%252BAAJtpneYVlOooJ64ogtR0TA6SBJf3sMd4SZuM50lCTooOlhJb97CC7VGoRIwmzKMeGwcScOC9FyAK0NbbPXbDfh1z69P2u1nMjT2eD8rpmOl%252Fpzr%252B%252BewDK2Xc6rCKEjthOyvE1dNGgWz8w%252B8iYcQKa1Ee1hdZp3q5uv1%252BRsdRWQxdXCU6PzCJ%252FsUcAddzCWudaFfwSjygUo7Lp09iAuT8N20A6%252FSz28EZJOBldd0zkKYauFt4pseQSCYi0qCnY4PihXKHPIeBufsJWoTe%252FKnJvbTpfK68DthQ%252Fd%252Bbf%252Bw%252B1gN71d0p16YqyYKuWb2rdMcq0rzANVJbnEah%252FlYsK7OV%252BMC%252FX6U61eLuMiHJHmSd0L9Edtz5fFIsyOmUa3NOKwaUJJLKAw1NY0JSiWdaZ96bvDUM07UU%252FOPw10uvzhj%252FvT%252FzDNHidE58Wq1jRqbzHlQUzMuc0ZxoS9lyYH8DrMbr9FonRo8jN8Pxl448XNQ3c1%252Bmdl4s03xqmcJfDKlw%252Bc%252BTtlFFRc0StqQDHEbsXqZNXIiatnmVcyZ8ZJ3XGvMOkSlNAaFsOVHlESuk6U5CD6HPnm2XXKbpLGUZwbrzTEhKFJL8ruLG7meRoMizyIZzAex%252FnMZFnLyR0YBXlUFmuYwR%252FZU4ns5tXxLhyP10%252FrX3QpBub2BgAA%26po%3D%5B%28pg%3A2481888+pid%3A100803+pladvids%3A%5B0%5D%29%5D&trknvpsvc=%3Ca%3Enqc%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26nqt%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26es%3D0%26ec%3D4%3C%2Fa%3E&tguid=adedd9ac15d76d1dd9e1f3f001b26d8d&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 13
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2051249);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:37.205")));
    ubiEvent.setRemoteIP("10.156.85.171");
    ubiEvent.setRequestCorrelationId("15af57f8f54a33e3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*0631%3A2%28twwgsvv%28umj%28bad%7F%29%60jk-171e862bc3e-0x12a");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(474312784994L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&c=1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&px=4249&chnl=9&n=3a68651d1660abc2dbb5278fe7b14991&uc=1&es=0&nqt=AQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*&p=2317508&uaid=e862b1ef1710a4b79876eb3ad108d845S0&bs=0&t=0&u=1496174693&cflgs=QA**&ul=en-US&plmt=9gYAAB%252BLCAAAAAAAAACNlVtT2kAYhv%252FL3jYz3d3skRkvqKBoaRUtVnQcJyQbCCQk5gCCw3%252Fvl4CVUkLLzR6y%252B%252B6z75svvKFkhBqUKaKUslASoMbjGwo81CAYK2xbyIUpionEHDOYE1RYKIU5JMSQM6k85mnCjNQOc5R2mTLMMBcbH1nI5CD3BK2zkd2qw2amWaoiMoJFEWrMijC0UBa%252F95LyeEIs5EPHQkGGGm%252BIwDYipdQErdfWRoZjFWSrqV8rw7gQf8tQG39okGmeTWfG1GoISvkhFKG1%252FJCxVTJdhf5LrQzF%252BAAJtpneYVlOooJ64ogtR0TA6SBJf3sMd4SZuM50lCTooOlhJb97CC7VGoRIwmzKMeGwcScOC9FyAK0NbbPXbDfh1z69P2u1nMjT2eD8rpmOl%252Fpzr%252B%252BewDK2Xc6rCKEjthOyvE1dNGgWz8w%252B8iYcQKa1Ee1hdZp3q5uv1%252BRsdRWQxdXCU6PzCJ%252FsUcAddzCWudaFfwSjygUo7Lp09iAuT8N20A6%252FSz28EZJOBldd0zkKYauFt4pseQSCYi0qCnY4PihXKHPIeBufsJWoTe%252FKnJvbTpfK68DthQ%252Fd%252Bbf%252Bw%252B1gN71d0p16YqyYKuWb2rdMcq0rzANVJbnEah%252FlYsK7OV%252BMC%252FX6U61eLuMiHJHmSd0L9Edtz5fFIsyOmUa3NOKwaUJJLKAw1NY0JSiWdaZ96bvDUM07UU%252FOPw10uvzhj%252FvT%252FzDNHidE58Wq1jRqbzHlQUzMuc0ZxoS9lyYH8DrMbr9FonRo8jN8Pxl448XNQ3c1%252Bmdl4s03xqmcJfDKlw%252Bc%252BTtlFFRc0StqQDHEbsXqZNXIiatnmVcyZ8ZJ3XGvMOkSlNAaFsOVHlESuk6U5CD6HPnm2XXKbpLGUZwbrzTEhKFJL8ruLG7meRoMizyIZzAex%252FnMZFnLyR0YBXlUFmuYwR%252FZU4ns5tXxLhyP10%252FrX3QpBub2BgAA&ec=4&pn=2&rq=kmC8PxpF%2F9y3&pagename=cos__mfe&po=%5B%28pg%3A2481888+pid%3A100803+pladvids%3A%5B0%5D%29%5D&ciid=YrHveYc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3Dc5c3749c59c532cb%26REQUEST_GUID"
            + "%3D171e862b-1ee0-a4b7-9876-9de8e84e25ad%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A063"
            + "%253E4%253C%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e862b1ef-0x19d%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=37.48.43.216&Script=/roverimp/0/2481888/9&Server=internal.rover.vip.ebay"
            + ".com&TMachine=10.75.121.135&TStamp=22:08:34"
            + ".41&TName=roverimp&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;"
            + "T-Mobile CZ;1080x2130;2.5,GingerClient/2.9.7-RELEASE&RemoteIP=10.69.226.234"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2481888/9?site=0&trknvp=n%3D3a68651d1660abc2dbb5278fe7b14991%26u"
            + "%3D1496174693%26plmt%3D9gYAAB%252BLCAAAAAAAAACNlVtT2kAYhv"
            + "%252FL3jYz3d3skRkvqKBoaRUtVnQcJyQbCCQk5gCCw3%252Fvl4CVUkLLzR6y%252B"
            +
            "%252B6z75svvKFkhBqUKaKUslASoMbjGwo81CAYK2xbyIUpionEHDOYE1RYKIU5JMSQM6k85mnCjNQOc5R2mTLMMBcbH1nI5CD3BK2zkd2qw2amWaoiMoJFEWrMijC0UBa%252F95LyeEIs5EPHQkGGGm%252BIwDYipdQErdfWRoZjFWSrqV8rw7gQf8tQG39okGmeTWfG1GoISvkhFKG1%252FJCxVTJdhf5LrQzF%252BAAJtpneYVlOooJ64ogtR0TA6SBJf3sMd4SZuM50lCTooOlhJb97CC7VGoRIwmzKMeGwcScOC9FyAK0NbbPXbDfh1z69P2u1nMjT2eD8rpmOl%252Fpzr%252B%252BewDK2Xc6rCKEjthOyvE1dNGgWz8w%252B8iYcQKa1Ee1hdZp3q5uv1%252BRsdRWQxdXCU6PzCJ%252FsUcAddzCWudaFfwSjygUo7Lp09iAuT8N20A6%252FSz28EZJOBldd0zkKYauFt4pseQSCYi0qCnY4PihXKHPIeBufsJWoTe%252FKnJvbTpfK68DthQ%252Fd%252Bbf%252Bw%252B1gN71d0p16YqyYKuWb2rdMcq0rzANVJbnEah%252FlYsK7OV%252BMC%252FX6U61eLuMiHJHmSd0L9Edtz5fFIsyOmUa3NOKwaUJJLKAw1NY0JSiWdaZ96bvDUM07UU%252FOPw10uvzhj%252FvT%252FzDNHidE58Wq1jRqbzHlQUzMuc0ZxoS9lyYH8DrMbr9FonRo8jN8Pxl448XNQ3c1%252Bmdl4s03xqmcJfDKlw%252Bc%252BTtlFFRc0StqQDHEbsXqZNXIiatnmVcyZ8ZJ3XGvMOkSlNAaFsOVHlESuk6U5CD6HPnm2XXKbpLGUZwbrzTEhKFJL8ruLG7meRoMizyIZzAex%252FnMZFnLyR0YBXlUFmuYwR%252FZU4ns5tXxLhyP10%252FrX3QpBub2BgAA%26po%3D%5B%28pg%3A2481888+pid%3A100803+pladvids%3A%5B0%5D%29%5D&trknvpsvc=%3Ca%3Enqc%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26nqt%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26es%3D0%26ec%3D4%3C%2Fa%3E&tguid=adedd9ac15d76d1dd9e1f3f001b26d8d&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(3);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 14
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.503")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%28533110%3B%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b247"
            + "-0xcb");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSTMPL");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521020682850L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&dm=HUAWEI&dn=HWPAR&locale=en-US-x-lvariant-MAIN&pagetmpl=HomePage&uc=56"
            + "&modulergn=rgn%3ARIVER%7Crank%3A1%7Cmi%3A4236%7Ciid%3A1%7Cuxcg"
            + "%3AITEMS_CAROUSEL_GROUP"
            + "%7Cscen%3Anative_recently_viewed_items_1%7Cisfixed%3Atrue%2Crgn%3ARIVER%7Crank%3A2"
            + "%7Cmi%3A2615%7Ciid%3A1%7Cuxcg%3AITEMS_CAROUSEL_GROUP%7Cscen"
            + "%3Anative_featured_deals_1"
            + "%7Cisfixed%3Atrue&mos=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a"
            + "&osv=9"
            + "&ul=en-US&callingpagename"
            + "=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename"
            + "=ANSWERS_PLATFORM_PAGE&chunkcnt=1&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0"
            + ".1&ansdomain=HOMEPAGE&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp"
            + "=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&anschannel"
            + "=NATIVE"
            + "&t=0&u=1496174693&cflgs=AAE*&chunknum=1&eactn=ANSTMPL&rq=kmC8PxpF%2F9y3&ciid"
            + "=YrJAT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D265572e2f0a5f479%26REQUEST_GUID"
            + "%3D171e862b-2400-a9e4-f794-ef6bf808423c%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dpiesqn47pse31"
            + "%2528533110%253B%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b247-0xcb"
            + "%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=1246&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43"
            + ".216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79"
            + ".121&TStamp=22:08:34.50&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components"
            + "=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,"
            + "FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,"
            + "NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1496174693");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 15
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(3084);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.886")));
    ubiEvent.setRemoteIP("10.212.176.56");
    ubiEvent.setRequestCorrelationId("af383200bc425749");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*5ft3%7B%28rbpv6710-171e862b3c7-0x14b");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(628504376162L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "app=2571&c=1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&nid=&h=ac&cguidsrc=cookie&n"
            + "=3a68651d1660abc2dbb5278fe7b14991&uc=1&p=3084&uaid"
            + "=e862b3c71710a6e55920a9f1f0e89f39S0"
            + "&bs=0&rvrid=2410890507510&t=0&cflgs=EA**&ul=en-US&mppid=117&pn=2&pcguid"
            + "=3a68651d1660abc2dbb5278fe7b14991&rq=af383200bc425749&pagename=EntryTracking&ciid"
            + "=YrPHVZI*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Daf383200bc425749%26node_id%3Db975e50910b5b972%26REQUEST_GUID"
            + "%3D171e862b-3c60-a6e5-5924-af0bf8141767%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A5ft3"
            + "%257B%2528rbpv6710-171e862b3c7-0x14b%26cal_mod%3Dfalse&TPool=r1rover&TDuration=16"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48.43"
            + ".216&Script=/rover/1/0/4&Server=localhost&TMachine=10.110.85.146&TStamp=22:08:34"
            + ".88&TName=rover&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.212.176.56"));
    ubiEvent.setUrlQueryString(
        "/rover/1/0/4?memsz=2048.0&res=1080x2130&designsystem=6&mnt=MOBILE&prefl=cs_CZ&tzname"
            + "=Europe%2FPrague&androidid=2b330951daa57eeb&ort=p&carrier=T-Mobile+CZ&dpi=409"
            + ".432x409"
            + ".903&dn=PAR-LX1&mtsts=2020-05-05T22%3A08%3A33"
            + ".748&ctr=0&nrd=1&site=0&mrollp=43&ou=nY%2BsHZ2PrBmdj6wVnY%2BsEZ2PrA2dj6AAmYSgD5SEqA"
            + "%2Bdj6x9nY%2BseQ**&c=1&mav=6.0"
            + ".1&osv=9&ids=MP%253Djakuvol2&ist=0&udid=adedd9ac15d76d1dd9e1f3f001b26d8d&rvrhostname"
            + "=rover.ebay.com&user_name=jakuvol2&ai=2571&rvrsite=0&tz=2"
            + ".0&reqts=1588741714879&rlutype=1&mos=Android&gadid=ff41b8f3-a816-4d74-87b2"
            + "-963e3fc137e9%2C0&mppid=117");
    ubiEvent.setPageName("rover");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 16
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2356359);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:36.541")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("855a5aa0184811c1");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%2853311%3F2%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862bdba"
            + "-0x1e4");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("VIEWDTLS");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(418519563618L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&tz=2.0&dm=HUAWEI&dn=HWPAR&moduledtl=mi%3A4236%7Ciid%3A1%7Cdur%3A1%2Cmi%3A2615"
            + "%7Ciid%3A1%7Cdur%3A1&mos=Android&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9"
            + "&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET"
            + "&mtsts"
            + "=2020-05-06T05%3A08%3A36.541Z&pagename=PulsarGateway&app=2571&res=0x0&parentrq"
            + "=kmC8PxpF%252F9y3&efam=HOMEPAGE&mav=6.0"
            + ".1&c=2&ou=1496174693&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp"
            + "=2481888"
            + "&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&ort=p&p=2356359&t=0&u=1496174693"
            + "&cflgs=AA**&ids=MP%3Djakuvol2&designsystem=6&mrollp=43&gadid=ff41b8f3-a816-4d74-87b2"
            + "-963e3fc137e9%2C0&eactn=VIEWDTLS&androidid=2b330951daa57eeb&rq=855a5aa0184811c1&ciid"
            + "=Yr21cWE*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D855a5aa0184811c1%26node_id%3D967874623fb29de2%26REQUEST_GUID"
            + "%3D171e862bda2.ad4a413.337cd.bd33f959%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31"
            + "%252853311%253F2%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862bdba-0x1e4"
            + "%26cal_mod%3Dfalse&TPool=r1pulsgwy&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=1131"
            + "&ForwardedFor=92.122.154.106; 2.23.97.28;37.48.43"
            + ".216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.173.113"
            + ".97&TStamp=22:08:37.43&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/base/tracking/v1/track_events");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(2);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 17
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.503")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%285226165%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b247"
            + "-0xe7");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSLAYT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521020551778L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&laytcols=rgn%3ARIVER%7Clayt%3ALIST_1_COLUMN%7Cnumcol%3A1&dm=HUAWEI&dn=HWPAR&uc"
            + "=56&mos=Android&bs=197&modulelayt=layt%3ALIST_1_COLUMN%7Cmi%3A2615%7Ciid%3A1%7Crow"
            + "%3A2"
            + "%7Ccol%3A1%7Csize%3AROW%7ClazyLoadId%3Anull%2Clayt%3ALIST_1_COLUMN%7Cmi%3A4236%7Ciid"
            + "%3A1%7Crow%3A1%7Ccol%3A1%7Csize%3AROW%7ClazyLoadId%3Anull&pageci=a2a23ecf-8f57-11ea"
            + "-a1d3-74dbd1808f0a&osv=9&ul=en-US&callingpagename"
            + "=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename"
            + "=ANSWERS_PLATFORM_PAGE&chunkcnt=1&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0"
            + ".1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an"
            + "=eBayAndroid&n"
            + "=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u=1496174693&cflgs=AAE*&chunknum=1"
            + "&eactn=ANSLAYT&rq=kmC8PxpF%2F9y3&ciid=YrI%2BT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D67a3f5ea27d19019%26REQUEST_GUID"
            + "%3D171e862b-23f0-a9e4-f794-ef6bf808423d%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dpiesqn47pse31"
            + "%25285226165%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b247-0xe7%26cal_mod"
            + "%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=1098"
            + "&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43"
            + ".216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79"
            + ".121&TStamp=22:08:34.50&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;"
            + "1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components"
            + "=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,"
            + "FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,"
            + "NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(14, sessionAccumulator.getUbiSession().getTrafficSrcId());

  }

  @Test
  public void test_TrafficSourceMetric2() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e88ee98d1710acc3d3911a0ac0f5d4b2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605052);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:56:52.230")));
    ubiEvent.setRemoteIP("90.186.49.84");
    ubiEvent.setRequestCorrelationId("deeaab0130c48395");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0%3D0%3F326-171e88ee995-0x109");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("90.186.49.84");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPad; CPU OS 12_4_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/12.1.2 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(9);
    ubiEvent.setCurrentImprId(245846305166L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=e88ee98d1710acc3d3911a0ac0f5d4b2&rurl=https%25253A%25252F%25252Fwww"
            + ".ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&h=8d&px"
            + "=4249&chnl=9&uc=77&p=1605052&uaid=e88ee98e1710acc3d3911a0ac0f5d4b1S0&bs=77&catid=80"
            + "&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77®=Hamburg&cflgs=QA**&ul=de-DE&pn=2&rq=deeaab0130c48395"
            + "&pagename"
            + "=http://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=jumUPTk*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Ddeeaab0130c48395%26node_id%3D4b542f8f20dcfdc8%26REQUEST_GUID"
            + "%3D171e88ee-9860-acc3-d392-fdf6de68a87c%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0"
            + "%253D0%253F326-171e88ee995-0x109%26cal_mod%3Dfalse&TPool=r1rover&TDuration=16"
            + "&TStatus"
            + "=0&TType=URL&ContentLength=-1&ForwardedFor=90.186.49"
            + ".84&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.204.61"
            + ".57&TStamp=22:56:52"
            + ".23&TName=roverimp_INTL&Agent=Mozilla/5.0 (iPad; CPU OS 12_4_5 like Mac OS X)"
            + " AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604"
            + ".1&RemoteIP=90.186.49.84&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=reg%3DHamburg%26catid%3D80%26rurl%3Dhttps%25253A%25252F%25252Fwww"
            + ".ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&mpt"
            + "=1588744612147&imp=1605052");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(2, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric3() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("62a512c516e86d87e5e2bbe00111d41d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2051248);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:58.533")));
    ubiEvent.setRemoteIP("10.155.214.148");
    ubiEvent.setRequestCorrelationId("08d3875a397558d3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E15%28twwgsvv%28umj%28bad%7F%29%60jk-171e7fd74ff-0x11b");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(670333629693L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=depechegirl_88&tz=-5.0&lv=1&dn=SM-G930V&ist=0&rlutype=1&tzname=America"
            + "%2FChicago&uc=1&mlocerr=1&mos=Android&bs=0&uaid=e7fd74ff1710a4d129c707299f5ced84S0"
            + "&mlocset=3&memsz=1024.0&osv=8.0.0&ul=en-US&mtsts=2020-05-05T20%3A17%3A58"
            + ".533&apn=3&pagename=Foreground&app=2571&res=1080x1920&c=1&mav=6.0"
            + ".1&g=62a512c516e86d87e5e2bbe00111d41d&h=c5&reqts=1588735079654&cguidsrc=cookie&n"
            + "=62a51dc616e0a9e470c0b7eff117aa97&ort=p&p=2051248&ttp=Page&mnt=WIFI&carrier"
            + "=Verizon&t"
            + "=0&u=799526959&prefl=en_US&cflgs=EA**&ids=MP%253Ddepechegirl_88&designsystem=6"
            + "&mrollp"
            + "=5&gadid=987035ad-5ab0-4ca1-9d37-729c83a08cc8%2C1&mppid=119&androidid"
            + "=fd20dca20e522752"
            + "&pcguid=62a51dc616e0a9e470c0b7eff117aa97&pn=2&rq=08d3875a397558d3&ciid=%2FXT%2FEpw"
            + "*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D08d3875a397558d3%26node_id%3Df828fb8c0d13a616%26REQUEST_GUID"
            + "%3D171e7fd7-4fe0-a4d1-29c7-ba4dcb2a22b1%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E15%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7fd74ff-0x11b%26cal_mod"
            + "%3Dtrue&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=173.173.220.37&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18"
            + ".156&TStamp=20:17:59.67&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.155"
            + ".214"
            + ".148"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&site=0&ai=2571&rvrsite=0&rlutype=1&imp=2051248&lv=udid"
            + "%3D62a512c516e86d87e5e2bbe00111d41d%26ai%3D2571%26mav%3D6.0"
            + ".1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wDmYukCpaLpAWdj6x9nY"
            + "%252BseQ**%26res%3D1080x1920%26memsz%3D1024"
            + ".0%26designsystem%3D6%26mnt%3DWIFI%26prefl%3Den_US%26tzname%3DAmerica%2FChicago"
            + "%26androidid%3Dfd20dca20e522752%26ort%3Dp%26carrier%3DVerizon%26dpi%3D435.42825x431"
            + ".57477%26dn%3DSM-G930V%26apn%3D3%26mrollp%3D5%26c%3D1%26ids%3DMP%253Ddepechegirl_88"
            + "%26osv%3D8.0.0%26ist%3D0%26mlocerr%3D1%26user_name%3Ddepechegirl_88%26reqts"
            + "%3D1588735079654%26tz%3D-5.0%26rlutype%3D1%26mos%3DAndroid%26mlocset%3D3%26gadid"
            + "%3D987035ad-5ab0-4ca1-9d37-729c83a08cc8%2C1%26mppid%3D119%26ttp%3DPage%26mtsts"
            + "%3D2020"
            + "-05-05T20%3A17%3A58.533&udid=62a512c516e86d87e5e2bbe00111d41d&mppid=119");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("799526959");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("62a512c516e86d87e5e2bbe00111d41d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(3112193);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:59.383")));
    ubiEvent.setRemoteIP("173.173.220.37");
    ubiEvent.setRequestCorrelationId("bcNiPWw0CM%2BP");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%2853%3A321%3B%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh"
            + "-171e7fd73d8-0x12b");
    ubiEvent.setEventFamily("MYBWL");
    ubiEvent.setEventAction("ACTN");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(543647298045L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&flgs=AA**&dm=samsung&dn=heroqltevzw&uc=1&nqt=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&mos=Android&bs=0&osv=8.0.0&ul=en-US&callingpagename=txexpsvc__experience_myebay_buying_v1_watching_activity_GET&ec=4&pagename=Watch_DomSvc_GetWatchList&app=2571&res=0x0&uit=1588735078877&efam=MYBWL&mav=6.0.1&g=62a512c516e86d87e5e2bbe00111d41d&h=c5&nativeApp=true&cp=2510300&an=eBayAndroid&n=62a51dc616e0a9e470c0b7eff117aa97&es=0&p=3112193&signals=%7B%22signals%22%3A%5B%7B%22iid%22%3A%22163636275006%22%7D%2C%7B%22iid%22%3A%22352159743760%22%2C%22vid%22%3A%22621597197669%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22123866428185%22%7D%2C%7B%22iid%22%3A%22143204758712%22%7D%2C%7B%22iid%22%3A%22352180312213%22%2C%22vid%22%3A%22621613495298%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22273690588524%22%7D%2C%7B%22iid%22%3A%22132460397482%22%7D%2C%7B%22iid%22%3A%22184025924131%22%7D%2C%7B%22iid%22%3A%22323638738780%22%7D%2C%7B%22iid%22%3A%22163808278271%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22223092986530%22%7D%2C%7B%22iid%22%3A%22382456734896%22%7D%2C%7B%22iid%22%3A%22331702594218%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22231745637587%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22252980415642%22%7D%2C%7B%22iid%22%3A%22401504095582%22%2C%22exc%22%3A%5B4%5D%7D%2C%7B%22iid%22%3A%22133196652307%22%7D%2C%7B%22iid%22%3A%22382585044859%22%2C%22vid%22%3A%22651348972807%22%7D%2C%7B%22iid%22%3A%22382222703064%22%2C%22vid%22%3A%22651054700683%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22184120749661%22%2C%22vid%22%3A%22691831573216%22%7D%2C%7B%22iid%22%3A%22223972459147%22%2C%22vid%22%3A%22522726683975%22%7D%2C%7B%22iid%22%3A%22193200995572%22%7D%2C%7B%22iid%22%3A%22401704527234%22%7D%2C%7B%22iid%22%3A%22153718453223%22%7D%2C%7B%22iid%22%3A%22223510165224%22%7D%2C%7B%22iid%22%3A%22233554254042%22%2C%22vid%22%3A%22533213749912%22%7D%2C%7B%22iid%22%3A%22264393722808%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22164026527043%22%7D%2C%7B%22iid%22%3A%22133048395264%22%2C%22vid%22%3A%22432347397088%22%7D%2C%7B%22iid%22%3A%22382946710862%22%2C%22vid%22%3A%22651605795211%22%7D%2C%7B%22iid%22%3A%22143210403820%22%7D%2C%7B%22iid%22%3A%22123732099379%22%7D%2C%7B%22iid%22%3A%22123732181109%22%2C%22vid%22%3A%22424331640434%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22281410542932%22%7D%2C%7B%22iid%22%3A%22123936871948%22%7D%2C%7B%22iid%22%3A%22254451077996%22%7D%2C%7B%22iid%22%3A%22113716301889%22%2C%22vid%22%3A%22413849149501%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22333512884169%22%7D%2C%7B%22iid%22%3A%22301527392720%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22132916924372%22%7D%2C%7B%22iid%22%3A%22382899100288%22%7D%2C%7B%22iid%22%3A%22273211517195%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22132743370892%22%7D%2C%7B%22iid%22%3A%22153566492091%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22362487078165%22%2C%22vid%22%3A%22631989423938%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22222154118402%22%2C%22vid%22%3A%22520996522530%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22312657912395%22%7D%2C%7B%22iid%22%3A%22381330658858%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22381296362419%22%2C%22inc%22%3A%5B1%5D%2C%22exc%22%3A%5B4%5D%7D%2C%7B%22iid%22%3A%22322831136257%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22183921564667%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22112409340736%22%2C%22vid%22%3A%22412808379887%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22253316061669%22%7D%2C%7B%22iid%22%3A%22324105421303%22%7D%2C%7B%22iid%22%3A%22362884133894%22%7D%2C%7B%22iid%22%3A%22353007936282%22%7D%2C%7B%22iid%22%3A%22283108218747%22%7D%2C%7B%22iid%22%3A%22122709235100%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22283419794526%22%7D%2C%7B%22iid%22%3A%22283419796093%22%7D%2C%7B%22iid%22%3A%22253316437509%22%7D%2C%7B%22iid%22%3A%22132541221419%22%7D%2C%7B%22iid%22%3A%22231786839153%22%7D%2C%7B%22iid%22%3A%22193162851810%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22151330621851%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22324045585804%22%2C%22vid%22%3A%22513091474480%22%7D%2C%7B%22iid%22%3A%22254484661171%22%7D%2C%7B%22iid%22%3A%22253499298397%22%7D%2C%7B%22iid%22%3A%22281443059937%22%2C%22vid%22%3A%22581475712324%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22122138733305%22%2C%22vid%22%3A%22422127456015%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22301020559678%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22254204183689%22%7D%2C%7B%22iid%22%3A%22223671362548%22%7D%2C%7B%22iid%22%3A%22352465414925%22%2C%22vid%22%3A%22621799985078%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22113384515498%22%7D%2C%7B%22iid%22%3A%22291823009709%22%7D%2C%7B%22iid%22%3A%22254427359907%22%2C%22vid%22%3A%22554052198664%22%7D%2C%7B%22iid%22%3A%22303453638903%22%7D%2C%7B%22iid%22%3A%22113657505559%22%7D%2C%7B%22iid%22%3A%22142356646522%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22351765571757%22%2C%22vid%22%3A%22620692136509%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22323813387765%22%7D%2C%7B%22iid%22%3A%22222448837630%22%7D%2C%7B%22iid%22%3A%22152955699061%22%7D%2C%7B%22iid%22%3A%22401175833577%22%2C%22vid%22%3A%22670737051039%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22263163558527%22%2C%22exc%22%3A%5B4%5D%7D%2C%7B%22iid%22%3A%22362329132601%22%2C%22inc%22%3A%5B1%5D%2C%22exc%22%3A%5B3%2C4%2C6%5D%7D%2C%7B%22iid%22%3A%22132633972120%22%2C%22inc%22%3A%5B1%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22323925970321%22%7D%2C%7B%22iid%22%3A%22352468319119%22%2C%22vid%22%3A%22621802273449%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22382234573817%22%2C%22vid%22%3A%22651064024229%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22192897381160%22%7D%2C%7B%22iid%22%3A%22402117733012%22%7D%2C%7B%22iid%22%3A%22163704907752%22%7D%2C%7B%22iid%22%3A%22351009650707%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22223926852432%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22221950837365%22%7D%2C%7B%22iid%22%3A%22264546648878%22%7D%2C%7B%22iid%22%3A%22382863071336%22%2C%22vid%22%3A%22651551345209%22%7D%2C%7B%22iid%22%3A%22162445009332%22%7D%2C%7B%22iid%22%3A%22223495038582%22%7D%2C%7B%22iid%22%3A%22303136842751%22%7D%2C%7B%22iid%22%3A%22324116711053%22%7D%2C%7B%22iid%22%3A%22153878271793%22%2C%22vid%22%3A%22453926865374%22%7D%2C%7B%22iid%22%3A%22191052041868%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22303010391071%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22123394142063%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22254247024814%22%7D%2C%7B%22iid%22%3A%22311666044361%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22122674179876%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22273677635586%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22153321821369%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22372699769689%22%7D%2C%7B%22iid%22%3A%22292504010567%22%7D%2C%7B%22iid%22%3A%22113733192060%22%7D%2C%7B%22iid%22%3A%22301169880753%22%7D%2C%7B%22iid%22%3A%22333137355336%22%7D%2C%7B%22iid%22%3A%22142492390828%22%7D%2C%7B%22iid%22%3A%22193280354493%22%7D%2C%7B%22iid%22%3A%22323528027208%22%7D%2C%7B%22iid%22%3A%22361459844076%22%2C%22vid%22%3A%22630822679084%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22283800877540%22%7D%2C%7B%22iid%22%3A%22323716633368%22%7D%2C%7B%22iid%22%3A%22112392013917%22%2C%22vid%22%3A%22412795795789%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22183978773005%22%7D%2C%7B%22iid%22%3A%22173859398355%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22164014591715%22%7D%2C%7B%22iid%22%3A%22381481076191%22%2C%22vid%22%3A%22650614310307%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22301813509319%22%2C%22vid%22%3A%22600625223308%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22151292747029%22%2C%22vid%22%3A%22450406303421%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22283190010914%22%7D%2C%7B%22iid%22%3A%22222740443555%22%2C%22vid%22%3A%22521597165609%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22254185676927%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22283801364716%22%7D%2C%7B%22iid%22%3A%22352420889097%22%2C%22vid%22%3A%22621770358126%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22323048346276%22%2C%22vid%22%3A%22512252791263%22%7D%2C%7B%22iid%22%3A%22391131861176%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22264586289840%22%7D%2C%7B%22iid%22%3A%22223469151831%22%7D%2C%7B%22iid%22%3A%22391974324014%22%2C%22inc%22%3A%5B1%5D%2C%22exc%22%3A%5B2%2C4%2C6%5D%7D%2C%7B%22iid%22%3A%22281180348699%22%2C%22vid%22%3A%22580232487527%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22311952578732%22%2C%22vid%22%3A%22610748586544%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22323532538164%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22323936852787%22%7D%2C%7B%22iid%22%3A%22352634286498%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22263530441092%22%7D%2C%7B%22iid%22%3A%22283632807817%22%2C%22vid%22%3A%22584944268056%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22313017951156%22%2C%22vid%22%3A%22611709839778%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22111638342390%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22351570046827%22%2C%22vid%22%3A%22620587377535%22%2C%22inc%22%3A%5B1%5D%7D%2C%7B%22iid%22%3A%22264453732275%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22322713666479%22%7D%2C%7B%22iid%22%3A%22222603188726%22%7D%2C%7B%22iid%22%3A%22202426841734%22%7D%2C%7B%22iid%22%3A%22183762707386%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22293409980552%22%7D%2C%7B%22iid%22%3A%22372865917459%22%7D%2C%7B%22iid%22%3A%22383312732688%22%2C%22vid%22%3A%22651796746556%22%7D%2C%7B%22iid%22%3A%22153813825863%22%7D%2C%7B%22iid%22%3A%22323522802163%22%7D%2C%7B%22iid%22%3A%22233499243772%22%7D%2C%7B%22iid%22%3A%22402232518631%22%7D%2C%7B%22iid%22%3A%22223730614285%22%7D%2C%7B%22iid%22%3A%22173987616433%22%7D%2C%7B%22iid%22%3A%22133232061694%22%7D%2C%7B%22iid%22%3A%22312961098550%22%7D%2C%7B%22iid%22%3A%22333518936294%22%2C%22vid%22%3A%22542617864096%22%7D%2C%7B%22iid%22%3A%22133363056017%22%7D%2C%7B%22iid%22%3A%22402214187198%22%7D%2C%7B%22iid%22%3A%22233449898568%22%7D%2C%7B%22iid%22%3A%22383493034753%22%7D%2C%7B%22iid%22%3A%22333306749480%22%2C%22vid%22%3A%22542321164746%22%7D%2C%7B%22iid%22%3A%22333306749332%22%2C%22vid%22%3A%22542321164501%22%7D%2C%7B%22iid%22%3A%22333306769522%22%2C%22vid%22%3A%22542321170274%22%7D%2C%7B%22iid%22%3A%22333306769474%22%2C%22vid%22%3A%22542321170223%22%7D%5D%7D&t=0&u=799526959&cflgs=AA**&eactn=ACTN&rq=bcNiPWw0CM%2BP&ciid=%2FXHnk34*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D50d64663788c9519%26node_id%3D642ab31ea3eb7c18%26REQUEST_GUID"
            + "%3D171e7fd7-3ca0-aad9-37e4-5177f8ff313f%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dpiesqn47pse31"
            + "%252853%253A321%253B%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e7fd73d8-0x12b"
            + "%26cal_mod%3Dfalse&TPool=r1watch&TDuration=2&TStatus=0&TType=URL&ContentLength=8034"
            + "&ForwardedFor=69.192.7.158;173.173.220"
            + ".37&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.173.147"
            + ".126&TStamp=20:17:59.38&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;"
            + "Verizon;1080x1920;3.0&RemoteIP=173.173.220.37&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/myebay_buying/v1/watching_activity?items_per_page=300");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("799526959");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("62a512c516e86d87e5e2bbe00111d41d");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(3112193);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:59.503")));
    ubiEvent.setRemoteIP("173.173.220.37");
    ubiEvent.setRequestCorrelationId("bcNiPWw0CM%2BP");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dosusqn47pse31%2850%3B0766%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e7fd7450"
            + "-0x1b1b");
    ubiEvent.setEventFamily("MYBWL");
    ubiEvent.setEventAction("ACTN");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(166125662717L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&flgs=AA**&dm=samsung&dn=heroqltevzw&uc=1&nqt=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&mos=Android&bs=0&osv=8.0.0&ul=en-US&callingpagename=txexpsvc__experience_myebay_buying_v1_watching_activity_GET&ec=4&pagename=txexpsvc__experience_myebay_buying_v1_watching_activity_GET&app=2571&res=0x0&uit=1588735078877&efam=MYBWL&mav=6.0.1&g=62a512c516e86d87e5e2bbe00111d41d&h=c5&nativeApp=true&cp=2510300&an=eBayAndroid&n=62a51dc616e0a9e470c0b7eff117aa97&es=0&p=2510300&t=0&u=799526959&cflgs=AA**&guest=false&eactn=ACTN&rq=bcNiPWw0CM%2BP&ciid=%2FXHcrSY*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dcc88986190e14577%26node_id%3D7a9c66d151b7dc26%26REQUEST_GUID"
            + "%3D171e7fd7-4460-a9ca-d264-1df7fcf1afe7%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%252850%253B0766%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e7fd7450-0x1b1b"
            + "%26cal_mod"
            + "%3Dfalse&TPool=r1txexpsvc&TDuration=1&TStatus=0&TType=URL&ContentLength=1447"
            + "&ForwardedFor=69.192.7.158;173.173.220"
            + ".37&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.156.173"
            + ".38&TStamp=20:17:59.50&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;"
            + "Verizon;1080x1920;3.0&RemoteIP=173.173.220.37&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/myebay_buying/v1/watching_activity?items_per_page=300");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("799526959");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("62a512c516e86d87e5e2bbe00111d41d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2530290);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:59.734")));
    ubiEvent.setRemoteIP("173.173.220.37");
    ubiEvent.setRequestCorrelationId("619c2626bfc5b2c6");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dosusqn47pse31%285000735%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e7fd7537"
            + "-0x1d8");
    ubiEvent.setEventFamily("MFE");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(347827959293L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&flgs=AA**&dm=samsung&dn=heroqltevzw&uc=1&nqt=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&mos=Android&bs=0&osv=8.0.0&plmt=xAAAAB%252BLCAAAAAAAAAA1jssKgzAQRf9l1ilMkknC5FeKixC1CFqlLwriv%252FeqdHXgwJy5Ky03yk7Ua2JDy0D5utLQUrbMmsRQhbqooQdIoZfOhsi%252BVRVhKSmGGDn2vk9SfCBD3QsJ54JlZ3Y4aSDL2UXj%252Fh5HQ2WvqSqzxU35%252FP0Ejx3T98BcD5TniRmrNtQqXjgAK3lrth%252BDtxwqxAAAAA%253D%253D&ul=en-US&callingpagename=cartexsvc__DefaultPage&!xt=225102%2C225124&ec=4&pagename=reco__experience_merchandising_v1_module_provider_GET&app=2571&res=0x0&uit=1588735079646&efam=MFE&mav=6.0.1&g=62a512c516e86d87e5e2bbe00111d41d&rpg=2493970&h=c5&nativeApp=true&cp=2493970&an=eBayAndroid&n=62a51dc616e0a9e470c0b7eff117aa97&es=0&p=2530290&t=0&u=799526959&cflgs=AA**&eactn=EXPM&rq=619c2626bfc5b2c6&po=%5B%28pg%3A2493970+pid%3A100974%29%5D&ciid=%2FXUp%2FFA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Daa7060acd16fc820%26node_id%3D7b7349a6df2564d5%26REQUEST_GUID"
            + "%3D171e7fd7-5320-aa6f-c506-0cc4fbcc5498%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%25285000735%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e7fd7537-0x1d8%26cal_mod"
            + "%3Dfalse&TPool=r1reco&TDuration=2&TStatus=0&TType=URL&ContentLength=1468"
            + "&ForwardedFor"
            + "=23.52.0.101; 69.192.7.158;173.173.220"
            + ".37&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.166.252"
            + ".80&TStamp=20:17:59.73&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;"
            + "Verizon;1080x1920;3.0&RemoteIP=173.173.220.37&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping_cart/v1/cart?mode=lite");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("799526959");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("62a512c516e86d87e5e2bbe00111d41d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2493970);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:59.756")));
    ubiEvent.setRemoteIP("173.173.220.37");
    ubiEvent.setRequestCorrelationId("619c2626bfc5b2c6");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285%3F45164-171e7fd754d-0x214");
    ubiEvent.setEventFamily("CART");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(775994373373L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AEQ*");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&flgs=AEQ*&cartic=0&dm=samsung&dn=heroqltevzw&mcc=false&uic=0&uc=1&!xe=23243&tic=0&nqt=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&mos=Android&bs=0&osv=8.0.0&nmq=0&ul=en-US&callingpagename=cartexsvc__DefaultPage&!xt=59961&ec=4&pagename=cartexsvc__DefaultPage&app=2571&res=0x0&uit=1588735079646&efam=CART&mav=6.0.1&crtsz=0&ebc=5040102108&g=62a512c516e86d87e5e2bbe00111d41d&rpg=2493970&h=c5&num_itm_unavbl=0&prof=ANDROID&nativeApp=true&cp=2493970&an=eBayAndroid&n=62a51dc616e0a9e470c0b7eff117aa97&es=0&p=2493970&t=0&u=799526959&cflgs=AA**&cmd=DefaultPage&xt=59961&issmeoic=false&eactn=EXPC&bic=0&rq=619c2626bfc5b2c6&ciid=%2FXTerLQ*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D20385fb681f27818%26node_id%3Dabad130d4606960c%26REQUEST_GUID"
            + "%3D171e7fd7-5420-a12a-cb42-395efe7ca11f%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%25285%253F45164-171e7fd754d-0x214%26cal_mod%3Dfalse&TPool=r1cartexsvc&TDuration=2"
            + "&TStatus=0&TType=URL&ContentLength=1666&ForwardedFor=23.52.0.101; 69.192.7.158;173"
            + ".173"
            + ".220.37&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.18"
            + ".172"
            + ".180&TStamp=20:17:59.75&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;"
            + "Verizon;1080x1920;3.0&RemoteIP=173.173.220.37&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping_cart/v1/cart?mode=lite");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("799526959");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(14, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric4() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2349624);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:36:08.771")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("f8b90dd54c8e8c9d");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%28530262%3A%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e87bf044"
            + "-0xd4");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(984733511291L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYKEAAAAAAAIAcQEEABAAgIATgAAAAAIwAAABAAAiAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "c=1&g=e88ee98d1710acc3d3911a0ac0f5d4b2&rurl=https%25253A%25252F%25252Fwww"
            + ".ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&h=8d&px"
            + "=4249&chnl=9&uc=77&p=1605052&uaid=e88ee98e1710acc3d3911a0ac0f5d4b1S0&bs=77&catid=80"
            + "&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77®=Hamburg&cflgs=QA**&ul=de-DE&pn=2&rq=deeaab0130c48395"
            + "&pagename"
            + "=http://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=jumUPTk*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Ddeeaab0130c48395%26node_id%3D4b542f8f20dcfdc8%26REQUEST_GUID"
            + "%3D171e88ee-9860-acc3-d392-fdf6de68a87c%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0"
            + "%253D0%253F326-171e88ee995-0x109%26cal_mod%3Dfalse&TPool=r1rover&TDuration=16"
            + "&TStatus"
            + "=0&TType=URL&ContentLength=-1&ForwardedFor=90.186.49"
            + ".84&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.204.61"
            + ".57&TStamp=22:56:52"
            + ".23&TName=roverimp_INTL&Agent=Mozilla/5.0 (iPad; CPU OS 12_4_5 like Mac OS X)"
            + " AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604"
            + ".1&RemoteIP=90.186.49.84&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=reg%3DHamburg%26catid%3D80%26rurl%3Dhttps%25253A%25252F%25252Fwww"
            + ".ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&mpt"
            + "=1588744612147&imp=1605052");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(174034506016L);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2349624);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:56:42.386")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("f592057f6d65f27b");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dosusqn47pse31%285005436%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e857d493"
            + "-0x1df");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(204098687831L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYOEAAAAAAAICYQAEABAAgAADgAAAAAAwAAABAAAiAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc"
            +
            "=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYOEAAAAAAAICYQAEABAAgAADgAAAAAAwAAABAAAiAAAAAAAAUA**&ssc=23830334018&gsp=0&!wtballqs=1041-null|939--0.003797867009310254&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=218&!_viwtbranks=1|2&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=6&vibisdm=770X1000&app=2571&!_OBFS_SELLERID=1364404048&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=75508&plcampt=0%3A10855064018%2C&!_callingpageid=2349624&swccount=1&meta=888&slr=1364404048&curprice=3.83&viPageType=0&attrct=6&!pymntMethods=PPL|MSC|VSA|MAE|AMX&rq=f592057f6d65f27b&l1=1492&l2=179961&qtys=20&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=173884637945&binamt=3.83&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=770X1000&sn=rich-five&qtya=22&st=9&uit=1588741002042&mav=6.0.1&pudo=0&to_zip=pe133rh&vibisbm=1000&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=7300&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=97&t=3&u=1151771663&nofp=6&rpdur=14&tr=1716419&dc=3&!vidsig=DEFAULT|DEFAULT&nozp=6&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=173884637945&promol=1&vpcg=false&epidonvi=22027473982&iver=3469975721007&!_OBFS_BUYERID=1151771663&es=3&itmtitle=5Pcs%2FLot+Fishing+Lures+Kinds+Of+Minnow+Fish+Bass+Tackle+Hooks+Baits+Crankbait+RF&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=1041|939&shipsiteid=0&obfs_listing_is_eligible=true&nw=10&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=USD%3A1.99%5E-1%5E-1%5E2020-05-21T03%3A00%3A00-07%3A00%5E2020-07-01T03%3A00%3A00-07%3A00%5EECONOMY%5EOtherInternational%5E0%5E-1%5E3%5E-1%5EUK%5Epe133rh%5E173884637945%5E-1%5E10%5E38&qtymod=true&!_wtbqs=1041|939&addOnTypes=SUPPORT%2CGENERIC%2CFREIGHT&fimbsa=500&vidsigct=3&cp_usd=4.76&!_viwtbids=1041|939&swcembg=true&xt=226616&!_OBFS_EMAIL_COUNT=0&ciid=V9M6hS8*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D9036bfb29f48b653%26node_id%3D01348554cc698a21%26REQUEST_GUID"
            + "%3D171e857d-48d0-a688-52f4-2466feaa9c80%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%25285005436%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e857d493-0x1df%26cal_mod"
            + "%3Dfalse&TPool=r1viexpsvc7&TDuration=3&TStatus=0&TType=URL&ContentLength=3359"
            + "&ForwardedFor=23.209.73.30;80.0.153"
            + ".110&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.104.133"
            + ".47&TStamp=21:56:42.38&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;"
            + "1080x2145;"
            + "3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=173884637945&modules=VLS"
            + "&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,"
            + "PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,"
            + "VEHICLE_HISTORY,"
            + "VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,"
            + "VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,"
            + "PRP_PRODUCT_DETAILS,PRP_LISTINGS");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(173884637945L);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(3084);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:56:42.054")));
    ubiEvent.setRemoteIP("10.174.141.15");
    ubiEvent.setRequestCorrelationId("4f586ab346e38b03");
    ubiEvent.setSid("e11404.m2780.l2648");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ckuvthu%60t*0220%3A%3D4%29pqtfwpu%29osu%29fgg%7E-fij-171e857d348-0x1a7");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(431380484951L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "segname=11404&osub=-1%257E1&nid=&uc=1&uaid=e857d3471710aa4706418404c10e362bS0&bs=0&bu"
            + "=44201623906&crd=20200505133947&ul=en-US&pagename=EntryTracking&app=2571&c=1&ch"
            + "=osgood"
            + "&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&cguidsrc=cookie&n"
            + "=07b876771700a4cc0287d4cfe1dd5ad1&url=https%253A%252F%252Fwww.ebay.co"
            + ".uk%252Fulk%252Fi%252F173884637945%253F_trksid%253Dp11404.c100142"
            + ".m2780%2526_trkparms%253Daid%25253D1110001%252526algo%25253DSPLICE"
            + ".SIM%252526ao%25253D1%252526asc%25253D20161019143441%252526meid"
            + "%25253Dab56af7b63c74e43aaf025b3b2e9ed25%252526pid%25253D100142%252526rk%25253D2"
            + "%252526rkt%25253D4%252526mehot%25253Dnone%252526b%25253D1%252526sd"
            + "%25253D254338294634"
            + "%252526itm%25253D173884637945%252526pmt%25253D1%252526noa%25253D1%252526pg"
            + "%25253D11404"
            + "&p=3084&t=3&cflgs=EA**&mppid=117&pn=2&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&rq"
            + "=4f586ab346e38b03&euid=c9e1c53be22a417e9f9b575e9f6294c8&chnl=7&ciid=V9NHcGQ*&sid"
            + "=e11404.m2780.l2648");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4f586ab346e38b03%26node_id%3D7aa1f2221ae6df8f%26REQUEST_GUID"
            + "%3D171e857d-3460-aa47-0641-6ab3de75b915%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t"
            + "%2A0220"
            + "%253A%253D4%2529pqtfwpu%2529osu%2529fgg%257E-fij-171e857d348-0x1a7%26cal_mod%3Dfalse"
            + "&TPool=r1rover&TDuration=8&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=80.0"
            + ".153"
            + ".110&Script=/rover/0/e11404.m2780.l2648/7&Server=localhost&TMachine=10.164.112"
            + ".100&TStamp=21:56:42.05&TName=rover&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.174.141"
            + ".15"));
    ubiEvent.setUrlQueryString(
        "/rover/0/e11404.m2780.l2648/7?memsz=2048"
            + ".0&res=1080x2145&designsystem=6&mnt=WIFI&prefl=en_LT&segname=11404&tzname=Europe"
            + "%2FLondon&bu=44201623906&androidid=3e98b7f5fbf8de8f&crd=20200505133947&srcrot=e11404"
            + ".m2780.l2648&osub=-1%257E1&ort=p&carrier=O2+-+UK&rvr_ts"
            + "=e857cc9d1710aaecf3611a27ffcc6682&dn=LYA-L09&dpi=537.882x539"
            + ".102&mtsts=2020-05-05T21%3A56%3A41"
            + ".546&euid=c9e1c53be22a417e9f9b575e9f6294c8&ctr=0&nrd=1&site=3&mrollp=81&ou=nY"
            + "%2BsHZ2PrBmdj6wVnY%2BsEZ2PrA2dj6AFlYOmD5GEpw%2Bdj6x9nY%2BseQ**&c=26&mav=6.0"
            + ".1&osv=9&ids=MP%253Dgedmas1991&loc=https%253A%252F%252Fwww.ebay.co"
            + ".uk%252Fulk%252Fi%252F173884637945%253F_trksid%253Dp11404.c100142"
            + ".m2780%2526_trkparms%253Daid%25253D1110001%252526algo%25253DSPLICE"
            + ".SIM%252526ao%25253D1%252526asc%25253D20161019143441%252526meid"
            + "%25253Dab56af7b63c74e43aaf025b3b2e9ed25%252526pid%25253D100142%252526rk%25253D2"
            + "%252526rkt%25253D4%252526mehot%25253Dnone%252526b%25253D1%252526sd"
            + "%25253D254338294634"
            + "%252526itm%25253D173884637945%252526pmt%25253D1%252526noa%25253D1%252526pg"
            + "%25253D11404"
            + "&ist=0&udid=ad402a4c168acc2d4dd1bb50015645bd&rvrhostname=rover.ebay"
            + ".com&user_name=gedmas1991&ai=2571&rvrsite=0&tz=1"
            + ".0&reqts=1588741002030&rlutype=1&mos=Android&rvr_id=0&gadid=0e1f6958-d08e-4569-a58b"
            + "-fac4b0aa670c%2C1&sojTags=bu%3Dbu%2Cch%3Dch%2Csegname%3Dsegname%2Ccrd%3Dcrd%2Curl"
            + "%3Dloc%2Cosub%3Dosub&ch=osgood&mppid=117");
    ubiEvent.setPageName("rover");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2349624);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:57:39.589")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("d80903500813dcd4");
    ubiEvent.setSid("p2061037.m2983");
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dosusqn47pse31%28504421%3A%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e858b406"
            + "-0xec");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(273787040088L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYOEAAAAAAAIAYQAEABAAgAADgAAAAAAwAAABAAACAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc"
            +
            "=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYOEAAAAAAAIAYQAEABAAgAADgAAAAAAwAAABAAACAAAAAAAAUA**&ssc=1%7C952523219&gsp=0&!wtballqs=1041-null|938-0.002196675875661365|1007--0.0013971137445672119|932--0.005935865798236718|939--0.007558882983979158&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=210&!_viwtbranks=1|3&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&!_wtbsh=938&picsig=938&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=13&vibisdm=1000X1000&app=2571&!_OBFS_SELLERID=1126282819&var=610799513818&mskutraitct=1&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=319539&plcampt=0%3A11314177019%2C&!_callingpageid=2349624&swccount=1&meta=888&slr=1126282819&curprice=9.81&viPageType=0&attrct=5&!pymntMethods=PPL|MSC|VSA|MAE|AMX&rq=d80903500813dcd4&l1=1492&l2=179961&qtys=28&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=312056955535&binamt=9.81&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=1000X1000&sn=a_coming868&qtya=36&st=9&uit=1588741059066&mav=6.0.1&pudo=0&to_zip=pe133rh&vibisbm=1000&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=7300&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=97&t=3&u=1151771663&nofp=13&rpdur=30&tr=2170361&dc=3&!vidsig=DEFAULT|DEFAULT|RETURNS%2CSELLER_ACCEPT_RETURNS_SIGNAL%2C30|DEFAULT&nozp=13&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=312056955535&promol=1&vpcg=false&iver=2083152502021&!_OBFS_BUYERID=1151771663&es=3&itmtitle=10pcs+Fishing+Lures+Crankbaits+Hooks+Minnow+Frog+Baits+Tackle+Crank+Set&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=1041|938|1007|932|939&shipsiteid=0&obfs_listing_is_eligible=true&nw=0&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&viwtburgency=938&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=USD%3A0.5%5E-1%5E-1%5E2020-05-20T03%3A00%3A00-07%3A00%5E2020-06-30T03%3A00%3A00-07%3A00%5EECONOMY%5EOtherInternational%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E312056955535%5E-1%5E9%5E37&qtymod=true&!_wtbqs=1041|938|1007&addOnTypes=SUPPORT%2CGENERIC%2CFREIGHT&fimbsa=500&vidsigct=3&cp_usd=8.39&!_viwtbids=1041|1007&swcembg=true&xt=226616&!_OBFS_EMAIL_COUNT=0&ciid=WLH6vj8*&sid=p2061037.m2983");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0e892f4d4896a82b%26node_id%3Ddce2522cfcfafdb6%26REQUEST_GUID"
            + "%3D171e858b-3ff0-ad4b-e3f6-97d6fe82f756%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%2528504421%253A%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e858b406-0xec%26cal_mod"
            + "%3Dfalse&TPool=r1viexpsvc7&TDuration=2&TStatus=0&TType=URL&ContentLength=3602"
            + "&ForwardedFor=95.101.129.159;80.0.153.110; 23.209.73"
            + ".30&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.212.190"
            + ".63&TStamp=21:57:39.58&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;"
            + "1080x2145;"
            + "3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=312056955535&modules=VLS"
            + "&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,"
            + "PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,"
            + "VEHICLE_HISTORY,"
            + "VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,"
            + "VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,"
            + "PRP_PRODUCT_DETAILS,PRP_LISTINGS");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(312056955535L);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2349624);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:58:08.616")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("ee7ead005b5241b3");
    ubiEvent.setSid("p2061037.m2983");
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dosusqn47pse31%28507426%3A%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e8592569"
            + "-0x1dd");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(1026794922841L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYOEAAAAAAAIAYQAEABAAgAADgAAAAAAwAAABAAAiAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc"
            +
            "=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYOEAAAAAAAIAYQAEABAAgAADgAAAAAAwAAABAAAiAAAAAAAAUA**&ssc=23830334018&gsp=0&!wtballqs=1041-null|938-0.01277635504668143|932-0.0066229853921415845|939--0.0023557580435975695&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=205&!_viwtbranks=1|3&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&!_wtbsh=938&picsig=938&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=12&vibisdm=1100X1100&app=2571&!_OBFS_SELLERID=1364404048&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=75508&plcampt=0%3A11649003018%2C&!_callingpageid=2349624&swccount=1&meta=888&slr=1364404048&curprice=4.02&viPageType=0&attrct=5&!pymntMethods=PPL|MSC|VSA|MAE|AMX|DSC&rq=ee7ead005b5241b3&l1=1492&l2=179961&qtys=476&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=173123598536&binamt=4.02&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=1100X1100&sn=rich-five&qtya=33&st=9&uit=1588741088191&mav=6.0.1&pudo=0&to_zip=pe133rh&vibisbm=1000&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=7300&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=97&t=3&u=1151771663&nofp=12&rpdur=14&tr=1812293&dc=3&!vidsig=DEFAULT|DEFAULT|DEFAULT&nozp=12&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=173123598536&promol=1&vpcg=false&epidonvi=523127437&iver=3483274213007&!_OBFS_BUYERID=1151771663&es=3&itmtitle=5PCS+Large+Frog+Topwater+Soft+Fishing+Lure+Crankbait+Hooks+Bass+Bait+Tackle+RF&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=1041|938|932|939&shipsiteid=0&obfs_listing_is_eligible=true&nw=68&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&viwtburgency=938&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=USD%3A1.99%5E-1%5E-1%5E2020-05-21T03%3A00%3A00-07%3A00%5E2020-07-01T03%3A00%3A00-07%3A00%5EECONOMY%5EOtherInternational%5E0%5E-1%5E3%5E-1%5EUK%5Epe133rh%5E173123598536%5E-1%5E10%5E38&qtymod=true&!_wtbqs=1041|938|932&addOnTypes=SUPPORT%2CGENERIC%2CFREIGHT&fimbsa=500&vidsigct=3&cp_usd=4.99&!_viwtbids=1041|932&swcembg=true&xt=226616&!_OBFS_EMAIL_COUNT=0&ciid=WSO%2FEe8*&sid=p2061037.m2983");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De1c588c2c0730b08%26node_id%3Dda4feaa6e16107ec%26REQUEST_GUID"
            + "%3D171e8592-5640-aa41-1ef4-da74feb2005e%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%2528507426%253A%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e8592569-0x1dd"
            + "%26cal_mod"
            + "%3Dfalse&TPool=r1viexpsvc7&TDuration=3&TStatus=0&TType=URL&ContentLength=3523"
            + "&ForwardedFor=80.0.153.110; 23.209.73.30;92.123.78"
            + ".28&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.164.17"
            + ".239&TStamp=21:58:08.61&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;"
            + "1080x2145;"
            + "3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=173123598536&modules=VLS"
            + "&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,"
            + "PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,"
            + "VEHICLE_HISTORY,"
            + "VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,"
            + "VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,"
            + "PRP_PRODUCT_DETAILS,PRP_LISTINGS");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(173123598536L);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 6
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2349624);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:37:48.173")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("7ffbd529b0fb48ad");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dosusqn47pse31%285012324%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e87d748e"
            + "-0xf2");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(780264895101L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYKEAAAAAAAIAcQAEABAAgIATgAAAAAAwAAABgAAiAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc"
            +
            "=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYKEAAAAAAAIAcQAEABAAgIATgAAAAAAwAAABgAAiAAAAAAAAUA**&ssc=1&gsp=0&!sh2srv=-1%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EONE_DAY%5EUK_DPDNextDay%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E222989488054%5E-1%5E2%5E4&ppcPromotionType=BillMeLaterPromotionalOffer&!wtballqs=1041-null|930-0.013080889791487364|932--0.012322682181600698|938--0.013310982207928305|1007--0.015445393226008552|939--0.019989737910013024|928--0.024469165233424718&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=241&!_viwtbranks=1|2&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&picsig=1049%7C1046&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=12&vibisdm=1600X1600&app=2571&!_OBFS_SELLERID=123139015&bdrs=0&an=eBayAndroid&swctrs=true&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=14930&vpcgpcui=2&!_callingpageid=2349624&swccount=2&meta=11700&slr=123139015&curprice=97.99&viPageType=0&attrct=17&!pymntMethods=PPL|MSC|VSA|MAE|AMX&rq=7ffbd529b0fb48ad&l1=631&l2=3244&qtys=795&vidp=true&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=222989488054&vict=android&binamt=97.99&obfs_desc_has_contactInfo=false&vibisb=1600&vibisd=1600X1600&sn=scottdirect1&qtya=2&st=9&uit=1588743467625&mav=6.0.1&pudo=1&to_zip=pe133rh&vibisbm=1600&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=23782&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=100&t=3&u=1151771663&nofp=12&rpdur=30&tr=1504178&dc=3&!vidsig=DEFAULT|DELIVERY%2CQUALIFIES_BOPIS_SIGNAL|DEFAULT|DEFAULT&viFinancingUKModule=true&nozp=12&vioid=5767268105&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=222989488054&promol=0&vpcgpc=4&!sh3srv=-1%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EONE_DAY%5EUK_DPDNextDay%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E222989488054%5E-1%5E2%5E4&vpcg=true&epidonvi=22031984125&iver=2812187846012&!_OBFS_BUYERID=1151771663&es=3&itmtitle=Makita+P-90532+General+Maintenance+227PC+Tool+Kit&viFinancingModule=true&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=1041|930|932|938|1007|939|928&!sh4srv=-1%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EONE_DAY%5EUK_DPDNextDay%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E222989488054%5E-1%5E2%5E4&shipsiteid=3&obfs_listing_is_eligible=true&nw=3312&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&viwtburgency=1046&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=GBP%3A0.0%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EONE_DAY%5EUK_DPDNextDay%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E222989488054%5E-1%5E2%5E4&!_wtbqs=1041|930|932&addOnTypes=WARRANTY&fimbsa=500&viot=14&vidsigct=3&cp_usd=121.71338&!_viwtbids=1041|930&swcembg=true&xt=226616&viof=1&!_OBFS_EMAIL_COUNT=0&ciid=fXJpq7U*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D07bc26766d43df17%26node_id%3Dd67553cb35d6706e%26REQUEST_GUID"
            + "%3D171e87d7-4880-aada-bb57-6ff3febfa119%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%25285012324%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e87d748e-0xf2%26cal_mod"
            + "%3Dfalse&TPool=r1viexpsvc7&TDuration=3&TStatus=0&TType=URL&ContentLength=4270"
            + "&ForwardedFor=23.209.73.30;80.0.153"
            + ".110&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.173.171"
            + ".181&TStamp=22:37:48.17&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;"
            + "1080x2145;"
            + "3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=222989488054&modules=VLS"
            + "&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,"
            + "PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,"
            + "VEHICLE_HISTORY,"
            + "VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,"
            + "VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,"
            + "PRP_PRODUCT_DETAILS,PRP_LISTINGS");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(222989488054L);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 7
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2349624);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:39:22.938")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("224ba7cda504a72d");
    ubiEvent.setSid("p2061037.m2983");
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%287316466%2Busqdrrp%2Btil%2Bceb%7C%28dlh-171e87ee6ba"
            + "-0x12f");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(54205146238L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYKEAAAAAAAIAcQEEABAAgAADgAAAAAAwAAABgAACAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc"
            +
            "=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYKEAAAAAAAIAcQEEABAAgAADgAAAAAAwAAABgAACAAAAAAAAUA**&ssc=1&gsp=0&!sh2srv=-1%5E-1%5E-1%5E2020-05-14T03%3A00%3A00-07%3A00%5E2020-05-19T03%3A00%3A00-07%3A00%5EECONOMY%5EUK_OtherCourier%5E0%5E-1%5E2%5E-1%5EUK%5Epe133rh%5E223975082419%5E-1%5E5%5E8&ppcPromotionType=Installment&!wtballqs=930--0.013574376569852078|932--0.021772023985621617|1007--0.022523457056626006&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=240&!_viwtbranks=1|2&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&picsig=1049%7C764%7C1046&ppc_ofid=6342793715&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=7&vibisdm=1344X1477&app=2571&!_OBFS_SELLERID=173914835&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=224&!_callingpageid=2349624&swccount=1&meta=11700&slr=173914835&curprice=110.92&viPageType=0&attrct=10&!pymntMethods=PPL|MSC|VSA|MAE|AMX&rq=224ba7cda504a72d&l1=631&l2=3244&qtys=2&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=223975082419&binamt=110.92&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=1344X1477&sn=caringquality&qtya=2&st=9&uit=1588743562464&mav=6.0.1&pudo=1&to_zip=pe133rh&vibisbm=1000&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=23782&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=99&t=3&u=1151771663&nofp=7&rpdur=30&tr=542395&dc=3&!vidsig=DELIVERY%2CQUALIFIES_BOPIS_SIGNAL|DEFAULT|DEFAULT|RETURNS%2CSELLER_ACCEPT_RETURNS_SIGNAL%2C30|DEFAULT&viFinancingUKModule=true&nozp=7&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=223975082419&promol=0&!sh3srv=-1%5E-1%5E-1%5E2020-05-14T03%3A00%3A00-07%3A00%5E2020-05-19T03%3A00%3A00-07%3A00%5EECONOMY%5EUK_OtherCourier%5E0%5E-1%5E2%5E-1%5EUK%5Epe133rh%5E223975082419%5E-1%5E5%5E8&vpcg=false&iver=2811307934012&!_OBFS_BUYERID=1151771663&es=3&viFinancingModule=true&itmtitle=Professional+Mechanics+Home+Tool+Kit+Set+102pc+Maintenance+Hand+Tools+with+Case&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=930|932|1007&!sh4srv=-1%5E-1%5E-1%5E2020-05-14T03%3A00%3A00-07%3A00%5E2020-05-19T03%3A00%3A00-07%3A00%5EECONOMY%5EUK_OtherCourier%5E0%5E-1%5E2%5E-1%5EUK%5Epe133rh%5E223975082419%5E-1%5E5%5E8&shipsiteid=3&obfs_listing_is_eligible=true&nw=35&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&viwtburgency=764&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=GBP%3A0.0%5E-1%5E-1%5E2020-05-14T03%3A00%3A00-07%3A00%5E2020-05-19T03%3A00%3A00-07%3A00%5EECONOMY%5EUK_OtherCourier%5E0%5E-1%5E2%5E-1%5EUK%5Epe133rh%5E223975082419%5E-1%5E5%5E8&qtymod=true&!_wtbqs=930|932|1007&addOnTypes=WARRANTY&fimbsa=500&vidsigct=3&cp_usd=137.77373&!_viwtbids=930|932&swcembg=true&!_OBFS_EMAIL_COUNT=0&ciid=fuTgngw*&sid=p2061037.m2983");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4c73035a46db4550%26node_id%3D91c37dfd957ab383%26REQUEST_GUID"
            + "%3D171e87ee-6b80-ad39-e0c3-0b00ff549b4b%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%25287316466%252Busqdrrp%252Btil%252Bceb%257C%2528dlh-171e87ee6ba-0x12f%26cal_mod"
            + "%3Dfalse&TPool=r1viexpsvc7&TDuration=2&TStatus=0&TType=URL&ContentLength=4115"
            + "&ForwardedFor=80.0.153.110;92.123.78.44; 23.209.73"
            + ".30&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.211.158"
            + ".12&TStamp=22:39:22.93&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;"
            + "1080x2145;"
            + "3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=223975082419&modules=VLS"
            + "&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,"
            + "PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,"
            + "VEHICLE_HISTORY,"
            + "VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,"
            + "VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,"
            + "PRP_PRODUCT_DETAILS,PRP_LISTINGS");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(223975082419L);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 8
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2048309);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:40:03.345")));
    ubiEvent.setRemoteIP("10.202.7.45");
    ubiEvent.setRequestCorrelationId("887634bf8cac3a4f");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E61%28twwgsvv%28umj%28bad%7F%29%60jk-171e8803ee7-0x121");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(734756552320L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=gedmas1991&tz=1.0&imgscount=1&lv=1&dn=LYA-L09&ist=0&rlutype=1&tzname=Europe"
            + "%2FLondon&uc=1&mos=Android&bs=0&uaid=e8803ee71710a4d12ab3b31e9eff15d6S0&memsz=2048"
            + ".0&osv=9&ul=en-US&mtsts=2020-05-05T22%3A40%3A03"
            + ".345&pagename=EnterSearch&app=2571&res=1080x2145&c=11&mav=6.0"
            + ".1&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&reqts=1588743651008&cguidsrc=cookie&n"
            + "=07b876771700a4cc0287d4cfe1dd5ad1&ort=p&p=2048309&ttp=Page&mnt=WIFI&carrier=O2"
            + "+-+UK&t"
            + "=3&u=1151771663&prefl=en_LT&cflgs=EA**&ids=MP%253Dgedmas1991&designsystem=6&mrollp"
            + "=81"
            + "&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&mppid=117&androidid=3e98b7f5fbf8de8f"
            + "&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&pn=2&rq=887634bf8cac3a4f&ciid=gD7nEqs*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D887634bf8cac3a4f%26node_id%3Df896a7b78caaedea%26REQUEST_GUID"
            + "%3D171e8803-ee60-a4d1-2ab0-7de2cafadc2f%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E61%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e8803ee7-0x121%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=80.0.153.110&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18"
            + ".171&TStamp=22:40:51.04&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.202.7"
            + ".45"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&site=3&ai=2571&rvrsite=0&rlutype=1&imp=2048309&lv=udid"
            + "%3Dad402a4c168acc2d4dd1bb50015645bd%26ai%3D2571%26mav%3D6.0"
            + ".1%26site%3D3%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AFlYOmD5GEpw%252Bdj6x9nY"
            + "%252BseQ**%26memsz%3D2048.0%26res%3D1080x2145%26mrollp%3D81%26designsystem%3D6%26c"
            + "%3D11%26osv%3D9%26ids%3DMP%253Dgedmas1991%26mnt%3DWIFI%26ist%3D0%26prefl%3Den_LT"
            + "%26imgscount%3D1%26tzname%3DEurope%2FLondon%26user_name%3Dgedmas1991%26androidid"
            + "%3D3e98b7f5fbf8de8f%26reqts%3D1588743651008%26tz%3D1"
            + ".0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DO2+-+UK%26gadid%3D0e1f6958"
            + "-d08e"
            + "-4569-a58b-fac4b0aa670c%2C1%26dn%3DLYA-L09%26dpi%3D537.882x539"
            + ".102%26mppid%3D117%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A40%3A03"
            + ".345&udid=ad402a4c168acc2d4dd1bb50015645bd&mppid=117");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(11);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 9
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2052300);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:39:30.147")));
    ubiEvent.setRemoteIP("10.202.7.45");
    ubiEvent.setRequestCorrelationId("887634bf8cac3a4f");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E61%28twwgsvv%28umj%28bad%7F%29%60jk-171e8803ee5-0x113");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(734756355712L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=gedmas1991&tz=1.0&lv=1&dn=LYA-L09&ist=0&rlutype=1&tzname=Europe%2FLondon&uc=1"
            + "&mos=Android&bs=0&uaid=e8803ee41710a4d12ab3b31e9eff15d8S0&memsz=2048"
            + ".0&osv=9&ul=en-US&mtsts=2020-05-05T22%3A39%3A30"
            + ".147&pagename=ViewItemPhotos&app=2571&res=1080x2145&c=10&mav=6.0"
            + ".1&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&reqts=1588743651007&cguidsrc=cookie&n"
            + "=07b876771700a4cc0287d4cfe1dd5ad1&ort=p&p=2052300&ttp=Page&mnt=WIFI&carrier=O2"
            + "+-+UK&t"
            + "=3&u=1151771663&prefl=en_LT&cflgs=EA**&ids=MP%253Dgedmas1991&designsystem=6&mrollp"
            + "=81"
            + "&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&mppid=117&androidid=3e98b7f5fbf8de8f"
            + "&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&pn=2&rq=887634bf8cac3a4f&ciid=gD7kEqs*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D887634bf8cac3a4f%26node_id%3D8faf50de6cdc6b41%26REQUEST_GUID"
            + "%3D171e8803-ee40-a4d1-2ab0-7de2cafadc30%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E61%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e8803ee5-0x113%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=80.0.153.110&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18"
            + ".171&TStamp=22:40:51.04&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.202.7"
            + ".45"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&site=3&ai=2571&rvrsite=0&rlutype=1&imp=2052300&lv=udid"
            + "%3Dad402a4c168acc2d4dd1bb50015645bd%26ai%3D2571%26mav%3D6.0"
            + ".1%26site%3D3%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AFlYOmD5GEpw%252Bdj6x9nY"
            + "%252BseQ**%26memsz%3D2048.0%26res%3D1080x2145%26mrollp%3D81%26designsystem%3D6%26c"
            + "%3D10%26osv%3D9%26ids%3DMP%253Dgedmas1991%26mnt%3DWIFI%26ist%3D0%26prefl%3Den_LT"
            + "%26tzname%3DEurope%2FLondon%26user_name%3Dgedmas1991%26androidid%3D3e98b7f5fbf8de8f"
            + "%26reqts%3D1588743651007%26tz%3D1"
            + ".0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier"
            + "%3DO2+-+UK%26gadid%3D0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1%26dn%3DLYA-L09%26dpi"
            + "%3D537.882x539.102%26mppid%3D117%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A39%3A30"
            + ".147&udid=ad402a4c168acc2d4dd1bb50015645bd&mppid=117");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(10);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 10
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2056193);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:40:13.365")));
    ubiEvent.setRemoteIP("10.202.7.45");
    ubiEvent.setRequestCorrelationId("887634bf8cac3a4f");
    ubiEvent.setSid("p2048309.m2511.l1311");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E61%28twwgsvv%28umj%28bad%7F%29%60jk-171e8803eee-0x126");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(734757011072L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=gedmas1991&tz=1.0&lv=1&dn=LYA-L09&ist=0&rlutype=1&tzname=Europe%2FLondon&uc=1"
            + "&mos=Android&bs=0&uaid=e8803eee1710a4d12ab3b31e9eff15d2S0&memsz=2048"
            + ".0&osv=9&trkp=qacc%253DTRS0%252CR1%252CTR5%252CTRC0%252CXlong%252520wre%252CQlong"
            + "%252520wrench%252CNPF0%252CPFS0%252CTTC1853%252CMEDT307%252CAVGT309&ul=en-US&mtsts"
            + "=2020-05-05T22%3A40%3A13.365&pagename=SearchResultsViewed&app=2571&res=1080x2145&c"
            + "=16"
            + "&mav=6.0.1&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&reqts=1588743651009&cguidsrc"
            + "=cookie"
            + "&n=07b876771700a4cc0287d4cfe1dd5ad1&ort=p&p=2056193&ttp=Page&mnt=WIFI&carrier=O2"
            + "+-+UK"
            + "&t=3&u=1151771663&prefl=en_LT&cflgs=EA**&ids=MP%253Dgedmas1991&designsystem=6&mrollp"
            + "=81&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&mppid=117&androidid"
            + "=3e98b7f5fbf8de8f&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&pn=2&rq=887634bf8cac3a4f"
            + "&ciid=gD7uEqs*&sid=p2048309.m2511.l1311");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D887634bf8cac3a4f%26node_id%3D8bc35b9c3a1f1a97%26REQUEST_GUID"
            + "%3D171e8803-eed0-a4d1-2ab0-7de2cafadc2d%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E61%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e8803eee-0x126%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=80.0.153.110&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18"
            + ".171&TStamp=22:40:51.05&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.202.7"
            + ".45"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&site=3&ai=2571&rvrsite=0&rlutype=1&imp=2056193&lv=udid"
            + "%3Dad402a4c168acc2d4dd1bb50015645bd%26ai%3D2571%26mav%3D6.0"
            + ".1%26site%3D3%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AFlYOmD5GEpw%252Bdj6x9nY"
            + "%252BseQ**%26memsz%3D2048.0%26res%3D1080x2145%26mrollp%3D81%26designsystem%3D6%26c"
            + "%3D16%26osv%3D9%26ids%3DMP%253Dgedmas1991%26mnt%3DWIFI%26ist%3D0%26prefl%3Den_LT"
            + "%26tzname%3DEurope%2FLondon%26user_name%3Dgedmas1991%26androidid%3D3e98b7f5fbf8de8f"
            + "%26reqts%3D1588743651009%26tz%3D1"
            + ".0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26trkp%3Dqacc%253DTRS0%252CR1%252CTR5"
            + "%252CTRC0%252CXlong%252520wre%252CQlong%252520wrench%252CNPF0%252CPFS0%252CTTC1853"
            + "%252CMEDT307%252CAVGT309%26carrier%3DO2+-+UK%26gadid%3D0e1f6958-d08e-4569-a58b"
            + "-fac4b0aa670c%2C1%26dn%3DLYA-L09%26dpi%3D537.882x539"
            + ".102%26mppid%3D117%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A40%3A13"
            + ".365&udid=ad402a4c168acc2d4dd1bb50015645bd&_trksid=p2048309.m2511.l1311&mppid=117");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(16);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 11
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2052300);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:37:50.347")));
    ubiEvent.setRemoteIP("10.202.7.45");
    ubiEvent.setRequestCorrelationId("887634bf8cac3a4f");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E61%28twwgsvv%28umj%28bad%7F%29%60jk-171e8803edb-0x119");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(734755765888L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=gedmas1991&tz=1.0&lv=1&dn=LYA-L09&ist=0&rlutype=1&tzname=Europe%2FLondon&uc=1"
            + "&mos=Android&bs=0&uaid=e8803edb1710a4d12ab3b31e9eff15dfS0&memsz=2048"
            + ".0&osv=9&ul=en-US&mtsts=2020-05-05T22%3A37%3A50"
            + ".347&pagename=ViewItemPhotos&app=2571&res=1080x2145&c=5&mav=6.0"
            + ".1&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&reqts=1588743651007&cguidsrc=cookie&n"
            + "=07b876771700a4cc0287d4cfe1dd5ad1&ort=p&p=2052300&ttp=Page&mnt=WIFI&carrier=O2"
            + "+-+UK&t"
            + "=3&u=1151771663&prefl=en_LT&cflgs=EA**&ids=MP%253Dgedmas1991&designsystem=6&mrollp"
            + "=81"
            + "&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&mppid=117&androidid=3e98b7f5fbf8de8f"
            + "&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&pn=2&rq=887634bf8cac3a4f&ciid=gD7bEqs*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D887634bf8cac3a4f%26node_id%3Da02d9d72d6630038%26REQUEST_GUID"
            + "%3D171e8803-edb0-a4d1-2ab0-7de2cafadc34%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E61%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e8803edb-0x119%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=80.0.153.110&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18"
            + ".171&TStamp=22:40:51.03&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.202.7"
            + ".45"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&site=3&ai=2571&rvrsite=0&rlutype=1&imp=2052300&lv=udid"
            + "%3Dad402a4c168acc2d4dd1bb50015645bd%26ai%3D2571%26mav%3D6.0"
            + ".1%26site%3D3%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AFlYOmD5GEpw%252Bdj6x9nY"
            + "%252BseQ**%26memsz%3D2048.0%26res%3D1080x2145%26mrollp%3D81%26designsystem%3D6%26c"
            + "%3D5"
            + "%26osv%3D9%26ids%3DMP%253Dgedmas1991%26mnt%3DWIFI%26ist%3D0%26prefl%3Den_LT%26tzname"
            + "%3DEurope%2FLondon%26user_name%3Dgedmas1991%26androidid%3D3e98b7f5fbf8de8f%26reqts"
            + "%3D1588743651007%26tz%3D1.0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DO2"
            + "+-+UK%26gadid%3D0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1%26dn%3DLYA-L09%26dpi%3D537"
            + ".882x539.102%26mppid%3D117%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A37%3A50"
            + ".347&udid=ad402a4c168acc2d4dd1bb50015645bd&mppid=117");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(5);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1151771663");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(1, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric5() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e83186d0171dc45b331bda30016b6cd2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2349624);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:14:55.624")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId("e0esyQ4x541w");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dosusqn47pse31%285020%3E05%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e8319488"
            + "-0x1fa");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;1080x2164;3.4");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(421370827569L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxIMAAAAAAAAICYQAEABAAgAABgAAAAAAwAAABAAACAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc"
            +
            "=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&flgs=AIAxIMAAAAAAAAICYQAEABAAgAABgAAAAAAwAAABAAACAAAAAAAAUA**&ssc=11190903013&gsp=0&ppcPromotionType=INSTALLMENT&viStreamId=e83186d0171dc45b331bda30016b6cd2&snippetlength=54&obfs_sid_uid_same=false&viFinancingBanner=true&mos=Android&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&ppc_ofid=6342819203&osv=10&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=0&noep=11&vibisdm=1200X1600&app=2571&!_OBFS_SELLERID=687196293&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=2429&!_callingpageid=2349624&swccount=1&meta=6000&slr=687196293&curprice=1150.0&viPageType=0&attrct=5&ccc_off_id=6342779101&!pymntMethods=PPL|VSA|MSC|AMX|DSC&rq=e0esyQ4x541w&!cat1=10063&l1=6028&l2=10063&qtys=0&nsm=NF&itmcond=3000&!_OBFS_BUYER_VIEWING_SITEID=0&bs=0&OBFS_ITEMID=323799503664&binamt=1150.0&obfs_desc_has_contactInfo=false&vibisb=1600&vibisd=1200X1600&sn=trebla119&qtya=1&st=9&mav=6.0.1&pudo=0&vibisbm=1600&g=e83186d0171dc45b331bda30016b6cd2&h=d0&leaf=38664&nativeApp=true&cp=2349624&n=e8318b081710ad4841125071c0a1ddab&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=99&t=0&nofp=11&rpdur=30&tr=54629&dc=1&nozp=11&dm=samsung&dn=z3q&!_OBFS_LINK_COUNT=0&uc=1&mbsa=500&ul=en-US&pymntVersion=1&!oed=0&ec=4&res=0x0&efam=ITM&itm=323799503664&promol=0&vpcg=false&iver=2387039332011&!_OBFS_BUYERID=0&es=0&vi_finopt=1&itmtitle=Harley+Touring+Custom+Tins+Paint+Set+Fenders+Gas+Fuel+Tank+Bags&viFinancingModule=true&cflgs=AA**&gxoe=vine&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&eactn=EXPC&shipsiteid=100&obfs_listing_is_eligible=true&nw=5&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&vi_cobranded=true&!notes=0&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=USD%3A86.98%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EECONOMY%5EOther%5E0%5E-1%5E1%5E-1%5EUS%5Enull%5E323799503664%5E-1%5E3%5E5&vi_cobrand_mem=false&!cmplsvs=0|0&addOnTypes=WARRANTY&fimbsa=500&cp_usd=1150.0&swcembg=true&!vifit=4&!_OBFS_EMAIL_COUNT=0&ciid=MZOoG2I*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dce5745d0bb18d62c%26node_id%3Ddc3d244b25f8ad68%26REQUEST_GUID"
            + "%3D171e8319-4820-aa11-b622-671cfe9dbe4f%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%25285020%253E05%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e8319488-0x1fa"
            + "%26cal_mod"
            + "%3Dfalse&TPool=r1viexpsvc7&TDuration=4&TStatus=0&TType=URL&ContentLength=3197"
            + "&ForwardedFor=23.56.175.28; 172.232.21.4;174.221.5"
            + ".135&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.161.27"
            + ".98&TStamp=21:14:55.62&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;"
            + "1080x2164;"
            + "3.4&RemoteIP=174.221.5.135&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=323799503664&modules=VLS,"
            + "VOLUME_PRICING,SME&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,"
            + "FITMENT,BUY_BOX,PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,"
            + "EBAY_PLUS_PROMO,"
            + "VEHICLE_HISTORY,VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,"
            + "ITEM_STATUS_MESSAGE,VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,"
            + "PRODUCT_REVIEWS_SUMMARY,PRP_PRODUCT");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(323799503664L);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e83186d0171dc45b331bda30016b6cd2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2481888);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:37.737")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId("PTFPS5qqa0Ou");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%28535413%3A%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e83e1fea"
            + "-0x13a");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;1080x2164;3.4");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(276172382014L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&flgs=AA**&dm=samsung&dn=z3q&uc=1&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&mos=Android&bs=0&pageci=0e22e718-8f52-11ea-858d-74dbd180dcf0&osv=10&ul=en-US&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&ec=4&pagename=hpservice__experience_search_v1_get_homepage_search_result_GET&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0.1&g=e83186d0171dc45b331bda30016b6cd2&h=d0&nativeApp=true&cp=2481888&an=eBayAndroid&n=e8318b081710ad4841125071c0a1ddab&es=0&p=2481888&t=0&cflgs=AA**&lfcat=0&eactn=EXPC&rq=PTFPS5qqa0Ou&ciid=Ph8oTUA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De80a3c64e99de636%26node_id%3D0841a1d5c1bc5259%26REQUEST_GUID"
            + "%3D171e83e1-fe60-a9c4-d400-11d4f6bd6e23%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dpiesqn47pse31"
            + "%2528535413%253A%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e83e1fea-0x13a"
            + "%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=1376&ForwardedFor=172.232.21.21;174.221.5"
            + ".135&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.156.77"
            + ".64&TStamp=21:28:37.73&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;"
            + "1080x2164;"
            + "3.4&RemoteIP=174.221.5.135&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components"
            + "=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,"
            + "THIRD_PARTY_ADS_BANNER,TEXT_BANNER,FULL_BLEED_BANNER,MULTI_CTA_BANNER,"
            + "NAVIGATION_IMAGE_GRID,NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e83186d0171dc45b331bda30016b6cd2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(3084);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:37.743")));
    ubiEvent.setRemoteIP("10.195.199.95");
    ubiEvent.setRequestCorrelationId("22d630a3caeb61e7");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0060%3A27%29pqtfwpu%29pie%29fgg%7E-fij-171e83e1ff1-0x1a9");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(948012326718L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "app=2571&c=1&g=e83186d0171dc45b331bda30016b6cd2&nid=&h=d0&cguidsrc=cookie&n"
            + "=e8318b081710ad4841125071c0a1ddab&uc=1&p=3084&uaid"
            + "=e83e1ff01710a9cb9dc77f56c2bc39bcS0"
            + "&bs=0&rvrid=2410574292579&t=0&cflgs=EA**&ul=en-US&mppid=119&pn=2&pcguid"
            + "=e8318b081710ad4841125071c0a1ddab&rq=22d630a3caeb61e7&pagename=EntryTracking&ciid=Ph"
            + "%2Fwudw*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D22d630a3caeb61e7%26node_id%3Deb2b8f62f540b0d5%26REQUEST_GUID"
            + "%3D171e83e1-fef0-a9cb-9dc0-c6c3e010d67f%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0060"
            + "%253A27%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e83e1ff1-0x1a9%26cal_mod%3Dfalse"
            + "&TPool"
            + "=r1rover&TDuration=39&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=174.221.5"
            + ".135&Script=/rover/1/0/4&Server=localhost&TMachine=10.156.185.220&TStamp=21:28:37"
            + ".74&TName=rover&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.195.199.95"));
    ubiEvent.setUrlQueryString(
        "/rover/1/0/4?memsz=8192.0&res=1080x2164&designsystem=6&mnt=MOBILE&prefl=en_US&tzname"
            + "=America%2FChicago&androidid=9af24c9972b9e493&ort=p&carrier=Verizon&dpi=386.3655x383"
            + ".3955&dn=SM-G988U&mtsts=2020-05-05T21%3A28%3A37"
            + ".575&ctr=0&nrd=1&site=0&mrollp=73&c=7&mav=6.0"
            + ".1&osv=10&ids=MP&ist=0&udid=e83186d0171dc45b331bda30016b6cd2&rvrhostname=rover.ebay"
            + ".com&ai=2571&rvrsite=0&reqts=1588739317741&tz=-5"
            + ".0&rlutype=1&mos=Android&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&mppid=119");
    ubiEvent.setPageName("rover");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e83186d0171dc45b331bda30016b6cd2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2504946);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:48.147")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId(null);
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pbhnmpo%3D9whhpbhnmpo*krbsa(rbpv6710-171e83e4838-0x2906");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Linux; Android 10; SM-G988U Build/QP1A.190711.020; wv) AppleWebKit/537.36 "
            + "(KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(504798136382L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "app=2571&nqc"
            +
            "=AAAAAAAABAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJA&webview=true&c=1&!qt=56226,226654,226602,226007,226526&g=e83186d0171dc45b331bda30016b6cd2&h=49&es=0&nqt=AAAAAAAABAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJA&p=2504946&bot_provider=%7B%22providers%22%3A%7B%22DT%22%3A%7B%22headers%22%3A%7B%22xdb%22%3A%220%22%2C%22drid%22%3A%22a0badd05-bd1d-4c67-8f1e-9640383106ad%22%2C%22xrip%22%3A%22174.221.5.135%22%7D%7D%7D%7D&!qc=590081,590081,590081,590081,590081&t=0&cflgs=QA**&SigninRedirect=NodeOther&ec=2&pn=0&pagename=sgninui__SignInMFA&ciid=PkhQiHU*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De83e483b1710aae8c7285ad1ffdf4c22%26node_id%3D%26REQUEST_GUID"
            + "%3De83e48491710aae8c72c92d1ff9b2e26%26logid%3Dt6pbhnmpo%253D9whhpbhnmpo%2Akrbsa"
            + "%28rbpv6710-171e83e4838-0x2906%26statusCode%3D200&TPool=r1sgninui&TStatus=0&TType"
            + "=URL"
            + "&ContentLength=0&ForwardedFor=174.221.5.135&Script=/signin/mfa&Server=www.ebay"
            + ".com&TMachine=10.174.140.114&TStamp=21:28:48.14&TName=SignInMFA&Agent=Mozilla/5.0 "
            + "(Linux; Android 10; SM-G988U Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML,"
            + " like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36&RemoteIP=174.221.5"
            + ".135&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/signin/mfa?id=1VcDxbrQsUAnFqWyfBvql-19&UUID=e83186d0171dc45b331bda30016b6cd2&srcAppId"
            + "=2571&trackingSrcAppId=2571&trackingNativeAppGuid=e83186d0171dc45b331bda30016b6cd2"
            + "&trackingApp=webview&correlation=si=e83e48491710aae8c72c92d1ff9b2e26,c=1,"
            + "trk-gflgs=QA**&SSRFallback=0");
    ubiEvent.setPageName("SignInMFA");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e83186d0171dc45b331bda30016b6cd2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2504946);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:29:48.361")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId(null);
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pbhnmpo%3D9whhpbhnmpo*hh%3F1s(rbpv6710-171e83f332f-0x2902");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Linux; Android 10; SM-G988U Build/QP1A.190711.020; wv) AppleWebKit/537.36 "
            + "(KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(487198044991L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "app=2571&nqc"
            +
            "=AAAAAAAAgAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACBA&webview=true&c=6&!qt=56226,226601,226522,226006&g=e83186d0171dc45b331bda30016b6cd2&h=d0&n=e83e4a8d1710a9cb71024307c2e37139&es=0&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACBA&p=2504946&bot_provider=%7B%22providers%22%3A%7B%22DT%22%3A%7B%22headers%22%3A%7B%22xdb%22%3A%221%22%2C%22drid%22%3A%22ba8e8c57-4d80-4d6a-b89a-fd3db35a075c%22%2C%22xrip%22%3A%22174.221.5.135%22%7D%7D%7D%7D&!qc=590081,590081,590081,590081&t=0&SigninRedirect=NodeOther&ec=2&pn=0&pagename=sgninui__SignInMFA&ciid=PzNEb3E*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De83f33311710aae8c27d24ecffe0c688%26node_id%3D%26REQUEST_GUID"
            + "%3De83186d0171dc45b331bda30016b6cd2%26logid%3Dt6pbhnmpo%253D9whhpbhnmpo%2Ahh%253F1s"
            + "%28rbpv6710-171e83f332f-0x2902%26statusCode%3D200&TPool=r1sgninui&TStatus=0&TType"
            + "=URL"
            + "&ContentLength=0&ForwardedFor=174.221.5.135&Script=/signin/mfa&Server=www.ebay"
            + ".com&TMachine=10.174.140.39&TStamp=21:29:48.36&TName=SignInMFA&Agent=Mozilla/5.0 "
            + "(Linux; Android 10; SM-G988U Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML,"
            + " like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36&RemoteIP=174.221.5"
            + ".135&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/signin/mfa?id=IJIssZNEb397bdAToGfWG-21&UUID=e83186d0171dc45b331bda30016b6cd2&srcAppId"
            + "=2571&trackingSrcAppId=2571&trackingNativeAppGuid=e83186d0171dc45b331bda30016b6cd2"
            + "&trackingApp=webview&correlation=gci=e83e4a8d1710a9cb71024307c2e37139,"
            + "si=e83186d0171dc45b331bda30016b6cd2,c=6,trk-gflgs=&SSRFallback=0");
    ubiEvent.setPageName("SignInMFA");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(6);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 6
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e83186d0171dc45b331bda30016b6cd2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2540848);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:55.761")));
    ubiEvent.setRemoteIP("10.15.147.19");
    ubiEvent.setRequestCorrelationId("608f65024b8a1974");
    ubiEvent.setSid("p2050533");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0055%3B14%29pqtfwpu%29pie%29fgg%7E-fij-171e83fb6f0-0x1a6");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(351448118847L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&tz=-5.0&lv=1&dn=SM-G988U&ist=0&rlutype=1&tzname=America%2FChicago&uc=1&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&mos=Android&bs=0&uaid=e83fb6f01710a9cd3517a0ecc2db664eS0&memsz=8192.0&osv=10&ul=en-US&mtsts=2020-05-05T21%3A28%3A55.761&ec=4&pagename=SignInSocial&app=2571&res=1080x2164&c=12&mav=6.0.1&g=e83186d0171dc45b331bda30016b6cd2&h=d0&reqts=1588739421919&cguidsrc=cookie&n=e8318b081710ad4841125071c0a1ddab&es=0&ort=p&p=2540848&ttp=Page&mnt=MOBILE&carrier=Verizon&t=0&prefl=en_US&cflgs=EA**&ids=MP&designsystem=6&mrollp=73&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&mppid=119&androidid=9af24c9972b9e493&pcguid=e8318b081710ad4841125071c0a1ddab&pn=2&rq=608f65024b8a1974&ciid=P7bw01E*&sid=p2050533");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D608f65024b8a1974%26node_id%3D99be9ccab4b152ec%26REQUEST_GUID"
            + "%3D171e83fb-6ef0-a9cd-3511-6f7be02234d3%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0055"
            + "%253B14%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e83fb6f0-0x1a6%26cal_mod%3Dfalse"
            + "&TPool"
            + "=r1rover&TDuration=4&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=174.221.5"
            + ".135&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.156.211.81&TStamp=21:30:21"
            + ".93&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.15.147.19"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&site=0&ai=2571&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26nqt%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26ec%3D4%3C%2Fa%3E&rlutype=1&imp=2540848&lv=udid%3De83186d0171dc45b331bda30016b6cd2%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26memsz%3D8192.0%26res%3D1080x2164%26mrollp%3D73%26designsystem%3D6%26c%3D12%26osv%3D10%26ids%3DMP%26mnt%3DMOBILE%26ist%3D0%26prefl%3Den_US%26tzname%3DAmerica%2FChicago%26androidid%3D9af24c9972b9e493%26reqts%3D1588739421919%26tz%3D-5.0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DVerizon%26gadid%3D8780e3f4-6571-436b-b6ac-3cd243611196%2C1%26dn%3DSM-G988U%26dpi%3D386.3655x383.3955%26mppid%3D119%26ttp%3DPage%26mtsts%3D2020-05-05T21%3A28%3A55.761&udid=e83186d0171dc45b331bda30016b6cd2&_trksid=p2050533&mppid=119");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(12);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 7
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e83186d0171dc45b331bda30016b6cd2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2553215);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:45.730")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId("6d71cc897547b2e9");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%2855%3A516%3B%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh"
            + "-171e83fb4c4-0xd1");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("CLIENT_PAGE_VIEW");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;1080x2164;3.4");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(1027667244095L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&usecase=prm&tz=-5.0&dm=samsung&dn=z3q&rlutype=1&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26nqt%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26ec%3D4%3C%2Fa%3E&mos=Android&pageci=0e22e718-8f52-11ea-858d-74dbd180dcf0&osv=10&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&mtsts=2020-05-06T04%3A28%3A45.730Z&pagename=CLIENT_PAGE_VIEW&app=2571&res=0x0&parentrq=PTFPS5qqa0Ou&efam=HOMEPAGE&mav=6.0.1&c=8&g=e83186d0171dc45b331bda30016b6cd2&h=d0&nativeApp=true&cp=2481888&an=eBayAndroid&n=e8318b081710ad4841125071c0a1ddab&ort=p&p=2553215&t=0&cflgs=AA**&ids=MP&designsystem=6&mrollp=73&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&mppid=119&eactn=CLIENT_PAGE_VIEW&androidid=9af24c9972b9e493&rq=6d71cc897547b2e9&ciid=P7S9Re8*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D6d71cc897547b2e9%26node_id%3D1faddb51a06f4d20%26REQUEST_GUID"
            + "%3D171e83fb475.a9cbeda.1a70e.dbc26baf%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31"
            + "%252855%253A516%253B%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e83fb4c4-0xd1"
            + "%26cal_mod%3Dfalse&TPool=r1pulsgwy&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=1329"
            + "&ForwardedFor=23.56.175.14; 172.232.21.21;174.221.5"
            + ".135&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.202.69"
            + ".239&TStamp=21:30:21.37&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;"
            + "1080x2164;"
            + "3.4&RemoteIP=174.221.5.135&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/base/tracking/v1/track_events");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(8);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    // 8
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e83186d0171dc45b331bda30016b6cd2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2050533);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:55.719")));
    ubiEvent.setRemoteIP("10.15.147.19");
    ubiEvent.setRequestCorrelationId("608f65024b8a1974");
    ubiEvent.setSid("p2047939");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0055%3B14%29pqtfwpu%29pie%29fgg%7E-fij-171e83fb6eb-0x126");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setAppId(2571);
    ubiEvent.setCurrentImprId(351447725631L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&tz=-5.0&lv=1&dn=SM-G988U&ist=0&rlutype=1&tzname=America%2FChicago&uc=1&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&mos=Android&bs=0&uaid=e83fb6ea1710a9cd3517a0ecc2db6650S0&memsz=8192.0&osv=10&ul=en-US&mtsts=2020-05-05T21%3A28%3A55.719&ec=4&pagename=UserSignin&app=2571&res=1080x2164&c=11&mav=6.0.1&g=e83186d0171dc45b331bda30016b6cd2&h=d0&reqts=1588739421919&cguidsrc=cookie&n=e8318b081710ad4841125071c0a1ddab&es=0&ort=p&p=2050533&ttp=Page&mnt=MOBILE&carrier=Verizon&t=0&prefl=en_US&cflgs=EA**&ids=MP&designsystem=6&mrollp=73&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&mppid=119&androidid=9af24c9972b9e493&pcguid=e8318b081710ad4841125071c0a1ddab&pn=2&rq=608f65024b8a1974&ciid=P7bq01E*&sid=p2047939");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D608f65024b8a1974%26node_id%3Dfb44e7327317a64a%26REQUEST_GUID"
            + "%3D171e83fb-6e90-a9cd-3511-6f7be02234d4%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0055"
            + "%253B14%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e83fb6eb-0x126%26cal_mod%3Dfalse"
            + "&TPool"
            + "=r1rover&TDuration=4&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=174.221.5"
            + ".135&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.156.211.81&TStamp=21:30:21"
            + ".92&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.15.147.19"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&site=0&ai=2571&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26nqt%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26ec%3D4%3C%2Fa%3E&rlutype=1&imp=2050533&lv=udid%3De83186d0171dc45b331bda30016b6cd2%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26memsz%3D8192.0%26res%3D1080x2164%26mrollp%3D73%26designsystem%3D6%26c%3D11%26osv%3D10%26ids%3DMP%26mnt%3DMOBILE%26ist%3D0%26prefl%3Den_US%26tzname%3DAmerica%2FChicago%26androidid%3D9af24c9972b9e493%26reqts%3D1588739421919%26tz%3D-5.0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DVerizon%26gadid%3D8780e3f4-6571-436b-b6ac-3cd243611196%2C1%26dn%3DSM-G988U%26dpi%3D386.3655x383.3955%26mppid%3D119%26ttp%3DPage%26mtsts%3D2020-05-05T21%3A28%3A55.719&udid=e83186d0171dc45b331bda30016b6cd2&_trksid=p2047939&mppid=119");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(11);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(14, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric6() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4e18a3f31710a9b12631ac8df426f4a2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605052);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:43:42.721")));
    ubiEvent.setRemoteIP("46.87.160.134");
    ubiEvent.setRequestCorrelationId("99f0a402ef032878");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*qm%3Fir%28rbpv6710-171e882dd84-0x171");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("46.87.160.134");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.5 Safari/605.1.15");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(293593210242L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=4e18a3f31710a9b12631ac8df426f4a2&rurl=https%25253A%25252F%25252Fwww.google"
            + ".de%25252F&h=f3&px=4249&chnl=9&n=6f49edae1710abd98a677b9ade357cfc&uc=77&p=1605052"
            + "&uaid"
            + "=e882dd821710a6e5b443bcb1f08c3f9aS0&bs=77&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77&cflgs=EA**&ul=de-DE&pn=2&rq=99f0a402ef032878&pagename=http"
            + "://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=gt2EW0Q*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D99f0a402ef032878%26node_id%3D98e9c9967aa6d39e%26REQUEST_GUID"
            + "%3D171e882d-d810-a6e5-b445-389ff7e4b7bb%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2Aqm"
            + "%253Fir%2528rbpv6710-171e882dd84-0x171%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=46.87.160"
            + ".134&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.110.91"
            + ".68&TStamp=22:43:42"
            + ".72&TName=roverimp_INTL&Agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15)"
            + " AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.5 Safari/605.1.15&RemoteIP=46"
            + ".87.160.134&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=rurl%3Dhttps%25253A%25252F%25252Fwww.google"
            + ".de%25252F&mpt=1588743822528&imp=1605052");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(715462550L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4e18a3f31710a9b12631ac8df426f4a2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605053);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:44:50.669")));
    ubiEvent.setRemoteIP("46.87.160.134");
    ubiEvent.setRequestCorrelationId("e08b97a4f9ed9d3c");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ctofthu%60t*00%3F30%3D2-171e883e6f4-0x113");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("46.87.160.134");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.5 Safari/605.1.15");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(480549070467L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "cadid=1395751086&c=1&g=4e18a3f31710a9b12631ac8df426f4a2&rurl=https%25253A%25252F"
            + "%25252Fwww.ebay-kleinanzeigen.de%25252Fs-schallplatten%25252Fk0&h=f3&px=4249&chnl"
            + "=9&n"
            + "=6f49edae1710abd98a677b9ade357cfc&uc=77&p=1605053&uaid"
            + "=e883e6ee1710ac3e26f04aa0c2b539e3S0&bs=77&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77®=Bayern%2520-%2520Aschau%2520im%2520Chiemgau&cflgs=EA**&ul"
            + "=de"
            + "-DE&pn=2&rq=e08b97a4f9ed9d3c&pagename=http://kleinanzeigen.ebay"
            + ".de/anzeigen/s-anzeige/dalmatinerwelpe-ohne-papiere-in-liebevolle-hnde/1098221"
            + "&ciid=g"
            + "%2Bb04m8*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De08b97a4f9ed9d3c%26node_id%3Da15b8d9b4ed6fc3d%26REQUEST_GUID"
            + "%3D171e883e-6ed0-ac3e-26f4-b630e00e11ef%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t%2A00"
            + "%253F30%253D2-171e883e6f4-0x113%26cal_mod%3Dfalse&TPool=r1rover&TDuration=8"
            + "&TStatus=0"
            + "&TType=URL&ContentLength=-1&ForwardedFor=46.87.160"
            + ".134&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.195.226"
            + ".111&TStamp=22:44:50.66&TName=roverimp_INTL&Agent=Mozilla/5.0 (Macintosh;"
            + " Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0"
            + ".5 Safari/605.1.15&RemoteIP=46.87.160.134&Encoding=gzip&Referer=https://www"
            + ".ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=cadid%3D1395751086%26reg%3DBayern%2520-%2520Aschau%2520im"
            + "%2520Chiemgau%26rurl%3Dhttps%25253A%25252F%25252Fwww.ebay-kleinanzeigen"
            + ".de%25252Fs-schallplatten%25252Fk0&mpt=1588743890482&imp=1605053");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(715462550L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4e18a3f31710a9b12631ac8df426f4a2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605052);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:45:25.360")));
    ubiEvent.setRemoteIP("46.87.160.134");
    ubiEvent.setRequestCorrelationId("ec3cb698d178516b");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*4unpu%28rbpv6710-171e8846e74-0x15e");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("46.87.160.134");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.5 Safari/605.1.15");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(437272211076L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=4e18a3f31710a9b12631ac8df426f4a2&rurl=https%25253A%25252F%25252Fwww.google"
            + ".de%25252F&h=f3&px=4249&chnl=9&n=6f49edae1710abd98a677b9ade357cfc&uc=77&p=1605052"
            + "&uaid"
            + "=e8846e711710aaecf655725ef0927a0eS0&bs=77&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77&cflgs=EA**&ul=de-DE&pn=2&rq=ec3cb698d178516b&pagename=http"
            + "://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=hG50z2U*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dec3cb698d178516b%26node_id%3D902374d7a9c4f82b%26REQUEST_GUID"
            + "%3D171e8846-e700-aaec-f656-1e28f7e7744f%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A4unpu%2528rbpv6710-171e8846e74-0x15e%26cal_mod%3Dfalse&TPool=r1rover&TDuration=5"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=46.87.160"
            + ".134&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.174.207"
            + ".101&TStamp=22:45:25.36&TName=roverimp_INTL&Agent=Mozilla/5.0 (Macintosh;"
            + " Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0"
            + ".5 Safari/605.1.15&RemoteIP=46.87.160.134&Encoding=gzip&Referer=https://www"
            + ".ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=rurl%3Dhttps%25253A%25252F%25252Fwww.google"
            + ".de%25252F&mpt=1588743925256&imp=1605052");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(715462550L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4e18a3f31710a9b12631ac8df426f4a2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605053);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:46:04.650")));
    ubiEvent.setRemoteIP("46.87.160.134");
    ubiEvent.setRequestCorrelationId("49bd2dd0c30f94ca");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*ikeq1%28rbpv6710-171e88507f0-0x173");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("46.87.160.134");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML,"
            + " like Gec Version/13.0.5 Safari/605.1.15");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(536064493445L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "cadid=1395731657&c=1&g=4e18a3f31710a9b12631ac8df426f4a2&rurl=https%25253A%25252F"
            + "%25252Fwww.ebay-kleinanzeigen.de%25252Fs-schallplatten%25252Fk0&h=f3&px=4249&chnl"
            + "=9&n"
            + "=6f49edae1710abd98a677b9ade357cfc&uc=77&p=1605053&uaid"
            + "=e88507eb1710aaecf7c49657f08f6becS0&bs=77&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77®=Bayern%2520-%2520Memmingen&cflgs=EA**&ul=de-DE&pn=2&rq"
            + "=49bd2dd0c30f94ca&pagename=http://kleinanzeigen.ebay"
            + ".de/anzeigen/s-anzeige/dalmatinerwelpe-ohne-papiere-in-liebevolle-hnde/1098221&ciid"
            + "=hQfvz3w*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D49bd2dd0c30f94ca%26node_id%3D1950fc003431d09c%26REQUEST_GUID"
            + "%3D171e8850-7ea0-aaec-f7c6-a078f7e5dec7%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2Aikeq1%2528rbpv6710-171e88507f0-0x173%26cal_mod%3Dfalse&TPool=r1rover&TDuration=6"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=46.87.160"
            + ".134&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.174.207"
            + ".124&TStamp=22:46:04.65&TName=roverimp_INTL&Agent=Mozilla/5.0 (Macintosh;"
            + " Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0"
            + ".5 Safari/605.1.15&RemoteIP=46.87.160.134&Encoding=gzip&Referer=https://www"
            + ".ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=cadid%3D1395731657%26reg%3DBayern%2520-%2520Memmingen%26rurl"
            + "%3Dhttps%25253A%25252F%25252Fwww.ebay-kleinanzeigen"
            + ".de%25252Fs-schallplatten%25252Fk0&mpt=1588743964550&imp=1605053");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(715462550L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4e18a3f31710a9b12631ac8df426f4a2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605052);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:46:59.102")));
    ubiEvent.setRemoteIP("46.87.160.134");
    ubiEvent.setRequestCorrelationId("26575aecf2f6b117");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ckuvthu%60t*0%3D6465%3F%29pqtfwpu%29osu%29fgg%7E-fij-171e885dca4-0x11d");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("46.87.160.134");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML,"
            + " like Gec Version/13.0.5 Safari/605.1.15");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(115001777285L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=4e18a3f31710a9b12631ac8df426f4a2&rurl=https%25253A%25252F%25252Fwww.google"
            + ".de%25252F&h=f3&px=4249&chnl=9&n=6f49edae1710abd98a677b9ade357cfc&uc=77&p=1605052"
            + "&uaid"
            + "=e885dc9e1710aadc61a2d178c06b85e1S0&bs=77&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77&cflgs=EA**&ul=de-DE&pn=2&rq=26575aecf2f6b117&pagename=http"
            + "://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=hdyjxho*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D26575aecf2f6b117%26node_id%3D9516dba2d8240f8f%26REQUEST_GUID"
            + "%3D171e885d-c9e0-aadc-61a1-b519de1e68b2%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0"
            + "%253D6465%253F%2529pqtfwpu%2529osu%2529fgg%257E-fij-171e885dca4-0x11d%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=7&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=46.87.160.134, 23.214.197.143&Script=/roverimp/0/0/9&Server=rover.ebay"
            + ".de&TMachine=10"
            + ".173.198.26&TStamp=22:46:59.10&TName=roverimp_INTL&Agent=Mozilla/5.0 (Macintosh;"
            + " Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0"
            + ".5 Safari/605.1.15&RemoteIP=46.87.160.134&Encoding=gzip&Referer=https://www"
            + ".ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=rurl%3Dhttps%25253A%25252F%25252Fwww.google"
            + ".de%25252F&mpt=1588744018998&imp=1605052");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(715462550L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 6
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4e18a3f31710a9b12631ac8df426f4a2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605053);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:47:16.890")));
    ubiEvent.setRemoteIP("46.87.160.134");
    ubiEvent.setRequestCorrelationId("fc2703369781d246");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ckuvthu%60t*023%3E%3B24%29pqtfwpu%29osu%29fgg%7E-fij-171e8862221-0x11a");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("46.87.160.134");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML,"
            + " like Gec Version/13.0.5 Safari/605.1.15");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(672734978694L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "cadid=1281018235&c=1&g=4e18a3f31710a9b12631ac8df426f4a2&rurl=https%25253A%25252F"
            + "%25252Fwww.ebay-kleinanzeigen.de%25252Fs-schallplatten%25252Fk0&h=f3&px=4249&chnl"
            + "=9&n"
            + "=6f49edae1710abd98a677b9ade357cfc&uc=77&p=1605053&uaid"
            + "=e886221b1710aa6a29c1d1d7c0be1800S0&bs=77&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77®=Hessen%2520-%2520Schl%25C3%25BCchtern&cflgs=EA**&ul=de-DE"
            + "&pn"
            + "=2&rq=fc2703369781d246&pagename=http://kleinanzeigen.ebay"
            + ".de/anzeigen/s-anzeige/dalmatinerwelpe-ohne-papiere-in-liebevolle-hnde/1098221&ciid"
            + "=hiIhopw*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dfc2703369781d246%26node_id%3D1d063420dd035df3%26REQUEST_GUID"
            + "%3D171e8862-21a0-aa6a-29c2-d433de4754ff%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t"
            + "%2A023"
            + "%253E%253B24%2529pqtfwpu%2529osu%2529fgg%257E-fij-171e8862221-0x11a%26cal_mod"
            + "%3Dfalse"
            + "&TPool=r1rover&TDuration=9&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=46.87"
            + ".160"
            + ".134, 23.214.197.143&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.166.162"
            + ".156&TStamp=22:47:16.89&TName=roverimp_INTL&Agent=Mozilla/5.0 (Macintosh;"
            + " Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0"
            + ".5 Safari/605.1.15&RemoteIP=46.87.160.134&Encoding=gzip&Referer=https://www"
            + ".ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=cadid%3D1281018235%26reg%3DHessen%2520-%2520Schl%25C3%25BCchtern"
            + "%26rurl%3Dhttps%25253A%25252F%25252Fwww.ebay-kleinanzeigen"
            + ".de%25252Fs-schallplatten%25252Fk0&mpt=1588744036784&imp=1605053");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(715462550L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(21, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric7() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e88156fc1710a99bd6d26fe0c3108742");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605052);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:56:52.230")));
    ubiEvent.setRemoteIP("217.245.34.41");
    ubiEvent.setRequestCorrelationId("7c5cd78b631cda54");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0114%3B16%29pqtfwpu%29pie%29fgg%7E-fij-171e8815704-0x10f");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("217.245.34.41");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(471322613633L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=e88156fc1710a99bd6d26fe0c3108742&rurl=https%25253A%25252F%25252Fwww.google"
            + ".com%25252F&h=fc&px=4249&chnl=9&uc=77&p=1605052&uaid"
            + "=e88156fc1710a99bd6d26fe0c3108741S0&bs=77&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2Fs-hille-coaster-530%2Fk0&r=-459252443&t=77&cflgs=QA**&ul=de-DE&pn=2&rq"
            + "=7c5cd78b631cda54&pagename=http://kleinanzeigen.ebay.de/anzeigen/s-suchen"
            + ".html&ciid=gVcEvW0*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D7c5cd78b631cda54%26node_id%3D2ef4267965ddc09b%26REQUEST_GUID"
            + "%3D171e8815-6f30-a99b-d6d1-802fe03c1802%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0114"
            + "%253B16%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e8815704-0x10f%26cal_mod%3Dfalse"
            + "&TPool"
            + "=r1rover&TDuration=19&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=217.245.34"
            + ".41&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.153.189"
            + ".109&TStamp=22:42:02.73&TName=roverimp_INTL&Agent=Mozilla/5.0 (Windows NT 10.0;"
            + " Win64;"
            + " x64; rv:75.0) Gecko/20100101 Firefox/75.0&RemoteIP=217.245.34"
            + ".41&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen"
            + ".de/s-hille-coaster-530/k0"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=rurl%3Dhttps%25253A%25252F%25252Fwww.google"
            + ".com%25252F&mpt=1588743722724&imp=1605052");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(-459252443L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/s-hille-coaster-530/k0");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(21, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric8() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e8808f1a1710aa413900deafc0dd728d");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605051);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:41:11.572")));
    ubiEvent.setRemoteIP("168.149.159.11");
    ubiEvent.setRequestCorrelationId("f596fbab499bd4e8");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ckuvthu%60t*020117%3E%29pqtfwpu%29osu%29fgg%7E-fij-171e8808f20-0x1b2");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("168.149.159.11");
    ubiEvent.setAgentInfo("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(618796191616L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=e8808f1a1710aa413900deafc0dd728d&rurl=https%25253A%25252F%25252Flogin.gs"
            + ".schaeffler.com%25252Fnidp%25252Fsaml2%25252Fsso%25253Fsid%25253D0&h=1a&px=4249"
            + "&chnl=9"
            + "&uc=77&p=1605051&uaid=e8808f1a1710aa413900deafc0dd728cS0&bs=77&ref=https%3A%2F%2Fwww"
            + ".ebay-kleinanzeigen.de%2F&r=715462550&t=77&cflgs=QA**&ul=de-DE&pn=2&rq"
            + "=f596fbab499bd4e8&pagename=http://kleinanzeigen.ebay.de/anzeigen/&ciid=gI8gE5A*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Df596fbab499bd4e8%26node_id%3D7b70dfb5a542a800%26REQUEST_GUID"
            + "%3D171e8808-f140-aa41-3900-28c6de5b2b7b%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t"
            + "%2A020117%253E%2529pqtfwpu%2529osu%2529fgg%257E-fij-171e8808f20-0x1b2%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=13&TStatus=0&TType=URL&ContentLength=-1"
            + "&ForwardedFor"
            + "=10.177.7.23, 168.149.159.11, 72.247.179.140&Script=/roverimp/0/0/9&Server=rover"
            + ".ebay"
            + ".de&TMachine=10.164.19.144&TStamp=22:41:11.57&TName=roverimp_INTL&Agent=Mozilla/5.0 "
            + "(Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko&RemoteIP=168.149.159"
            + ".11&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=rurl%3Dhttps%25253A%25252F%25252Flogin.gs.schaeffler"
            + ".com%25252Fnidp%25252Fsaml2%25252Fsso%25253Fsid%25253D0&mpt=1588743653772&imp"
            + "=1605051");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(715462550L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(24, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric9() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("df842c331710a9e1e1c2f4f8c53e1b82");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605053);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:09:52.838")));
    ubiEvent.setRemoteIP("157.25.20.226");
    ubiEvent.setRequestCorrelationId("9e861ad45c0bbb8d");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ctofthu%60t*00%3F30%3D6-171e863e450-0x12b");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("157.25.20.226");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
            + " Chrome/81.0.4044.138 Safari/537.36");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(798248395875L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "cadid=1394369253&c=1&g=df842c331710a9e1e1c2f4f8c53e1b82&rurl=https%25253A%25252F"
            + "%25252Fkleinanzeige.focus.de%25252Fboote%25252Fboot-defekt"
            + ".html%25253Futm_campaign%25253D12simplifiedalert%252526utm_medium%25253Demail"
            + "%252526utm_source%25253Decosa-pl%252526utm_emailab%25253DmaxAdNo_A"
            + "%252526subscriptionid%25253DY2DISft0Dr7tDhCqf7OBrA%25253D%25253D%252526utm_content"
            + "%25253Dbuttonviewlinkseconda%252526s%25253Ddesc%252526landingpageindex"
            + "%25253D1161597082&h=33&px=4249&chnl=9&n=ca864df11710a9b13a7707eefdcf1bc7&uc=163&p"
            + "=1605053&uaid=e863e4481710ac3dbb944fa8c2e88bcaS0&bs=212&ref=https%3A%2F%2Fwww"
            + ".ebay-kleinanzeigen.de%2Fs-anzeige%2Fjohnson-evinrude-50-ps-2-takt-aussenborder"
            + "-startet-nicht-defekt%2F1394369253-211-18314%3Futm_source%3Dfocus"
            + ".de%26utm_medium%3Dcpc%26utm_campaign%3Dcoop-focus"
            + ".de&r=2065185079&t=77®=Harburg%2520-%2520Hamburg%2520Marmstorf&cflgs=EA**&ul=de-DE"
            + "&pn"
            + "=2&rq=9e861ad45c0bbb8d&pagename=http://kleinanzeigen.ebay"
            + ".de/anzeigen/s-anzeige/dalmatinerwelpe-ohne-papiere-in-liebevolle-hnde/1098221"
            + "&ciid=Y"
            + "%2BRP27k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D9e861ad45c0bbb8d%26node_id%3Dd57a41054146f034%26REQUEST_GUID"
            + "%3D171e863e-4460-ac3d-bb95-85e3e029a690%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t%2A00"
            + "%253F30%253D6-171e863e450-0x12b%26cal_mod%3Dfalse&TPool=r1rover&TDuration=11"
            + "&TStatus=0"
            + "&TType=URL&ContentLength=-1&ForwardedFor=157.25.20.226, 2.18.215"
            + ".207&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.195.219"
            + ".185&TStamp=22:09:52.83&TName=roverimp_INTL&Agent=Mozilla/5.0 (Windows NT 10.0;"
            + " Win64;"
            + " x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537"
            + ".36&RemoteIP=157.25.20.226&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen"
            + ".de/s-anzeige/johnson-evinrude-50-ps-2-takt-aussenborder-startet-nicht-defekt"
            + "/1394369253-211-18314?utm_source=focus.de&utm_medium=cpc&utm_campaign=coop-focus"
            + ".de"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=cadid%3D1394369253%26reg%3DHarburg%2520-%2520Hamburg"
            + "%2520Marmstorf%26rurl%3Dhttps%25253A%25252F%25252Fkleinanzeige.focus"
            + ".de%25252Fboote%25252Fboot-defekt"
            + ".html%25253Futm_campaign%25253D12simplifiedalert%252526utm_medium%25253Demail"
            + "%252526utm_source%25253Decosa-pl%252526utm_emailab%25253DmaxAdNo_A"
            + "%252526subscriptionid%25253DY2DISft0Dr7tDhCqf7OBrA%25253D%25253D%252526utm_content"
            + "%25253Dbuttonviewlinkseconda%252526s%25253Ddesc%252526landingpageindex"
            + "%25253D1161597082&mpt=1588741781586&imp=1605053");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(2065185079L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(
        "https://www.ebay-kleinanzeigen.de/s-anzeige/johnson-evinrude-50-ps-2-takt-aussenborder"
            + "-startet-nicht-defekt/1394369253-211-18314?utm_source=focus"
            + ".de&utm_medium=cpc&utm_campaign=coop-focus.de");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("df842c331710a9e1e1c2f4f8c53e1b82");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605053);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:12:28.604")));
    ubiEvent.setRemoteIP("157.25.20.226");
    ubiEvent.setRequestCorrelationId("91d145e8bdfe7bf2");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ctofthu%60t*036743%3F-171e86644c4-0x110");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("157.25.20.226");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
            + " Chrome/81.0.4044.138 Safari/537.36");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(466503287910L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "cadid=1376051830&c=1&g=df842c331710a9e1e1c2f4f8c53e1b82&rurl=https%25253A%25252F"
            + "%25252Fkleinanzeige.focus.de%25252Fboote%25252Fboot-defekt"
            + ".html%25253Futm_campaign%25253D12simplifiedalert%252526utm_medium%25253Demail"
            + "%252526utm_source%25253Decosa-pl%252526utm_emailab%25253DmaxAdNo_A"
            + "%252526subscriptionid%25253DY2DISft0Dr7tDhCqf7OBrA%25253D%25253D%252526utm_content"
            + "%25253Dbuttonviewlinkseconda%252526s%25253Ddesc%252526landingpageindex"
            + "%25253D1161597082&h=33&px=4249&chnl=9&n=ca864df11710a9b13a7707eefdcf1bc7&uc=163&p"
            + "=1605053&uaid=e86644bd1710a9c9d6c06ee8c2d7e905S0&bs=212&ref=https%3A%2F%2Fwww"
            + ".ebay-kleinanzeigen.de%2Fs-anzeige%2Fjohnson-4-0-deluxe-bootsmotor-defekt-an-bastler"
            + "%2F1376051830-211-3405%3Futm_source%3Dfocus"
            + ".de%26utm_medium%3Dcpc%26utm_campaign%3Dcoop-focus"
            + ".de&r=-759454744&t=77®=Berlin%2520-%2520Tempelhof&cflgs=EA**&ul=de-DE&pn=2&rq"
            + "=91d145e8bdfe7bf2&pagename=http://kleinanzeigen.ebay"
            + ".de/anzeigen/s-anzeige/dalmatinerwelpe-ohne-papiere-in-liebevolle-hnde/1098221&ciid"
            + "=ZkTDnWw*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D91d145e8bdfe7bf2%26node_id%3D6961676765ada90d%26REQUEST_GUID"
            + "%3D171e8664-4bc0-a9c9-d6c2-d30ae01fc18b%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A036743%253F-171e86644c4-0x110%26cal_mod%3Dfalse&TPool=r1rover&TDuration=10"
            + "&TStatus"
            + "=0&TType=URL&ContentLength=-1&ForwardedFor=157.25.20.226, 2.18.215"
            + ".207&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.156.157"
            + ".108&TStamp=22:12:28.60&TName=roverimp_INTL&Agent=Mozilla/5.0 (Windows NT 10.0;"
            + " Win64;"
            + " x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537"
            + ".36&RemoteIP=157.25.20.226&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen"
            + ".de/s-anzeige/johnson-4-0-deluxe-bootsmotor-defekt-an-bastler/1376051830-211-3405"
            + "?utm_source=focus.de&utm_medium=cpc&utm_campaign=coop-focus.de"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=cadid%3D1376051830%26reg%3DBerlin%2520-%2520Tempelhof%26rurl"
            + "%3Dhttps%25253A%25252F%25252Fkleinanzeige.focus.de%25252Fboote%25252Fboot-defekt"
            + ".html%25253Futm_campaign%25253D12simplifiedalert%252526utm_medium%25253Demail"
            + "%252526utm_source%25253Decosa-pl%252526utm_emailab%25253DmaxAdNo_A"
            + "%252526subscriptionid%25253DY2DISft0Dr7tDhCqf7OBrA%25253D%25253D%252526utm_content"
            + "%25253Dbuttonviewlinkseconda%252526s%25253Ddesc%252526landingpageindex"
            + "%25253D1161597082&mpt=1588741937463&imp=1605053");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(-759454744L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(
        "https://www.ebay-kleinanzeigen.de/s-anzeige/johnson-4-0-deluxe-bootsmotor-defekt-an"
            + "-bastler/1376051830-211-3405?utm_source=focus"
            + ".de&utm_medium=cpc&utm_campaign=coop-focus.de");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(24, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric10() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("9d5bf12216f0ad4d9333d413f9ceb862");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605051);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:59:55.862")));
    ubiEvent.setRemoteIP("46.87.40.174");
    ubiEvent.setRequestCorrelationId("3b3261b24926bc27");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0%3C05073-171e823d9e1-0x10b");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("46.87.40.174");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(879644170531L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=9d5bf12216f0ad4d9333d413f9ceb862&rurl=https%25253A%25252F%25252Fwww.google"
            + ".de%25252F&h=22&px=4249&chnl=9&n=9d5bf07916f0a9caf7f23b22fa191961&uc=77&p=1605051"
            + "&uaid"
            + "=e823d9d81710aadcecc04e2fc78e64f1S0&bs=77&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77&cflgs=EA**&ul=de-DE&pn=2&rq=3b3261b24926bc27&pagename=http"
            + "://kleinanzeigen.ebay.de/anzeigen/&ciid=I9ngzsw*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D3b3261b24926bc27%26node_id%3D7c81b77457206ecb%26REQUEST_GUID"
            + "%3D171e823d-9d60-aadc-ecc0-3c7ae1e1454c%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0"
            + "%253C05073-171e823d9e1-0x10b%26cal_mod%3Dfalse&TPool=r1rover&TDuration=12&TStatus=0"
            + "&TType=URL&ContentLength=-1&ForwardedFor=46.87.40"
            + ".174&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.173.206"
            + ".204&TStamp=20:59:55.86&TName=roverimp_INTL&Agent=Mozilla/5.0 (Windows NT 10.0;"
            + " Win64;"
            + " x64; rv:75.0) Gecko/20100101 Firefox/75.0&RemoteIP=46.87.40"
            + ".174&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=rurl%3Dhttps%25253A%25252F%25252Fwww.google"
            + ".de%25252F&mpt=1588737594409&imp=1605051");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(715462550L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("9d5bf12216f0ad4d9333d413f9ceb862");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605052);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:00:05.046")));
    ubiEvent.setRemoteIP("46.87.40.174");
    ubiEvent.setRequestCorrelationId("e723de5d78a6be4e");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0103566%29pqtfwpu%29pie%29fgg%7E-fij-171e823fdbe-0x172");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("46.87.40.174");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(238802566435L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=9d5bf12216f0ad4d9333d413f9ceb862&rurl=https%25253A%25252F%25252Fwww"
            + ".ebay-kleinanzeigen.de%25252F&h=22&px=4249&chnl=9&n=9d5bf07916f0a9caf7f23b22fa191961"
            + "&uc=77&p=1605052&uaid=e823fdb81710a9c99377d999c4fdbee0S0&bs=77&ref=https%3A%2F%2Fwww"
            + ".ebay-kleinanzeigen.de%2Fs-77960%2Fhebeb%25C3%25BChne%2Fk0l8037r100&r=1348758891&t"
            + "=77"
            + "®=77960%2520Seelbach&cflgs=EA**&ul=de-DE&pn=2&rq=e723de5d78a6be4e&pagename=http"
            + "://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=I%2F29mTc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De723de5d78a6be4e%26node_id%3Da4429d46da7bb780%26REQUEST_GUID"
            + "%3D171e823f-db60-a9c9-9377-07f0e1474a02%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0103566%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e823fdbe-0x172%26cal_mod%3Dfalse"
            + "&TPool=r1rover&TDuration=9&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=46.87"
            + ".40"
            + ".174, 84.53.140.22&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.156.153"
            + ".55&TStamp=21:00:05.04&TName=roverimp_INTL&Agent=Mozilla/5.0 (Windows NT 10.0;"
            + " Win64;"
            + " x64; rv:75.0) Gecko/20100101 Firefox/75.0&RemoteIP=46.87.40"
            + ".174&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen"
            + ".de/s-77960/hebeb%C3%BChne/k0l8037r100"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=reg%3D77960%2520Seelbach%26rurl%3Dhttps%25253A%25252F%25252Fwww"
            + ".ebay-kleinanzeigen.de%25252F&mpt=1588737603571&imp=1605052");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(1348758891L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/s-77960/hebeb%C3%BChne/k0l8037r100");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(8, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric11() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e82f3ecd1710ad4a92b40e31c0a09b72");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605051);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:12:22.599")));
    ubiEvent.setRemoteIP("77.2.118.43");
    ubiEvent.setRequestCorrelationId("1369cbce82212b3f");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ckuvthu%60t*023271%3E%29pqtfwpu%29osu%29fgg%7E-fij-171e82f3ed4-0x1aa");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("77.2.118.43");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
            + " Chrome/81.0.4044.129 Safari/537.36");
    ubiEvent.setCobrand(5);
    ubiEvent.setCurrentImprId(187532787247L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=e82f3ecd1710ad4a92b40e31c0a09b72&rurl=https%25253A%25252F%25252Fwww.google"
            + ".com%25252F&h=cd&px=4249&chnl=9&uc=77&p=1605051&uaid"
            + "=e82f3ecd1710ad4a92b40e31c0a09b71S0&bs=77&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77&cflgs=QA**&ul=de-DE&pn=2&rq=1369cbce82212b3f&pagename=http"
            + "://kleinanzeigen.ebay.de/anzeigen/&ciid=Lz7TqSs*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D1369cbce82212b3f%26node_id%3Dbbae358a054a173c%26REQUEST_GUID"
            + "%3D171e82f3-ec70-ad4a-92b7-46fede3c0324%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t"
            + "%2A023271%253E%2529pqtfwpu%2529osu%2529fgg%257E-fij-171e82f3ed4-0x1aa%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=14&TStatus=0&TType=URL&ContentLength=-1"
            + "&ForwardedFor"
            + "=77.2.118.43, 92.123.212.78&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10"
            + ".212.169.43&TStamp=21:12:22.59&TName=roverimp_INTL&Agent=Mozilla/5.0 "
            + "(Windows NT 10.0;"
            + " Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537"
            + ".36&RemoteIP=77.2.118.43&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=rurl%3Dhttps%25253A%25252F%25252Fwww.google"
            + ".com%25252F&mpt=1588738344585&imp=1605051");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(715462550L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay-kleinanzeigen.de/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(8, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric12() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(true);
    ubiEvent.setPageId(3084);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:55.939")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("5e27b9069663d175");
    ubiEvent.setSid("e11050.m44.l3478");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0114002%29pqtfwpu%29pie%29fgg%7E-fij-171e7acd566-0x150");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(970870609324L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "rdt=1&segname=11050&osub=-1%7E1&nid=&uc=1&url_mpre=http%3A%2F%2Fmesgmy.ebay"
            + ".com%2Fws%2FeBayISAPI.dll%3FViewMyMessageDetails%26View%3DMyMessageDetails"
            + "%26CurrentPage%3DMyeBayMyMessageDetails%26SubmitAction"
            + ".Show%3Dx%26emailUniqueId%3Ddb64bcce16c24d7eba2d9bb82cf38396%26sourceImagePreview"
            + "%3DImagePreview&bs=0&uaid=e7acd5651710aad0ce20fe9fc5255643S0&bu=43103000438&crd"
            + "=20200505181620&ul=en-US&hrc=301&pagename=EntryTracking&c=1&ch=osgood&g"
            + "=ae70db601680aca524931354fe0d8632&h=60&cguidsrc=cookie&n"
            + "=ae70db601680aca524931354fe0d8630&url=http%3A%2F%2Fmesgmy.ebay.com%2Fws%2FeBayISAPI"
            + ".dll%3FViewMyMessageDetails%26View%3DMyMessageDetails%26CurrentPage"
            + "%3DMyeBayMyMessageDetails%26SubmitAction"
            + ".Show%3Dx%26emailUniqueId%3Ddb64bcce16c24d7eba2d9bb82cf38396%26sourceImagePreview"
            + "%3DImagePreview&p=3084&t=0&u=119173659&pcguid=ae70db601680aca524931354fe0d8630&pn"
            + "=2&rq"
            + "=5e27b9069663d175&euid=db64bcce16c24d7eba2d9bb82cf38396&chnl=7&ciid=rNVlDOI*&sid"
            + "=e11050.m44.l3478");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D5e27b9069663d175%26node_id%3D6e473995ee27c98c%26REQUEST_GUID"
            + "%3D171e7acd-5630-aad0-ce26-e92ee15c895e%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0114002%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e7acd566-0x150%26cal_mod%3Dfalse"
            + "&TPool=r1rover&TDuration=5&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=206.126"
            + ".218.39&Script=/rover/0/e11050.m44.l3478/7&Server=rover.ebay.com&TMachine=10.173.12"
            + ".226&TStamp=18:49:55.93&TName=rover&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39&Encoding=gzip,"
            + " deflate, br"));
    ubiEvent.setUrlQueryString(
        "/rover/0/e11050.m44.l3478/7?osub=-1%7E1&crd=20200505181620&ul_ref=https%253A%252F"
            + "%252Frover.ebay.com%252Frover%252F0%252F0%252F99%253Floc%253Dhttps%25253A%25252F"
            + "%25252Frover.ebay.com%25252Frover%25252F0%25252Fe11050.m44"
            + ".l3478%25252F7%25253Feuid%25253Ddb64bcce16c24d7eba2d9bb82cf38396%252526bu"
            + "%25253D43103000438%252526segname%25253D11050%252526crd%25253D20200505181620"
            + "%252526osub"
            + "%25253D-1%25257E1%252526ch%25253Dosgood%252526loc%25253Dhttp%2525253A%2525252F"
            + "%2525252Fmesgmy.ebay.com%2525252Fws%2525252FeBayISAPI"
            + ".dll%2525253FViewMyMessageDetails%25252526View%2525253DMyMessageDetails"
            + "%25252526CurrentPage%2525253DMyeBayMyMessageDetails%25252526SubmitAction"
            + ".Show%2525253Dx%25252526emailUniqueId%2525253Ddb64bcce16c24d7eba2d9bb82cf38396"
            + "%25252526sourceImagePreview%2525253DImagePreview%252526sojTags%25253Dbu%25253Dbu"
            + "%25252Cch%25253Dch%25252Csegname%25253Dsegname%25252Ccrd%25253Dcrd%25252Curl"
            + "%25253Dloc"
            + "%25252Cosub%25253Dosub%2526rvr_id%253D0%2526rvr_ts"
            + "%253De7acd5071710aad93553cf8dff935ec5&loc=http%3A%2F%2Fmesgmy.ebay"
            + ".com%2Fws%2FeBayISAPI.dll%3FViewMyMessageDetails%26View%3DMyMessageDetails"
            + "%26CurrentPage%3DMyeBayMyMessageDetails%26SubmitAction"
            + ".Show%3Dx%26emailUniqueId%3Ddb64bcce16c24d7eba2d9bb82cf38396%26sourceImagePreview"
            + "%3DImagePreview&sojTags=bu%3Dbu%2Cch%3Dch%2Csegname%3Dsegname%2Ccrd%3Dcrd%2Curl"
            + "%3Dloc"
            + "%2Cosub%3Dosub&ch=osgood&segname=11050&bu=43103000438&euid"
            + "=db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("rover");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:57.275")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("a5b36147201efe9e");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Fjqpvgig%285%3E%3A%3C%3F1%3B-171e7acda99-0x300a");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(1049678502572L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&sameid=a5bd37b39b964a7c924136392f266fc8&sapcxkw=&schemaversion=3&pagename"
            + "=SandPage&saucxgdpry=false&efam=SAND&ac=119173659&saucxgdprct=false&saty=1&g"
            + "=ae70db601680aca524931354fe0d8632&saebaypid=100603&h=60&salv=5&n"
            + "=ae70db601680aca524931354fe0d8630&ciid=rNq2ZfQ*&p=2367355&sapcxcat=&t=0&saiid"
            + "=ea65e87c"
            + "-094d-4775-9037-b71557cb9908&u=119173659&cflgs=AA**&samslid=2940%2C8150%2C8165"
            + "%2C7460"
            + "%2C8085%2C8080%2C7425%2C7690%2C8075%2C4422%2C4428%2C9240%2C4302%2C216%2C9225%2C3438"
            + "%2C8065%2C8170%2C3450%2C4434%2C6565%2C6940%2C5270%2C8105%2C2868%2C3126%2C8090%2C4416"
            + "%2C3660%2C8095%2C8110%2C6585%2C8120%2C7470%2C4296%2C7450%2C5435%2C8160%2C7420%2C6170"
            + "%2C8725%2C7475%2C8060%2C9220%2C5420%2C7655%2C7530%2C7520%2C8115%2C8070%2C6595%2C6155"
            + "%2C4242%2C2436%2C8130&eactn=AUCT&pn=2&rq=a5b36147201efe9e&ciid=rNq2ZfQ*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Da5b36147201efe9e%26node_id%3D76b9d2c791b5c4c2%26REQUEST_GUID"
            + "%3D171e7acd-a9a0-aa46-5f46-da68fde2f93c%26logid%3Dt6pdhc9%253Fjqpvgig%25285%253E"
            + "%253A"
            + "%253C%253F1%253B-171e7acda99-0x300a&TPool=r1sand&TDuration=28&ContentLength=522"
            + "&ForwardedFor=10.164.102.228&Script=sand&Server=sand.stratus.ebay.com&TMachine=10"
            + ".164"
            + ".101.244&TStamp=18:49:57.27&TName=sand.v1&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39"));
    ubiEvent.setUrlQueryString(
        "/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(3962);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:57.673")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("0864598f25bbb0d4");
    ubiEvent.setSid("e11050");
    ubiEvent.setRlogid("t6qjpbq%3F%3Ctofthu%60t*0310322-171e7acdc2c-0x10b");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(866596412588L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "c=3&euid=db64bcce16c24d7eba2d9bb82cf38396&segname=11050&osub=-1%7E1&ch=osgood&g"
            + "=ae70db601680aca524931354fe0d8632&h=60&chnl=7&n=ae70db601680aca524931354fe0d8630"
            + "&uc=1"
            + "&sid=e11050&p=3962&uaid=e7acdc2b1710a9cc5c93b752c2edc589S0&bs=0&bu=43103000438&crd"
            + "=20200505181620&t=0&u=119173659&ul=en-US&pn=2&rq=0864598f25bbb0d4&pagename=roveropen"
            + "&ciid=rNwrxck*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0864598f25bbb0d4%26node_id%3D23cb4714aef571ea%26REQUEST_GUID"
            + "%3D171e7acd-c290-a9cc-5c93-b895e02b843f%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0310322-171e7acdc2c-0x10b%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0"
            + "&TType=URL&ContentLength=-1&ForwardedFor=206.126.218"
            + ".39&Script=/roveropen/0/e11050/7&Server=rover.ebay.com&TMachine=10.156.197"
            + ".201&TStamp=18:49:57.67&TName=roveropen&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39&Encoding=gzip,"
            + " deflate, br"));
    ubiEvent.setUrlQueryString(
        "/roveropen/0/e11050/7?osub=-1%7E1&crd=20200505181620&sojTags=bu%3Dbu%2Cch%3Dch%2Csegname"
            + "%3Dsegname%2Ccrd%3Dcrd%2Cosub%3Dosub&ch=osgood&segname=11050&bu=43103000438&euid"
            + "=db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("roveropen");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(3);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(true);
    ubiEvent.setPageId(5038);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:56.158")));
    ubiEvent.setRemoteIP("10.21.240.19");
    ubiEvent.setRequestCorrelationId(null);
    ubiEvent.setSid("p2065413");
    ubiEvent.setRlogid(null);
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("10.21.240.19");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(86409860780L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("mesgmy.ebay.com");
    ubiEvent.setApplicationPayload(
        "c=3&euid=db64bcce16c24d7eba2d9bb82cf38396&segname=11050&osub=-1%7E1&ch=osgood&g"
            + "=ae70db601680aca524931354fe0d8632&h=60&chnl=7&n=ae70db601680aca524931354fe0d8630"
            + "&uc=1"
            + "&sid=e11050&p=3962&uaid=e7acdc2b1710a9cc5c93b752c2edc589S0&bs=0&bu=43103000438&crd"
            + "=20200505181620&t=0&u=119173659&ul=en-US&pn=2&rq=0864598f25bbb0d4&pagename=roveropen"
            + "&ciid=rNwrxck*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0864598f25bbb0d4%26node_id%3D23cb4714aef571ea%26REQUEST_GUID"
            + "%3D171e7acd-c290-a9cc-5c93-b895e02b843f%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0310322-171e7acdc2c-0x10b%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0"
            + "&TType=URL&ContentLength=-1&ForwardedFor=206.126.218"
            + ".39&Script=/roveropen/0/e11050/7&Server=rover.ebay.com&TMachine=10.156.197"
            + ".201&TStamp=18:49:57.67&TName=roveropen&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39&Encoding=gzip,"
            + " deflate, br"));
    ubiEvent.setUrlQueryString(
        "/roveropen/0/e11050/7?osub=-1%7E1&crd=20200505181620&sojTags=bu%3Dbu%2Cch%3Dch%2Csegname"
            + "%3Dsegname%2Ccrd%3Dcrd%2Cosub%3Dosub&ch=osgood&segname=11050&bu=43103000438&euid"
            + "=db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("ViewMyMessageDetailsMyeBayMyMessageDetails");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2512161);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:58.390")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId(null);
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6diiebinbbacuewiiw%60hc9%3Fuk%60bjhadjofdbbqcthmqcig(5561504-171e7acde86-0x2504");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(1010568715948L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "uit=1576674616225&nqc"
            +
            "=UAAAAAAAgAQAAAAAAAAAAAAAAAAgBAAAAIAAAAAAAQAAACAAEAAAIAAAAAAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAhBCA*&c=3&g=ae70db601680aca524931354fe0d8632&h=60&n=ae70db601680aca524931354fe0d8630&uc=1&es=0&nqt=UAAAAAAAgAQAAAAAAAAAAAAAAAAgBAAAAIAAAAAAAQAAACAAEAAAIAAAAAAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAhBCA*&p=2512161&bs=0&r=-1552470496&bu=119173659&t=0&ul=en-US&ec=2&pn=0&pagename=globalheaderfrontend__GHUserAcquisition&ciid=rN6WSus*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De7acde8a1710a9c96c91178fff151ff8%26node_id%3D%26REQUEST_GUID"
            + "%3Dae70db601680aca524931354fe0d8632%26logid%3Dt6diiebinbbacuewiiw%2560hc9%253Fuk"
            + "%2560bjhadjofdbbqcthmqcig%285561504-171e7acde86-0x2504%26statusCode%3D200&TPool"
            + "=r1globalheaderfrontend&TStatus=0&TType=URL&ContentLength=0&ForwardedFor=206.126.218"
            + ".39, 96.17.11.86,209.211.216.172&Script=/gh/useracquisition&Server=www.ebay"
            + ".com&TMachine=10.156.150.201&TStamp=18:49:58"
            + ".39&TName=GHUserAcquisition&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218"
            + ".39&Encoding=gzip&Referer=https://mesg.ebay"
            + ".com/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396"));
    ubiEvent.setUrlQueryString(
        "/gh/useracquisition?correlation=operationId=2065413&modules_groups"
            + "=UserAcquisitionModules&multi_account_login=0&acting_on_diff_acct=0");
    ubiEvent.setPageName("GHUserAcquisition");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(3);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(-1552470496L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(
        "https://mesg.ebay.com/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 6
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:57.275")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("a5b36147201efe9e");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Fjqpvgig%285%3E%3A%3C%3F1%3B-171e7acda99-0x300a");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(1049678502572L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&sameid=308b537ae8024bae92e4e88b43e6a27d&sapcxkw=&schemaversion=3&pagename"
            + "=SandPage&saucxgdpry=false&efam=SAND&ac=119173659&saucxgdprct=false&saty=1&g"
            + "=ae70db601680aca524931354fe0d8632&saebaypid=100602&h=60&salv=5&n"
            + "=ae70db601680aca524931354fe0d8630&ciid=rNq2ZfQ*&p=2367355&sapcxcat=&t=0&saiid"
            + "=6389a697"
            + "-8861-4473-b290-c10c506cc707&u=119173659&cflgs=AA**&samslid=2940%2C8150%2C8165"
            + "%2C7460"
            + "%2C8085%2C8080%2C7425%2C7690%2C8075%2C4422%2C4428%2C9240%2C4302%2C216%2C9225%2C3438"
            + "%2C8065%2C8170%2C3450%2C4434%2C6565%2C6940%2C5270%2C8105%2C2868%2C3126%2C8090%2C4416"
            + "%2C3660%2C8095%2C8110%2C6585%2C8120%2C7470%2C4296%2C7450%2C5435%2C8160%2C7420%2C6170"
            + "%2C8725%2C7475%2C8060%2C9220%2C5420%2C7655%2C7530%2C7520%2C8115%2C8070%2C6595%2C6155"
            + "%2C4242%2C2436%2C8130&eactn=AUCT&pn=2&rq=a5b36147201efe9e&ciid=rNq2ZfQ*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Da5b36147201efe9e%26node_id%3D76b9d2c791b5c4c2%26REQUEST_GUID"
            + "%3D171e7acd-a9a0-aa46-5f46-da68fde2f93c%26logid%3Dt6pdhc9%253Fjqpvgig%25285%253E"
            + "%253A"
            + "%253C%253F1%253B-171e7acda99-0x300a&TPool=r1sand&TDuration=28&ContentLength=522"
            + "&ForwardedFor=10.164.102.228&Script=sand&Server=sand.stratus.ebay.com&TMachine=10"
            + ".164"
            + ".101.244&TStamp=18:49:57.27&TName=sand.v1&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39"));
    ubiEvent.setUrlQueryString(
        "/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 7
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2046301);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:57.836")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("e871c850b060d17b");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0111407%29pqtfwpu%29pie%29fgg%7E-fij-171e7acdcd4-0x196");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(503649131692L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "c=3&g=ae70db601680aca524931354fe0d8632&h=60&px=4249&ghi=98&chnl=9&cp=2065413&n"
            + "=ae70db601680aca524931354fe0d8630&uc=1&p=2046301&uaid"
            + "=e7acdccd1710a9943751c131c2753d17S0&bs=0&ref=https%3A%2F%2Fmesg.ebay"
            + ".com%2Fmesgweb%2FViewMessageDetail%2Femail%2F0%2Fdb64bcce16c24d7eba2d9bb82cf38396&r"
            + "=-1552470496&t=0&u=119173659&ul=en-US&callingpagename=mesg__ViewMessageDetail&pn=2"
            + "&rq"
            + "=e871c850b060d17b&pagename=GH_impressions&ciid=rNzTQ3U*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De871c850b060d17b%26node_id%3D6c48812d45e29837%26REQUEST_GUID"
            + "%3D171e7acd-ccc0-a994-3755-9548dfeae7e6%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0111407%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e7acdcd4-0x196%26cal_mod%3Dfalse"
            + "&TPool=r1rover&TDuration=8&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=206.126"
            + ".218.39&Script=/roverimp/0/0/9&Server=rover.ebay.com&TMachine=10.153.67"
            + ".117&TStamp=18:49:57.83&TName=roverimp&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39&Encoding=gzip,"
            + " deflate, br&Referer=https://mesg.ebay"
            + ".com/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=cp%3D2065413%26ghi%3D98&imp=2046301&1588729797673=");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(3);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(-1552470496L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(
        "https://mesg.ebay.com/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 8
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(true);
    ubiEvent.setPageId(3084);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:55.844")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("bcd1386038d2c62d");
    ubiEvent.setSid("p2065413");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*01166%3D3%29pqtfwpu%29pie%29fgg%7E-fij-171e7acd507-0x19a");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(367538918828L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "rdt=1&c=1&g=ae70db601680aca524931354fe0d8632&h=60&cguidsrc=cookie&n"
            + "=ae70db601680aca524931354fe0d8630&uc=1&url_mpre=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F0%2Fe11050.m44.l3478%2F7%3Feuid%3Ddb64bcce16c24d7eba2d9bb82cf38396"
            + "%26bu"
            + "%3D43103000438%26segname%3D11050%26crd%3D20200505181620%26osub%3D-1%7E1%26ch"
            + "%3Dosgood"
            + "%26loc%3Dhttp%253A%252F%252Fmesgmy.ebay.com%252Fws%252FeBayISAPI"
            + ".dll%253FViewMyMessageDetails%2526View%253DMyMessageDetails%2526CurrentPage"
            + "%253DMyeBayMyMessageDetails%2526SubmitAction"
            + ".Show%253Dx%2526emailUniqueId%253Ddb64bcce16c24d7eba2d9bb82cf38396"
            + "%2526sourceImagePreview%253DImagePreview%26sojTags%3Dbu%3Dbu%2Cch%3Dch%2Csegname"
            + "%3Dsegname%2Ccrd%3Dcrd%2Curl%3Dloc%2Cosub%3Dosub&p=3084&uaid"
            + "=e7acd5061710aad93557c810c30b4e9bS0&bs=0&t=0&u=119173659&ul=en-US&hrc=301&pn=2"
            + "&pcguid"
            + "=ae70db601680aca524931354fe0d8630&rq=bcd1386038d2c62d&pagename=EntryTracking&ciid"
            + "=rNUGk1U*&sid=p2065413");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dbcd1386038d2c62d%26node_id%3D683aaf3a7c18cd89%26REQUEST_GUID"
            + "%3D171e7acd-5040-aad9-3557-9dbce03a3e75%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A01166%253D3%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e7acd507-0x19a%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=4&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=206.126.218.39&Script=/rover/0/0/99&Server=rover.ebay.com&TMachine=10.173.147"
            + ".85&TStamp=18:49:55.84&TName=rover&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39&Encoding=gzip,"
            + " deflate, br"));
    ubiEvent.setUrlQueryString(
        "/rover/0/0/99?loc=https%3A%2F%2Frover.ebay.com%2Frover%2F0%2Fe11050.m44"
            + ".l3478%2F7%3Feuid%3Ddb64bcce16c24d7eba2d9bb82cf38396%26bu%3D43103000438%26segname"
            + "%3D11050%26crd%3D20200505181620%26osub%3D-1%7E1%26ch%3Dosgood%26loc%3Dhttp%253A%252F"
            + "%252Fmesgmy.ebay.com%252Fws%252FeBayISAPI"
            + ".dll%253FViewMyMessageDetails%2526View%253DMyMessageDetails%2526CurrentPage"
            + "%253DMyeBayMyMessageDetails%2526SubmitAction"
            + ".Show%253Dx%2526emailUniqueId%253Ddb64bcce16c24d7eba2d9bb82cf38396"
            + "%2526sourceImagePreview%253DImagePreview%26sojTags%3Dbu%3Dbu%2Cch%3Dch%2Csegname"
            + "%3Dsegname%2Ccrd%3Dcrd%2Curl%3Dloc%2Cosub%3Dosub");
    ubiEvent.setPageName("rover");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 9
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:57.270")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("a5b36147201efe9e");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Fuk%60vgig%280%3Fesr*w%60ut3522-171e7acda96-0x3dbe");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(289300470700L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&sagooglssa=false&saaoluc=UPfdb71cdb-f91b-11e9-b40d-0eb5e2a831ee&saaoluco=1"
            + "&saidpubid=183403&saidxuc=XbY5v9HM6GYAACkpBH8AAAB9ACoAAAIB&saanxuco=1&sarbcuc"
            + "=K29P8DLS"
            + "-4-9W20&sagoogltagid=21663075830&sapbmsid=165303&saopxpubid=537121625&efam=SAND&ac"
            + "=119173659&sarbcuco=1&saucxgdprct=false&ciid=rNumW0M*&sapbmuc=B1CAD7C7-8803-4545"
            + "-9782"
            + "-DC085A2AA780&sapcxcat=&saopxuc=a61e2ec0-60cd-8cf1-9e03-e86ae044ec9f&saiid=68e43cd8"
            + "-287a-4417-9349-df29c7620c0b&sarbctid=650078&cflgs=AA**&sapbmpubid=156009&samslid"
            + "=2940"
            + "%2C8150%2C8165%2C7460%2C8085%2C8080%2C7425%2C7690%2C8075%2C4422%2C4428%2C9240%2C4302"
            + "%2C216%2C9225%2C3438%2C8065%2C8170%2C3450%2C4434%2C6565%2C6940%2C5270%2C8105%2C2868"
            + "%2C3126%2C8090%2C4416%2C3660%2C8095%2C8110%2C6585%2C8120%2C7470%2C4296%2C7450%2C5435"
            + "%2C8160%2C7420%2C6170%2C8725%2C7475%2C8060%2C9220%2C5420%2C7655%2C7530%2C7520%2C8115"
            + "%2C8070%2C6595%2C6155%2C4242%2C2436%2C8130&eactn=AUCT&saanxpid=9794063&rq"
            + "=a5b36147201efe9e&sagoogluc=CAESEE3x9R7nk1V93EFwDmV44_o&sameid"
            + "=2d781559989d4b129f0c635b4cc7cd04&sapcxkw=&schemaversion=3&sapbmuco=1&sagooglsid"
            + "=21663075830009&saflps=Config&saaolpubid=12005&saanxuc=2846386642456817880&sapbmtid"
            + "=851352&!sbids=sbid%3A1%2Csbe%3A0.12%2Csbbid%3A%2Csbaid%3Asling"
            + ".com%2Csbdid%3A1037%2Csbsid%3AAol%2Csbadid%3A%2Csbdlid%3A%2Csbcid%3A1037-2979"
            + "-9x5ca3gn"
            + "%2Csbt%3A1%2Csbadbts%3A342%2Csbfa%3Asling"
            + ".com%2Csbrp%3A%2Csbs%3A1037%2Csblurl%3A&saidxuco=1&saflp=0"
            + ".8&pagename=SandPage&saucxgdpry=false&salr=100&sagoogluco=1&saanxmid=7208&saaolsid"
            + "=345487&saty=1&saebaypid=100858&g=ae70db601680aca524931354fe0d8632&h=60&saopxuco=1"
            + "&salv=5&n=ae70db601680aca524931354fe0d8630&saidtid=29011&p=2367355&t=0&u=119173659"
            + "&sarbcpubid=11440&sarbcsid=138876&saopxtid=538587091&pn=2&ciid=rNumW0M*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Da5b36147201efe9e%26node_id%3D9e9d95592343ee03%26REQUEST_GUID"
            + "%3D171e7acd-a960-a1b5-b434-34d2fcc03cac%26logid%3Dt6pdhc9%253Fuk%2560vgig%25280"
            + "%253Fesr%2Aw%2560ut3522-171e7acda96-0x3dbe&TPool=r1sand&TDuration=272&ContentLength"
            + "=519&ForwardedFor=10.164.102.228&Script=sand&Server=sand.stratus.ebay"
            + ".com&TMachine=10"
            + ".27.91.67&TStamp=18:49:57.27&TName=sand.v1&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39"));
    ubiEvent.setUrlQueryString(
        "/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 10
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:57.270")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("a5b36147201efe9e");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Fuk%60vgig%280%3Fesr*w%60ut3522-171e7acda96-0x3dbe");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(289300470700L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&sagooglssa=false&saaoluc=UPfdb71cdb-f91b-11e9-b40d-0eb5e2a831ee&saaoluco=1"
            + "&saidpubid=190136&saidxuc=XbY5v9HM6GYAACkpBH8AAAB9ACoAAAIB&saanxuco=1&sarbcuc"
            + "=K29P8DLS"
            + "-4-9W20&sacriteotagid=300x250_Footer_1_BTF_PB&sagoogltagid=21663075830&sapbmsid"
            + "=165303"
            + "&saopxpubid=537121625&efam=SAND&ac=119173659&sarbcuco=1&saucxgdprct=false&sacriteouc"
            + "=31238024-5ccb-4531-bb99-f648dd6b8398&ciid=rNumW0M*&sapbmuc=B1CAD7C7-8803-4545-9782"
            + "-DC085A2AA780&sapcxcat=&saopxuc=a61e2ec0-60cd-8cf1-9e03-e86ae044ec9f&saiid=74a63b81"
            + "-82dc-4b15-ad0f-7de691c3ff61&sarbctid=650068&cflgs=AA**&sapbmpubid=156009&samslid"
            + "=2940"
            + "%2C8150%2C8165%2C7460%2C8085%2C8080%2C7425%2C7690%2C8075%2C4422%2C4428%2C9240%2C4302"
            + "%2C216%2C9225%2C3438%2C8065%2C8170%2C3450%2C4434%2C6565%2C6940%2C5270%2C8105%2C2868"
            + "%2C3126%2C8090%2C4416%2C3660%2C8095%2C8110%2C6585%2C8120%2C7470%2C4296%2C7450%2C5435"
            + "%2C8160%2C7420%2C6170%2C8725%2C7475%2C8060%2C9220%2C5420%2C7655%2C7530%2C7520%2C8115"
            + "%2C8070%2C6595%2C6155%2C4242%2C2436%2C8130&eactn=AUCT&saanxpid=10492943&rq"
            + "=a5b36147201efe9e&sagoogluc=CAESEE3x9R7nk1V93EFwDmV44_o&sameid"
            + "=0ee1e76410a44207af948f6559bd68ed&sapcxkw=&schemaversion=3&sacriteopubid=186706"
            + "&sapbmuco=1&sagooglsid=21663075830031&saflps=Config&saaolpubid=12005&saanxuc"
            + "=2846386642456817880&sapbmtid=851353&!sbids=sbid%3A5eb217c552cb87897654246adb6e8000"
            + "%2Csbe%3A0.03%2Csbbid%3A%2Csbaid%3A%2Csbdid%3A%2Csbsid%3ACriteo%2Csbadid%3A52484"
            + "%2Csbdlid%3A%2Csbcid%3A10722378%2Csbt%3A1%2Csbadbts%3A716%2Csbfa%3Apandora"
            + ".com%2Csbrp%3A%2Csbs%3Acriteo-global%2Csblurl%3A&saidxuco=1&saflp=0"
            + ".8&pagename=SandPage&saucxgdpry=false&salr=100&sagoogluco=1&saanxmid=7208&saaolsid"
            + "=346780&saty=1&saebaypid=100859&g=ae70db601680aca524931354fe0d8632&h=60&saopxuco=1"
            + "&salv=5&n=ae70db601680aca524931354fe0d8630&saidtid=30387&p=2367355&t=0&u=119173659"
            + "&sarbcpubid=11440&sarbcsid=138868&saopxtid=538715644&sacriteouco=1&pn=2&ciid"
            + "=rNumW0M*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Da5b36147201efe9e%26node_id%3D9e9d95592343ee03%26REQUEST_GUID"
            + "%3D171e7acd-a960-a1b5-b434-34d2fcc03cac%26logid%3Dt6pdhc9%253Fuk%2560vgig%25280"
            + "%253Fesr%2Aw%2560ut3522-171e7acda96-0x3dbe&TPool=r1sand&TDuration=272&ContentLength"
            + "=519&ForwardedFor=10.164.102.228&Script=sand&Server=sand.stratus.ebay"
            + ".com&TMachine=10"
            + ".27.91.67&TStamp=18:49:57.27&TName=sand.v1&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39"));
    ubiEvent.setUrlQueryString(
        "/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 11
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:57.270")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("a5b36147201efe9e");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Fuk%60vgig%280%3Fesr*w%60ut3522-171e7acda96-0x3dbe");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(289300470700L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&saaoluc=UPfdb71cdb-f91b-11e9-b40d-0eb5e2a831ee&saaoluco=1&saidpubid=190137"
            + "&saidxuc=XbY5v9HM6GYAACkpBH8AAAB9ACoAAAIB&saanxuco=1&sarbcuc=K29P8DLS-4-9W20"
            + "&sacriteotagid=300x250_Footer_2_BTF_PB&sapbmsid=197531&saopxpubid=537121625&efam"
            + "=SAND"
            + "&ac=119173659&sarbcuco=1&saucxgdprct=false&sacriteouc=31238024-5ccb-4531-bb99"
            + "-f648dd6b8398&ciid=rNumW0M*&sapbmuc=B1CAD7C7-8803-4545-9782-DC085A2AA780&sapcxcat"
            + "=&saopxuc=a61e2ec0-60cd-8cf1-9e03-e86ae044ec9f&saiid=0bf163fa-fc5b-486c-8618"
            + "-da6d2fcf565e&sarbctid=650070&cflgs=AA**&sapbmpubid=156009&samslid=2940%2C8150"
            + "%2C8165"
            + "%2C7460%2C8085%2C8080%2C7425%2C7690%2C8075%2C4422%2C4428%2C9240%2C4302%2C216%2C9225"
            + "%2C3438%2C8065%2C8170%2C3450%2C4434%2C6565%2C6940%2C5270%2C8105%2C2868%2C3126%2C8090"
            + "%2C4416%2C3660%2C8095%2C8110%2C6585%2C8120%2C7470%2C4296%2C7450%2C5435%2C8160%2C7420"
            + "%2C6170%2C8725%2C7475%2C8060%2C9220%2C5420%2C7655%2C7530%2C7520%2C8115%2C8070%2C6595"
            + "%2C6155%2C4242%2C2436%2C8130&eactn=AUCT&saanxpid=10492944&rq=a5b36147201efe9e&sameid"
            + "=539a0e1bada245549c34498c760866e8&sapcxkw=&schemaversion=3&sacriteopubid=186706"
            + "&sapbmuco=1&saflps=Config&saaolpubid=12005&saanxuc=2846386642456817880&sapbmtid"
            + "=1087103&!sbids=sbid%3A5eb217c50efe3b498b6442d6b330ed00%2Csbe%3A0"
            + ".03%2Csbbid%3A%2Csbaid%3A%2Csbdid%3A%2Csbsid%3ACriteo%2Csbadid%3A52484%2Csbdlid%3A"
            + "%2Csbcid%3A10722378%2Csbt%3A1%2Csbadbts%3A727%2Csbfa%3Apandora"
            + ".com%2Csbrp%3A%2Csbs%3Acriteo-global%2Csblurl%3A&saidxuco=1&saflp=0"
            + ".85&pagename=SandPage&saucxgdpry=false&salr=100&saanxmid=7208&saaolsid=346781&saty=1"
            + "&saebaypid=100860&g=ae70db601680aca524931354fe0d8632&h=60&saopxuco=1&salv=5&n"
            + "=ae70db601680aca524931354fe0d8630&saidtid=30388&p=2367355&t=0&u=119173659&sarbcpubid"
            + "=11440&sarbcsid=138870&saopxtid=538715645&sacriteouco=1&pn=2&ciid=rNumW0M*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Da5b36147201efe9e%26node_id%3D9e9d95592343ee03%26REQUEST_GUID"
            + "%3D171e7acd-a960-a1b5-b434-34d2fcc03cac%26logid%3Dt6pdhc9%253Fuk%2560vgig%25280"
            + "%253Fesr%2Aw%2560ut3522-171e7acda96-0x3dbe&TPool=r1sand&TDuration=272&ContentLength"
            + "=519&ForwardedFor=10.164.102.228&Script=sand&Server=sand.stratus.ebay"
            + ".com&TMachine=10"
            + ".27.91.67&TStamp=18:49:57.27&TName=sand.v1&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39"));
    ubiEvent.setUrlQueryString(
        "/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 12
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:57.270")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("a5b36147201efe9e");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Fuk%60vgig%280%3Fesr*w%60ut3522-171e7acda96-0x3dbe");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(289300405164L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&saaoluc=UPfdb71cdb-f91b-11e9-b40d-0eb5e2a831ee&saaoluco=1&saidpubid=190138"
            + "&saidxuc=XbY5v9HM6GYAACkpBH8AAAB9ACoAAAIB&saanxuco=1&sarbcuc=K29P8DLS-4-9W20"
            + "&sacriteotagid=300x250_Footer_3_BTF_PB&sapbmsid=197532&saopxpubid=537121625&efam"
            + "=SAND"
            + "&ac=119173659&sarbcuco=1&saucxgdprct=false&sacriteouc=31238024-5ccb-4531-bb99"
            + "-f648dd6b8398&ciid=rNulW0M*&sapbmuc=B1CAD7C7-8803-4545-9782-DC085A2AA780&sapcxcat"
            + "=&saopxuc=a61e2ec0-60cd-8cf1-9e03-e86ae044ec9f&saiid=97c50509-4367-4083-aaa6"
            + "-ff6d9189bc1e&sarbctid=650072&cflgs=AA**&sapbmpubid=156009&samslid=2940%2C8150"
            + "%2C8165"
            + "%2C7460%2C8085%2C8080%2C7425%2C7690%2C8075%2C4422%2C4428%2C9240%2C4302%2C216%2C9225"
            + "%2C3438%2C8065%2C8170%2C3450%2C4434%2C6565%2C6940%2C5270%2C8105%2C2868%2C3126%2C8090"
            + "%2C4416%2C3660%2C8095%2C8110%2C6585%2C8120%2C7470%2C4296%2C7450%2C5435%2C8160%2C7420"
            + "%2C6170%2C8725%2C7475%2C8060%2C9220%2C5420%2C7655%2C7530%2C7520%2C8115%2C8070%2C6595"
            + "%2C6155%2C4242%2C2436%2C8130&eactn=AUCT&saanxpid=10492947&rq=a5b36147201efe9e&sameid"
            + "=179e10216d334bbeaf02c9365ef4cad3&sapcxkw=&schemaversion=3&sacriteopubid=186706"
            + "&sapbmuco=1&saflps=Config&saaolpubid=12005&saanxuc=2846386642456817880&sapbmtid"
            + "=1087104&!sbids=sbid%3A5eb217c47ff1e168b6284976c1ec9300%2Csbe%3A0"
            + ".03%2Csbbid%3A%2Csbaid%3A%2Csbdid%3A%2Csbsid%3ACriteo%2Csbadid%3A52484%2Csbdlid%3A"
            + "%2Csbcid%3A10722378%2Csbt%3A1%2Csbadbts%3A716%2Csbfa%3Apandora"
            + ".com%2Csbrp%3A%2Csbs%3Acriteo-global%2Csblurl%3A&saidxuco=1&saflp=0"
            + ".85&pagename=SandPage&saucxgdpry=false&salr=100&saanxmid=7208&saaolsid=346782&saty=1"
            + "&saebaypid=100861&g=ae70db601680aca524931354fe0d8632&h=60&saopxuco=1&salv=5&n"
            + "=ae70db601680aca524931354fe0d8630&saidtid=30389&p=2367355&t=0&u=119173659&sarbcpubid"
            + "=11440&sarbcsid=138872&saopxtid=538715646&sacriteouco=1&pn=2&ciid=rNulW0M*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Da5b36147201efe9e%26node_id%3D9e9d95592343ee03%26REQUEST_GUID"
            + "%3D171e7acd-a960-a1b5-b434-34d2fcc03cac%26logid%3Dt6pdhc9%253Fuk%2560vgig%25280"
            + "%253Fesr%2Aw%2560ut3522-171e7acda96-0x3dbe&TPool=r1sand&TDuration=272&ContentLength"
            + "=519&ForwardedFor=10.164.102.228&Script=sand&Server=sand.stratus.ebay"
            + ".com&TMachine=10"
            + ".27.91.67&TStamp=18:49:57.27&TName=sand.v1&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39"));
    ubiEvent.setUrlQueryString(
        "/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 13
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:57.290")));
    ubiEvent.setRemoteIP("10.77.72.223");
    ubiEvent.setRequestCorrelationId("e912b72ccbf0de15");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*0631%3B%3D%28twwgsvv%28umj%28bad%7F%29%60jk-171e7acdaab-0x196");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(856706177708L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAAgAQAAAAAAAAAAAAAAAAgBAAAAIAAAAAAAQAAACAAEAAAIAAAAAAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAhBCA*&c=1&g=ae70db601680aca524931354fe0d8632&h=60&px=4249&chnl=9&n=ae70db601680aca524931354fe0d8630&uc=1&es=0&nqt=UAAAAAAAgAQAAAAAAAAAAAAAAAAgBAAAAIAAAAAAAQAAACAAEAAAIAAAAAAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAhBCA*&p=2317508&uaid=e7acdaaa1710a4b77c750fdbd0fd75f3S0&bs=0&t=0&u=119173659&cflgs=QA**&ul=en-US&plmt=Xj4AAB%252BLCAAAAAAAAADtmW1v3LgRgP%252BKoc%252Byl%252B8v%252BRa3uSJBgSbnyxXXQ3CgJMqrs1ZSJa3XTpD%252F3hlS66zjGlVeCsS2bIAaksPhcEQOH6w%252BJN158owRJQXladJVybPfPyRVkTyjhBhF0ySvsJ9qYpglmmpU66Etodp6ShhVBeciy7wrCcstV9KXIncFT9LEj2DuHTxdNAvDmm1dp0kbGzowM7im2FX5GtRztMoYSCP0fEgc9p%252FloOBq7HaTuRxcTpzXpMgUocoQlzvJhOWUS1F6UhjFCQzYFLiQRBDolEwRqRiznEvOwO%252FnN3%252BguR3C1ChtQLIovAdBciIMVNoR%252B0EoL8PSIRw4wRBrhHBc7RhqQmmDhsod1pjiVqLmLo4zHM2NocapljiuzG5phho1xEp0Y8ymCbUKqu0t1fa2anuoOvgDT8uhPfQ71vZ%252Bx5qCoECtvthBLXPj%252BggCf5S1xfXRru0vhqO%252BbTdHQ9e766POd53vfXE0bH3h8d2ESGOENhjKt0N1Kn%252FK3D9Of1uf%252FnmpX%252Fd9s8FAN%252Fmk1qLw2z%252FVi5b%252BK3v95tV5d%252FrTy5G%252Ff49aPRpjhAhCCa6s3OxfzxDEY4utVT69qq4P6iBddXupuZG28%252FZqncVJqT0m6jiGYjhsohqasgxaMLxF6Aou4d443Q5V44cB612IQHPRtLsGQ8PyG5e3NezgxFIbdhVlmhs0ZjTsSHhaQ5XADiIkjwIxMghcCI2CJZTKONYYDCRVBAKFPYqIYEyZOJbDecBpjISmYIwqhl5Yq8OTUmZDiCyJGlBGxzizJmpIacJ0RNGgCgIPSzFaqvAUJuwiq2ARWLcqhMUoYuOsyobdai2LszIlFIuCDsaNYSG4lHBlp8VJSvYuByuCMEnCtDQcL2utCKpcMR59lpCfYvDg%252FAcrhIeIUKmttlHZ6hhXaaiZAhzsGS3MZEZqNGO1iU4J%252BLdTGHUMVgw8pJXgm7GSTKHZu29pjIWWYTrUoGGopLGdcRZNxFBYLYPv4IvgYS6hSAyqmCID6S159ymlVh1ucBKOQd5P4nnR9W8Hv6929WaM5zLkcmhxOe5FfzXiCSxLkC99f%252F33dod9dX6rXtR41OogrzH1HvRdVu3tscXt%252FqI%252Fv1Uv0aXS1QPmigEn%252Bh0OYwqLhQJ2XcqsICmHHZFywU3KlSKpYIJBYVUqOGHQIaFNwDAhGHQILlAyqWSapFIwLLhMFexZKKBNSQUSbGAoLBQ4h0Y9KCQUEiWFhQ4FtEnshayfagVWtLIkNUSFQkKhUdIomVBgNaiAeQNvEwpKsECJocSxkFigFfQKJOjV4AEcCIIFSoK8%252BwhvLNx7Q%252FXe43Ptq%252FM1BBZ2fprsqmJcJ884IaDX%252BBFz8kt8uYqFg%252BCKt001hhafuesTLLbDSdm2o%252B9Xm97nf4zrCjIrzjL3Xuy2WV0N61%252BuO3CI39T%252F6mt3HRABXHHdRdW8PlRk8I7jrf3qLKxnqgy%252F%252Bn6o2gYz5wk9objTtz2kxGS1qvrg8TC6scpP8nazyvvV5SqnqyFfne2tHcdxx%252BJkUzUnfw4JxAKjNQScuC9eeFyGv52dvWhcVntY%252BthvPRyl5hKm7vq22OYjugVXYZVf%252BHHehYHg8jwEfUbIh2r0%252BG7AsSoE%252F8WbV%252F%252BWx%252BPVzzWe%252F%252FPQNgNpzqs5miz5iIt2iA5SQu7BLOgu9%252FS1gXYgu81V8AdvYqi5IdRcG2p488GL867P12%252B2cI7BEga7LTH9oPF8DIo5LupjesCM5A4zKiEnZpTcOuJp5goHW08Km8O9Zk2uFQxUHrPpwowLM%252F4QzDhjry7MuDBj8piZEVsWZnzKzDj4vG0WaJwFjTNujFnQeBPzJ0ONivAbagT%252FCBwETKoTNTqZFVxn3GZWCadz2PmQlbllJVOqzOdS4%252Fo66ysMa%252BAJLjCrLtC4QOP3hMYZW3WBxgUak8cLjZDKkwUaHyY0KvKJgcDMF0Hj1cmm7ld%252FFL502xpf%252B1cCI%252Fsh6fDzyMDB6H3Z%252BxusBWRElanxL0gUFLFtePn8bM%252BOVfNr5Xc%252FR5Wbxi%252BEzBk3zL2QeecdPSHAZHcAE5LCBJicmExy7bwhTGTOW%252BaFNyYT3CvHNDLjApgLYP4IgDljqy6AuQBm8pgBE%252BdZAPMhAqb9RFGamS%252Fiy801Cqv42ECCcucek9SjwsyD%252BFgd%252BOW%252ByP33Hy4%252FG3TYZchXUuvgNv4MPP2cW78HzM64ze6F2fs2xJNhWiPtnU%252FtkqiJaYn31GslKHGQW4h2JVzVJeQCmxXK%252BLlMu3xqX6D2%252Fw61M%252FbqArUL1CaPF2pNSBEL1D5EqP1en9rLqh%252B%252B4YfTp%252FSlfcaFMetL%252Bz7kT4gZzR1mFMJMzMgKyKoQRLhdCpFBnixJrrjMRJ7rvAjX2FcxY8i%252FCzQu0Pg9oXHGZl2gcYHG5DFDI7q6QONDhMZv%252BCV0Aphv%252FtL%252BYIDx%252Ft8%252BP%252Fu9dM7vm%252BzLaXPGTfO%252FaPPJfHKHehaCAOnz438AA%252FoaAV4%252BAAA%253D&ec=2&pn=2&rq=e912b72ccbf0de15&pagename=cos__mfe&po=%5B%28pg%3A2065413+pid%3A100861%29%28pg%3A2065413+pid%3A100860%29%28pg%3A2065413+pid%3A100603%29%28pg%3A2065413+pid%3A100602%29%28pg%3A2065413+pid%3A100859%29%28pg%3A2065413+pid%3A100858%29%5D&ciid=rNqqd8c*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De912b72ccbf0de15%26node_id%3D68cc804a3f7bdc5b%26REQUEST_GUID"
            + "%3D171e7acd-aaa0-a4b7-7c76-fbcee84acd5f%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A0631"
            + "%253B%253D%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7acdaab-0x196%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=206.126.218.39&Script=/roverimp/0/2065413/9&Server=internal.rover.vip.ebay"
            + ".com&TMachine=10.75.119.199&TStamp=18:49:57.29&TName=roverimp&Agent=Mozilla/5.0 "
            + "(iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1,GingerClient/2.9.7-RELEASE&RemoteIP=10.77"
            + ".72"
            + ".223"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2065413/9?site=0&trknvp=n%3Dae70db601680aca524931354fe0d8630%26u%3D119173659"
            + "%26plmt%3DXj4AAB%252BLCAAAAAAAAADtmW1v3LgRgP%252BKoc%252Byl%252B8v"
            + "%252BRa3uSJBgSbnyxXXQ3CgJMqrs1ZSJa3XTpD%252F3hlS66zjGlVeCsS2bIAaksPhcEQOH6w"
            +
            "%252BJN158owRJQXladJVybPfPyRVkTyjhBhF0ySvsJ9qYpglmmpU66Etodp6ShhVBeciy7wrCcstV9KXIncFT9LEj2DuHTxdNAvDmm1dp0kbGzowM7im2FX5GtRztMoYSCP0fEgc9p%252FloOBq7HaTuRxcTpzXpMgUocoQlzvJhOWUS1F6UhjFCQzYFLiQRBDolEwRqRiznEvOwO%252FnN3%252BguR3C1ChtQLIovAdBciIMVNoR%252B0EoL8PSIRw4wRBrhHBc7RhqQmmDhsod1pjiVqLmLo4zHM2NocapljiuzG5phho1xEp0Y8ymCbUKqu0t1fa2anuoOvgDT8uhPfQ71vZ%252Bx5qCoECtvthBLXPj%252BggCf5S1xfXRru0vhqO%252BbTdHQ9e766POd53vfXE0bH3h8d2ESGOENhjKt0N1Kn%252FK3D9Of1uf%252FnmpX%252Fd9s8FAN%252Fmk1qLw2z%252FVi5b%252BK3v95tV5d%252FrTy5G%252Ff49aPRpjhAhCCa6s3OxfzxDEY4utVT69qq4P6iBddXupuZG28%252FZqncVJqT0m6jiGYjhsohqasgxaMLxF6Aou4d443Q5V44cB612IQHPRtLsGQ8PyG5e3NezgxFIbdhVlmhs0ZjTsSHhaQ5XADiIkjwIxMghcCI2CJZTKONYYDCRVBAKFPYqIYEyZOJbDecBpjISmYIwqhl5Yq8OTUmZDiCyJGlBGxzizJmpIacJ0RNGgCgIPSzFaqvAUJuwiq2ARWLcqhMUoYuOsyobdai2LszIlFIuCDsaNYSG4lHBlp8VJSvYuByuCMEnCtDQcL2utCKpcMR59lpCfYvDg%252FAcrhIeIUKmttlHZ6hhXaaiZAhzsGS3MZEZqNGO1iU4J%252BLdTGHUMVgw8pJXgm7GSTKHZu29pjIWWYTrUoGGopLGdcRZNxFBYLYPv4IvgYS6hSAyqmCID6S159ymlVh1ucBKOQd5P4nnR9W8Hv6929WaM5zLkcmhxOe5FfzXiCSxLkC99f%252F33dod9dX6rXtR41OogrzH1HvRdVu3tscXt%252FqI%252Fv1Uv0aXS1QPmigEn%252Bh0OYwqLhQJ2XcqsICmHHZFywU3KlSKpYIJBYVUqOGHQIaFNwDAhGHQILlAyqWSapFIwLLhMFexZKKBNSQUSbGAoLBQ4h0Y9KCQUEiWFhQ4FtEnshayfagVWtLIkNUSFQkKhUdIomVBgNaiAeQNvEwpKsECJocSxkFigFfQKJOjV4AEcCIIFSoK8%252BwhvLNx7Q%252FXe43Ptq%252FM1BBZ2fprsqmJcJ884IaDX%252BBFz8kt8uYqFg%252BCKt001hhafuesTLLbDSdm2o%252B9Xm97nf4zrCjIrzjL3Xuy2WV0N61%252BuO3CI39T%252F6mt3HRABXHHdRdW8PlRk8I7jrf3qLKxnqgy%252F%252Bn6o2gYz5wk9objTtz2kxGS1qvrg8TC6scpP8nazyvvV5SqnqyFfne2tHcdxx%252BJkUzUnfw4JxAKjNQScuC9eeFyGv52dvWhcVntY%252BthvPRyl5hKm7vq22OYjugVXYZVf%252BHHehYHg8jwEfUbIh2r0%252BG7AsSoE%252F8WbV%252F%252BWx%252BPVzzWe%252F%252FPQNgNpzqs5miz5iIt2iA5SQu7BLOgu9%252FS1gXYgu81V8AdvYqi5IdRcG2p488GL867P12%252B2cI7BEga7LTH9oPF8DIo5LupjesCM5A4zKiEnZpTcOuJp5goHW08Km8O9Zk2uFQxUHrPpwowLM%252F4QzDhjry7MuDBj8piZEVsWZnzKzDj4vG0WaJwFjTNujFnQeBPzJ0ONivAbagT%252FCBwETKoTNTqZFVxn3GZWCadz2PmQlbllJVOqzOdS4%252Fo66ysMa%252BAJLjCrLtC4QOP3hMYZW3WBxgUak8cLjZDKkwUaHyY0KvKJgcDMF0Hj1cmm7ld%252FFL502xpf%252B1cCI%252Fsh6fDzyMDB6H3Z%252BxusBWRElanxL0gUFLFtePn8bM%252BOVfNr5Xc%252FR5Wbxi%252BEzBk3zL2QeecdPSHAZHcAE5LCBJicmExy7bwhTGTOW%252BaFNyYT3CvHNDLjApgLYP4IgDljqy6AuQBm8pgBE%252BdZAPMhAqb9RFGamS%252Fiy801Cqv42ECCcucek9SjwsyD%252BFgd%252BOW%252ByP33Hy4%252FG3TYZchXUuvgNv4MPP2cW78HzM64ze6F2fs2xJNhWiPtnU%252FtkqiJaYn31GslKHGQW4h2JVzVJeQCmxXK%252BLlMu3xqX6D2%252Fw61M%252FbqArUL1CaPF2pNSBEL1D5EqP1en9rLqh%252B%252B4YfTp%252FSlfcaFMetL%252Bz7kT4gZzR1mFMJMzMgKyKoQRLhdCpFBnixJrrjMRJ7rvAjX2FcxY8i%252FCzQu0Pg9oXHGZl2gcYHG5DFDI7q6QONDhMZv%252BCV0Aphv%252FtL%252BYIDx%252Ft8%252BP%252Fu9dM7vm%252BzLaXPGTfO%252FaPPJfHKHehaCAOnz438AA%252FoaAV4%252BAAA%253D%26po%3D%5B%28pg%3A2065413+pid%3A100861%29%28pg%3A2065413+pid%3A100860%29%28pg%3A2065413+pid%3A100603%29%28pg%3A2065413+pid%3A100602%29%28pg%3A2065413+pid%3A100859%29%28pg%3A2065413+pid%3A100858%29%5D&trknvpsvc=%3Ca%3Enqc%3DUAAAAAAAgAQAAAAAAAAAAAAAAAAgBAAAAIAAAAAAAQAAACAAEAAAIAAAAAAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAhBCA*%26nqt%3DUAAAAAAAgAQAAAAAAAAAAAAAAAAgBAAAAIAAAAAAAQAAACAAEAAAIAAAAAAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAhBCA*%26es%3D0%26ec%3D2%3C%2Fa%3E&tguid=ae70db601680aca524931354fe0d8632&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 14
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2065413);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:49:56.863")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("e912b72ccbf0de15");
    ubiEvent.setSid("p2065413");
    ubiEvent.setRlogid(
        "t6n%60u%609%3Fjqphctd%285157%3F26%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e7acd917-0x117");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(580659567020L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("mesg.ebay.com");
    ubiEvent.setApplicationPayload(
        "uit=1576674616225&nqc"
            +
            "=UAAAAAAAgAQAAAAAAAAAAAAAAAAgBAAAAIAAAAAAAQAAACAAEAAAIAAAAAAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAhBCA*&c=2&!ampid=3P_IMP&g=ae70db601680aca524931354fe0d8632&h=60&rpg=2065413&n=ae70db601680aca524931354fe0d8630&uc=1&es=0&nqt=UAAAAAAAgAQAAAAAAAAAAAAAAAAgBAAAAIAAAAAAAQAAACAAEAAAIAAAAAAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAhBCA*&p=2065413&uaid=e7acd9011710aaa328708a10ff5fe4ccS0&bs=0&t=0&u=119173659&ul=en-US&ec=2&pn=2&rq=e912b72ccbf0de15&pagename=mesg__ViewMessageDetail&ciid=rNkBMoc*&sid=p2065413");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De912b72ccbf0de15%26node_id%3D4594d4cd7198c339%26REQUEST_GUID"
            + "%3D171e7acd-8ff0-aaa3-2871-35d2ff6aaf0f%26logid%3Dt6n%2560u%25609%253Fjqphctd"
            + "%25285157"
            + "%253F26%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e7acd917-0x117&TPool=r1mesg"
            + "&TDuration=594&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=206.126.218"
            + ".39&Script=/mesgweb&Server=mesg.ebay.com&TMachine=10.170.50.135&TStamp=18:49:56"
            + ".86&TName=ViewMessageDetail&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218.39&Encoding=gzip,"
            + " deflate, br"));
    ubiEvent.setUrlQueryString(
        "/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("ViewMessageDetail");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(2);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 15
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:50:28.581")));
    ubiEvent.setRemoteIP("10.164.51.73");
    ubiEvent.setRequestCorrelationId("52967bd5d890d921");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0%3C176%3C2-171e7ad54e7-0x11d");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(291234010285L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AQAAAAAAAAQAAAIAAAAAAAAAAAAABAAAAQAEAAAAAQCAAQABAAAAIAAAAAAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAIAACABAAIAAABAAAAAAAAAAAgAQA**&c=1&g=ae70db601680aca524931354fe0d8632&h=60&px=4249&chnl=9&n=ae70db601680aca524931354fe0d8630&uc=1&es=0&nqt=AQAAAAAAAAQAAAIAAAAAAAAAAAAABAAAAQAEAAAAAQCAAQABAAAAIAAAAAAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAIAACABAIIAAABAAAAAAAAAAAgAQA**&p=2317508&uaid=e7ad54e61710ad4ce430fe2dc9f14298S0&bs=0&t=0&cflgs=QA**&ul=en-US&plmt=2AUAAB%252BLCAAAAAAAAACNVE1v2zAM%252FSuFzm6ib8m9DdswbKcNWXcpelBkOdHq2IZk92NF%252FvtIJQ522LBenh8p%252BomkRL2ScUduONXcKlORMZKbu1cSG3LDKLXKVsRHXGeGWl5Tw6QEXwIfUWYrXWi3ojFassBrLnmwTWtZrY3VLalImEDuHr7uJAu%252F9XPXVWQ4OUaQya5vnqLfQ7gHk3FKgU6w9EocBmw8RLgO191Zz0POxAVDm62mTFvqvFNc1oIJJdtAG6sFqswZFQWyAzB0DRO6gLQ5IDOwnwWze3gqmimGq11spivvUoN7Yi%252FKn70%252Fk4QeKLJmXBSlRTtfWFxixwSEI3seF9Zf2Py2Nm63Z7VmycXzZYOlxXEcL3v7dKa7Zky3OSzm2B2mUs7pbMHjPOqE5wmz74DfcaYrLgWA1bbitaSVYFxXQgpbCa1pJSFBMBWyWldSUF5JCb9JyS2AkMh4pbihlZIcQahKM4UAPq00MGURagDcw2AcgAJQyDSCKQA%252BhatKAGhQMbqmlaW6gAIwyAwyWwDNEgLyljEEWhj44IIBCASFgCqYFTAIMZBBzSEEAJmk90doXLmNOf4K%252BN2HuNtD22EH8gRXZU9uDLcQ1ofpaUgPn7HFmkuFDW5u%252BzgVT9i6lxXCnFftMEwhrZvQurnD5o%252Fztot5%252F%252F1lhC3Exf4QOvdSZhHU3fgQ%252B69%252FBnI4tdN0fNmUDM9G%252FhFSjkOP92zFVqcJSB2Y63VMJYk8uSn6lR8Oa5%252FWj2vP1tmvN4va9em%252Fa7k6xH71MxMoD%252BvPZW7%252F1YG%252FrtSGHuGCptCmcKlIUIo1nZ3vcdChmJg%252FbTYfe7ftAnRsSnOAC94%252FQt5jGprZT1gTPA7RP%252BC9f8vs4PPyrhzC%252F44gxyngQUFauzIlb3hgdvEtkZwcsQOuvJpKQRjejMflMTyAn8Hnuew9%252BGK5XCw3FCs35XiDS37%252FbQ4JOkjwSIYWpx%252FF%252FVQCPRZwvD%252F%252BBmtlAdDYBQAA&ec=1&pn=2&rq=52967bd5d890d921&pagename=cos__mfe&po=%5B%28pg%3A2062857+pid%3A100858%29%5D&ciid=rVTmzkM*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D52967bd5d890d921%26node_id%3D677a4ffc5ae2fcc6%26REQUEST_GUID"
            + "%3D171e7ad5-4e50-ad4c-e433-06e2e31dceea%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0"
            + "%253C176%253C2-171e7ad54e7-0x11d%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2"
            + "&TStatus=0"
            + "&TType=URL&ContentLength=-1&ForwardedFor=206.126.218"
            + ".39&Script=/roverimp/0/2062857/9&Server=internal.rover.vip.ebay.com&TMachine=10.212"
            + ".206.67&TStamp=18:50:28.58&TName=roverimp&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1,GingerClient/2.9.7-RELEASE&RemoteIP=10"
            + ".164.51"
            + ".73"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2062857/9?site=0&trknvp=n%3Dae70db601680aca524931354fe0d8630%26plmt%3D2AUAAB"
            + "%252BLCAAAAAAAAACNVE1v2zAM%252FSuFzm6ib8m9DdswbKcNWXcpelBkOdHq2IZk92NF"
            + "%252FvtIJQ522LBenh8p"
            +
            "%252BomkRL2ScUduONXcKlORMZKbu1cSG3LDKLXKVsRHXGeGWl5Tw6QEXwIfUWYrXWi3ojFassBrLnmwTWtZrY3VLalImEDuHr7uJAu%252F9XPXVWQ4OUaQya5vnqLfQ7gHk3FKgU6w9EocBmw8RLgO191Zz0POxAVDm62mTFvqvFNc1oIJJdtAG6sFqswZFQWyAzB0DRO6gLQ5IDOwnwWze3gqmimGq11spivvUoN7Yi%252FKn70%252Fk4QeKLJmXBSlRTtfWFxixwSEI3seF9Zf2Py2Nm63Z7VmycXzZYOlxXEcL3v7dKa7Zky3OSzm2B2mUs7pbMHjPOqE5wmz74DfcaYrLgWA1bbitaSVYFxXQgpbCa1pJSFBMBWyWldSUF5JCb9JyS2AkMh4pbihlZIcQahKM4UAPq00MGURagDcw2AcgAJQyDSCKQA%252BhatKAGhQMbqmlaW6gAIwyAwyWwDNEgLyljEEWhj44IIBCASFgCqYFTAIMZBBzSEEAJmk90doXLmNOf4K%252BN2HuNtD22EH8gRXZU9uDLcQ1ofpaUgPn7HFmkuFDW5u%252BzgVT9i6lxXCnFftMEwhrZvQurnD5o%252Fztot5%252F%252F1lhC3Exf4QOvdSZhHU3fgQ%252B69%252FBnI4tdN0fNmUDM9G%252FhFSjkOP92zFVqcJSB2Y63VMJYk8uSn6lR8Oa5%252FWj2vP1tmvN4va9em%252Fa7k6xH71MxMoD%252BvPZW7%252F1YG%252FrtSGHuGCptCmcKlIUIo1nZ3vcdChmJg%252FbTYfe7ftAnRsSnOAC94%252FQt5jGprZT1gTPA7RP%252BC9f8vs4PPyrhzC%252F44gxyngQUFauzIlb3hgdvEtkZwcsQOuvJpKQRjejMflMTyAn8Hnuew9%252BGK5XCw3FCs35XiDS37%252FbQ4JOkjwSIYWpx%252FF%252FVQCPRZwvD%252F%252BBmtlAdDYBQAA%26po%3D%5B%28pg%3A2062857+pid%3A100858%29%5D&trknvpsvc=%3Ca%3Enqc%3DAQAAAAAAAAQAAAIAAAAAAAAAAAAABAAAAQAEAAAAAQCAAQABAAAAIAAAAAAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAIAACABAAIAAABAAAAAAAAAAAgAQA**%26nqt%3DAQAAAAAAAAQAAAIAAAAAAAAAAAAABAAAAQAEAAAAAQCAAQABAAAAIAAAAAAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAIAACABAIIAAABAAAAAAAAAAAgAQA**%26es%3D0%26ec%3D1%3C%2Fa%3E&tguid=ae70db601680aca524931354fe0d8632&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 16
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:50:28.580")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("49e3f8689a5b4a01");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Fjqpvgig%282720127-171e7ad54e2-0x2fc3");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(862792734381L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&sagooglssa=true&saaoluc=UPfdb71cdb-f91b-11e9-b40d-0eb5e2a831ee&saaoluco=1"
            + "&saidpubid=183403&saidxuc=XbY5v9HM6GYAACkpBH8AAAB9ACoAAAIB&saanxuco=1&sarbcuc"
            + "=K29P8DLS"
            + "-4-9W20&sagoogltagid=21663075830&sapbmsid=165303&saopxpubid=537121625&efam=SAND&ac"
            + "=119173659&sarbcuco=1&saucxgdprct=false&ciid=rVZ04sg*&sapbmuc=B1CAD7C7-8803-4545"
            + "-9782"
            + "-DC085A2AA780&sapcxcat=&saopxuc=a61e2ec0-60cd-8cf1-9e03-e86ae044ec9f&saiid=6799fe4e"
            + "-4cd5-4ea6-a992-47cce3265446&sarbctid=650078&cflgs=AA**&sapbmpubid=156009&samslid"
            + "=2940"
            + "%2C8150%2C8165%2C7460%2C8085%2C8080%2C7425%2C7690%2C8075%2C4422%2C4428%2C9240%2C4302"
            + "%2C216%2C9225%2C3438%2C8065%2C8170%2C3450%2C4434%2C6565%2C6940%2C5270%2C8105%2C2868"
            + "%2C3126%2C8090%2C4416%2C3660%2C8095%2C8110%2C6585%2C8120%2C7470%2C4296%2C7450%2C5435"
            + "%2C8160%2C7420%2C6170%2C8725%2C7475%2C8060%2C9220%2C5420%2C7655%2C7530%2C7520%2C8115"
            + "%2C8070%2C6595%2C6155%2C4242%2C2436%2C8130&eactn=AUCT&saanxpid=9794063&rq"
            + "=49e3f8689a5b4a01&sagoogluc=CAESEE3x9R7nk1V93EFwDmV44_o&sameid"
            + "=57b4aefb3d7641e29242e8df8196786f&sapcxkw=&schemaversion=3&sapbmuco=1&sagooglsid"
            + "=21663075830009&saaolpubid=12005&saanxuc=2846386642456817880&sapbmtid=851352"
            + "&saidxuco"
            + "=1&pagename=SandPage&saucxgdpry=false&sagoogluco=1&saanxmid=7208&saaolsid=345487"
            + "&saty"
            + "=1&saebaypid=100858&g=ae70db601680aca524931354fe0d8632&h=60&saopxuco=1&salv=5"
            + "&saidtid"
            + "=29011&p=2367355&r=-1552470496&t=0&u=119173659&sarbcpubid=11440&sarbcsid=138876"
            + "&saopxtid=538587091&pn=2&ciid=rVZ04sg*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D49e3f8689a5b4a01%26node_id%3D299637e3314e0ba8%26REQUEST_GUID"
            + "%3D171e7ad5-4e20-aade-2c87-ca15fdfee40c%26logid%3Dt6pdhc9%253Fjqpvgig%25282720127"
            + "-171e7ad54e2-0x2fc3&TPool=r1sand&TDuration=400&ContentLength=159&ForwardedFor=10.212"
            + ".208.138&Script=sand&Server=sand.stratus.ebay.com&TMachine=10.173.226"
            + ".200&TStamp=18:50:28.58&TName=sand.v1&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218"
            + ".39&Referer=https://mesg"
            + ".ebay.com/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396"));
    ubiEvent.setUrlQueryString(
        "/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(-1552470496L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(
        "https://mesg.ebay.com/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 17
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:50:28.667")));
    ubiEvent.setRemoteIP("10.164.20.233");
    ubiEvent.setRequestCorrelationId("7dda670f7a125333");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*7544%3A32-171e7ad553d-0x189");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(346268980653L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AQAAAAAAAAQAAAIAAAAAAAAAAAAABAAAAQAEAAAAAQCAAQABAAAAIAAAAAAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAIAACABAAIAAABAAAAAAAAAAAgAQA**&c=1&g=ae70db601680aca524931354fe0d8632&h=60&px=4249&chnl=9&n=ae70db601680aca524931354fe0d8630&uc=1&es=0&nqt=AQAAAAAAAAQAAAIAAAAAAAAAAAAABAAAAQAEAAAAAQCAAQABAAAAIAAAAAAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAIAACABAIIAAABAAAAAAAAAAAgAQA**&p=2317508&uaid=e7ad553c1710aa19f504234ce1b865a7S0&bs=0&t=0&cflgs=QA**&ul=en-US&plmt=RwYAAB%252BLCAAAAAAAAACNVEuP2zYQ%252FisBz4zNt6S9BW1RJMihrZtcgkVBkZTNriwJpGSvu9j%252F3hnaWnQDBN3Lp2%252FewxmRT2TakzvBjKh1RckUyd23JxI9ueOMGSYpcRHt3DDOai641JqSBDribNvaRmtv6k41HWu6VtnOqZp1bV17TygJM6S7h6%252B9poWwYel7SsarYoI0h0ubIjo7ELhUHOgMhidi0bxzdvC2R7u9ZXPQMbGhYr6FrkzNrLNaqEZCc6oLzNdGMghYcsmI7AgMVeOMKiBdDsgqwVgNYv9wLjlTDO%252F20c%252FvnE2lJ5xEiRzcjSTU8MY0XMiSac2dX1hcfacERCB7nFY2vLDlbUNs21s2v%252FbixFpgHXCcppfaLt3o3k%252FpSw6rOPXHuRznulnQWId5wuOMk%252Bk64KeQLp%252FHM9p690r2PZ6uL%252FyAm%252FiP7RTH17H%252Btd2n%252FSu5w5Y628MOYGxY6JvghgolAWpTU9EoRiUXhkpjGFVCCaokAxCNoUoJgVADSAUAsVLJmmpRQZjSjGolEKSmhmsEMBhtgOkaoQHAGhX6VRgBTANUBVCEupVGq5YABrJUpmG0ZgUqBIMA%252BWrONLKiQ1YhNGiAzDXnyAQyDOOy6BDQmWOWCoo34goMQLH7Z9hYuQY5%252FhPwewhxf4DBGsYoOcNPeoBLahj4DWE%252Bj%252BnhIy7XCKXLCr4McS6a0NrLBmHJm8fNsU%252Fbv3zo7NLj2qel7WM%252B%252FHmZoIaAVHZ6iMNv32nz9RJ%252B2pV%252BbkL%252BGlKO44C%252F84Zvrhct9SButzGVinm2c3QbNx63Lm1PW8e32W13a7b317j3anOMw%252BbvTOAseNpcHocfnRd%252B9xS6FPLh59DbC7mTjKHLTfkTPhswXRLzxw%252FQ8JyWAMLwNYbzH1eXF2X%252Bdbf7ZbBtH%252FyqC8MJTjCl0S9uxtPBaxTdA160t1zWDEf7UGb%252Ff5PPcQ64H%252Bh0X67lG160fXyLpyDPOCSLj7TWGtzwhzitb%252B8R9Bw%252Bj6X26Ipkc5HsWKTsy6KDTe7w%252BwL3FjLhcsYOnxtM7ubi6PAAz%252FfP%252FwLgn%252BmKRwYAAA%253D%253D&ec=1&pn=2&rq=7dda670f7a125333&pagename=cos__mfe&po=%5B%28pg%3A2062857+pid%3A100603%29%5D&ciid=rVU9n1A*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D7dda670f7a125333%26node_id%3D067698570b9ae873%26REQUEST_GUID"
            + "%3D171e7ad5-53b0-aa19-f504-bc3aef97c641%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t"
            + "%2A7544"
            + "%253A32-171e7ad553d-0x189%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0"
            + "&TType"
            + "=URL&ContentLength=-1&ForwardedFor=206.126.218"
            + ".39&Script=/roverimp/0/2062857/9&Server=internal.rover.vip.ebay.com&TMachine=10.161"
            + ".159.80&TStamp=18:50:28.66&TName=roverimp&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1,GingerClient/2.9.7-RELEASE&RemoteIP=10"
            + ".164.20"
            + ".233"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2062857/9?site=0&trknvp=n%3Dae70db601680aca524931354fe0d8630%26plmt%3DRwYAAB"
            + "%252BLCAAAAAAAAACNVEuP2zYQ%252FisBz4zNt6S9BW1RJMihrZtcgkVBkZTNriwJpGSvu9j"
            + "%252F3hnaWnQDBN3Lp2"
            +
            "%252FewxmRT2TakzvBjKh1RckUyd23JxI9ueOMGSYpcRHt3DDOai641JqSBDribNvaRmtv6k41HWu6VtnOqZp1bV17TygJM6S7h6%252B9poWwYel7SsarYoI0h0ubIjo7ELhUHOgMhidi0bxzdvC2R7u9ZXPQMbGhYr6FrkzNrLNaqEZCc6oLzNdGMghYcsmI7AgMVeOMKiBdDsgqwVgNYv9wLjlTDO%252F20c%252FvnE2lJ5xEiRzcjSTU8MY0XMiSac2dX1hcfacERCB7nFY2vLDlbUNs21s2v%252FbixFpgHXCcppfaLt3o3k%252FpSw6rOPXHuRznulnQWId5wuOMk%252Bk64KeQLp%252FHM9p690r2PZ6uL%252FyAm%252FiP7RTH17H%252Btd2n%252FSu5w5Y628MOYGxY6JvghgolAWpTU9EoRiUXhkpjGFVCCaokAxCNoUoJgVADSAUAsVLJmmpRQZjSjGolEKSmhmsEMBhtgOkaoQHAGhX6VRgBTANUBVCEupVGq5YABrJUpmG0ZgUqBIMA%252BWrONLKiQ1YhNGiAzDXnyAQyDOOy6BDQmWOWCoo34goMQLH7Z9hYuQY5%252FhPwewhxf4DBGsYoOcNPeoBLahj4DWE%252Bj%252BnhIy7XCKXLCr4McS6a0NrLBmHJm8fNsU%252Fbv3zo7NLj2qel7WM%252B%252FHmZoIaAVHZ6iMNv32nz9RJ%252B2pV%252BbkL%252BGlKO44C%252F84Zvrhct9SButzGVinm2c3QbNx63Lm1PW8e32W13a7b317j3anOMw%252BbvTOAseNpcHocfnRd%252B9xS6FPLh59DbC7mTjKHLTfkTPhswXRLzxw%252FQ8JyWAMLwNYbzH1eXF2X%252Bdbf7ZbBtH%252FyqC8MJTjCl0S9uxtPBaxTdA160t1zWDEf7UGb%252Ff5PPcQ64H%252Bh0X67lG160fXyLpyDPOCSLj7TWGtzwhzitb%252B8R9Bw%252Bj6X26Ipkc5HsWKTsy6KDTe7w%252BwL3FjLhcsYOnxtM7ubi6PAAz%252FfP%252FwLgn%252BmKRwYAAA%253D%253D%26po%3D%5B%28pg%3A2062857+pid%3A100603%29%5D&trknvpsvc=%3Ca%3Enqc%3DAQAAAAAAAAQAAAIAAAAAAAAAAAAABAAAAQAEAAAAAQCAAQABAAAAIAAAAAAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAIAACABAAIAAABAAAAAAAAAAAgAQA**%26nqt%3DAQAAAAAAAAQAAAIAAAAAAAAAAAAABAAAAQAEAAAAAQCAAQABAAAAIAAAAAAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAIAACABAIIAAABAAAAAAAAAAAgAQA**%26es%3D0%26ec%3D1%3C%2Fa%3E&tguid=ae70db601680aca524931354fe0d8632&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 18
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ae70db601680aca524931354fe0d8632");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:50:28.685")));
    ubiEvent.setRemoteIP("206.126.218.39");
    ubiEvent.setRequestCorrelationId("c2O%2B6xun4QzU");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Ftilvgig%28%7E3dn%3F*w%60ut3541-171e7ad554d-0x8f60");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("206.126.218.39");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(443829671341L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&sameid=cabba955d68f49f09fb4afc480fb88dd&sapcxkw=&schemaversion=3&pagename"
            + "=SandPage&saucxgdpry=false&efam=SAND&ac=119173659&saucxgdprct=false&saty=1&g"
            + "=ae70db601680aca524931354fe0d8632&saebaypid=100603&h=60&salv=5&ciid=rVVPVmc*&p"
            + "=2367355"
            + "&r=-1552470496&sapcxcat=&t=0&saiid=b0605585-66d3-4a3d-a90a-b5f5ea7e5b59&u=119173659"
            + "&cflgs=AA**&samslid=2940%2C8150%2C8165%2C7460%2C8085%2C8080%2C7425%2C7690%2C8075"
            + "%2C4422%2C4428%2C9240%2C4302%2C216%2C9225%2C3438%2C8065%2C8170%2C3450%2C4434%2C6565"
            + "%2C6940%2C5270%2C8105%2C2868%2C3126%2C8090%2C4416%2C3660%2C8095%2C8110%2C6585%2C8120"
            + "%2C7470%2C4296%2C7450%2C5435%2C8160%2C7420%2C6170%2C8725%2C7475%2C8060%2C9220%2C5420"
            + "%2C7655%2C7530%2C7520%2C8115%2C8070%2C6595%2C6155%2C4242%2C2436%2C8130&eactn=AUCT"
            + "&pn=2"
            + "&rq=c2O%2B6xun4QzU&ciid=rVVPVmc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dc2O%2B6xun4QzU%26node_id%3D4817d1a6be709b74%26REQUEST_GUID"
            + "%3D171e7ad5-54d0-a4d5-6676-660cf868e4cf%26logid%3Dt6pdhc9%253Ftilvgig%2528%257E3dn"
            + "%253F%2Aw%2560ut3541-171e7ad554d-0x8f60&TPool=r1sand&TDuration=4&ContentLength=183"
            + "&ForwardedFor=10.211.25.233&Script=sand&Server=sand.stratus.ebay.com&TMachine=10"
            + ".77.86"
            + ".103&TStamp=18:50:28.68&TName=sand.v1&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.1 Mobile/15E148 Safari/604.1&RemoteIP=206.126.218"
            + ".39&Referer=https://mesg"
            + ".ebay.com/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396"));
    ubiEvent.setUrlQueryString(
        "/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(-1552470496L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(
        "https://mesg.ebay.com/mesgweb/ViewMessageDetail/email/0/db64bcce16c24d7eba2d9bb82cf38396");
    ubiEvent.setUserId("119173659");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(4, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric13() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(true);
    ubiEvent.setPageId(3084);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:58.682")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("44abb6fd73f87506");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0%3C7%3E%3B44-171e7d7e7be-0x19e");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(374681233367L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "rdt=1&c=1&g=d52fb1481710a4d264558da69f8a715d&nid=&h=48&cguidsrc=cookie&n"
            + "=d52fb1481710a4d264558da69f8a715b&uc=1&url_mpre=https%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832&p=3084&uaid=e7d7e7bd1710acc3c571d17fc0e37d72S0&bs=0&rvrid"
            + "=2410463730471&t=0&cflgs=EA**&ul=en-US&hrc=301&pn=2&pcguid"
            + "=d52fb1481710a4d264558da69f8a715b&rq=44abb6fd73f87506&pagename=EntryTracking&ciid=1"
            + "%2Be9PFc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D44abb6fd73f87506%26node_id%3D1cc66574125ef79d%26REQUEST_GUID"
            + "%3D171e7d7e-7ba0-acc3-c572-e729de5ec6fc%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0"
            + "%253C7%253E%253B44-171e7d7e7be-0x19e%26cal_mod%3Dfalse&TPool=r1rover&TDuration=59"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=172.58.157"
            + ".100&Script=/rover/1/711-219698-2056-1/2&Server=rover.ebay.com&TMachine=10.204.60"
            + ".87&TStamp=19:36:58.68&TName=rover&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58.157.100&Encoding=gzip,"
            + " deflate, br"));
    ubiEvent.setUrlQueryString(
        "/rover/1/711-219698-2056-1/2?gclid"
            + "=Cj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB&adpos=&PARM3_ID=GBH_165&cmpgn=344175529&crlp"
            + "=289697120826&rule=&ul_ref=https%253A%252F%252Frover.ebay"
            + ".com%252Frover%252F0%252F0%252F99%253Floc%253Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F1%25252F711-219698-2056-1%25252F2%25253Fmpre%25253Dhttps"
            + "%25253A"
            + "%25252F%25252Fwww.ebay.com%25252Fc%25252F2222324832%252526rule%25253D%252526crlp"
            + "%25253D289697120826%252526adpos%25253D%252526device%25253Dm%252526cmpgn"
            + "%25253D344175529%252526PARM3_ID%25253DGBH_165%252526gclid"
            + "%25253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%2526rvr_id%253D0%2526rvr_ts"
            + "%253De7d7e70e1710aa162630649effdf75d8&device=m&crdt=0&mpre=https%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832");
    ubiEvent.setPageName("rover");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2455605);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.500")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%28%3F%3F%3B2%3F%3E-vrubqst-whh-%60dfz%2Behn-171e7d7eaed"
            + "-0xce");
    ubiEvent.setEventFamily("PRP");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(496281840343L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&flgs=AA**&uc=1&nqt=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&mos=iOS&bs=0&pageci=2dba9a86-f91b-4411-a1f0-56155bc58605&osv=13_1_2&ul=en-US&pcl=2&callingpagename=viexpsvc__VIEWPRODUCTCLUSTER&glpsl=4475%3A9925%2C6409%2C6846%2C&glpsm=4475%2C3637%2C4277%2C&svctime=50&ec=2&pagename=prpexpsvc__product_v1_module_provider_PRODUCTID_GET&app=3564&prpnewui=true&res=480x320&efam=PRP&pri=2222324832&g=d52fb1481710a4d264558da69f8a715d&h=48&mobile=true&leaf=11071&cp=2546137&requestPri=2222324832&n=d52fb1481710a4d264558da69f8a715b&es=0&p=2455605&t=0&cflgs=EA**&eactn=EXPM&respCode=200&rq=e7d7e9fe1710ad332cd15f28fff27545&pooltype=production&ciid=1%2BqzjHM*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D9ca65b574c5a353a%26node_id%3Daff4a177ca953f5f%26REQUEST_GUID"
            + "%3D171e7d7e-ae90-aae8-c735-0147fe8add79%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%2528%253F%253F%253B2%253F%253E-vrubqst-whh-%2560dfz%252Behn-171e7d7eaed-0xce"
            + "%26cal_mod%3Dfalse&TPool=r1prpexpsvc8&TDuration=1&TStatus=0&TType=URL&ContentLength"
            + "=1306&ForwardedFor=23.33.238.125;172.58.157"
            + ".100&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.com&TMachine=10.174.140"
            + ".115&TStamp=19:36:59.50&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58.157.100&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/c/2222324832?ul_ref=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%26ul_ref%3Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F0%25252F0%25252F99%25253Floc%25253Dhttps%2525253A%2525252F"
            + "%2525252Frover.ebay.com%2525252Frover%2525252F1%2525252F711-219698-2056-1%2525252F2"
            + "%2525253Fmpre%2525253Dhttps%2525253A%2525252F%2525252Fwww.ebay"
            + ".com%2525252Fc%2525252F2222324832%25252526rule%2525253D%25252526crlp"
            + "%2525253D289697120826%25252526adpos%2525253D%25252526device%2525253Dm%25252526cmpgn"
            + "%2525253D344175529%25252526PARM3_ID%2525253DGBH_165%25252526gclid"
            + "%2525253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%252526rvr_id%25253D0%252526rvr_ts"
            + "%25253De7d7e70e1710aa162630649effdf75d8%26srcrot%3D711-219698-2056-1%26rvr_id"
            + "%3D2410463730471%26rvr_ts%3De7d7e7f41710acc3c572f8d7ffdfed95");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(3564);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.534")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dqkisqn47pse31%283674%3F%3E-171e7d7eb0f-0x134");
    ubiEvent.setEventFamily("LST");
    ubiEvent.setEventAction("ANSTMPL");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(657600539607L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "app=3564&res=480x320&flgs=AA**&efam=LST&g=d52fb1481710a4d264558da69f8a715d&h=48&mobile"
            + "=true&pagetmpl=SRP&n=d52fb1481710a4d264558da69f8a715b&uc=1&mos=iOS&p=2370942&bs=0"
            + "&osv"
            + "=13_1_2&pageci=75c0460a-8f42-11ea-be96-b259064e8d6b&t=0&cflgs=AA**&ul=en-US&eactn"
            + "=ANSTMPL&rq=e7d7e9fe1710ad332cd15f28fff27545&pagename=ANSWERS_PLATFORM_PAGE&ciid=1"
            + "%2BsLHJk*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De7d7e9fe1710ad332cd15f28fff27545%26node_id%3De6e120f2bb16325b"
            + "%26REQUEST_GUID%3D171e7d7e-aa30-ad39-2797-eaa7fe8798b2%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31%25283674%253F%253E-171e7d7eb0f-0x134%26cal_mod%3Dfalse&TPool"
            + "=r1searchsvc&TDuration=1&TStatus=0&TType=URL&ContentLength=871&ForwardedFor=23.33"
            + ".238"
            + ".125;172.58.157.100&Script=/trk20svc/TrackingResource/v1&Server=www.ebay"
            + ".com&TMachine=10.155.28.153&TStamp=19:36:59.53&TName=Ginger.CollectionSvc"
            + ".track&Agent=Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X)"
            + " AppleWebKit/605"
            + ".1.15 (KHTML, like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58"
            + ".157.100&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/c/2222324832?ul_ref=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%26ul_ref%3Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F0%25252F0%25252F99%25253Floc%25253Dhttps%2525253A%2525252F"
            + "%2525252Frover.ebay.com%2525252Frover%2525252F1%2525252F711-219698-2056-1%2525252F2"
            + "%2525253Fmpre%2525253Dhttps%2525253A%2525252F%2525252Fwww.ebay"
            + ".com%2525252Fc%2525252F2222324832%25252526rule%2525253D%25252526crlp"
            + "%2525253D289697120826%25252526adpos%2525253D%25252526device%2525253Dm%25252526cmpgn"
            + "%2525253D344175529%25252526PARM3_ID%2525253DGBH_165%25252526gclid"
            + "%2525253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%252526rvr_id%25253D0%252526rvr_ts"
            + "%25253De7d7e70e1710aa162630649effdf75d8%26srcrot%3D711-219698-2056-1%26rvr_id"
            + "%3D2410463730471%26rvr_ts%3De7d7e7f41710acc3c572f8d7ffdfed95");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(3564);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.690")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%28%3F13515-vrubqst-whh-%60dfz%2Behn-171e7d7ebab-0x132");
    ubiEvent.setEventFamily("LST");
    ubiEvent.setEventAction("ANSTRIG");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(508276763607L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "app=3564&res=480x320&flgs=AA**&efam=LST&g=d52fb1481710a4d264558da69f8a715d&h=48&mobile"
            + "=true&n=d52fb1481710a4d264558da69f8a715b&uc=1&mos=iOS&p=2370942&bs=0&osv=13_1_2"
            + "&pageci"
            + "=75c54f5e-8f42-11ea-9a17-c6f8c3503081&t=0&cflgs=AA**&ul=en-US&eactn=ANSTRIG&rq"
            + "=e7d7e9fe1710ad332cd15f28fff27545&pagename=ANSWERS_PLATFORM_PAGE&ciid=1%2BunV3Y*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De7d7e9fe1710ad332cd15f28fff27545%26node_id%3D2defb77a8a8cd7e6"
            + "%26REQUEST_GUID%3D171e7d7e-ac10-ad39-2797-eaa7fe8798ab%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31%2528%253F13515-vrubqst-whh-%2560dfz%252Behn-171e7d7ebab-0x132"
            + "%26cal_mod%3Dfalse&TPool=r1searchsvc&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=899"
            + "&ForwardedFor=23.33.238.125;172.58.157"
            + ".100&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.com&TMachine=10.110.87"
            + ".118&TStamp=19:36:59.69&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58.157.100&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/c/2222324832?ul_ref=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%26ul_ref%3Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F0%25252F0%25252F99%25253Floc%25253Dhttps%2525253A%2525252F"
            + "%2525252Frover.ebay.com%2525252Frover%2525252F1%2525252F711-219698-2056-1%2525252F2"
            + "%2525253Fmpre%2525253Dhttps%2525253A%2525252F%2525252Fwww.ebay"
            + ".com%2525252Fc%2525252F2222324832%25252526rule%2525253D%25252526crlp"
            + "%2525253D289697120826%25252526adpos%2525253D%25252526device%2525253Dm%25252526cmpgn"
            + "%2525253D344175529%25252526PARM3_ID%2525253DGBH_165%25252526gclid"
            + "%2525253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%252526rvr_id%25253D0%252526rvr_ts"
            + "%25253De7d7e70e1710aa162630649effdf75d8%26srcrot%3D711-219698-2056-1%26rvr_id"
            + "%3D2410463730471%26rvr_ts%3De7d7e7f41710acc3c572f8d7ffdfed95");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(3564);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2552134);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.674")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%2877701%3E0%2Busqdrrp%2Btil%2Bceb%7C%28dlh-171e7d7eb9a"
            + "-0xe6");
    ubiEvent.setEventFamily("LST");
    ubiEvent.setEventAction("SERV");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(508275715031L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "app=3564&res=480x320&flgs=AA**&efam=LST&g=d52fb1481710a4d264558da69f8a715d&h=48&mobile"
            + "=true&n=d52fb1481710a4d264558da69f8a715b&uc=1&mos=iOS&p=2552134&bs=0&osv=13_1_2&t=0"
            + "&cflgs=AA**&ul=en-US&itmattr=bhEAAB%2BLCAAAAAAAAAC9mEuzqkYUhf"
            + "%2BLU07Kbt6cqgwERFBQRMVHkkGDzUN5g4Dcuv89nkplJINUdc51hI%2Fiq9177bUX%2FpgUyeTzjx"
            + "%2BTuEknnxN6EFAYs%2FXkY1In1euD2XY2n71ec9M"
            + "%2BB55BMYqM1ZWHC3mHnf3999cPE4yCySeEQICvN3V8nXyCj0lcF9U%2FF0369fXHpEBh9rr4"
            + "%2BfEvjEmvkc"
            + "%2Fw5TtMN"
            +
            "%2B9iGLmPYoZrt1dOVznqpmciGAekvksC7x1mza6aYpxjq2TpwhbljZIxakhWmdiDLveYd9hmjfhnaPinSt4vt4uLw99lnhCW4oi%2F1eAdNjvQfBhvq6lx6ERnJXrw6DkGIcwT%2BK7r32GqsaqdAqC2SQs%2B0SD0H0u5Jq3M7zIEf0llrPjIYZyF7zDjHN26MO8umqsl%2FT7NxZOtsGSVxaBlmPQ5Iv1o9ejtfrOwNaVxpVTlslNC2rPXMeat%2BEuOEYYYN1JRvMPWBQWL5a6xb%2Bc5Xd2X0VpnSrKhpn3Ig%2BQx4iCyERvaqSqWIJ72cpkVUVyst2QCkUQu6CA%2F4iA6cjXLVTutxo6HFGmdHzCpN%2BI7oON4pDLdG3YSvToIEdeKC5tLn%2B2RtGdhwzTpfcSujPRJaVrxVPn91rv6fZOjTU7WM6%2FnB8yiX9QzoffYMB2BpXoHZ77vLbKZuAdTsL1uKzIjZtNCqstwZKjNy2wl29EQ4KOdAXfHc7c4IJM%2BEw1M3I65%2Fjc4CMu2aVXeuxHXtyJJY7ho1y%2FQg9%2F09BKujTmZGguhgSGL32HatOxCOajgZndBshesbLfeWWSVVZ6YMV4%2BUlkG%2Bq2q3lVQRqZ%2BuOdx4z7JjpFOxDKrgpFjXJlctvCl5GiZ%2FcEfPCQdn%2FWMLPCwJXjS6PEOmydOWgValDS45bB05BK41EVi6VdNO2JX3yB9OmwF1h%2BakX0motxUmfvejKhL68ydma48%2F7tAwNiclWXJelB4h9lnVd5fZ6A6WqDpHXyYhUVGGAtSP6BzKXuHLeypvp42hnhxcLQ3lCiEAkdmxBzAgSd0I4HHxHxA7XZlXT8GM%2BP01lo5W8IMEgPxlvfcSGWWHznxQ0eQWg8rOOhaAAMyI6YH5GW0NDLU1tlJTpsep2p3ydSd4dLb4daRwR4eV%2FPJSAaxp%2BKcqS5PrKPaFUvzdkNxTOaNzFDDocfsSEhlUZ%2ByBt94dl6ngrmOD%2BcL2VC%2FYkGL%2B9uI9L8hFnBsHVRYHJG%2BLpur604QD9n6lvuCoD06g9D1mRZj%2FhFVI94IeC2XFAtAd2F4c1bbiJcdIN1nhZfDdkSNwzY4XZcCghW3Kk%2B8e4hWEVnPmDZohNgfUaNq7n3g166FTymSnVupPL0L4aa%2Bt3gA95F%2FC6xOL4Ncmd8MhuqOqr7eCweLbJ%2B9Ag%2BLQTsSUv%2B%2FwPPX697br3vv8zqKPURx3J8TSklQXVPsnTokTYUoXaXo12NHQe1SVDWUOVepvUsdp5T8iJPmtzijlKjKX89cqP5aUX4x%2BaQ5locC%2FfNvt44U4m4RAAA%3D&eactn=SERV&rq=e7d7e9fe1710ad332cd15f28fff27545&pagename=Organic tracking for PL&ciid=1%2BuXV3Y*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D611af91a0345eb53%26node_id%3Ddd0eaa931e7a2466%26REQUEST_GUID"
            + "%3D171e7d7e-b970-a6e5-7763-0003fdf3258c%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%252877701%253E0%252Busqdrrp%252Btil%252Bceb%257C%2528dlh-171e7d7eb9a-0xe6%26cal_mod"
            + "%3Dfalse&TPool=r1searchsvc&TDuration=1&TStatus=0&TType=URL&ContentLength=2344"
            + "&ForwardedFor=23.33.238.125;172.58.157"
            + ".100&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.com&TMachine=10.110.87"
            + ".118&TStamp=19:36:59.67&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58.157.100&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/c/2222324832?ul_ref=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%26ul_ref%3Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F0%25252F0%25252F99%25253Floc%25253Dhttps%2525253A%2525252F"
            + "%2525252Frover.ebay.com%2525252Frover%2525252F1%2525252F711-219698-2056-1%2525252F2"
            + "%2525253Fmpre%2525253Dhttps%2525253A%2525252F%2525252Fwww.ebay"
            + ".com%2525252Fc%2525252F2222324832%25252526rule%2525253D%25252526crlp"
            + "%2525253D289697120826%25252526adpos%2525253D%25252526device%2525253Dm%25252526cmpgn"
            + "%2525253D344175529%25252526PARM3_ID%2525253DGBH_165%25252526gclid"
            + "%2525253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%252526rvr_id%25253D0%252526rvr_ts"
            + "%25253De7d7e70e1710aa162630649effdf75d8%26srcrot%3D711-219698-2056-1%26rvr_id"
            + "%3D2410463730471%26rvr_ts%3De7d7e7f41710acc3c572f8d7ffdfed95");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(3564);
    ubiEventList.add(ubiEvent);

    // 6
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.908")));
    ubiEvent.setRemoteIP("10.69.230.106");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E11%28twwgsvv%28umj%28bad%7F%29%60jk-171e7d7ec85-0x697b2c");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(756225010903L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&c=1&g=d52fb1481710a4d264558da69f8a715d&h=48&px=4249&chnl=9&n=d52fb1481710a4d264558da69f8a715b&uc=1&es=0&nqt=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&p=2317508&uaid=e7d7ec841710a4d12b00c9719e4c85e6S0&bs=0&t=0&cflgs=QA**&ul=en-US&plmt=wxUAAB%252BLCAAAAAAAAADFWFtz2jgU%252Fisdvy6d6OoLM%252FsAuWxJeknoNmna6TDGlo2IsYwvkNDJf98jYy5JMHW3ScuLLFk%252B%252Bs4nHZ3v8N1IQqNNODOxRVpGIo321%252B%252BG9I02RhjZMOTBEEHYQSbGyKEIOS0jhTGDMZMh0wscRxBmesw2A4yYZ%252BtZpsUCo2WIHMx9g9Zdmq2sw8dkYbmhZBlMmhjtuIiilpGp1VNitBllDm8ZAQBpGTIz2t8NC74DDBY27u%252FBqEzStTn3JiuIbRl6XG1WYWxuDwMeLFcxksR4tAos4oA%252FUbnK9loIpsNkvGzIsqHLhi0bDg2C1tSwEKIWRyZ3LBjZAG0ZtnaW6lEHnjoXneMO%252FI57Y%252F425%252FNRYd9e2YvpqSqiEHf%252B1kuiyi7GW2awhlA%252BaBC8fM9WE3n57v6%252BVXHrBRRPlar1mmFmotJrss9rrD1DbYypiRyTIsDQhAobUWxxSllDKj4s%252FnpzwLvZ3dEXNMVE8JszNd5QgX%252BNCmyiqJjWU7E%252BAPSXDkAzV7s92Tv5nCanSB7cdqdxMpLJ%252B4vn2XV8p6ZT7GX1rhKrcpW9wK7rAMDMYg7%252B87tOJ2JkjjNUURGrWDwhQ0d%252BSQZvRAZwwcBHbDUhg1BEOLds0%252BHNyOh8ImYoL9KD3qe53T%252Bzh%252Fhq2O81IaOeAj9PCiZqTwN3VgffbHYasEkQEGA3IgASAqKQU5q5f8hOu2fR57cCf5hY3T73cPyx28j9JjeAOVTp1K1PAWRFhPX8YWEhSpGDSE1asH%252FrXRgWSSZ8VMsEWR8J%252B5fuQooo5ACTWKiZ0y%252BZC6k9D2Voz%252FZcBdThaJkNnd%252BQAk4mX6xTkpz8G%252BaO0%252B%252BRq8sQ8%252BsmztZf%252FKNkmpPxvhxXpXuMnv%252BIM0QJSEGCzD948YPsczUXtm2DQIU37gx6RzLzLrUTE1lKncmt0QYGlLdkws3KrquWjDz4IvM1P0OR5f8UIsvOU%252BUXXt6DUeO1BpOr3I0%252ByoUYpO5cg3D0sc6E8A%252FdXIQqvdtyAViophGNmhC86pYv7ZUNSrXlkZgI6L1Xr8rHV4EqYr807qbe6KIQpe3SzupLRqvFt3E%252BzEKlrdeul8sZANTvAzfKxLYrGkKFSEfOEjg8OcY9kAbn5qvhuUmiV5Ge%252FlKlMpQxfOypVPdTN76RcbjqittEeLBWX8xEXOgR1%252B%252FD4ldChqMcuiM304SDf7onEqndHEwCMfDcvDzFRVoulIkoEmlPv05SNVHaAahM3LwcilUnz1M5LHKpYm1W5THs2ZGb61s%252FykqzMp%252FoMiCJPHeS5MY3fWS8vNx5D9hAZTRVZY%252BFt8seKHwoIYhXZU%252FgkYDRADuU%252BdA6NiYEmB9SBw8ZLw%252Ff85Y9m2BoWvaA%252FvHmambvufQ2%252BmdH4fNY8GyH4yoaaTP9wqrpvGrLK%252BZ%252FKrUdxcrvQ8pYEfjkJq3PnqSmktjG9ABLfd57iGF5rfItMGR4Zy%252FGSX2FR9epnDUEU1%252Ba%252FAgMnPnbeRQM96VY09mttmvhvOv4J4e9a%252Fluykhybnc%252FHMb0KHwKZ7M%252Fk8RJ85msUESqPs2bDTG8%252FdI5656PFoG4Oo%252FR5UeTj2VwvQfDT1Sb1otvy0%252F8C7BD7j2WAT8MrPq0vjuwdgBurk%252Bdl48wMlvEs1jWgqF4%252Fc%252FRDlH1R%252BhLJ1PPpPX0bf7rwi%252FP38L2EHdGey4FwnEVkJg0xHN8Td5Zbv8yfHMTjkf%252BUV8dLKzjXRG5EoQY7EBuWCtCEbhFlO%252BWhOSxJFyKwInyRfTeLUXZTCaDGR8NkvnANwc5fyrL1lJppTQeKJot9bKlaR4LmEqrVMplW848lS3f7v8DPxGNysMVAAA%253D&ec=2&pn=2&rq=e7d7e9fe1710ad332cd15f28fff27545&pagename=cos__mfe&po=%5B%28pg%3A2546172+pid%3A101082+pladvids%3A%5B0%5D%29%28pg%3A2546172+pid%3A101071+pladvids%3A%5B0%5D%29%5D&ciid=1%2ByFErA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De7d7e9fe1710ad332cd15f28fff27545%26node_id%3Dd3eef1d981cc4bc0"
            + "%26REQUEST_GUID%3D171e7d7e-c840-a4d1-2b06-6191ca9ca62f%26logid%3Dt6qjpbq%253F"
            + "%253Cumjthu%2560t%2A517%253E11%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk"
            + "-171e7d7ec85-0x697b2c%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType"
            + "=URL"
            + "&ContentLength=-1&ForwardedFor=172.58.157"
            + ".100&Script=/roverimp/0/2546172/9&Server=internal.rover.vip.ebay.com&TMachine=10"
            + ".77.18"
            + ".176&TStamp=19:36:59.90&TName=roverimp&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.1 Mobile/15E148 Safari/604.1,GingerClient/2.9.7-RELEASE&RemoteIP=10.69"
            + ".230.106"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2546172/9?site=0&trknvp=n%3Dd52fb1481710a4d264558da69f8a715b%26plmt%3DwxUAAB"
            + "%252BLCAAAAAAAAADFWFtz2jgU%252Fisdvy6d6OoLM"
            + "%252FsAuWxJeknoNmna6TDGlo2IsYwvkNDJf98jYy5JMHW3ScuLLFk%252B"
            + "%252Bs4nHZ3v8N1IQqNNODOxRVpGIo321%252B"
            + "%252BG9I02RhjZMOTBEEHYQSbGyKEIOS0jhTGDMZMh0wscRxBmesw2A4yYZ"
            +
            "%252BtZpsUCo2WIHMx9g9Zdmq2sw8dkYbmhZBlMmhjtuIiilpGp1VNitBllDm8ZAQBpGTIz2t8NC74DDBY27u"
            + "%252FBqEzStTn3JiuIbRl6XG1WYWxuDwMeLFcxksR4tAos4oA"
            + "%252FUbnK9loIpsNkvGzIsqHLhi0bDg2C1tSwEKIWRyZ3LBjZAG0ZtnaW6lEHnjoXneMO%252FI57Y"
            + "%252F425"
            + "%252FNRYd9e2YvpqSqiEHf%252B1kuiyi7GW2awhlA%252BaBC8fM9WE3n57v6"
            + "%252BVXHrBRRPlar1mmFmotJrss9rrD1DbYypiRyTIsDQhAobUWxxSllDKj4s"
            + "%252FnpzwLvZ3dEXNMVE8JszNd5QgX%252BNCmyiqJjWU7E%252BAPSXDkAzV7s92Tv5nCanSB7cdqdxMpLJ"
            + "%252B4vn2XV8p6ZT7GX1rhKrcpW9wK7rAMDMYg7%252B87tOJ2JkjjNUURGrWDwhQ0d"
            + "%252BSQZvRAZwwcBHbDUhg1BEOLds0%252BHNyOh8ImYoL9KD3qe53T%252Bzh"
            +
            "%252Fhq2O81IaOeAj9PCiZqTwN3VgffbHYasEkQEGA3IgASAqKQU5q5f8hOu2fR57cCf5hY3T73cPyx28j9JjeAOVTp1K1PAWRFhPX8YWEhSpGDSE1asH%252FrXRgWSSZ8VMsEWR8J%252B5fuQooo5ACTWKiZ0y%252BZC6k9D2Voz%252FZcBdThaJkNnd%252BQAk4mX6xTkpz8G%252BaO0%252B%252BRq8sQ8%252BsmztZf%252FKNkmpPxvhxXpXuMnv%252BIM0QJSEGCzD948YPsczUXtm2DQIU37gx6RzLzLrUTE1lKncmt0QYGlLdkws3KrquWjDz4IvM1P0OR5f8UIsvOU%252BUXXt6DUeO1BpOr3I0%252ByoUYpO5cg3D0sc6E8A%252FdXIQqvdtyAViophGNmhC86pYv7ZUNSrXlkZgI6L1Xr8rHV4EqYr807qbe6KIQpe3SzupLRqvFt3E%252BzEKlrdeul8sZANTvAzfKxLYrGkKFSEfOEjg8OcY9kAbn5qvhuUmiV5Ge%252FlKlMpQxfOypVPdTN76RcbjqittEeLBWX8xEXOgR1%252B%252FD4ldChqMcuiM304SDf7onEqndHEwCMfDcvDzFRVoulIkoEmlPv05SNVHaAahM3LwcilUnz1M5LHKpYm1W5THs2ZGb61s%252FykqzMp%252FoMiCJPHeS5MY3fWS8vNx5D9hAZTRVZY%252BFt8seKHwoIYhXZU%252FgkYDRADuU%252BdA6NiYEmB9SBw8ZLw%252Ff85Y9m2BoWvaA%252FvHmambvufQ2%252BmdH4fNY8GyH4yoaaTP9wqrpvGrLK%252BZ%252FKrUdxcrvQ8pYEfjkJq3PnqSmktjG9ABLfd57iGF5rfItMGR4Zy%252FGSX2FR9epnDUEU1%252Ba%252FAgMnPnbeRQM96VY09mttmvhvOv4J4e9a%252Fluykhybnc%252FHMb0KHwKZ7M%252Fk8RJ85msUESqPs2bDTG8%252FdI5656PFoG4Oo%252FR5UeTj2VwvQfDT1Sb1otvy0%252F8C7BD7j2WAT8MrPq0vjuwdgBurk%252Bdl48wMlvEs1jWgqF4%252Fc%252FRDlH1R%252BhLJ1PPpPX0bf7rwi%252FP38L2EHdGey4FwnEVkJg0xHN8Td5Zbv8yfHMTjkf%252BUV8dLKzjXRG5EoQY7EBuWCtCEbhFlO%252BWhOSxJFyKwInyRfTeLUXZTCaDGR8NkvnANwc5fyrL1lJppTQeKJot9bKlaR4LmEqrVMplW848lS3f7v8DPxGNysMVAAA%253D%26po%3D%5B%28pg%3A2546172+pid%3A101082+pladvids%3A%5B0%5D%29%28pg%3A2546172+pid%3A101071+pladvids%3A%5B0%5D%29%5D&trknvpsvc=%3Ca%3Enqc%3DAAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*%26nqt%3DAAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*%26es%3D0%26ec%3D2%3C%2Fa%3E&tguid=d52fb1481710a4d264558da69f8a715d&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 7
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2455605);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.539")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%285222%3F5-vrubqst-whh-%60dfz%2Behn-171e7d7eb14-0xe3");
    ubiEvent.setEventFamily("PRP");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(522150669015L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&flgs=AA**&saasll=0&uc=1&nqt=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&mos=iOS&bs=0&pageci=2dba9a86-f91b-4411-a1f0-56155bc58605&osv=13_1_2&ul=en-US&pcl=2&callingpagename=viexpsvc__VIEWPRODUCTCLUSTER&svctime=116&ec=2&pagename=prpexpsvc__product_v1_module_provider_PRODUCTID_GET&app=3564&prpnewui=true&res=480x320&efam=PRP&pri=2222324832&g=d52fb1481710a4d264558da69f8a715d&h=48&mobile=true&cp=2546137&n=d52fb1481710a4d264558da69f8a715b&es=0&p=2455605&t=0&cflgs=EA**&eactn=EXPM&respCode=200&rq=e7d7e9fe1710ad332cd15f28fff27545&pooltype=production&ciid=1%2Bqaknk*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4ba94750bebb0569%26node_id%3D6752aec22436e623%26REQUEST_GUID"
            + "%3D171e7d7e-b100-ad39-2797-eaa7fe8798a4%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%25285222%253F5-vrubqst-whh-%2560dfz%252Behn-171e7d7eb14-0xe3%26cal_mod%3Dfalse"
            + "&TPool"
            + "=r1prpexpsvc8&TDuration=2&TStatus=0&TType=URL&ContentLength=1224&ForwardedFor=23.33"
            + ".238.125;172.58.157.100&Script=/trk20svc/TrackingResource/v1&Server=www.ebay"
            + ".com&TMachine=10.211.146.121&TStamp=19:36:59.53&TName=Ginger.CollectionSvc"
            + ".track&Agent=Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X)"
            + " AppleWebKit/605"
            + ".1.15 (KHTML, like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58"
            + ".157.100&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/c/2222324832?ul_ref=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%26ul_ref%3Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F0%25252F0%25252F99%25253Floc%25253Dhttps%2525253A%2525252F"
            + "%2525252Frover.ebay.com%2525252Frover%2525252F1%2525252F711-219698-2056-1%2525252F2"
            + "%2525253Fmpre%2525253Dhttps%2525253A%2525252F%2525252Fwww.ebay"
            + ".com%2525252Fc%2525252F2222324832%25252526rule%2525253D%25252526crlp"
            + "%2525253D289697120826%25252526adpos%2525253D%25252526device%2525253Dm%25252526cmpgn"
            + "%2525253D344175529%25252526PARM3_ID%2525253DGBH_165%25252526gclid"
            + "%2525253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%252526rvr_id%25253D0%252526rvr_ts"
            + "%25253De7d7e70e1710aa162630649effdf75d8%26srcrot%3D711-219698-2056-1%26rvr_id"
            + "%3D2410463730471%26rvr_ts%3De7d7e7f41710acc3c572f8d7ffdfed95");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(3564);
    ubiEventList.add(ubiEvent);

    // 8
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.690")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%2877701%3E4%2Busqdrrp%2Btil%2Bceb%7C%28dlh-171e7d7ebaa"
            + "-0x10a");
    ubiEvent.setEventFamily("LST");
    ubiEvent.setEventAction("ANSTMPL");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(508276698071L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "app=3564&res=480x320&flgs=AA**&efam=LST&g=d52fb1481710a4d264558da69f8a715d&h=48&mobile"
            + "=true&pagetmpl=SRP&n=d52fb1481710a4d264558da69f8a715b&uc=1&mos=iOS&p=2370942&bs=0"
            + "&osv"
            + "=13_1_2&pageci=75c54f5e-8f42-11ea-9a17-c6f8c3503081&t=0&cflgs=AA**&ul=en-US&eactn"
            + "=ANSTMPL&rq=e7d7e9fe1710ad332cd15f28fff27545&pagename=ANSWERS_PLATFORM_PAGE&ciid=1"
            + "%2BumV3Y*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De7d7e9fe1710ad332cd15f28fff27545%26node_id%3Dd7d8aa59123bfba4"
            + "%26REQUEST_GUID%3D171e7d7e-ac10-ad39-2797-eaa7fe8798ab%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31%252877701%253E4%252Busqdrrp%252Btil%252Bceb%257C%2528dlh"
            + "-171e7d7ebaa-0x10a%26cal_mod%3Dfalse&TPool=r1searchsvc&TDuration=1&TStatus=0&TType"
            + "=URL"
            + "&ContentLength=871&ForwardedFor=23.33.238.125;172.58.157"
            + ".100&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.com&TMachine=10.110.87"
            + ".118&TStamp=19:36:59.69&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58.157.100&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/c/2222324832?ul_ref=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%26ul_ref%3Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F0%25252F0%25252F99%25253Floc%25253Dhttps%2525253A%2525252F"
            + "%2525252Frover.ebay.com%2525252Frover%2525252F1%2525252F711-219698-2056-1%2525252F2"
            + "%2525253Fmpre%2525253Dhttps%2525253A%2525252F%2525252Fwww.ebay"
            + ".com%2525252Fc%2525252F2222324832%25252526rule%2525253D%25252526crlp"
            + "%2525253D289697120826%25252526adpos%2525253D%25252526device%2525253Dm%25252526cmpgn"
            + "%2525253D344175529%25252526PARM3_ID%2525253DGBH_165%25252526gclid"
            + "%2525253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%252526rvr_id%25253D0%252526rvr_ts"
            + "%25253De7d7e70e1710aa162630649effdf75d8%26srcrot%3D711-219698-2056-1%26rvr_id"
            + "%3D2410463730471%26rvr_ts%3De7d7e7f41710acc3c572f8d7ffdfed95");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(3564);
    ubiEventList.add(ubiEvent);

    // 9
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.534")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%28706%3D32-vrubqst-whh-%60dfz%2Behn-171e7d7eb0f-0x109");
    ubiEvent.setEventFamily("LST");
    ubiEvent.setEventAction("ANSTRIG");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(657600539607L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "app=3564&res=480x320&flgs=AA**&efam=LST&g=d52fb1481710a4d264558da69f8a715d&h=48&mobile"
            + "=true&n=d52fb1481710a4d264558da69f8a715b&uc=1&mos=iOS&p=2370942&bs=0&osv=13_1_2"
            + "&pageci"
            + "=75c0460a-8f42-11ea-be96-b259064e8d6b&t=0&cflgs=AA**&ul=en-US&eactn=ANSTRIG&rq"
            + "=e7d7e9fe1710ad332cd15f28fff27545&pagename=ANSWERS_PLATFORM_PAGE&ciid=1%2BsLHJk*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De7d7e9fe1710ad332cd15f28fff27545%26node_id%3D0ad20c8f02806721"
            + "%26REQUEST_GUID%3D171e7d7e-aa30-ad39-2797-eaa7fe8798b2%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31%2528706%253D32-vrubqst-whh-%2560dfz%252Behn-171e7d7eb0f-0x109"
            + "%26cal_mod%3Dfalse&TPool=r1searchsvc&TDuration=1&TStatus=0&TType=URL&ContentLength"
            + "=899"
            + "&ForwardedFor=23.33.238.125;172.58.157"
            + ".100&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.com&TMachine=10.155.28"
            + ".153&TStamp=19:36:59.53&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58.157.100&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/c/2222324832?ul_ref=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%26ul_ref%3Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F0%25252F0%25252F99%25253Floc%25253Dhttps%2525253A%2525252F"
            + "%2525252Frover.ebay.com%2525252Frover%2525252F1%2525252F711-219698-2056-1%2525252F2"
            + "%2525253Fmpre%2525253Dhttps%2525253A%2525252F%2525252Fwww.ebay"
            + ".com%2525252Fc%2525252F2222324832%25252526rule%2525253D%25252526crlp"
            + "%2525253D289697120826%25252526adpos%2525253D%25252526device%2525253Dm%25252526cmpgn"
            + "%2525253D344175529%25252526PARM3_ID%2525253DGBH_165%25252526gclid"
            + "%2525253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%252526rvr_id%25253D0%252526rvr_ts"
            + "%25253De7d7e70e1710aa162630649effdf75d8%26srcrot%3D711-219698-2056-1%26rvr_id"
            + "%3D2410463730471%26rvr_ts%3De7d7e7f41710acc3c572f8d7ffdfed95");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(3564);
    ubiEventList.add(ubiEvent);

    // 10
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(true);
    ubiEvent.setPageId(3084);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:58.508")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("2762a2860cae2991");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ckuvthu%60t*023%3F%3B%3C6%29pqtfwpu%29osu%29fgg%7E-fij-171e7d7e70d-0x120");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(426846840791L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "rdt=1&c=1&g=d52fb1481710a4d264558da69f8a715d&h=48&cguidsrc=cookie&n"
            + "=d52fb1481710a4d264558da69f8a715b&uc=1&url_mpre=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB&p=3084&uaid=e7d7e70d1710aa1626357806c001f919S0&bs=0"
            + "&t=0"
            + "&cflgs=EA**&ul=en-US&hrc=301&pn=2&pcguid=d52fb1481710a4d264558da69f8a715b&rq"
            + "=2762a2860cae2991&pagename=EntryTracking&ciid=1%2BcNYmM*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D2762a2860cae2991%26node_id%3D1d83ed830144a470%26REQUEST_GUID"
            + "%3D171e7d7e-70c0-aa16-2637-eb47dde5c67f%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t"
            + "%2A023"
            + "%253F%253B%253C6%2529pqtfwpu%2529osu%2529fgg%257E-fij-171e7d7e70d-0x120%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=172.58.157.100&Script=/rover/0/0/99&Server=rover.ebay.com&TMachine=10.161.98"
            + ".99&TStamp=19:36:58.50&TName=rover&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58.157.100&Encoding=gzip,"
            + " deflate, br"));
    ubiEvent.setUrlQueryString(
        "/rover/0/0/99?loc=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB");
    ubiEvent.setPageName("rover");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 11
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2546137);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.984")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%284%3F%3B30%3F-vrubqst-whh-%60dfz%2Behn-171e7d7ecd1"
            + "-0x28d");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(991089060567L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxIIEAAAAAAAIAcQAEABAAgEABgAAAAAAwAAABAAAiAAAAAAAAUEA*");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc"
            +
            "=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&flgs=AIAxIIEAAAAAAAIAcQAEABAAgEABgAAAAAAwAAABAAAiAAAAAAAAUEA*&ssc=27034591012&gsp=0&viStreamId=d52fb1481710a4d264558da69f8a715d&snippetlength=218&obfs_sid_uid_same=false&mos=iOS&nqt=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&ppc_ofid=6342819202&osv=13_1_2&callingpagename=viexpsvc__VIEWPRODUCTCLUSTER&viSignedInFlag=0&noep=3&vibisdm=683X1024&app=3564&itmSrc=FB_L&!_OBFS_SELLERID=1301608862&bdrs=0&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=1095&!_callingpageid=2546172&swccount=1&invThreshold=5&meta=293&OBFS_STATUS=NOT_REQUIRED&curprice=434.95&maturity_score=5&viPageType=0&attrct=18&ccc_off_id=6342779101&!pymntMethods=PPL|VSA|MSC|AMX|DSC&rq=e7d7e9fe1710ad332cd15f28fff27545&l1=32852&l2=184972&qtys=11&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=0&bs=0&OBFS_ITEMID=233356539340&!viqsig=URGENCY%2CLAST_ONE_SIGNAL|SHIPPING%2CFREE_SHIPPING_SIGNAL|POPULARITY%2CVIEWERS_COUNT_1_HOUR_SIGNAL|RETURNS%2CSELLER_ACCEPT_RETURNS_SIGNAL,URGENCY%2CLAST_ONE_SIGNAL|SHIPPING%2CFREE_SHIPPING_SIGNAL|POPULARITY%2CVIEWERS_COUNT_1_HOUR_SIGNAL|RETURNS%2CSELLER_ACCEPT_RETURNS_SIGNAL&binamt=434.95&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=683X1024&sn=createmagic14&qtya=1&st=9&c=1&pudo=0&vibisbm=1000&g=d52fb1481710a4d264558da69f8a715d&h=48&leaf=11071&cp=2546137&prd_qualify=VALID&n=d52fb1481710a4d264558da69f8a715b&!_OBFS_PHONE_COUNT=0&p=2546137&fdp=99&t=0&nofp=3&rpdur=30&tr=2160850&dc=1&!vidsig=URGENCY%2CLAST_ONE_SIGNAL|EMPHASIS|POPULARITY%2CVIEWERS_COUNT_1_HOUR_SIGNAL%2C2|DEFAULT|RETURNS%2CSELLER_ACCEPT_RETURNS_SIGNAL%2C30|DEFAULT,URGENCY%2CLAST_ONE_SIGNAL|EMPHASIS|POPULARITY%2CVIEWERS_COUNT_1_HOUR_SIGNAL%2C2|DEFAULT|RETURNS%2CSELLER_ACCEPT_RETURNS_SIGNAL%2C30|DEFAULT&nozp=3&rating=4.57&!_OBFS_LINK_COUNT=0&uc=1&mbsa=500&MskuCtbRdt=0&reviews=7&ul=en-US&hrc=200&pymntVersion=1&ec=2&res=480x320&guwatch=true&efam=ITM&itm=233356539340&pri=2222324832&vpcg=false&epidonvi=23025578695&iver=2727126462013&!_OBFS_BUYERID=0&es=0&clp_iid_epid_mismatch=true&itmtitle=Smart+TV+4K+Samsung+50+Inch+LED+2160P+Ultra+HD+Built+In+WiFi+HDMI+USB+Best+New&cflgs=EA**&bcdata=t%3A5%3Bbc%3A1586461038455%2C1865552%2C1648392%2C115077697%2C738302&gxoe=vine&merchdisp=101082%2C101071&simSrc=FB_L&fbcause=IIS_THRESHOLD&eactn=EXPC&shipsiteid=0&obfs_listing_is_eligible=true&nw=61&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&oosType=FB_L&ppfoid=0&pagename=viexpsvc__VIEWPRODUCTCLUSTER&!sh1srv=USD%3A0.0%5E-1%5E-1%5E2020-05-13T03%3A00%3A00-07%3A00%5E2020-05-27T03%3A00%3A00-07%3A00%5EECONOMY%5EOther%5E0%5E-1%5E5%5E-1%5EUS%5Enull%5E233356539340%5E-1%5E5%5E14&qtymod=true&bcvalid=true&mobile=true&iisll=0&fimbsa=500&pidinstock=false&vidsigct=3&cp_usd=434.95&swcembg=true&!_OBFS_EMAIL_COUNT=0&ciid=1%2BqCweY*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dd9428d23d72646c7%26node_id%3Db4caec094767e2b9%26REQUEST_GUID"
            + "%3D171e7d7e-ccd0-a69c-1e64-0d7fff6cc498%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%25284%253F%253B30%253F-vrubqst-whh-%2560dfz%252Behn-171e7d7ecd1-0x28d%26cal_mod"
            + "%3Dfalse&TPool=r1viexpsvc3&TDuration=2&TStatus=0&TType=URL&ContentLength=3945"
            + "&ForwardedFor=23.33.238.125;172.58.157"
            + ".100&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.com&TMachine=10.105.193"
            + ".230&TStamp=19:36:59.98&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58.157.100&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/c/2222324832?ul_ref=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%26ul_ref%3Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F0%25252F0%25252F99%25253Floc%25253Dhttps%2525253A%2525252F"
            + "%2525252Frover.ebay.com%2525252Frover%2525252F1%2525252F711-219698-2056-1%2525252F2"
            + "%2525253Fmpre%2525253Dhttps%2525253A%2525252F%2525252Fwww.ebay"
            + ".com%2525252Fc%2525252F2222324832%25252526rule%2525253D%25252526crlp"
            + "%2525253D289697120826%25252526adpos%2525253D%25252526device%2525253Dm%25252526cmpgn"
            + "%2525253D344175529%25252526PARM3_ID%2525253DGBH_165%25252526gclid"
            + "%2525253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%252526rvr_id%25253D0%252526rvr_ts"
            + "%25253De7d7e70e1710aa162630649effdf75d8%26srcrot%3D711-219698-2056-1%26rvr_id"
            + "%3D2410463730471%26rvr_ts%3De7d7e7f41710acc3c572f8d7ffdfed95");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(233356539340L);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(3564);
    ubiEventList.add(ubiEvent);

    // 12
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("d52fb1481710a4d264558da69f8a715d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2455605);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:36:59.702")));
    ubiEvent.setRemoteIP("172.58.157.100");
    ubiEvent.setRequestCorrelationId("e7d7e9fe1710ad332cd15f28fff27545");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%285262%3F4-vrubqst-whh-%60dfz%2Behn-171e7d7ebb7-0x174");
    ubiEvent.setEventFamily("PRP");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("172.58.157.100");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(522152241879L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&flgs=AA**&uc=1&nqt=AAAAAAAABAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACA*&mos=iOS&bs=0&pageci=2dba9a86-f91b-4411-a1f0-56155bc58605&osv=13_1_2&ul=en-US&pcl=2&callingpagename=viexpsvc__VIEWPRODUCTCLUSTER&glpsm=4551%2C&svctime=253&ec=2&pagename=prpexpsvc__product_v1_module_provider_PRODUCTID_GET&app=3564&prpnewui=true&res=480x320&efam=PRP&pri=2222324832&g=d52fb1481710a4d264558da69f8a715d&h=48&mobile=true&FBLCOUNT=9&cp=2546137&n=d52fb1481710a4d264558da69f8a715b&es=0&p=2455605&t=0&cflgs=EA**&eactn=EXPM&respCode=200&rq=e7d7e9fe1710ad332cd15f28fff27545&pooltype=production&ciid=1%2Bqyknk*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D7a9775d02f1ea667%26node_id%3De96cac226ee27324%26REQUEST_GUID"
            + "%3D171e7d7e-bb30-ad39-2797-eaa7fe87989b%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%25285262%253F4-vrubqst-whh-%2560dfz%252Behn-171e7d7ebb7-0x174%26cal_mod%3Dfalse"
            + "&TPool"
            + "=r1prpexpsvc8&TDuration=2&TStatus=0&TType=URL&ContentLength=1242&ForwardedFor=23.33"
            + ".238.125;172.58.157.100&Script=/trk20svc/TrackingResource/v1&Server=www.ebay"
            + ".com&TMachine=10.211.146.121&TStamp=19:36:59.70&TName=Ginger.CollectionSvc"
            + ".track&Agent=Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X)"
            + " AppleWebKit/605"
            + ".1.15 (KHTML, like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1&RemoteIP=172.58"
            + ".157.100&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/c/2222324832?ul_ref=https%3A%2F%2Frover.ebay"
            + ".com%2Frover%2F1%2F711-219698-2056-1%2F2%3Fmpre%3Dhttps%3A%2F%2Fwww.ebay"
            + ".com%2Fc%2F2222324832%26rule%3D%26crlp%3D289697120826%26adpos%3D%26device%3Dm"
            + "%26cmpgn"
            + "%3D344175529%26PARM3_ID%3DGBH_165%26gclid"
            + "%3DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%26ul_ref%3Dhttps%25253A%25252F%25252Frover.ebay"
            + ".com%25252Frover%25252F0%25252F0%25252F99%25253Floc%25253Dhttps%2525253A%2525252F"
            + "%2525252Frover.ebay.com%2525252Frover%2525252F1%2525252F711-219698-2056-1%2525252F2"
            + "%2525253Fmpre%2525253Dhttps%2525253A%2525252F%2525252Fwww.ebay"
            + ".com%2525252Fc%2525252F2222324832%25252526rule%2525253D%25252526crlp"
            + "%2525253D289697120826%25252526adpos%2525253D%25252526device%2525253Dm%25252526cmpgn"
            + "%2525253D344175529%25252526PARM3_ID%2525253DGBH_165%25252526gclid"
            + "%2525253DCj0KCQjwncT1BRDhARIsAOQF9LnseFFhRejiM4LLxjrhboQtptIVKpqW9YxgpFJ"
            + "-I_GJgmTLGPCjzScaAvCiEALw_wcB%252526rvr_id%25253D0%252526rvr_ts"
            + "%25253De7d7e70e1710aa162630649effdf75d8%26srcrot%3D711-219698-2056-1%26rvr_id"
            + "%3D2410463730471%26rvr_ts%3De7d7e7f41710acc3c572f8d7ffdfed95");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(3564);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(10, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric14() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e849ff051710a4d020811edeff6818ad");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:41:35.848")));
    ubiEvent.setRemoteIP("10.77.72.251");
    ubiEvent.setRequestCorrelationId("e82ae452e93b7b80");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*0631%3B7%28twwgsvv%28umj%28bad%7F%29%60jk-171e849ff69-0x198");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("162.14.22.99");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko)"
            + " Version/5.1 Safari/534.50,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(994392538953L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "c=1&g=e88ee98d1710acc3d3911a0ac0f5d4b2&rurl=https%25253A%25252F%25252Fwww"
            + ".ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&h=8d&px"
            + "=4249&chnl=9&uc=77&p=1605052&uaid=e88ee98e1710acc3d3911a0ac0f5d4b1S0&bs=77&catid=80"
            + "&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen"
            + ".de%2F&r=715462550&t=77®=Hamburg&cflgs=QA**&ul=de-DE&pn=2&rq=deeaab0130c48395"
            + "&pagename"
            + "=http://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=jumUPTk*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Ddeeaab0130c48395%26node_id%3D4b542f8f20dcfdc8%26REQUEST_GUID"
            + "%3D171e88ee-9860-acc3-d392-fdf6de68a87c%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0"
            + "%253D0%253F326-171e88ee995-0x109%26cal_mod%3Dfalse&TPool=r1rover&TDuration=16"
            + "&TStatus"
            + "=0&TType=URL&ContentLength=-1&ForwardedFor=90.186.49"
            + ".84&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.204.61"
            + ".57&TStamp=22:56:52"
            + ".23&TName=roverimp_INTL&Agent=Mozilla/5.0 (iPad; CPU OS 12_4_5 like Mac OS X)"
            + " AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604"
            + ".1&RemoteIP=90.186.49.84&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=reg%3DHamburg%26catid%3D80%26rurl%3Dhttps%25253A%25252F%25252Fwww"
            + ".ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&mpt"
            + "=1588744612147&imp=1605052");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e849ff051710a4d020811edeff6818ad");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2507874);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:41:36.151")));
    ubiEvent.setRemoteIP(null);
    ubiEvent.setRequestCorrelationId("e81bc51a1710a9e4f1e6a615ffff7af8");
    ubiEvent.setSid(null);
    ubiEvent
        .setRlogid("t6kjkbpujn%60%602%3D9vjdkjkbpujn%60%602*ib%60ks%28rbpv6770-171e84a0096-0x2353");
    ubiEvent.setEventFamily("PRLS");
    ubiEvent.setEventAction("ALGO");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP(null);
    ubiEvent.setAgentInfo(null);
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(275028115530L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("homesplice4.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "expl=0&meid=c6327233e276416a951874b8e59f3113&flgs=AA**&efam=PRLS&sv=9&g"
            + "=e849ff051710a4d020811edeff6818ad&h=05&snappy=1&schemaversion=3&ciid=SgD0CEA*&p"
            + "=2507874&t=0&cflgs=AA**&plmt=100012&pl"
            +
            "=vhrw1OKD0IS9XAAqU2ltcGxBTUx2NVBhaXJ3aXNlV2ViAAAC9pCQ2eEHAo7Z2OY7AkBlODQ5ZmYwNTE3MTBhNGQwMjA4MTFlZGVmZjY4MThhZAAAAgpDTjo6OgIyMC4xMTcwMC4yMDYyNS4yMDY2Ny4yMDY4MQAABBgCBm1scmYC6KnRzMYJAoSD5NtKAAAAYOZTKj8CAAAAAImxOj8AAAAAAADwPwQzMQAAAAACAhJlcGlkUHJpY2UCAACAPgAAAojSquqaBwAAAACAsQU0PwIAAADgKtFDPwk7FABACjI1LBU%2BIGFyYml0cmFnZRE%2BTIDdt9aQCgL0j8SaCAAAAKCFsSM%2FAX4QYKlXPD8JQwTwPzJDAEKBACzUmb3whwoCuIeN0BAJMgQAAAFDHQFywQAU7tvWjtsVAS4MQA57RgV%2BDEBnBVwNfggMQAQBvgACDQIAAAX0SOb68bTuFgKqmdmKYQAAAMDwJSUFOAzArotCDThycwBI%2Fq%2BhwtEGAsSi748JAAAAYJq0FAVADOASQjeOQAAQzPK0sa0hMAkBOu4AABwNswgqAioBtwmzLLKooaDGFALmqaq9JQGzCKTqLgVzDGCZWD82cwAMAhICEhk4FNLgyL%2BcBwFoDED0lRsFMxiArdkzP5qZAQEACQlrBAAAThoCEOj2oNjhBTsMgDjwHgU7DOBmkUA6bgAIIAIgGW4M%2BMiH4gkzDGCsdysFMwxAdptDDTMAFAluDAIWAhYZM0ia25a17hYCttbF9wsAAABAZ%2F8pCTgI%2FIlFDThyjAEUuPnThvwPAeEM4D0nQwU7DGAUVlk2OwAMAgQCBBlzLNKx6ZL6CAL63fXnBkHvCFEcMQU4CMAyvxHeUTcIDAIMHTgA8Y63AQgiAiIZMyzqqNyj%2BQ8Cor%2BipQUB3ghxDiYFaxSgVII3PzMFAQARKRZWhAEs4ruOhcwNAryuwI5SAasIDBU7BUAMQHhOUg3jDatWQAAs2pX%2F7%2BsWApqDk8UWAYAIgLsqBUAMwFdsOA1AABYy4wMEJmFx4wx8ZXBpNisEEOzEuNCfIaUQAEA9AU4FSAhAJOZxagAQMkgARisEGNahr47sFgKlBiGvCAUvOgVDDMBfn01VjxAQMjQsMp1xQoQAEMjq4LeoAcsQACDHWi8FQQggiFExt00qDAIQAhA5hCzutpWJpRYCnJqMtg0BeQEBOnMDDTglRDUBkeQUzIPXhZEKFbkMYMvGQAV4DEAAr1MNuQARhrkAEKSUmb6yRagFATqBAAD4NiUFQoQAFOSFu877D3UmCGCKTynIDKCtmUYtyAAIbkgCSKKnjq7oDgLe2ZOUDgAAAGARNyAFxAygSWA9DUAtBAwCHgIeOTwU2MzAj%2B8SAbIMoNHaMwUzDGCvs0cNMwAACXMMAgoCChkzMKDCh%2Ff0CAL%2BxPbSnAEBOgEBFe9F8wQzEw05CCgCKBk5KOTf4d2IEQK45P3lwQkI4BjFCaQIgKyWkiwDKOTSuJzKDQKK8IrHAeQM4KJfLAWxDCAN0Ts2bAMMAhQCFBl4DPi%2B%2B9s9XAjAiL4p4AxgwCRWDektHEUgQpMCLNiPsbr3CAKq8vXWDAHsCJ1pMAV4CGA1hTGcfgQHKPr2%2B%2FjnDgKEzciHgScIoDPFCYMIgP3YUWMACS0zCAYCBhm7EPbGhsnnAe0MAMB3u%2Bl6FCAPzDo%2FZgUBAAoNMwgcAhwZMxCSiIuUkOUvCAAO5AmpDCCIPFMN6THSCA4CDhkzEKSLm7nfxQkFATrMAQT7P%2BliCCYCJhkzEOaPj4XMZW4MwIipRSUPDKA%2BAFsNZo2zAAIBAQFP5ZUw9Mm86%2BwSAq7QjLLJAQFpCPF3NgU5DOCbA05taAAJNmQEQosBLIjgrdrHDQK4tMfzwQFEDOBa6UEJRAQOKbEwNowBbvAELMrYqbHUEQKenKezqwFOBQE6%2FgAA83q5Ayis%2BPnwrg8C%2FNWDukpEAAkBcl4CDO6w87YWoAkMwAqpIgXNDMAojzMtSjbNAEY4BCzoj5zn4QcCzsfQsDQOoAkEqYupdQxgMQ9QDUPxlggIAgg5jRTw24bwgQ2V9AyAfKs3BXsM4ESgRU1eDBhABDIBVFI8CBDg0Ivh%2BInuBNEGyfMMgEWrNDqHAwgaAhoZcyz%2Bnu6Igw0CwP%2BuxGgpGAAAOW0JAQDwTWsIJAIkGTgEpr9lvwz%2BxJmYwZwQ4GuaSj8hpQxgbkBiDeM6JgFCNwIk9OjOiooKAsS284V6BQEBQx0BDXsB7m4zAhCancKLhEX4DAD5cCgFiAiALDjRn1H4CBgCGBnAENCqg4GpydIIsPIxCTMImsNBDbttkQF4Qt4BXAAAAQoeVG9yYUl0ZW1Db3ZpZXdzBDIxAE2scPA%2FLENhc3NpbmlQcm9kdWN0UHJvbW90ZWQEMjQGFSQANg0kKFRpdGxlU2VlZENhHSkENRoVKQBILikAIEhpZ2hJZGZPcj4yAAgzMWQVMgAuNpwABE9mEaQwMzIAAQAAAAAAAPA%2FAA%3D%3D&eactn=ALGO&algo=HOMESPLICE.plsim.SimplAMLv5PairwiseWeb&pn=2&rq=e81bc51a1710a9e4f1e6a615ffff7af8&pooltype=prod&pagename=PromotedListingsMLPipeline&ciid=SgD0CEA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De81bc51a1710a9e4f1e6a615ffff7af8%26node_id%3D1c9f41d70a17fb85"
            + "%26REQUEST_GUID%3D171e84a0-0960-a370-8400-ef36fff7d974%26logid%3Dt6kjkbpujn%2560"
            + "%25602"
            + "%253D9vjdkjkbpujn%2560%25602%2Aib%2560ks%2528rbpv6770-171e84a0096-0x2353&TPool"
            + "=r1homesplice4&TDuration=93&ContentLength=1031&ForwardedFor=10.156.175"
            + ".130&Script=api&Server=homesplice4.vip.ebay.com&TMachine=10.55.8.64&TStamp=21:41:36"
            + ".15&TName=postSim"));
    ubiEvent.setUrlQueryString(null);
    ubiEvent.setPageName("postSim");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e849ff051710a4d020811edeff6818ad");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2047675);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:41:35.742")));
    ubiEvent.setRemoteIP("162.14.22.99");
    ubiEvent.setRequestCorrelationId("e82ae452e93b7b80");
    ubiEvent.setSid(null);
    ubiEvent
        .setRlogid("t6ulcpjqcj9%3Ftilsobtlrbn%28075141-vrubqst-whh-%60dfz%2Behn-171e849ff11-0x170");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("162.14.22.99");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko)"
            + " Version/5.1 Safari/534.50");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(34393685833L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("QIAwIIEAACAAAAAAAQAAABAAgABBAAAAAABwAAABAAAEAEAAAAAAEA**");
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "visplt=100564%2C100939%2C100726%2C100566%2C100920%2C100921%2C100922%2C100923%2C&nqc"
            +
            "=AAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAADAAAAAACAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAABAAAAAABAAAQAAAAAAAg*&flgs=QIAwIIEAACAAAAAAAQAAABAAgABBAAAAAABwAAABAAAEAEAAAAAAEA**&ssc=2839973011&nozp=1&!ampid=3P_IMP&endeddiff=9D&bsnippets=true&sdes=1&!_OBFS_LINK_COUNT=0&obfs_sid_uid_same=false&uc=45&shsvrcnt=1&mbsa=500&nqt=AAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAADAAAAAACAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAABAAAAAABAAAQAAAAAAAg*&uaid=e849ff051710a4d020811edeff6818acS0&!SHIPPINGSERVICE=SSP%3A1%5EDSP%3A1&swcenabled=true&ul=zh-CN&pymntVersion=1&ec=1&noep=2&vibisdm=803X1071&iwsv=0&!IMPORT_CHARGE=USD%3A19.13%5E%5E%5E%5E&!vimc=1%5E32%5E100008,1%5E34%5E100623,1%5E242%5E100011%2C100012&itm=133371659323&promol=0&!_OBFS_SELLERID=391826031&pri=8026527303&mtpvi=0&iver=2629166321003&!_OBFS_BUYERID=0&bdrs=0&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fistime=32&es=0&vimr=EMPTY&fbscore=17938&!_callingpageid=2047675&swccount=1&itmtitle=Chefman+%E5%8E%8B%E9%93%B8%E7%94%B5%E5%8A%A8%E5%BE%97%E5%8A%9B%2F%E9%A3%9F%E5%93%81%E5%88%87%E7%89%87%E6%9C%BA&cflgs=QAE*&meta=11700&slr=391826031&pymntMethods=PPL%7CVSA%7CMSC%7CAMX%7CDSC&OBFS_STATUS=NOT_REQUIRED&curprice=69.95&attrct=13&virvwcnt=0&rq=e82ae452e93b7b80&bc=0&visbetyp=2&l1=20625&virvwavg=0.0&shipsiteid=0&l2=20667&obfs_listing_is_eligible=true&qtys=1&nw=1&vibis=300&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=0&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&bs=223&OBFS_ITEMID=133371659323&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=803X1071&ppfoid=0&qtya=1&pagename=ViewItemPageRaptor&!sh1srv=USD%3A55.989999999999995%5E-1%5E-1%5E2020-05-18T03%3A41%3A36-07%3A00%5E2020-05-28T03%3A41%3A36-07%3A00%5EEXPEDITED%5EInternationalPriorityShipping%5E1%5EUSD%3A19.13%5E1%5E-1%5ECN%5Enull%5E133371659323%5E-1%5E11%5E19&st=9&c=1&vibisbm=1000&g=e849ff051710a4d020811edeff6818ad&rpg=2047675&h=05&leaf=20681&fimbsa=500&!_OBFS_PHONE_COUNT=0&p=2047675&iimp=0&cp_usd=69.95&fdp=99&swcembg=true&t=0&srv=0&nofp=2&!_OBFS_EMAIL_COUNT=0&pn=2&qtrmn=0&dc=45&ciid=Sf8FAgg*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De82ae452e93b7b80%26node_id%3D2b3a22d3f8969716%26REQUEST_GUID"
            + "%3D171e849f-efe0-a4d0-2081-f28bff921565%26logid%3Dt6ulcpjqcj9%253Ftilsobtlrbn"
            + "%2528075141-vrubqst-whh-%2560dfz%252Behn-171e849ff11-0x170&TPool=r1viewitem"
            + "&TDuration"
            + "=669&TStatus=0&TType=URL&ContentLength=0&ForwardedFor=162.14.22.99, 23.33.94.197,23"
            + ".200.238.206&Script=/itm&Server=www.ebay.com&TMachine=10.77.2.8&TStamp=21:41:35"
            + ".74&TName=ViewItemPageRaptor&Agent=Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us)"
            + " AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50&RemoteIP=162.14.22"
            + ".99&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/itm/133371659323");
    ubiEvent.setPageName("ViewItemPageRaptor");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(133371659323L);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(12, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric15() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("batchguidf51ccbc84bf64506b2de3c2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2369663);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:04:48.006")));
    ubiEvent.setRemoteIP(null);
    ubiEvent.setRequestCorrelationId("c848d436e7f8ddc0");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%285262%3F4-vrubqst-whh-%60dfz%2Behn-171e85f3d87-0x1d6");
    ubiEvent.setEventFamily("MLSG");
    ubiEvent.setEventAction("EMIT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP(null);
    ubiEvent.setAgentInfo(null);
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(998802406751L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer(null);
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAACAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAABAAAAAAGA&flgs=AA**&mlCondition=1000&mlFpMinBinPrice=4.51&mlLeaf1Cat=33164&nqt=AAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAACAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIACAAQABAAAAAAGAAACA&mlUserId=origUserId%3DorigAcctId%3D0&mlEpid=18035256422&mlGuid=guid%3Dbatchguidf51ccbc84bf64506b2de3c2%2Cpageid%3D0&mlFixedPrice=6.95&ec=1&pagename=invmlsvc__DefaultPage&efam=MLSG&mlAppId=56507&g=batchguidf51ccbc84bf64506b2de3c2&h=null&mlItmTitle=CND+Shellac+Luxe+Decadence+12.5ml&mlInvocationId=batchguidf51ccbc84bf64506b2de3c2&es=0&p=2369663&dsktop=true&mlFpConf=0.5695012914728708&t=0&mlFpMaxBinPrice=8.14&cflgs=AA**&!mlReqGuidance=FIXED_PRICE&eactn=EMIT&rq=c848d436e7f8ddc0&ciid=Xz1Djeg*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De2395f499d68bd2f%26node_id%3D653d16cb1f20e227%26REQUEST_GUID"
            + "%3D171e85f3-d840-a488-de82-5b26e4bfa586%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%25285262%253F4-vrubqst-whh-%2560dfz%252Behn-171e85f3d87-0x1d6%26cal_mod%3Dfalse"
            + "&TPool"
            + "=r1invmlsvc46&TDuration=1&TStatus=0&TType=URL&ContentLength=1301&Script=/trk20svc"
            + "/TrackingResource/v1&TMachine=10.72.141.232&TStamp=22:04:48.00&TName=Ginger"
            + ".CollectionSvc.track"));
    ubiEvent.setUrlQueryString(
        "/trk20svc/TrackingResource/v1/track");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(12, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric16() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("342b9b1a16f0a4d34ed02cdbff9e987d");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605052);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:59:06.472")));
    ubiEvent.setRemoteIP("91.11.93.188");
    ubiEvent.setRequestCorrelationId("37c09fca40ec2063");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0102005%29pqtfwpu%29pie%29fgg%7E-fij-171e82318f2-0x10f");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("91.11.93.188");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Linux; Android 7.1.1; SM-T555 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML,"
            + " like Gecko) Version/4.0 Chrome/81.0.4044.117 Safari/537.36 [FB_IAB/FB4A;FBAV/268"
            + ".1.0"
            + ".54.121;]");
    ubiEvent.setCobrand(9);
    ubiEvent.setCurrentImprId(170539423779L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=342b9b1a16f0a4d34ed02cdbff9e987d&rurl=http%25253A%25252F%25252Fm.facebook"
            + ".com%25252F&h=1a&px=4249&js=1&chnl=9&n=342bb2d516f0aad33664a23fff8bec91&uc=77&p"
            + "=1605052&uaid=e82318eb1710a99b427789f9c3281140S0&bs=77&catid=223&ref=https%3A%2F"
            + "%2Fwww"
            + ".ebay-kleinanzeigen.de%2Fs-autoteile-reifen%2Fmannheim%2Fc223l7971%3Forigin"
            + "%3DDELETED_AD%26utm_source%3Dsharesheet%26utm_medium%3Dsocial%26utm_campaign"
            + "%3Dsocialbuttons%26utm_content%3Dapp_android&r=1831914225&t=77®=Mannheim%2520"
            + "-%2520Baden-W%25C3%25BCrttemberg&cflgs=EA**&ul=de-DE&pn=2&rq=37c09fca40ec2063"
            + "&pagename"
            + "=http://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=IxjxtCc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D37c09fca40ec2063%26node_id%3Dcdbe51c977cd747a%26REQUEST_GUID"
            + "%3D171e8231-8e80-a99b-4276-8bace04a21b9%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0102005%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e82318f2-0x10f%26cal_mod%3Dfalse"
            + "&TPool=r1rover&TDuration=12&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=91"
            + ".11.93"
            + ".188, 2.21.133.197&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.153.180"
            + ".39&TStamp=20:59:06.47&TName=roverimp_INTL&Agent=Mozilla/5.0 (Linux; Android 7.1.1;"
            + " SM-T555 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4"
            + ".0 Chrome/81.0.4044.117 Safari/537.36 [FB_IAB/FB4A;FBAV/268.1.0.54.121;]&RemoteIP=91"
            + ".11.93.188&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen"
            + ".de/s-autoteile-reifen/mannheim/c223l7971?origin=DELETED_AD&utm_source=sharesheet"
            + "&utm_medium=social&utm_campaign=socialbuttons&utm_content=app_android"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=reg%3DMannheim%2520-%2520Baden-W%25C3%25BCrttemberg%26catid%3D223"
            + "%26rurl%3Dhttp%25253A%25252F%25252Fm.facebook"
            + ".com%25252F&mpt=1588737545925&imp=1605052");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(1831914225L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(
        "https://www.ebay-kleinanzeigen.de/s-autoteile-reifen/mannheim/c223l7971?origin"
            + "=DELETED_AD&utm_source=sharesheet&utm_medium=social&utm_campaign=socialbuttons"
            + "&utm_content=app_android");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("342b9b1a16f0a4d34ed02cdbff9e987d");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(1605052);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:00:25.907")));
    ubiEvent.setRemoteIP("91.11.93.188");
    ubiEvent.setRequestCorrelationId("237b84fd8e8d9a85");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0106617%29pqtfwpu%29pie%29fgg%7E-fij-171e8244f3f-0x1a1");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("91.11.93.188");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Linux; Android 7.1.1; SM-T555 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML,"
            + " like Gecko) Version/4.0 Chrome/81.0.4044.117 Safari/537.36 [FB_IAB/FB4A;FBAV/268"
            + ".1.0"
            + ".54.121;]");
    ubiEvent.setCobrand(9);
    ubiEvent.setCurrentImprId(117645987620L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=342b9b1a16f0a4d34ed02cdbff9e987d&rurl=http%25253A%25252F%25252Fm.facebook"
            + ".com%25252F&h=1a&px=4249&js=1&chnl=9&n=342bb2d516f0aad33664a23fff8bec91&uc=77&p"
            + "=1605052&uaid=e8244f371710abc641b12b4ec2c14f53S0&bs=77&catid=223&ref=https%3A%2F"
            + "%2Fwww"
            + ".ebay-kleinanzeigen.de%2Fs-autoteile-reifen%2Fmannheim%2Fc223l7971%3Forigin"
            + "%3DDELETED_AD%26utm_source%3Dsharesheet%26utm_medium%3Dsocial%26utm_campaign"
            + "%3Dsocialbuttons%26utm_content%3Dapp_android&r=1831914225&t=77®=Mannheim%2520"
            + "-%2520Baden-W%25C3%25BCrttemberg&cflgs=EA**&ul=de-DE&pn=2&rq=237b84fd8e8d9a85"
            + "&pagename"
            + "=http://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=JE8%2FZBs*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D237b84fd8e8d9a85%26node_id%3Da81ef4ccf8e5a898%26REQUEST_GUID"
            + "%3D171e8244-f330-abc6-41b6-b49fe013420b%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t"
            + "%2A0106617%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e8244f3f-0x1a1%26cal_mod%3Dfalse"
            + "&TPool=r1rover&TDuration=14&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=91"
            + ".11.93"
            + ".188, 2.21.133.197&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.188.100"
            + ".27&TStamp=21:00:25.90&TName=roverimp_INTL&Agent=Mozilla/5.0 (Linux; Android 7.1.1;"
            + " SM-T555 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4"
            + ".0 Chrome/81.0.4044.117 Safari/537.36 [FB_IAB/FB4A;FBAV/268.1.0.54.121;]&RemoteIP=91"
            + ".11.93.188&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen"
            + ".de/s-autoteile-reifen/mannheim/c223l7971?origin=DELETED_AD&utm_source=sharesheet"
            + "&utm_medium=social&utm_campaign=socialbuttons&utm_content=app_android"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=reg%3DMannheim%2520-%2520Baden-W%25C3%25BCrttemberg%26catid%3D223"
            + "%26rurl%3Dhttp%25253A%25252F%25252Fm.facebook"
            + ".com%25252F&mpt=1588737625397&imp=1605052");
    ubiEvent.setPageName("roverimp_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(77);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(1831914225L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(
        "https://www.ebay-kleinanzeigen.de/s-autoteile-reifen/mannheim/c223l7971?origin"
            + "=DELETED_AD&utm_source=sharesheet&utm_medium=social&utm_campaign=socialbuttons"
            + "&utm_content=app_android");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(22, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric17() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("c6621823167bfe71e16f8e5001055762");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2051248);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:06:45.851")));
    ubiEvent.setRemoteIP("10.211.141.235");
    ubiEvent.setRequestCorrelationId("4b203ea75ca99fab");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E46%28twwgsvv%28umj%28bad%7F%29%60jk-171e7f3b94f-0x157696a");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("10.211.141.235");
    ubiEvent.setAgentInfo("eBayAndroid/5.15.0.20");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(288405567987L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "tz=5.5&lv=1&dn=ANE-LX1&ist=0&tzname=Asia%2FKolkata&uc=1&mos=Android&bs=0&uaid"
            + "=e7f3b94f1710a4d26432b619985cb433S0&memsz=2048"
            + ".0&osv=9&ul=en-US&mtsts=2020-05-05T20%3A06%3A45"
            + ".851&pagename=Foreground&app=2571&res=1080x2060&c=1&mav=5.15"
            + ".0&g=c6621823167bfe71e16f8e5001055762&h=23&reqts=1588734441798&cguidsrc=cookie&n"
            + "=ddbdb8c51700a9cd62471b77f67bb112&ort=p&p=2051248&ttp=Page&mnt=WIFI&carrier=wifi&t"
            + "=203"
            + "&prefl=en_GB&cflgs=EA**&ids=MP&mrollp=29&gadid=87b0c09c-6275-47ff-91fd-2829510145e4"
            + "%2C1&mppid=0&androidid=49aa5023a9867212&pcguid=ddbdb8c51700a9cd62471b77f67bb112&pn=2"
            + "&rq=4b203ea75ca99fab&ciid=87lPJkM*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4b203ea75ca99fab%26node_id%3D67c30a68ac34d624%26REQUEST_GUID"
            + "%3D171e7f3b-94e0-a4d2-6433-dde5c76086e8%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E46%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7f3b94f-0x157696a%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=4&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=10.211.16.16&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.38"
            + ".67&TStamp=20:07:21.80&TName=roverimp&Agent=eBayAndroid/5.15.0.20&RemoteIP=10.211"
            + ".141"
            + ".235"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&imp=2051248&lv=udid%3Dc6621823167bfe71e16f8e5001055762%26ai%3D2571"
            + "%26mav%3D5.15.0%26site%3D203%26memsz%3D2048"
            + ".0%26res%3D1080x2060%26mrollp%3D29%26c%3D1%26ids%3DMP%26osv%3D9%26mnt%3DWIFI%26prefl"
            + "%3Den_GB%26ist%3D0%26tzname%3DAsia%2FKolkata%26androidid%3D49aa5023a9867212%26reqts"
            + "%3D1588734441798%26tz%3D5.5%26mos%3DAndroid%26ort%3Dp%26carrier%3Dwifi%26gadid"
            + "%3D87b0c09c-6275-47ff-91fd-2829510145e4%2C1%26dn%3DANE-LX1%26dpi%3D435.428x432"
            + ".179%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T20%3A06%3A45.851");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(203);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("c6621823167bfe71e16f8e5001055762");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(3084);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:10:53.903")));
    ubiEvent.setRemoteIP("10.195.199.95");
    ubiEvent.setRequestCorrelationId("24a385c4c9cdc305");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*hf%7Cig%28rbpv6710-171e82de450-0x120");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("10.195.199.95");
    ubiEvent.setAgentInfo("eBayAndroid/5.15.0.20");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(55168787501L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "app=2571&c=1&g=c6621823167bfe71e16f8e5001055762&nid=&h=23&cguidsrc=cookie&n"
            + "=ddbdb8c51700a9cd62471b77f67bb112&uc=1&p=3084&uaid"
            + "=e82de4501710a77d80c3e132fac5c1ddS0"
            + "&bs=0&rvrid=2410752785839&t=203&cflgs=EA**&ul=en-US&mppid=117&pn=2&pcguid"
            + "=ddbdb8c51700a9cd62471b77f67bb112&rq=24a385c4c9cdc305&pagename=EntryTracking&ciid"
            + "=LeRQ2Aw*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D24a385c4c9cdc305%26node_id%3D69def36862fb9b72%26REQUEST_GUID"
            + "%3D171e82de-44f0-a77d-80c6-af50fd429273%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2Ahf"
            + "%257Cig%2528rbpv6710-171e82de450-0x120%26cal_mod%3Dfalse&TPool=r1rover&TDuration=26"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=10.156.200"
            + ".85&Script=/rover/1/0/4&Server=localhost&TMachine=10.119.216.12&TStamp=21:10:53"
            + ".90&TName=rover&Agent=eBayAndroid/5.15.0.20&RemoteIP=10.195.199.95"));
    ubiEvent.setUrlQueryString(
        "/rover/1/0/4?res=1080x2060&memsz=2048"
            + ".0&mnt=WIFI&prefl=en_GB&tzname=Asia%2FKolkata&androidid=49aa5023a9867212&ort=p"
            + "&carrier"
            + "=wifi&dn=ANE-LX1&dpi=435.428x432.179&mtsts=2020-05-05T21%3A10%3A49"
            + ".675&ctr=0&nrd=1&site=203&mrollp=29&mav=5.15"
            + ".0&c=2&osv=9&ids=MP&ist=0&udid=c6621823167bfe71e16f8e5001055762&rvrhostname=rover"
            + ".ebay"
            + ".com&ai=2571&rvrsite=0&reqts=1588738253895&tz=5"
            + ".5&rlutype=1&mos=Android&gadid=87b0c09c-6275-47ff-91fd-2829510145e4%2C1&mppid=117");
    ubiEvent.setPageName("rover");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(203);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("c6621823167bfe71e16f8e5001055762");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2371265);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:06:47.085")));
    ubiEvent.setRemoteIP("10.211.141.235");
    ubiEvent.setRequestCorrelationId("4b203ea75ca99fab");
    ubiEvent.setSid("p2047939");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E46%28twwgsvv%28umj%28bad%7F%29%60jk-171e7f3b95a-0x997c30f");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("10.211.141.235");
    ubiEvent.setAgentInfo("eBayAndroid/5.15.0.20");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(288406288883L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "tz=5.5&lv=1&dn=ANE-LX1&ist=0&tzname=Asia%2FKolkata&uc=1&mos=Android&bs=0&uaid"
            + "=e7f3b95a1710a4d26432b619985cb423S0&memsz=2048"
            + ".0&osv=9&ul=en-US&mtsts=2020-05-05T20%3A06%3A47"
            + ".085&pagename=HomeCards&app=2571&res=1080x2060&c=3&mav=5.15"
            + ".0&g=c6621823167bfe71e16f8e5001055762&h=23&reqts=1588734441799&cguidsrc=cookie&n"
            + "=ddbdb8c51700a9cd62471b77f67bb112&ort=p&p=2371265&ttp=Page&mnt=WIFI&carrier=wifi&t"
            + "=203"
            + "&prefl=en_GB&cflgs=EA**&ids=MP&mrollp=29&gadid=87b0c09c-6275-47ff-91fd-2829510145e4"
            + "%2C1&mppid=0&androidid=49aa5023a9867212&pcguid=ddbdb8c51700a9cd62471b77f67bb112&pn=2"
            + "&rq=4b203ea75ca99fab&ciid=87laJkM*&sid=p2047939");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4b203ea75ca99fab%26node_id%3D9e19ce8f3996d9cd%26REQUEST_GUID"
            + "%3D171e7f3b-9590-a4d2-6433-dde5c76086e0%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E46%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7f3b95a-0x997c30f%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=10.211.16.16&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.38"
            + ".67&TStamp=20:07:21.81&TName=roverimp&Agent=eBayAndroid/5.15.0.20&RemoteIP=10.211"
            + ".141"
            + ".235"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&imp=2371265&lv=udid%3Dc6621823167bfe71e16f8e5001055762%26ai%3D2571"
            + "%26mav%3D5.15.0%26site%3D203%26memsz%3D2048"
            + ".0%26res%3D1080x2060%26mrollp%3D29%26c%3D3%26ids%3DMP%26osv%3D9%26mnt%3DWIFI%26prefl"
            + "%3Den_GB%26ist%3D0%26tzname%3DAsia%2FKolkata%26androidid%3D49aa5023a9867212%26reqts"
            + "%3D1588734441799%26tz%3D5.5%26mos%3DAndroid%26ort%3Dp%26carrier%3Dwifi%26gadid"
            + "%3D87b0c09c-6275-47ff-91fd-2829510145e4%2C1%26dn%3DANE-LX1%26dpi%3D435.428x432"
            + ".179%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T20%3A06%3A47"
            + ".085&_trksid=p2047939");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(203);
    ubiEvent.setClickId(3);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("c6621823167bfe71e16f8e5001055762");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2051248);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:10:51.909")));
    ubiEvent.setRemoteIP("10.156.85.171");
    ubiEvent.setRequestCorrelationId("5e7c1ac387b43996");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0%3D00463-171e82de7cd-0x1a8");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("10.156.85.171");
    ubiEvent.setAgentInfo("eBayAndroid/5.15.0.20");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(417430628141L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "tz=5.5&lv=1&dn=ANE-LX1&ist=0&tzname=Asia%2FKolkata&uc=1&mlocerr=1&mos=Android&bs=0&uaid"
            + "=e82de7cd1710aaa30613aabec1647ce3S0&mlocset=0&memsz=2048"
            + ".0&osv=9&ul=en-US&mtsts=2020-05-05T21%3A10%3A51"
            + ".909&apn=1&pagename=Foreground&app=2571&res=1080x2060&c=1&mav=5.15"
            + ".0&mtsgitcommit=d6b703b28b7c71bdb516eedb3d62a9c832dfa163&g"
            + "=c6621823167bfe71e16f8e5001055762&h=23&reqts=1588738254787&cguidsrc=cookie&n"
            + "=ddbdb8c51700a9cd62471b77f67bb112&ort=p&p=2051248&ttp=Page&mnt=WIFI&carrier=wifi&t"
            + "=203"
            + "&prefl=en_GB&cflgs=EA**&ids=MP&mrollp=29&gadid=87b0c09c-6275-47ff-91fd-2829510145e4"
            + "%2C1&mppid=0&androidid=49aa5023a9867212&pcguid=ddbdb8c51700a9cd62471b77f67bb112&pn=2"
            + "&rq=5e7c1ac387b43996&ciid=LefNMGE*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D5e7c1ac387b43996%26node_id%3D7e56b1ed05a20cc4%26REQUEST_GUID"
            + "%3D171e82de-7cb0-aaa3-0612-e71bdea2d36a%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0"
            + "%253D00463-171e82de7cd-0x1a8%26cal_mod%3Dfalse&TPool=r1rover&TDuration=4&TStatus=0"
            + "&TType=URL&ContentLength=-1&ForwardedFor=10.156.197"
            + ".55&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.170.48.97&TStamp=21:10:54"
            + ".79&TName=roverimp&Agent=eBayAndroid/5.15.0.20&RemoteIP=10.156.85.171"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&imp=2051248&lv=udid%3Dc6621823167bfe71e16f8e5001055762%26ai%3D2571"
            + "%26mav%3D5.15.0%26site%3D203%26memsz%3D2048"
            + ".0%26res%3D1080x2060%26mrollp%3D29%26c%3D1%26ids%3DMP%26osv%3D9%26mnt%3DWIFI"
            + "%26mtsgitcommit%3Dd6b703b28b7c71bdb516eedb3d62a9c832dfa163%26prefl%3Den_GB%26ist%3D0"
            + "%26tzname%3DAsia%2FKolkata%26mlocerr%3D1%26androidid%3D49aa5023a9867212%26reqts"
            + "%3D1588738254787%26tz%3D5.5%26mos%3DAndroid%26mlocset%3D0%26ort%3Dp%26carrier%3Dwifi"
            + "%26gadid%3D87b0c09c-6275-47ff-91fd-2829510145e4%2C1%26dn%3DANE-LX1%26dpi%3D435"
            + ".428x432"
            + ".179%26apn%3D1%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T21%3A10%3A51.909");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(203);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("c6621823167bfe71e16f8e5001055762");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2047939);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:06:47.124")));
    ubiEvent.setRemoteIP("10.211.141.235");
    ubiEvent.setRequestCorrelationId("4b203ea75ca99fab");
    ubiEvent.setSid("p2051248");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E46%28twwgsvv%28umj%28bad%7F%29%60jk-171e7f3b956-0x116");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("10.211.141.235");
    ubiEvent.setAgentInfo("eBayAndroid/5.15.0.20");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(288406026739L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "tz=5.5&lv=1&dn=ANE-LX1&ist=0&tzname=Asia%2FKolkata&uc=1&mos=Android&bs=0&uaid"
            + "=e7f3b9561710a4d26432b619985cb42bS0&memsz=2048"
            + ".0&osv=9&ul=en-US&mtsts=2020-05-05T20%3A06%3A47"
            + ".124&pagename=HomePage&app=2571&res=1080x2060&c=2&mav=5.15"
            + ".0&g=c6621823167bfe71e16f8e5001055762&h=23&reqts=1588734441798&cguidsrc=cookie&n"
            + "=ddbdb8c51700a9cd62471b77f67bb112&ort=p&p=2047939&ttp=Page&mnt=WIFI&carrier=wifi&t"
            + "=203"
            + "&prefl=en_GB&cflgs=EA**&ids=MP&mrollp=29&gadid=87b0c09c-6275-47ff-91fd-2829510145e4"
            + "%2C1&mppid=0&androidid=49aa5023a9867212&pcguid=ddbdb8c51700a9cd62471b77f67bb112&pn=2"
            + "&rq=4b203ea75ca99fab&ciid=87lWJkM*&sid=p2051248");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4b203ea75ca99fab%26node_id%3De78793aecf60e488%26REQUEST_GUID"
            + "%3D171e7f3b-9550-a4d2-6433-dde5c76086e5%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E46%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7f3b956-0x116%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=10.211.16.16&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.38"
            + ".67&TStamp=20:07:21.81&TName=roverimp&Agent=eBayAndroid/5.15.0.20&RemoteIP=10.211"
            + ".141"
            + ".235"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&imp=2047939&lv=udid%3Dc6621823167bfe71e16f8e5001055762%26ai%3D2571"
            + "%26mav%3D5.15.0%26site%3D203%26memsz%3D2048"
            + ".0%26res%3D1080x2060%26mrollp%3D29%26c%3D2%26ids%3DMP%26osv%3D9%26mnt%3DWIFI%26prefl"
            + "%3Den_GB%26ist%3D0%26tzname%3DAsia%2FKolkata%26androidid%3D49aa5023a9867212%26reqts"
            + "%3D1588734441798%26tz%3D5.5%26mos%3DAndroid%26ort%3Dp%26carrier%3Dwifi%26gadid"
            + "%3D87b0c09c-6275-47ff-91fd-2829510145e4%2C1%26dn%3DANE-LX1%26dpi%3D435.428x432"
            + ".179%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T20%3A06%3A47"
            + ".124&_trksid=p2051248");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(203);
    ubiEvent.setClickId(2);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(1, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric18() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("9d531d97170aaad5f2b032300104c1bd");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367320);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:40:14.144")));
    ubiEvent.setRemoteIP("10.153.189.142");
    ubiEvent.setRequestCorrelationId("0915531b9779ee47");
    ubiEvent.setSid("p2051248");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3F%3B2%28twwgsvv%28umj%28bad%7F%29%60jk-171e7a4899c"
            + "-0x19e2155");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("68.53.26.66");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(765202303396L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&tz=-5.0&nid=587821589072&lv=1&dn=SM-G975U&ist=0&uc=1&mos=Android&nqt=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&uaid=e7a4899c1710ad329b276d299e431dc0S0&osv=10&ul=en-US&mtsts=2020-05-05T18%3A40%3A14.144&ec=4&navp=user.saved_searches&app=2571&res=1080x2029&cguidsrc=cookie&es=0&cflgs=EA**&ids=MP%253Dmarines1624&designsystem=6&mppid=0&mrollp=62&gadid=b1f485c6-ea40-482b-8548-aa94728ab48d%2C0&naid=GO_TO_SAVED_SEARCHES&androidid=242e1919bddc9378&pcguid=9d5323d01700ad4d14769f4ce029570f&rq=0915531b9779ee47&user_name=marines1624&tzname=America%2FChicago&bs=0&memsz=4096.0&!xt=59341&pagename=DeepLinkAction&uit=1583189780000&c=4&mav=6.0.1&g=9d531d97170aaad5f2b032300104c1bd&deeplink=ebay%253A%252F%252Flink%252F%253Fnav%253Duser.saved_searches%2526isnotif%253D1&h=97&reqts=1588729252243&ntype=SVDSRCH_SINGLE&n=9d5323d01700ad4d14769f4ce029570f&ort=p&p=2367320&ttp=Page&carrier=Xfinity+Mobile+Wi-Fi+Calling&mnt=WIFI&t=0&u=1012482381&prefl=en_US&pn=2&ciid=pImcKbI*&sid=p2051248");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0915531b9779ee47%26node_id%3Dd4ae47a8eada9b0f%26REQUEST_GUID"
            + "%3D171e7a48-99b0-ad32-9b26-ea1aca942efd%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253F%253B2%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7a4899c-0x19e2155"
            + "%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1"
            + "&ForwardedFor=68.53.26.66&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.211"
            + ".41"
            + ".178&TStamp=18:40:52.25&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.153"
            + ".189"
            + ".142"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26nqt%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26ec%3D4%26uit%3D1583189780000%3C%2Fa%3E&imp=2367320&lv=udid%3D9d531d97170aaad5f2b032300104c1bd%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AEkYClAJKBqQ2dj6x9nY%252BseQ**%26res%3D1080x2029%26memsz%3D4096.0%26designsystem%3D6%26mnt%3DWIFI%26prefl%3Den_US%26tzname%3DAmerica%2FChicago%26androidid%3D242e1919bddc9378%26ntype%3DSVDSRCH_SINGLE%26ort%3Dp%26carrier%3DXfinity+Mobile+Wi-Fi+Calling%26naid%3DGO_TO_SAVED_SEARCHES%26dpi%3D391.8855x391.29675%26dn%3DSM-G975U%26navp%3Duser.saved_searches%26mrollp%3D62%26c%3D4%26ids%3DMP%253Dmarines1624%26osv%3D10%26nid%3D587821589072%26dlsource%3Dntray%26ist%3D0%26%21xt%3D59341%26user_name%3Dmarines1624%26reqts%3D1588729252243%26tz%3D-5.0%26mos%3DAndroid%26deeplink%3Debay%253A%252F%252Flink%252F%253Fnav%253Duser.saved_searches%2526isnotif%253D1%26gadid%3Db1f485c6-ea40-482b-8548-aa94728ab48d%2C0%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T18%3A40%3A14.144&_trksid=p2051248");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(4);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1012482381");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("9d531d97170aaad5f2b032300104c1bd");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367320);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:40:14.144")));
    ubiEvent.setRemoteIP("10.153.189.142");
    ubiEvent.setRequestCorrelationId("0915531b9779ee47");
    ubiEvent.setSid("p2051248");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3F%3B2%28twwgsvv%28umj%28bad%7F%29%60jk-171e7a4899c"
            + "-0x19e2155");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("68.53.26.66");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(765202303396L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&tz=-5.0&nid=587821589072&lv=1&dn=SM-G975U&ist=0&uc=1&mos=Android&nqt=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&uaid=e7a4899c1710ad329b276d299e431dc0S0&osv=10&ul=en-US&mtsts=2020-05-05T18%3A40%3A14.144&ec=4&navp=user.saved_searches&app=2571&res=1080x2029&cguidsrc=cookie&es=0&cflgs=EA**&ids=MP%253Dmarines1624&designsystem=6&mppid=0&mrollp=62&gadid=b1f485c6-ea40-482b-8548-aa94728ab48d%2C0&naid=GO_TO_SAVED_SEARCHES&androidid=242e1919bddc9378&pcguid=9d5323d01700ad4d14769f4ce029570f&rq=0915531b9779ee47&user_name=marines1624&tzname=America%2FChicago&bs=0&memsz=4096.0&!xt=59341&pagename=DeepLinkAction&uit=1583189780000&c=4&mav=6.0.1&g=9d531d97170aaad5f2b032300104c1bd&deeplink=ebay%253A%252F%252Flink%252F%253Fnav%253Duser.saved_searches%2526isnotif%253D1&h=97&reqts=1588729252243&ntype=SVDSRCH_SINGLE&n=9d5323d01700ad4d14769f4ce029570f&ort=p&p=2367320&ttp=Page&carrier=Xfinity+Mobile+Wi-Fi+Calling&mnt=WIFI&t=0&u=1012482381&prefl=en_US&pn=2&ciid=pImcKbI*&sid=p2051248");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0915531b9779ee47%26node_id%3Dd4ae47a8eada9b0f%26REQUEST_GUID"
            + "%3D171e7a48-99b0-ad32-9b26-ea1aca942efd%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253F%253B2%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7a4899c-0x19e2155"
            + "%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1"
            + "&ForwardedFor=68.53.26.66&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.211"
            + ".41"
            + ".178&TStamp=18:40:52.25&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.153"
            + ".189"
            + ".142"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26nqt%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26ec%3D4%26uit%3D1583189780000%3C%2Fa%3E&imp=2367320&lv=udid%3D9d531d97170aaad5f2b032300104c1bd%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AEkYClAJKBqQ2dj6x9nY%252BseQ**%26res%3D1080x2029%26memsz%3D4096.0%26designsystem%3D6%26mnt%3DWIFI%26prefl%3Den_US%26tzname%3DAmerica%2FChicago%26androidid%3D242e1919bddc9378%26ntype%3DSVDSRCH_SINGLE%26ort%3Dp%26carrier%3DXfinity+Mobile+Wi-Fi+Calling%26naid%3DGO_TO_SAVED_SEARCHES%26dpi%3D391.8855x391.29675%26dn%3DSM-G975U%26navp%3Duser.saved_searches%26mrollp%3D62%26c%3D4%26ids%3DMP%253Dmarines1624%26osv%3D10%26nid%3D587821589072%26dlsource%3Dntray%26ist%3D0%26%21xt%3D59341%26user_name%3Dmarines1624%26reqts%3D1588729252243%26tz%3D-5.0%26mos%3DAndroid%26deeplink%3Debay%253A%252F%252Flink%252F%253Fnav%253Duser.saved_searches%2526isnotif%253D1%26gadid%3Db1f485c6-ea40-482b-8548-aa94728ab48d%2C0%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T18%3A40%3A14.144&_trksid=p2051248");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(4);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1012482381");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("9d531d97170aaad5f2b032300104c1bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2050466);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:40:14.262")));
    ubiEvent.setRemoteIP("10.153.189.142");
    ubiEvent.setRequestCorrelationId("0915531b9779ee47");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3F%3B2%28twwgsvv%28umj%28bad%7F%29%60jk-171e7a489a0"
            + "-0x6fc33a4");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("68.53.26.66");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(765202500004L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&user_name=marines1624&tz=-5.0&lv=1&dn=SM-G975U&ist=0&tzname=America%2FChicago&uc=1&nqt=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&mos=Android&bs=0&uaid=e7a4899f1710ad329b276d299e431db8S0&memsz=4096.0&osv=10&ul=en-US&mtsts=2020-05-05T18%3A40%3A14.262&!xt=59341&ec=4&pagename=SearchResultsFromSSNotification&app=2571&res=1080x2029&uit=1583189780000&c=5&mav=6.0.1&g=9d531d97170aaad5f2b032300104c1bd&h=97&reqts=1588729252243&cguidsrc=cookie&n=9d5323d01700ad4d14769f4ce029570f&es=0&ort=p&p=2050466&ttp=Page&mnt=WIFI&carrier=Xfinity+Mobile+Wi-Fi+Calling&t=0&u=1012482381&prefl=en_US&cflgs=EA**&ids=MP%253Dmarines1624&designsystem=6&mrollp=62&gadid=b1f485c6-ea40-482b-8548-aa94728ab48d%2C0&mppid=0&androidid=242e1919bddc9378&pcguid=9d5323d01700ad4d14769f4ce029570f&pn=2&rq=0915531b9779ee47&ciid=pImfKbI*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0915531b9779ee47%26node_id%3Dcebd4e49be3d1fa8%26REQUEST_GUID"
            + "%3D171e7a48-99f0-ad32-9b26-ea1aca942ef9%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253F%253B2%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7a489a0-0x6fc33a4"
            + "%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1"
            + "&ForwardedFor=68.53.26.66&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.211"
            + ".41"
            + ".178&TStamp=18:40:52.25&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.153"
            + ".189"
            + ".142"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26nqt%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26ec%3D4%26uit%3D1583189780000%3C%2Fa%3E&imp=2050466&lv=udid%3D9d531d97170aaad5f2b032300104c1bd%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AEkYClAJKBqQ2dj6x9nY%252BseQ**%26memsz%3D4096.0%26res%3D1080x2029%26mrollp%3D62%26designsystem%3D6%26c%3D5%26osv%3D10%26ids%3DMP%253Dmarines1624%26mnt%3DWIFI%26ist%3D0%26prefl%3Den_US%26%21xt%3D59341%26tzname%3DAmerica%2FChicago%26user_name%3Dmarines1624%26androidid%3D242e1919bddc9378%26reqts%3D1588729252243%26tz%3D-5.0%26mos%3DAndroid%26ort%3Dp%26carrier%3DXfinity+Mobile+Wi-Fi+Calling%26gadid%3Db1f485c6-ea40-482b-8548-aa94728ab48d%2C0%26dn%3DSM-G975U%26dpi%3D391.8855x391.29675%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T18%3A40%3A14.262");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(5);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1012482381");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("9d531d97170aaad5f2b032300104c1bd");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2356359);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:40:26.013")));
    ubiEvent.setRemoteIP("68.53.26.66");
    ubiEvent.setRequestCorrelationId("74bccef829254c16");
    ubiEvent.setSid("p2506613.m5234.l47241");
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%2853%3A323%3A%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh"
            + "-171e7a48b5e-0x126");
    ubiEvent.setEventFamily("MYBPH");
    ubiEvent.setEventAction("ACTN");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("68.53.26.66");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;beyond2q;"
            + "Xfinity Mobile Wi-Fi Calling;1080x2029;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(378332154788L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&tz=-5.0&dm=samsung&dn=beyond2q&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26nqt%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26ec%3D4%26uit%3D1583189780000%3C%2Fa%3E&sid=p2506613.m5234.l47241&mos=Android&osv=10&callingpagename=txexpsvc__experience_myebay_buying_v1_purchase_activity_GET&mtsts=2020-05-06T01%3A40%3A26.013Z&!xt=59341&pagename=PulsarGateway&app=2571&res=0x0&efam=MYBPH&mav=6.0.1&c=8&ou=1012482381&g=9d531d97170aaad5f2b032300104c1bd&h=97&nativeApp=true&cp=2506613&an=eBayAndroid&n=9d5323d01700ad4d14769f4ce029570f&ort=p&p=2356359&t=0&u=1012482381&cflgs=AA**&actionKind=NAVSRC&ids=MP%3Dmarines1624&designsystem=6&mrollp=62&gadid=b1f485c6-ea40-482b-8548-aa94728ab48d%2C0&eactn=ACTN&androidid=242e1919bddc9378&rq=74bccef829254c16&ciid=pItaFlg*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D74bccef829254c16%26node_id%3D63df2a622ec3d6a2%26REQUEST_GUID"
            + "%3D171e7a48b4f.a9cd6e4.75382.b9e6754b%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31"
            + "%252853%253A323%253A%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e7a48b5e-0x126"
            + "%26cal_mod%3Dfalse&TPool=r1pulsgwy&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=1374"
            + "&ForwardedFor=68.53.26.66; 23.58.92"
            + ".61&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.188.22"
            + ".88&TStamp=18:40:52.70&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;beyond2q;"
            + "Xfinity Mobile Wi-Fi Calling;1080x2029;3.0&RemoteIP=68.53.26"
            + ".66&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/base/tracking/v1/track_events");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(8);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1012482381");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("9d531d97170aaad5f2b032300104c1bd");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2051249);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:40:52.260")));
    ubiEvent.setRemoteIP("10.153.11.133");
    ubiEvent.setRequestCorrelationId("32e511b19d5ecd29");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E46%28twwgsvv%28umj%28bad%7F%29%60jk-171e7a48a6f-0x6e05f5b");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("68.53.26.66");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(288407653028L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&tz=-5.0&lv=1&dn=SM-G975U&ist=0&uc=1&mos=Android&nqt=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&uaid=e7a48a6f1710a4d26432b6199873ba37S0&osv=10&ul=en-US&mtsts=2020-05-05T18%3A40%3A52.260&ec=4&apn=1&app=2571&res=1080x2029&cguidsrc=cookie&es=0&cflgs=EA**&ids=MP%253Dmarines1624&designsystem=6&mppid=0&mrollp=62&gadid=b1f485c6-ea40-482b-8548-aa94728ab48d%2C0&androidid=242e1919bddc9378&pcguid=9d5323d01700ad4d14769f4ce029570f&rq=32e511b19d5ecd29&user_name=marines1624&tzname=America%2FChicago&mlocerr=1&bs=0&mlocset=3&memsz=4096.0&!xt=59341&pagename=Background&uit=1583189780000&c=9&mav=6.0.1&g=9d531d97170aaad5f2b032300104c1bd&h=97&reqts=1588729252456&n=9d5323d01700ad4d14769f4ce029570f&ort=p&p=2051249&ttp=Page&carrier=Xfinity+Mobile+Wi-Fi+Calling&mnt=WIFI&t=0&u=1012482381&prefl=en_US&pn=2&ciid=pIpvJkM*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D32e511b19d5ecd29%26node_id%3D5036ec4ba367392d%26REQUEST_GUID"
            + "%3D171e7a48-a6e0-a4d2-6433-dde5c76cf97e%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E46%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7a48a6f-0x6e05f5b%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=68.53.26.66&Script=/roverimp/0/0/14&Server=rover.ebay.com&TMachine=10.77.38"
            + ".67&TStamp=18:40:52.46&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.153.11"
            + ".133"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26nqt%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26ec%3D4%26uit%3D1583189780000%3C%2Fa%3E&imp=2051249&lv=udid%3D9d531d97170aaad5f2b032300104c1bd%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AEkYClAJKBqQ2dj6x9nY%252BseQ**%26res%3D1080x2029%26memsz%3D4096.0%26designsystem%3D6%26mnt%3DWIFI%26prefl%3Den_US%26tzname%3DAmerica%2FChicago%26androidid%3D242e1919bddc9378%26ort%3Dp%26carrier%3DXfinity+Mobile+Wi-Fi+Calling%26dpi%3D391.8855x391.29675%26dn%3DSM-G975U%26apn%3D1%26mrollp%3D62%26c%3D9%26ids%3DMP%253Dmarines1624%26osv%3D10%26ist%3D0%26%21xt%3D59341%26mlocerr%3D1%26user_name%3Dmarines1624%26reqts%3D1588729252456%26tz%3D-5.0%26mos%3DAndroid%26mlocset%3D3%26gadid%3Db1f485c6-ea40-482b-8548-aa94728ab48d%2C0%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T18%3A40%3A52.260");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(9);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1012482381");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    // 6
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("9d531d97170aaad5f2b032300104c1bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2174529);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:40:15.632")));
    ubiEvent.setRemoteIP("10.153.189.142");
    ubiEvent.setRequestCorrelationId("0915531b9779ee47");
    ubiEvent.setSid("p2051246");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3F%3B2%28twwgsvv%28umj%28bad%7F%29%60jk-171e7a489a3-0x123");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("68.53.26.66");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(765202762148L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&FollowFilter=searches&tz=-5.0&lv=1&dn=SM-G975U&ist=0&uc=1&mos=Android&nqt=UAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*&uaid=e7a489a31710ad329b276d299e431db2S0&osv=10&ul=en-US&mtsts=2020-05-05T18%3A40%3A15.632&ec=4&app=2571&res=1080x2029&sort=alpha&cguidsrc=cookie&es=0&cflgs=EA**&memcnt=6&ids=MP%253Dmarines1624&designsystem=6&mppid=0&mrollp=62&gadid=b1f485c6-ea40-482b-8548-aa94728ab48d%2C0&androidid=242e1919bddc9378&pcguid=9d5323d01700ad4d14769f4ce029570f&rq=0915531b9779ee47&user_name=marines1624&tzname=America%2FChicago&bs=0&memsz=4096.0&intrstcnt=7&!xt=59341&pagename=Following&uit=1583189780000&c=6&mav=6.0.1&g=9d531d97170aaad5f2b032300104c1bd&h=97&reqts=1588729252243&n=9d5323d01700ad4d14769f4ce029570f&ort=p&p=2174529&ttp=Page&carrier=Xfinity+Mobile+Wi-Fi+Calling&mnt=WIFI&t=0&u=1012482381&prefl=en_US&pn=2&ciid=pImjKbI*&sid=p2051246");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0915531b9779ee47%26node_id%3D48ef1ea267e21149%26REQUEST_GUID"
            + "%3D171e7a48-9a20-ad32-9b26-ea1aca942ef6%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253F%253B2%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7a489a3-0x123%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=68.53.26.66&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.211.41"
            + ".178&TStamp=18:40:52.25&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.153"
            + ".189"
            + ".142"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26nqt%3DUAAAAAAABAAAAAEABABAAAAAAQASAAAAAIAAACAAAQAAAQAAQAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAATAEIAAAAAAAggAE*%26ec%3D4%26uit%3D1583189780000%3C%2Fa%3E&imp=2174529&lv=udid%3D9d531d97170aaad5f2b032300104c1bd%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AEkYClAJKBqQ2dj6x9nY%252BseQ**%26res%3D1080x2029%26memsz%3D4096.0%26sort%3Dalpha%26designsystem%3D6%26mnt%3DWIFI%26prefl%3Den_US%26tzname%3DAmerica%2FChicago%26androidid%3D242e1919bddc9378%26ort%3Dp%26carrier%3DXfinity+Mobile+Wi-Fi+Calling%26memcnt%3D6%26dpi%3D391.8855x391.29675%26dn%3DSM-G975U%26intrstcnt%3D7%26mrollp%3D62%26c%3D6%26ids%3DMP%253Dmarines1624%26osv%3D10%26FollowFilter%3Dsearches%26ist%3D0%26%21xt%3D59341%26user_name%3Dmarines1624%26reqts%3D1588729252243%26tz%3D-5.0%26mos%3DAndroid%26bncnt%3D0%26gadid%3Db1f485c6-ea40-482b-8548-aa94728ab48d%2C0%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T18%3A40%3A15.632&_trksid=p2051246");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(6);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("1012482381");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(2571);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(1, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric19() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e7a455bc1710a866609d6af7ffbff02d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:40:39.086")));
    ubiEvent.setRemoteIP("10.105.220.23");
    ubiEvent.setRequestCorrelationId("bd12071140599762");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*062363%28twwgsvv%28umj%28bad%7F%29%60jk-171e7a4562f-0x191");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("86.20.187.195");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.5 Mobile/15E148 Safari/604.1,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(942983304868L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB&c=1&g=e7a455bc1710a866609d6af7ffbff02d&h=bc&px=4249&chnl=9&uc=1&es=3&nqt=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB&p=2317508&uaid=e7a4562f1710a128edb4d670d07c2e5dS0&bs=0&t=3&cflgs=QA**&ul=en-US&plmt=VQkAAB%252BLCAAAAAAAAADFVU1z2zYQ%252FSsaZCYnWQK%252FRd9kt2nTcSdJZSeHTCYDAqCIigRYAJTiePzfuwvqw46TGefS2gdoH4Bd7Nu3yzvSr8l5nCRpFqdT0ity%252FvGOKEHOI0oXi2xKOEAxjQpaxpSWtMiKKbGAkbiUohJ1nHJZpVXCF1nKcwDjnKYF5xmZEunB3SdY2egWrumhbafEjEAPbprbykI8CARGlCR4z8PGHWG4veJMC9biPkNvJM6TEs8kUZHHsOZFShcEghiPDgDZ7OBHJZkVE29V10kL4PBzL1Y9BqeAKM7t%252Fqfu2T7EWvT2xkm7N%252Fu288gZGUkDhHFMR37x5B62QzpOfZW4NlKtGwgTZ3RKdkr4hpwnlMI5Lf3O2M1r9JQs0gL9iButfEBkxW5nw2bmIDPezLudrDD0ULXKNde3PTiPwQfrN0q%252F%252FQZ1I4t%252FrMJD9oZ7L61TRiMzs2gWJUiTbcGcz5WdYTznmVd8xk0353a%252BnfNo7vh8dfB2Nt47S2ed0rO%252FHSaLabpQ3R8metp6uBPTeyC%252Bbgdksmatk0C%252Be71cPbCu2NfbK8NOBxzr5Aoi%252FiVrK11zOqrfK7l7grrfVqtfNataCS68HQCTegsZ99aIgXtkA%252BSn%252BAaF8BzBOKBiGYr0wxI55SVWMAHdqCATWbA0yyoeFRFlizzPaSlyVhd1XdU1jQW5RyoYdlmWZTTKUQnbQ%252FN0gEewfCHnQJ%252FhwWIuWMwEy4lQ6PCMd4O0t086AiplatQ6RuJYCljgVijPvv%252FzMj72f442pXGcHPqfxVFVFjyXLM9TEVclS0TJUlmJMi%252Br%252BLn9zx07ND9NkmPzN5IJaS9Nayx2%252Fa4BEj%252BPIJx5ZH46aoiwwRts1KGrpH1TLwUqsZyWcKRVGnUJRaiN9q9Yp1pkZSutYJpNJ1BIdwYtrWoyHkFZXSvfQgdFyQn6RTpuVR%252Bk8njDdEzpK6U3I87x7cHBiJEXNM%252FSiyXZbz08%252F%252B3eNU4OQJPwd0AvGN%252BsrRk0aujFqwX%252BY7YmRLnRQAZmeVC2R%252FTCtEepQ64gbdauesaVXoea44XfQzc%252BSiymj7YePBV3wmuWYiV7ZpnHCo0VQZl6UFg1oJd9mAvZmh3q6TiijyMRav%252BWreWbENSFmS8QCGWCyYZd8zF01dmwOaPYBg8MKOo%252F39P2ZHLZwvyG4TZ5ObkeMRdmqZcOX2BqrDFvmNYSx11XS4%252BET0mDpsQZII4pLyuzfVz1kc1xhl3Cw72Fo%252FI0lNboZdiQQOHmmtl1mCafq5ZpRPngvOmWJ6b25V714MxYKSYYF5%252FsZde3bBwfwB%252FmZDRrQW0iaDsEvH%252F6FehkmDPPaNE9C%252BNN8qepVCs%252FjGPrv%252Fxm%252FPSE%252FM7g%252Fj%252BHJthV%252BARQSu7%252FBY%252B%252FPetVCQAA&ec=2&pn=2&rq=bd12071140599762&pagename=cos__mfe&po=%5B%28pg%3A2334524+pid%3A100885%29%28pg%3A2334524+pid%3A100692%29%5D&ciid=pFYvjts*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dbd12071140599762%26node_id%3Dbcfd18ce08755a69%26REQUEST_GUID"
            + "%3D171e7a45-62e0-a128-edb2-b9bee8083794%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A062363%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7a4562f-0x191%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=86.20.187.195&Script=/roverimp/0/2334524/9&Server=internal.rover.vip.ebay"
            + ".com&TMachine=10.18.142.219&TStamp=18:40:39.08&TName=roverimp&Agent=Mozilla/5.0 "
            + "(iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko)"
            + " Version/13.0.5 Mobile/15E148 Safari/604.1,GingerClient/2.9.7-RELEASE&RemoteIP=10"
            + ".105"
            + ".220.23"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2334524/9?site=3&trknvp=plmt%3DVQkAAB%252BLCAAAAAAAAADFVU1z2zYQ"
            + "%252FSsaZCYnWQK"
            +
            "%252FRd9kt2nTcSdJZSeHTCYDAqCIigRYAJTiePzfuwvqw46TGefS2gdoH4Bd7Nu3yzvSr8l5nCRpFqdT0ity"
            +
            "%252FvGOKEHOI0oXi2xKOEAxjQpaxpSWtMiKKbGAkbiUohJ1nHJZpVXCF1nKcwDjnKYF5xmZEunB3SdY2egWrumhbafEjEAPbprbykI8CARGlCR4z8PGHWG4veJMC9biPkNvJM6TEs8kUZHHsOZFShcEghiPDgDZ7OBHJZkVE29V10kL4PBzL1Y9BqeAKM7t%252Fqfu2T7EWvT2xkm7N%252Fu288gZGUkDhHFMR37x5B62QzpOfZW4NlKtGwgTZ3RKdkr4hpwnlMI5Lf3O2M1r9JQs0gL9iButfEBkxW5nw2bmIDPezLudrDD0ULXKNde3PTiPwQfrN0q%252F%252FQZ1I4t%252FrMJD9oZ7L61TRiMzs2gWJUiTbcGcz5WdYTznmVd8xk0353a%252BnfNo7vh8dfB2Nt47S2ed0rO%252FHSaLabpQ3R8metp6uBPTeyC%252Bbgdksmatk0C%252Be71cPbCu2NfbK8NOBxzr5Aoi%252FiVrK11zOqrfK7l7grrfVqtfNataCS68HQCTegsZ99aIgXtkA%252BSn%252BAaF8BzBOKBiGYr0wxI55SVWMAHdqCATWbA0yyoeFRFlizzPaSlyVhd1XdU1jQW5RyoYdlmWZTTKUQnbQ%252FN0gEewfCHnQJ%252FhwWIuWMwEy4lQ6PCMd4O0t086AiplatQ6RuJYCljgVijPvv%252FzMj72f442pXGcHPqfxVFVFjyXLM9TEVclS0TJUlmJMi%252Br%252BLn9zx07ND9NkmPzN5IJaS9Nayx2%252Fa4BEj%252BPIJx5ZH46aoiwwRts1KGrpH1TLwUqsZyWcKRVGnUJRaiN9q9Yp1pkZSutYJpNJ1BIdwYtrWoyHkFZXSvfQgdFyQn6RTpuVR%252Bk8njDdEzpK6U3I87x7cHBiJEXNM%252FSiyXZbz08%252F%252B3eNU4OQJPwd0AvGN%252BsrRk0aujFqwX%252BY7YmRLnRQAZmeVC2R%252FTCtEepQ64gbdauesaVXoea44XfQzc%252BSiymj7YePBV3wmuWYiV7ZpnHCo0VQZl6UFg1oJd9mAvZmh3q6TiijyMRav%252BWreWbENSFmS8QCGWCyYZd8zF01dmwOaPYBg8MKOo%252F39P2ZHLZwvyG4TZ5ObkeMRdmqZcOX2BqrDFvmNYSx11XS4%252BET0mDpsQZII4pLyuzfVz1kc1xhl3Cw72Fo%252FI0lNboZdiQQOHmmtl1mCafq5ZpRPngvOmWJ6b25V714MxYKSYYF5%252FsZde3bBwfwB%252FmZDRrQW0iaDsEvH%252F6FehkmDPPaNE9C%252BNN8qepVCs%252FjGPrv%252Fxm%252FPSE%252FM7g%252Fj%252BHJthV%252BARQSu7%252FBY%252B%252FPetVCQAA%26po%3D%5B%28pg%3A2334524+pid%3A100885%29%28pg%3A2334524+pid%3A100692%29%5D&trknvpsvc=%3Ca%3Enqc%3DAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB%26nqt%3DAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB%26es%3D3%26ec%3D2%3C%2Fa%3E&tguid=e7a455bc1710a866609d6af7ffbff02d&imp=2317508");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e7a455bc1710a866609d6af7ffbff02d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:40:39.081")));
    ubiEvent.setRemoteIP("86.20.187.195");
    ubiEvent.setRequestCorrelationId("e6f2c6a31710aca06e10b994fed69c98");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Ftilvgig%28%7C%3Egcb*w%60ut3550-171e7a45629-0x46ee");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("86.20.187.195");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.5 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(626363487908L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&sameid=29edbdf24ceb4b3c854c629e26047cc5&sapcxkw=beard+trimmer&schemaversion=3"
            + "&pagename=SandPage&saucxgdpry=true&efam=SAND&ac=&saucxgdprct=false&saty=1&g"
            + "=e7a455bc1710a866609d6af7ffbff02d&saebaypid=100885&h=bc&salv=5&ciid=pFYs1pE*&p"
            + "=2367355"
            + "&r=-2100344230&sapcxcat=26395%2C31762%2C67408&t=3&saiid=2833f499-f9ed-4ec7-9d3f"
            + "-030009bdf1ec&cflgs=AA**&samslid=&eactn=AUCT&pn=2&rq"
            + "=e6f2c6a31710aca06e10b994fed69c98"
            + "&ciid=pFYs1pE*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De6f2c6a31710aca06e10b994fed69c98%26node_id%3D8327fb3647daec3b"
            + "%26REQUEST_GUID%3D171e7a45-6290-a9bd-6912-067ffc83007d%26logid%3Dt6pdhc9%253Ftilvgig"
            + "%2528%257C%253Egcb%2Aw%2560ut3550-171e7a45629-0x46ee&TPool=r1sand&TDuration=5"
            + "&ContentLength=160&ForwardedFor=10.110.67.85&Script=sand&Server=sand.stratus.ebay"
            + ".com&TMachine=10.155.214.145&TStamp=18:40:39.08&TName=sand.v1&Agent=Mozilla/5.0 "
            + "(iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko)"
            + " Version/13.0.5 Mobile/15E148 Safari/604.1&RemoteIP=86.20.187"
            + ".195&Referer=https://www"
            + ".ebay.co.uk/sch/i.html?_nkw=beard trimmer&_sacat=0&_pgn=2"));
    ubiEvent.setUrlQueryString(
        "/sch/i.html?_nkw=beard trimmer&_sacat=0&_pgn=3");
    ubiEvent.setPageName("sand.v1");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(-2100344230L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay.co.uk/sch/i.html?_nkw=beard trimmer&_sacat=0&_pgn=2");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("e7a455bc1710a866609d6af7ffbff02d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 18:40:39.400")));
    ubiEvent.setRemoteIP("86.20.187.195");
    ubiEvent.setRequestCorrelationId("bd12071140599762");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%2853311%3F2%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e7a4576a"
            + "-0xd9");
    ubiEvent.setEventFamily("LST");
    ubiEvent.setEventAction("ANSTRIG");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("86.20.187.195");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Version/13.0.5 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(65554962340L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("www.ebay.co.uk");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&uc=3&modulereq=prov%3AVEHICLE_PARTS_PROVIDER%7Cmi%3A1003%7Cenabled%3A1"
            + "%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov%3ASEARCH_GUIDANCE_PROVIDER"
            + "%7Cmi%3A2682%7Cscen%3AShopByBrand%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A1%2Cprov"
            + "%3ASEARCH_GUIDANCE_PROVIDER%7Cmi%3A2682%7Cscen%3AShopByType%7Cenabled%3A1"
            + "%7Ccandidate"
            + "%3A1%7Ciid%3A2%2Cprov%3ASEARCH_GUIDANCE_PROVIDER%7Cmi%3A2682%7Cscen%3AShopByFeatures"
            + "%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A3%2Cprov%3ASPONSORED_ITEMS_ANSWER_PROVIDER"
            + "%7Cmi"
            + "%3A45600%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov"
            + "%3AMFE_AD_PROVIDER%7Cmi%3A5156%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A1%2Cprov"
            + "%3AMFE_AD_PROVIDER%7Cmi%3A4751%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A1&mos=iOS&bs=3"
            + "&osv=13_3_1&pageci=96de2f8c-8f3a-11ea-ae5f-74dbd1807e50&ul=en-GB&callingpagename"
            + "=searchsvc__experience_search_v1_get_search_result_GET&pagename"
            + "=ANSWERS_PLATFORM_PAGE"
            + "&app=3564&res=480x320&efam=LST&c=1&g=e7a455bc1710a866609d6af7ffbff02d&h=bc&mobile"
            + "=true"
            + "&cp=2351460&sections=rgn%3ARIVER%7Csection%3ATOP_SECTION%7Cslots%3A3%2Crgn%3ARIVER"
            + "%7Csection%3AMIDDLE_SECTION%7Cslots%3A2%2Crgn%3ARIVER%7Csection%3ABOTTOM_SECTION"
            + "%7Cslots%3A4%2Crgn%3ABELOW_RIVER%7Csection%3AFULL_REGION%7Cslots%3A1%2Crgn"
            + "%3ACENTER_TOP%7Csection%3AFULL_REGION%7Cslots%3A1&p=2370942&t=3&cflgs=QA"
            + "**&moduletrig"
            + "=prov%3AMFE_AD_PROVIDER%7Cmi%3A4751%7Ciid%3A0%7Cscen%3A100692%7Cslot%3A2%7Crank%3A1"
            + "%7Cscore%3A0.0%2Cprov%3ASTATUS_BAR_PROVIDER%7Cmi%3A4495%7Ciid%3A0%7Cscen%3AStatusBar"
            + "%7Cslot%3A1%7Crank%3A1%7Cscore%3A0"
            + ".0%2Cprov%3APAGINATION_PROVIDER%7Cmi%3A1686%7Ciid%3A0%7Cscen%3APAGINATION_ALL%7Cslot"
            + "%3A51%7Crank%3A2%7Cscore%3A0.0%2Cprov%3AMULTI_ASPECT_GUIDANCE_PROVIDER%7Cmi%3A43632"
            + "%7Ciid%3A0%7Cscen%3AMULTI_ASPECT_GUIDANCE_PROVIDER%7Cslot%3A1%7Crank%3A1%7Cscore%3A0"
            + ".0%2Cprov%3AMFE_AD_PROVIDER%7Cmi%3A5156%7Ciid%3A0%7Cscen%3A100885%7Cslot%3A1%7Crank"
            + "%3A1%7Cscore%3A0.0&eactn=ANSTRIG&rq=bd12071140599762&ciid=pFdhQw8*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dbd12071140599762%26node_id%3D2f1199f65eba2e21%26REQUEST_GUID"
            + "%3D171e7a45-7610-acc4-30f5-ab54fbc4a91a%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dpiesqn47pse31"
            + "%252853311%253F2%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e7a4576a-0xd9"
            + "%26cal_mod%3Dfalse&TPool=r1searchsvc&TDuration=3&TStatus=0&TType=URL&ContentLength"
            + "=2087&ForwardedFor= 23.212.1.71;86.20.187.195;95.100.156"
            + ".181&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.co.uk&TMachine=10.204.67"
            + ".15&TStamp=18:40:39.40&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/13.0.5 Mobile/15E148 Safari/604.1&RemoteIP=86.20.187"
            + ".195&Encoding=gzip&Referer=https://www.ebay.co.uk/sch/i"
            + ".html?_nkw=beard+trimmer&_sacat=0&_pgn=2"));
    ubiEvent.setUrlQueryString(
        "/sch/i.html?_nkw=beard+trimmer&_sacat=0&_pgn=3");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://www.ebay.co.uk/sch/i.html?_nkw=beard+trimmer&_sacat=0&_pgn=2");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(1, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric20() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2054081);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.318")));
    ubiEvent.setRemoteIP("10.173.226.107");
    ubiEvent.setRequestCorrelationId("4d4f2c420ddce57d");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E7%3C%28twwgsvv%28umj%28bad%7F%29%60jk-171e7c3401e-0x114");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo("eBayiPhone/6.0.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(579906453699L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&user_name=geosoc1&tz=-6.00&nid=588088590080&lv=1&dn=iPhone12_1&tzname=America%2FDenver&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&uaid=e7c3401e1710a4d05876588498d86d7cS0&memsz=3.75&osv=13.4.1&ul=en-US&mtsts=2020-05-05T19%3A14%3A24.318&ec=5&pagename=NotificationReceived&app=1462&res=828X1792&uit=1569968239000&c=1&mav=6.0.0&g=8953ec6916d93c1d43031330013330d2&idfa=00000000-0000-0000-0000-000000000000%2C0&h=69&reqts=1588731265036&cguidsrc=cookie&ntype=M2MMSGHDR&n=89663faa16d0a4d128c7a974e64bb162&es=0&ort=P&p=2054081&ttp=Page&mnt=wifi&carrier=wifi&t=0&u=15901402&prefl=en-US&cflgs=EA**&ids=MP%253Dgeosoc1&designsystem=6&mrollp=30.02&pcguid=89663faa16d0a4d128c7a974e64bb162&pn=2&rq=4d4f2c420ddce57d&ciid=w0AeBYc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4d4f2c420ddce57d%26node_id%3D95ad90fbe3a8c7cd%26REQUEST_GUID"
            + "%3D171e7c34-01d0-a4d0-5877-4bcfc7a15d1b%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E7%253C%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7c3401e-0x114%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=71.205.28.140&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.5"
            + ".135&TStamp=19:14:25.05&TName=roverimp&Agent=eBayiPhone/6.0.0&RemoteIP=10.173.226"
            + ".107"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26nqt%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26ec%3D5%26uit%3D1569968239000%3C%2Fa%3E&imp=2054081&lv=udid%3D8953ec6916d93c1d43031330013330d2%26ai%3D1462%26mav%3D6.0.0%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wJkYeoCJGGoQ6dj6x9nY%252BseQ**%26memsz%3D3.75%26res%3D828X1792%26mrollp%3D30.02%26designsystem%3D6%26c%3D1%26nid%3D588088590080%26osv%3D13.4.1%26ids%3DMP%253Dgeosoc1%26mnt%3Dwifi%26prefl%3Den-US%26un%3Dgeosoc1%26tzname%3DAmerica%2FDenver%26reqts%3D1588731265036%26tz%3D-6.00%26mos%3DiOS%26ntype%3DM2MMSGHDR%26ort%3DP%26carrier%3Dwifi%26uc%3DUS%26dn%3DiPhone12_1%26idfa%3D00000000-0000-0000-0000-000000000000%2C0%26ttp%3DPage%26mtsts%3D2020-05-05T19%3A14%3A24.318");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 2
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2050535);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.442")));
    ubiEvent.setRemoteIP("10.173.226.107");
    ubiEvent.setRequestCorrelationId("4d4f2c420ddce57d");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E7%3C%28twwgsvv%28umj%28bad%7F%29%60jk-171e7c3401c-0x129");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo("eBayiPhone/6.0.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(579906257091L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&user_name=geosoc1&tz=-6.00&lv=1&dn=iPhone12_1&tzname=America%2FDenver&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&uaid=e7c3401b1710a4d05876588498d86d7eS0&memsz=3.75&osv=13.4.1&ul=en-US&maup=0&mtsts=2020-05-05T19%3A14%3A24.442&ec=5&pagename=Launch&aiBadgeCnt=1&app=1462&res=828X1792&uit=1569968239000&c=3&mav=6.0.0&g=8953ec6916d93c1d43031330013330d2&idfa=00000000-0000-0000-0000-000000000000%2C0&h=69&reqts=1588731265035&cguidsrc=cookie&n=89663faa16d0a4d128c7a974e64bb162&es=0&ort=P&p=2050535&ttp=Page&mnt=wifi&carrier=wifi&t=0&u=15901402&prefl=en-US&cflgs=EA**&mlch=0&ids=MP%253Dgeosoc1&designsystem=6&mrollp=30.02&pcguid=89663faa16d0a4d128c7a974e64bb162&pn=2&rq=4d4f2c420ddce57d&ciid=w0AbBYc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4d4f2c420ddce57d%26node_id%3Dc0046ca32c91c312%26REQUEST_GUID"
            + "%3D171e7c34-01b0-a4d0-5877-4bcfc7a15d1c%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E7%253C%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7c3401c-0x129%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=71.205.28.140&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.5"
            + ".135&TStamp=19:14:25.05&TName=roverimp&Agent=eBayiPhone/6.0.0&RemoteIP=10.173.226"
            + ".107"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26nqt%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26ec%3D5%26uit%3D1569968239000%3C%2Fa%3E&imp=2050535&lv=udid%3D8953ec6916d93c1d43031330013330d2%26ai%3D1462%26mav%3D6.0.0%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wJkYeoCJGGoQ6dj6x9nY%252BseQ**%26memsz%3D3.75%26res%3D828X1792%26mrollp%3D30.02%26designsystem%3D6%26c%3D3%26osv%3D13.4.1%26ids%3DMP%253Dgeosoc1%26mnt%3Dwifi%26prefl%3Den-US%26un%3Dgeosoc1%26tzname%3DAmerica%2FDenver%26reqts%3D1588731265035%26tz%3D-6.00%26maup%3D0%26mos%3DiOS%26mlch%3D0%26aiBadgeCnt%3D1%26ort%3DP%26carrier%3Dwifi%26uc%3DUS%26dn%3DiPhone12_1%26idfa%3D00000000-0000-0000-0000-000000000000%2C0%26ttp%3DPage%26mtsts%3D2020-05-05T19%3A14%3A24.442");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(3);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 3
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2530290);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.742")));
    ubiEvent.setRemoteIP("71.205.28.140");
    ubiEvent.setRequestCorrelationId("a326695d57f0111f");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%28574521-vrubqst-whh-%60dfz%2Behn-171e7c33ee6-0xea");
    ubiEvent.setEventFamily("MFE");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;414x896;2.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(179362938563L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAKAABAAg*&flgs=AA**&dm=Apple&dn=iPhone12_1&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&osv=13.4.1&plmt=twAAAB%252BLCAAAAAAAAAA1jtsKgzAQRP9lnlPYxLXJ%252BivFh5jUImiV3ihI%252Fr2r0qcDw87ZWbHc0DiWSjwZLAOay4oho7FE4tkgaXQSg4cSubM2SOwk%252BIrPdQg1%252B1565zk4ztnC4PpSRauMh0pr9%252Fc4GsRNICJE21n8%252FPNJc309fXfMaUd8Hph1SFFbUqtT6DAqbfkBeFdxZrcAAAA%253D&ul=en-US&callingpagename=cartexsvc__DefaultPage&ec=5&pagename=reco__experience_merchandising_v1_module_provider_GET&app=1462&res=0x0&uit=1569968239000&efam=MFE&mav=6.0.0&g=8953ec6916d93c1d43031330013330d2&rpg=2493970&h=69&nativeApp=true&cp=2493970&an=eBayIOS&n=89663faa16d0a4d128c7a974e64bb162&es=0&p=2530290&t=0&u=15901402&cflgs=AA**&eactn=EXPM&rq=a326695d57f0111f&po=%5B%28pg%3A2493970+pid%3A100974%29%5D&ciid=wz7dwik*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0a1eaf899c6b9695%26node_id%3Db05e9803cef7b465%26REQUEST_GUID"
            + "%3D171e7c33-ee30-a69c-2292-6c51ffa0a16b%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%2528574521-vrubqst-whh-%2560dfz%252Behn-171e7c33ee6-0xea%26cal_mod%3Dfalse&TPool"
            + "=r1reco&TDuration=1&TStatus=0&TType=URL&ContentLength=1430&ForwardedFor=71.205.28"
            + ".140;"
            + "184.25.204.167&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay"
            + ".com&TMachine=10"
            + ".105.194.41&TStamp=19:14:24.74&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;"
            + "414x896;2"
            + ".0&RemoteIP=71.205.28.140&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping_cart/v1/cart?mode=lite");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 4
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2051248);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.444")));
    ubiEvent.setRemoteIP("10.173.226.107");
    ubiEvent.setRequestCorrelationId("4d4f2c420ddce57d");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E7%3C%28twwgsvv%28umj%28bad%7F%29%60jk-171e7c34019"
            + "-0x6ad0b7f");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo("eBayiPhone/6.0.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(579906126019L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&user_name=geosoc1&tz=-6.00&lv=1&dn=iPhone12_1&tzname=America%2FDenver&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&uaid=e7c340191710a4d05876588498d86d82S0&mlocset=2&memsz=3.75&osv=13.4.1&ul=en-US&mtsts=2020-05-05T19%3A14%3A24.444&ec=5&pagename=Foreground&app=1462&res=828X1792&uit=1569968239000&c=4&mav=6.0.0&g=8953ec6916d93c1d43031330013330d2&idfa=00000000-0000-0000-0000-000000000000%2C0&h=69&reqts=1588731265035&cguidsrc=cookie&n=89663faa16d0a4d128c7a974e64bb162&es=0&ort=P&p=2051248&ttp=Page&mnt=wifi&carrier=wifi&t=0&u=15901402&prefl=en-US&cflgs=EA**&ids=MP%253Dgeosoc1&designsystem=6&mrollp=30.02&pcguid=89663faa16d0a4d128c7a974e64bb162&pn=2&rq=4d4f2c420ddce57d&ciid=w0AZBYc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4d4f2c420ddce57d%26node_id%3Dcc045d5013b6ded3%26REQUEST_GUID"
            + "%3D171e7c34-0180-a4d0-5877-4bcfc7a15d1e%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E7%253C%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7c34019-0x6ad0b7f"
            + "%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1"
            + "&ForwardedFor=71.205.28.140&Script=/roverimp/0/0/14&Server=localhost&TMachine=10"
            + ".77.5"
            + ".135&TStamp=19:14:25.04&TName=roverimp&Agent=eBayiPhone/6.0.0&RemoteIP=10.173.226"
            + ".107"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26nqt%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26ec%3D5%26uit%3D1569968239000%3C%2Fa%3E&imp=2051248&lv=udid%3D8953ec6916d93c1d43031330013330d2%26ai%3D1462%26mav%3D6.0.0%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wJkYeoCJGGoQ6dj6x9nY%252BseQ**%26memsz%3D3.75%26res%3D828X1792%26mrollp%3D30.02%26designsystem%3D6%26c%3D4%26osv%3D13.4.1%26ids%3DMP%253Dgeosoc1%26mnt%3Dwifi%26prefl%3Den-US%26un%3Dgeosoc1%26tzname%3DAmerica%2FDenver%26reqts%3D1588731265035%26tz%3D-6.00%26mos%3DiOS%26mlocset%3D2%26ort%3DP%26carrier%3Dwifi%26uc%3DUS%26dn%3DiPhone12_1%26idfa%3D00000000-0000-0000-0000-000000000000%2C0%26ttp%3DPage%26mtsts%3D2020-05-05T19%3A14%3A24.444");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(4);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 5
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:25.009")));
    ubiEvent.setRemoteIP("71.205.28.140");
    ubiEvent.setRequestCorrelationId("1Z%2FmkuiOmmzjjccl");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dpiesqn47pse31%2853%3B%3C4%3E5%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh"
            + "-171e7c33ff2-0xe8");
    ubiEvent.setEventFamily("LST");
    ubiEvent.setEventAction("ANSTRIG");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;414x896;2.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(553629859779L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&dm=Apple&dn=iPhone12_1&uc=1&modulereq=prov%3ANATIVE_AFS_ADS_PROVIDER%7Cmi"
            + "%3A45983%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A1%2Cprov%3AVEHICLE_PARTS_PROVIDER"
            + "%7Cmi"
            + "%3A1003%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A1%2Cprov%3ASEARCH_GUIDANCE_PROVIDER"
            + "%7Cmi"
            + "%3A4213%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov"
            + "%3ARELATED_SEARCHES_PROVIDER%7Cmi%3A4117%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A1"
            + "%2Cprov%3ASPONSORED_ITEMS_ANSWER_PROVIDER%7Cmi%3A45600%7Cenabled%3A1%7Ccandidate%3A0"
            + "%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov%3AMFE_MERCHANT_PROMOTION_PROVIDER%7Cenabled"
            + "%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1&mos=iOS&bs=0&pageci=4e4cd0aa"
            + "-8f3f-11ea-99ee-74dbd1804d73&osv=13.4"
            + ".1&ul=en-US&pagename=ANSWERS_PLATFORM_PAGE&app=1462&res=0x0&efam=LST&mav=6.0"
            + ".0&g=8953ec6916d93c1d43031330013330d2&h=69&nativeApp=true&an=eBayIOS&sections=rgn"
            + "%3ASTATUS_BAR_REGION%7Csection%3AFULL_REGION%7Cslots%3A1%2Crgn%3ARIVER%7Csection"
            + "%3ATOP_SECTION%7Cslots%3A3%2Crgn%3ARIVER%7Csection%3AMIDDLE_SECTION%7Cslots%3A2"
            + "%2Crgn"
            + "%3ARIVER%7Csection%3ABOTTOM_SECTION%7Cslots%3A4%2Crgn%3ACENTER_TOP%7Csection"
            + "%3AFULL_REGION%7Cslots%3A1&n=89663faa16d0a4d128c7a974e64bb162&p=2370942&t=0&u"
            + "=15901402"
            + "&cflgs=AA**&moduletrig=prov%3ARELATED_SEARCHES_PROVIDER%7Cmi%3A4117%7Ciid%3A0%7Cscen"
            + "%3A%7Cslot%3A9%7Crank%3A1%7Cscore%3A0"
            + ".0%2Cprov%3ASTATUS_BAR_PROVIDER%7Cmi%3A4495%7Ciid%3A0%7Cscen%3AStatusBar%7Cslot%3A1"
            + "%7Crank%3A1%7Cscore%3A0.0%2Cprov%3ANATIVE_AFS_ADS_PROVIDER%7Cmi%3A45983%7Ciid%3A0"
            + "%7Cscen%3ANativeAfsAds%7Cslot%3A9%7Crank%3A1%7Cscore%3A0"
            + ".0%2Cprov%3ASAVE_SEARCH_ANSWER_PROVIDER%7Cmi%3A44880%7Ciid%3A0%7Cscen"
            + "%3ASAVE_SEARCH_ALL%7Cslot%3A2%7Crank%3A1%7Cscore%3A0"
            + ".0&eactn=ANSTRIG&rq=1Z%2FmkuiOmmzjjccl&ciid=wz%2Fp5oA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D1Z%2FmkuiOmmzjjccl%26node_id%3D2fef6eb2b5f3f43d%26REQUEST_GUID"
            + "%3D171e7c33ed7.ad383d6.1b621.81a97c87%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31"
            + "%252853%253B%253C4%253E5%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e7c33ff2"
            + "-0xe8"
            + "%26cal_mod%3Dfalse&TPool=r1srchsvc1&TDuration=2&TStatus=0&TType=URL&ContentLength"
            + "=1865"
            + "&ForwardedFor=71.205.28.140;184.25.204"
            + ".167&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.173.230"
            + ".128&TStamp=19:14:25.00&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayIOS;6"
            + ".0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;414x896;2.0&RemoteIP=71.205.28"
            + ".140&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/search/v2");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 6
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(3962);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:25.408")));
    ubiEvent.setRemoteIP("71.205.28.140");
    ubiEvent.setRequestCorrelationId("63534c254a7bcb67");
    ubiEvent.setSid("e11050");
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*422637%28twwgsvv%28umj%28bad%7F%29%60jk-171e7c34184-0x12d");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
            + " like Gecko) Mobile/15E148");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(204740968899L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "webview=true&segname=11050&osub=-1%7E1&chnl=7&uc=1&sid=e11050&bs=0&uaid"
            + "=e7c341811710aafab2f161519ddbe568S0&ref=https%3A%2F%2Febay"
            + ".com%2F&bu=43197876151&crd=20200505191352&ul=en-US&pagename=roveropen&app=1462&c=1"
            + "&euid=5d514476ae794bbc9546d531f12acb9c&ch=osgood&g"
            + "=8953ec6916d93c1d43031330013330d2&h"
            + "=69&n=ee8d4ee714c0a7e348a081b0fb9fa503&p=3962&r=-1245461096&t=0&cflgs=EA**&pn=2&rq"
            + "=63534c254a7bcb67&ciid=w0GDqy8*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D63534c254a7bcb67%26node_id%3Dc1a72e15f4772a33%26REQUEST_GUID"
            + "%3D171e7c34-1800-aafa-b2f1-aedbca5cd62f%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A422637%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7c34184-0x12d%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=4&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=71.205.28.140&Script=/roveropen/0/e11050/7&Server=rover.ebay.com&TMachine=10.175"
            + ".171"
            + ".47&TStamp=19:14:25.40&TName=roveropen&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Mobile/15E148&RemoteIP=71.205.28.140&Encoding=gzip, deflate,"
            + " br&Referer=https://ebay"
            + ".com/"));
    ubiEvent.setUrlQueryString(
        "/roveropen/0/e11050/7?osub=-1%7E1&crd=20200505191352&sojTags=bu%3Dbu%2Cch%3Dch%2Csegname"
            + "%3Dsegname%2Ccrd%3Dcrd%2Cosub%3Dosub&ch=osgood&segname=11050&bu=43197876151&euid"
            + "=5d514476ae794bbc9546d531f12acb9c");
    ubiEvent.setPageName("roveropen");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(-1245461096L);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer("https://ebay.com/");
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 7
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2493970);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.749")));
    ubiEvent.setRemoteIP("71.205.28.140");
    ubiEvent.setRequestCorrelationId("a326695d57f0111f");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%28752647-vrubqst-whh-%60dfz%2Behn-171e7c33eed-0x1f8");
    ubiEvent.setEventFamily("CART");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;414x896;2.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(775991148227L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AEQ*");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAKAABAAg*&flgs=AEQ*&cartic=0&dm=Apple&dn=iPhone12_1&mcc=false&uic=0&uc=1&tic=0&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&osv=13.4.1&nmq=0&ul=en-US&callingpagename=cartexsvc__DefaultPage&ec=5&pagename=cartexsvc__DefaultPage&app=1462&res=0x0&uit=1569968239000&efam=CART&mav=6.0.0&crtsz=0&ebc=5043141117&g=8953ec6916d93c1d43031330013330d2&rpg=2493970&h=69&num_itm_unavbl=0&prof=IOS&nativeApp=true&cp=2493970&an=eBayIOS&n=89663faa16d0a4d128c7a974e64bb162&es=0&p=2493970&t=0&u=15901402&cflgs=AA**&cmd=DefaultPage&issmeoic=false&eactn=EXPC&bic=0&rq=a326695d57f0111f&ciid=wz6trLQ*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Db54fb3ee1c9caf5a%26node_id%3De8c44616003bbc4c%26REQUEST_GUID"
            + "%3D171e7c33-eea0-a12a-cb42-395efe7e477c%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%2528752647-vrubqst-whh-%2560dfz%252Behn-171e7c33eed-0x1f8%26cal_mod%3Dfalse&TPool"
            + "=r1cartexsvc&TDuration=2&TStatus=0&TType=URL&ContentLength=1591&ForwardedFor=71"
            + ".205.28"
            + ".140;184.25.204.167&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay"
            + ".com&TMachine=10.18.172.180&TStamp=19:14:24.74&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;"
            + "414x896;2"
            + ".0&RemoteIP=71.205.28.140&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping_cart/v1/cart?mode=lite");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 8
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2530290);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.588")));
    ubiEvent.setRemoteIP("71.205.28.140");
    ubiEvent.setRequestCorrelationId("2775fd0ee0030e8e");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dosusqn47pse31%2850732%3E6%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e7c33e4d"
            + "-0xc3");
    ubiEvent.setEventFamily("MFE");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;no-carrier;414x896;2.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(664700534467L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAKAABAAg*&flgs=AA**&dm=Apple&dn=iPhone12_1&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&osv=13.4.1&plmt=twAAAB%252BLCAAAAAAAAAA1jtsKgzAQRP9ln1NY05Dt%252BCvFh5iUImiV3ihI%252Fr1TpU8Hhp2zs8pyldYHHGHqZBmkPa8yFGkbVVhwkhkd4OROijdFzFGtFA0wIKpqgvU5xt77kzi5PKnoyLSrWLu9xtFJ%252BgkAqDY8S%252B9%252FPjHn6%252BmzYc4b0mPHzCGVtkyrJzhMa1e%252FEnjFULcAAAA%253D&ul=en-US&callingpagename=cartexsvc__DefaultPage&ec=5&pagename=reco__experience_merchandising_v1_module_provider_GET&app=1462&res=0x0&uit=1569968239000&efam=MFE&mav=6.0.0&g=8953ec6916d93c1d43031330013330d2&rpg=2493970&h=69&nativeApp=true&cp=2493970&an=eBayIOS&n=89663faa16d0a4d128c7a974e64bb162&es=0&p=2530290&t=0&u=15901402&cflgs=AA**&eactn=EXPM&rq=2775fd0ee0030e8e&po=%5B%28pg%3A2493970+pid%3A100974%29%5D&ciid=wz49w5o*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4666727c5dc35971%26node_id%3D840875bf0918c568%26REQUEST_GUID"
            + "%3D171e7c33-e480-ad4c-39a0-3b09fbd43731%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dosusqn47pse31"
            + "%252850732%253E6%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e7c33e4d-0xc3%26cal_mod"
            + "%3Dfalse&TPool=r1reco&TDuration=2&TStatus=0&TType=URL&ContentLength=1426"
            + "&ForwardedFor"
            + "=71.205.28.140;184.25.204.167&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay"
            + ".com&TMachine=10.212.195.154&TStamp=19:14:24.58&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;no-carrier;"
            + "414x896;2.0&RemoteIP=71.205.28.140&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping_cart/v1/cart?mode=lite");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 9
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2370942);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:25.005")));
    ubiEvent.setRemoteIP("71.205.28.140");
    ubiEvent.setRequestCorrelationId("1Z%2FmkuiOmmzjjccl");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285%3E2725%3A-171e7c33fee-0xd9");
    ubiEvent.setEventFamily("LST");
    ubiEvent.setEventAction("ANSTMPL");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;414x896;2.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(553629794243L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr("postalCodeTestQuery");
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&dm=Apple&dn=iPhone12_1&locale=en-US-x-lvariant-MAIN&pagetmpl=SRP&uc=1"
            + "&modulergn=rgn%3ARIVER%7Crank%3A1%7Cmi%3A44880%7Ciid%3A1%7Cuxcg"
            + "%3ASAVE_CARD_MESSAGE_GROUP%7Cscen%3ASAVE_SEARCH_ALL%2Crgn%3ARIVER%7Crank%3A2%7Cmi"
            + "%3A4117%7Ciid%3A1%7Cuxcg%3AQUERY_ANSWER_TEXT_LIST_GROUP%7Cscen%3A%7Csver%3A&mos"
            + "=iOS&bs"
            + "=0&pageci=4e4cd0aa-8f3f-11ea-99ee-74dbd1804d73&osv=13.4"
            + ".1&ul=en-US&pagename=ANSWERS_PLATFORM_PAGE&app=1462&res=0x0&efam=LST&mav=6.0"
            + ".0&ansdomain=SEARCH&g=8953ec6916d93c1d43031330013330d2&h=69&nativeApp=true&an"
            + "=eBayIOS"
            + "&n=89663faa16d0a4d128c7a974e64bb162&p=2370942&anschannel=NATIVE&t=0&sQr"
            + "=postalCodeTestQuery&u=15901402&cflgs=AA**&eactn=ANSTMPL&rq=1Z%2FmkuiOmmzjjccl"
            + "&ciid=wz"
            + "%2Fo5oA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D1Z%2FmkuiOmmzjjccl%26node_id%3D2d2989eb02377678%26REQUEST_GUID"
            + "%3D171e7c33ed7.ad383d6.1b621.81a97c87%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31"
            + "%25285%253E2725%253A-171e7c33fee-0xd9%26cal_mod%3Dfalse&TPool=r1srchsvc1&TDuration=4"
            + "&TStatus=0&TType=URL&ContentLength=1149&ForwardedFor=71.205.28.140;184.25.204"
            + ".167&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.173.230"
            + ".128&TStamp=19:14:25.00&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayIOS;6"
            + ".0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;414x896;2.0&RemoteIP=71.205.28"
            + ".140&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/search/v2");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 10
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2380424);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.447")));
    ubiEvent.setRemoteIP("71.205.28.140");
    ubiEvent.setRequestCorrelationId("5af9aafccca2fcfc");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%2855711%3E-vrubqst-whh-%60dfz%2Behn-171e7c33dbf-0xcf");
    ubiEvent.setEventFamily("NOTF");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;414x896;2.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(845311786435L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "!pgi=2380424%3A0&nqc"
            +
            "=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAKAABAAg*&flgs=AA**&!qt=225272,226680,226658,226345,225831,225841,226465,225061,225064,226101,59377,226079,225405,226613,226009,225387,225987,226006,226108,59950,226601,225539,61088&!ni_nt=MESSAGE_RECEIVED,ITEM_SHIPPED,PackageDeliveryConfirmation,MESSAGE_RECEIVED&dm=Apple&dn=iPhone12_1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&osv=13.4.1&callingpagename=notifexp__experience_notification_inbox_v1_notification_hub_GET&ec=5&pagename=notifexp__experience_notification_inbox_v1_notification_hub_GET&app=1462&res=0x0&uit=1569968239000&efam=NOTF&mav=6.0.0&ni_nC=1&g=8953ec6916d93c1d43031330013330d2&h=69&ni_tC=4&!ni_nsid=124886112942,313065028923,202927160849,124253423802&nativeApp=true&cp=2380424&an=eBayIOS&n=89663faa16d0a4d128c7a974e64bb162&es=0&p=2380424&!ni_nid=452263537136,435029300216,434904397040,433282286096&t=0&u=15901402&cflgs=AA**&eactn=EXPC&!mi=2|5395%3Atrue&rq=5af9aafccca2fcfc&ciid=wz2C0MQ*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D5f7830f51cbbb6bb%26node_id%3Dd34bcf7c3d04cb5f%26REQUEST_GUID"
            + "%3D171e7c33-dbc0-a9bd-0c44-3bf2ffa017e7%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%252855711%253E-vrubqst-whh-%2560dfz%252Behn-171e7c33dbf-0xcf%26cal_mod%3Dfalse"
            + "&TPool"
            + "=r1notifexp&TDuration=1&TStatus=0&TType=URL&ContentLength=1529&ForwardedFor=71.205"
            + ".28"
            + ".140;184.25.204.167&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay"
            + ".com&TMachine=10.155.208.196&TStamp=19:14:24.44&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;Verizon;"
            + "414x896;2"
            + ".0&RemoteIP=71.205.28.140&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/notification_inbox/v1/notification_hub?offset=0&limit=40");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 11
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2054060);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.320")));
    ubiEvent.setRemoteIP("10.173.226.107");
    ubiEvent.setRequestCorrelationId("4d4f2c420ddce57d");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Cumjthu%60t*517%3E7%3C%28twwgsvv%28umj%28bad%7F%29%60jk-171e7c34020-0x121");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo("eBayiPhone/6.0.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(579906584771L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&user_name=geosoc1&tz=-6.00&nid=588088590080&pnact=1&lv=1&dn=iPhone12_1&tzname=America%2FDenver&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&uaid=e7c340201710a4d05876588498d86d7aS0&memsz=3.75&osv=13.4.1&ul=en-US&mtsts=2020-05-05T19%3A14%3A24.320&ec=5&pagename=NotificationAction&app=1462&res=828X1792&uit=1569968239000&c=2&mav=6.0.0&g=8953ec6916d93c1d43031330013330d2&idfa=00000000-0000-0000-0000-000000000000%2C0&h=69&reqts=1588731265036&cguidsrc=cookie&ntype=M2MMSGHDR&n=89663faa16d0a4d128c7a974e64bb162&es=0&ort=P&p=2054060&ttp=Page&mnt=wifi&carrier=wifi&t=0&u=15901402&prefl=en-US&cflgs=EA**&ids=MP%253Dgeosoc1&designsystem=6&mrollp=30.02&pcguid=89663faa16d0a4d128c7a974e64bb162&pn=2&rq=4d4f2c420ddce57d&ciid=w0AgBYc*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4d4f2c420ddce57d%26node_id%3D3f41458df9862a0c%26REQUEST_GUID"
            + "%3D171e7c34-01f0-a4d0-5877-4bcfc7a15d1a%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2A517"
            + "%253E7%253C%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7c34020-0x121%26cal_mod"
            + "%3Dfalse&TPool=r1rover&TDuration=1&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor"
            + "=71.205.28.140&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.5"
            + ".135&TStamp=19:14:25.05&TName=roverimp&Agent=eBayiPhone/6.0.0&RemoteIP=10.173.226"
            + ".107"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26nqt%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26ec%3D5%26uit%3D1569968239000%3C%2Fa%3E&imp=2054060&lv=udid%3D8953ec6916d93c1d43031330013330d2%26ai%3D1462%26mav%3D6.0.0%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wJkYeoCJGGoQ6dj6x9nY%252BseQ**%26memsz%3D3.75%26res%3D828X1792%26mrollp%3D30.02%26pnact%3D1%26designsystem%3D6%26c%3D2%26nid%3D588088590080%26osv%3D13.4.1%26ids%3DMP%253Dgeosoc1%26mnt%3Dwifi%26prefl%3Den-US%26un%3Dgeosoc1%26tzname%3DAmerica%2FDenver%26reqts%3D1588731265036%26tz%3D-6.00%26mos%3DiOS%26ntype%3DM2MMSGHDR%26ort%3DP%26carrier%3Dwifi%26uc%3DUS%26dn%3DiPhone12_1%26idfa%3D00000000-0000-0000-0000-000000000000%2C0%26ttp%3DPage%26mtsts%3D2020-05-05T19%3A14%3A24.320");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(2);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 12
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2493970);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.605")));
    ubiEvent.setRemoteIP("71.205.28.140");
    ubiEvent.setRequestCorrelationId("2775fd0ee0030e8e");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6wwm53vpd77%3C%3Dqkisqn47pse31%285222%3F3-vrubqst-whh-%60dfz%2Behn-171e7c33e5e-0x12a");
    ubiEvent.setEventFamily("CART");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo(
        "ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;no-carrier;414x896;2.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(629446229443L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AEQ*");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAKAABAAg*&flgs=AEQ*&cartic=0&dm=Apple&dn=iPhone12_1&mcc=false&uic=0&uc=1&tic=0&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&osv=13.4.1&nmq=0&ul=en-US&callingpagename=cartexsvc__DefaultPage&ec=5&pagename=cartexsvc__DefaultPage&app=1462&res=0x0&uit=1569968239000&efam=CART&mav=6.0.0&crtsz=0&ebc=5043141117&g=8953ec6916d93c1d43031330013330d2&rpg=2493970&h=69&num_itm_unavbl=0&prof=IOS&nativeApp=true&cp=2493970&an=eBayIOS&n=89663faa16d0a4d128c7a974e64bb162&es=0&p=2493970&t=0&u=15901402&cflgs=AA**&cmd=DefaultPage&issmeoic=false&eactn=EXPC&bic=0&rq=2775fd0ee0030e8e&ciid=wz3rjZI*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4af3f1f8d706a0b7%26node_id%3Dd4771a1eb8f204d6%26REQUEST_GUID"
            + "%3D171e7c33-e590-aaf8-d921-a630fe167314%26logid%3Dt6wwm53vpd77%253C"
            + "%253Dqkisqn47pse31"
            + "%25285222%253F3-vrubqst-whh-%2560dfz%252Behn-171e7c33e5e-0x12a%26cal_mod%3Dfalse"
            + "&TPool"
            + "=r1cartexsvc&TDuration=1&TStatus=0&TType=URL&ContentLength=1596&ForwardedFor=71"
            + ".205.28"
            + ".140;184.25.204.167&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay"
            + ".com&TMachine=10.175.141.146&TStamp=19:14:24.60&TName=Ginger.CollectionSvc"
            + ".track&Agent=ebayUserAgent/eBayIOS;6.0.0;iOS;13.4.1;Apple;iPhone12_1;no-carrier;"
            + "414x896;2.0&RemoteIP=71.205.28.140&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping_cart/v1/cart?mode=lite");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(0);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 13
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2052307);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.444")));
    ubiEvent.setRemoteIP("10.212.191.198");
    ubiEvent.setRequestCorrelationId("e652e96264977a82");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*t1j%7Fa%28rbpv6710-171e7c3acc4-0x151");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo("eBayiPhone/6.0.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(778844810435L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&user_name=geosoc1&tz=-6.00&lv=1&dn=iPhone12_1&tzname=America%2FDenver&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&uaid=e7c3acc41710a6e56b56aedaf1048de9S0&memsz=3.75&osv=13.4.1&ul=en-US&mtsts=2020-05-05T19%3A14%3A24.444&ec=5&pagename=MessageFolder&app=1462&res=828X1792&uit=1569968239000&c=5&mav=6.0.0&mtsgitcommit=d6b703b28b7c71bdb516eedb3d62a9c832dfa163&g=8953ec6916d93c1d43031330013330d2&idfa=00000000-0000-0000-0000-000000000000%2C0&h=69&reqts=1588731292857&cguidsrc=cookie&n=89663faa16d0a4d128c7a974e64bb162&es=0&ort=P&p=2052307&ttp=Page&mnt=wifi&carrier=wifi&t=0&u=15901402&prefl=en-US&cflgs=EA**&ids=MP%253Dgeosoc1&designsystem=6&mrollp=30.02&pcguid=89663faa16d0a4d128c7a974e64bb162&pn=2&rq=e652e96264977a82&ciid=w6zEVrU*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De652e96264977a82%26node_id%3D8d3558196af2d4ff%26REQUEST_GUID"
            + "%3D171e7c3a-cc30-a6e5-6b56-072cf822ed54%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2At1j"
            + "%257Fa%2528rbpv6710-171e7c3acc4-0x151%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=71.205.28"
            + ".140&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.110.86.181&TStamp=19:14:52"
            + ".86&TName=roverimp&Agent=eBayiPhone/6.0.0&RemoteIP=10.212.191.198"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26nqt%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26ec%3D5%26uit%3D1569968239000%3C%2Fa%3E&imp=2052307&lv=udid%3D8953ec6916d93c1d43031330013330d2%26ai%3D1462%26mav%3D6.0.0%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wJkYeoCJGGoQ6dj6x9nY%252BseQ**%26memsz%3D3.75%26res%3D828X1792%26mrollp%3D30.02%26designsystem%3D6%26c%3D5%26osv%3D13.4.1%26ids%3DMP%253Dgeosoc1%26mnt%3Dwifi%26mtsgitcommit%3Dd6b703b28b7c71bdb516eedb3d62a9c832dfa163%26prefl%3Den-US%26un%3Dgeosoc1%26tzname%3DAmerica%2FDenver%26reqts%3D1588731292857%26tz%3D-6.00%26mos%3DiOS%26ort%3DP%26carrier%3Dwifi%26uc%3DUS%26dn%3DiPhone12_1%26idfa%3D00000000-0000-0000-0000-000000000000%2C0%26ttp%3DPage%26mtsts%3D2020-05-05T19%3A14%3A24.444");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(5);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 14
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2051249);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:52.067")));
    ubiEvent.setRemoteIP("10.212.191.198");
    ubiEvent.setRequestCorrelationId("e652e96264977a82");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*t1j%7Fa%28rbpv6710-171e7c3acd4-0x170");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo("eBayiPhone/6.0.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(778845859011L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&user_name=geosoc1&tz=-6.00&lv=1&dn=iPhone12_1&tzname=America%2FDenver&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&uaid=e7c3acd31710a6e56b56aedaf1048ddfS0&mlocset=2&memsz=3.75&osv=13.4.1&ul=en-US&mtsts=2020-05-05T19%3A14%3A52.067&ec=5&pagename=Background&app=1462&res=828X1792&uit=1569968239000&c=9&mav=6.0.0&mtsgitcommit=d6b703b28b7c71bdb516eedb3d62a9c832dfa163&g=8953ec6916d93c1d43031330013330d2&idfa=00000000-0000-0000-0000-000000000000%2C0&h=69&reqts=1588731292858&cguidsrc=cookie&n=89663faa16d0a4d128c7a974e64bb162&es=0&ort=P&p=2051249&ttp=Page&mnt=wifi&carrier=wifi&t=0&u=15901402&prefl=en-US&cflgs=EA**&ids=MP%253Dgeosoc1&designsystem=6&mrollp=30.02&pcguid=89663faa16d0a4d128c7a974e64bb162&pn=2&rq=e652e96264977a82&ciid=w6zUVrU*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De652e96264977a82%26node_id%3D90824c9c3f382bb4%26REQUEST_GUID"
            + "%3D171e7c3a-cd30-a6e5-6b56-072cf822ed4e%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2At1j"
            + "%257Fa%2528rbpv6710-171e7c3acd4-0x170%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=71.205.28"
            + ".140&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.110.86.181&TStamp=19:14:52"
            + ".88&TName=roverimp&Agent=eBayiPhone/6.0.0&RemoteIP=10.212.191.198"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26nqt%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26ec%3D5%26uit%3D1569968239000%3C%2Fa%3E&imp=2051249&lv=udid%3D8953ec6916d93c1d43031330013330d2%26ai%3D1462%26mav%3D6.0.0%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wJkYeoCJGGoQ6dj6x9nY%252BseQ**%26memsz%3D3.75%26res%3D828X1792%26mrollp%3D30.02%26designsystem%3D6%26c%3D9%26osv%3D13.4.1%26ids%3DMP%253Dgeosoc1%26mnt%3Dwifi%26mtsgitcommit%3Dd6b703b28b7c71bdb516eedb3d62a9c832dfa163%26prefl%3Den-US%26un%3Dgeosoc1%26tzname%3DAmerica%2FDenver%26reqts%3D1588731292858%26tz%3D-6.00%26mos%3DiOS%26mlocset%3D2%26ort%3DP%26carrier%3Dwifi%26uc%3DUS%26dn%3DiPhone12_1%26idfa%3D00000000-0000-0000-0000-000000000000%2C0%26ttp%3DPage%26mtsts%3D2020-05-05T19%3A14%3A52.067");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(9);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 15
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2054121);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.460")));
    ubiEvent.setRemoteIP("10.212.191.198");
    ubiEvent.setRequestCorrelationId("e652e96264977a82");
    ubiEvent.setSid("p2054121.m2622");
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*t1j%7Fa%28rbpv6710-171e7c3acca-0x165");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo("eBayiPhone/6.0.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(778845203651L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&tz=-6.00&lv=1&dn=iPhone12_1&uc=1&mos=iOS&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&uaid=e7c3acca1710a6e56b56aedaf1048de5S0&osv=13.4.1&neSound=1&ul=en-US&mtsts=2020-05-05T19%3A14%3A24.460&ec=5&apn=1&app=1462&res=828X1792&idfa=00000000-0000-0000-0000-000000000000%2C0&cguidsrc=cookie&es=0&cflgs=EA**&ids=MP%253Dgeosoc1&neBadge=1&designsystem=6&mrollp=30.02&pcguid=89663faa16d0a4d128c7a974e64bb162&rq=e652e96264977a82&user_name=geosoc1&tzname=America%2FDenver&bs=0&memsz=3.75&pagename=SettingsNotificationsChanged&uit=1569968239000&mav=6.0.0&c=7&mtsgitcommit=d6b703b28b7c71bdb516eedb3d62a9c832dfa163&g=8953ec6916d93c1d43031330013330d2&neAlert=1&h=69&reqts=1588731292858&n=89663faa16d0a4d128c7a974e64bb162&ort=P&p=2054121&ttp=Page&carrier=wifi&mnt=wifi&t=0&u=15901402&prefl=en-US&pn=2&ciid=w6zKVrU*&sid=p2054121.m2622");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De652e96264977a82%26node_id%3D964d6748b1167b98%26REQUEST_GUID"
            + "%3D171e7c3a-cc90-a6e5-6b56-072cf822ed52%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2At1j"
            + "%257Fa%2528rbpv6710-171e7c3acca-0x165%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=71.205.28"
            + ".140&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.110.86.181&TStamp=19:14:52"
            + ".87&TName=roverimp&Agent=eBayiPhone/6.0.0&RemoteIP=10.212.191.198"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26nqt%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26ec%3D5%26uit%3D1569968239000%3C%2Fa%3E&imp=2054121&lv=udid%3D8953ec6916d93c1d43031330013330d2%26ai%3D1462%26mav%3D6.0.0%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wJkYeoCJGGoQ6dj6x9nY%252BseQ**%26res%3D828X1792%26memsz%3D3.75%26designsystem%3D6%26mnt%3Dwifi%26prefl%3Den-US%26tzname%3DAmerica%2FDenver%26neAlert%3D1%26neBadge%3D1%26ort%3DP%26carrier%3Dwifi%26dn%3DiPhone12_1%26apn%3D1%26mrollp%3D30.02%26c%3D7%26ids%3DMP%253Dgeosoc1%26osv%3D13.4.1%26neSound%3D1%26mtsgitcommit%3Dd6b703b28b7c71bdb516eedb3d62a9c832dfa163%26un%3Dgeosoc1%26reqts%3D1588731292858%26tz%3D-6.00%26mos%3DiOS%26uc%3DUS%26idfa%3D00000000-0000-0000-0000-000000000000%2C0%26ttp%3DPage%26mtsts%3D2020-05-05T19%3A14%3A24.460&_trksid=p2054121.m2622");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(7);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 16
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2052307);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:24.451")));
    ubiEvent.setRemoteIP("10.212.191.198");
    ubiEvent.setRequestCorrelationId("e652e96264977a82");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*t1j%7Fa%28rbpv6710-171e7c3acc7-0x16d");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo("eBayiPhone/6.0.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(778845007043L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&user_name=geosoc1&tz=-6.00&lv=1&dn=iPhone12_1&tzname=America%2FDenver&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&uaid=e7c3acc71710a6e56b56aedaf1048de7S0&memsz=3.75&osv=13.4.1&ul=en-US&mtsts=2020-05-05T19%3A14%3A24.451&ec=5&pagename=MessageFolder&app=1462&res=828X1792&uit=1569968239000&c=6&mav=6.0.0&mtsgitcommit=d6b703b28b7c71bdb516eedb3d62a9c832dfa163&g=8953ec6916d93c1d43031330013330d2&idfa=00000000-0000-0000-0000-000000000000%2C0&h=69&reqts=1588731292857&cguidsrc=cookie&n=89663faa16d0a4d128c7a974e64bb162&es=0&ort=P&p=2052307&ttp=Page&mnt=wifi&carrier=wifi&t=0&u=15901402&prefl=en-US&cflgs=EA**&ids=MP%253Dgeosoc1&designsystem=6&mrollp=30.02&pcguid=89663faa16d0a4d128c7a974e64bb162&pn=2&rq=e652e96264977a82&ciid=w6zHVrU*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De652e96264977a82%26node_id%3D8be75838deb75da7%26REQUEST_GUID"
            + "%3D171e7c3a-cc60-a6e5-6b56-072cf822ed53%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2At1j"
            + "%257Fa%2528rbpv6710-171e7c3acc7-0x16d%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=71.205.28"
            + ".140&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.110.86.181&TStamp=19:14:52"
            + ".87&TName=roverimp&Agent=eBayiPhone/6.0.0&RemoteIP=10.212.191.198"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26nqt%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26ec%3D5%26uit%3D1569968239000%3C%2Fa%3E&imp=2052307&lv=udid%3D8953ec6916d93c1d43031330013330d2%26ai%3D1462%26mav%3D6.0.0%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wJkYeoCJGGoQ6dj6x9nY%252BseQ**%26memsz%3D3.75%26res%3D828X1792%26mrollp%3D30.02%26designsystem%3D6%26c%3D6%26osv%3D13.4.1%26ids%3DMP%253Dgeosoc1%26mnt%3Dwifi%26mtsgitcommit%3Dd6b703b28b7c71bdb516eedb3d62a9c832dfa163%26prefl%3Den-US%26un%3Dgeosoc1%26tzname%3DAmerica%2FDenver%26reqts%3D1588731292857%26tz%3D-6.00%26mos%3DiOS%26ort%3DP%26carrier%3Dwifi%26uc%3DUS%26dn%3DiPhone12_1%26idfa%3D00000000-0000-0000-0000-000000000000%2C0%26ttp%3DPage%26mtsts%3D2020-05-05T19%3A14%3A24.451");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(6);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    // 17
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("8953ec6916d93c1d43031330013330d2");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2048313);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 19:14:25.102")));
    ubiEvent.setRemoteIP("10.212.191.198");
    ubiEvent.setRequestCorrelationId("e652e96264977a82");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*t1j%7Fa%28rbpv6710-171e7c3acd1-0x15e");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("71.205.28.140");
    ubiEvent.setAgentInfo("eBayiPhone/6.0.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(778845662403L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&user_name=geosoc1&tz=-6.00&lv=1&dn=iPhone12_1&tzname=America%2FDenver&uc=1&nqt=EQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*&mos=iOS&bs=0&uaid=e7c3acd11710a6e56b56aedaf1048de1S0&memsz=3.75&osv=13.4.1&ul=en-US&mtsts=2020-05-05T19%3A14%3A25.102&ec=5&pagename=ViewMessage&app=1462&res=828X1792&uit=1569968239000&c=8&mav=6.0.0&mtsgitcommit=d6b703b28b7c71bdb516eedb3d62a9c832dfa163&g=8953ec6916d93c1d43031330013330d2&idfa=00000000-0000-0000-0000-000000000000%2C0&h=69&reqts=1588731292858&cguidsrc=cookie&n=89663faa16d0a4d128c7a974e64bb162&es=0&ort=P&p=2048313&ttp=Page&mnt=wifi&carrier=wifi&t=0&u=15901402&prefl=en-US&cflgs=EA**&ids=MP%253Dgeosoc1&designsystem=6&mrollp=30.02&pcguid=89663faa16d0a4d128c7a974e64bb162&pn=2&rq=e652e96264977a82&ciid=w6zRVrU*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De652e96264977a82%26node_id%3Da3b78b6b71d47092%26REQUEST_GUID"
            + "%3D171e7c3a-cd10-a6e5-6b56-072cf822ed4f%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2At1j"
            + "%257Fa%2528rbpv6710-171e7c3acd1-0x15e%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2"
            + "&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=71.205.28"
            + ".140&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.110.86.181&TStamp=19:14:52"
            + ".88&TName=roverimp&Agent=eBayiPhone/6.0.0&RemoteIP=10.212.191.198"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay"
            + ".com&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc"
            +
            "%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26nqt%3DEQAAAAAAgAAAAAAABAAIABAAAAAQBAAEAAAIACAAAQAAABAAQAAAAgAAAIAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAEKAABAAg*%26ec%3D5%26uit%3D1569968239000%3C%2Fa%3E&imp=2048313&lv=udid%3D8953ec6916d93c1d43031330013330d2%26ai%3D1462%26mav%3D6.0.0%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wJkYeoCJGGoQ6dj6x9nY%252BseQ**%26memsz%3D3.75%26res%3D828X1792%26mrollp%3D30.02%26designsystem%3D6%26c%3D8%26osv%3D13.4.1%26ids%3DMP%253Dgeosoc1%26mnt%3Dwifi%26mtsgitcommit%3Dd6b703b28b7c71bdb516eedb3d62a9c832dfa163%26prefl%3Den-US%26un%3Dgeosoc1%26tzname%3DAmerica%2FDenver%26reqts%3D1588731292858%26tz%3D-6.00%26mos%3DiOS%26ort%3DP%26carrier%3Dwifi%26uc%3DUS%26dn%3DiPhone12_1%26idfa%3D00000000-0000-0000-0000-000000000000%2C0%26ttp%3DPage%26mtsts%3D2020-05-05T19%3A14%3A25.102");
    ubiEvent.setPageName("roverimp");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(0);
    ubiEvent.setClickId(8);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId("15901402");
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEvent.setAppId(1462);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(4, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetricFor1and2() throws Exception {

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("02d4ad491720ac3cf9e5f5dbbc9421ef");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(true);
    ubiEvent.setPageId(3084);
    ubiEvent
        .setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-11 01:23:11.941")));
    ubiEvent.setRemoteIP("66.249.79.98");
    ubiEvent.setRequestCorrelationId("9e7003bf60cf1edc");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*0112303%29pqtfwpu%29pie%29fgg%7E-fij-17202d4ad4b-0x112");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("66.249.79.98");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(682082610644L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "rdt=1&c=1&cguidurlp=02d4ab7c1720ad39179387dfe66d59ee&rvrrefts"
            + "=02d4ab841720ad391791a340ffb3e913&g=02d4ad491720ac3cf9e5f5dbbc9421ef&h=49&cguidsrc"
            + "=urlparam&n=02d4ab7c1720ad39179387dfe66d59ee&uc=1&url_mpre=https%3A%2F%2Fwww.ebay.co"
            + ".uk%2Fi%2F153095420907&p=3084&uaid=02d4ad4a1720ac3cf9e5f5dbbc9421eeS0&bs=0&t=3&cflgs"
            + "=wA**&ul=en-GB&hrc=301&pn=2&rq=9e7003bf60cf1edc&pagename=EntryTracking&ciid=1K1Kz54*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D9e7003bf60cf1edc%26node_id%3D21c710021fb9f405%26REQUEST_GUID%3D17202d4a-d450-ac3c-f9e4-1ce0dce10ee0%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t%2A0112303%2529pqtfwpu%2529pie%2529fgg%257E-fij-17202d4ad4b-0x112%26cal_mod%3Dfalse&TPool=r1rover&TDuration=12&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=66.249.79.98, 23.209.36.231&Script=/rover/1/710-53481-19255-0/1&Server=rover.ebay.co.uk&TMachine=10.195.207.158&TStamp=01:23:11.94&TName=rover_INTL&Agent=Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)&RemoteIP=66.249.79.98&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/rover/1/710-53481-19255-0/1?rvrrefts=02d4ab841720ad391791a340ffb3e913&cguid=02d4ab7c1720ad39179387dfe66d59ee&toolid=11000&dashenCnt=0&item=153095420907&customid=La+Riche+Directions&catId=26395&campid=5338328342&type=2&mpre=https%3A%2F%2Fwww.ebay.co.uk%2Fi%2F153095420907&dashenId=6665526644281729025&ext=153095420907");
    ubiEvent.setPageName("rover_INTL");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(1);
    ubiEvent.setItemId(null);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(0);
    ubiEvent.setSourceImprId(null);
    ubiEventList.add(ubiEvent);

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(21, sessionAccumulator.getUbiSession().getTrafficSrcId());
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
