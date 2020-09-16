package com.ebay.sojourner.rt.pipeline;

public class Test {

  public static void main(String[] args) {
    System.out.println(get("guid"));
  }

  private static String get(String... strings) {
//    return strings.toString();
    return strings[0];
  }
}
