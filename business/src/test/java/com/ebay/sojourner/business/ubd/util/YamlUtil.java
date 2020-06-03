package com.ebay.sojourner.business.ubd.util;

import java.util.HashMap;
import org.yaml.snakeyaml.Yaml;

public class YamlUtil {

  private static Yaml yaml;

  static {
    yaml = new Yaml();
  }

  public static YamlUtil getInstance() {
    return new YamlUtil();
  }

  public HashMap<String, Object> loadFileMap(String filePath) {
    HashMap<String, Object> map =
        yaml.loadAs(this.getClass().getClassLoader().getResourceAsStream(filePath), HashMap.class);
    return map;
  }
}
