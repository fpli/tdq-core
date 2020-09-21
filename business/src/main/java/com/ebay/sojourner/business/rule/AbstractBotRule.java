package com.ebay.sojourner.business.rule;

import com.ebay.sojourner.dsl.domain.rule.Rule;

public abstract class AbstractBotRule<T> implements Rule<T> {

  @Override
  public void init() {
    // default empty implementation
  }
}
