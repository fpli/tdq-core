package com.ebay.sojourner.ubd.common.env;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.InputStream;
import java.util.Map;

public class PropertySource extends AbstractEnvironment {

  private static final String CONFIG_FILE_NAME = "application.yml";

  @Override
  public void sourceProps() {
    ObjectMapper objectMapper =
        new ObjectMapper(new YAMLFactory())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      InputStream inputStream =
          this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
      props = objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
