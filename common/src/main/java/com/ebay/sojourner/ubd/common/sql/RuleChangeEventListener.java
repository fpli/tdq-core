package com.ebay.sojourner.ubd.common.sql;

public interface RuleChangeEventListener<E extends RuleChangeEvent> {
  void onChange(E e);
}
