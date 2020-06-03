package com.ebay.sojourner.business.ubd.rule.icf;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.IcfRuleUtils;

public class IcfRule10 extends AbstractIcfRule<UbiEvent> {

  @Override
  public int getBotFlag(UbiEvent ubiEvent) {
    return IcfRuleUtils.getIcfRuleType(ubiEvent.getIcfBinary(), 10);
  }
}
