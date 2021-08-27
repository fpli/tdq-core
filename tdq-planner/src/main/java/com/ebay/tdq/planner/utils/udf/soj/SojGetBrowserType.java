package com.ebay.tdq.planner.utils.udf.soj;

import com.ebay.tdq.planner.utils.udf.utils.FunctionUtil;
import java.io.Serializable;

/**
 * Created by xiaoding on 2017/1/20.
 */
public class SojGetBrowserType implements Serializable {

  public String evaluate(String userAgent) {
    if (userAgent == null) {
      return ("NULL UserAgent");
    }
    if ("".equals(userAgent)) {
      return "NULL UserAgent";
    } else if (FunctionUtil.isContain(userAgent, "Firefox")) {
      return "Firefox";
    } else if (FunctionUtil.isContain(userAgent, "Opera")) {
      return "Opera";
    } else if (FunctionUtil.isContain(userAgent, "Safari") && !FunctionUtil.isContain(userAgent, "Chrome/")) {
      return "Safari";
    } else if (FunctionUtil.isContain(userAgent, "WebTV")) {
      return "WebTV/MSNTV";
    } else if (FunctionUtil.isContain(userAgent, "Netscape")) {
      return "Netscape";
    } else if (FunctionUtil.isContain(userAgent, "MSIE")) {
      if (FunctionUtil.isContain(userAgent, "MSIE 5.2") || FunctionUtil.isContain(userAgent, "MSIE 5.1")) {
        return "MacIE";
      } else {
        return "IE";
      }
    } else if (FunctionUtil.isContain(userAgent, "Mozilla/4")) {
      return "Netscape";
    } else if (FunctionUtil.isContain(userAgent, "Chrome")) {
      return "Chrome";
    } else {
      return "Unknown Browser Type";
    }
  }
}
