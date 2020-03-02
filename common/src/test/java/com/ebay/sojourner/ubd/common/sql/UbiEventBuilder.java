package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.model.UbiEvent;

public class UbiEventBuilder {
  private String agentInfo;
  private int staticPageType;
  private int pageId;
  private String flags;
  private long icfBinary;

  public UbiEventBuilder agentInfo(String agentInfo) {
    this.agentInfo = agentInfo;
    return this;
  }

  public UbiEventBuilder staticPageType(int staticPageType) {
    this.staticPageType = staticPageType;
    return this;
  }

  public UbiEventBuilder pageId(int pageId) {
    this.pageId = pageId;
    return this;
  }

  public UbiEventBuilder flags(String flags) {
    this.flags = flags;
    return this;
  }

  public UbiEventBuilder icfBinary(long icfBinary) {
    this.icfBinary = icfBinary;
    return this;
  }

  public UbiEvent build() {
    UbiEvent event = new UbiEvent();
    event.setAgentInfo(this.agentInfo);
    event.setStaticPageType(this.staticPageType);
    event.setPageId(this.pageId);
    event.setFlags(this.flags);
    event.setIcfBinary(this.icfBinary);
    return event;
  }
}
