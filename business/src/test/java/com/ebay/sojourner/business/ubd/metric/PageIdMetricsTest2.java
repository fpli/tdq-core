package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.common.model.ClientData;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.SOJParseClientInfo;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PageIdMetricsTest2 {

  private List<UbiEvent> ubiEventList;
  private TimestampMetrics timestampMetrics;
  private UbiSession ubiSession;
  private SessionAccumulator sessionAccumulator;
  private SessionMetrics sessionMetrics;

  @Before
  public void setUp() throws Exception {
    ubiEventList = new ArrayList<>();
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2507874);
    ubiEvent.setEventTimestamp(3795064810698000L);
    ubiEvent.setRemoteIP("10.199.52.247");
    ubiEvent.setRequestCorrelationId("0b90ec52e782910d");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6wwm53vpd77%3C%3Dqkisqn47pse31%28%3F1062%3E"
        + "-vrubqst-whh-%60dfz%2Behn-1714affce4a-0xd5");
    ubiEvent.setEventFamily("PRLS");
    ubiEvent.setEventAction("ALGO");
    ubiEvent.setPartialValidPage(false);
    ubiEvent.setClientIP("10.199.52.247");
    ubiEvent.setAgentInfo("eBayNioHttpClient 1.0");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(46779518719L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("reco.vip");
    ubiEvent.setApplicationPayload("expl=0&meid=e7fe0beead184b3cba554b29db690978&res=-1x-1&flgs=AA"
        + "**&efam=PRLS&sv=8&g=4affcde01710ac734f70f1afff5eb980&h=e0&snappy=1&p=2507874&uaid=4affc"
        + "e0c1710a45e40a19fcafcea94c8S0&t=3&cflgs=AA**&plmt=100623&pl=2QTw6Iq5%2Fq%2BpXAIqU2ltcGx"
        + "BTUx2NVBhaXJ3aXNlV2ViAAYAApa6nKeJEQACQDRhZmZjZGUwMTcxMGFjNzM0ZjcwZjFhZmZmNWViOTgwAAACCl"
        + "VTOjo6AjQwLjEyNTc2LjQyODkyLjU3NTE2LjE4MTcwOQACGDIyNTA3NTo5NTM0NwACAgIedHJlZXBsZXgvMC4wL"
        + "jUxCAKmvNfgiREAhyFhp1h4MD8CAAAAgDHgEMAAAAAgheuxPwQzMQAAAgACAAYABAAAgD7MKww7AAAC6M%2FX4I"
        + "kRAII8OSaeCSY%2FAgAAAEDieBLAAAAAIIXrETcMAgICBgk3NMWNuzoAAAKO2r2UkgoAEQEBbhCg6mEVwDZuAAg"
        + "EAgQNNwkpMAKEj6yo%2BAgCjJmPnwEJEwAABTwMILmLFwE8FKCZmZk%2FBAWqCAYCBjI8AAEogEBcj8LvPwEMKE"
        + "VwaWRDYXNzaW5pUmV0cmlldmVyBDI0AA1VaPA%2FKlRvcmFJdGVtQ292aWV3c0xvb2t1cAQyMRkjABoNQREbADU"
        + "ZGyQySGlnaElkZk9yDSQZZQgzMQgVZQA6AWUJYQRPZgkIAXMRbQQzMhlSADAZKxhQcm9kdWN0FSYsMwABAAAAAA"
        + "AA8D8A&eactn=ALGO&algo=PL.SIM.SimplAMLv5PairwiseWeb&rq=0b90ec52e782910d&pooltype=prod&p"
        + "agename=PromotedListingsMLPipeline&ciid=%2F85G5Ao*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D0b90ec52e782910d%26node_id%3Df2271993e206ba4a%26REQUEST_GUI"
            + "D%3D1714affc-e090-a0f0-bc82-c5b2fd4715c1%26logid%3Dt6wwm53vpd77%253C%253Dqkisqn47pse"
            + "31%2528%253F1062%253E-vrubqst-whh-%2560dfz%252Behn-1714affce4a-0xd5%26cal_mod%3Dfals"
            + "e&TPool=r1plsim&TDuration=2&TStatus=0&TType=URL&ContentLength=1435&ForwardedFor=null"
            + "&Script=/trk20svc/TrackingResource/v1&Server=reco.vip&TMachine=10.69.228.10&TStamp=0"
            + "8:40:10.69&TName=Ginger.CollectionSvc.track&Agent=eBayNioHttpClient 1.0&RemoteIP=10."
            + "199.52.247&Encoding=null&Referer=null"));
    ubiEvent.setUrlQueryString(
        "/rec/plmt/100623?guid=4affcde01710ac734f70f1afff5eb980&itm=293306863243&bWidth=1015&fmt=ht"
            + "ml&locale=en-GB&srchCtxt=(dmLCat=-1|srCnt=0|mCCatId=0|minPrice=-1.0|maxPrice=-1.0|cr"
            + "ncyId=826|fShip=0|etrs=0)&usrSi=US&si=3&_qi=t6ulcpjqcj9?jqpsobtlrbn(046700-vrubqst-i"
            + "pt-`dfz+ehn&_usrip=66.249.79.37");
    ubiEvent.setPageName("Ginger.CollectionSvc.track");
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
        "saucxgdpry=true&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid=aba1bc1d538a41ffbc"
            + "6c135ae4110882&g=4affcde01710ac734f70f1afff5eb980&saebaypid=100938&sapcxkw=1pcs+Used"
            + "+Cermate+10.4-inch+PanelMaster+Touch+Screen+PL104-TST3A-F2R1&h=e0&schemaversion=3&sa"
            + "lv=5&ciid=%2F85x62I*&p=2367355&sapcxcat=12576%2C42892%2C57516%2C181709&t=3&saiid=120"
            + "638eb-409e-4c64-9551-0f47e8337aec&cflgs=gA**&samslid=&eactn=AUCT&pn=2&rq=f8e7978d9a5"
            + "23089&pagename=SandPage&ciid=%2F85x62I*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Df8e7978d9a523089%26node_id%3D85644d56e7645d3f%26REQUEST_GUI"
            + "D%3D1714affc-e6a0-a16e-b627-dc42eec06e87%26logid%3Dt6pdhc9%253Fuk%2560vgig%25285226%"
            + "253E00-1714affce6a-0x103c0&TPool=r1sand&TDuration=6&ContentLength=1113&ForwardedFor="
            + "10.202.81.132&Script=sand&Server=sand.stratus.ebay.com&TMachine=10.22.235.98&TStamp="
            + "08:40:10.73&TName=sand.v1&Agent=Mozilla/5.0 (compatible; Googlebot/2.1; +http://www."
            + "google.com/bot.html)&RemoteIP=66.249.79.37"));
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
    ubiEvent.setCurrentImprId(424856833791L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "saucxgdpry=true&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid=a1f5bf7abbbe48138"
            + "5918a227734e91d&g=4affcde01710ac734f70f1afff5eb980&saebaypid=100562&sapcxkw=1pcs+Us"
            + "ed+Cermate+10.4-inch+PanelMaster+Touch+Screen+PL104-TST3A-F2R1&h=e0&schemaversion=3"
            + "&salv=5&ciid=%2F85w62I*&p=2367355&sapcxcat=12576%2C42892%2C57516%2C181709&t=3&saiid"
            + "=0f674689-cab2-45e9-a85d-44ed1d7fa6d1&cflgs=gA**&samslid=&eactn=AUCT&pn=2&rq=f8e797"
            + "8d9a523089&pagename=SandPage&ciid=%2F85w62I*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Df8e7978d9a523089%26node_id%3D85644d56e7645d3f%26REQUEST_GUI"
            + "D%3D1714affc-e6a0-a16e-b627-dc42eec06e87%26logid%3Dt6pdhc9%253Fuk%2560vgig%25285226%"
            + "253E00-1714affce6a-0x103c0&TPool=r1sand&TDuration=6&ContentLength=1113&ForwardedFor="
            + "10.202.81.132&Script=sand&Server=sand.stratus.ebay.com&TMachine=10.22.235.98&TStamp="
            + "08:40:10.73&TName=sand.v1&Agent=Mozilla/5.0 (compatible; Googlebot/2.1; +http://www."
            + "google.com/bot.html)&RemoteIP=66.249.79.37"));
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
    ubiEvent.setCurrentImprId(424856637183L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags("AA**");
    ubiEvent.setWebServer("sand.stratus.ebay.com");
    ubiEvent.setApplicationPayload(
        "saucxgdpry=true&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid=e139a5ced4a94bccab"
            + "e9ce26521aec74&g=4affcde01710ac734f70f1afff5eb980&saebaypid=100939&sapcxkw=1pcs+Used"
            + "+Cermate+10.4-inch+PanelMaster+Touch+Screen+PL104-TST3A-F2R1&h=e0&schemaversion=3&sa"
            + "lv=5&ciid=%2F85t62I*&p=2367355&sapcxcat=12576%2C42892%2C57516%2C181709&t=3&saiid=3c5"
            + "9be9b-5ad9-47d8-99c9-24e6aad184e6&cflgs=gA**&samslid=&eactn=AUCT&pn=2&rq=f8e7978d9a5"
            + "23089&pagename=SandPage&ciid=%2F85t62I*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3Df8e7978d9a523089%26node_id%3D85644d56e7645d3f%26REQUEST_GUI"
            + "D%3D1714affc-e6a0-a16e-b627-dc42eec06e87%26logid%3Dt6pdhc9%253Fuk%2560vgig%25285226%"
            + "253E00-1714affce6a-0x103c0&TPool=r1sand&TDuration=5&ContentLength=1113&ForwardedFor="
            + "10.202.81.132&Script=sand&Server=sand.stratus.ebay.com&TMachine=10.22.235.98&TStamp="
            + "08:40:10.73&TName=sand.v1&Agent=Mozilla/5.0 (compatible; Googlebot/2.1; +http://www."
            + "google.com/bot.html)&RemoteIP=66.249.79.37"));
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
    ubiEvent.setEventTimestamp(3795064810744000L);
    ubiEvent.setRemoteIP("10.173.125.154");
    ubiEvent.setRequestCorrelationId("57d752ae069e619c");
    ubiEvent.setSid(null);
    ubiEvent.setRlogid("t6qjpbq%3F%3Ctofthu%60t*0207311-1714affce7a-0x199");
    ubiEvent.setEventFamily(null);
    ubiEvent.setEventAction(null);
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setClientIP("66.249.79.37");
    ubiEvent.setAgentInfo(
        "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html),"
            + "GingerClient/2.9.7-RELEASE");
    ubiEvent.setCobrand(0);
    ubiEvent.setCurrentImprId(1046872706815L);
    ubiEvent.setEventAttr(null);
    ubiEvent.setReservedForFuture(0);
    ubiEvent.setSqr(null);
    ubiEvent.setFlags(null);
    ubiEvent.setWebServer("internal.rover.vip.ebay.com");
    ubiEvent.setApplicationPayload(
        "nqc=AA**&c=1&g=4affcde01710ac734f70f1afff5eb980&h=e0&px=4249&chnl=9&uc=1&es=3&nqt=AAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAABA**&p=2317508&uaid=4affce791710a99bef31e3f5fc71058cS0&bs=0&t=3&cflgs=wA**&u"
            + "l=en-US&plmt=py0AAB%252BLCAAAAAAAAADtWllz2zgS%252FisuTtU%252B%252BcBJAH6zPclMtpKNN3L"
            + "mZWoq1bgsriiSISl5PKn8921Qhw%252FFEztxErvGfpCIbrCB7v76wyF%252FyJrTbJ8RoXIlt7OmyPZ%252"
            + "F%252F5AVPtunhBhutjNXJD3VRFBGCSWMbGctyrJAuQHpghdghHUObDAusFwyCsEpkW1noUdzf%252BA3LMw"
            + "urePL%252FP371hE%252Bx07TbL%252BaleV21tWrpybbF4xrHD3iRD6ihXphARXZ%252BNy2OEGcGTYo5xI"
            + "fe1R8yCCpRw4qD2XSQxo%252Bo0yqHJuCacPwWypJU5tqqojJ0HiR%252FM2Y4ZzkOudMcFQXfTLfuG7rbRf"
            + "81lFop9CHLUp2xU5RufHWMVShfAVdH9qtk3qGkpFrQ6i2jl9SInZORif8YOc5e0OTsTSzYabO1T5g45fD4yR"
            + "H%252FzKLD%252FUwGj5Mzu5x2Nnd0lQ0aZokzcu5dvlYNbCc2qlvWpxUu2w25bQfArdACkrApZSEP3t8np%"
            + "252Bl2ItkQaaPPH0oksJ92k5v1M3PiFwqU9Zjis88tOcv67M0QOmutH2JlrJyeB6nbF%252FSzYv66rv%252"
            + "Bqt63p1faMfkVoexC9hGdGwDVFX%252BF9D0OxekYXzeI%252FbPC9%252BNsXzGN3arQn9Xt5EUKA9dCDeO"
            + "8rYp%252BkAQL57uzye68aPagj%252B986CZ93aTYzWxZdOOT8wbtM7QDzaSojq9JuwWU%252Fz0a5rJsdL%"
            + "252BFtivqKiF2l%252B6m%252FM3aElt7e0W7m4bseugLt%252Bvq6Z5r9%252BZ7ju51bm%252B0MrYzvLY"
            + "7Lard%252F3XJ1%252BRlN5TXTX5%252BUmMUSZXZhtiGbvxzKOE82%252BcE%252F9bCo1SRSCtF9%252BI"
            + "AnejbWUiNl%252FAXBh0wREO8UVT9VoSzN4uXLvr9Mho9q8CWwa9koZqjp01b%252B5nrUxCw9As3SQC%252"
            + "BDdA7DMHBkJ%252B%252FzU5X9CElkH9MDkLiKyklGVgD5iuSmqKc4tef2T56XLuhBd3QgnpodX7IXIDWjf8"
            + "7Q6ihpRTxOqaCS8ZdP3R02JGkOK%252BZV19nXmKoXDIvWKDWUS%252B5BkFjtC53lEsIglKiNXti3gfBvHd"
            + "I09cwr74f5kVKuiDeSzR80XOQU%252FJEz0%252F0%252FAX0fItqeDT0rFi%252BpmdFGM2R6QjJl%252FR"
            + "MJFI1pbnxjIloc3CUCZmrQLT11NlvRc%252BugxU3E87X3DwO4EN7VJd1m0r3tIXzdwtZquElWjKY9XXinNn"
            + "UhvZ1PPAJcgY7lEWV4MdxyLrqn8O0KFOcDtpioPokHCFCT4q%252BxLKg%252FEL0c%252BhcWzQDEPYpu6S"
            + "op1BUL4tqspC7NLfBwEKW%252FZRgenSULVWX%252Byed1oSsdCeJ9JZvDNKqHiy9rdDBNPc1ivskPqzLNVq"
            + "RXBCdUI4acEWFdJMW2PTKr0M1XZl%252BOvVcUl2aUNIMMznwo9BAC32Kc3Y2RpQmQPZ9W9hZsrIc5zAkRkQ"
            + "YrdeeNaFhBo%252FhNLweBu2GhdUnwQBaJKWE%252Bt%252BHytiZTXawMnYITUl8vwTvvaxeW7%252B%252"
            + "BerH1r63XDepeVNg9gguL17sFUYcuzTuxP7o%252Bhgo12J7G0C9WoHFqhlT9fh2oA1vPryJikYQFcx2hu32"
            + "LXcMF65wmK7NJNgR%252BcgLt6cAj72wJVZK2dVmiTYz7IbjJaVvPKr9EefbTs8Pn6lk6k7hZ19fTg4ssLAE"
            + "zanDIusVQpdklx%252FowbUpYUAuGu%252BvPy%252BEZqynXRuVapppqkN7rCkqEsR%252BKZJgtZnAahiX"
            + "5FsW%252FDNliHcmG%252FH7HxeTHc6fa4E7BV9yZG8mJZ0aQQAUVYHEHLy1uDD1ECpb%252FWO4civqJPJ%"
            + "252FI84k8vwl53qL6%252F9nkKXO5Jk9cZTjBMPE1eTKnA%252Fc5MqwWgjurg7AYNkttUEE7da%252FkeXv"
            + "3ye3c%252F1u3xYbbTKzcDpFFQXMfLdEieg4MPY4qB2AeUPl0HfIgrkPukKYvvw5BpHzddcgnLj8e4E0HHjf"
            + "Xuw8809%252FpquPo1fHbh3%252FHcd3DL7vKuOMN8ucReuMVxTKqD2GJUBtcKdnq6lgR3HK7wHWMuaDWQgh"
            + "Wcqejid4bbb4VV%252FrY%252FAchgbuYomtS%252FhasOdzKP3HmjZx5h3R9DWeqK5x5I%252BtweVGSSt6"
            + "JdN6cvHqXG%252FMJ3vlhDHPNmz9uzRcPocbzjRoX6cf6xf2jotGrqCxCREiOoLGKaqWC5EZ7Hx7tNtAwdul"
            + "XMdwBUiG0pCu3g3fgIsfdLhfGgwWn85w5o7XXOj7e3a9hfMNtQ%252FXSbWFzr6WmVEUr0GHQUXAwVkdHARx"
            + "5xG6TDbcVXy1kQTktpVeUiyDwdGOsyVmMkDOBn%252BR%252BF7Lv6zbdBDkVq2wHmXOwiGiqBckjKMMMODw"
            + "hezwSQXy0bsucbVAaXf%252BkAjRK3InhWdYGjAbXEgsAGFMK02%252BofzriPYjtyh3S9DXbFfb5I94%252"
            + "F9nT3dLi76ffnz2PzxsPdQznbDav%252BtbUhF2xJkuiUByPAayCoIbgnCDQQ6RgNgUb3aNcGQ83mTmC9NsT"
            + "AJGcePfRcKGHABsVyoLkzTFpyv%252F%252BHWjTt%252Bl2S%252FTgc5BsBEVKtFkvGDaUszz1KgRmjWOT"
            + "eBWEssYbe733o98WB2oQ%252FWf0%252FssMtfzQaAjgqNOFYByxqL4UOLHgGj8xtbNvFvkBnH%252F8Pq3e"
            + "P5actAAA%253D&ec=1&pn=2&rq=57d752ae069e619c&pagename=cos__mfe&po=%5B%28pg%3A2047675+"
            + "pid%3A100939%29%28pg%3A2047675+pid%3A100938%29%28pg%3A2047675+pid%3A100726%29%28pg%3"
            + "A2047675+pid%3A100727%29%28pg%3A2047675+pid%3A100565%29%28pg%3A2047675+pid%3A100564%"
            + "29%28pg%3A2047675+pid%3A100567%29%28pg%3A2047675+pid%3A100566%29%28pg%3A2047675+pid%"
            + "3A100922%29%28pg%3A2047675+pid%3A100923%29%28pg%3A2047675+pid%3A100920%29%28pg%3A204"
            + "7675+pid%3A100921%29%28pg%3A2047675+pid%3A100562%29%28pg%3A2047675+pid%3A100918%29%2"
            + "8pg%3A2047675+pid%3A100919%29%28pg%3A2047675+pid%3A100916%29%28pg%3A2047675+pid%3A10"
            + "0917%29%5D&ciid=%2F855vvM*");
    ubiEvent.setClientData(constructClientData(
        "TPayload=corr_id_%3D57d752ae069e619c%26node_id%3Ddeb34b6cb870b3e1%26REQUEST_GUI"
            + "D%3D1714affc-e780-a99b-ef34-993dfe21c3ef%26logid%3Dt6qjpbq%253F%253Ctofthu%2560t%2A0"
            + "207311-1714affce7a-0x199%26cal_mod%3Dfalse&TPool=r1rover&TDuration=3&TStatus=0&TType"
            + "=URL&ContentLength=-1&ForwardedFor=66.249.79.37&Script=/roverimp/0/2047675/9&Server="
            + "internal.rover.vip.ebay.com&TMachine=10.153.190.243&TStamp=08:40:10.74&TName=roverim"
            + "p&Agent=Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html),Gin"
            + "gerClient/2.9.7-RELEASE&RemoteIP=10.173.125.154"));
    ubiEvent.setUrlQueryString(
        "/roverimp/0/2047675/9?site=3&trknvp=plmt%3Dpy0AAB%252BLCAAAAAAAAADtWllz2zgS%252FisuTtU%252"
            + "B%252BcBJAH6zPclMtpKNN3LmZWoq1bgsriiSISl5PKn8921Qhw%252FFEztxErvGfpCIbrCB7v76wyF%252"
            + "FyJrTbJ8RoXIlt7OmyPZ%252F%252F5AVPtunhBhutjNXJD3VRFBGCSWMbGctyrJAuQHpghdghHUObDAusFw"
            + "yCsEpkW1noUdzf%252BA3LMwurePL%252FP371hE%252Bx07TbL%252BaleV21tWrpybbF4xrHD3iRD6ihXp"
            + "hARXZ%252BNy2OEGcGTYo5xIfe1R8yCCpRw4qD2XSQxo%252Bo0yqHJuCacPwWypJU5tqqojJ0HiR%252FM2"
            + "Y4ZzkOudMcFQXfTLfuG7rbRf81lFop9CHLUp2xU5RufHWMVShfAVdH9qtk3qGkpFrQ6i2jl9SInZORif8YOc"
            + "5e0OTsTSzYabO1T5g45fD4yRH%252FzKLD%252FUwGj5Mzu5x2Nnd0lQ0aZokzcu5dvlYNbCc2qlvWpxUu2w"
            + "25bQfArdACkrApZSEP3t8np%252Bl2ItkQaaPPH0oksJ92k5v1M3PiFwqU9Zjis88tOcv67M0QOmutH2JlrJ"
            + "yeB6nbF%252FSzYv66rv%252Bqt63p1faMfkVoexC9hGdGwDVFX%252BF9D0OxekYXzeI%252FbPC9%252BN"
            + "sXzGN3arQn9Xt5EUKA9dCDeO8rYp%252BkAQL57uzye68aPagj%252B986CZ93aTYzWxZdOOT8wbtM7QDzaS"
            + "ojq9JuwWU%252Fz0a5rJsdL%252BFtivqKiF2l%252B6m%252FM3aElt7e0W7m4bseugLt%252Bvq6Z5r9%2"
            + "52BZ7ju51bm%252B0MrYzvLY7Lard%252F3XJ1%252BRlN5TXTX5%252BUmMUSZXZhtiGbvxzKOE82%252Bc"
            + "E%252F9bCo1SRSCtF9%252BIAnejbWUiNl%252FAXBh0wREO8UVT9VoSzN4uXLvr9Mho9q8CWwa9koZqjp01"
            + "b%252B5nrUxCw9As3SQC%252BDdA7DMHBkJ%252B%252FzU5X9CElkH9MDkLiKyklGVgD5iuSmqKc4tef2T5"
            + "6XLuhBd3QgnpodX7IXIDWjf87Q6ihpRTxOqaCS8ZdP3R02JGkOK%252BZV19nXmKoXDIvWKDWUS%252B5BkF"
            + "jtC53lEsIglKiNXti3gfBvHdI09cwr74f5kVKuiDeSzR80XOQU%252FJEz0%252F0%252FAX0fItqeDT0rFi"
            + "%252BpmdFGM2R6QjJl%252FRMJFI1pbnxjIloc3CUCZmrQLT11NlvRc%252BugxU3E87X3DwO4EN7VJd1m0r"
            + "3tIXzdwtZquElWjKY9XXinNnUhvZ1PPAJcgY7lEWV4MdxyLrqn8O0KFOcDtpioPokHCFCT4q%252BxLKg%25"
            + "2FEL0c%252BhcWzQDEPYpu6Sop1BUL4tqspC7NLfBwEKW%252FZRgenSULVWX%252Byed1oSsdCeJ9JZvDNK"
            + "qHiy9rdDBNPc1ivskPqzLNVqRXBCdUI4acEWFdJMW2PTKr0M1XZl%252BOvVcUl2aUNIMMznwo9BAC32Kc3Y"
            + "2RpQmQPZ9W9hZsrIc5zAkRkQYrdeeNaFhBo%252FhNLweBu2GhdUnwQBaJKWE%252Bt%252BHytiZTXawMnY"
            + "ITUl8vwTvvaxeW7%252B%252BerH1r63XDepeVNg9gguL17sFUYcuzTuxP7o%252Bhgo12J7G0C9WoHFqhlT"
            + "9fh2oA1vPryJikYQFcx2hu32LXcMF65wmK7NJNgR%252BcgLt6cAj72wJVZK2dVmiTYz7IbjJaVvPKr9Eefb"
            + "Ts8Pn6lk6k7hZ19fTg4ssLAEzanDIusVQpdklx%252FowbUpYUAuGu%252BvPy%252BEZqynXRuVapppqkN7"
            + "rCkqEsR%252BKZJgtZnAahiX5FsW%252FDNliHcmG%252FH7HxeTHc6fa4E7BV9yZG8mJZ0aQQAUVYHEHLy1"
            + "uDD1ECpb%252FWO4civqJPJ%252FI84k8vwl53qL6%252F9nkKXO5Jk9cZTjBMPE1eTKnA%252Fc5MqwWgju"
            + "rg7AYNkttUEE7da%252FkeXv3ye3c%252F1u3xYbbTKzcDpFFQXMfLdEieg4MPY4qB2AeUPl0HfIgrkPukKY"
            + "vvw5BpHzddcgnLj8e4E0HHjfXuw8809%252FpquPo1fHbh3%252FHcd3DL7vKuOMN8ucReuMVxTKqD2GJUBt"
            + "cKdnq6lgR3HK7wHWMuaDWQghWcqejid4bbb4VV%252FrY%252FAchgbuYomtS%252FhasOdzKP3HmjZx5h3R"
            + "9DWeqK5x5I%252BtweVGSSt6JdN6cvHqXG%252FMJ3vlhDHPNmz9uzRcPocbzjRoX6cf6xf2jotGrqCxCREi"
            + "OoLGKaqWC5EZ7Hx7tNtAwdulXMdwBUiG0pCu3g3fgIsfdLhfGgwWn85w5o7XXOj7e3a9hfMNtQ%252FXSbWF"
            + "zr6WmVEUr0GHQUXAwVkdHARx5xG6TDbcVXy1kQTktpVeUiyDwdGOsyVmMkDOBn%252BR%252BF7Lv6zbdBDk"
            + "Vq2wHmXOwiGiqBckjKMMMODwhezwSQXy0bsucbVAaXf%252BkAjRK3InhWdYGjAbXEgsAGFMK02%252Bofzr"
            + "iPYjtyh3S9DXbFfb5I94%252F9nT3dLi76ffnz2PzxsPdQznbDav%252BtbUhF2xJkuiUByPAayCoIbgnCDQ"
            + "Q6RgNgUb3aNcGQ83mTmC9NsTAJGcePfRcKGHABsVyoLkzTFpyv%252F%252BHWjTt%252Bl2S%252FTgc5Bs"
            + "BEVKtFkvGDaUszz1KgRmjWOTeBWEssYbe733o98WB2oQ%252FWf0%252FssMtfzQaAjgqNOFYByxqL4UOLHg"
            + "Gj8xtbNvFvkBnH%252F8Pq3eP5actAAA%253D%26po%3D%5B%28pg%3A2047675+pid%3A100939%29%28pg"
            + "%3A2047675+pid%3A100938%29%28pg%3A2047675+pid%3A100726%29%28pg%3A2047675+pid%3A10072"
            + "7%29%28pg%3A2047675+pid%3A100565%29%28pg%3A2047675+pid%3A100564%29%28pg%3A2047675+pi"
            + "d%3A100567%29%28pg%3A2047675+pid%3A100566%29%28pg%3A2047675+pid%3A100922%29%28pg%3A2"
            + "047675+pid%3A100923%29%28pg%3A2047675+pid%3A100920%29%28pg%3A2047675+pid%3A100921%29"
            + "%28pg%3A2047675+pid%3A100562%29%28pg%3A2047675+pid%3A100918%29%28pg%3A2047675+pid%3A"
            + "100919%29%28pg%3A2047675+pid%3A100916%29%28pg%3A2047675+pid%3A100917%29%5D&trknvpsvc"
            + "=%3Ca%3Enqc%3DAA**%26nqt%3DAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABA**%26es%3D3%26ec%3D1%3C%2Fa%3E&tguid=4a"
            + "ffcde01710ac734f70f1afff5eb980&imp=2317508");
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
