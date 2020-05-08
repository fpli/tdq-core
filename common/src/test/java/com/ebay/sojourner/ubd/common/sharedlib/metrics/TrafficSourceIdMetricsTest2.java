package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.rule.BotRule11;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJParseClientInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TrafficSourceIdMetricsTest2 {

  private List<UbiEvent> ubiEventList;
  private TimestampMetrics timestampMetrics;
  private UbiSession ubiSession;
  private SessionAccumulator sessionAccumulator;
  private SessionMetrics sessionMetrics;

  @Before
  public void setUp() throws Exception {
    ubiEventList = new ArrayList<>();

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("bb688c061710a6e5b2863a54c0b1ccc2");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(true);
    ubiEvent.setPageId(3084);
    ubiEvent.setEventTimestamp(3796950723201000L);
    ubiEvent.setRemoteIP("72.14.199.71");
    ubiEvent.setRequestCorrelationId("5efe69464a4e70f0");
    ubiEvent.setSid(null);
    ubiEvent
        .setRlogid("t6qjpbq%3F%3Cumjthu%60t*q2%7Caw%28rbpv6710-171bb688c06-0x125");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("72.14.199.71");
    ubiEvent
        .setAgentInfo(
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)Mozilla/5.0 "
                + "(iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML,"
                + " like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible;"
                + " AdsBot-Google-Mobile; +http://www.google.com/mobile/adsbot.html)");
    ubiEvent.setCobrand(7);
    ubiEvent.setCurrentImprId(173325847656L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("rover.ebay.com");
    ubiEvent.setApplicationPayload(
        "rdt=1&c=1&rvrrefts=bb688c071710a6e5b281758efeb7c74e&g=bb688c061710a6e5b2863a54c0b1ccc2"
            + "&nid=&h=06&cguidsrc=new&n=bb688c061710a6e5b2863a54c0b1ccc0&uc=1&url_mpre=https%3A"
            + "%2F%2Fwww.ebay.co.uk%2Fsch%2Fi.html%3F_nkw%3D"
            + ".22+Exploding+Pellets&p=3084&uaid=bb688c061710a6e5b2863a54c0b1ccc1S0&bs=0&rvrid"
            + "=2395692177655&t=0&cflgs=wA**&ul=en-US&hrc=301&pn=2&rq=5efe69464a4e70f0&pagename"
            + "=EntryTracking&ciid=aIwGWyg*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D5efe69464a4e70f0%26node_id%3D3474eb409176b632%26REQUEST_GUID"
            + "%3D171bb688-c010-a6e5-b285-ee7ade722385%26logid%3Dt6qjpbq%253F%253Cumjthu%2560t"
            + "%2Aq2%257Caw%2528rbpv6710-171bb688c06-0x125%26cal_mod%3Dfalse&TPool=r1rover"
            + "&TDuration=7&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=72.14.199"
            + ".71&Script=/rover/1/710-154084-954045-4/2&Server=rover.ebay.com&TMachine=10.110.91"
            + ".40&TStamp=04:32:03.20&TName=rover&Agent=Mozilla/5.0 (iPhone;"
            + " CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko)"
            + " Version/9.0 Mobile/13B143 Safari/601.1 (compatible; AdsBot-Google-Mobile;"
            + " +http://www.google.com/mobile/adsbot.html)&RemoteIP=72.14.199.71&Encoding=gzip,"
            + "deflate,br"));
    ubiEvent.setUrlQueryString(
        "/rover/1/710-154084-954045-4/2?gclid"
            + "=EAIaIQobChMIwPu5t4qs3AIVAQAAAB0BAAAAEAAYACAAEgJVzfD_BwE&geo_id=32251&MT_ID=584476"
            + "&cmpgn=1583412208&crlp=432947547545_&keyword="
            + ".22+exploding+pellets&rlsatarget=kwd-312361277726&abcId=1139966&loc=1007032"
            + "&sitelnk=&mkrid=710-154084-954045-4&poi=&mkevt=1&adpos=&device=m&mkcid=2&crdt=0"
            + "&mpre=https%3A%2F%2Fwww.ebay.co.uk%2Fsch%2Fi.html%3F_nkw%3D.22+Exploding+Pellets");
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
    timestampMetrics = new TimestampMetrics();
    ubiSession = new UbiSession();
    ubiSession.setTrafficSrcId(10);
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
    Assert.assertEquals(ubiSession.getTrafficSrcId(),
        sessionAccumulator.getUbiSession().getTrafficSrcId());
    UbiSession ubiSession = new UbiSession();
    ubiSession.setUserAgent(
        "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835"
            + ".202 Safari/535.1 google_partner_monitoring JPWzRTQUIz15879940557413T");
    ubiSession.setNonIframeRdtEventCnt(1);
    BotRule11 botRule11 = new BotRule11();
    botRule11.init();
    System.out.println(botRule11.getBotFlag(ubiSession));
    Map<String, Integer> aa = new HashMap<>();
    aa.put(null,1);
    System.out.println(aa.get(null));
    Map<String, Integer> bb = new ConcurrentHashMap<>();
    bb.put("",1);
    System.out.println(bb.get(""));
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
