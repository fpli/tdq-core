package com.ebay.sojourner.ubd.util.sojlib;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SOJURLDecodeEscape {
  public static int hexlookup[] = { 
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 0-15
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 16-31
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 32-47
    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, // 48-63
    -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 64-79
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 80-95
    -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 96-111
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 112-127
  };

  /**
   * this func cannot suppot no-ascii chars, recommend to use javaNetUrlDecode
   * @param url
   * @param esc_char
   * @return String
   */
  public static String decodeEscapes(String url, char esc_char) {
    if (StringUtils.isBlank(url)) {
      return null;
    }

    int i = 0;
    char src[] = null;
    int srcend;
    int h1;
    int h2;

    StringBuilder ret = new StringBuilder();

    src = url.toCharArray();
    srcend = url.length();

    while (i < srcend) {
      if (src[i] == esc_char) {
        // if it is escape char make sure there are two more characters
        // left
        if (i + 2 >= srcend) {
          ret.append(src, i, srcend - i);
          break;
        }

        h1 = sg_get_hex(src[i + 1]);

        h2 = sg_get_hex(src[i + 2]);

        // if one of them is not a valid hex value copy the content
        if (h1 < 0 || h2 < 0) {
          ret.append(src, i, 3);
        } else {
          // multiply by 16
          h1 = h1 << 4;
          ret.append((char) (h1 | h2));
        }
        // skip three char
        i += 3;
      } else {
        ret.append(src[i++]);
      }
    }
    return ret.toString();
  }

  private static int sg_get_hex(char c) {
    int res = -1;

    if (c > 0 && c < 127)
      res = hexlookup[c];

    return res;
  }

  /**
   * UrlDecode first to support utf-8 
   * if failed on UnsupportedEncodingException or IllegalArgumentException, use old func instead
   * @param url
   * @param enc
   * @return
   */
  public static String javaNetUrlDecode(String url, String enc){
    if (StringUtils.isBlank(url)) {
      return null;
    }
    
    try {
      return URLDecoder.decode(url, enc);
    } catch (UnsupportedEncodingException e) {
      return decodeEscapes(url, '%');
    } catch (IllegalArgumentException e){
      return decodeEscapes(url, '%');
    }
  }
}
