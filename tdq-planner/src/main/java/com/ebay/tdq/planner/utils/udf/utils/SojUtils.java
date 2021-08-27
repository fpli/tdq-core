package com.ebay.tdq.planner.utils.udf.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SojUtils {

  protected static final HashSet<String> INVALILD_STRING = new HashSet<String>(
      Arrays.asList("segname", "crd", "bu", "$", "<"));

  public static Map<String, String> parsePayload(String payload, String searchPayloadFeed, int decodeLevel) {
    if (payload == null) {
      return new HashMap<String, String>();
    }

    // put search seeds in a map to lookup later
    String[] searchPayload = searchPayloadFeed.split(",");
    Map<String, List<String>> searchs = new LinkedHashMap<String, List<String>>();

    for (String p : searchPayload) {
      String[] keyMultiValue = p.split("=");
      if (keyMultiValue.length == 1) {
        searchs.put(keyMultiValue[0], Arrays.asList(keyMultiValue[0]));
      } else if (keyMultiValue.length == 2) {
        searchs.put(keyMultiValue[0], Arrays.asList(keyMultiValue[1].split("\\|")));
      }
    }

    return decodeAndParse(payload, searchs, decodeLevel);

  }

  private static Map<String, String> decodeAndParse(String payload, Map<String, List<String>> searchs,
      int decodeLevel) {

    Map<String, String> payloadMap = new LinkedHashMap<String, String>();

    String firstDecodedUrl;
    try {
      if (decodeLevel == 0) {
        firstDecodedUrl = payload;
      } else {
        firstDecodedUrl = java.net.URLDecoder.decode(payload, "UTF-8");
      }
    } catch (Exception e) {
      firstDecodedUrl = payload;
    }

    for (String key : searchs.keySet()) {
      List<String> schValues = searchs.get(key);
      for (String v : schValues) {
        String value = fetchValue(firstDecodedUrl, v, "&");

        if (!isEmpty(value)) {
          payloadMap.put(key, value);
          break;
        }
      }

      if (isEmpty(payloadMap.get(key))) {
        if (decodeLevel == 2) {
          try {
            firstDecodedUrl = java.net.URLDecoder.decode(firstDecodedUrl, "UTF-8");
          } catch (UnsupportedEncodingException e) {
            // ignore
          }

          List<String> schValues2 = searchs.get(key);
          for (String v : schValues2) {
            String value = fetchValue(firstDecodedUrl, v, "&");

            if (!isEmpty(value)) {
              payloadMap.put(key, value);
              break;
            }
          }
        }
      }
    }

    return payloadMap;
  }

  private static boolean isEmpty(String cs) {
    return cs == null || cs.length() == 0;
  }

  protected static String fetchValue(String sid, String start, String... ends) {
    return fetchValue(sid, 0, start, ends);
  }

  protected static String fetchValue(String sid, int startPos1, String start, String... endChars) {
    if (isEmpty(sid)) {
      return null;
    }
    int startPos = findValuePosition(sid, startPos1, start);
    if (startPos < 0) {
      return null;
    }

    int minEndPos = -1;
    for (String end : endChars) {
      int endPos = sid.indexOf(end, startPos);
      if (endPos > 0) {
        if (minEndPos < 0) {
          minEndPos = endPos;
        } else {
          minEndPos = Math.min(minEndPos, endPos);
        }
      }
    }

    if (minEndPos < 0) { // end is optional
      minEndPos = sid.length();
    }

    String value = sid.substring(startPos, minEndPos);
    if (!isValid(value)) {
      return fetchValue(sid, minEndPos, start, endChars);
    } else {
      return value;
    }
  }

  private static int findValuePosition(String sid, int startPos1, String start) {
    //add & and = to the key
    String ampedStart = "&" + start + "=";
    String questionedStart = "?" + start + "=";

    int questionStartPos = sid.indexOf(questionedStart, startPos1);
    int ampStartPos = sid.indexOf(ampedStart, startPos1);

    int foundPos = Math.max(questionStartPos, ampStartPos);

    if (foundPos < 0) { // must start from begin
      return foundPos;
    } else {
      return foundPos + ampedStart.length();
    }
  }

  public static boolean isValid(String input) {
    if (INVALILD_STRING.contains(input)) {
      return false;
    }
    return true;
  }
}