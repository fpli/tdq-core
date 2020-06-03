package com.ebay.sojourner.business.ubd.sql;

public class SojReflectiveEvent {

  public String agentInfo;
  public int staticPageType;
  public int pageId;
  public long icfBinary;

  public static class Builder {

    private String agentInfo;
    private int staticPageType;
    private int pageId;
    private long icfBinary;

    public Builder agentInfo(String agentInfo) {
      this.agentInfo = agentInfo;
      return this;
    }

    public Builder staticPageType(int staticPageType) {
      this.staticPageType = staticPageType;
      return this;
    }

    public Builder pageId(int pageId) {
      this.pageId = pageId;
      return this;
    }

    public Builder icfBinary(long icfBinary) {
      this.icfBinary = icfBinary;
      return this;
    }

    public SojReflectiveEvent build() {
      SojReflectiveEvent event = new SojReflectiveEvent();
      event.agentInfo = this.agentInfo;
      event.staticPageType = this.staticPageType;
      event.pageId = pageId;
      event.icfBinary = this.icfBinary;
      return event;
    }
  }
}
