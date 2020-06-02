package com.ebay.sojourner.ubd.common.env;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.Map;
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
    PROP_SOURCES.add(new ArgsSource());

    // source props
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      propSource.sourceProps();
    }

  }

  public static void activateProfile(String profile) {
    Preconditions.checkNotNull(profile);

    String configFileName = "application-" + profile;
    PROP_SOURCES.add(new PropertySource(configFileName, 3));
  }

  public static void fromMap(Map<String, Object> map) {
    Preconditions.checkNotNull(map);
    PROP_SOURCES.add(new AbstractEnvironment() {
      @Override
      public Integer order() {
        return 0;
      }

      @Override
      public void sourceProps() {
        this.props = map;
      }
    });

  }

  public static String get(String key) {
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      if (propSource.contains(key)) {
        return propSource.getProperty(key);
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
