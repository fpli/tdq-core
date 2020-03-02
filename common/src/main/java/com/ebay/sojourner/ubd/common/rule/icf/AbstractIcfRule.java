package com.ebay.sojourner.ubd.common.rule.icf;

import com.ebay.sojourner.ubd.common.rule.Rule;

public abstract class AbstractIcfRule<T> implements Rule<T> {

  @Override
  public void init() {
    // default empty implementation
  }
}
