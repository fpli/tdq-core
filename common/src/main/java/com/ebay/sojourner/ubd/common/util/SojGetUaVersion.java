package com.ebay.sojourner.ubd.common.util;

import java.util.HashSet;

/**
 * Created by xiaoding on 2017/1/20.
 */
public class SojGetUaVersion {

  public static String getUaVersion(String userAgent, int startPos) {
    int len = userAgent.length();
    HashSet<String> deliSet = new HashSet<String>();
    deliSet.add(";");
    deliSet.add(")");

    if (startPos > len - 1) {
      return " ";
    } else if (deliSet.contains(userAgent.substring(startPos, startPos + 1))) {
      return " ";
    } else {
      String subUserAgent = userAgent.substring(startPos);
      System.out.println("subUserAgent:" + subUserAgent);
      int minPos = getMinStartPos(subUserAgent, " /");
      char minPosChar = getMinStartChar(subUserAgent, " /");
      if (minPos == Integer.MAX_VALUE) {
        return " ";
      }
      String startUserAgent = subUserAgent.substring(minPos + 1);

      System.out.println("startUserAgent:" + startUserAgent);
      int minEnd = getMinStartPos(startUserAgent, ";/)([ -+,");
      char minEndChar = getMinStartChar(startUserAgent, ";/)([ -+,");
      System.out.println("minPos:" + minPos);
      System.out.println("minPosChar:" + minPosChar);
      System.out.println("minEnd:" + minEnd);
      System.out.println("minEndChar:" + minEndChar);
      String subStr =
          SOJStrBetweenEndlist.getStringBetweenEndList(subUserAgent, minPosChar, minEndChar);
      if (subStr == null) {
        return " ";
      } else {
        return subStr;
      }
    }
  }

  private static char getMinStartChar(String subUserAgent, String chars) {
    char[] arr = chars.toCharArray();
    int min = Integer.MAX_VALUE;
    char minChar = ' ';
    for (int i = 0; i < arr.length; i++) {
      int midIndex = subUserAgent.indexOf(arr[i]);
      if (midIndex < min && midIndex != -1) {
        min = midIndex;
        minChar = arr[i];
      }
    }
    return minChar;
  }

  private static int getMinStartPos(String subUserAgent, String chars) {
    char[] arr = chars.toCharArray();
    int min = Integer.MAX_VALUE;
    char minChar = ' ';
    for (int i = 0; i < arr.length; i++) {
      int midIndex = subUserAgent.indexOf(arr[i]);
      if (midIndex < min && midIndex != -1) {
        min = midIndex;
        minChar = arr[i];
      }
    }
    return min;
  }

  public static void main(String[] args) {
    //         String str=new String("android.webview");
    //
    //
    // System.out.println("----"+getUaVersion(str,str.indexOf("android")+5).toString()+"+++");
  }
}
