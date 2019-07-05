package com.ebay.sojourner.ubd.common.util;

import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;

public class YamlUtil {
    private static Yaml yaml;

    static {
        yaml = new Yaml();
    }

    public static YamlUtil getInstance(){
        return new YamlUtil();
    }

    public HashMap<String,Object> loadFileMap(String filePath){
        HashMap<String,Object> map =
                yaml.loadAs(this.getClass().getClassLoader().getResourceAsStream(filePath), HashMap.class);
        return map;
    }
}


