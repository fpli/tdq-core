package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class SojUrlExtractNvp implements Serializable {

  private SojExtractNVP nvpExtract;

  public SojUrlExtractNvp() {
    super();
    nvpExtract = new SojExtractNVP();
  }

  public String evaluate(String urlStr, String namePair, int format) {
    if (StringUtils.isBlank(urlStr) || StringUtils.isBlank(namePair)) {
      return null;
    }

    if (format == 0) {
      if (nvpExtract.evaluate(urlStr, namePair, "&", "=") != null) {
        return nvpExtract.evaluate(urlStr, namePair, "&", "=").toString();
      } else {
        return null;
      }
    } else if (format == 1) {
      String patternStr = String.format("((QQ%sZ)|(&%s=))((.*?)(QQ|&|$))", namePair, namePair);
      Pattern pattern = Pattern.compile(patternStr);
      Matcher m = pattern.matcher(urlStr);
      if (m.find()) {
        return m.group(5);
      } else {
        return null;
      }
    } else if (format == 2) {
      String key = namePair.startsWith("_") ? namePair.substring(1) : namePair;

      String patternStr = String.format("(QQ?%sZ)((.*?)(QQ|$))", key, key);
      Pattern pattern = Pattern.compile(patternStr);
      Matcher m = pattern.matcher(urlStr);
      if (m.find()) {
        return m.group(3);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
