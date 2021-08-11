package com.ebay.tdq.planner.utils.udf.soj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.zip.GZIPInputStream;
import org.apache.commons.codec.binary.Base64;

public class DecodePlmtTag implements Serializable {

  public static String uncompress(String str) throws IOException {
    byte[] compressed = Base64.decodeBase64(str);
    if (compressed.length > 4) {
      GZIPInputStream gzipInputStream = null;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      try {
        gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressed, 4, compressed.length - 4));

        for (int value = 0; value != -1; ) {
          value = gzipInputStream.read();
          if (value != -1) {
            baos.write(value);
          }
        }
        return new String(baos.toByteArray(), "UTF-8");
      } finally {
        if (gzipInputStream != null) {
          gzipInputStream.close();
        }
        baos.close();
      }

    } else {
      return "";
    }
  }

  public static String decode(String sojData) throws IOException {
    String urlDecodedSoj = URLDecoder.decode(sojData, "UTF-8");
    String decodedSoj = uncompress(urlDecodedSoj);
    return decodedSoj;
  }

  public static String doubleDecode(String sojData) {
    try {
      return decode(URLDecoder.decode(sojData, "UTF-8"));
    } catch (Exception e) {
      return null;
    }
  }

  public String evaluate(String str) {
    if (str == null) {
      return null;
    }
    return doubleDecode(str);
  }

}
