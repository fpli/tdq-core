package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class GuidSampling implements Serializable {

  public static final int MOD_VALUE = 1000;
  public static final int FETCH_BIT = 10;

  /**
   * Fetch the sample data by requiring how many percents, and the sampling is based on mod on guid.  Please consider
   * adding partition to narrow the data volume.
   *
   * @param guid the guid text
   * @param pct  how many percents required.
   * @return true, it belongs to the sample data.
   */
  public Boolean evaluate(final String guid, int pct) {
    if (guid == null) {
      return (false);
    }
    int modValue = SOJSampleHash.sampleHash(guid, MOD_VALUE) / FETCH_BIT;
    return modValue < pct;
  }
}
