package com.ebay.sojourner.ubd.common.rule.icf;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.IcfRuleUtils;

public class IcfRule5 extends AbstractIcfRule<UbiEvent> {

  @Override
  public int getBotFlag(UbiEvent ubiEvent) {
    return IcfRuleUtils.getIcfRuleType(ubiEvent.getIcfBinary(), 5);
  }
}