package com.ebay.sojourner.dsl.sql;

import com.ebay.sojourner.common.model.UbiSession;

/**
 * This class is for ReflectiveSchema, so all fields must be public
 */
public class ReflectiveUbiSession {

  public String agentInfo;

  public ReflectiveUbiSession(UbiSession ubiSession) {
    this.agentInfo = ubiSession.getAgentInfo();
  }

}
