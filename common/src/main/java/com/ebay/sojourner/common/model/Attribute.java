package com.ebay.sojourner.common.model;

public interface Attribute<T> {

  void feed(T t, int botFlag);

  void revert(T t, int botFlag);

  void clear();

  void clear(int botFlag);
}
