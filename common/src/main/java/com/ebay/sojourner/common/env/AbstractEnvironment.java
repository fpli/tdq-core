package com.ebay.sojourner.common.env;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class AbstractEnvironment implements Environment {

  protected Map<String, Object> props = new HashMap<>();

  @Override
  public boolean contains(String key) {
    return props.containsKey(key);
  }

  @Nullable
  @Override
  public String getProperty(String key) {
    return (String) props.get(key);
  }

  @Nullable
  @Override
  public <T> T getProperty(String key, Class<T> clazz) {
    return (T) props.get(key);
  }

  public abstract Integer order();

}
