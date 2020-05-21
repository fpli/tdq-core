package com.ebay.sojourner.ubd.rt.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.InputStream;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.utils.ParameterTool;

@Deprecated
@Slf4j
@Data
public class AppEnv {

  private static final String CONFIG_FILE_NAME = "application.yml";
  private static volatile AppEnv appEnv;
  private KafkaConsumerConfig kafkaConsumerConfig;
  private KafkaProducerConfig kafkaProducerConfig;
  private RheosConfig rheos;
  private FlinkConfig flink;
  private HdfsConfig hdfs;

  private AppEnv() {
    // private
  }

  public static AppEnv config() {
    if (appEnv == null) {
      synchronized (AppEnv.class) {
        if (appEnv == null) {
          ObjectMapper objectMapper =
              new ObjectMapper(new YAMLFactory())
                  .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
          try {
            InputStream inputStream =
                AppEnv.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
            appEnv = objectMapper.readValue(inputStream, AppEnv.class);
          } catch (Exception e) {
            log.error("Cannot load {} file", CONFIG_FILE_NAME, e);
            throw new RuntimeException(e);
          }
        }
      }
    }
    return appEnv;
  }

  public static AppEnv config(ParameterTool parameterTool) {
    if (parameterTool == null || parameterTool.getNumberOfParameters() == 0) {
      return config();
    }

    if (appEnv == null) {
      synchronized (AppEnv.class) {
        if (appEnv == null) {
          ObjectMapper objectMapper =
              new ObjectMapper(new YAMLFactory())
                  .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
          try {
            InputStream inputStream =
                AppEnv.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
            Map<String, Object> config =
                objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {
                });
            replaceWithParamTool(config, parameterTool);
            String jsonStr = objectMapper.writeValueAsString(config);

            appEnv = objectMapper.readValue(jsonStr, AppEnv.class);
            return appEnv;
          } catch (Exception e) {
            log.error("Cannot load {} file", CONFIG_FILE_NAME, e);
            throw new RuntimeException(e);
          }
        }
      }
    }
    return appEnv;
  }

  private static void replaceWithParamTool(
      Map<String, Object> config, ParameterTool parameterTool) {

    for (String key : config.keySet()) {

      if (config.get(key) instanceof Map) {
        nestReplace(key, (Map<String, Object>) config.get(key), parameterTool);
      } else if (parameterTool.get(key) != null) {
        config.put(key, parameterTool.get(key));
      }
    }
  }

  private static Map<String, Object> nestReplace(
      String key, Map<String, Object> map, ParameterTool parameterTool) {
    for (String mapKey : map.keySet()) {
      String newKey = String.format("%s.%s", key, mapKey);
      if (map.get(mapKey) instanceof Map) {
        nestReplace(newKey, (Map<String, Object>) map.get(mapKey), parameterTool);
      } else if (parameterTool.get(newKey) != null) {
        map.put(mapKey, parameterTool.get(newKey));
      }
    }

    return map;
  }
}
