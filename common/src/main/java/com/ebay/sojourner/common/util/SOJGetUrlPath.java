package com.ebay.sojourner.common.util;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;

public class SOJGetUrlPath {

  /*
   * this function is to get path from a url string
   */
  public static String getUrlPath(String urlString) {
    if (StringUtils.isBlank(urlString)) {
      return "";
    }
    URL url;

    try {
      url = new URL(urlString);
    } catch (IOException e) {
      return "";
    }

    return url.getPath();
  }
}
