package com.ebay.sojourner.business.rule.icf;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.IcfRuleUtils;

public class IcfRule6 extends AbstractIcfRule<UbiEvent> {

  @Override
  public int getBotFlag(UbiEvent ubiEvent) {
    return IcfRuleUtils.getIcfRuleType(ubiEvent.getIcfBinary(), 6);
  }
}
