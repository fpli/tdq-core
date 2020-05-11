package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.IsValidIPv4;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJParseClientInfo;
import com.ebay.sojourner.ubd.common.util.SojTimestamp;
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
    System.out.println(IsValidIPv4.isValidIP("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5"));

  }

  @Test
  public void test_TrafficSourceMetric1() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("adedd9ac15d76d1dd9e1f3f001b26d8d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.380")));
    ubiEvent.setRemoteIP("10.164.146.154");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0%3C%3F%3E%3A%3D5-171e862b1cd-0x19a");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,GingerClient/2.9.7-RELEASE");
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
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D1c2d989a5f78e81d%26REQUEST_GUID%3D171e862b-1cc0-aadc-2680-1c05eaaa4309%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0%253C%253F%253E%253A%253D5-171e862b1cd-0x19a%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48.43.216&Script=/roverimp/0/2481888/9&Server=internal.rover.vip.ebay.com&TMachine=10.173.194.104&TStamp=22:08:34.38&TName=roverimp&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,GingerClient/2.9.7-RELEASE&RemoteIP=10.164.146.154"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2481888/9?site=0&trknvp=n%3D3a68651d1660abc2dbb5278fe7b14991%26u%3D1496174693%26plmt%3D2wAAAB%252BLCAAAAAAAAAA1jksOwjAMRO8y6yycKsRpbsEadRGSFCoVWvWDQFXujmnFaqTn8bM3jDf4yjjtnFMYO%252FjLhi7BayJHRiEKqkgzncgIs4YVJmGwKadcc8uc2Fx1qF02TJks21BTbKGQF9E1kuHQytpz7XuF8BNoLT4ttfD684dwLfGGJ4Uh7hHmI4Z9NstvG%252BYcpng%252Fr3n6iAmlyJW47IUoBSpN%252BQIKkKbg2wAAAA%253D%253D%26po%3D%5B%28pg%3A2481888+pid%3A100804%29%5D&trknvpsvc=%3Ca%3Enqc%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26nqt%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26es%3D0%26ec%3D4%3C%2Fa%3E&tguid=adedd9ac15d76d1dd9e1f3f001b26d8d&imp=2317508");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.295")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("763519d1c1a77e14");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285044103%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e862b177-0xc6");
    ubiEvent.setEventFamily("CART");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
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
        "TPayload=corr_id_%3Df11926985e66045b%26node_id%3Dd2eec94149f0a26d%26REQUEST_GUID%3D171e862b-1730-a68b-bda5-c166fa3d78c2%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%25285044103%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e862b177-0xc6%26cal_mod%3Dfalse&TPool=r1cartexsvc&TDuration=1&TStatus=0&TType=URL&ContentLength=1654&ForwardedFor=92.122.154.106; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.104.187.218&TStamp=22:08:34.29&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.446")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%285331%3E62%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b20f-0xd4");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSPROV");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521017078370L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&modulectx=prov%3ARECENTLY_VIEWED_ITEMS_PROVIDER%7Cscen%3Anative_recently_viewed_items_1%7Csver%3A%7Cuxcg%3A&dm=HUAWEI&dn=HWPAR&modulekey=mi%3A4236%7Ciid%3A1&uc=56&mos=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9&ul=en-US&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename=ANSWERS_PLATFORM_PAGE&chunkcnt=2&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0.1&ansdomain=HOMEPAGE&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u=1496174693&cflgs=AAE*&chunknum=2&eactn=ANSPROV&rq=kmC8PxpF%2F9y3&moduledata=c%3A5%7Citm%3A324011747498%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC.SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D5%252526rkt%25253D7%252526mehot%25253Dpp%252526sd%25253D333335313556%252526itm%25253D324011747498%252526lsid%25253D0%252526ci%25253D75708%252526itmf%25253D1%252526sid%25253DAQAEAAAAEIj5Lt5whu8xW8zqJoulg1A%2525253D%252526pmt%25253D0%252526noa%25253D1%252526pg%25253D2481888%252526brand%25253DStar%252BWars%2Cc%3A6%7Citm%3A123832811152%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC.SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D6%252526rkt%25253D7%252526mehot%25253Dnone%252526sd%25253D333335313556%252526itm%25253D123832811152%252526lsid%25253D0%252526ci%25253D86207%252526plcampt%25253D0%2525253A11687061018%252526itmf%25253D1%252526sid%25253DAQAEAAAAEBUcbl8vHmQ7v%2525252BY9ryTfhUk%2525253D%252526pmt%25253D1%252526noa%25253D1%252526pg%25253D2481888%2Cc%3A7%7Citm%3A273611584619%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC.SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D7%252526rkt%25253D7%252526mehot%25253Dpp%252526sd%25253D333335313556%252526itm%25253D273611584619%252526lsid%25253D0%252526ci%25253D15687%252526plcampt%25253D0%2525253A10553540014%252526itmf%25253D1%252526sid%25253DAQAEAAAAELUD1mrbetF0XjYdhwRZLzg%2525253D%252526pmt%25253D1%252526noa%25253D1%252526pg%25253D2481888%252526brand%25253DAffliction&ciid=YrIJT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D870b3b7e5ed47b92%26REQUEST_GUID%3D171e862b-20a0-a9e4-f794-ef6bf808423e%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%25285331%253E62%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b20f-0xd4%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=2432&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79.121&TStamp=22:08:34.44&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.285")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("763519d1c1a77e14");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285%3F1522%3B-171e862b16d-0xd8");
    ubiEvent.setEventFamily("MFE");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
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
        "TPayload=corr_id_%3D6895bb90afadf062%26node_id%3D11b082070a5fe676%26REQUEST_GUID%3D171e862b-1690-aa46-5c02-382afbe15e16%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%25285%253F1522%253B-171e862b16d-0xd8%26cal_mod%3Dfalse&TPool=r1reco&TDuration=1&TStatus=0&TType=URL&ContentLength=1438&ForwardedFor=92.122.154.106; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.164.101.192&TStamp=22:08:34.28&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:33.828")));
    ubiEvent.setRemoteIP("10.158.42.70");
    ubiEvent.setRequestCorrelationId("f38380aebcb09fe1");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*517%3E37%28twwgsvv%28umj%28bad%7F%29%60jk-171e862b200-0x15c");
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
        "user_name=jakuvol2&tz=2.0&lv=1&dn=PAR-LX1&ist=0&rlutype=1&tzname=Europe%2FPrague&uc=1&mos=Android&bs=0&uaid=e862b2001710a4d0bb825a4ce1423a0bS0&memsz=2048.0&osv=9&ul=en-US&maup=1&mtsts=2020-05-05T22%3A08%3A33.828&pagename=Install&app=2571&res=1080x2130&c=2&mav=6.0.1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&reqts=1588741714424&cguidsrc=cookie&n=3a68651d1660abc2dbb5278fe7b14991&ort=p&p=2050454&mnt=MOBILE&carrier=T-Mobile+CZ&t=0&u=1496174693&prefl=cs_CZ&cflgs=EA**&ids=MP%253Djakuvol2&designsystem=6&mrollp=43&gadid=ff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0&mppid=117&androidid=2b330951daa57eeb&pcguid=3a68651d1660abc2dbb5278fe7b14991&pn=2&rq=f38380aebcb09fe1&ciid=YrIAC7g*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Df38380aebcb09fe1%26node_id%3Dba5283e9f9bee29c%26REQUEST_GUID%3D171e862b-1ff0-a4d0-bb86-da36ef453ee5%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A517%253E37%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e862b200-0x15c%26cal_mod%3Dfalse&TPool=r1rover&TDuration=28&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48.43.216&Script=/roverimp/1/0/4&Server=localhost&TMachine=10.77.11.184&TStamp=22:08:34.43&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.158.42.70"));
    ubiEvent.setUrlQueryString(
        "/roverimp/1/0/4?res=1080x2130&memsz=2048.0&designsystem=6&imp=2050454&lv=udid%3Dadedd9ac15d76d1dd9e1f3f001b26d8d%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AAmYSgD5SEqA%252Bdj6x9nY%252BseQ**%26memsz%3D2048.0%26res%3D1080x2130%26mrollp%3D43%26designsystem%3D6%26c%3D2%26osv%3D9%26ids%3DMP%253Djakuvol2%26mnt%3DMOBILE%26ist%3D0%26prefl%3Dcs_CZ%26tzname%3DEurope%2FPrague%26user_name%3Djakuvol2%26androidid%3D2b330951daa57eeb%26reqts%3D1588741714424%26tz%3D2.0%26rlutype%3D1%26maup%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DT-Mobile+CZ%26gadid%3Dff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0%26dn%3DPAR-LX1%26dpi%3D409.432x409.903%26mppid%3D117%26mtsts%3D2020-05-05T22%3A08%3A33.828&mnt=MOBILE&prefl=cs_CZ&tzname=Europe%2FPrague&androidid=2b330951daa57eeb&maup=1&ort=p&carrier=T-Mobile+CZ&dpi=409.432x409.903&dn=PAR-LX1&site=0&mrollp=43&ou=nY%2BsHZ2PrBmdj6wVnY%2BsEZ2PrA2dj6AAmYSgD5SEqA%2Bdj6x9nY%2BseQ**&c=2&mav=6.0.1&osv=9&ids=MP%253Djakuvol2&udid=adedd9ac15d76d1dd9e1f3f001b26d8d&ist=0&rvrhostname=rover.ebay.com&user_name=jakuvol2&ai=2571&rvrsite=0&reqts=1588741714424&tz=2.0&rlutype=1&mos=Android&gadid=ff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0&mppid=117");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.241")));
    ubiEvent.setRemoteIP("10.173.210.218");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0217%3B%3C7%29pqtfwpu%29osu%29fgg%7E-fij-171e862b142-0x115");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,GingerClient/2.9.7-RELEASE");
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
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D4323d4bab869fd98%26REQUEST_GUID%3D171e862b-1410-aa64-ae67-a4b5ddb929f8%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0217%253B%253C7%2529pqtfwpu%2529osu%2529fgg%257E-fij-171e862b142-0x115%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48.43.216&Script=/roverimp/0/2481888/9&Server=internal.rover.vip.ebay.com&TMachine=10.166.74.230&TStamp=22:08:34.24&TName=roverimp&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,GingerClient/2.9.7-RELEASE&RemoteIP=10.173.210.218"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2481888/9?site=0&trknvp=n%3D3a68651d1660abc2dbb5278fe7b14991%26u%3D1496174693%26plmt%3D8wAAAB%252BLCAAAAAAAAAAljrFuwzAMRP%252BFswbKUi3BX5EW2QIPMk03BpzIkJIiqeF%252F7zWZDjweH2%252Bj9Zu6xkcbYzS0ztSdNppH6ixzZGtIYDVsA3%252Bwh%252BddMFTgkTgdpknbyY3ieWij1VZ98hK8c6wNGdIbcD00vbE4u96XxVD6B4QQmC1i6QfTQUvN17TMvzoe8zpL%252FdIpyS0XJC7Io8vlQR0byvKSVN%252BSX7uKzhtVTUXOn3ctTzBp3%252FFd0KKBIMB7v%252F8BHMxBJPMAAAA%253D%26po%3D%5B%28pg%3A2481888+pid%3A100801%29%5D&trknvpsvc=%3Ca%3Enqc%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26nqt%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26es%3D0%26ec%3D4%3C%2Fa%3E&tguid=adedd9ac15d76d1dd9e1f3f001b26d8d&imp=2317508");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.504")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%2854%3A%3C5%3E7%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b249-0xd6");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSTRIG");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521020813922L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&dm=HUAWEI&dn=HWPAR&uc=56&modulereq=prov%3AFEATURED_DEALS_PROVIDER%7Cmi%3A2615%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A1%2Cprov%3APOPULAR_IN_INTERESTS_PROVIDER%7Cmi%3A4497%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov%3ARECENTLY_VIEWED_ITEMS_PROVIDER%7Cmi%3A4236%7Cenabled%3A1%7Ccandidate%3A1%7Ciid%3A1%2Cprov%3AWATCHING_PROVIDER%7Cmi%3A3607%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov%3AFULL_BLEED_BANNER_PROVIDER%7Cmi%3A4519%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov%3ALOYALTY_MODULE_PROVIDER%7Cmi%3A4523%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov%3AMULTI_CTA_BANNER_PROVIDER%7Cmi%3A43886%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1%2Cprov%3ANOTIFICATIONS_PROVIDER%7Cmi%3A4926%7Cenabled%3A1%7Ccandidate%3A0%7Cnoshowcode%3ANO_DATA%7Ciid%3A1&mos=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9&ul=en-US&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename=ANSWERS_PLATFORM_PAGE&chunkcnt=1&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0.1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u=1496174693&cflgs=AAE*&moduletrig=mi%3A4236%7Ciid%3A1%7Cscen%3Anative_recently_viewed_items_1%7CpriorAlpha%3A1.0%7CpriorBeta%3A1.0%7Calpha%3A0%7Cbeta%3A0%7Cscore%3A0.9162149121594685%7Cforcedpos%3A2%7Cshown%3A1%7Crank%3A2%2Cmi%3A2615%7Ciid%3A1%7Cscen%3Anative_featured_deals_1%7CpriorAlpha%3A1.0%7CpriorBeta%3A1.0%7Calpha%3A0%7Cbeta%3A0%7Cscore%3A0.653219759768443%7Cforcedpos%3A7%7Cshown%3A1%7Crank%3A7&chunknum=1&eactn=ANSTRIG&rq=kmC8PxpF%2F9y3&ciid=YrJCT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3Df2fd1a4124245abe%26REQUEST_GUID%3D171e862b0b4.abd80a2.17e8.b1229534%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%252854%253A%253C5%253E7%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b249-0xd6%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=1851&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79.121&TStamp=22:08:34.50&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.506")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%285354025%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b24b-0x130");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521032544354L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&modulectx=prov%3AFEATURED_DEALS_PROVIDER%7Cscen%3Anative_featured_deals_1%7Csver%3A%7Cuxcg%3A&dm=HUAWEI&dn=HWPAR&modulekey=mi%3A2615%7Ciid%3A1&uc=56&mos=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9&ul=en-US&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename=ANSWERS_PLATFORM_PAGE&chunkcnt=1&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0.1&ansdomain=HOMEPAGE&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u=1496174693&cflgs=AAE*&chunknum=1&eactn=ANSPROV&rq=kmC8PxpF%2F9y3&moduledata=c%3A1%7Citm%3A264379674226%2Cc%3A2%7Citm%3A223649956355%2Cc%3A3%7Citm%3A272622872854%2Cc%3A4%7Citm%3A143435259741%2Cc%3A5%7Citm%3A223750802816%2Cc%3A6%7Citm%3A254510009388%2Cc%3A7%7Citm%3A183746331896%2Cc%3A8%7Citm%3A163271073369%2Cc%3A9%7Citm%3A264526512109%2Cc%3A10%7Citm%3A372993341934%2Cc%3A11%7Citm%3A124048209325%2Cc%3A12%7Citm%3A392384692424%2Cc%3A13%7Citm%3A151203678229%2Cc%3A14%7Citm%3A302397270994%2Cc%3A15%7Citm%3A303455179376&ciid=YrEtT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D159900a3194a633e%26REQUEST_GUID%3D171e862b-12d0-a9e4-f794-ef6bf8084240%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%252851757%253E1-171e862b145-0x135%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=1363&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79.121&TStamp=22:08:34.24&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.244")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%2851757%3E1-171e862b145-0x135");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSPROV");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
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
        "TPayload=corr_id_%3D74516a9107f63cf0%26node_id%3Dc66a3016573c6641%26REQUEST_GUID%3D171e862b-2470-a9e4-f794-ef6bf808423b%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%25285354025%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b24b-0x130%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=1512&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79.121&TStamp=22:08:34.50&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:32.600")));
    ubiEvent.setRemoteIP("10.153.189.142");
    ubiEvent.setRequestCorrelationId("75df0990fa0c8827");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*517%3E53%28twwgsvv%28umj%28bad%7F%29%60jk-171e862b0ec-0x12c");
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
        "user_name=jakuvol2&tz=2.0&lv=1&dn=PAR-LX1&ist=0&tzname=Europe%2FPrague&uc=1&mos=Android&bs=0&uaid=e862b0ec1710a4d12a20560698764b99S0&memsz=2048.0&osv=9&ul=en-US&mtsts=2020-05-05T22%3A08%3A32.600&pagename=Foreground&app=2571&res=1080x2130&c=1&mav=6.0.1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&reqts=1588741714149&cguidsrc=cookie&n=3a68651d1660abc2dbb5278fe7b14991&ort=p&p=2051248&ttp=Page&mnt=MOBILE&carrier=T-Mobile+CZ&t=0&u=1496174693&prefl=cs_CZ&cflgs=EA**&ids=MP%253Djakuvol2&designsystem=6&mrollp=43&gadid=ff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0&mppid=0&androidid=2b330951daa57eeb&pcguid=3a68651d1660abc2dbb5278fe7b14991&pn=2&rq=75df0990fa0c8827&ciid=YrDsEqI*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D75df0990fa0c8827%26node_id%3D6f9c55062df2226d%26REQUEST_GUID%3D171e862b-0eb0-a4d1-2a20-f62dc76e7006%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A517%253E53%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e862b0ec-0x12c%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48.43.216&Script=/roverimp/0/0/14&Server=rover.ebay.com&TMachine=10.77.18.162&TStamp=22:08:34.15&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.153.189.142"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?imp=2051248&lv=udid%3Dadedd9ac15d76d1dd9e1f3f001b26d8d%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AAmYSgD5SEqA%252Bdj6x9nY%252BseQ**%26memsz%3D2048.0%26res%3D1080x2130%26mrollp%3D43%26designsystem%3D6%26c%3D1%26osv%3D9%26ids%3DMP%253Djakuvol2%26mnt%3DMOBILE%26prefl%3Dcs_CZ%26ist%3D0%26tzname%3DEurope%2FPrague%26user_name%3Djakuvol2%26androidid%3D2b330951daa57eeb%26reqts%3D1588741714149%26tz%3D2.0%26mos%3DAndroid%26ort%3Dp%26carrier%3DT-Mobile+CZ%26gadid%3Dff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0%26dn%3DPAR-LX1%26dpi%3D409.432x409.903%26mppid%3D0%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A08%3A32.600");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.446")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%28533110%3B%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b20f-0xe6");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSPROV");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521017078370L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&modulectx=prov%3ARECENTLY_VIEWED_ITEMS_PROVIDER%7Cscen%3Anative_recently_viewed_items_1%7Csver%3A%7Cuxcg%3A&dm=HUAWEI&dn=HWPAR&modulekey=mi%3A4236%7Ciid%3A1&uc=56&mos=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9&ul=en-US&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename=ANSWERS_PLATFORM_PAGE&chunkcnt=2&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0.1&ansdomain=HOMEPAGE&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u=1496174693&cflgs=AAE*&chunknum=1&eactn=ANSPROV&rq=kmC8PxpF%2F9y3&moduledata=c%3A1%7Citm%3A333335313556%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC.SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D1%252526rkt%25253D7%252526mehot%25253Dpp%252526sd%25253D333335313556%252526itm%25253D333335313556%252526lsid%25253D2%252526ci%25253D177791%252526plcampt%25253D0%2525253A11714325015%252526itmf%25253D1%252526sid%25253DAQAEAAAAECXFDDamd9sYGVArhy9%2525252FQUc%2525253D%252526pmt%25253D1%252526noa%25253D1%252526pg%25253D2481888%2Cc%3A2%7Citm%3A123701322614%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC.SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D2%252526rkt%25253D7%252526mehot%25253Dnone%252526sd%25253D333335313556%252526itm%25253D123701322614%252526lsid%25253D15%252526ci%25253D176997%252526itmf%25253D1%252526sid%25253DAQAEAAAAEHAVzRKP1FzOi1wOwd8gGm0%2525253D%252526pmt%25253D0%252526noa%25253D1%252526pg%25253D2481888%2Cc%3A3%7Citm%3A124004802327%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC.SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D3%252526rkt%25253D7%252526mehot%25253Dnone%252526sd%25253D333335313556%252526itm%25253D124004802327%252526lsid%25253D15%252526ci%25253D20349%252526itmf%25253D1%252526sid%25253DAQAEAAAAEJClEiElN79bR672jYOLeH0%2525253D%252526pmt%25253D0%252526noa%25253D1%252526pg%25253D2481888%252526brand%25253DUnbranded%2Cc%3A4%7Citm%3A254465165203%7CclickTracking%3Aaid%25253D111001%252526algo%25253DREC.SEED%252526ao%25253D1%252526asc%25253D20170504100626%252526meid%25253D66b5478d4d914e79a4a89c48e4e4c0ef%252526pid%25253D100803%252526rk%25253D4%252526rkt%25253D7%252526mehot%25253Dnone%252526sd%25253D333335313556%252526itm%25253D254465165203%252526lsid%25253D0%252526ci%25253D63861%252526plcampt%25253D0%2525253A11547188011%252526itmf%25253D1%252526sid%25253DAQAEAAAAEOeGeSHL27PicQlZLvMUZSY%2525253D%252526pmt%25253D1%252526noa%25253D1%252526pg%25253D2481888%252526brand%25253DUnbranded&ciid=YrIJT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3Df210854fd7742920%26REQUEST_GUID%3D171e862b-20a0-a9e4-f794-ef6bf808423f%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%2528533110%253B%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b20f-0xe6%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=2852&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79.121&TStamp=22:08:34.44&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.414")));
    ubiEvent.setRemoteIP("10.69.226.234");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*063%3E4%3C%28twwgsvv%28umj%28bad%7F%29%60jk-171e862b1ef-0x19d");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,GingerClient/2.9.7-RELEASE");
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
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3Dc5c3749c59c532cb%26REQUEST_GUID%3D171e862b-1ee0-a4b7-9876-9de8e84e25ad%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A063%253E4%253C%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e862b1ef-0x19d%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48.43.216&Script=/roverimp/0/2481888/9&Server=internal.rover.vip.ebay.com&TMachine=10.75.121.135&TStamp=22:08:34.41&TName=roverimp&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,GingerClient/2.9.7-RELEASE&RemoteIP=10.69.226.234"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2481888/9?site=0&trknvp=n%3D3a68651d1660abc2dbb5278fe7b14991%26u%3D1496174693%26plmt%3D9gYAAB%252BLCAAAAAAAAACNlVtT2kAYhv%252FL3jYz3d3skRkvqKBoaRUtVnQcJyQbCCQk5gCCw3%252Fvl4CVUkLLzR6y%252B%252B6z75svvKFkhBqUKaKUslASoMbjGwo81CAYK2xbyIUpionEHDOYE1RYKIU5JMSQM6k85mnCjNQOc5R2mTLMMBcbH1nI5CD3BK2zkd2qw2amWaoiMoJFEWrMijC0UBa%252F95LyeEIs5EPHQkGGGm%252BIwDYipdQErdfWRoZjFWSrqV8rw7gQf8tQG39okGmeTWfG1GoISvkhFKG1%252FJCxVTJdhf5LrQzF%252BAAJtpneYVlOooJ64ogtR0TA6SBJf3sMd4SZuM50lCTooOlhJb97CC7VGoRIwmzKMeGwcScOC9FyAK0NbbPXbDfh1z69P2u1nMjT2eD8rpmOl%252Fpzr%252B%252BewDK2Xc6rCKEjthOyvE1dNGgWz8w%252B8iYcQKa1Ee1hdZp3q5uv1%252BRsdRWQxdXCU6PzCJ%252FsUcAddzCWudaFfwSjygUo7Lp09iAuT8N20A6%252FSz28EZJOBldd0zkKYauFt4pseQSCYi0qCnY4PihXKHPIeBufsJWoTe%252FKnJvbTpfK68DthQ%252Fd%252Bbf%252Bw%252B1gN71d0p16YqyYKuWb2rdMcq0rzANVJbnEah%252FlYsK7OV%252BMC%252FX6U61eLuMiHJHmSd0L9Edtz5fFIsyOmUa3NOKwaUJJLKAw1NY0JSiWdaZ96bvDUM07UU%252FOPw10uvzhj%252FvT%252FzDNHidE58Wq1jRqbzHlQUzMuc0ZxoS9lyYH8DrMbr9FonRo8jN8Pxl448XNQ3c1%252Bmdl4s03xqmcJfDKlw%252Bc%252BTtlFFRc0StqQDHEbsXqZNXIiatnmVcyZ8ZJ3XGvMOkSlNAaFsOVHlESuk6U5CD6HPnm2XXKbpLGUZwbrzTEhKFJL8ruLG7meRoMizyIZzAex%252FnMZFnLyR0YBXlUFmuYwR%252FZU4ns5tXxLhyP10%252FrX3QpBub2BgAA%26po%3D%5B%28pg%3A2481888+pid%3A100803+pladvids%3A%5B0%5D%29%5D&trknvpsvc=%3Ca%3Enqc%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26nqt%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26es%3D0%26ec%3D4%3C%2Fa%3E&tguid=adedd9ac15d76d1dd9e1f3f001b26d8d&imp=2317508");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:37.205")));
    ubiEvent.setRemoteIP("10.156.85.171");
    ubiEvent.setRequestCorrelationId("15af57f8f54a33e3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*0631%3A2%28twwgsvv%28umj%28bad%7F%29%60jk-171e862bc3e-0x12a");
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
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3Dc5c3749c59c532cb%26REQUEST_GUID%3D171e862b-1ee0-a4b7-9876-9de8e84e25ad%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A063%253E4%253C%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e862b1ef-0x19d%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48.43.216&Script=/roverimp/0/2481888/9&Server=internal.rover.vip.ebay.com&TMachine=10.75.121.135&TStamp=22:08:34.41&TName=roverimp&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5,GingerClient/2.9.7-RELEASE&RemoteIP=10.69.226.234"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2481888/9?site=0&trknvp=n%3D3a68651d1660abc2dbb5278fe7b14991%26u%3D1496174693%26plmt%3D9gYAAB%252BLCAAAAAAAAACNlVtT2kAYhv%252FL3jYz3d3skRkvqKBoaRUtVnQcJyQbCCQk5gCCw3%252Fvl4CVUkLLzR6y%252B%252B6z75svvKFkhBqUKaKUslASoMbjGwo81CAYK2xbyIUpionEHDOYE1RYKIU5JMSQM6k85mnCjNQOc5R2mTLMMBcbH1nI5CD3BK2zkd2qw2amWaoiMoJFEWrMijC0UBa%252F95LyeEIs5EPHQkGGGm%252BIwDYipdQErdfWRoZjFWSrqV8rw7gQf8tQG39okGmeTWfG1GoISvkhFKG1%252FJCxVTJdhf5LrQzF%252BAAJtpneYVlOooJ64ogtR0TA6SBJf3sMd4SZuM50lCTooOlhJb97CC7VGoRIwmzKMeGwcScOC9FyAK0NbbPXbDfh1z69P2u1nMjT2eD8rpmOl%252Fpzr%252B%252BewDK2Xc6rCKEjthOyvE1dNGgWz8w%252B8iYcQKa1Ee1hdZp3q5uv1%252BRsdRWQxdXCU6PzCJ%252FsUcAddzCWudaFfwSjygUo7Lp09iAuT8N20A6%252FSz28EZJOBldd0zkKYauFt4pseQSCYi0qCnY4PihXKHPIeBufsJWoTe%252FKnJvbTpfK68DthQ%252Fd%252Bbf%252Bw%252B1gN71d0p16YqyYKuWb2rdMcq0rzANVJbnEah%252FlYsK7OV%252BMC%252FX6U61eLuMiHJHmSd0L9Edtz5fFIsyOmUa3NOKwaUJJLKAw1NY0JSiWdaZ96bvDUM07UU%252FOPw10uvzhj%252FvT%252FzDNHidE58Wq1jRqbzHlQUzMuc0ZxoS9lyYH8DrMbr9FonRo8jN8Pxl448XNQ3c1%252Bmdl4s03xqmcJfDKlw%252Bc%252BTtlFFRc0StqQDHEbsXqZNXIiatnmVcyZ8ZJ3XGvMOkSlNAaFsOVHlESuk6U5CD6HPnm2XXKbpLGUZwbrzTEhKFJL8ruLG7meRoMizyIZzAex%252FnMZFnLyR0YBXlUFmuYwR%252FZU4ns5tXxLhyP10%252FrX3QpBub2BgAA%26po%3D%5B%28pg%3A2481888+pid%3A100803+pladvids%3A%5B0%5D%29%5D&trknvpsvc=%3Ca%3Enqc%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26nqt%3DAQAAAAAAgAAAAAEABABAABAAAEAiAAAAAAAAACAAAQAAAgABAAAAIAAAACAACQAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAoAAAAAACAgAI*%26es%3D0%26ec%3D4%3C%2Fa%3E&tguid=adedd9ac15d76d1dd9e1f3f001b26d8d&imp=2317508");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.503")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%28533110%3B%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b247-0xcb");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSTMPL");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521020682850L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&dm=HUAWEI&dn=HWPAR&locale=en-US-x-lvariant-MAIN&pagetmpl=HomePage&uc=56&modulergn=rgn%3ARIVER%7Crank%3A1%7Cmi%3A4236%7Ciid%3A1%7Cuxcg%3AITEMS_CAROUSEL_GROUP%7Cscen%3Anative_recently_viewed_items_1%7Cisfixed%3Atrue%2Crgn%3ARIVER%7Crank%3A2%7Cmi%3A2615%7Ciid%3A1%7Cuxcg%3AITEMS_CAROUSEL_GROUP%7Cscen%3Anative_featured_deals_1%7Cisfixed%3Atrue&mos=Android&bs=197&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9&ul=en-US&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename=ANSWERS_PLATFORM_PAGE&chunkcnt=1&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0.1&ansdomain=HOMEPAGE&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&anschannel=NATIVE&t=0&u=1496174693&cflgs=AAE*&chunknum=1&eactn=ANSTMPL&rq=kmC8PxpF%2F9y3&ciid=YrJAT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D265572e2f0a5f479%26REQUEST_GUID%3D171e862b-2400-a9e4-f794-ef6bf808423c%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%2528533110%253B%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b247-0xcb%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=1246&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79.121&TStamp=22:08:34.50&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.886")));
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
        "app=2571&c=1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&nid=&h=ac&cguidsrc=cookie&n=3a68651d1660abc2dbb5278fe7b14991&uc=1&p=3084&uaid=e862b3c71710a6e55920a9f1f0e89f39S0&bs=0&rvrid=2410890507510&t=0&cflgs=EA**&ul=en-US&mppid=117&pn=2&pcguid=3a68651d1660abc2dbb5278fe7b14991&rq=af383200bc425749&pagename=EntryTracking&ciid=YrPHVZI*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Daf383200bc425749%26node_id%3Db975e50910b5b972%26REQUEST_GUID%3D171e862b-3c60-a6e5-5924-af0bf8141767%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A5ft3%257B%2528rbpv6710-171e862b3c7-0x14b%26cal_mod%3Dfalse&TPool=r1rover&TDuration=16&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=37.48.43.216&Script=/rover/1/0/4&Server=localhost&TMachine=10.110.85.146&TStamp=22:08:34.88&TName=rover&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.212.176.56"));
    ubiEvent.setUrlQueryString(
        "/rover/1/0/4?memsz=2048.0&res=1080x2130&designsystem=6&mnt=MOBILE&prefl=cs_CZ&tzname=Europe%2FPrague&androidid=2b330951daa57eeb&ort=p&carrier=T-Mobile+CZ&dpi=409.432x409.903&dn=PAR-LX1&mtsts=2020-05-05T22%3A08%3A33.748&ctr=0&nrd=1&site=0&mrollp=43&ou=nY%2BsHZ2PrBmdj6wVnY%2BsEZ2PrA2dj6AAmYSgD5SEqA%2Bdj6x9nY%2BseQ**&c=1&mav=6.0.1&osv=9&ids=MP%253Djakuvol2&ist=0&udid=adedd9ac15d76d1dd9e1f3f001b26d8d&rvrhostname=rover.ebay.com&user_name=jakuvol2&ai=2571&rvrsite=0&tz=2.0&reqts=1588741714879&rlutype=1&mos=Android&gadid=ff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0&mppid=117");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:36.541")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("855a5aa0184811c1");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%2853311%3F2%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862bdba-0x1e4");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("VIEWDTLS");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(418519563618L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&tz=2.0&dm=HUAWEI&dn=HWPAR&moduledtl=mi%3A4236%7Ciid%3A1%7Cdur%3A1%2Cmi%3A2615%7Ciid%3A1%7Cdur%3A1&mos=Android&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&mtsts=2020-05-06T05%3A08%3A36.541Z&pagename=PulsarGateway&app=2571&res=0x0&parentrq=kmC8PxpF%252F9y3&efam=HOMEPAGE&mav=6.0.1&c=2&ou=1496174693&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&ort=p&p=2356359&t=0&u=1496174693&cflgs=AA**&ids=MP%3Djakuvol2&designsystem=6&mrollp=43&gadid=ff41b8f3-a816-4d74-87b2-963e3fc137e9%2C0&eactn=VIEWDTLS&androidid=2b330951daa57eeb&rq=855a5aa0184811c1&ciid=Yr21cWE*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D855a5aa0184811c1%26node_id%3D967874623fb29de2%26REQUEST_GUID%3D171e862bda2.ad4a413.337cd.bd33f959%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%252853311%253F2%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862bdba-0x1e4%26cal_mod%3Dfalse&TPool=r1pulsgwy&TDuration=2&TStatus=0&TType=URL&ContentLength=1131&ForwardedFor=92.122.154.106; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.173.113.97&TStamp=22:08:37.43&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:08:34.503")));
    ubiEvent.setRemoteIP("37.48.43.216");
    ubiEvent.setRequestCorrelationId("kmC8PxpF%2F9y3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%285226165%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e862b247-0xe7");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("ANSLAYT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("37.48.43.216");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(521020551778L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setAppId(2571);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&laytcols=rgn%3ARIVER%7Clayt%3ALIST_1_COLUMN%7Cnumcol%3A1&dm=HUAWEI&dn=HWPAR&uc=56&mos=Android&bs=197&modulelayt=layt%3ALIST_1_COLUMN%7Cmi%3A2615%7Ciid%3A1%7Crow%3A2%7Ccol%3A1%7Csize%3AROW%7ClazyLoadId%3Anull%2Clayt%3ALIST_1_COLUMN%7Cmi%3A4236%7Ciid%3A1%7Crow%3A1%7Ccol%3A1%7Csize%3AROW%7ClazyLoadId%3Anull&pageci=a2a23ecf-8f57-11ea-a1d3-74dbd1808f0a&osv=9&ul=en-US&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&pagename=ANSWERS_PLATFORM_PAGE&chunkcnt=1&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0.1&g=adedd9ac15d76d1dd9e1f3f001b26d8d&h=ac&nativeApp=true&cp=2481888&an=eBayAndroid&n=3a68651d1660abc2dbb5278fe7b14991&p=2370942&t=0&u=1496174693&cflgs=AAE*&chunknum=1&eactn=ANSLAYT&rq=kmC8PxpF%2F9y3&ciid=YrI%2BT3k*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3DkmC8PxpF%2F9y3%26node_id%3D67a3f5ea27d19019%26REQUEST_GUID%3D171e862b-23f0-a9e4-f794-ef6bf808423d%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%25285226165%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e862b247-0xe7%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=1098&ForwardedFor=92.122.154.93; 2.23.97.28;37.48.43.216&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.158.79.121&TStamp=22:08:34.50&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWPAR;T-Mobile CZ;1080x2130;2.5&RemoteIP=37.48.43.216&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,TEXT_BANNER,FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:56:52.230")));
    ubiEvent.setRemoteIP("90.186.49.84");
    ubiEvent.setRequestCorrelationId("deeaab0130c48395");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0%3D0%3F326-171e88ee995-0x109");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("90.186.49.84");
    ubiEvent.setAgentInfo("Mozilla/5.0 (iPad; CPU OS 12_4_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604.1");
    ubiEvent.setCobrand(9);
    ubiEvent.setCurrentImprId(245846305166L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.de");
    ubiEvent.setApplicationPayload(
        "c=1&g=e88ee98d1710acc3d3911a0ac0f5d4b2&rurl=https%25253A%25252F%25252Fwww.ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&h=8d&px=4249&chnl=9&uc=77&p=1605052&uaid=e88ee98e1710acc3d3911a0ac0f5d4b1S0&bs=77&catid=80&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen.de%2F&r=715462550&t=77®=Hamburg&cflgs=QA**&ul=de-DE&pn=2&rq=deeaab0130c48395&pagename=http://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=jumUPTk*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Ddeeaab0130c48395%26node_id%3D4b542f8f20dcfdc8%26REQUEST_GUID%3D171e88ee-9860-acc3-d392-fdf6de68a87c%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0%253D0%253F326-171e88ee995-0x109%26cal_mod%3Dfalse&TPool=r1rover&TDuration=16&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=90.186.49.84&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.204.61.57&TStamp=22:56:52.23&TName=roverimp_INTL&Agent=Mozilla/5.0 (iPad; CPU OS 12_4_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604.1&RemoteIP=90.186.49.84&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=reg%3DHamburg%26catid%3D80%26rurl%3Dhttps%25253A%25252F%25252Fwww.ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&mpt=1588744612147&imp=1605052");
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
    Assert.assertEquals(1, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric3() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("62a512c516e86d87e5e2bbe00111d41d");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2051248);
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:58.533")));
    ubiEvent.setRemoteIP("10.155.214.148");
    ubiEvent.setRequestCorrelationId("08d3875a397558d3");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*517%3E15%28twwgsvv%28umj%28bad%7F%29%60jk-171e7fd74ff-0x11b");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(670333629693L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=depechegirl_88&tz=-5.0&lv=1&dn=SM-G930V&ist=0&rlutype=1&tzname=America%2FChicago&uc=1&mlocerr=1&mos=Android&bs=0&uaid=e7fd74ff1710a4d129c707299f5ced84S0&mlocset=3&memsz=1024.0&osv=8.0.0&ul=en-US&mtsts=2020-05-05T20%3A17%3A58.533&apn=3&pagename=Foreground&app=2571&res=1080x1920&c=1&mav=6.0.1&g=62a512c516e86d87e5e2bbe00111d41d&h=c5&reqts=1588735079654&cguidsrc=cookie&n=62a51dc616e0a9e470c0b7eff117aa97&ort=p&p=2051248&ttp=Page&mnt=WIFI&carrier=Verizon&t=0&u=799526959&prefl=en_US&cflgs=EA**&ids=MP%253Ddepechegirl_88&designsystem=6&mrollp=5&gadid=987035ad-5ab0-4ca1-9d37-729c83a08cc8%2C1&mppid=119&androidid=fd20dca20e522752&pcguid=62a51dc616e0a9e470c0b7eff117aa97&pn=2&rq=08d3875a397558d3&ciid=%2FXT%2FEpw*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D08d3875a397558d3%26node_id%3Df828fb8c0d13a616%26REQUEST_GUID%3D171e7fd7-4fe0-a4d1-29c7-ba4dcb2a22b1%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A517%253E15%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e7fd74ff-0x11b%26cal_mod%3Dtrue&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=173.173.220.37&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18.156&TStamp=20:17:59.67&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.155.214.148"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay.com&site=0&ai=2571&rvrsite=0&rlutype=1&imp=2051248&lv=udid%3D62a512c516e86d87e5e2bbe00111d41d%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6wDmYukCpaLpAWdj6x9nY%252BseQ**%26res%3D1080x1920%26memsz%3D1024.0%26designsystem%3D6%26mnt%3DWIFI%26prefl%3Den_US%26tzname%3DAmerica%2FChicago%26androidid%3Dfd20dca20e522752%26ort%3Dp%26carrier%3DVerizon%26dpi%3D435.42825x431.57477%26dn%3DSM-G930V%26apn%3D3%26mrollp%3D5%26c%3D1%26ids%3DMP%253Ddepechegirl_88%26osv%3D8.0.0%26ist%3D0%26mlocerr%3D1%26user_name%3Ddepechegirl_88%26reqts%3D1588735079654%26tz%3D-5.0%26rlutype%3D1%26mos%3DAndroid%26mlocset%3D3%26gadid%3D987035ad-5ab0-4ca1-9d37-729c83a08cc8%2C1%26mppid%3D119%26ttp%3DPage%26mtsts%3D2020-05-05T20%3A17%3A58.533&udid=62a512c516e86d87e5e2bbe00111d41d&mppid=119");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:59.383")));
    ubiEvent.setRemoteIP("173.173.220.37");
    ubiEvent.setRequestCorrelationId("bcNiPWw0CM%2BP");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%2853%3A321%3B%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e7fd73d8-0x12b");
    ubiEvent.setEventFamily("MYBWL");
    ubiEvent.setEventAction("ACTN");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(543647298045L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&flgs=AA**&dm=samsung&dn=heroqltevzw&uc=1&nqt=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&mos=Android&bs=0&osv=8.0.0&ul=en-US&callingpagename=txexpsvc__experience_myebay_buying_v1_watching_activity_GET&ec=4&pagename=Watch_DomSvc_GetWatchList&app=2571&res=0x0&uit=1588735078877&efam=MYBWL&mav=6.0.1&g=62a512c516e86d87e5e2bbe00111d41d&h=c5&nativeApp=true&cp=2510300&an=eBayAndroid&n=62a51dc616e0a9e470c0b7eff117aa97&es=0&p=3112193&signals=%7B%22signals%22%3A%5B%7B%22iid%22%3A%22163636275006%22%7D%2C%7B%22iid%22%3A%22352159743760%22%2C%22vid%22%3A%22621597197669%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22123866428185%22%7D%2C%7B%22iid%22%3A%22143204758712%22%7D%2C%7B%22iid%22%3A%22352180312213%22%2C%22vid%22%3A%22621613495298%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22273690588524%22%7D%2C%7B%22iid%22%3A%22132460397482%22%7D%2C%7B%22iid%22%3A%22184025924131%22%7D%2C%7B%22iid%22%3A%22323638738780%22%7D%2C%7B%22iid%22%3A%22163808278271%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22223092986530%22%7D%2C%7B%22iid%22%3A%22382456734896%22%7D%2C%7B%22iid%22%3A%22331702594218%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22231745637587%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22252980415642%22%7D%2C%7B%22iid%22%3A%22401504095582%22%2C%22exc%22%3A%5B4%5D%7D%2C%7B%22iid%22%3A%22133196652307%22%7D%2C%7B%22iid%22%3A%22382585044859%22%2C%22vid%22%3A%22651348972807%22%7D%2C%7B%22iid%22%3A%22382222703064%22%2C%22vid%22%3A%22651054700683%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22184120749661%22%2C%22vid%22%3A%22691831573216%22%7D%2C%7B%22iid%22%3A%22223972459147%22%2C%22vid%22%3A%22522726683975%22%7D%2C%7B%22iid%22%3A%22193200995572%22%7D%2C%7B%22iid%22%3A%22401704527234%22%7D%2C%7B%22iid%22%3A%22153718453223%22%7D%2C%7B%22iid%22%3A%22223510165224%22%7D%2C%7B%22iid%22%3A%22233554254042%22%2C%22vid%22%3A%22533213749912%22%7D%2C%7B%22iid%22%3A%22264393722808%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22164026527043%22%7D%2C%7B%22iid%22%3A%22133048395264%22%2C%22vid%22%3A%22432347397088%22%7D%2C%7B%22iid%22%3A%22382946710862%22%2C%22vid%22%3A%22651605795211%22%7D%2C%7B%22iid%22%3A%22143210403820%22%7D%2C%7B%22iid%22%3A%22123732099379%22%7D%2C%7B%22iid%22%3A%22123732181109%22%2C%22vid%22%3A%22424331640434%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22281410542932%22%7D%2C%7B%22iid%22%3A%22123936871948%22%7D%2C%7B%22iid%22%3A%22254451077996%22%7D%2C%7B%22iid%22%3A%22113716301889%22%2C%22vid%22%3A%22413849149501%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22333512884169%22%7D%2C%7B%22iid%22%3A%22301527392720%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22132916924372%22%7D%2C%7B%22iid%22%3A%22382899100288%22%7D%2C%7B%22iid%22%3A%22273211517195%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22132743370892%22%7D%2C%7B%22iid%22%3A%22153566492091%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22362487078165%22%2C%22vid%22%3A%22631989423938%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22222154118402%22%2C%22vid%22%3A%22520996522530%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22312657912395%22%7D%2C%7B%22iid%22%3A%22381330658858%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22381296362419%22%2C%22inc%22%3A%5B1%5D%2C%22exc%22%3A%5B4%5D%7D%2C%7B%22iid%22%3A%22322831136257%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22183921564667%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22112409340736%22%2C%22vid%22%3A%22412808379887%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22253316061669%22%7D%2C%7B%22iid%22%3A%22324105421303%22%7D%2C%7B%22iid%22%3A%22362884133894%22%7D%2C%7B%22iid%22%3A%22353007936282%22%7D%2C%7B%22iid%22%3A%22283108218747%22%7D%2C%7B%22iid%22%3A%22122709235100%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22283419794526%22%7D%2C%7B%22iid%22%3A%22283419796093%22%7D%2C%7B%22iid%22%3A%22253316437509%22%7D%2C%7B%22iid%22%3A%22132541221419%22%7D%2C%7B%22iid%22%3A%22231786839153%22%7D%2C%7B%22iid%22%3A%22193162851810%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22151330621851%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22324045585804%22%2C%22vid%22%3A%22513091474480%22%7D%2C%7B%22iid%22%3A%22254484661171%22%7D%2C%7B%22iid%22%3A%22253499298397%22%7D%2C%7B%22iid%22%3A%22281443059937%22%2C%22vid%22%3A%22581475712324%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22122138733305%22%2C%22vid%22%3A%22422127456015%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22301020559678%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22254204183689%22%7D%2C%7B%22iid%22%3A%22223671362548%22%7D%2C%7B%22iid%22%3A%22352465414925%22%2C%22vid%22%3A%22621799985078%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22113384515498%22%7D%2C%7B%22iid%22%3A%22291823009709%22%7D%2C%7B%22iid%22%3A%22254427359907%22%2C%22vid%22%3A%22554052198664%22%7D%2C%7B%22iid%22%3A%22303453638903%22%7D%2C%7B%22iid%22%3A%22113657505559%22%7D%2C%7B%22iid%22%3A%22142356646522%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22351765571757%22%2C%22vid%22%3A%22620692136509%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22323813387765%22%7D%2C%7B%22iid%22%3A%22222448837630%22%7D%2C%7B%22iid%22%3A%22152955699061%22%7D%2C%7B%22iid%22%3A%22401175833577%22%2C%22vid%22%3A%22670737051039%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22263163558527%22%2C%22exc%22%3A%5B4%5D%7D%2C%7B%22iid%22%3A%22362329132601%22%2C%22inc%22%3A%5B1%5D%2C%22exc%22%3A%5B3%2C4%2C6%5D%7D%2C%7B%22iid%22%3A%22132633972120%22%2C%22inc%22%3A%5B1%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22323925970321%22%7D%2C%7B%22iid%22%3A%22352468319119%22%2C%22vid%22%3A%22621802273449%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22382234573817%22%2C%22vid%22%3A%22651064024229%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22192897381160%22%7D%2C%7B%22iid%22%3A%22402117733012%22%7D%2C%7B%22iid%22%3A%22163704907752%22%7D%2C%7B%22iid%22%3A%22351009650707%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22223926852432%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22221950837365%22%7D%2C%7B%22iid%22%3A%22264546648878%22%7D%2C%7B%22iid%22%3A%22382863071336%22%2C%22vid%22%3A%22651551345209%22%7D%2C%7B%22iid%22%3A%22162445009332%22%7D%2C%7B%22iid%22%3A%22223495038582%22%7D%2C%7B%22iid%22%3A%22303136842751%22%7D%2C%7B%22iid%22%3A%22324116711053%22%7D%2C%7B%22iid%22%3A%22153878271793%22%2C%22vid%22%3A%22453926865374%22%7D%2C%7B%22iid%22%3A%22191052041868%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22303010391071%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22123394142063%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22254247024814%22%7D%2C%7B%22iid%22%3A%22311666044361%22%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22122674179876%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22273677635586%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22153321821369%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22372699769689%22%7D%2C%7B%22iid%22%3A%22292504010567%22%7D%2C%7B%22iid%22%3A%22113733192060%22%7D%2C%7B%22iid%22%3A%22301169880753%22%7D%2C%7B%22iid%22%3A%22333137355336%22%7D%2C%7B%22iid%22%3A%22142492390828%22%7D%2C%7B%22iid%22%3A%22193280354493%22%7D%2C%7B%22iid%22%3A%22323528027208%22%7D%2C%7B%22iid%22%3A%22361459844076%22%2C%22vid%22%3A%22630822679084%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22283800877540%22%7D%2C%7B%22iid%22%3A%22323716633368%22%7D%2C%7B%22iid%22%3A%22112392013917%22%2C%22vid%22%3A%22412795795789%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22183978773005%22%7D%2C%7B%22iid%22%3A%22173859398355%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22164014591715%22%7D%2C%7B%22iid%22%3A%22381481076191%22%2C%22vid%22%3A%22650614310307%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22301813509319%22%2C%22vid%22%3A%22600625223308%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22151292747029%22%2C%22vid%22%3A%22450406303421%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22283190010914%22%7D%2C%7B%22iid%22%3A%22222740443555%22%2C%22vid%22%3A%22521597165609%22%2C%22inc%22%3A%5B3%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22254185676927%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22283801364716%22%7D%2C%7B%22iid%22%3A%22352420889097%22%2C%22vid%22%3A%22621770358126%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22323048346276%22%2C%22vid%22%3A%22512252791263%22%7D%2C%7B%22iid%22%3A%22391131861176%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22264586289840%22%7D%2C%7B%22iid%22%3A%22223469151831%22%7D%2C%7B%22iid%22%3A%22391974324014%22%2C%22inc%22%3A%5B1%5D%2C%22exc%22%3A%5B2%2C4%2C6%5D%7D%2C%7B%22iid%22%3A%22281180348699%22%2C%22vid%22%3A%22580232487527%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22311952578732%22%2C%22vid%22%3A%22610748586544%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22323532538164%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22323936852787%22%7D%2C%7B%22iid%22%3A%22352634286498%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22263530441092%22%7D%2C%7B%22iid%22%3A%22283632807817%22%2C%22vid%22%3A%22584944268056%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22313017951156%22%2C%22vid%22%3A%22611709839778%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22111638342390%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22351570046827%22%2C%22vid%22%3A%22620587377535%22%2C%22inc%22%3A%5B1%5D%7D%2C%7B%22iid%22%3A%22264453732275%22%2C%22inc%22%3A%5B2%5D%2C%22exc%22%3A%5B4%2C6%5D%7D%2C%7B%22iid%22%3A%22322713666479%22%7D%2C%7B%22iid%22%3A%22222603188726%22%7D%2C%7B%22iid%22%3A%22202426841734%22%7D%2C%7B%22iid%22%3A%22183762707386%22%2C%22exc%22%3A%5B6%5D%7D%2C%7B%22iid%22%3A%22293409980552%22%7D%2C%7B%22iid%22%3A%22372865917459%22%7D%2C%7B%22iid%22%3A%22383312732688%22%2C%22vid%22%3A%22651796746556%22%7D%2C%7B%22iid%22%3A%22153813825863%22%7D%2C%7B%22iid%22%3A%22323522802163%22%7D%2C%7B%22iid%22%3A%22233499243772%22%7D%2C%7B%22iid%22%3A%22402232518631%22%7D%2C%7B%22iid%22%3A%22223730614285%22%7D%2C%7B%22iid%22%3A%22173987616433%22%7D%2C%7B%22iid%22%3A%22133232061694%22%7D%2C%7B%22iid%22%3A%22312961098550%22%7D%2C%7B%22iid%22%3A%22333518936294%22%2C%22vid%22%3A%22542617864096%22%7D%2C%7B%22iid%22%3A%22133363056017%22%7D%2C%7B%22iid%22%3A%22402214187198%22%7D%2C%7B%22iid%22%3A%22233449898568%22%7D%2C%7B%22iid%22%3A%22383493034753%22%7D%2C%7B%22iid%22%3A%22333306749480%22%2C%22vid%22%3A%22542321164746%22%7D%2C%7B%22iid%22%3A%22333306749332%22%2C%22vid%22%3A%22542321164501%22%7D%2C%7B%22iid%22%3A%22333306769522%22%2C%22vid%22%3A%22542321170274%22%7D%2C%7B%22iid%22%3A%22333306769474%22%2C%22vid%22%3A%22542321170223%22%7D%5D%7D&t=0&u=799526959&cflgs=AA**&eactn=ACTN&rq=bcNiPWw0CM%2BP&ciid=%2FXHnk34*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D50d64663788c9519%26node_id%3D642ab31ea3eb7c18%26REQUEST_GUID%3D171e7fd7-3ca0-aad9-37e4-5177f8ff313f%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%252853%253A321%253B%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e7fd73d8-0x12b%26cal_mod%3Dfalse&TPool=r1watch&TDuration=2&TStatus=0&TType=URL&ContentLength=8034&ForwardedFor=69.192.7.158;173.173.220.37&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.173.147.126&TStamp=20:17:59.38&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0&RemoteIP=173.173.220.37&Encoding=gzip&Referer=null"));
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:59.503")));
    ubiEvent.setRemoteIP("173.173.220.37");
    ubiEvent.setRequestCorrelationId("bcNiPWw0CM%2BP");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%2850%3B0766%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e7fd7450-0x1b1b");
    ubiEvent.setEventFamily("MYBWL");
    ubiEvent.setEventAction("ACTN");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(166125662717L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&flgs=AA**&dm=samsung&dn=heroqltevzw&uc=1&nqt=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&mos=Android&bs=0&osv=8.0.0&ul=en-US&callingpagename=txexpsvc__experience_myebay_buying_v1_watching_activity_GET&ec=4&pagename=txexpsvc__experience_myebay_buying_v1_watching_activity_GET&app=2571&res=0x0&uit=1588735078877&efam=MYBWL&mav=6.0.1&g=62a512c516e86d87e5e2bbe00111d41d&h=c5&nativeApp=true&cp=2510300&an=eBayAndroid&n=62a51dc616e0a9e470c0b7eff117aa97&es=0&p=2510300&t=0&u=799526959&cflgs=AA**&guest=false&eactn=ACTN&rq=bcNiPWw0CM%2BP&ciid=%2FXHcrSY*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dcc88986190e14577%26node_id%3D7a9c66d151b7dc26%26REQUEST_GUID%3D171e7fd7-4460-a9ca-d264-1df7fcf1afe7%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%252850%253B0766%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e7fd7450-0x1b1b%26cal_mod%3Dfalse&TPool=r1txexpsvc&TDuration=1&TStatus=0&TType=URL&ContentLength=1447&ForwardedFor=69.192.7.158;173.173.220.37&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.156.173.38&TStamp=20:17:59.50&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0&RemoteIP=173.173.220.37&Encoding=gzip&Referer=null"));
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:59.734")));
    ubiEvent.setRemoteIP("173.173.220.37");
    ubiEvent.setRequestCorrelationId("619c2626bfc5b2c6");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285000735%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e7fd7537-0x1d8");
    ubiEvent.setEventFamily("MFE");
    ubiEvent.setEventAction("EXPM");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(347827959293L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&flgs=AA**&dm=samsung&dn=heroqltevzw&uc=1&nqt=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&mos=Android&bs=0&osv=8.0.0&plmt=xAAAAB%252BLCAAAAAAAAAA1jssKgzAQRf9l1ilMkknC5FeKixC1CFqlLwriv%252FeqdHXgwJy5Ky03yk7Ua2JDy0D5utLQUrbMmsRQhbqooQdIoZfOhsi%252BVRVhKSmGGDn2vk9SfCBD3QsJ54JlZ3Y4aSDL2UXj%252Fh5HQ2WvqSqzxU35%252FP0Ejx3T98BcD5TniRmrNtQqXjgAK3lrth%252BDtxwqxAAAAA%253D%253D&ul=en-US&callingpagename=cartexsvc__DefaultPage&!xt=225102%2C225124&ec=4&pagename=reco__experience_merchandising_v1_module_provider_GET&app=2571&res=0x0&uit=1588735079646&efam=MFE&mav=6.0.1&g=62a512c516e86d87e5e2bbe00111d41d&rpg=2493970&h=c5&nativeApp=true&cp=2493970&an=eBayAndroid&n=62a51dc616e0a9e470c0b7eff117aa97&es=0&p=2530290&t=0&u=799526959&cflgs=AA**&eactn=EXPM&rq=619c2626bfc5b2c6&po=%5B%28pg%3A2493970+pid%3A100974%29%5D&ciid=%2FXUp%2FFA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Daa7060acd16fc820%26node_id%3D7b7349a6df2564d5%26REQUEST_GUID%3D171e7fd7-5320-aa6f-c506-0cc4fbcc5498%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%25285000735%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e7fd7537-0x1d8%26cal_mod%3Dfalse&TPool=r1reco&TDuration=2&TStatus=0&TType=URL&ContentLength=1468&ForwardedFor=23.52.0.101; 69.192.7.158;173.173.220.37&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.166.252.80&TStamp=20:17:59.73&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0&RemoteIP=173.173.220.37&Encoding=gzip&Referer=null"));
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 20:17:59.756")));
    ubiEvent.setRemoteIP("173.173.220.37");
    ubiEvent.setRequestCorrelationId("619c2626bfc5b2c6");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285%3F45164-171e7fd754d-0x214");
    ubiEvent.setEventFamily("CART");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("173.173.220.37");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(775994373373L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AEQ*");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&flgs=AEQ*&cartic=0&dm=samsung&dn=heroqltevzw&mcc=false&uic=0&uc=1&!xe=23243&tic=0&nqt=UAAAAAAAgAAAAAIABACIAAAAASAABAAABAAAAEAAAQAAAQAAQAAAIAAAACAAJQAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAKCEAAAAAAAAgAAI*&mos=Android&bs=0&osv=8.0.0&nmq=0&ul=en-US&callingpagename=cartexsvc__DefaultPage&!xt=59961&ec=4&pagename=cartexsvc__DefaultPage&app=2571&res=0x0&uit=1588735079646&efam=CART&mav=6.0.1&crtsz=0&ebc=5040102108&g=62a512c516e86d87e5e2bbe00111d41d&rpg=2493970&h=c5&num_itm_unavbl=0&prof=ANDROID&nativeApp=true&cp=2493970&an=eBayAndroid&n=62a51dc616e0a9e470c0b7eff117aa97&es=0&p=2493970&t=0&u=799526959&cflgs=AA**&cmd=DefaultPage&xt=59961&issmeoic=false&eactn=EXPC&bic=0&rq=619c2626bfc5b2c6&ciid=%2FXTerLQ*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D20385fb681f27818%26node_id%3Dabad130d4606960c%26REQUEST_GUID%3D171e7fd7-5420-a12a-cb42-395efe7ca11f%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%25285%253F45164-171e7fd754d-0x214%26cal_mod%3Dfalse&TPool=r1cartexsvc&TDuration=2&TStatus=0&TType=URL&ContentLength=1666&ForwardedFor=23.52.0.101; 69.192.7.158;173.173.220.37&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.18.172.180&TStamp=20:17:59.75&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;8.0.0;samsung;heroqltevzw;Verizon;1080x1920;3.0&RemoteIP=173.173.220.37&Encoding=gzip&Referer=null"));
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
    Assert.assertEquals(1, sessionAccumulator.getUbiSession().getTrafficSrcId());
  }

  @Test
  public void test_TrafficSourceMetric4() throws Exception {

    // 1
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("ad402a4c168acc2d4dd1bb50015645bd");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2349624);
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:36:08.771")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("f8b90dd54c8e8c9d");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%28530262%3A%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e87bf044-0xd4");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(984733511291L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYKEAAAAAAAIAcQEEABAAgIATgAAAAAIwAAABAAAiAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "c=1&g=e88ee98d1710acc3d3911a0ac0f5d4b2&rurl=https%25253A%25252F%25252Fwww.ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&h=8d&px=4249&chnl=9&uc=77&p=1605052&uaid=e88ee98e1710acc3d3911a0ac0f5d4b1S0&bs=77&catid=80&ref=https%3A%2F%2Fwww.ebay-kleinanzeigen.de%2F&r=715462550&t=77®=Hamburg&cflgs=QA**&ul=de-DE&pn=2&rq=deeaab0130c48395&pagename=http://kleinanzeigen.ebay.de/anzeigen/s-suchen.html&ciid=jumUPTk*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Ddeeaab0130c48395%26node_id%3D4b542f8f20dcfdc8%26REQUEST_GUID%3D171e88ee-9860-acc3-d392-fdf6de68a87c%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0%253D0%253F326-171e88ee995-0x109%26cal_mod%3Dfalse&TPool=r1rover&TDuration=16&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=90.186.49.84&Script=/roverimp/0/0/9&Server=rover.ebay.de&TMachine=10.204.61.57&TStamp=22:56:52.23&TName=roverimp_INTL&Agent=Mozilla/5.0 (iPad; CPU OS 12_4_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604.1&RemoteIP=90.186.49.84&Encoding=gzip&Referer=https://www.ebay-kleinanzeigen.de/"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/9?trknvp=reg%3DHamburg%26catid%3D80%26rurl%3Dhttps%25253A%25252F%25252Fwww.ebay-kleinanzeigen.de%25252Fs-haus-garten%25252Fgartendeko-alte%25252Fk0c80&mpt=1588744612147&imp=1605052");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:56:42.386")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("f592057f6d65f27b");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285005436%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e857d493-0x1df");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(204098687831L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYOEAAAAAAAICYQAEABAAgAADgAAAAAAwAAABAAAiAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYOEAAAAAAAICYQAEABAAgAADgAAAAAAwAAABAAAiAAAAAAAAUA**&ssc=23830334018&gsp=0&!wtballqs=1041-null|939--0.003797867009310254&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=218&!_viwtbranks=1|2&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=6&vibisdm=770X1000&app=2571&!_OBFS_SELLERID=1364404048&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=75508&plcampt=0%3A10855064018%2C&!_callingpageid=2349624&swccount=1&meta=888&slr=1364404048&curprice=3.83&viPageType=0&attrct=6&!pymntMethods=PPL|MSC|VSA|MAE|AMX&rq=f592057f6d65f27b&l1=1492&l2=179961&qtys=20&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=173884637945&binamt=3.83&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=770X1000&sn=rich-five&qtya=22&st=9&uit=1588741002042&mav=6.0.1&pudo=0&to_zip=pe133rh&vibisbm=1000&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=7300&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=97&t=3&u=1151771663&nofp=6&rpdur=14&tr=1716419&dc=3&!vidsig=DEFAULT|DEFAULT&nozp=6&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=173884637945&promol=1&vpcg=false&epidonvi=22027473982&iver=3469975721007&!_OBFS_BUYERID=1151771663&es=3&itmtitle=5Pcs%2FLot+Fishing+Lures+Kinds+Of+Minnow+Fish+Bass+Tackle+Hooks+Baits+Crankbait+RF&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=1041|939&shipsiteid=0&obfs_listing_is_eligible=true&nw=10&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=USD%3A1.99%5E-1%5E-1%5E2020-05-21T03%3A00%3A00-07%3A00%5E2020-07-01T03%3A00%3A00-07%3A00%5EECONOMY%5EOtherInternational%5E0%5E-1%5E3%5E-1%5EUK%5Epe133rh%5E173884637945%5E-1%5E10%5E38&qtymod=true&!_wtbqs=1041|939&addOnTypes=SUPPORT%2CGENERIC%2CFREIGHT&fimbsa=500&vidsigct=3&cp_usd=4.76&!_viwtbids=1041|939&swcembg=true&xt=226616&!_OBFS_EMAIL_COUNT=0&ciid=V9M6hS8*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D9036bfb29f48b653%26node_id%3D01348554cc698a21%26REQUEST_GUID%3D171e857d-48d0-a688-52f4-2466feaa9c80%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%25285005436%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e857d493-0x1df%26cal_mod%3Dfalse&TPool=r1viexpsvc7&TDuration=3&TStatus=0&TType=URL&ContentLength=3359&ForwardedFor=23.209.73.30;80.0.153.110&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.104.133.47&TStamp=21:56:42.38&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=173884637945&modules=VLS&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,VEHICLE_HISTORY,VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,PRP_PRODUCT_DETAILS,PRP_LISTINGS");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:56:42.054")));
    ubiEvent.setRemoteIP("10.174.141.15");
    ubiEvent.setRequestCorrelationId("4f586ab346e38b03");
    ubiEvent.setSid("e11404.m2780.l2648");
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0220%3A%3D4%29pqtfwpu%29osu%29fgg%7E-fij-171e857d348-0x1a7");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(431380484951L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "segname=11404&osub=-1%257E1&nid=&uc=1&uaid=e857d3471710aa4706418404c10e362bS0&bs=0&bu=44201623906&crd=20200505133947&ul=en-US&pagename=EntryTracking&app=2571&c=1&ch=osgood&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&cguidsrc=cookie&n=07b876771700a4cc0287d4cfe1dd5ad1&url=https%253A%252F%252Fwww.ebay.co.uk%252Fulk%252Fi%252F173884637945%253F_trksid%253Dp11404.c100142.m2780%2526_trkparms%253Daid%25253D1110001%252526algo%25253DSPLICE.SIM%252526ao%25253D1%252526asc%25253D20161019143441%252526meid%25253Dab56af7b63c74e43aaf025b3b2e9ed25%252526pid%25253D100142%252526rk%25253D2%252526rkt%25253D4%252526mehot%25253Dnone%252526b%25253D1%252526sd%25253D254338294634%252526itm%25253D173884637945%252526pmt%25253D1%252526noa%25253D1%252526pg%25253D11404&p=3084&t=3&cflgs=EA**&mppid=117&pn=2&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&rq=4f586ab346e38b03&euid=c9e1c53be22a417e9f9b575e9f6294c8&chnl=7&ciid=V9NHcGQ*&sid=e11404.m2780.l2648");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4f586ab346e38b03%26node_id%3D7aa1f2221ae6df8f%26REQUEST_GUID%3D171e857d-3460-aa47-0641-6ab3de75b915%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0220%253A%253D4%2529pqtfwpu%2529osu%2529fgg%257E-fij-171e857d348-0x1a7%26cal_mod%3Dfalse&TPool=r1rover&TDuration=8&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=80.0.153.110&Script=/rover/0/e11404.m2780.l2648/7&Server=localhost&TMachine=10.164.112.100&TStamp=21:56:42.05&TName=rover&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.174.141.15"));
    ubiEvent.setUrlQueryString(
        "/rover/0/e11404.m2780.l2648/7?memsz=2048.0&res=1080x2145&designsystem=6&mnt=WIFI&prefl=en_LT&segname=11404&tzname=Europe%2FLondon&bu=44201623906&androidid=3e98b7f5fbf8de8f&crd=20200505133947&srcrot=e11404.m2780.l2648&osub=-1%257E1&ort=p&carrier=O2+-+UK&rvr_ts=e857cc9d1710aaecf3611a27ffcc6682&dn=LYA-L09&dpi=537.882x539.102&mtsts=2020-05-05T21%3A56%3A41.546&euid=c9e1c53be22a417e9f9b575e9f6294c8&ctr=0&nrd=1&site=3&mrollp=81&ou=nY%2BsHZ2PrBmdj6wVnY%2BsEZ2PrA2dj6AFlYOmD5GEpw%2Bdj6x9nY%2BseQ**&c=26&mav=6.0.1&osv=9&ids=MP%253Dgedmas1991&loc=https%253A%252F%252Fwww.ebay.co.uk%252Fulk%252Fi%252F173884637945%253F_trksid%253Dp11404.c100142.m2780%2526_trkparms%253Daid%25253D1110001%252526algo%25253DSPLICE.SIM%252526ao%25253D1%252526asc%25253D20161019143441%252526meid%25253Dab56af7b63c74e43aaf025b3b2e9ed25%252526pid%25253D100142%252526rk%25253D2%252526rkt%25253D4%252526mehot%25253Dnone%252526b%25253D1%252526sd%25253D254338294634%252526itm%25253D173884637945%252526pmt%25253D1%252526noa%25253D1%252526pg%25253D11404&ist=0&udid=ad402a4c168acc2d4dd1bb50015645bd&rvrhostname=rover.ebay.com&user_name=gedmas1991&ai=2571&rvrsite=0&tz=1.0&reqts=1588741002030&rlutype=1&mos=Android&rvr_id=0&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&sojTags=bu%3Dbu%2Cch%3Dch%2Csegname%3Dsegname%2Ccrd%3Dcrd%2Curl%3Dloc%2Cosub%3Dosub&ch=osgood&mppid=117");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:57:39.589")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("d80903500813dcd4");
    ubiEvent.setSid("p2061037.m2983");
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%28504421%3A%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e858b406-0xec");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(273787040088L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYOEAAAAAAAIAYQAEABAAgAADgAAAAAAwAAABAAACAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYOEAAAAAAAIAYQAEABAAgAADgAAAAAAwAAABAAACAAAAAAAAUA**&ssc=1%7C952523219&gsp=0&!wtballqs=1041-null|938-0.002196675875661365|1007--0.0013971137445672119|932--0.005935865798236718|939--0.007558882983979158&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=210&!_viwtbranks=1|3&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&!_wtbsh=938&picsig=938&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=13&vibisdm=1000X1000&app=2571&!_OBFS_SELLERID=1126282819&var=610799513818&mskutraitct=1&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=319539&plcampt=0%3A11314177019%2C&!_callingpageid=2349624&swccount=1&meta=888&slr=1126282819&curprice=9.81&viPageType=0&attrct=5&!pymntMethods=PPL|MSC|VSA|MAE|AMX&rq=d80903500813dcd4&l1=1492&l2=179961&qtys=28&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=312056955535&binamt=9.81&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=1000X1000&sn=a_coming868&qtya=36&st=9&uit=1588741059066&mav=6.0.1&pudo=0&to_zip=pe133rh&vibisbm=1000&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=7300&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=97&t=3&u=1151771663&nofp=13&rpdur=30&tr=2170361&dc=3&!vidsig=DEFAULT|DEFAULT|RETURNS%2CSELLER_ACCEPT_RETURNS_SIGNAL%2C30|DEFAULT&nozp=13&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=312056955535&promol=1&vpcg=false&iver=2083152502021&!_OBFS_BUYERID=1151771663&es=3&itmtitle=10pcs+Fishing+Lures+Crankbaits+Hooks+Minnow+Frog+Baits+Tackle+Crank+Set&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=1041|938|1007|932|939&shipsiteid=0&obfs_listing_is_eligible=true&nw=0&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&viwtburgency=938&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=USD%3A0.5%5E-1%5E-1%5E2020-05-20T03%3A00%3A00-07%3A00%5E2020-06-30T03%3A00%3A00-07%3A00%5EECONOMY%5EOtherInternational%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E312056955535%5E-1%5E9%5E37&qtymod=true&!_wtbqs=1041|938|1007&addOnTypes=SUPPORT%2CGENERIC%2CFREIGHT&fimbsa=500&vidsigct=3&cp_usd=8.39&!_viwtbids=1041|1007&swcembg=true&xt=226616&!_OBFS_EMAIL_COUNT=0&ciid=WLH6vj8*&sid=p2061037.m2983");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0e892f4d4896a82b%26node_id%3Ddce2522cfcfafdb6%26REQUEST_GUID%3D171e858b-3ff0-ad4b-e3f6-97d6fe82f756%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%2528504421%253A%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e858b406-0xec%26cal_mod%3Dfalse&TPool=r1viexpsvc7&TDuration=2&TStatus=0&TType=URL&ContentLength=3602&ForwardedFor=95.101.129.159;80.0.153.110; 23.209.73.30&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.212.190.63&TStamp=21:57:39.58&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=312056955535&modules=VLS&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,VEHICLE_HISTORY,VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,PRP_PRODUCT_DETAILS,PRP_LISTINGS");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:58:08.616")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("ee7ead005b5241b3");
    ubiEvent.setSid("p2061037.m2983");
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%28507426%3A%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e8592569-0x1dd");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(1026794922841L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYOEAAAAAAAIAYQAEABAAgAADgAAAAAAwAAABAAAiAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYOEAAAAAAAIAYQAEABAAgAADgAAAAAAwAAABAAAiAAAAAAAAUA**&ssc=23830334018&gsp=0&!wtballqs=1041-null|938-0.01277635504668143|932-0.0066229853921415845|939--0.0023557580435975695&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=205&!_viwtbranks=1|3&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&!_wtbsh=938&picsig=938&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=12&vibisdm=1100X1100&app=2571&!_OBFS_SELLERID=1364404048&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=75508&plcampt=0%3A11649003018%2C&!_callingpageid=2349624&swccount=1&meta=888&slr=1364404048&curprice=4.02&viPageType=0&attrct=5&!pymntMethods=PPL|MSC|VSA|MAE|AMX|DSC&rq=ee7ead005b5241b3&l1=1492&l2=179961&qtys=476&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=173123598536&binamt=4.02&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=1100X1100&sn=rich-five&qtya=33&st=9&uit=1588741088191&mav=6.0.1&pudo=0&to_zip=pe133rh&vibisbm=1000&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=7300&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=97&t=3&u=1151771663&nofp=12&rpdur=14&tr=1812293&dc=3&!vidsig=DEFAULT|DEFAULT|DEFAULT&nozp=12&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=173123598536&promol=1&vpcg=false&epidonvi=523127437&iver=3483274213007&!_OBFS_BUYERID=1151771663&es=3&itmtitle=5PCS+Large+Frog+Topwater+Soft+Fishing+Lure+Crankbait+Hooks+Bass+Bait+Tackle+RF&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=1041|938|932|939&shipsiteid=0&obfs_listing_is_eligible=true&nw=68&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&viwtburgency=938&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=USD%3A1.99%5E-1%5E-1%5E2020-05-21T03%3A00%3A00-07%3A00%5E2020-07-01T03%3A00%3A00-07%3A00%5EECONOMY%5EOtherInternational%5E0%5E-1%5E3%5E-1%5EUK%5Epe133rh%5E173123598536%5E-1%5E10%5E38&qtymod=true&!_wtbqs=1041|938|932&addOnTypes=SUPPORT%2CGENERIC%2CFREIGHT&fimbsa=500&vidsigct=3&cp_usd=4.99&!_viwtbids=1041|932&swcembg=true&xt=226616&!_OBFS_EMAIL_COUNT=0&ciid=WSO%2FEe8*&sid=p2061037.m2983");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De1c588c2c0730b08%26node_id%3Dda4feaa6e16107ec%26REQUEST_GUID%3D171e8592-5640-aa41-1ef4-da74feb2005e%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%2528507426%253A%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e8592569-0x1dd%26cal_mod%3Dfalse&TPool=r1viexpsvc7&TDuration=3&TStatus=0&TType=URL&ContentLength=3523&ForwardedFor=80.0.153.110; 23.209.73.30;92.123.78.28&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.164.17.239&TStamp=21:58:08.61&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=173123598536&modules=VLS&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,VEHICLE_HISTORY,VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,PRP_PRODUCT_DETAILS,PRP_LISTINGS");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:37:48.173")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("7ffbd529b0fb48ad");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285012324%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e87d748e-0xf2");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(780264895101L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYKEAAAAAAAIAcQAEABAAgIATgAAAAAAwAAABgAAiAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYKEAAAAAAAIAcQAEABAAgIATgAAAAAAwAAABgAAiAAAAAAAAUA**&ssc=1&gsp=0&!sh2srv=-1%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EONE_DAY%5EUK_DPDNextDay%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E222989488054%5E-1%5E2%5E4&ppcPromotionType=BillMeLaterPromotionalOffer&!wtballqs=1041-null|930-0.013080889791487364|932--0.012322682181600698|938--0.013310982207928305|1007--0.015445393226008552|939--0.019989737910013024|928--0.024469165233424718&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=241&!_viwtbranks=1|2&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&picsig=1049%7C1046&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=12&vibisdm=1600X1600&app=2571&!_OBFS_SELLERID=123139015&bdrs=0&an=eBayAndroid&swctrs=true&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=14930&vpcgpcui=2&!_callingpageid=2349624&swccount=2&meta=11700&slr=123139015&curprice=97.99&viPageType=0&attrct=17&!pymntMethods=PPL|MSC|VSA|MAE|AMX&rq=7ffbd529b0fb48ad&l1=631&l2=3244&qtys=795&vidp=true&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=222989488054&vict=android&binamt=97.99&obfs_desc_has_contactInfo=false&vibisb=1600&vibisd=1600X1600&sn=scottdirect1&qtya=2&st=9&uit=1588743467625&mav=6.0.1&pudo=1&to_zip=pe133rh&vibisbm=1600&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=23782&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=100&t=3&u=1151771663&nofp=12&rpdur=30&tr=1504178&dc=3&!vidsig=DEFAULT|DELIVERY%2CQUALIFIES_BOPIS_SIGNAL|DEFAULT|DEFAULT&viFinancingUKModule=true&nozp=12&vioid=5767268105&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=222989488054&promol=0&vpcgpc=4&!sh3srv=-1%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EONE_DAY%5EUK_DPDNextDay%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E222989488054%5E-1%5E2%5E4&vpcg=true&epidonvi=22031984125&iver=2812187846012&!_OBFS_BUYERID=1151771663&es=3&itmtitle=Makita+P-90532+General+Maintenance+227PC+Tool+Kit&viFinancingModule=true&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=1041|930|932|938|1007|939|928&!sh4srv=-1%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EONE_DAY%5EUK_DPDNextDay%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E222989488054%5E-1%5E2%5E4&shipsiteid=3&obfs_listing_is_eligible=true&nw=3312&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&viwtburgency=1046&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=GBP%3A0.0%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EONE_DAY%5EUK_DPDNextDay%5E0%5E-1%5E1%5E-1%5EUK%5Epe133rh%5E222989488054%5E-1%5E2%5E4&!_wtbqs=1041|930|932&addOnTypes=WARRANTY&fimbsa=500&viot=14&vidsigct=3&cp_usd=121.71338&!_viwtbids=1041|930&swcembg=true&xt=226616&viof=1&!_OBFS_EMAIL_COUNT=0&ciid=fXJpq7U*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D07bc26766d43df17%26node_id%3Dd67553cb35d6706e%26REQUEST_GUID%3D171e87d7-4880-aada-bb57-6ff3febfa119%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%25285012324%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e87d748e-0xf2%26cal_mod%3Dfalse&TPool=r1viexpsvc7&TDuration=3&TStatus=0&TType=URL&ContentLength=4270&ForwardedFor=23.209.73.30;80.0.153.110&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.173.171.181&TStamp=22:37:48.17&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=222989488054&modules=VLS&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,VEHICLE_HISTORY,VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,PRP_PRODUCT_DETAILS,PRP_LISTINGS");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:39:22.938")));
    ubiEvent.setRemoteIP("80.0.153.110");
    ubiEvent.setRequestCorrelationId("224ba7cda504a72d");
    ubiEvent.setSid("p2061037.m2983");
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dqkisqn47pse31%287316466%2Busqdrrp%2Btil%2Bceb%7C%28dlh-171e87ee6ba-0x12f");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(54205146238L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxYKEAAAAAAAIAcQEEABAAgAADgAAAAAAwAAABgAACAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&flgs=AIAxYKEAAAAAAAIAcQEEABAAgAADgAAAAAAwAAABgAACAAAAAAAAUA**&ssc=1&gsp=0&!sh2srv=-1%5E-1%5E-1%5E2020-05-14T03%3A00%3A00-07%3A00%5E2020-05-19T03%3A00%3A00-07%3A00%5EECONOMY%5EUK_OtherCourier%5E0%5E-1%5E2%5E-1%5EUK%5Epe133rh%5E223975082419%5E-1%5E5%5E8&ppcPromotionType=Installment&!wtballqs=930--0.013574376569852078|932--0.021772023985621617|1007--0.022523457056626006&viStreamId=ad402a4c168acc2d4dd1bb50015645bd&snippetlength=240&!_viwtbranks=1|2&obfs_sid_uid_same=false&mos=Android&nqt=AAAAggAAAAAAAAAAAEAAAwAAAEAAAAABCAAAAAgAQABAAAQAAgAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACDAAAAABAAAIA**&picsig=1049%7C764%7C1046&ppc_ofid=6342793715&osv=9&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=1&noep=7&vibisdm=1344X1477&app=2571&!_OBFS_SELLERID=173914835&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=224&!_callingpageid=2349624&swccount=1&meta=11700&slr=173914835&curprice=110.92&viPageType=0&attrct=10&!pymntMethods=PPL|MSC|VSA|MAE|AMX&rq=224ba7cda504a72d&l1=631&l2=3244&qtys=2&nsm=NF&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=3&OBFS_ITEMID=223975082419&binamt=110.92&obfs_desc_has_contactInfo=false&vibisb=1000&vibisd=1344X1477&sn=caringquality&qtya=2&st=9&uit=1588743562464&mav=6.0.1&pudo=1&to_zip=pe133rh&vibisbm=1000&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&leaf=23782&nativeApp=true&cp=2349624&n=07b876771700a4cc0287d4cfe1dd5ad1&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=99&t=3&u=1151771663&nofp=7&rpdur=30&tr=542395&dc=3&!vidsig=DELIVERY%2CQUALIFIES_BOPIS_SIGNAL|DEFAULT|DEFAULT|RETURNS%2CSELLER_ACCEPT_RETURNS_SIGNAL%2C30|DEFAULT&viFinancingUKModule=true&nozp=7&dm=HUAWEI&dn=HWLYA&!_OBFS_LINK_COUNT=0&uc=3&mbsa=500&ul=en-GB&pymntVersion=1&ec=4&res=0x0&efam=ITM&itm=223975082419&promol=0&!sh3srv=-1%5E-1%5E-1%5E2020-05-14T03%3A00%3A00-07%3A00%5E2020-05-19T03%3A00%3A00-07%3A00%5EECONOMY%5EUK_OtherCourier%5E0%5E-1%5E2%5E-1%5EUK%5Epe133rh%5E223975082419%5E-1%5E5%5E8&vpcg=false&iver=2811307934012&!_OBFS_BUYERID=1151771663&es=3&viFinancingModule=true&itmtitle=Professional+Mechanics+Home+Tool+Kit+Set+102pc+Maintenance+Hand+Tools+with+Case&cflgs=AA**&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&eactn=EXPC&!_wtbaqs=930|932|1007&!sh4srv=-1%5E-1%5E-1%5E2020-05-14T03%3A00%3A00-07%3A00%5E2020-05-19T03%3A00%3A00-07%3A00%5EECONOMY%5EUK_OtherCourier%5E0%5E-1%5E2%5E-1%5EUK%5Epe133rh%5E223975082419%5E-1%5E5%5E8&shipsiteid=3&obfs_listing_is_eligible=true&nw=35&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&viwtburgency=764&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=GBP%3A0.0%5E-1%5E-1%5E2020-05-14T03%3A00%3A00-07%3A00%5E2020-05-19T03%3A00%3A00-07%3A00%5EECONOMY%5EUK_OtherCourier%5E0%5E-1%5E2%5E-1%5EUK%5Epe133rh%5E223975082419%5E-1%5E5%5E8&qtymod=true&!_wtbqs=930|932|1007&addOnTypes=WARRANTY&fimbsa=500&vidsigct=3&cp_usd=137.77373&!_viwtbids=930|932&swcembg=true&!_OBFS_EMAIL_COUNT=0&ciid=fuTgngw*&sid=p2061037.m2983");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D4c73035a46db4550%26node_id%3D91c37dfd957ab383%26REQUEST_GUID%3D171e87ee-6b80-ad39-e0c3-0b00ff549b4b%26logid%3Dt6wwm53vpd77%253C%253Dqkisqn47pse31%25287316466%252Busqdrrp%252Btil%252Bceb%257C%2528dlh-171e87ee6ba-0x12f%26cal_mod%3Dfalse&TPool=r1viexpsvc7&TDuration=2&TStatus=0&TType=URL&ContentLength=4115&ForwardedFor=80.0.153.110;92.123.78.44; 23.209.73.30&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.211.158.12&TStamp=22:39:22.93&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;9;HUAWEI;HWLYA;O2 - UK;1080x2145;3.0&RemoteIP=80.0.153.110&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=223975082419&modules=VLS&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,VEHICLE_HISTORY,VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,PRP_PRODUCT_DETAILS,PRP_LISTINGS");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:40:03.345")));
    ubiEvent.setRemoteIP("10.202.7.45");
    ubiEvent.setRequestCorrelationId("887634bf8cac3a4f");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*517%3E61%28twwgsvv%28umj%28bad%7F%29%60jk-171e8803ee7-0x121");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(734756552320L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=gedmas1991&tz=1.0&imgscount=1&lv=1&dn=LYA-L09&ist=0&rlutype=1&tzname=Europe%2FLondon&uc=1&mos=Android&bs=0&uaid=e8803ee71710a4d12ab3b31e9eff15d6S0&memsz=2048.0&osv=9&ul=en-US&mtsts=2020-05-05T22%3A40%3A03.345&pagename=EnterSearch&app=2571&res=1080x2145&c=11&mav=6.0.1&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&reqts=1588743651008&cguidsrc=cookie&n=07b876771700a4cc0287d4cfe1dd5ad1&ort=p&p=2048309&ttp=Page&mnt=WIFI&carrier=O2+-+UK&t=3&u=1151771663&prefl=en_LT&cflgs=EA**&ids=MP%253Dgedmas1991&designsystem=6&mrollp=81&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&mppid=117&androidid=3e98b7f5fbf8de8f&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&pn=2&rq=887634bf8cac3a4f&ciid=gD7nEqs*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D887634bf8cac3a4f%26node_id%3Df896a7b78caaedea%26REQUEST_GUID%3D171e8803-ee60-a4d1-2ab0-7de2cafadc2f%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A517%253E61%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e8803ee7-0x121%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=80.0.153.110&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18.171&TStamp=22:40:51.04&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.202.7.45"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay.com&site=3&ai=2571&rvrsite=0&rlutype=1&imp=2048309&lv=udid%3Dad402a4c168acc2d4dd1bb50015645bd%26ai%3D2571%26mav%3D6.0.1%26site%3D3%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AFlYOmD5GEpw%252Bdj6x9nY%252BseQ**%26memsz%3D2048.0%26res%3D1080x2145%26mrollp%3D81%26designsystem%3D6%26c%3D11%26osv%3D9%26ids%3DMP%253Dgedmas1991%26mnt%3DWIFI%26ist%3D0%26prefl%3Den_LT%26imgscount%3D1%26tzname%3DEurope%2FLondon%26user_name%3Dgedmas1991%26androidid%3D3e98b7f5fbf8de8f%26reqts%3D1588743651008%26tz%3D1.0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DO2+-+UK%26gadid%3D0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1%26dn%3DLYA-L09%26dpi%3D537.882x539.102%26mppid%3D117%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A40%3A03.345&udid=ad402a4c168acc2d4dd1bb50015645bd&mppid=117");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:39:30.147")));
    ubiEvent.setRemoteIP("10.202.7.45");
    ubiEvent.setRequestCorrelationId("887634bf8cac3a4f");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*517%3E61%28twwgsvv%28umj%28bad%7F%29%60jk-171e8803ee5-0x113");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(734756355712L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=gedmas1991&tz=1.0&lv=1&dn=LYA-L09&ist=0&rlutype=1&tzname=Europe%2FLondon&uc=1&mos=Android&bs=0&uaid=e8803ee41710a4d12ab3b31e9eff15d8S0&memsz=2048.0&osv=9&ul=en-US&mtsts=2020-05-05T22%3A39%3A30.147&pagename=ViewItemPhotos&app=2571&res=1080x2145&c=10&mav=6.0.1&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&reqts=1588743651007&cguidsrc=cookie&n=07b876771700a4cc0287d4cfe1dd5ad1&ort=p&p=2052300&ttp=Page&mnt=WIFI&carrier=O2+-+UK&t=3&u=1151771663&prefl=en_LT&cflgs=EA**&ids=MP%253Dgedmas1991&designsystem=6&mrollp=81&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&mppid=117&androidid=3e98b7f5fbf8de8f&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&pn=2&rq=887634bf8cac3a4f&ciid=gD7kEqs*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D887634bf8cac3a4f%26node_id%3D8faf50de6cdc6b41%26REQUEST_GUID%3D171e8803-ee40-a4d1-2ab0-7de2cafadc30%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A517%253E61%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e8803ee5-0x113%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=80.0.153.110&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18.171&TStamp=22:40:51.04&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.202.7.45"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay.com&site=3&ai=2571&rvrsite=0&rlutype=1&imp=2052300&lv=udid%3Dad402a4c168acc2d4dd1bb50015645bd%26ai%3D2571%26mav%3D6.0.1%26site%3D3%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AFlYOmD5GEpw%252Bdj6x9nY%252BseQ**%26memsz%3D2048.0%26res%3D1080x2145%26mrollp%3D81%26designsystem%3D6%26c%3D10%26osv%3D9%26ids%3DMP%253Dgedmas1991%26mnt%3DWIFI%26ist%3D0%26prefl%3Den_LT%26tzname%3DEurope%2FLondon%26user_name%3Dgedmas1991%26androidid%3D3e98b7f5fbf8de8f%26reqts%3D1588743651007%26tz%3D1.0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DO2+-+UK%26gadid%3D0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1%26dn%3DLYA-L09%26dpi%3D537.882x539.102%26mppid%3D117%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A39%3A30.147&udid=ad402a4c168acc2d4dd1bb50015645bd&mppid=117");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:40:13.365")));
    ubiEvent.setRemoteIP("10.202.7.45");
    ubiEvent.setRequestCorrelationId("887634bf8cac3a4f");
    ubiEvent.setSid("p2048309.m2511.l1311");
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*517%3E61%28twwgsvv%28umj%28bad%7F%29%60jk-171e8803eee-0x126");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(734757011072L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=gedmas1991&tz=1.0&lv=1&dn=LYA-L09&ist=0&rlutype=1&tzname=Europe%2FLondon&uc=1&mos=Android&bs=0&uaid=e8803eee1710a4d12ab3b31e9eff15d2S0&memsz=2048.0&osv=9&trkp=qacc%253DTRS0%252CR1%252CTR5%252CTRC0%252CXlong%252520wre%252CQlong%252520wrench%252CNPF0%252CPFS0%252CTTC1853%252CMEDT307%252CAVGT309&ul=en-US&mtsts=2020-05-05T22%3A40%3A13.365&pagename=SearchResultsViewed&app=2571&res=1080x2145&c=16&mav=6.0.1&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&reqts=1588743651009&cguidsrc=cookie&n=07b876771700a4cc0287d4cfe1dd5ad1&ort=p&p=2056193&ttp=Page&mnt=WIFI&carrier=O2+-+UK&t=3&u=1151771663&prefl=en_LT&cflgs=EA**&ids=MP%253Dgedmas1991&designsystem=6&mrollp=81&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&mppid=117&androidid=3e98b7f5fbf8de8f&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&pn=2&rq=887634bf8cac3a4f&ciid=gD7uEqs*&sid=p2048309.m2511.l1311");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D887634bf8cac3a4f%26node_id%3D8bc35b9c3a1f1a97%26REQUEST_GUID%3D171e8803-eed0-a4d1-2ab0-7de2cafadc2d%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A517%253E61%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e8803eee-0x126%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=80.0.153.110&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18.171&TStamp=22:40:51.05&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.202.7.45"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay.com&site=3&ai=2571&rvrsite=0&rlutype=1&imp=2056193&lv=udid%3Dad402a4c168acc2d4dd1bb50015645bd%26ai%3D2571%26mav%3D6.0.1%26site%3D3%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AFlYOmD5GEpw%252Bdj6x9nY%252BseQ**%26memsz%3D2048.0%26res%3D1080x2145%26mrollp%3D81%26designsystem%3D6%26c%3D16%26osv%3D9%26ids%3DMP%253Dgedmas1991%26mnt%3DWIFI%26ist%3D0%26prefl%3Den_LT%26tzname%3DEurope%2FLondon%26user_name%3Dgedmas1991%26androidid%3D3e98b7f5fbf8de8f%26reqts%3D1588743651009%26tz%3D1.0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26trkp%3Dqacc%253DTRS0%252CR1%252CTR5%252CTRC0%252CXlong%252520wre%252CQlong%252520wrench%252CNPF0%252CPFS0%252CTTC1853%252CMEDT307%252CAVGT309%26carrier%3DO2+-+UK%26gadid%3D0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1%26dn%3DLYA-L09%26dpi%3D537.882x539.102%26mppid%3D117%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A40%3A13.365&udid=ad402a4c168acc2d4dd1bb50015645bd&_trksid=p2048309.m2511.l1311&mppid=117");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 22:37:50.347")));
    ubiEvent.setRemoteIP("10.202.7.45");
    ubiEvent.setRequestCorrelationId("887634bf8cac3a4f");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*517%3E61%28twwgsvv%28umj%28bad%7F%29%60jk-171e8803edb-0x119");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("80.0.153.110");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(734755765888L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "user_name=gedmas1991&tz=1.0&lv=1&dn=LYA-L09&ist=0&rlutype=1&tzname=Europe%2FLondon&uc=1&mos=Android&bs=0&uaid=e8803edb1710a4d12ab3b31e9eff15dfS0&memsz=2048.0&osv=9&ul=en-US&mtsts=2020-05-05T22%3A37%3A50.347&pagename=ViewItemPhotos&app=2571&res=1080x2145&c=5&mav=6.0.1&g=ad402a4c168acc2d4dd1bb50015645bd&h=4c&reqts=1588743651007&cguidsrc=cookie&n=07b876771700a4cc0287d4cfe1dd5ad1&ort=p&p=2052300&ttp=Page&mnt=WIFI&carrier=O2+-+UK&t=3&u=1151771663&prefl=en_LT&cflgs=EA**&ids=MP%253Dgedmas1991&designsystem=6&mrollp=81&gadid=0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1&mppid=117&androidid=3e98b7f5fbf8de8f&pcguid=07b876771700a4cc0287d4cfe1dd5ad1&pn=2&rq=887634bf8cac3a4f&ciid=gD7bEqs*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D887634bf8cac3a4f%26node_id%3Da02d9d72d6630038%26REQUEST_GUID%3D171e8803-edb0-a4d1-2ab0-7de2cafadc34%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A517%253E61%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171e8803edb-0x119%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=80.0.153.110&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.77.18.171&TStamp=22:40:51.03&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.202.7.45"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay.com&site=3&ai=2571&rvrsite=0&rlutype=1&imp=2052300&lv=udid%3Dad402a4c168acc2d4dd1bb50015645bd%26ai%3D2571%26mav%3D6.0.1%26site%3D3%26ou%3DnY%252BsHZ2PrBmdj6wVnY%252BsEZ2PrA2dj6AFlYOmD5GEpw%252Bdj6x9nY%252BseQ**%26memsz%3D2048.0%26res%3D1080x2145%26mrollp%3D81%26designsystem%3D6%26c%3D5%26osv%3D9%26ids%3DMP%253Dgedmas1991%26mnt%3DWIFI%26ist%3D0%26prefl%3Den_LT%26tzname%3DEurope%2FLondon%26user_name%3Dgedmas1991%26androidid%3D3e98b7f5fbf8de8f%26reqts%3D1588743651007%26tz%3D1.0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DO2+-+UK%26gadid%3D0e1f6958-d08e-4569-a58b-fac4b0aa670c%2C1%26dn%3DLYA-L09%26dpi%3D537.882x539.102%26mppid%3D117%26ttp%3DPage%26mtsts%3D2020-05-05T22%3A37%3A50.347&udid=ad402a4c168acc2d4dd1bb50015645bd&mppid=117");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:14:55.624")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId("e0esyQ4x541w");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dosusqn47pse31%285020%3E05%2Busqdrrp%2Bjqp%2Bceb%7C%28dlh-171e8319488-0x1fa");
    ubiEvent.setEventFamily("ITM");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;1080x2164;3.4");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(421370827569L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AIAxIMAAAAAAAAICYQAEABAAgAABgAAAAAAwAAABAAACAAAAAAAAUA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "snippet=1&nqc=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&flgs=AIAxIMAAAAAAAAICYQAEABAAgAABgAAAAAAwAAABAAACAAAAAAAAUA**&ssc=11190903013&gsp=0&ppcPromotionType=INSTALLMENT&viStreamId=e83186d0171dc45b331bda30016b6cd2&snippetlength=54&obfs_sid_uid_same=false&viFinancingBanner=true&mos=Android&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&ppc_ofid=6342819203&osv=10&callingpagename=viexpsvc__VIEWLISTING&viSignedInFlag=0&noep=11&vibisdm=1200X1600&app=2571&!_OBFS_SELLERID=687196293&bdrs=0&an=eBayAndroid&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=2429&!_callingpageid=2349624&swccount=1&meta=6000&slr=687196293&curprice=1150.0&viPageType=0&attrct=5&ccc_off_id=6342779101&!pymntMethods=PPL|VSA|MSC|AMX|DSC&rq=e0esyQ4x541w&!cat1=10063&l1=6028&l2=10063&qtys=0&nsm=NF&itmcond=3000&!_OBFS_BUYER_VIEWING_SITEID=0&bs=0&OBFS_ITEMID=323799503664&binamt=1150.0&obfs_desc_has_contactInfo=false&vibisb=1600&vibisd=1200X1600&sn=trebla119&qtya=1&st=9&mav=6.0.1&pudo=0&vibisbm=1600&g=e83186d0171dc45b331bda30016b6cd2&h=d0&leaf=38664&nativeApp=true&cp=2349624&n=e8318b081710ad4841125071c0a1ddab&OBFS_DEFAULT_DESCRIPTION=true&!_OBFS_PHONE_COUNT=0&p=2349624&fdp=99&t=0&nofp=11&rpdur=30&tr=54629&dc=1&nozp=11&dm=samsung&dn=z3q&!_OBFS_LINK_COUNT=0&uc=1&mbsa=500&ul=en-US&pymntVersion=1&!oed=0&ec=4&res=0x0&efam=ITM&itm=323799503664&promol=0&vpcg=false&iver=2387039332011&!_OBFS_BUYERID=0&es=0&vi_finopt=1&itmtitle=Harley+Touring+Custom+Tins+Paint+Set+Fenders+Gas+Fuel+Tank+Bags&viFinancingModule=true&cflgs=AA**&gxoe=vine&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&eactn=EXPC&shipsiteid=100&obfs_listing_is_eligible=true&nw=5&vibis=400&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&vi_cobranded=true&!notes=0&ppfoid=0&pagename=viexpsvc__VIEWLISTING&!sh1srv=USD%3A86.98%5E-1%5E-1%5E2020-05-11T03%3A00%3A00-07%3A00%5E2020-05-13T03%3A00%3A00-07%3A00%5EECONOMY%5EOther%5E0%5E-1%5E1%5E-1%5EUS%5Enull%5E323799503664%5E-1%5E3%5E5&vi_cobrand_mem=false&!cmplsvs=0|0&addOnTypes=WARRANTY&fimbsa=500&cp_usd=1150.0&swcembg=true&!vifit=4&!_OBFS_EMAIL_COUNT=0&ciid=MZOoG2I*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dce5745d0bb18d62c%26node_id%3Ddc3d244b25f8ad68%26REQUEST_GUID%3D171e8319-4820-aa11-b622-671cfe9dbe4f%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%25285020%253E05%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171e8319488-0x1fa%26cal_mod%3Dfalse&TPool=r1viexpsvc7&TDuration=4&TStatus=0&TType=URL&ContentLength=3197&ForwardedFor=23.56.175.28; 172.232.21.4;174.221.5.135&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.161.27.98&TStamp=21:14:55.62&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;1080x2164;3.4&RemoteIP=174.221.5.135&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/listing_details/v1/view_item?item_id=323799503664&modules=VLS,VOLUME_PRICING,SME&supported_ux_components=ITEM_CARD,ITEM_PLACEHOLDER,UNANSWERED_Q,FITMENT,BUY_BOX,PICTURES,TITLE,MSKU_PICKER,SECTIONS,AUTHENTICITY,SME,EBAY_PLUS_PROMO,VEHICLE_HISTORY,VEHICLE_PRICING,ADD_ON,ITEM_CONDENSED_CONTAINER,ITEM_CONDENSED,ITEM_STATUS_MESSAGE,VALIDATE,ALERT_CUSTOM,MULTI_TOP_PICK,PRODUCT_SUMMARY,PRODUCT_REVIEWS_SUMMARY,PRP_PRODUCT");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:37.737")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId("PTFPS5qqa0Ou");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%28535413%3A%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e83e1fea-0x13a");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("EXPC");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;1080x2164;3.4");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(276172382014L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&flgs=AA**&dm=samsung&dn=z3q&uc=1&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&mos=Android&bs=0&pageci=0e22e718-8f52-11ea-858d-74dbd180dcf0&osv=10&ul=en-US&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&ec=4&pagename=hpservice__experience_search_v1_get_homepage_search_result_GET&app=2571&res=0x0&efam=HOMEPAGE&mav=6.0.1&g=e83186d0171dc45b331bda30016b6cd2&h=d0&nativeApp=true&cp=2481888&an=eBayAndroid&n=e8318b081710ad4841125071c0a1ddab&es=0&p=2481888&t=0&cflgs=AA**&lfcat=0&eactn=EXPC&rq=PTFPS5qqa0Ou&ciid=Ph8oTUA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De80a3c64e99de636%26node_id%3D0841a1d5c1bc5259%26REQUEST_GUID%3D171e83e1-fe60-a9c4-d400-11d4f6bd6e23%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%2528535413%253A%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e83e1fea-0x13a%26cal_mod%3Dfalse&TPool=r1hpservice30&TDuration=2&TStatus=0&TType=URL&ContentLength=1376&ForwardedFor=172.232.21.21;174.221.5.135&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.156.77.64&TStamp=21:28:37.73&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;1080x2164;3.4&RemoteIP=174.221.5.135&Encoding=gzip&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/experience/shopping/v1/home?_answersVersion=1&_pgn=ALL&supported_ux_components=ITEMS_CAROUSEL,ITEMS_CAROUSEL_V3,EVENTS_CAROUSEL,SELLERS,NOTIFICATIONS,THIRD_PARTY_ADS_BANNER,TEXT_BANNER,FULL_BLEED_BANNER,MULTI_CTA_BANNER,NAVIGATION_IMAGE_GRID,NAVIGATION_IMAGE_ANSWER_CAROUSEL,USER_GARAGE_CAROUSEL");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:37.743")));
    ubiEvent.setRemoteIP("10.195.199.95");
    ubiEvent.setRequestCorrelationId("22d630a3caeb61e7");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ctofthu%60t*0060%3A27%29pqtfwpu%29pie%29fgg%7E-fij-171e83e1ff1-0x1a9");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(948012326718L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "app=2571&c=1&g=e83186d0171dc45b331bda30016b6cd2&nid=&h=d0&cguidsrc=cookie&n=e8318b081710ad4841125071c0a1ddab&uc=1&p=3084&uaid=e83e1ff01710a9cb9dc77f56c2bc39bcS0&bs=0&rvrid=2410574292579&t=0&cflgs=EA**&ul=en-US&mppid=119&pn=2&pcguid=e8318b081710ad4841125071c0a1ddab&rq=22d630a3caeb61e7&pagename=EntryTracking&ciid=Ph%2Fwudw*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D22d630a3caeb61e7%26node_id%3Deb2b8f62f540b0d5%26REQUEST_GUID%3D171e83e1-fef0-a9cb-9dc0-c6c3e010d67f%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t%2A0060%253A27%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e83e1ff1-0x1a9%26cal_mod%3Dfalse&TPool=r1rover&TDuration=39&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=174.221.5.135&Script=/rover/1/0/4&Server=localhost&TMachine=10.156.185.220&TStamp=21:28:37.74&TName=rover&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.195.199.95"));
    ubiEvent.setUrlQueryString(
        "/rover/1/0/4?memsz=8192.0&res=1080x2164&designsystem=6&mnt=MOBILE&prefl=en_US&tzname=America%2FChicago&androidid=9af24c9972b9e493&ort=p&carrier=Verizon&dpi=386.3655x383.3955&dn=SM-G988U&mtsts=2020-05-05T21%3A28%3A37.575&ctr=0&nrd=1&site=0&mrollp=73&c=7&mav=6.0.1&osv=10&ids=MP&ist=0&udid=e83186d0171dc45b331bda30016b6cd2&rvrhostname=rover.ebay.com&ai=2571&rvrsite=0&reqts=1588739317741&tz=-5.0&rlutype=1&mos=Android&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&mppid=119");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:48.147")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId(null);
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pbhnmpo%3D9whhpbhnmpo*krbsa(rbpv6710-171e83e4838-0x2906");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("Mozilla/5.0 (Linux; Android 10; SM-G988U Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(504798136382L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "app=2571&nqc=AAAAAAAABAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJA&webview=true&c=1&!qt=56226,226654,226602,226007,226526&g=e83186d0171dc45b331bda30016b6cd2&h=49&es=0&nqt=AAAAAAAABAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJA&p=2504946&bot_provider=%7B%22providers%22%3A%7B%22DT%22%3A%7B%22headers%22%3A%7B%22xdb%22%3A%220%22%2C%22drid%22%3A%22a0badd05-bd1d-4c67-8f1e-9640383106ad%22%2C%22xrip%22%3A%22174.221.5.135%22%7D%7D%7D%7D&!qc=590081,590081,590081,590081,590081&t=0&cflgs=QA**&SigninRedirect=NodeOther&ec=2&pn=0&pagename=sgninui__SignInMFA&ciid=PkhQiHU*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De83e483b1710aae8c7285ad1ffdf4c22%26node_id%3D%26REQUEST_GUID%3De83e48491710aae8c72c92d1ff9b2e26%26logid%3Dt6pbhnmpo%253D9whhpbhnmpo%2Akrbsa%28rbpv6710-171e83e4838-0x2906%26statusCode%3D200&TPool=r1sgninui&TStatus=0&TType=URL&ContentLength=0&ForwardedFor=174.221.5.135&Script=/signin/mfa&Server=www.ebay.com&TMachine=10.174.140.114&TStamp=21:28:48.14&TName=SignInMFA&Agent=Mozilla/5.0 (Linux; Android 10; SM-G988U Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36&RemoteIP=174.221.5.135&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/signin/mfa?id=1VcDxbrQsUAnFqWyfBvql-19&UUID=e83186d0171dc45b331bda30016b6cd2&srcAppId=2571&trackingSrcAppId=2571&trackingNativeAppGuid=e83186d0171dc45b331bda30016b6cd2&trackingApp=webview&correlation=si=e83e48491710aae8c72c92d1ff9b2e26,c=1,trk-gflgs=QA**&SSRFallback=0");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:29:48.361")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId(null);
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pbhnmpo%3D9whhpbhnmpo*hh%3F1s(rbpv6710-171e83f332f-0x2902");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("Mozilla/5.0 (Linux; Android 10; SM-G988U Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(487198044991L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "app=2571&nqc=AAAAAAAAgAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACBA&webview=true&c=6&!qt=56226,226601,226522,226006&g=e83186d0171dc45b331bda30016b6cd2&h=d0&n=e83e4a8d1710a9cb71024307c2e37139&es=0&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACBA&p=2504946&bot_provider=%7B%22providers%22%3A%7B%22DT%22%3A%7B%22headers%22%3A%7B%22xdb%22%3A%221%22%2C%22drid%22%3A%22ba8e8c57-4d80-4d6a-b89a-fd3db35a075c%22%2C%22xrip%22%3A%22174.221.5.135%22%7D%7D%7D%7D&!qc=590081,590081,590081,590081&t=0&SigninRedirect=NodeOther&ec=2&pn=0&pagename=sgninui__SignInMFA&ciid=PzNEb3E*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3De83f33311710aae8c27d24ecffe0c688%26node_id%3D%26REQUEST_GUID%3De83186d0171dc45b331bda30016b6cd2%26logid%3Dt6pbhnmpo%253D9whhpbhnmpo%2Ahh%253F1s%28rbpv6710-171e83f332f-0x2902%26statusCode%3D200&TPool=r1sgninui&TStatus=0&TType=URL&ContentLength=0&ForwardedFor=174.221.5.135&Script=/signin/mfa&Server=www.ebay.com&TMachine=10.174.140.39&TStamp=21:29:48.36&TName=SignInMFA&Agent=Mozilla/5.0 (Linux; Android 10; SM-G988U Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36&RemoteIP=174.221.5.135&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/signin/mfa?id=IJIssZNEb397bdAToGfWG-21&UUID=e83186d0171dc45b331bda30016b6cd2&srcAppId=2571&trackingSrcAppId=2571&trackingNativeAppGuid=e83186d0171dc45b331bda30016b6cd2&trackingApp=webview&correlation=gci=e83e4a8d1710a9cb71024307c2e37139,si=e83186d0171dc45b331bda30016b6cd2,c=6,trk-gflgs=&SSRFallback=0");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:55.761")));
    ubiEvent.setRemoteIP("10.15.147.19");
    ubiEvent.setRequestCorrelationId("608f65024b8a1974");
    ubiEvent.setSid("p2050533");
    ubiEvent.setRlogid("t6qjpbq%3F%3Ctofthu%60t*0055%3B14%29pqtfwpu%29pie%29fgg%7E-fij-171e83fb6f0-0x1a6");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(351448118847L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("www.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&tz=-5.0&lv=1&dn=SM-G988U&ist=0&rlutype=1&tzname=America%2FChicago&uc=1&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&mos=Android&bs=0&uaid=e83fb6f01710a9cd3517a0ecc2db664eS0&memsz=8192.0&osv=10&ul=en-US&mtsts=2020-05-05T21%3A28%3A55.761&ec=4&pagename=SignInSocial&app=2571&res=1080x2164&c=12&mav=6.0.1&g=e83186d0171dc45b331bda30016b6cd2&h=d0&reqts=1588739421919&cguidsrc=cookie&n=e8318b081710ad4841125071c0a1ddab&es=0&ort=p&p=2540848&ttp=Page&mnt=MOBILE&carrier=Verizon&t=0&prefl=en_US&cflgs=EA**&ids=MP&designsystem=6&mrollp=73&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&mppid=119&androidid=9af24c9972b9e493&pcguid=e8318b081710ad4841125071c0a1ddab&pn=2&rq=608f65024b8a1974&ciid=P7bw01E*&sid=p2050533");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D608f65024b8a1974%26node_id%3D99be9ccab4b152ec%26REQUEST_GUID%3D171e83fb-6ef0-a9cd-3511-6f7be02234d3%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t%2A0055%253B14%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e83fb6f0-0x1a6%26cal_mod%3Dfalse&TPool=r1rover&TDuration=4&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=174.221.5.135&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.156.211.81&TStamp=21:30:21.93&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.15.147.19"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay.com&site=0&ai=2571&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26nqt%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26ec%3D4%3C%2Fa%3E&rlutype=1&imp=2540848&lv=udid%3De83186d0171dc45b331bda30016b6cd2%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26memsz%3D8192.0%26res%3D1080x2164%26mrollp%3D73%26designsystem%3D6%26c%3D12%26osv%3D10%26ids%3DMP%26mnt%3DMOBILE%26ist%3D0%26prefl%3Den_US%26tzname%3DAmerica%2FChicago%26androidid%3D9af24c9972b9e493%26reqts%3D1588739421919%26tz%3D-5.0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DVerizon%26gadid%3D8780e3f4-6571-436b-b6ac-3cd243611196%2C1%26dn%3DSM-G988U%26dpi%3D386.3655x383.3955%26mppid%3D119%26ttp%3DPage%26mtsts%3D2020-05-05T21%3A28%3A55.761&udid=e83186d0171dc45b331bda30016b6cd2&_trksid=p2050533&mppid=119");
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:45.730")));
    ubiEvent.setRemoteIP("174.221.5.135");
    ubiEvent.setRequestCorrelationId("6d71cc897547b2e9");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dpiesqn47pse31%2855%3A516%3B%2Busqdrrp%2Buk%60%2Bceb%7C%28dlh-171e83fb4c4-0xd1");
    ubiEvent.setEventFamily("HOMEPAGE");
    ubiEvent.setEventAction("CLIENT_PAGE_VIEW");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;1080x2164;3.4");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(1027667244095L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("apisd.ebay.com");
    ubiEvent.setApplicationPayload(
        "flgs=AA**&usecase=prm&tz=-5.0&dm=samsung&dn=z3q&rlutype=1&trknvpsvc=%3Ca%3Ees%3D0%26nqc%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26nqt%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26ec%3D4%3C%2Fa%3E&mos=Android&pageci=0e22e718-8f52-11ea-858d-74dbd180dcf0&osv=10&callingpagename=hpservice__experience_search_v1_get_homepage_search_result_GET&mtsts=2020-05-06T04%3A28%3A45.730Z&pagename=CLIENT_PAGE_VIEW&app=2571&res=0x0&parentrq=PTFPS5qqa0Ou&efam=HOMEPAGE&mav=6.0.1&c=8&g=e83186d0171dc45b331bda30016b6cd2&h=d0&nativeApp=true&cp=2481888&an=eBayAndroid&n=e8318b081710ad4841125071c0a1ddab&ort=p&p=2553215&t=0&cflgs=AA**&ids=MP&designsystem=6&mrollp=73&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&mppid=119&eactn=CLIENT_PAGE_VIEW&androidid=9af24c9972b9e493&rq=6d71cc897547b2e9&ciid=P7S9Re8*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D6d71cc897547b2e9%26node_id%3D1faddb51a06f4d20%26REQUEST_GUID%3D171e83fb475.a9cbeda.1a70e.dbc26baf%26logid%3Dt6wwm53vpd77%253C%253Dpiesqn47pse31%252855%253A516%253B%252Busqdrrp%252Buk%2560%252Bceb%257C%2528dlh-171e83fb4c4-0xd1%26cal_mod%3Dfalse&TPool=r1pulsgwy&TDuration=2&TStatus=0&TType=URL&ContentLength=1329&ForwardedFor=23.56.175.14; 172.232.21.21;174.221.5.135&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.202.69.239&TStamp=21:30:21.37&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayAndroid;6.0.1;Android;10;samsung;z3q;Verizon;1080x2164;3.4&RemoteIP=174.221.5.135&Encoding=gzip&Referer=null"));
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
    ubiEvent.setEventTimestamp(Long.parseLong(SojTimestamp.getSojTimestamp("2020-05-05 21:28:55.719")));
    ubiEvent.setRemoteIP("10.15.147.19");
    ubiEvent.setRequestCorrelationId("608f65024b8a1974");
    ubiEvent.setSid("p2047939");
    ubiEvent.setRlogid("t6qjpbq%3F%3Ctofthu%60t*0055%3B14%29pqtfwpu%29pie%29fgg%7E-fij-171e83fb6eb-0x126");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("174.221.5.135");
    ubiEvent.setAgentInfo("eBayAndroid/6.0.1.15");
    ubiEvent.setCobrand(6);
    ubiEvent.setCurrentImprId(351447725631L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("localhost");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&tz=-5.0&lv=1&dn=SM-G988U&ist=0&rlutype=1&tzname=America%2FChicago&uc=1&nqt=AAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA&mos=Android&bs=0&uaid=e83fb6ea1710a9cd3517a0ecc2db6650S0&memsz=8192.0&osv=10&ul=en-US&mtsts=2020-05-05T21%3A28%3A55.719&ec=4&pagename=UserSignin&app=2571&res=1080x2164&c=11&mav=6.0.1&g=e83186d0171dc45b331bda30016b6cd2&h=d0&reqts=1588739421919&cguidsrc=cookie&n=e8318b081710ad4841125071c0a1ddab&es=0&ort=p&p=2050533&ttp=Page&mnt=MOBILE&carrier=Verizon&t=0&prefl=en_US&cflgs=EA**&ids=MP&designsystem=6&mrollp=73&gadid=8780e3f4-6571-436b-b6ac-3cd243611196%2C1&mppid=119&androidid=9af24c9972b9e493&pcguid=e8318b081710ad4841125071c0a1ddab&pn=2&rq=608f65024b8a1974&ciid=P7bq01E*&sid=p2047939");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D608f65024b8a1974%26node_id%3Dfb44e7327317a64a%26REQUEST_GUID%3D171e83fb-6e90-a9cd-3511-6f7be02234d4%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t%2A0055%253B14%2529pqtfwpu%2529pie%2529fgg%257E-fij-171e83fb6eb-0x126%26cal_mod%3Dfalse&TPool=r1rover&TDuration=4&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=174.221.5.135&Script=/roverimp/0/0/14&Server=localhost&TMachine=10.156.211.81&TStamp=21:30:21.92&TName=roverimp&Agent=eBayAndroid/6.0.1.15&RemoteIP=10.15.147.19"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/0/14?rvrhostname=rover.ebay.com&site=0&ai=2571&rvrsite=0&trknvpsvc=%3Ca%3Ees%3D0%26nqc%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26nqt%3DAAAAAAAAgAAAAAAAAAAAAAAAAQAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADACA%26ec%3D4%3C%2Fa%3E&rlutype=1&imp=2050533&lv=udid%3De83186d0171dc45b331bda30016b6cd2%26ai%3D2571%26mav%3D6.0.1%26site%3D0%26memsz%3D8192.0%26res%3D1080x2164%26mrollp%3D73%26designsystem%3D6%26c%3D11%26osv%3D10%26ids%3DMP%26mnt%3DMOBILE%26ist%3D0%26prefl%3Den_US%26tzname%3DAmerica%2FChicago%26androidid%3D9af24c9972b9e493%26reqts%3D1588739421919%26tz%3D-5.0%26rlutype%3D1%26mos%3DAndroid%26ort%3Dp%26carrier%3DVerizon%26gadid%3D8780e3f4-6571-436b-b6ac-3cd243611196%2C1%26dn%3DSM-G988U%26dpi%3D386.3655x383.3955%26mppid%3D119%26ttp%3DPage%26mtsts%3D2020-05-05T21%3A28%3A55.719&udid=e83186d0171dc45b331bda30016b6cd2&_trksid=p2047939&mppid=119");
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
    Assert.assertEquals(1, sessionAccumulator.getUbiSession().getTrafficSrcId());
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
