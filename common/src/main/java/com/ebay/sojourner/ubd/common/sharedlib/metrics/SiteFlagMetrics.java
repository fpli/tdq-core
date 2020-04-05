package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.ByteArrayToNum;
import java.util.Arrays;

public class SiteFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private static final Integer NUMBER_SITE_FLAG = 47;

  // idx 0~46 used
  // private BitSet siteFlags = new BitSet();
  private byte[] siteFlags = {
      0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0
  };

  void setSiteFlag(Integer siteId, SessionAccumulator sessionAccumulator) {
    byte[] siteFlagsMid = sessionAccumulator.getUbiSession().getSiteFlagsSet();
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
        siteFlagsMid[0] = 1;
        break;
      case 0:
        siteFlagsMid[1] = 1;
        break;
      case 1:
        siteFlagsMid[2] = 1;
        break;
      case 2:
        siteFlagsMid[3] = 1;
        break;
      case 3:
        siteFlagsMid[4] = 1;
        break;
      case 15:
        siteFlagsMid[5] = 1;
        break;
      case 16:
        siteFlagsMid[6] = 1;
        break;
      case 23:
        siteFlagsMid[7] = 1;
        break;
      case 37:
        siteFlagsMid[8] = 1;
        break;
      case 71:
        siteFlagsMid[9] = 1;
        break;
      case 77:
        siteFlagsMid[10] = 1;
        break;
      case 100:
        siteFlagsMid[11] = 1;
        break;
      case 101:
        siteFlagsMid[12] = 1;
        break;
      case 104:
        siteFlagsMid[13] = 1;
        break;
      case 123:
        siteFlagsMid[14] = 1;
        break;
      case 146:
        siteFlagsMid[15] = 1;
        break;
      case 186:
        siteFlagsMid[16] = 1;
        break;
      case 193:
        siteFlagsMid[17] = 1;
        break;
      case 196:
        siteFlagsMid[18] = 1;
        break;
      case 197:
        siteFlagsMid[19] = 1;
        break;
      case 198:
        siteFlagsMid[20] = 1;
        break;
      case 199:
        siteFlagsMid[21] = 1;
        break;
      case 200:
        siteFlagsMid[22] = 1;
        break;
      case 201:
        siteFlagsMid[23] = 1;
        break;
      case 202:
        siteFlagsMid[24] = 1;
        break;
      case 203:
        siteFlagsMid[25] = 1;
        break;
      case 204:
        siteFlagsMid[26] = 1;
        break;
      case 205:
        siteFlagsMid[27] = 1;
        break;
      case 206:
        siteFlagsMid[28] = 1;
        break;
      case 207:
        siteFlagsMid[29] = 1;
        break;
      case 208:
        siteFlagsMid[30] = 1;
        break;
      case 209:
        siteFlagsMid[31] = 1;
        break;
      case 210:
        siteFlagsMid[32] = 1;
        break;
      case 211:
        siteFlagsMid[33] = 1;
        break;
      case 212:
        siteFlagsMid[34] = 1;
        break;
      case 213:
        siteFlagsMid[35] = 1;
        break;
      case 214:
        siteFlagsMid[36] = 1;
        break;
      case 215:
        siteFlagsMid[37] = 1;
        break;
      case 216:
        siteFlagsMid[38] = 1;
        break;
      case 217:
        siteFlagsMid[39] = 1;
        break;
      case 218:
        siteFlagsMid[40] = 1;
        break;
      case 219:
        siteFlagsMid[41] = 1;
        break;
      case 220:
        siteFlagsMid[42] = 1;
        break;
      case 221:
        siteFlagsMid[43] = 1;
        break;
      case 223:
        siteFlagsMid[44] = 1;
        break;
      case 224:
        siteFlagsMid[45] = 1;
        break;
      case 225:
        siteFlagsMid[46] = 1;
        break;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (int i = 0; i < NUMBER_SITE_FLAG - 1; i++) {
      sb.append(siteFlags[i]).append(",");
    }
    sb.append(siteFlags[NUMBER_SITE_FLAG - 1]).append("}");
    return sb.toString();
  }

  public void initSiteFlags() {
    Arrays.fill(siteFlags, (byte) 0);
  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    initSiteFlags();
    sessionAccumulator.getUbiSession().setSiteFlagsSet(siteFlags);
    sessionAccumulator.getUbiSession().setSiteFlags(null);
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
    sessionAccumulator
        .getUbiSession()
        .setSiteFlags(ByteArrayToNum.getLong(sessionAccumulator.getUbiSession().getSiteFlagsSet()));
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
