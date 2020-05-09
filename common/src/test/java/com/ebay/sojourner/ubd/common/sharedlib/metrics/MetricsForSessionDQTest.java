package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJParseClientInfo;
import com.ebay.sojourner.ubd.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class MetricsForSessionDQTest {

  private List<UbiEvent> ubiEventList;
  private SessionAccumulator sessionAccumulator;
  private SessionMetrics sessionMetrics;

  @BeforeEach
  public void setup() throws Exception {

    sessionAccumulator = new SessionAccumulator();
    sessionMetrics = SessionMetrics.getInstance();
    ubiEventList = new ArrayList<>();

  }

  @Test
  public void test_TrafficSourceMetric() throws Exception {

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
