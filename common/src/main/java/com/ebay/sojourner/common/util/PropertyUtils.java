package com.ebay.sojourner.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    HashSet<Integer> propertySet = new HashSet<Integer>();
    if (StringUtils.isNotBlank(property)) {
      String[] list = property.split(delimiter);
      for (int i = 0; i < list.length; i++) {
        propertySet.add(Integer.valueOf(list[i].trim()));
        // Do not ignore the NumberFormatException as it indicates the configuration errors.
      }
    }
    return propertySet;
  }

  public static Set<Long> getLongSet(String property, String delimiter) {
    HashSet<Long> propertySet = new HashSet<Long>();
    if (StringUtils.isNotBlank(property)) {
      String[] list = property.split(delimiter);
      for (int i = 0; i < list.length; i++) {
        propertySet.add(Long.valueOf(list[i].trim()));
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

  public static Map stringToMap(String sojStr) {
    if (StringUtils.isEmpty(sojStr)) {
      return null;
    }
    Map<String, String> sojMap = new LinkedHashMap<>();
    String[] keyValues = sojStr.split("&");
    if (keyValues != null && keyValues.length > 0) {
      for (String keyValue : keyValues) {
        String[] keyValuePair = keyValue.split("=");
        if (keyValuePair != null && keyValuePair.length > 0) {
          sojMap.put(keyValuePair[0], keyValuePair.length == 2 ? keyValuePair[1] : "");
        }
      }
    }
    return sojMap;
  }

  public static void main(String[] args) {
    String clientInfo = "TPayload=corr_id_%3Dd8614a035f295369%26node_id%3Dbb5990c6f0e9cd1c"
        + "%26REQUEST_GUID%3D174b01b1-9090-aaec-ccd3-ce05f5eb8c01%26logid%3Dt6qjpbq%253F"
        + "%253Cumjthu%2560t%2A%253Bwhls%2528rbpv6710-174b01b190e-0x130%26cal_mod%3Dfalse&TPool"
        + "=r1rover&TDuration=14&TStatus=0&TType=URL&ContentLength=-1&ForwardedFor=10.21.177"
        + ".19&Script=/rover/1/709-53476-19255-0/1&Server=rover.ebay.com&TMachine=10.174.204"
        + ".205&TStamp=03:00:00.00&TName=rover&Agent=Mozilla/5.0 (compatible; adidxbot/2.0;"
        + " +http://www.bing.com/bingbot.htm)&RemoteIP=40.77.167.74&Encoding=gzip, deflate";
    log.info(mapToString(stringToMap(clientInfo)));
    String applicationPayLoad = "rdt=1&c=1&rvrrefts=b01b19161740aaecccd50bd1ff7dd311&g"
        + "=b01b190d1740aaecccd64171f5782adb&nid=&h=0d&cguidsrc=new&n"
        + "=b01b190e1740aaecccd64171f5782ad9&uc=1&url_mpre=http%3A%2F%2Fwww.ebay"
        + ".fr%2Fitm%2Flike%2F123910644857&p=3084&uaid=b01b190d1740aaecccd64171f5782adaS0&bs=0"
        + "&rvrid=2610749100186&t=0&cflgs=wA**&ul=en-US&hrc=301&gitCommitId"
        + "=ef61ef010bd721c30b3e3092a4bae5d88481e9ab&pn=2&rq=d8614a035f295369&pagename"
        + "=EntryTracking&ciid=GxkOzM0*";
    log.info(mapToString(stringToMap(applicationPayLoad)));
  }
}
