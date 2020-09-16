package com.ebay.sojourner.business.util;

import com.ebay.sojourner.common.model.ClientData;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.RheosHeader;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class InitRawEvent {

  private static RawEvent rawEvent;
  private static RheosHeader rheosHeader;
  private static ClientData clientData;

  public static RawEvent initRawEvent(HashMap m, String parser) {

    if (m.isEmpty() && StringUtils.isBlank(parser)) {
      return null;
    }

    if (parser.contains("agent")) {
      clientData = new ClientData();
      rawEvent = new RawEvent();

      HashMap<String, Object> clientDataMap =
          TypeTransUtil.ObjectToHashMap(m.get(ParserConstants.CLIENTDATA));
      clientData.setAgent(TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.AGENT)));

      rawEvent.setClientData(clientData);
    } else if (parser.contains("click")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("ciid")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("flag")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("identity")) {
      clientData = new ClientData();
      rawEvent = new RawEvent();

      HashMap<String, Object> clientDataMap =
          TypeTransUtil.ObjectToHashMap(m.get(ParserConstants.CLIENTDATA));
      clientData.setUrlQueryString(
          TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.URLQUERYSTRING)));
      clientData.setTName(TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.TNAME)));

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
      rawEvent.setClientData(clientData);
    } else if (parser.contains("item")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("pageId")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("cookies")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("rdt")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("referer")) {
      rawEvent = new RawEvent();
      clientData = new ClientData();

      HashMap<String, Object> clientDataMap =
          TypeTransUtil.ObjectToHashMap(m.get(ParserConstants.CLIENTDATA));
      clientData.setReferrer(
          TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.REFERRER)));

      rawEvent.setClientData(clientData);
    } else if (parser.contains("referrerHash")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("regu")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("server")) {
      rawEvent = new RawEvent();
      clientData = new ClientData();

      HashMap<String, Object> clientDataMap =
          TypeTransUtil.ObjectToHashMap(m.get(ParserConstants.CLIENTDATA));
      clientData.setServer(TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.SERVER)));

      rawEvent.setClientData(clientData);
    } else if (parser.contains("siid")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("site")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("appid")) {
      rawEvent = new RawEvent();
      clientData = new ClientData();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      HashMap<String, Object> clientDataMap =
          TypeTransUtil.ObjectToHashMap(m.get(ParserConstants.CLIENTDATA));
      clientData.setUrlQueryString(
          TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.URLQUERYSTRING)));
      clientData.setReferrer(
          TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.REFERRER)));
      clientData.setAgent(TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.AGENT)));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
      rawEvent.setClientData(clientData);
    } else if (parser.contains("finding")) {
      rawEvent = new RawEvent();
    } else if (parser.contains("iframe")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("sqr")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("clientIP")) {
      rawEvent = new RawEvent();
      clientData = new ClientData();

      HashMap<String, Object> clientDataMap =
          TypeTransUtil.ObjectToHashMap(m.get(ParserConstants.CLIENTDATA));
      clientData.setRemoteIP(
          TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.REMOTEIP)));
      clientData.setForwardFor(
          TypeTransUtil.ObjectToString(clientDataMap.get(ParserConstants.FORWARDFOR)));

      rawEvent.setClientData(clientData);
    } else if (parser.contains("userId")) {
      rawEvent = new RawEvent();

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
    } else if (parser.contains("staticPage")) {
      rawEvent = new RawEvent();
    } else if (parser.contains("partial")) {
      rawEvent = new RawEvent();
    } else if (parser.contains("cobrand")) {
      rawEvent = new RawEvent();
    } else if (parser.contains("timestamp")) {
      rawEvent = new RawEvent();
      rheosHeader = new RheosHeader();

      HashMap<String, Object> rheosHeaderMap =
          TypeTransUtil.ObjectToHashMap(m.get(ParserConstants.RHEOSHEADER));
      rheosHeader.setEventCreateTimestamp(
          TypeTransUtil.ObjectToLong(rheosHeaderMap.get(ParserConstants.EVENTCREATETIMESTAMP)));

      HashMap<String, String> sojaMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJA));
      HashMap<String, String> sojcMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJC));
      HashMap<String, String> sojkMap = TypeTransUtil.ObjectToHashMap1(m.get(ParserConstants.SOJK));

      rawEvent.setSojA(sojaMap);
      rawEvent.setSojC(sojcMap);
      rawEvent.setSojK(sojkMap);
      rawEvent.setRheosHeader(rheosHeader);
    }

    return rawEvent;
  }
}
