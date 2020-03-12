package com.ebay.sojourner.ubd.common.sharedlib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yunjzhang search the partten "&key={value}" in the source string if match then return
 * "value"
 */
public class SOJParseClientInfo {

  public static final String keyWords =
      "ForwardedFor|RemoteIP|Referer|ContentLength|Script|Server|Agent|Encoding|TPool|"
          + "TStamp|TType|TName|TStatus|TDuration|TPayload|TMachine";
  public static final char kvpDelimiter = '&', kvDelimiter = '=';
  public static final String tSearchWords = "&" + keyWords.replace("|", "=|" + kvpDelimiter) + "=";
  static Pattern p1 = Pattern.compile(keyWords), p2 = Pattern.compile(tSearchWords);

  public static String getClientInfo(String clientinfo, String key) {
    if (StringUtils.isBlank(clientinfo)) {
      return null;
    }

    if (StringUtils.isBlank(key) || isValidCIname(key) == 0) {
      return null;
    }

    Matcher m1 = p2.matcher(clientinfo);
    int valueStartPos = -1;
    int delStartPos = -1;
    int matchStartPos = 0;

    // if leading key, not '&' need
    if (clientinfo.startsWith(key + kvDelimiter)) {
      valueStartPos = (key + kvDelimiter).length();
      matchStartPos = valueStartPos;
      while (m1.find(matchStartPos) && delStartPos == -1) {
        delStartPos = m1.start();
        if (clientinfo.charAt(delStartPos - 1) == '&') {
          matchStartPos = delStartPos + 1;
          delStartPos = -1;
        }
      }
      if (delStartPos == -1) {
        delStartPos = clientinfo.length();
      }
      return (clientinfo.substring(valueStartPos, delStartPos));
    } else if (clientinfo.indexOf("&" + key + kvDelimiter) >= 0) {
      int p = clientinfo.indexOf("&" + key + kvDelimiter);
      valueStartPos = p + ("&" + key + kvDelimiter).length();
      matchStartPos = valueStartPos;

      while (m1.find(matchStartPos) && delStartPos == -1) {
        delStartPos = m1.start();
        if (clientinfo.charAt(delStartPos - 1) == '&') {
          matchStartPos = delStartPos + 1;
          delStartPos = -1;
        }
      }
      if (delStartPos == -1) {
        delStartPos = clientinfo.length();
      }
      return (clientinfo.substring(valueStartPos, delStartPos));
    }

    return null;
  }

  private static int isValidCIname(String key) {
    Matcher m1 = p1.matcher(key);
    if (m1.find()) {
      return 1;
    }
    return 0;
  }
}
