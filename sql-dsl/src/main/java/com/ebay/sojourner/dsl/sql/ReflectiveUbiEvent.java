package com.ebay.sojourner.dsl.sql;

import com.ebay.sojourner.common.model.UbiEvent;

/**
 * This class is for ReflectiveSchema, so all fields must be public
 */
public class ReflectiveUbiEvent {

  public String agentInfo;
  public int staticPageType;
  public int pageId;
  public long icfBinary;

  public ReflectiveUbiEvent(UbiEvent ubiEvent) {
    this.agentInfo = ubiEvent.getAgentInfo();
    this.staticPageType = ubiEvent.getStaticPageType();
    this.pageId = ubiEvent.getPageId();
    this.icfBinary = ubiEvent.getIcfBinary();
  }
}
