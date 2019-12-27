package com.ebay.sojourner.ubd.rt.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.beans.IntrospectionException;
import java.io.InputStream;

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
                    Representer representer = new Representer();
                    representer.setPropertyUtils(new MyPropUtils());
                    representer.getPropertyUtils().setSkipMissingProperties(true);
                    InputStream in = AppEnv.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
                    Yaml yaml = new Yaml(new Constructor(AppEnv.class), representer);
                    appEnv = yaml.loadAs(in, AppEnv.class);
                }
            }
        }
        return appEnv;
    }

    static class MyPropUtils extends PropertyUtils {
        @Override
        public Property getProperty(Class<?> type, String name) throws IntrospectionException {
            if (name.indexOf('-') > -1) {
                name = StringUtils.uncapitalize(StringUtils.remove(WordUtils.capitalize(name, '-'), '-'));
            }
            return super.getProperty(type, name);
        }
    }
}
