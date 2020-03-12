package com.ebay.sojourner.ubd.common.model;

public interface Attribute<T> {

  void feed(T t, int botFlag, boolean isNeeded);

  void revert(T t, int botFlag);

  void clear();

  void clear(int botFlag);
}
