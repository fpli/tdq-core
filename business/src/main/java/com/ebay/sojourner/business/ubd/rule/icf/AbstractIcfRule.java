package com.ebay.sojourner.business.ubd.rule.icf;

import com.ebay.sojourner.business.ubd.rule.Rule;

public abstract class AbstractIcfRule<T> implements Rule<T> {

  @Override
  public void init() {
    // default empty implementation
  }
}
