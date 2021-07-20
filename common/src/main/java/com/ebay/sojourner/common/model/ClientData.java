package com.ebay.sojourner.common.model;

import com.ebay.sojourner.common.util.CalTimeOfDay;
import com.ebay.sojourner.common.util.PropertyUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientData implements Serializable {
  // TODO through reflection
  public static Set<String> FIELDS = Sets.newHashSet(
      "forwardFor", "script", "server", "TMachine",
      "TStamp", "TName", "t", "colo", "pool", "agent", "remoteIP", "TType",
      "TPool", "TStatus", "corrId", "contentLength", "nodeId",
      "requestGuid", "urlQueryString", "referrer", "rlogid",
      "acceptEncoding", "TDuration", "encoding", "TPayload"
  );

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
  private String corrId = "";
  private String contentLength;
  private String nodeId = "";
  private String requestGuid = "";
  private String urlQueryString;
  private String referrer;
  private String rlogid = "";
  private String acceptEncoding;
  private String tDuration;
  private String encoding;
  private String tPayload;

  @Override
  public String toString() {
    StringBuilder clientInfo = new StringBuilder(1000);

    clientInfo.append("TPayload=").append(tPayload);

    if (tPool != null && !tPool.equals("")) {
      String tagValue = PropertyUtils.encodeValue(tPool);
      clientInfo.append("&TPool=").append(tagValue);
    }
    if (tDuration != null && !tDuration.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(tDuration);
      clientInfo.append("TDuration=").append(tagValue);

    }
    if (tStatus != null && !tStatus.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(tStatus);
      clientInfo.append("TStatus=").append(tagValue);
    }
    if (tType != null && !tType.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(tType);
      clientInfo.append("TType=").append(tagValue);
    }
    if (contentLength != null && !contentLength.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(contentLength);
      clientInfo.append("ContentLength=").append(tagValue);
    }
    if (forwardFor != null && !forwardFor.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(forwardFor);
      clientInfo.append("ForwardedFor=").append(tagValue);
    }
    if (script != null && !script.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(script);
      clientInfo.append("Script=").append(tagValue);
    }
    if (server != null && !server.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(server);
      clientInfo.append("Server=").append(tagValue);
    }
    if (tMachine != null && !tMachine.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(tMachine);
      clientInfo.append("TMachine=").append(tagValue);
    }
    if (tStamp != null && !tStamp.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(tStamp);
      String tstamp = toCALDateString(Long.valueOf(tagValue));
      clientInfo.append("TStamp=").append(tstamp);
    }
    if (tName != null && !tName.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(tName);
      clientInfo.append("TName=").append(tagValue);
    }
    if (agent != null && !agent.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(agent);
      clientInfo.append("Agent=").append(tagValue);
    }
    if (remoteIP != null && !remoteIP.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(remoteIP);
      clientInfo.append("RemoteIP=").append(tagValue);
    }
    if (encoding != null && !encoding.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(encoding);
      clientInfo.append("Encoding=").append(tagValue);
    }
    // Referer must be in the end of clientData since it has nested '&' '='
    // if (referrer != null && !referrer.equals("") && !referrer.equalsIgnoreCase("null")) {
    if (referrer != null && !referrer.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(referrer);
      clientInfo.append("Referer=").append(tagValue);
    }

    if (corrId != null && !corrId.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(corrId);
      clientInfo.append("corrId=").append(tagValue);
    }

    if (nodeId != null && !nodeId.equals("")) {
      if (clientInfo.length() > 0) {
        clientInfo.append("&");
      }
      String tagValue = PropertyUtils.encodeValue(nodeId);
      clientInfo.append("nodeId=").append(tagValue);
    }
    return clientInfo.toString();
  }

  private String toCALDateString(long time) {
    CalTimeOfDay calTimeOfDay = new CalTimeOfDay(time);
    return calTimeOfDay.toString();
  }


}
