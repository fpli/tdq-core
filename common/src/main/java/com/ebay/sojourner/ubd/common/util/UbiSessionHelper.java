package com.ebay.sojourner.ubd.common.util;


import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;
import com.ebay.sojourner.ubd.common.sharedlib.util.Base64Ebay;
import com.ebay.sojourner.ubd.common.sharedlib.util.GUID2Date;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJTS2Date;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Xiaoding
 */
public class UbiSessionHelper {

    public static final long MINUS_GUID_MIN_MS = 180000L; // 417mins - 7hours = -3mins = -180000ms; UNIX.
    public static final long PLUS_GUID_MAX_MS = 300000L; // 425mins - 7hours = 5mins = 300000ms;
    private static final int DIRECT_SESSION_SRC = 1;
    private static final int SINGLE_PAGE_SESSION = 1;
    public static final float DEFAULT_LOAD_FACTOR = .75F;
    public static final int IAB_MAX_CAPACITY = 100 * 1024; // 250 * 1024 * 1024 = 250m - refer io.sort.mb (default spill size)
    public static final int IAB_INITIAL_CAPACITY = 10 * 1024; // 16 * 1024 * 1024 = 16m
    private static Map<String, Boolean> iabCache = new LinkedHashMap<String, Boolean>(IAB_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Boolean> eldest) {
            return size() > IAB_MAX_CAPACITY;
        }
    };

    public static boolean isSingleClickSession(UbiSession session) {
        if (session.getSingleClickSessionFlag() != null && session.getSingleClickSessionFlag()) {
            return true;
        }
        return false;
    }

    public static boolean isSingleClickNull(UbiSession session) {
        if (session.getSingleClickSessionFlag() == null) {
            return true;
        }
        return false;
    }


    public static boolean isBidBinConfirm(UbiSession session) {
        if (session.getBidBinConfirmFlag() != null && session.getBidBinConfirmFlag()) {
            return true;
        }
        return false;
    }

    public static boolean isAgentHoper(UbiSession session) {
        if (session.getAgentCnt() > 1) {
            return true;
        }
        return false;
    }

    public static boolean isNewGuid(UbiSession session) {
        if (isNewGuid(session.getGuid(), session.getStartTimestamp())) {
            return true;
        }
        return false;
    }

    public static boolean isHomePage(UbiSession session) {
        if (session.getValidPageCnt() == session.getHomepageCnt()) {
            return true;
        }
        return false;
    }

    public static boolean isFamilyVi(UbiSession session) {
        if (session.getValidPageCnt() == session.getFamilyViCnt()) {
            return true;
        }
        return false;
    }

    public static boolean isSignIn(UbiSession session) {
        if (session.getValidPageCnt() == session.getSigninPageCnt()) {
            return true;
        }
        return false;
    }

    public static boolean isNoUid(UbiSession session) {
        if (session.getFirstUserId() == null) {
            return true;
        }
        return false;
    }

    public static boolean isSps(UbiSession session) {
        return session.getValidPageCnt() == SINGLE_PAGE_SESSION;
    }

    public static boolean isDirect(UbiSession session) {
        return session.getTrafficSrcId() == DIRECT_SESSION_SRC;
    }

    public static boolean isMktg(UbiSession session) {
        return UbiLookups.getInstance().getMktgTraficSrcIds().contains(session.getTrafficSrcId());
    }

    public static boolean isSite(UbiSession session) {
        return UbiLookups.getInstance().getNonbrowserCobrands().contains(session.getCobrand());
    }

    public static boolean isAgentDeclarative(UbiSession session) throws IOException, InterruptedException {
        return StringUtils.isNotBlank(session.getAgentString())
                && UbiLookups.getInstance().getAgentMatcher().match(session.getAgentString());
    }
    public static boolean isAgentDeclarative(AgentAttribute agentAttribute) throws IOException, InterruptedException {
        return StringUtils.isNotBlank(agentAttribute.getAgent())
                && UbiLookups.getInstance().getAgentMatcher().match(agentAttribute.getAgent());
    }

    public static boolean isNonIframRdtCountZero(UbiSession session) {
        if (session.getNonIframeRdtEventCnt() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isAgentStringDiff(UbiSession session) {
        if (!MiscUtil.objEquals(session.getUserAgent(), session.getAgentString())) {
            return true;
        }
        return false;
    }

    public static String getAgentString(UbiSession session) {
        if (isAgentStringDiff(session)) {
            return session.getAgentString();
        } else {
            return session.getUserAgent();
        }
    }

    public static boolean isExInternalIpDiff(UbiSession session) {
        String eiipTrimed = null;
        if (session.getExInternalIp() != null) {
            eiipTrimed = session.getExInternalIp().trim();
        }
        if (!MiscUtil.objEquals(session.getIp(), eiipTrimed)) {
            return true;
        }
        return false;
    }

    public static boolean isExInternalIpNonTrimDiff(UbiSession session) {
        if (!MiscUtil.objEquals(session.getIp(), session.getExInternalIp())) {
            return true;
        }
        return false;
    }

    public static boolean isAgentStringAfterBase64Diff(UbiSession session) throws UnsupportedEncodingException {
        String agentBase64 = Base64Ebay.encode(session.getAgentString().getBytes());
        String agentStrAfterBase64 = Base64Ebay.decodeUTF8(agentBase64);
        if (!MiscUtil.objEquals(session.getUserAgent(), agentStrAfterBase64)) {
            return true;
        }
        return false;
    }

    public static String getExInternalIp(UbiSession session) {
        if (isExInternalIpDiff(session)) {
            return session.getExInternalIp().trim();
        } else {
            return session.getIp();
        }
    }

    public static String getExInternalIpNonTrim(UbiSession session) {
        if (isExInternalIpNonTrimDiff(session)) {
            return session.getExInternalIp();
        } else {
            return session.getIp();
        }
    }

    public static String getAgentStringAfterBase64(UbiSession session) throws UnsupportedEncodingException {
        if (isAgentStringAfterBase64Diff(session)) {
            String agentBase64 = Base64Ebay.encode(session.getAgentString().getBytes());
            String agentStrAfterBase64 = Base64Ebay.decodeUTF8(agentBase64);
            return agentStrAfterBase64;
        } else {
            return session.getUserAgent();
        }
    }

    public static boolean isIabAgent(UbiSession session) {
        if (session.getNonIframeRdtEventCnt() > 0 && session.getUserAgent() != null) {
            Boolean whether = iabCache.get(session.getUserAgent());
            if (whether == null) {
                whether = checkIabAgent(session.getUserAgent());
                iabCache.put(session.getUserAgent(), whether);
            }

            if (whether) {
                return true;
            }
        }

        return false;
    }

    public static boolean isYesterdaySession(UbiSession session) {
//        if (session.getFlags() != null) {
//            return BitUtil.isBitSet(session.getFlags(), SessionFlags.YESTERDAY_SESSION);
//        }
        return false;
    }

    protected static boolean checkIabAgent(String agent) {
        if (StringUtils.isNotBlank(agent)) {
            for (String iabAgentReg : LkpFetcher.getInstance().getIabAgentRegs()) {
                if (agent.toLowerCase().contains(iabAgentReg)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isNewGuid(String guid, Long startTimestamp) {
        try {
            if (startTimestamp != null) {
                long guidTimestamp = GUID2Date.getTimestamp(guid);
                long startTimestampInUnix = SOJTS2Date.getUnixTimestamp(startTimestamp);
                long minTimestamp = startTimestampInUnix - MINUS_GUID_MIN_MS;
                long maxTimestamp = startTimestampInUnix + PLUS_GUID_MAX_MS;
                if (guidTimestamp >= minTimestamp && guidTimestamp <= maxTimestamp) {
                    return true;
                }
            }
        } catch (RuntimeException e) {
            return false;
        }
        return false;
    }
}
