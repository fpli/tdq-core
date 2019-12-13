package com.ebay.sojourner.ubd.rt.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import scala.App;

import java.io.InputStream;

@Data
public class AppEnv {

    private static final String CONFIG_FILE_NAME = "application.yml";

    private KafkaConfig kafka;
    private RheosConfig rheos;

    private static volatile AppEnv appEnv;

    private AppEnv() {
        //private
    }

    public static AppEnv config() {
        if (appEnv == null) {
            synchronized (AppEnv.class) {
                if (appEnv == null) {
                    Representer representer = new Representer();
                    representer.getPropertyUtils().setSkipMissingProperties(true);
                    InputStream in = AppEnv.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
                    Yaml yaml = new Yaml(new Constructor(AppEnv.class), representer);
                    appEnv = yaml.loadAs(in, AppEnv.class);
                }
            }
        }
        return appEnv;
    }
}
