package com.ebay.sojourner.ubd.rt.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

@Slf4j
@Data
public class AppEnv {

    private static final String CONFIG_FILE_NAME = "application.yml";

    private KafkaConfig kafka;
    private RheosConfig rheos;
    private FlinkConfig flink;

    private static volatile AppEnv appEnv;

    private AppEnv() {
        //private
    }

    public static AppEnv config() {
        if (appEnv == null) {
            synchronized (AppEnv.class) {
                if (appEnv == null) {
                    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
                            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    objectMapper.findAndRegisterModules();
                    try {
                        String path = Objects.requireNonNull(AppEnv.class.getClassLoader().getResource(CONFIG_FILE_NAME)).getPath();
                        appEnv = objectMapper.readValue(new File(path), AppEnv.class);
                    } catch (Exception e) {
                        log.error("Cannot load {} file", CONFIG_FILE_NAME, e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return appEnv;
    }
}
