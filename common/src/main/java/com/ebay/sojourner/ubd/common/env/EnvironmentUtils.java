package com.ebay.sojourner.ubd.common.env;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvironmentUtils {

  private final static List<AbstractEnvironment> propSources = Lists.newArrayList();

  static {
    log.info("Load environment properties file");
    PropertySource propertySource = new PropertySource();
    EnvSource envSource = new EnvSource();
    ArgsSource argsSource = new ArgsSource();

    // order matters, DO NOT change it.
    propSources.add(argsSource);
    propSources.add(envSource);
    propSources.add(propertySource);
  }

  public static String get(String key) {
    for (AbstractEnvironment propSource : propSources) {
      if (propSource.contains(key)) {
        return propSource.getProperty(key);
      }
    }
    throw new IllegalStateException("Cannot find property " + key);
  }

  public static <T> T get(String key, Class<T> clazz) {
    for (AbstractEnvironment propSource : propSources) {
      if (propSource.contains(key)) {
        return propSource.getProperty(key, clazz);
      }
    }
    throw new IllegalStateException("Cannot find property " + key);
  }
}
