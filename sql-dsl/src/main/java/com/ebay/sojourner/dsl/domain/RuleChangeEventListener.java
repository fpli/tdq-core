package com.ebay.sojourner.dsl.domain;

import com.ebay.sojourner.dsl.domain.rule.RuleCategory;
import com.ebay.sojourner.dsl.domain.rule.RuleChangeEvent;

public interface RuleChangeEventListener<E extends RuleChangeEvent> {
  void onChange(E e);
  RuleCategory category();
}
