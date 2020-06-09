package com.ebay.sojourner.common.env;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvironmentUtils {

  private static final Set<AbstractEnvironment> PROP_SOURCES =
      Sets.newTreeSet(Comparator.comparing(AbstractEnvironment::order));


  static {
    log.info("Load environment properties file");

    PROP_SOURCES.add(new EnvSource());
    PROP_SOURCES.add(new PropertySource());

    // source props
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      propSource.sourceProps();
    }

  }

  public static void activateProfile(String profile) {
    Preconditions.checkNotNull(profile);

    String configFileName = "application-" + profile;
    PropertySource propertySource = new PropertySource(configFileName, 3);
    propertySource.sourceProps();
    PROP_SOURCES.add(propertySource);
  }

  public static void fromProperties(Properties properties) {
    Preconditions.checkNotNull(properties);
    ArgsSource argsSource = new ArgsSource(properties);
    argsSource.sourceProps();
    PROP_SOURCES.add(argsSource);
  }

  public static String get(String key) {
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      if (propSource.contains(key)) {
        return propSource.getProperty(key);
      }
    }
    throw new IllegalStateException("Cannot find property " + key);
  }

  public static Integer getInteger(String key) {
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      if (propSource.contains(key)) {
        return Integer.valueOf(Objects.requireNonNull(propSource.getProperty(key)));
      }
    }
    throw new IllegalStateException("Cannot find property " + key);
  }

  public static <T> T get(String key, Class<T> clazz) {
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      if (propSource.contains(key)) {
        return propSource.getProperty(key, clazz);
      }
    }
    throw new IllegalStateException("Cannot find property " + key);
  }
}
