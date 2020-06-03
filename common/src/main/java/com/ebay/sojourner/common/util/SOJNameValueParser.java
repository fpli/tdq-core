package com.ebay.sojourner.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class SOJNameValueParser {

  public static final String KV_DELIMITER = "&";
  public static final String BLANK_STRING = "";

  /*
   * this function provide a faster search through the string
   * while it might miss some keys start with _ or !
   */
  public static String getTagValue(String string, String key) {
    if (StringUtils.isBlank(string) || StringUtils.isBlank(key)) {
      return null;
    }

    // ensure string starts with &
    string = "&" + string;
    int keyIndex = string.indexOf("&" + key + "=");

    if (keyIndex >= 0) {
      keyIndex += 1 + key.length();

      int nextKeyIndex = string.indexOf("&", keyIndex + 1);

      if (nextKeyIndex < 0) {
        nextKeyIndex = string.length();
      }

      String finalValue = string.substring(keyIndex + 1, nextKeyIndex);

      return finalValue.equals("") ? null : finalValue;
    }

    return null;
  }

  public static void getTagValues(String string, Collection<String> keys, Map<String, String> kvMap)
      throws NullPointerException {
    if (kvMap == null) {
      throw new NullPointerException("No Map init to store the KV");
    } else if (kvMap.size() > 0) {
      kvMap.clear();
    }

    if (StringUtils.isBlank(string) || keys == null || keys.size() <= 0) {
      return;
    }

    Collection<String> cleanKeys = new HashSet<String>();
    Pattern p1, p2;
    Matcher m1, m2;
    String keySet = "";
    int startpos = 0;
    int endpos = 0;
    int tmppos = 0;
    int currentPos = 0;

    // ensure string starts with &
    string = "&" + string;

    // set search keys
    Iterator<String> iterator = keys.iterator();
    String key = null;
    while (iterator.hasNext()) {
      key = iterator.next();
      if (StringUtils.isNotBlank(key)) {
        keySet += "&" + key + "=|";
        cleanKeys.add(key);
      }
    }

    if (cleanKeys.size() == 0) {
      return;
    }

    keySet = keySet.substring(0, keySet.length() - 1);

    p1 = Pattern.compile(keySet);
    p2 = Pattern.compile(KV_DELIMITER);
    m1 = p1.matcher(string);
    m2 = p2.matcher(string);

    while (m1.find(currentPos)) {
      startpos = m1.start();
      tmppos = m1.end();
      if (m2.find(tmppos)) {
        endpos = m2.start();
      } else {
        // if not next delimiter, get the max position
        endpos = string.length();
      }

      if (endpos < 0) {
        endpos = string.length() - 1;
      }

      String[] kvPair = string.substring(startpos + 1, endpos).split("=", 2);
      if (!kvMap.containsKey(kvPair[0])) {
        kvMap.put(kvPair[0], kvPair[1].equals("") ? null : kvPair[1]);
      }

      currentPos = endpos;

      // return if all tags found
      if (kvMap.size() == cleanKeys.size()) {
        return;
      }
    }
  }

  public static Map<String, String> getTagValues(String string, Collection<String> keys) {
    Map<String, String> kvMap = new HashMap<String, String>();
    getTagValues(string, keys, kvMap);

    return kvMap;
  }

  public static Map<String, String> getTagValues(String string, String[] keys) {
    if (keys == null) {
      return null;
    }

    HashSet<String> keySet = new HashSet<String>();
    for (int i = 0; i < keys.length; i++) {
      if (StringUtils.isNotBlank(keys[i])) {
        keySet.add(keys[i]);
      }
    }

    if (keySet.size() == 0) {
      return null;
    }

    return getTagValues(string, keySet);
  }

  public static void main(String[] args) {
    System.out.println(getTagValue("app=2571&c=1&g=bbffceff169e2254039923d001172c86&nid=&h=ff"
        + "&cguidsrc=cookie&n=bbffd7151690aad765a3bca7d819e376&uc=1&p=3084&uaid"
        + "=1a1fee4f1720a9b12a948c5bdcfe98faS0&bs=0&rvrid=2426431642212&t=3&cflgs=EA**&ul=en-US"
        + "&mppid=117&pn=2&pcguid=bbffd7151690aad765a3bca7d819e376&rq=6a7d991ef6510251&pagename"
        + "=EntryTracking&ciid=H%2B5PEqk*","mppid"));
  }
}
