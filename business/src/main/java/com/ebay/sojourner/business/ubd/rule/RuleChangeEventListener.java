package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.rule.RuleCategory;
import com.ebay.sojourner.common.model.rule.RuleChangeEvent;

public interface RuleChangeEventListener<E extends RuleChangeEvent> {
  void onChange(E e);
  RuleCategory category();
}
