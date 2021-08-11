package com.ebay.tdq.planner.utils.udf.soj;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by qingxu1 on 2017/7/3.
 */
public class SojMd5Hash {

  public static final String MD5_ALGORITHM = "MD5";
  private static MessageDigest msgDigest;

  static {
    try {
      msgDigest = MessageDigest.getInstance(MD5_ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static Integer sojMd5Hash128(String guid, String constant_id, String experiment_id, int mod_value) {
    int const_len;
    int exp_len;
    int hash = 0;
    BigInteger[] bigValue = new BigInteger[4];
    StringBuffer sb = new StringBuffer();

    if (constant_id == null) {
      const_len = 0;
    } else {
      const_len = constant_id.length();
    }

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

    for (int i = 0; i < 4; i++) {
      preserved1[i] = (byte) (result[i] & -1);
    }
    bigValue[0] = new BigInteger(1, preserved1);

    for (int i = 0; i < 4; i++) {
      preserved2[i] = (byte) (result[4 + i] & -1);
    }

    bigValue[1] = new BigInteger(1, preserved2);

    for (int i = 0; i < 4; i++) {
      preserved3[i] = (byte) (result[8 + i] & -1);
    }
    bigValue[2] = new BigInteger(1, preserved3);

    for (int i = 0; i < 4; i++) {
      preserved4[i] = (byte) (result[12 + i] & -1);
    }
    bigValue[3] = new BigInteger(1, preserved4);

    for (int i = 0; i < 4; i++) {
      hash = (int) (31 * hash + (bigValue[i].intValue() & -1));
    }

    if (hash <= 0) {
      hash = hash * -1;
    }

    return (BigInteger.valueOf(hash).mod(BigInteger.valueOf(mod_value)).intValue());

  }

  public static Integer sojMd5Hash32(String guid, String constant_id, String experiment_id, int mod_value) {
    int const_len;
    int exp_len;
    int hash = 0;
    StringBuffer sb = new StringBuffer();

    if (constant_id == null) {
      const_len = 0;
    } else {
      const_len = constant_id.length();
    }

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

    for (int i = 0; i < 4; i++) {
      preserved[i] = (byte) (result[12 + i] & -1);
    }
    BigInteger bigValue = new BigInteger(1, preserved);

    hash = bigValue.intValue();

    if (hash <= 0) {
      hash = hash * -1;
    }

    return (BigInteger.valueOf(hash).mod(BigInteger.valueOf(mod_value)).intValue());

  }

  public static Integer sojJavaHash(String guid, String constant_id, String experiment_id, int mod_value) {
    int const_len;
    int exp_len;
    int hash = 0;
    StringBuffer sb = new StringBuffer();

    if (constant_id == null) {
      const_len = 0;
    } else {
      const_len = constant_id.length();
    }

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

    char[] a = sb.toString().toCharArray();
    int multiplier = 1;

    for (int i = (sb.toString().length() - 1); i >= 0; i--) {
      hash += (a[i] * multiplier);
      multiplier = (multiplier << 5) - multiplier;
    }

    if (hash <= 0) {
      hash = hash * -1;
    }

    return (BigInteger.valueOf(hash).mod(BigInteger.valueOf(mod_value)).intValue());

  }


}
