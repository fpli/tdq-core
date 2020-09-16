package com.ebay.sojourner.common.util;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;

public class SOJGetUrlParams {

  public static String getUrlParams(String url) {
    if (StringUtils.isBlank(url)) {
      return "";
    }

    // test valid url
    try {
      URL aURL = new URL(url);
    } catch (IOException e) {
      // if not valid url, return null
      return "";
    }

    // find char "?"
    int param_start_pos = url.indexOf("?");
    if (param_start_pos > 0) {
      return url.substring(++param_start_pos);
    } else {
      return "";
    }
  }
}
