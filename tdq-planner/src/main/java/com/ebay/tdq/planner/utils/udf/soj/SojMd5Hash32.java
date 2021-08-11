package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SojMd5Hash32 implements Serializable {

  public static final String MD5_ALGORITHM = "MD5";
  private MessageDigest msgDigest;

  public SojMd5Hash32() {
    super();
    try {
      msgDigest = MessageDigest.getInstance(MD5_ALGORITHM);
    } catch (NoSuchAlgorithmException var1) {
      throw new RuntimeException(var1);
    }
  }

  public Integer evaluate(String guid, String constant_id, String experiment_id, Integer mod_value) {
    if (guid == null || mod_value == null || mod_value <= 0) {
      return null;
    }

    int hash = 0;
    StringBuilder sb = new StringBuilder();
    int const_len;
    if (constant_id == null) {
      const_len = 0;
    } else {
      const_len = constant_id.length();
    }

    int exp_len;
    if (experiment_id == null) {
      exp_len = 0;
    } else {
      exp_len = experiment_id.length();
    }

    sb.append(guid);
    if (exp_len > 0) {
      sb.append(experiment_id);
    }

    if (const_len > 0) {
      sb.append(constant_id);
    }

    msgDigest.reset();
    byte[] result = msgDigest.digest(sb.toString().getBytes(Charset.forName("UTF8")));
    byte[] preserved = new byte[4];

    for (int i = 0; i < 4; ++i) {
      preserved[i] = (byte) (result[12 + i] & -1);
    }

    BigInteger bigValue = new BigInteger(1, preserved);
    hash = bigValue.intValue();
    if (hash <= 0) {
      hash *= -1;
    }

    return BigInteger.valueOf((long) hash).mod(BigInteger.valueOf((long) mod_value)).intValue();
  }
}
