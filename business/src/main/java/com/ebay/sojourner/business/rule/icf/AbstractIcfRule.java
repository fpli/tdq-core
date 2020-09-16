package com.ebay.sojourner.business.rule.icf;

import com.ebay.sojourner.common.model.rule.Rule;

public abstract class AbstractIcfRule<T> implements Rule<T> {

  @Override
  public void init() {
    // default empty implementation
  }
}
