package com.ebay.sojourner.business.rule.icf;

import com.ebay.sojourner.common.model.UbiEvent;

public class IcfRule10 extends AbstractIcfRule<UbiEvent> {

  @Override
  public int getBotFlag(UbiEvent ubiEvent) {
    return getIcfRuleType(ubiEvent.getIcfBinary(), 10);
  }
}