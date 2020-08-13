package com.ebay.sojourner.dsl.sql;

public interface SQLRule<T, OUT> {

  void compile();
  OUT execute(T t);

}
