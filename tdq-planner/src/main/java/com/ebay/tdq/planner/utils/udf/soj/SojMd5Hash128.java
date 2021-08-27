package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SojMd5Hash128 implements Serializable {

  public static final String MD5_ALGORITHM = "MD5";
  private MessageDigest msgDigest;

  public SojMd5Hash128() {
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

    return sojMd5Hash128Varchar(guid, constant_id, experiment_id, mod_value);
  }

  public Integer sojMd5Hash128Varchar(String guid, String constant_id, String experiment_id, Integer mod_value) {
    int hash = 0;
    BigInteger[] bigValue = new BigInteger[4];
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
    byte[] preserved1 = new byte[4];
    byte[] preserved2 = new byte[4];
    byte[] preserved3 = new byte[4];
    byte[] preserved4 = new byte[4];

    int i;
    for (i = 0; i < 4; ++i) {
      preserved1[i] = (byte) (result[i] & -1);
    }

    bigValue[0] = new BigInteger(1, preserved1);

    for (i = 0; i < 4; ++i) {
      preserved2[i] = (byte) (result[4 + i] & -1);
    }

    bigValue[1] = new BigInteger(1, preserved2);

    for (i = 0; i < 4; ++i) {
      preserved3[i] = (byte) (result[8 + i] & -1);
    }

    bigValue[2] = new BigInteger(1, preserved3);

    for (i = 0; i < 4; ++i) {
      preserved4[i] = (byte) (result[12 + i] & -1);
    }

    bigValue[3] = new BigInteger(1, preserved4);

    for (i = 0; i < 4; ++i) {
      hash = 31 * hash + (bigValue[i].intValue() & -1);
    }

    if (hash <= 0) {
      hash *= -1;
    }

    return BigInteger.valueOf((long) hash).mod(BigInteger.valueOf((long) mod_value)).intValue();
  }
}
