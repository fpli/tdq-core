package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.ByteArrayToNum;

import java.util.Arrays;

public class SiteFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private static final Integer NUMBER_SITE_FLAG = 47;

    // idx 0~46 used
    //private BitSet siteFlags = new BitSet();
    private byte[] siteFlags = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0};

    void setSiteFlag(Integer siteId, SessionAccumulator sessionAccumulator) {
        siteFlags = sessionAccumulator.getUbiSession().getSiteFlagsSet();
        switch (siteId) {
            case 998:
                siteFlags[0] = 1;
                break;
            case 0:
                siteFlags[1] = 1;
                break;
            case 1:
                siteFlags[2] = 1;
                break;
            case 2:
                siteFlags[3] = 1;
                break;
            case 3:
                siteFlags[4] = 1;
                break;
            case 15:
                siteFlags[5] = 1;
                break;
            case 16:
                siteFlags[6] = 1;
                break;
            case 23:
                siteFlags[7] = 1;
                break;
            case 37:
                siteFlags[8] = 1;
                break;
            case 71:
                siteFlags[9] = 1;
                break;
            case 77:
                siteFlags[10] = 1;
                break;
            case 100:
                siteFlags[11] = 1;
                break;
            case 101:
                siteFlags[12] = 1;
                break;
            case 104:
                siteFlags[13] = 1;
                break;
            case 123:
                siteFlags[14] = 1;
                break;
            case 146:
                siteFlags[15] = 1;
                break;
            case 186:
                siteFlags[16] = 1;
                break;
            case 193:
                siteFlags[17] = 1;
                break;
            case 196:
                siteFlags[18] = 1;
                break;
            case 197:
                siteFlags[19] = 1;
                break;
            case 198:
                siteFlags[20] = 1;
                break;
            case 199:
                siteFlags[21] = 1;
                break;
            case 200:
                siteFlags[22] = 1;
                break;
            case 201:
                siteFlags[23] = 1;
                break;
            case 202:
                siteFlags[24] = 1;
                break;
            case 203:
                siteFlags[25] = 1;
                break;
            case 204:
                siteFlags[26] = 1;
                break;
            case 205:
                siteFlags[27] = 1;
                break;
            case 206:
                siteFlags[28] = 1;
                break;
            case 207:
                siteFlags[29] = 1;
                break;
            case 208:
                siteFlags[30] = 1;
                break;
            case 209:
                siteFlags[31] = 1;
                break;
            case 210:
                siteFlags[32] = 1;
                break;
            case 211:
                siteFlags[33] = 1;
                break;
            case 212:
                siteFlags[34] = 1;
                break;
            case 213:
                siteFlags[35] = 1;
                break;
            case 214:
                siteFlags[36] = 1;
                break;
            case 215:
                siteFlags[37] = 1;
                break;
            case 216:
                siteFlags[38] = 1;
                break;
            case 217:
                siteFlags[39] = 1;
                break;
            case 218:
                siteFlags[40] = 1;
                break;
            case 219:
                siteFlags[41] = 1;
                break;
            case 220:
                siteFlags[42] = 1;
                break;
            case 221:
                siteFlags[43] = 1;
                break;
            case 223:
                siteFlags[44] = 1;
                break;
            case 224:
                siteFlags[45] = 1;
                break;
            case 225:
                siteFlags[46] = 1;
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < NUMBER_SITE_FLAG - 1; i++)
            sb.append(siteFlags[i]).append(",");
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
        sessionAccumulator.getUbiSession().setSiteFlags(ByteArrayToNum.getLong(sessionAccumulator.getUbiSession().getSiteFlagsSet()));
    }

    @Override
    public void init() throws Exception {
        // nothing to do
    }

}
