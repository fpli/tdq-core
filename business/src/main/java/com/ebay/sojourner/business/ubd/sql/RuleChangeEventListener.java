package com.ebay.sojourner.business.ubd.sql;

public interface RuleChangeEventListener<E extends RuleChangeEvent> {
  void onChange(E e);
}
