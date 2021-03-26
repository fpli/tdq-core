package com.ebay.sojourner.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PropertyUtilsTest {

  @Test
  void mapToString() {
    Map<String, String> map = new HashMap<>();
    map.put("a", "test");
    map.put("b", "test");
    map.put("c", null);
    map.put("d", "");
    map.put("e", "i=123|m=456");
    assertThat(PropertyUtils.mapToString(map)).isEqualTo("a=test&b=test&c=null&d=&e=i=123|m=456");
  }

  @Test
  void stringToMap() {
    String str = "a=test&b=test&c=null&d=&e=i=123|m=456";
    Map<String, String> resultMap = PropertyUtils.stringToMap(str, false);
    assertThat(resultMap).size().isEqualTo(5);
    assertThat(resultMap.get("a")).isEqualTo("test");
    assertThat(resultMap.get("b")).isEqualTo("test");
    assertThat(resultMap.get("c")).isEqualTo("null");
    assertThat(resultMap.get("d")).isEqualTo("");
    assertThat(resultMap.get("e")).isEqualTo("i=123|m=456");
  }
}