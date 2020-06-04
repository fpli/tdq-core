package com.ebay.sojourner.common.util;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;

public class SOJGetUrlDomain {

  /*
   * this function is to get domain/host from a url string
   */
  public static String getUrlDomain(String urlString) {
    if (StringUtils.isBlank(urlString)) {
      return "";
    }

    URL url;

    try {
      url = new URL(urlString);
    } catch (IOException e) {
      return "";
    }

    return url.getHost();
  }

  public static void main(String[] args) {
    System.out.println(getUrlDomain("https://www.ebay-kleinanzeigen"
        + ".de/s-wohnung-mieten/helmstedt/c203l2986r30+wohnung_mieten.zimmer_d:4,")
        .matches(".*(ebay|\\.kijiji\\.).*"));
  }
}