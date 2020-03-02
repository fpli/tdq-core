package com.ebay.sojourner.ubd.common.model;

import com.alibaba.fastjson.JSON;
import java.io.Serializable;
import java.util.Map;

public class RawEvent implements Serializable {

  private RheosHeader rheosHeader;
  private Map<String, String> sojA;
  private Map<String, String> sojK;
  private Map<String, String> sojC;
  private ClientData clientData;

  public RawEvent() {}

  public RawEvent(
      RheosHeader rheosHeader,
      Map<String, String> sojA,
      Map<String, String> sojK,
      Map<String, String> sojC,
      ClientData clientData) {
    this.rheosHeader = rheosHeader;
    this.sojA = sojA;
    this.sojK = sojK;
    this.sojC = sojC;
    this.clientData = clientData;
  }

  public RheosHeader getRheosHeader() {
    return rheosHeader;
  }

  public void setRheosHeader(RheosHeader rheosHeader) {
    this.rheosHeader = rheosHeader;
  }

  public Map<String, String> getSojA() {
    return sojA;
  }

  public void setSojA(Map<String, String> sojA) {
    this.sojA = sojA;
  }

  public Map<String, String> getSojK() {
    return sojK;
  }

  public void setSojK(Map<String, String> sojK) {
    this.sojK = sojK;
  }

  public Map<String, String> getSojC() {
    return sojC;
  }

  public void setSojC(Map<String, String> sojC) {
    this.sojC = sojC;
  }

  public ClientData getClientData() {
    return clientData;
  }

  public void setClientData(ClientData clientData) {
    this.clientData = clientData;
  }

  // For debug only
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    sb.append("rheosHeader:" + this.rheosHeader.toString()).append(",");
    sb.append("sojA:" + JSON.toJSONString(this.sojA)).append(",");
    sb.append("sojC:" + JSON.toJSONString(this.sojC)).append(",");
    sb.append("sojK:" + JSON.toJSONString(this.sojK)).append(",");
    sb.append("clientData:" + JSON.toJSONString(this.clientData)).append("}");
    //        return new ReflectionToStringBuilder(this, new RecursiveToStringStyle()).toString();
    return sb.toString();
  }
}
