package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SampleHash implements Serializable {

  public static final String MD5_ALGORITHM = "MD5";
  private MessageDigest msgDigest;

  public SampleHash() {
    super();
    try {
      msgDigest = MessageDigest.getInstance(MD5_ALGORITHM);
    } catch (NoSuchAlgorithmException var1) {
      throw new RuntimeException(var1);
    }
  }


  public Integer evaluate(String guid, int mod_value) {
    if (guid == null || mod_value <= 0) {
      return null;
    }

    byte[] result = msgDigest.digest(guid.getBytes());
    byte[] preserved = new byte[8];

    for (int i = 0; i < 8; ++i) {
      preserved[i] = result[7 - i];
    }

    BigInteger bigValue = new BigInteger(1, preserved);
    return bigValue.mod(BigInteger.valueOf((long) mod_value)).intValue();
  }
}