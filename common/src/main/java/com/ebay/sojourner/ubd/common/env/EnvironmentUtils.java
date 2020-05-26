package com.ebay.sojourner.ubd.common.env;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvironmentUtils {

  private static final List<AbstractEnvironment> PROP_SOURCES = Lists.newArrayList();

  static {
    log.info("Load environment properties file");

    PROP_SOURCES.add(new ArgsSource());
    PROP_SOURCES.add(new EnvSource());
    PROP_SOURCES.add(new PropertySource());

    String cmd = System.getProperty("sun.java.command");
    Optional<String> profile = Lists.newArrayList(cmd.split(" ")).stream()
        .filter(s -> s != null && s.startsWith("--profile="))
        .findFirst();
    if (profile.isPresent()) {
      // NOT support multiple profiles for now
      String profileName = profile.get().split("=")[1];
      String configFileName = "application-" + profileName;
      PROP_SOURCES.add(new PropertySource(configFileName, 3));
    }

    PROP_SOURCES.sort(Comparator.comparing(AbstractEnvironment::order));
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      propSource.sourceProps();
    }
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
