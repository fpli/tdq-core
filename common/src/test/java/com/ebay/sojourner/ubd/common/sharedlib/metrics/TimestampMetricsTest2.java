package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.SessionBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJParseClientInfo;
import com.ebay.sojourner.ubd.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimestampMetricsTest2 {

  private List<UbiEvent> ubiEventList;
  private SessionAccumulator sessionAccumulator;
  private SessionMetrics sessionMetrics;
  private SessionBotDetector sessionBotDetector;

  @Before
  public void setUp() throws Exception {
    ubiEventList = new ArrayList<>();
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("bb9007991710a860aec14a80ffb5be52");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2047675);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-27 05:15:10.739")));
    ubiEvent.setRemoteIP("40.77.167.181");
    ubiEvent.setRequestCorrelationId("cf6d84fae63729e4");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6ulcpjqcj9%3Fjqpsobtlrbn%285110376%2Busqdrrp%2Bj"
        + "qp%2Bceb%7C%28dlh-171bb9007aa-0x171");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("40.77.167.181");
    ubiEvent
        .setAgentInfo("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+"
            + " (KHTML, like Gecko) BingPreview/1.0b");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(1013790148496L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("QIAxIIEAAAAAAAICYQAEAAAAgIBBgIAAAABQAAABAAAEAEAAAAAAEA**");
    ubiEvent.setWebServer("www.ebay.co.uk");
    ubiEvent
        .setApplicationPayload(
            "nqc=AA**&flgs=QIAxIIEAAAAAAAICYQAEAAAAgIBBgIAAAABQAAABAAAEAEAAAAAAEA**&ssc=1&!wtballqs=946-0.036264010231655874|1001--0.02156751951919425|1007--0.02315153088320185&nlpp=10&obfs_sid_uid_same=false&nqt=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABA**&!SHIPPINGSERVICE=SSP%3A1%5EDSP%3A1&swcenabled=true&noep=4&vibisdm=765X600&iwsv=0&!IMPORT_CHARGE=GBP%3A0.0%5E%5E%5E%5E&!vimc=1%5E88%5E100008,1%5E144%5E100623&!_OBFS_SELLERID=774465978&mtpvi=0&!_wtbss=946|1001|1007&bdrs=0&OBFS_EP_STATE=NOT_IN_EXPERIMENT&fbscore=532&!_callingpageid=2047675&swccount=1&meta=11450&slr=774465978&OBFS_STATUS=NOT_REQUIRED&curprice=14.99&attrct=8&virvwcnt=0&fssmdc=23&rq=cf6d84fae63729e4&bc=0&visbetyp=2&l1=260012&l2=1059&!_itmhs=1035|941|946|945|1007|947|1001&qtys=0&itmcond=1000&!_OBFS_BUYER_VIEWING_SITEID=3&bs=0&OBFS_ITEMID=173926446112&obfs_desc_has_contactInfo=false&vibisb=640&vibisd=765X600&qtya=1&st=9&c=1&vibisbm=640&g=bb9007991710a860aec14a80ffb5be52&h=99&leaf=11507&!_OBFS_PHONE_COUNT=0&p=2047675&fdp=99&t=3&nofp=4&rpdur=30&tr=891398&dc=1&visplt=100562%2C100567%2C100938%2C100727%2C100565%2C100916%2C100917%2C100918%2C100919%2C&!ampid=3P_IMP&nozp=0&bsnippets=true&sdes=1&!_OBFS_LINK_COUNT=0&uc=1&fssmd=17&shsvrcnt=1&mbsa=500&uaid=bb90079a1710a860aec14a80ffb5be51S0&bot_provider=%7B%22providers%22%3A%7B%22AK%22%3A%7B%22headers%22%3A%7B%22akb%22%3A%22Akamai-Categorized+Bot+%28bingpreview%29%3Amonitor%3AWeb+Search+Engine+Bots%22%7D%7D%7D%7D&ul=en-GB&pymntVersion=1&ec=1&itm=173926446112&promol=0&iver=2994799419007&!_OBFS_BUYERID=0&es=3&fistime=15&vimr=100008&itmtitle=Nike+Mercurial+Lite+Shin+Guards+Small+Size+White%2FBlack+Brand+New&cflgs=QA**&gxoe=vine&pymntMethods=PPL%7CMSC%7CVSA%7CMAE%7CAMX&vwc=0&!_wtbaqs=946|1001|1007&shipsiteid=3&virvwavg=0.0&obfs_listing_is_eligible=true&nw=0&vibis=300&ppc_promo=&OBFS_NON_OBFUSCATION_REASON=NO_CONTACT_INFO_DETECTED&ppfoid=0&pagename=ViewItemPageRaptor&!sh1srv=GBP%3A18.59%5E-1%5E-1%5E2020-05-15T03%3A15%3A10-07%3A00%5E2020-05-20T03%3A15%3A10-07%3A00%5EEXPEDITED%5EInternationalPriorityShippingUK%5E1%5EGBP%3A0.0%5E1%5E-1%5EUS%5Enull%5E173926446112%5E-1%5E14%5E17&!_wtbqs=946|1001|1007&rpg=2047675&fimbsa=500&iimp=10&cp_usd=18.493914&swcembg=true&srv=0&wtbsh=946&!_itmhss=946|1001|1007&!_OBFS_EMAIL_COUNT=0&pn=2&qtrmn=1&ciid=kAeaCuw*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dcf6d84fae63729e4%26node_id%3D4d11bdb7af1e0d7c%26REQUEST_GUID%3D171bb900-7930-a860-aec0-c309ffc676ad%26logid%3Dt6ulcpjqcj9%253Fjqpsobtlrbn%25285110376%252Busqdrrp%252Bjqp%252Bceb%257C%2528dlh-171bb9007aa-0x171&TPool=r1viewitem&TDuration=414&TStatus=0&TType=URL&ContentLength=0&ForwardedFor=40.77.167.181, 23.213.54.140,23.77.231.15&Script=/itm&Server=www.ebay.co.uk&TMachine=10.134.10.236&TStamp=05:15:10.73&TName=ViewItemPageRaptor&Agent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ (KHTML, like Gecko) BingPreview/1.0b&RemoteIP=40.77.167.181&Encoding=gzip"));
    ubiEvent.setUrlQueryString(
        "/itm/Nike-Mercurial-Lite-Shin-Guards-Small-Size-White-Black-Brand-New-/173926446112");
    ubiEvent.setPageName("ViewItemPageRaptor");
    ubiEvent.setVersion(3);
    ubiEvent.setSiteId(3);
    ubiEvent.setClickId(1);
    ubiEvent.setRefererHash(null);
    ubiEvent.setCookies(null);
    ubiEvent.setReferrer(null);
    ubiEvent.setUserId(null);
    ubiEvent.setItemId(173926446112L);
    ubiEvent.setRegu(0);
    ubiEvent.setStaticPageType(3);
    ubiEvent.setSourceImprId(null);

    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("bb9007991710a860aec14a80ffb5be52");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-27 05:15:10.854")));
    ubiEvent.setRemoteIP("40.77.167.181");
    ubiEvent.setRequestCorrelationId("bb1878321710a4b5a344c3f3ff9425f4");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Ftilvgig%28uq%60b%3E*w%60ut3542-171bb900806-0xd522");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("40.77.167.181");
    ubiEvent
        .setAgentInfo(
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ (KHTML,"
                + " like Gecko) BingPreview/1.0b");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(71085787280L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "saucxgdpry=true&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid=3ca452341ddc4777a91a8590325d2fd7&g=bb9007991710a860aec14a80ffb5be52&saebaypid=100564&sapcxkw=Nike+Mercurial+Lite+Shin+Guards+Small+Size+White%2FBlack+Brand+New&h=99&schemaversion=3&salv=5&ciid=kAgLjRA*&p=2367355&sapcxcat=11450%2C260012%2C1059%2C11507&t=3&saiid=30fe6b38-9399-4c57-9ef3-5b57cb6da96d&cflgs=AA**&samslid=&eactn=AUCT&pn=2&rq=bb1878321710a4b5a344c3f3ff9425f4&pagename=SandPage&ciid=kAgLjRA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dbb1878321710a4b5a344c3f3ff9425f4%26node_id%3D5fe43ee3e746263b%26REQUEST_GUID%3D171bb900-8060-aae8-d104-9904f965e73a%26logid%3Dt6pdhc9%253Ftilvgig%2528uq%2560b%253E%2Aw%2560ut3542-171bb900806-0xd522&TPool=r1sand&TDuration=7&ContentLength=1113&ForwardedFor=10.75.101.216&Script=sand&Server=sand.stratus.ebay.com&TMachine=10.174.141.16&TStamp=05:15:10.85&TName=sand.v1&Agent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ (KHTML, like Gecko) BingPreview/1.0b&RemoteIP=40.77.167.181"));
    ubiEvent.setUrlQueryString(
        "/itm/Nike-Mercurial-Lite-Shin-Guards-Small-Size-White-Black-Brand-New-/173926446112");
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
    ubiEvent.setGuid("bb9007991710a860aec14a80ffb5be52");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-27 05:15:10.854")));
    ubiEvent.setRemoteIP("40.77.167.181");
    ubiEvent.setRequestCorrelationId("bb1878321710a4b5a344c3f3ff9425f4");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Ftilvgig%28uq%60b%3E*w%60ut3542-171bb900806-0xd522");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("40.77.167.181");
    ubiEvent
        .setAgentInfo("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+"
            + " (KHTML, like Gecko) BingPreview/1.0b");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(71086049424L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "saucxgdpry=true&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid=d254f5f4c92a447bae756fc5e11e50ae&g=bb9007991710a860aec14a80ffb5be52&saebaypid=100938&sapcxkw=Nike+Mercurial+Lite+Shin+Guards+Small+Size+White%2FBlack+Brand+New&h=99&schemaversion=3&salv=5&ciid=kAgPjRA*&p=2367355&sapcxcat=11450%2C260012%2C1059%2C11507&t=3&saiid=3f2b4559-4811-4889-9327-16f45d78920c&cflgs=AA**&samslid=&eactn=AUCT&pn=2&rq=bb1878321710a4b5a344c3f3ff9425f4&pagename=SandPage&ciid=kAgPjRA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dbb1878321710a4b5a344c3f3ff9425f4%26node_id%3D5fe43ee3e746263b%26REQUEST_GUID%3D171bb900-8060-aae8-d104-9904f965e73a%26logid%3Dt6pdhc9%253Ftilvgig%2528uq%2560b%253E%2Aw%2560ut3542-171bb900806-0xd522&TPool=r1sand&TDuration=9&ContentLength=1113&ForwardedFor=10.75.101.216&Script=sand&Server=sand.stratus.ebay.com&TMachine=10.174.141.16&TStamp=05:15:10.85&TName=sand.v1&Agent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ (KHTML, like Gecko) BingPreview/1.0b&RemoteIP=40.77.167.181"));
    ubiEvent
        .setUrlQueryString(
            "/itm/Nike-Mercurial-Lite-Shin-Guards-Small-Size-White-Black-Brand-New-/173926446112");
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
    ubiEvent.setGuid("bb9007991710a860aec14a80ffb5be52");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-27 05:15:10.919")));
    ubiEvent.setRemoteIP("10.204.120.71");
    ubiEvent.setRequestCorrelationId("cf6d84fae63729e4");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ckuvthu%60t*0227%3A42%29pqtfwpu%29osu%29f"
        + "gg%7E-fij-171bb900848-0x110");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("10.204.120.71");
    ubiEvent
        .setAgentInfo("eBayNioHttpClient 1.0,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(949628700816L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAgAAAIAQAAAAAAAAAAIAAAAAAAAAAAAQAAAAAQABAAAQAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAABAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB&c=1&g=bb9007991710a860aec14a80ffb5be52&h=99&px=4249&chnl=9&uc=1&es=3&nqt=AAAAgAAAIAQAAAAAAAAAAIAAAAAAAAAAAAQAAAAAQABAAAQAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAQEAAAAAABAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB&p=2317508&uaid=bb9008481710a861add68bc0cb421b0cS0&bs=0&t=3&cflgs=QA**&ul=en-US&plmt=SwEAAB%252BLCAAAAAAAAAA9j82OwyAMhN%252FFZw42JKDkIVba86pClJAtUtIgSNOuorz7OunPaUYez4dZIf1CK7Ey2tQCUoT2Z4XYQUuIWioBPu45aVRSEUqtKgGZZ9DUfYeI%252FmwaXxlPTpNU2inVIyljFAgIM%252BNOrO6JfdG5LM29P%252FtUeGmE9nobBgFlervEz1dNI6BnszEgpvypIuwTt1sivhKJIW55d0cOiOUBLQqY%252FCGuPGU6ssLfW2GcujB8uTEwaInJLvXFpruN1znkxQ2W0HbSzhKR%252BSW47C%252Fft5D%252FeB22%252FQQ%252FHzTPNNxO2z%252BZVMUKSwEAAA%253D%253D&ec=1&pn=2&rq=cf6d84fae63729e4&pagename=cos__mfe&po=%5B%28pg%3A2047675+pid%3A100623%29%5D&ciid=kAhIGt0*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dcf6d84fae63729e4%26node_id%3D6e82c814af74596c%26REQUEST_GUID%3D171bb900-8470-a861-add6-5842e40abcc1%26logid%3Dt6qjpbq%253F%253Ckuvthu%2560t%2A0227%253A42%2529pqtfwpu%2529osu%2529fgg%257E-fij-171bb900848-0x110%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=10.134.10.236&Script=/roverimp/0/2047675/9&Server=internal.rover.vip.ebay.com&TMachine=10.134.26.221&TStamp=05:15:10.91&TName=roverimp&Agent=eBayNioHttpClient 1.0,GingerClient/2.9.7-RELEASE&RemoteIP=10.204.120.71"));
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
    ubiEvent.setGuid("bb9007991710a860aec14a80ffb5be52");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-27 05:15:10.864")));
    ubiEvent.setRemoteIP("10.69.228.55");
    ubiEvent.setRequestCorrelationId("cf6d84fae63729e4");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Cumjthu%60t*062353%28twwgsvv%28umj%"
        + "28bad%7F%29%60jk-171bb900811-0x11e");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("40.77.167.181");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ (KHTML, like Gecko) "
            + "BingPreview/1.0b,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(252078786704L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AA**&c=1&g=bb9007991710a860aec14a80ffb5be52&h=99&px=4249&chnl=9&uc=1&es=3&nqt=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABA**&p=2317508&uaid=bb9008111710a12b13a6e973d8720a80S0&bs=0&t=3&cflgs=QA**&ul=en-US&plmt=di4AAB%252BLCAAAAAAAAADtmltz2zYWgP%252BKhn11bADE1W%252B2m2a7k6TZymkfOp0MbrS4okguSVl1M%252Fnvew5FyXYUt1Ls2nVHfjCJAxDEuX24UB%252BT%252BiI5ZoQrqcRBUufJ8S8fkzwkx5QQk5qDxOdYTzXhlFFCCSMHSQOyRDshaWoFiVLzoJnTKYtaZEZwbp0OyUESO%252BjuV7jaZbdD7%252FAwU4vM%252BbqFRrPkuJwXxUHSVqu7Gl7PDbw8g5tP0EG17ADkyeTKNTl27qFA01TAbQcVHxOL1WNvy2ALrLf49oRSLggUmSSEMrihRBi8UEFUAn3nqG1CVWqY5FzSvlEOzyZv82kcvYmNnze5LUav8y6OxpO8HL2a2ya0o%252FHMFsVonP8eRz9PoPLotLB%252BOjptYAijt3GB%252FeCYWIrD8VWIUHh1%252Bg7loFni4KbCF1G4mS4e5o3z3VyT1zhCNFDufTPclrUdRnUR6uZ9G5uhWBezbmmuPjpAYj36If7Wwf3lAg3OsYfe5BL%252FKYJGvmhmd9ZdLogYKtHVGZrmMjZXryvUxxb%252BVjkU0FNS9PcTdPGNusu8uv1suF0fmotb5Qz1ymzRxuQTKNdHUQvGxesk5hcTeNxAvC%252Fy0E2SY8U0NCtjt6ia6fdohlRz1b%252FnfZl3vSQ6e3U4nx5e5vWR7bIPIbbTrqrRdnNX5O3k%252FKqG%252Fhn0Y%252BtpXr77TNou4%252Fff434sQ6H9KTZtXpUYS4f0sI%252FQeVNA8egobw7xnW1nu9wf%252Bmp25JujyyNPj8arrl4sHzqc5eXhf1tUFZVs%252B5S6S80v1hhFMBubmDWxnXwbC3uVHKcE%252FtbCM8xCIEnefn8COnTNPGLhtf0dbG7BQr25QVT%252BlMfFj8uHrtu9Go9fltYVMaxksbwEPeumCnPfoQ0g3XM%252FxfjdJs5bsMFJ754%252FdE4LyYT%252BSyFUl0BwzhCijKGKEqslsdFTbjXJMidcFCz5hKawCDMhBKES4%252BByRbAZyClcfkuOwTaV70u27Uu26ktt6F0cbeMn%252F5lDTEJP6Jsqw8zEzn3XN%252FTQkKBH1ljWn2OZGCoGLAcmeCYy7g2znCtnoxIy8yJSGgVoscfyk2F5B9fcB8v6YbAMvLpZdxvU1817OSV7gO8BvjvAt8iIfyDAFZNrgCvCqKRMECIHgEuhQpoF54IzYBNuuDaKMiZSyVNn3V8EcN%252FaFb1Jmq7pPYk2xOasKqoGU%252FyisVcfljLM9SGsEjvvKgTUfOZi80N2EjA2DTQo8hLjFLySVWX3nZ3lBZrpBDmaLIWIzfO8KyB9aHot%252Bja2vsnrPmKOKbtRUc1sXr7Oy%252BlS7nFsfQdLWfINxvPZWTJU3WyPdVoTsqo7R0IOT%252FTSsup7el%252BCgjj2dbh3KD6tinVYA4QgjG0xrq3PS8ASzsD4yL%252F6tLs1fNwz3ai6MSCs6UdyEsaxto3t0M7JAmcRjMeua3I3x16G95xGJCdE0XqOWoMPPPjOXsQf%252Bpe2%252FdQbUNDHLMAL0%252BOXPoVezKcvIIVeEIpO%252FN8Qu%252Fed5Ua91RaQD0tyxxYHiNMB6DixZRkReLMsdst5aYLFiDwIa4ucuOrytuuX1l6y7Az06hpoGq85dIG9zKdJb%252BHpuW0uerJ8cIUtUdpURQF9goFPYbQXTTUvwxDOyTcvT79TL3HZ4edtV81Ors09RMa4hldWTQwjHB2mVRdndWGXsAG7tt1V0d9D2khIVKkFJk8NvK9KW4CxQp8N%252FWjBVbPYw2mLJB9MtpxYkt6Rjze77AzTL3D%252BCfmqNvjK0xVfU0u914bTlMEuwQrrUhsUIwTMLny0T8rXPvH3gN0Ddg%252FY%252BwF2iyzfA%252FZrASukWANWUJoSCnZeA9ZaFywJUntruHHResdiiJZTqZV07CEBu732ZDvt%252F1BrvqE14%252BtpxVsuWMppCJ4rpayhVgtDUiYCy4Lan7s82bnLDq75%252BnMXiI77nbv87U%252FCYZO6XpCkhOx0knL25t37v%252F0RyucKft1JyW4nIFvE5p0nIINRn9PRh5Bqg6GCrc6uM0ozGy01aAzOows%252BDTLwjGdROv9XHX2ErH4LkQProryt0dFLmiq%252BZ%252BkGS3dw0X1Yqm6x9E4kpeI6YZXYiUg%252Fnr%252F5II35ApSeCj%252BfKfPr1jB5XgCQGwDg%252BDuDHgA0OiEtVSIIzyMJJoATmY9a28za%252BKAAeMylo2Hsxic7WDVSzrWgq092VlJjhDDBOJgItMuolZkQaVQsqof9ZPe4WqcbWhuqV7BX3jNhItFCcSad9ow6nuk00w7%252Fnq%252FWZENrla4%252Fz1qRxSyDKd8yHnUEG5FUEWCqJFnoF4QPpnVeN%252BtHSfJ0Gc82Mp6uv3ZYHmQWjTJBp9wIrxlXTtsgU649yeh%252B2%252FRkU%252F0OrrnPVM%252F%252BfNu01Y5p9ZX6n7Rt2u%252Bavrxr2iI079w1Pb9Nk2F0c%252FVA%252BUBQnvLArbFBq5QLIJ6Q2kWpOGewm3QPOqM86jxK9YbWkrNBa4g6wfEzCUQBDzq6TBujiBdaOFhVPOhXnMfV2myuHtazJSwQPM%252BsUVopriR3IlLmUiEUkFTEZ3u0aqjc0JoLNWjtqKRpZMEaj7%252FrgxWxkRJWjzwLjgb2oEerj6u12oxwsvp9dXAghf21gI0Pl4paTU3kJHpvYkqseV5aQ9n1KCY6%252BfR%252FQ6FpYHYuAAA%253D&ec=1&pn=2&rq=cf6d84fae63729e4&pagename=cos__mfe&po=%5B%28pg%3A2047675+pid%3A100939%29%28pg%3A2047675+pid%3A100938%29%28pg%3A2047675+pid%3A100726%29%28pg%3A2047675+pid%3A100727%29%28pg%3A2047675+pid%3A100565%29%28pg%3A2047675+pid%3A100564%29%28pg%3A2047675+pid%3A100567%29%28pg%3A2047675+pid%3A100566%29%28pg%3A2047675+pid%3A100922%29%28pg%3A2047675+pid%3A100923%29%28pg%3A2047675+pid%3A100920%29%28pg%3A2047675+pid%3A100562%29%28pg%3A2047675+pid%3A100921%29%28pg%3A2047675+pid%3A100918%29%28pg%3A2047675+pid%3A100919%29%28pg%3A2047675+pid%3A100916%29%28pg%3A2047675+pid%3A100917%29%5D&ciid=kAgRsTo*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dcf6d84fae63729e4%26node_id%3Dce598311fd5f9229%26REQUEST_GUID%3D171bb900-8100-a12b-13a7-e02aec0e382d%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t%2A062353%2528twwgsvv%2528umj%2528bad%257F%2529%2560jk-171bb900811-0x11e%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=40.77.167.181&Script=/roverimp/0/2047675/9&Server=internal.rover.vip.ebay.com&TMachine=10.18.177.58&TStamp=05:15:10.86&TName=roverimp&Agent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ (KHTML, like Gecko) BingPreview/1.0b,GingerClient/2.9.7-RELEASE&RemoteIP=10.69.228.55"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2047675/9?site=3&trknvp=plmt%3Ddi4AAB%252BLCAAAAAAAAADtmltz2zYWgP%252BKhn11bADE1W%252B2m2a7k6TZymkfOp0MbrS4okguSVl1M%252Fnvew5FyXYUt1Ls2nVHfjCJAxDEuX24UB%252BT%252BiI5ZoQrqcRBUufJ8S8fkzwkx5QQk5qDxOdYTzXhlFFCCSMHSQOyRDshaWoFiVLzoJnTKYtaZEZwbp0OyUESO%252BjuV7jaZbdD7%252FAwU4vM%252BbqFRrPkuJwXxUHSVqu7Gl7PDbw8g5tP0EG17ADkyeTKNTl27qFA01TAbQcVHxOL1WNvy2ALrLf49oRSLggUmSSEMrihRBi8UEFUAn3nqG1CVWqY5FzSvlEOzyZv82kcvYmNnze5LUav8y6OxpO8HL2a2ya0o%252FHMFsVonP8eRz9PoPLotLB%252BOjptYAijt3GB%252FeCYWIrD8VWIUHh1%252Bg7loFni4KbCF1G4mS4e5o3z3VyT1zhCNFDufTPclrUdRnUR6uZ9G5uhWBezbmmuPjpAYj36If7Wwf3lAg3OsYfe5BL%252FKYJGvmhmd9ZdLogYKtHVGZrmMjZXryvUxxb%252BVjkU0FNS9PcTdPGNusu8uv1suF0fmotb5Qz1ymzRxuQTKNdHUQvGxesk5hcTeNxAvC%252Fy0E2SY8U0NCtjt6ia6fdohlRz1b%252FnfZl3vSQ6e3U4nx5e5vWR7bIPIbbTrqrRdnNX5O3k%252FKqG%252Fhn0Y%252BtpXr77TNou4%252Fff434sQ6H9KTZtXpUYS4f0sI%252FQeVNA8egobw7xnW1nu9wf%252Bmp25JujyyNPj8arrl4sHzqc5eXhf1tUFZVs%252B5S6S80v1hhFMBubmDWxnXwbC3uVHKcE%252FtbCM8xCIEnefn8COnTNPGLhtf0dbG7BQr25QVT%252BlMfFj8uHrtu9Go9fltYVMaxksbwEPeumCnPfoQ0g3XM%252FxfjdJs5bsMFJ754%252FdE4LyYT%252BSyFUl0BwzhCijKGKEqslsdFTbjXJMidcFCz5hKawCDMhBKES4%252BByRbAZyClcfkuOwTaV70u27Uu26ktt6F0cbeMn%252F5lDTEJP6Jsqw8zEzn3XN%252FTQkKBH1ljWn2OZGCoGLAcmeCYy7g2znCtnoxIy8yJSGgVoscfyk2F5B9fcB8v6YbAMvLpZdxvU1817OSV7gO8BvjvAt8iIfyDAFZNrgCvCqKRMECIHgEuhQpoF54IzYBNuuDaKMiZSyVNn3V8EcN%252FaFb1Jmq7pPYk2xOasKqoGU%252FyisVcfljLM9SGsEjvvKgTUfOZi80N2EjA2DTQo8hLjFLySVWX3nZ3lBZrpBDmaLIWIzfO8KyB9aHot%252Bja2vsnrPmKOKbtRUc1sXr7Oy%252BlS7nFsfQdLWfINxvPZWTJU3WyPdVoTsqo7R0IOT%252FTSsup7el%252BCgjj2dbh3KD6tinVYA4QgjG0xrq3PS8ASzsD4yL%252F6tLs1fNwz3ai6MSCs6UdyEsaxto3t0M7JAmcRjMeua3I3x16G95xGJCdE0XqOWoMPPPjOXsQf%252Bpe2%252FdQbUNDHLMAL0%252BOXPoVezKcvIIVeEIpO%252FN8Qu%252Fed5Ua91RaQD0tyxxYHiNMB6DixZRkReLMsdst5aYLFiDwIa4ucuOrytuuX1l6y7Az06hpoGq85dIG9zKdJb%252BHpuW0uerJ8cIUtUdpURQF9goFPYbQXTTUvwxDOyTcvT79TL3HZ4edtV81Ors09RMa4hldWTQwjHB2mVRdndWGXsAG7tt1V0d9D2khIVKkFJk8NvK9KW4CxQp8N%252FWjBVbPYw2mLJB9MtpxYkt6Rjze77AzTL3D%252BCfmqNvjK0xVfU0u914bTlMEuwQrrUhsUIwTMLny0T8rXPvH3gN0Ddg%252FY%252BwF2iyzfA%252FZrASukWANWUJoSCnZeA9ZaFywJUntruHHResdiiJZTqZV07CEBu732ZDvt%252F1BrvqE14%252BtpxVsuWMppCJ4rpayhVgtDUiYCy4Lan7s82bnLDq75%252BnMXiI77nbv87U%252FCYZO6XpCkhOx0knL25t37v%252F0RyucKft1JyW4nIFvE5p0nIINRn9PRh5Bqg6GCrc6uM0ozGy01aAzOows%252BDTLwjGdROv9XHX2ErH4LkQProryt0dFLmiq%252BZ%252BkGS3dw0X1Yqm6x9E4kpeI6YZXYiUg%252Fnr%252F5II35ApSeCj%252BfKfPr1jB5XgCQGwDg%252BDuDHgA0OiEtVSIIzyMJJoATmY9a28za%252BKAAeMylo2Hsxic7WDVSzrWgq092VlJjhDDBOJgItMuolZkQaVQsqof9ZPe4WqcbWhuqV7BX3jNhItFCcSad9ow6nuk00w7%252Fnq%252FWZENrla4%252Fz1qRxSyDKd8yHnUEG5FUEWCqJFnoF4QPpnVeN%252BtHSfJ0Gc82Mp6uv3ZYHmQWjTJBp9wIrxlXTtsgU649yeh%252B2%252FRkU%252F0OrrnPVM%252F%252BfNu01Y5p9ZX6n7Rt2u%252Bavrxr2iI079w1Pb9Nk2F0c%252FVA%252BUBQnvLArbFBq5QLIJ6Q2kWpOGewm3QPOqM86jxK9YbWkrNBa4g6wfEzCUQBDzq6TBujiBdaOFhVPOhXnMfV2myuHtazJSwQPM%252BsUVopriR3IlLmUiEUkFTEZ3u0aqjc0JoLNWjtqKRpZMEaj7%252FrgxWxkRJWjzwLjgb2oEerj6u12oxwsvp9dXAghf21gI0Pl4paTU3kJHpvYkqseV5aQ9n1KCY6%252BfR%252FQ6FpYHYuAAA%253D%26po%3D%5B%28pg%3A2047675+pid%3A100939%29%28pg%3A2047675+pid%3A100938%29%28pg%3A2047675+pid%3A100726%29%28pg%3A2047675+pid%3A100727%29%28pg%3A2047675+pid%3A100565%29%28pg%3A2047675+pid%3A100564%29%28pg%3A2047675+pid%3A100567%29%28pg%3A2047675+pid%3A100566%29%28pg%3A2047675+pid%3A100922%29%28pg%3A2047675+pid%3A100923%29%28pg%3A2047675+pid%3A100920%29%28pg%3A2047675+pid%3A100562%29%28pg%3A2047675+pid%3A100921%29%28pg%3A2047675+pid%3A100918%29%28pg%3A2047675+pid%3A100919%29%28pg%3A2047675+pid%3A100916%29%28pg%3A2047675+pid%3A100917%29%5D&trknvpsvc=%3Ca%3Enqc%3DAA**%26nqt%3DAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABA**%26es%3D3%26ec%3D1%3C%2Fa%3E&tguid=bb9007991710a860aec14a80ffb5be52&imp=23175088");
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
    ubiEvent.setGuid("bb9007991710a860aec14a80ffb5be52");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-27 05:15:10.854")));
    ubiEvent.setRemoteIP("40.77.167.181");
    ubiEvent.setRequestCorrelationId("bb1878321710a4b5a344c3f3ff9425f4");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6pdhc9%3Ftilvgig%28uq%60b%3E*w%60ut3542-171bb900806-0xd522");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("40.77.167.181");
    ubiEvent
        .setAgentInfo("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ "
            + "(KHTML, like Gecko) BingPreview/1.0b");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(71085918352L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "saucxgdpry=true&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid=8b5613a50e684d82b832e85f9544ab8d&g=bb9007991710a860aec14a80ffb5be52&saebaypid=100939&sapcxkw=Nike+Mercurial+Lite+Shin+Guards+Small+Size+White%2FBlack+Brand+New&h=99&schemaversion=3&salv=5&ciid=kAgNjRA*&p=2367355&sapcxcat=11450%2C260012%2C1059%2C11507&t=3&saiid=1a6325c3-3552-488b-9ba6-fab10b4be247&cflgs=AA**&samslid=&eactn=AUCT&pn=2&rq=bb1878321710a4b5a344c3f3ff9425f4&pagename=SandPage&ciid=kAgNjRA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dbb1878321710a4b5a344c3f3ff9425f4%26node_id%3D5fe43ee3e746263b%26REQUEST_GUID%3D171bb900-8060-aae8-d104-9904f965e73a%26logid%3Dt6pdhc9%253Ftilvgig%2528uq%2560b%253E%2Aw%2560ut3542-171bb900806-0xd522&TPool=r1sand&TDuration=9&ContentLength=1113&ForwardedFor=10.75.101.216&Script=sand&Server=sand.stratus.ebay.com&TMachine=10.174.141.16&TStamp=05:15:10.85&TName=sand.v1&Agent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ (KHTML, like Gecko) BingPreview/1.0b&RemoteIP=40.77.167.181"));
    ubiEvent.setUrlQueryString(
        "/itm/Nike-Mercurial-Lite-Shin-Guards-Small-Size-White-Black-Brand-New-/173926446112");
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
    ubiEvent.setGuid("bb9007991710a860aec14a80ffb5be52");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-27 05:15:10.855")));
    ubiEvent.setRemoteIP("10.188.85.241");
    ubiEvent.setRequestCorrelationId("cf6d84fae63729e4");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6qjpbq%3F%3Ctofthu%60t*004%3F%3B73%29pqtfwpu%29pie%29fgg%7E-fij-171bb900809-0x162");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("10.188.85.241");
    ubiEvent.setAgentInfo("eBayNioHttpClient 1.0,GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(557842958480L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AAAAgAAAIAQAAAAAAAAAAIAAAAAAAAAAAAQAAAAAQABAAAQAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAABAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB&c=1&g=bb9007991710a860aec14a80ffb5be52&h=99&px=4249&chnl=9&uc=1&es=3&nqt=AAAAgAAAIAQAAAAAAAAAAIAAAAAAAAAAAAQAAAAAQABAAAQAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAQEAAAAAABAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB&p=2317508&uaid=bb9008081710ac3e2817f031d03c2db4S0&bs=0&t=3&cflgs=QA**&ul=en-US&plmt=cw4AAB%252BLCAAAAAAAAADdl1tv2yAUgP8Lz1YGGIydag%252FdWk2Vtt6i7aWNKkKw48232OSu%252FPcdXCfNNiWrp1RT6wcDh%252BNzOB%252BHi1eoiFCXYiY8wR1UxKh7t0LxEHUJhsd3kIptP8WYEI8I4nOQlSBD2KMBVWrgSkaZ4sQnahDSgYtdj4VSCOQgbcBcH0r5aLaxDh9TMQsHqqhAKUXdbJIkDqryTa0A9ywIHBRCxUFxhborROEzQjgWaL0Gm3FRbq1hZCX5rnnjx2OsHs2jLM80%252Bs0Bqx0ktYNdNxj071b3wOAedbFzj5RMCxlH2YUVEOJSzlyGiQ9dg3h4rUulMyMjDb28w9d9cETqoW41QVIPngscQN21dSgZlKc3p%252Ben8JxfpoJd4enS9y7H8vayoCzKe%252BfvQY3XATrIs3F1BDv59OEamsKKT772zqDuNxYDKF3A42woLNzihwoaCkm%252BlwE9HgOv4740A99tw2CyGHledigTgoaC%252B4oywe3QoA2F2TIflMneTOAbBqwVA%252By7VHC8j4F4YvCkeUwGogUCHxPDR8lzEoEfLxHcXQgvsxwwbZMI89yMzPe9ieBttgTvzSaCmM19ltPnnA3iFe0ItMPaUQhnaSIOUKCb5eC%252FIgqk47U6IediEfr6AAW%252ByYXgmGcke%252BnTgbfaFGaz8TgKGwoy2ns6EPzGdgVwb2MI8DuKd4EYWg6HswZIUfwBhG6AtLs9%252Fl8gcHv8%252B82pIcJ%252FwTENBpx5B1aJu82Qo14lD20WAfcE%252FTcgZAskeM4ygX8LGddjsD9F1qecQvO6zNPc6GFPJ4kuqysz0uWF0Wn1rT5b4vpanc5t2ChXj2hkVTdlXiOqhhZRqZVMkl681N0v2siP0vTsr4ydAme387OW4bbT9lValmp0M9HlAiQgMLmRye32i2YawTvMyB2Sw6kuTVzBWEH3aR6g8ZCG%252BkFJA9Wiiaq2bwOrFao4ixJ9M5GZic0CBDBX8I4quzRik9q9I6lq2Sg3ma6qM2kk6lt2ytTBKggWr%252Fvrn2j1GUtzDgAA&ec=1&pn=2&rq=cf6d84fae63729e4&pagename=cos__mfe&po=%5B%28pg%3A2047675+pid%3A100008+pl%3A1+pladvids%3A%5B0%5D%29%5D&ciid=kAgI4oE*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dcf6d84fae63729e4%26node_id%3De6e905c8722f08be%26REQUEST_GUID%3D171bb900-8070-ac3e-2813-b7f3e71d7f85%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t%2A004%253F%253B73%2529pqtfwpu%2529pie%2529fgg%257E-fij-171bb900809-0x162%26cal_mod%3Dfalse&TPool=r1rover&TDuration=2&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=10.134.10.236&Script=/roverimp/0/2047675/9&Server=internal.rover.vip.ebay.com&TMachine=10.195.226.129&TStamp=05:15:10.85&TName=roverimp&Agent=eBayNioHttpClient 1.0,GingerClient/2.9.7-RELEASE&RemoteIP=10.188.85.241"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2047675/9?site=3&trknvp=plmt%3Dcw4AAB%252BLCAAAAAAAAADdl1tv2yAUgP8Lz1YGGIydag%252FdWk2Vtt6i7aWNKkKw48232OSu%252FPcdXCfNNiWrp1RT6wcDh%252BNzOB%252BHi1eoiFCXYiY8wR1UxKh7t0LxEHUJhsd3kIptP8WYEI8I4nOQlSBD2KMBVWrgSkaZ4sQnahDSgYtdj4VSCOQgbcBcH0r5aLaxDh9TMQsHqqhAKUXdbJIkDqryTa0A9ywIHBRCxUFxhborROEzQjgWaL0Gm3FRbq1hZCX5rnnjx2OsHs2jLM80%252Bs0Bqx0ktYNdNxj071b3wOAedbFzj5RMCxlH2YUVEOJSzlyGiQ9dg3h4rUulMyMjDb28w9d9cETqoW41QVIPngscQN21dSgZlKc3p%252Ben8JxfpoJd4enS9y7H8vayoCzKe%252BfvQY3XATrIs3F1BDv59OEamsKKT772zqDuNxYDKF3A42woLNzihwoaCkm%252BlwE9HgOv4740A99tw2CyGHledigTgoaC%252B4oywe3QoA2F2TIflMneTOAbBqwVA%252By7VHC8j4F4YvCkeUwGogUCHxPDR8lzEoEfLxHcXQgvsxwwbZMI89yMzPe9ieBttgTvzSaCmM19ltPnnA3iFe0ItMPaUQhnaSIOUKCb5eC%252FIgqk47U6IediEfr6AAW%252ByYXgmGcke%252BnTgbfaFGaz8TgKGwoy2ns6EPzGdgVwb2MI8DuKd4EYWg6HswZIUfwBhG6AtLs9%252Fl8gcHv8%252B82pIcJ%252FwTENBpx5B1aJu82Qo14lD20WAfcE%252FTcgZAskeM4ygX8LGddjsD9F1qecQvO6zNPc6GFPJ4kuqysz0uWF0Wn1rT5b4vpanc5t2ChXj2hkVTdlXiOqhhZRqZVMkl681N0v2siP0vTsr4ydAme387OW4bbT9lValmp0M9HlAiQgMLmRye32i2YawTvMyB2Sw6kuTVzBWEH3aR6g8ZCG%252BkFJA9Wiiaq2bwOrFao4ixJ9M5GZic0CBDBX8I4quzRik9q9I6lq2Sg3ma6qM2kk6lt2ytTBKggWr%252Fvrn2j1GUtzDgAA%26po%3D%5B%28pg%3A2047675+pid%3A100008+pl%3A1+pladvids%3A%5B0%5D%29%5D&trknvpsvc=%3Ca%3Enqc%3DAAAAgAAAIAQAAAAAAAAAAIAAAAAAAAAAAAQAAAAAQABAAAQAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAABAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB%26nqt%3DAAAAgAAAIAQAAAAAAAAAAIAAAAAAAAAAAAQAAAAAQABAAAQAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAQEAAAAAABAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB%26es%3D3%26ec%3D1%3C%2Fa%3E&tguid=bb9007991710a860aec14a80ffb5be52&imp=2317508");
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
    ubiEvent.setGuid("bb9007991710a860aec14a80ffb5be52");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent.setEventTimestamp(
        Long.parseLong(SojTimestamp.getSojTimestamp("2020-04-27 05:15:10.854")));
    ubiEvent.setRemoteIP("40.77.167.181");
    ubiEvent.setRequestCorrelationId("bb1878321710a4b5a344c3f3ff9425f4");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid(
        "t6pdhc9%3Ftilvgig%28uq%60b%3E*w%60ut3542-171bb900806-0xd522");
    ubiEvent.setEventFamily("SAND");
    ubiEvent.setEventAction("AUCT");
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("40.77.167.181");
    ubiEvent.setAgentInfo("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+"
        + " (KHTML, like Gecko) BingPreview/1.0b");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(71085918352L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "saucxgdpry=true&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid=a4d6fe979d83495c8247b8ad6348c0f1&g=bb9007991710a860aec14a80ffb5be52&saebaypid=100562&sapcxkw=Nike+Mercurial+Lite+Shin+Guards+Small+Size+White%2FBlack+Brand+New&h=99&schemaversion=3&salv=5&ciid=kAgNjRA*&p=2367355&sapcxcat=11450%2C260012%2C1059%2C11507&t=3&saiid=6f459cce-7d22-4bc1-b89f-3282a466429e&cflgs=AA**&samslid=&eactn=AUCT&pn=2&rq=bb1878321710a4b5a344c3f3ff9425f4&pagename=SandPage&ciid=kAgNjRA*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Dbb1878321710a4b5a344c3f3ff9425f4%26node_id%3D5fe43ee3e746263b%26REQUEST_GUID%3D171bb900-8060-aae8-d104-9904f965e73a%26logid%3Dt6pdhc9%253Ftilvgig%2528uq%2560b%253E%2Aw%2560ut3542-171bb900806-0xd522&TPool=r1sand&TDuration=7&ContentLength=1113&ForwardedFor=10.75.101.216&Script=sand&Server=sand.stratus.ebay.com&TMachine=10.174.141.16&TStamp=05:15:10.85&TName=sand.v1&Agent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ (KHTML, like Gecko) BingPreview/1.0b&RemoteIP=40.77.167.181"));
    ubiEvent.setUrlQueryString(
        "/itm/Nike-Mercurial-Lite-Shin-Guards-Small-Size-White-Black-Brand-New-/173926446112");
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

    /*
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
    sessionAccumulator = new SessionAccumulator();
    sessionMetrics = SessionMetrics.getInstance();
    sessionBotDetector = SessionBotDetector.getInstance();

  }

  @Test
  public void test1() throws Exception {
    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent ubiEvent : ubiEventList) {
      sessionMetrics.feed(ubiEvent, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Set<Integer> botFlagList
        = sessionBotDetector.getBotFlagList(sessionAccumulator.getUbiSession());
    Assert.assertEquals(true, botFlagList.contains(11));
    Assert.assertEquals(true, botFlagList.contains(206));
    Assert.assertEquals(true, botFlagList.contains(207));
    Assert.assertEquals(true, botFlagList.contains(215));

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
