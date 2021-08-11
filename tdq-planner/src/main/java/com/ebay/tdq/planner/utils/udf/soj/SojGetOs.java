package com.ebay.tdq.planner.utils.udf.soj;


import com.ebay.tdq.planner.utils.udf.utils.FunctionUtil;
import java.io.Serializable;

/**
 * Created by xiaoding on 2017/1/20.
 */
//
public class SojGetOs implements Serializable {

  public String evaluate(String str_vec) {
    if (str_vec == null) {
      return ("unknown");
    }
    String strUpper = str_vec.toUpperCase();
    if (FunctionUtil.isContain(strUpper, "Windows-NT".toUpperCase())) {
      return ("Windows NT");
    } else if (FunctionUtil.isContain(strUpper, "Windows 7".toUpperCase()) || FunctionUtil
        .isContain(strUpper, "Windows NT 6.1".toUpperCase())) {
      return ("Windows 7");
    } else if (FunctionUtil.isContain(strUpper, "Windows NT 6.0".toUpperCase())) {
      return ("Windows Vista");
    } else if (FunctionUtil.isContain(strUpper, "Windows NT 6".toUpperCase())) {
      return ("Windows 7");
    } else if (FunctionUtil.isContain(strUpper, "Windows NT 5.2".toUpperCase())) {
      return ("Windows 2003");
    } else if (FunctionUtil.isContain(strUpper, "Windows NT 5.1".toUpperCase())) {
      return ("Windows XP");
    } else if (FunctionUtil.isContain(strUpper, "Windows NT 5.0".toUpperCase()) || FunctionUtil
        .isContain(strUpper, "Windows NT 5".toUpperCase())) {
      return ("Windows 2000");
    } else if (FunctionUtil.isContain(strUpper, "Windows NT 4".toUpperCase()) || FunctionUtil
        .isContain(strUpper, "Windows NT".toUpperCase()) || FunctionUtil.isContain(strUpper, "WinNT".toUpperCase())) {
      return ("Windows NT");
    } else if (FunctionUtil.isContain(strUpper, "Win98".toUpperCase())) {
      return ("Windows 98");
    } else if (FunctionUtil.isContain(strUpper, "Win95".toUpperCase())) {
      return ("Windows 95");
    } else if (FunctionUtil.isContain(strUpper, "Win9x".toUpperCase())) {
      return ("Windows 9x");
    } else if (FunctionUtil.isContain(strUpper, "Win31".toUpperCase())) {
      return ("Windows 3.1");
    } else if (FunctionUtil.isContain(strUpper, "Windows CE".toUpperCase())) {
      return ("Windows CE ");
    } else if (FunctionUtil.isContain(strUpper, "Windows ME".toUpperCase())) {
      return ("Windows ME");
    } else if (FunctionUtil.isContain(strUpper, "Windows mo".toUpperCase())) {
      return ("Windows mobile");
    } else if (FunctionUtil.isContain(strUpper, "Windows phone".toUpperCase())) {
      return ("Windows phone OS");
    } else if (FunctionUtil.isContain(strUpper, "Windows".toUpperCase()) || FunctionUtil
        .isContain(strUpper, "Win".toUpperCase())) {
      return ("Windows ");
    } else if (FunctionUtil.isContain(strUpper, "Mac OS X".toUpperCase())) {
      if (FunctionUtil.isContain(strUpper, "iPhone".toUpperCase())) {
        if (FunctionUtil.isContain(strUpper, "iPod".toUpperCase())) {
          return (
              "iOS iPod " + SojGetUaVersion.getUaVersion(str_vec, strUpper.indexOf("iPhone OS".toUpperCase()) + 9));
        } else {
          return (
              "iOS iPhone " + SojGetUaVersion.getUaVersion(str_vec, strUpper.indexOf("iPhone OS".toUpperCase()) + 9));

        }

      } else if (FunctionUtil.isContain(strUpper, "iPad".toUpperCase())) {
        return (
            "iOS iPad " + SojGetUaVersion.getUaVersion(str_vec, strUpper.indexOf("CPU OS".toUpperCase()) + 6));
      } else {
        return (
            "MacOSX " + SojGetUaVersion.getUaVersion(str_vec, strUpper.indexOf("Mac OS X".toUpperCase()) + 8));
      }

    } else if (FunctionUtil.isContain(strUpper, "Android".toUpperCase())) {
      return (
          "Android " + SojGetUaVersion.getUaVersion(str_vec, strUpper.indexOf("Android".toUpperCase()) + 7));

    } else if (FunctionUtil.isContain(strUpper, "Mac PowerPC".toUpperCase()) || FunctionUtil
        .isContain(strUpper, "Macintosh".toUpperCase())) {
      return ("Mac OS");
    } else if (FunctionUtil.isContain(strUpper, "FreeBSD".toUpperCase())) {
      return ("FreeBSD");
    } else if (FunctionUtil.isContain(strUpper, "OpenBSD".toUpperCase())) {
      return ("OpenBSD");
    } else if (FunctionUtil.isContain(strUpper, "NetBSD".toUpperCase())) {
      return ("NetBSD");
    } else if (FunctionUtil.isContain(strUpper, "BSD".toUpperCase())) {
      return ("BSD");
    } else if (FunctionUtil.isContain(strUpper, "Linux".toUpperCase())) {
      if (FunctionUtil.isContain(strUpper, "Ubuntu".toUpperCase())) {
        return (
            "Ubuntu " + SojGetUaVersion.getUaVersion(str_vec, strUpper.indexOf("Ubuntu".toUpperCase()) + 6));
      } else {
        return (
            "Linux " + SojGetUaVersion.getUaVersion(str_vec, strUpper.indexOf("Linux".toUpperCase()) + 5));
      }
    } else if (FunctionUtil.isContain(strUpper, "CentOS".toUpperCase())) {
      return ("CentOS");
    } else if (FunctionUtil.isContain(strUpper, "Unix".toUpperCase())) {
      return ("Linux");
    } else if (FunctionUtil.isContain(strUpper, "SunOS".toUpperCase())) {
      return ("SunOS");
    } else if (FunctionUtil.isContain(strUpper, "IRIX".toUpperCase())) {
      return ("IRIX");
    } else if (FunctionUtil.isContain(strUpper, "Symbian".toUpperCase())) {
      return ("Symbian OS");
    } else if (FunctionUtil.isContain(strUpper, "Nokia".toUpperCase())) {
      return ("Nokia");
    } else if (FunctionUtil.isContain(strUpper, "Sony".toUpperCase())) {
      return ("SonyEricsson");
    } else if (FunctionUtil.isContain(strUpper, "BeOS".toUpperCase())) {
      return ("BeOS");
    } else if (FunctionUtil.isContain(strUpper, "BlackBerry".toUpperCase())) {
      return ("BlackBerry");
    } else if (FunctionUtil.isContain(strUpper, "Wii".toUpperCase())) {
      return ("Nintendo Wii");
    } else if (FunctionUtil.isContain(strUpper, "Nintendo DS".toUpperCase())) {
      return ("Nintendo DS");
    } else if (FunctionUtil.isContain(strUpper, "Nintendo".toUpperCase())) {
      return ("Nintendo");
    } else if (FunctionUtil.isContain(strUpper, "playstation".toUpperCase())) {
      return ("playstation");
    } else if (FunctionUtil.isContain(strUpper, "webOS".toUpperCase())) {
      return ("webOS");
    } else if (FunctionUtil.isContain(strUpper, "Brew".toUpperCase())) {
      return ("Brew");
    } else if (FunctionUtil.isContain(strUpper, "palmOS".toUpperCase())) {
      return ("palmOS");
    } else if (FunctionUtil.isContain(strUpper, "kindle".toUpperCase())) {
      return ("kindle");
    } else if (FunctionUtil.isContain(strUpper, "nook".toUpperCase())) {
      return ("nook");
    } else if (FunctionUtil.isContain(strUpper, "samsung".toUpperCase())) {
      return ("samsung");
    } else if (FunctionUtil.isContain(strUpper, "eBayi".toUpperCase())) {
      return ("iOS eBay App");
    } else if (FunctionUtil.isContain(strUpper, "eBayS".toUpperCase())) {
      return ("iOS eBay Selling App");
    } else if (FunctionUtil.isContain(strUpper, "eBayF".toUpperCase())) {
      return ("iOS eBay Fashion App");
    } else if (FunctionUtil.isContain(strUpper, "PayPal".toUpperCase())) {
      return ("iOS PayPal App");
    } else {
      return ("unknown");
    }
  }


}
