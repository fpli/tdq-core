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

  public static Map stringToMap(String sojStr, boolean needDecode) {
    if (StringUtils.isEmpty(sojStr)) {
      return null;
    }
    Map<String, String> sojMap = new LinkedHashMap<>();
    String[] keyValues = sojStr.split("&");
    if (keyValues != null && keyValues.length > 0) {
      for (String keyValue : keyValues) {
        String[] keyValuePair = keyValue.split("=", -1);
        if (keyValuePair != null && keyValuePair.length > 0) {
          if (keyValuePair.length == 1) {
            sojMap.put(keyValuePair[0], "");
          } else if (keyValuePair.length == 2) {
            if (needDecode) {
              if (!Constants.CLIENT_TAG_EXCLUDE.contains(keyValuePair[0])) {
                String tagValue = decodeValue(keyValuePair[1]);
                sojMap.put(keyValuePair[0], tagValue);
              } else {
                sojMap.put(keyValuePair[0], keyValuePair[1]);
              }
            } else {
              sojMap.put(keyValuePair[0], keyValuePair[1]);
            }
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
        tagValue = URLDecoder
            .decode(value, Constants.CHAR_SET);
      } catch (Exception e) {
        log.error("Decode value error: ", value, e);
      }
    }
    return tagValue;
  }

  public static String encodeValue(String value) {
    String tagValue = value;
    if (StringUtils.isNotEmpty(value) && (value.contains("&") || value.contains("=") || value
        .contains("%"))) {
      try {
        tagValue = URLEncoder
            .encode(value, Constants.CHAR_SET);
      } catch (Exception e) {
        log.error("encode value error: ", value, e);
      }
    }
    return tagValue;
  }

  //  public static void main(String[] args) {
  //    System.out.println("a&b".contains("&|=|%"));
  //    ClientData clientData = new ClientData();
  //    clientData.setAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML,
  //    like"
  //        + " Gecko) Chrome/85.0.4183.121 &&=Safari/537.36 Edg/85.0.564.63");
  //    clientData.setTPayload(
  //        "corr_id_%3D7359ac6641847458%26node_id%3D353eb9e6375b6414%26REQUEST_GUID%3D174cc0d1 -
  //        9820"
  //            + "-aada-ba47-8090ff1bd075%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%25285%253E"
  //            + "%253A745%253B-174cc0d1987-0xdd%26cal_mod%3Dfalse");
  //    clientData.setReferrer(
  //        "https://www.ebay.de/itm/%C3%98-100-125-160-mm-HM-Lochs%C3%A4ge-Satz-Bohrkrone"
  //            + "-Dosenbohrer-f%C3%BCr-Beton/223545013140&&");
  //    System.out.println(stringToMap(clientData.toString(), true));
  //    System.out.println(encodeValue(
  //        "https://www.ebay.de/itm/%C3%98-100-125-160-mm-HM-Lochs%C3%A4ge-Satz-Bohrkrone"
  //            + "-Dosenbohrer-f%C3%BCr-Beton/223545013140"));
  //    System.out.println(stringToMap(
  //        "TPayload=corr_id_%3D7359ac6641847458%26node_id%3D353eb9e6375b6414%26REQUEST_GUID"
  //            + "%3D174cc0d1-9820-aada-ba47-8090ff1bd075%26logid%3Dt6wwm53vpd77%253C"
  //            + "%253Dosusqn47pse31%25285%253E%253A745%253B-174cc0d1987-0xdd%26cal_mod%3Dfalse"
  //            + "&TPool=r1searchsvc&TDuration=3&TStatus=0&TType=URL&ContentLength=1300
  //            &ForwardedFor"
  //            + "=108.66.0.180;184.26.53.21&Script=/trk20svc/TrackingResource/v1&Server=www.ebay"
  //            + ".com&TMachine=10.173.171.164&TStamp=13:14:04.67&TName=Ginger.CollectionSvc"
  //            + ".track&Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36
  //            (KHTML, "
  //            + "like Gecko) Chrome/85.0.4183.121 Safari/537.36 Edg/85.0.564.63&RemoteIP=108.66.0"
  //            + ".180&Encoding=gzip&Referer=https://www.ebay.com/sch/6028/i"
  //            + ".html?_nkwfiat+x1%2F9", true));
  //  }
}
