package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SojParseRlogid implements Serializable {

  private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final String UTC = "UTC";
  public static int[] s_xors = {6, 7, 3, 5};
  private DateTimeFormatter formatter = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT)
      .withZone(
          DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT-7")));

  public String evaluate(String rlogId, String infoType) {
    if (StringUtils.isBlank(rlogId) || StringUtils.isBlank(infoType)) {
      return null;
    }

    infoType = infoType.toLowerCase();
    if (!"timestamp".equals(infoType)
        && !"threadid".equals(infoType)
        && !"pool".equals(infoType)
        && !"machine".equals(infoType)
        && !"datacenter".equals(infoType)
        && !"poolmachine".equals(infoType)) {
      return null;
    }

    String[] splitStr = rlogId.split("-");
    int index = 0;
    String encodedStr = splitStr[index++];
    String timestampStr = index < splitStr.length ? splitStr[index++] : null;
    String threadidStr = index < splitStr.length ? splitStr[index++] : null;
    if (StringUtils.isBlank(encodedStr) || StringUtils.isBlank(timestampStr) || StringUtils.isBlank(threadidStr)) {
      return null;
    }

    if ("timestamp".equals(infoType)) {
      long timestamp = Long.parseLong(timestampStr, 16);
      Date date = new Date(timestamp);

      return (formatter.print(date.getTime()));
    }

    if ("threadid".equals(infoType)) {
      return (threadidStr);
    }

    String decodedStr = null;
    try {
      decodedStr = URLDecoder.decode(encodedStr, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return null;
    }
    String poolmachine = null;
    if (decodedStr != null) {
      poolmachine = decrypt(decodedStr);
    }
    if ("poolmachine".equals(infoType)) {
      return (poolmachine);
    }

    int end = -1;
    for (int i = 0; i < poolmachine.length() - 1; i++) {
      if (poolmachine.charAt(i) == ':' && poolmachine.charAt(i + 1) == ':') {
        end = i;
        break;
      }
    }
    if (end == -1) {
      return null;
    }

    if ("pool".equals(infoType)) {
      if (end == -1 || end == 0) {
        return null;
      } else {
        return (poolmachine.substring(0, end));
      }
    }

    if (end == -1 || end + 1 == poolmachine.length() - 1) {
      return null;
    } else {

      String machine = poolmachine.substring(end + 2);
      if ("machine".equals(infoType)) {
        return (machine);
      } else if ("datacenter".equals(infoType)) {
        String datacenter = getColoForStratusEnvironment(machine);
        return (datacenter);
      }

    }

    return null;
  }


  public String decrypt(String str) {

    char[] dest = new char[str.length()];
    for (int i = 0; i < str.length(); i++) {
      char k = str.charAt(i);
      dest[i] = (char) (k ^ (s_xors[i % 4]));
    }
    return new String(dest);
  }

  public String getColoForStratusEnvironment(String machine) {
    if (machine.length() < 3) {
      return "unknown";
    }

    String colo = machine.substring(0, 3).toLowerCase();

    // For Rover
    if (colo.equals("slc")) {
      return "SLC";
    } else if (colo.equals("phx")) {
      return "PHX";
    } else if (colo.equals("lvs")) {
      return "LVS";
    } else if (colo.equals("rno")) {
      return "RNO";
    } else // for v4
      if (colo.equals("rc-") || colo.equals("sr-") ||
          colo.equals("sq-")) {
        return "smf";
      } else if (colo.equals("hr-")) {
        return "den";
      } else if (colo.equals("sj-")) {
        return "sjc";
      } else if (colo.equals("ct-")) {
        return "den";
      } else if (colo.equals("ph-")) {
        return "phx";
      } else if (colo.equals("px-")) {
        return "phx";
      }
    return "unknown";
  }
}
