package com.ebay.sojourner.ubd.common.env;

import javax.annotation.Nullable;

public interface PropertyResolver {

  boolean contains(String key);

  @Nullable
  String getProperty(String key);

  @Nullable
  <T> T getProperty(String key, Class<T> clazz);

}
