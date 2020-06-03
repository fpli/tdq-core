package com.ebay.sojourner.business.ubd.rule.icf;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.IcfRuleUtils;

public class IcfRule1 extends AbstractIcfRule<UbiEvent> {

  @Override
  public int getBotFlag(UbiEvent ubiEvent) {
    return IcfRuleUtils.getIcfRuleType(ubiEvent.getIcfBinary(), 1);
  }
}
