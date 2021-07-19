package com.ebay.sojourner.common.env;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class EnvironmentUtils {

  public static final String PROFILE = "tdq-profile";
  public static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(.*?)}");

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

  public static void print() {
    // checkstyle.off: Regexp
    String t = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
    System.out.println("=============== " + t + " start =================");
    Set<String> keys = new HashSet<>();
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      keys.addAll(propSource.props.keySet());
    }
    Lists.newArrayList(keys).forEach(k -> {
      if (k.equalsIgnoreCase("password")
          || k.equalsIgnoreCase("p")
          || k.equalsIgnoreCase("api-value")) {
        System.out.println(k + "=******");
      } else {
        try {
          System.out.println(k + "=" + get(k));
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    });
    System.out.println("=============== " + t + " end =================\n\n");
    // checkstyle.on: Regexp
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
    Preconditions.checkNotNull(key);
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      if (propSource.contains(key)) {
        return propSource.getProperty(key);
      }
    }
    throw new IllegalStateException("Cannot find property " + key);
  }

  public static String[] getStringArray(String key, String delimiter) {
    String s = get(key);
    delimiter = "\\s*" + delimiter + "\\s*";
    return s.split(delimiter);
  }

  public static List<String> getStringList(String key, String delimiter) {
    String[] stringArray = getStringArray(key, delimiter);
    return Lists.newArrayList(stringArray);
  }

  public static String getStringOrDefault(String key, String defaultValue) {
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      if (propSource.contains(key)) {
        return propSource.getProperty(key);
      }
    }
    return defaultValue;
  }

  public static Boolean getBoolean(String key) {
    String booleanVal = get(key);
    return Boolean.valueOf(booleanVal);
  }

  public static Integer getInteger(String key) {
    String intVal = get(key);
    return Integer.valueOf(intVal);
  }

  public static <T> T get(String key, Class<T> clazz) {
    for (AbstractEnvironment propSource : PROP_SOURCES) {
      if (propSource.contains(key)) {
        return propSource.getProperty(key, clazz);
      }
    }
    throw new IllegalStateException("Cannot find property " + key);
  }

  public static Set<String> getSet(String key) {
    List<String> list = get(key, List.class);
    return new HashSet<>(list);
  }

  public static String getStringWithPattern(String key) {
    String str = get(key);
    Matcher m = VARIABLE_PATTERN.matcher(str);
    String s = "";
    if (m.find()) {
      String subKey = m.group(1);
      if (StringUtils.isNotBlank(subKey)) {
        subKey = subKey.trim();
      }
      s = get(subKey);
      if (StringUtils.isBlank(s)) {
        throw new IllegalStateException("Cannot find property " + subKey + "=" + s);
      }
    }
    return m.replaceAll(s);
  }
}
