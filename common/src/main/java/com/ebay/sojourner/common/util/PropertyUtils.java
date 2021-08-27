package com.ebay.sojourner.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PropertyUtils {

  public static Properties loadInProperties(String absoluteFilePath, String alternativeResource)
      throws FileNotFoundException {
    Properties properties = new Properties();
    InputStream instream = null;
    try {
      instream = FileLoader.loadInStream(absoluteFilePath, alternativeResource);
      properties.load(instream);
    } catch (Exception e) {
      throw new FileNotFoundException(absoluteFilePath);
    } finally {
      if (instream != null) {
        try {
          instream.close();
        } catch (IOException e) {
        }
      }
    }

    return properties;
  }

  public static Properties loadInProperties(InputStream configFileStream)
      throws FileNotFoundException {
    Properties properties = new Properties();
    InputStream instream = null;
    try {
      instream = FileLoader.loadInStream(configFileStream);
      properties.load(instream);
    } catch (Exception e) {
      throw new FileNotFoundException("load file failed!!!");
    } finally {
      if (instream != null) {
        try {
          instream.close();
        } catch (IOException e) {
        }
      }
    }

    return properties;
  }

  /**
   * Return the values with sequential order by splitting property value with the delimiter
   */
  public static Collection<String> parseProperty(String property, String delimiter) {
    Collection<String> pageIdCollection = new ArrayList<String>();
    if (property != null) {
      String[] pageIds = property.split(delimiter);
      for (String pageId : pageIds) {
        pageIdCollection.add(pageId.trim());
      }
    }

    return pageIdCollection;
  }

  public static Set<Integer> getIntegerSet(String property, String delimiter) {
    Set<Integer> propertySet = new HashSet<>();
    if (StringUtils.isNotBlank(property)) {
      String[] list = property.split(delimiter);
      for (String s : list) {
        propertySet.add(Integer.valueOf(s.trim()));
        // Do not ignore the NumberFormatException as it indicates the configuration errors.
      }
    }
    return propertySet;
  }

  public static Set<Long> getLongSet(String property, String delimiter) {
    Set<Long> propertySet = new HashSet<>();
    if (StringUtils.isNotBlank(property)) {
      String[] list = property.split(delimiter);
      for (String s : list) {
        propertySet.add(Long.valueOf(s.trim()));
        // Do not ignore the NumberFormatException as it indicates the configuration errors.
      }
    }
    return propertySet;
  }

  public static String mapToString(Map<String, String> sojMap) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, String> pair : sojMap.entrySet()) {
      sb.append(pair.getKey()).append("=").append(pair.getValue()).append("&");
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  public static Map<String, String> stringToMap(String sojStr, boolean needDecode) {
    if (StringUtils.isEmpty(sojStr)) {
      return null;
    }
    Map<String, String> sojMap = new LinkedHashMap<>();
    String[] keyValues = sojStr.split("&");
    if (keyValues.length > 0) {
      for (String keyValue : keyValues) {
        int idx = keyValue.indexOf("=");
        if (idx > 0) {
          String k = keyValue.substring(0, idx);
          String v = keyValue.substring(idx + 1);
          if (needDecode) {
            if (!Constants.CLIENT_TAG_EXCLUDE.contains(k)) {
              sojMap.put(k, decodeValue(v));
            } else {
              sojMap.put(k, v);
            }
          } else {
            sojMap.put(k, v);
          }
        }
      }
    }
    return sojMap;
  }


  public static String decodeValue(String value) {
    String tagValue = value;
    if (StringUtils.isNotEmpty(value) && value.contains("%")) {
      try {
        tagValue = URLDecoder.decode(value, Constants.CHAR_SET);
      } catch (Exception e) {
        log.error("Decode value error: {}", value, e);
      }
    }
    return tagValue;
  }

  public static String encodeValue(String value) {
    String tagValue = value;
    if (StringUtils.isNotEmpty(value) && (value.contains("&") || value.contains("=") || value
        .contains("%"))) {
      try {
        tagValue = URLEncoder.encode(value, Constants.CHAR_SET);
      } catch (Exception e) {
        log.error("Encode value error: {}", value, e);
      }
    }
    return tagValue;
  }
}
