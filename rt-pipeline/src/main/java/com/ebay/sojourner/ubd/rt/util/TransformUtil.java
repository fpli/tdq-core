package com.ebay.sojourner.ubd.rt.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class TransformUtil {

  private static final Map<Character, Integer> dict = new HashMap<>();
  private static final int LEN = 16;
  private static final int BIT_OFF = 4;
  private static final int OFFSET = 10;

  static{
    for(int i=0;i<10;++i){
      dict.put((char)('0'+i), i);
    }
    for(int i=0;i<6;++i){
      dict.put((char)('a'+i), OFFSET+i);
    }
  }


  public static Long[] stringToLong(CharSequence cmd) {
    if (cmd == null) {
      return new Long[]{0L, 0L};
    }
    if (StringUtils.isBlank(cmd)) {
      return new Long[]{0L, 0L};
    }
    String md = cmd.toString();
    // f64d4e2916e0a4e2873791feff756a5c
    int firstLen = LEN;
    int secondLen = 0;
    if (md.length() <= LEN) {
      firstLen = md.length();
    } else {
      secondLen = md.length();
    }
    long part1 = getOnelong(md.substring(0, firstLen));
    long part2 = 0L;
    if (secondLen > 0) {
      part2 = getOnelong(md.substring(LEN, secondLen));
    }
    return new Long[]{part1, part2};
  }

  public static long getOnelong(String part) {
    long res = 0;
    for (int i = 0; i < part.length(); ++i) {
      char c = part.charAt(i);
      Integer bitC = dict.get(c);
      if (bitC == null) {
        // change behavior, convert illegal char. instead of return the whole as null
        bitC = Math.abs(c % 16);
      }
      long bb = bitC;
      res |= (bb << (i * BIT_OFF));
    }
    return res;
  }
}
