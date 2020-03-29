package com.ebay.sojourner.ubd.common.model;

import com.ebay.sojourner.ubd.common.sharedlib.util.CalTimeOfDay;
import lombok.Data;

@Data
public class ClientData {

  private String forwardFor;
  private String script;
  private String server;
  private String tMachine;
  private String tStamp;
  private String tName;
  private String t;
  private String colo;
  private String pool;
  private String agent;
  private String remoteIP;
  private String tType;
  private String tPool;
  private String tStatus;
  private String corrId;
  private String contentLength;
  private String nodeId;
  private String requestGuid;
  private String urlQueryString;
  private String referrer;
  private String rlogid;
  private String acceptEncoding;
  private String tDuration;
  private String encoding;
  private String tPayload;

  @Override
  public String toString() {
    StringBuilder clientInfo = new StringBuilder();

    clientInfo.append("TPayload=").append(tPayload);

    if (tPool != null && !tPool.equals("")) {
      clientInfo.append("&TPool=").append(tPool);
    }
    if (tDuration != null && !tDuration.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("TDuration=").append(tDuration);
    }
    if (tStatus != null && !tStatus.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("TStatus=").append(tStatus);
    }
    if (tType != null && !tType.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("TType=").append(tType);
    }
    if (contentLength != null && !contentLength.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("ContentLength=").append(contentLength);
    }
    if (forwardFor != null && !forwardFor.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("ForwardedFor=").append(forwardFor);
    }
    if (script != null && !script.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("Script=").append(script);
    }
    if (server != null && !server.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("Server=").append(server);
    }
    if (tMachine != null && !tMachine.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("TMachine=").append(tMachine);
    }
    if (tStamp != null && !tStamp.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tstamp = toCALDateString(Long.valueOf(tStamp));
      clientInfo.append("TStamp=").append(tstamp);
    }
    if (tName != null && !tName.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("TName=").append(tName);
    }
    if (agent != null && !agent.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("Agent=").append(agent);
    }
    if (remoteIP != null && !remoteIP.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("RemoteIP=").append(remoteIP);
    }
    if (encoding != null && !encoding.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("Encoding=").append(encoding);
    }
    // Referer must be in the end of clientData since it has nested '&' '='
    if (referrer != null && !referrer.equals("") && !referrer.equalsIgnoreCase("null")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      clientInfo.append("Referer=").append(referrer);
    }
    return clientInfo.toString();
  }

  private String toCALDateString(long time) {
    CalTimeOfDay calTimeOfDay = new CalTimeOfDay(time);
    return calTimeOfDay.toString();
  }


}
