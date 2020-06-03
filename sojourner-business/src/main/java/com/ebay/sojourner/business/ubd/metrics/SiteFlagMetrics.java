package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.BitUtils;

public class SiteFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private static final Integer NUMBER_SITE_FLAG = 47;

  // idx 0~46 used
  // private BitSet siteFlags = new BitSet();
  //  private int[] siteFlags = {
  //      0, 0, 0, 0, 0, 0, 0, 0,
  //      0, 0, 0, 0, 0, 0, 0, 0,
  //      0, 0, 0, 0, 0, 0, 0, 0,
  //      0, 0, 0, 0, 0, 0, 0, 0,
  //      0, 0, 0, 0, 0, 0, 0, 0,
  //      0, 0, 0, 0, 0, 0, 0, 0,
  //      0, 0, 0, 0, 0, 0, 0, 0,
  //      0, 0, 0, 0, 0, 0, 0, 0
  //  };

  public static void main(String[] args) {
    SiteFlagMetrics siteFlagMetrics = new SiteFlagMetrics();

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setSiteId(0);
    SessionAccumulator sessionAccumulator = new SessionAccumulator();
    //    siteFlagMetrics.start(sessionAccumulator);
    //    siteFlagMetrics.feed(ubiEvent, sessionAccumulator);
    //    ubiEvent.setSiteId(77);
    //    siteFlagMetrics.feed(ubiEvent, sessionAccumulator);
    //    ubiEvent.setSiteId(101);
    //    siteFlagMetrics.feed(ubiEvent, sessionAccumulator);
    //    ubiEvent.setSiteId(102);
    //    siteFlagMetrics.feed(ubiEvent, sessionAccumulator);
    //    ubiEvent.setSiteId(103);
    //    siteFlagMetrics.feed(ubiEvent, sessionAccumulator);
    //    siteFlagMetrics.end(sessionAccumulator);
    //    System.out.println(sessionAccumulator.getUbiSession().getSiteFlags());
    siteFlagMetrics.start(sessionAccumulator);
    ubiEvent.setSiteId(77);
    siteFlagMetrics.feed(ubiEvent, sessionAccumulator);
    //    siteFlagMetrics.end(sessionAccumulator);
    System.out.println(sessionAccumulator.getUbiSession().getSiteFlags());

  }

  void setSiteFlag(int siteId, SessionAccumulator sessionAccumulator) {
    long siteFlagsMid = sessionAccumulator.getUbiSession().getSiteFlags();
    switch (siteId) {
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 20:
      case 163:
      case 500:
      case 970:
      case 971:
      case 972:
      case 973:
      case 980:
      case 981:
      case 982:
      case 983:
      case 984:
      case 985:
      case 986:
      case 987:
      case 989:
      case 990:
      case 991:
      case 992:
      case 993:
      case 994:
      case 995:
      case 996:
      case 997:
      case 998:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 0);
        break;
      case 0:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 1);
        break;
      case 1:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 2);
        break;
      case 2:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 3);
        break;
      case 3:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 4);
        break;
      case 15:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 5);
        break;
      case 16:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 6);
        break;
      case 23:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 7);
        break;
      case 37:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 8);
        break;
      case 71:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 9);
        break;
      case 77:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 10);
        break;
      case 100:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 11);
        break;
      case 101:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 12);
        break;
      case 104:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 13);
        break;
      case 123:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 14);
        break;
      case 146:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 15);
        break;
      case 186:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 16);
        break;
      case 193:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 17);
        break;
      case 196:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 18);
        break;
      case 197:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 19);
        break;
      case 198:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 20);
        break;
      case 199:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 21);
        break;
      case 200:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 22);
        break;
      case 201:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 23);
        break;
      case 202:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 24);
        break;
      case 203:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 25);
        break;
      case 204:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 26);
        break;
      case 205:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 27);
        break;
      case 206:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 28);
        break;
      case 207:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 29);
        break;
      case 208:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 30);
        break;
      case 209:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 31);
        break;
      case 210:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 32);
        break;
      case 211:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 33);
        break;
      case 212:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 34);
        break;
      case 213:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 35);
        break;
      case 214:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 36);
        break;
      case 215:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 37);
        break;
      case 216:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 38);
        break;
      case 217:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 39);
        break;
      case 218:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 40);
        break;
      case 219:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 41);
        break;
      case 220:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 42);
        break;
      case 221:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 43);
        break;
      case 223:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 44);
        break;
      case 224:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 45);
        break;
      case 225:
        siteFlagsMid = BitUtils.setBit(siteFlagsMid, 46);
        break;
    }
    sessionAccumulator.getUbiSession().setSiteFlags(siteFlagsMid);
  }

  //  @Override
  //  public String toString() {
  //    StringBuilder sb = new StringBuilder();
  //    sb.append("{");
  //    for (int i = 0; i < NUMBER_SITE_FLAG - 1; i++) {
  //      sb.append(siteFlags[i]).append(",");
  //    }
  //    sb.append(siteFlags[NUMBER_SITE_FLAG - 1]) .append("}");
  //    return sb.toString();
  //  }

  //  public void initSiteFlags() {
  //    Arrays.fill(siteFlags, (byte) 0);
  //  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setSiteFlags(0L);
  }

  @Override
  public void feed(UbiEvent ubiEvent, SessionAccumulator sessionAccumulator) {
    int siteId = ubiEvent.getSiteId();
    if (siteId != -1) {
      setSiteFlag(siteId, sessionAccumulator);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
