package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.model.SessionCore;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Xiaoding
 */
public class UbiSessionHelper {

  public static final long MINUS_GUID_MIN_MS =
      180000L; // 417mins - 7hours = -3mins = -180000ms; UNIX.
  public static final long PLUS_GUID_MAX_MS = 300000L; // 425mins - 7hours = 5mins = 300000ms;
  public static final float DEFAULT_LOAD_FACTOR = .75F;
  public static final int IAB_MAX_CAPACITY =
      100 * 1024; // 250 * 1024 * 1024 = 250m - refer io.sort.mb (default spill size)
  public static final int IAB_INITIAL_CAPACITY = 10 * 1024; // 16 * 1024 * 1024 = 16m
  private static final int DIRECT_SESSION_SRC = 1;
  private static final int SINGLE_PAGE_SESSION = 1;

  private static Map<String, Boolean> iabCache =
      new ConcurrentHashMap<>(IAB_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);

  private List<String> iabAgentRegs;

  public UbiSessionHelper() {
    iabAgentRegs = LkpManager.getInstance().getIabAgentRegs();
  }

  public static boolean isSingleClickSession(IntermediateSession session) {
    return session.getSingleClickSessionFlag() != null && session.getSingleClickSessionFlag();
  }

  public static boolean isSingleClickNull(IntermediateSession session) {
    return session.getSingleClickSessionFlag() == null;
  }

  public static boolean isBidBinConfirm(IntermediateSession session) {
    return session.getBidBinConfirmFlag() != null && session.getBidBinConfirmFlag();
  }

  public static boolean isAgentHoper(IntermediateSession session) {
    return session.getAgentCnt() > 1;
  }

  public static boolean isNewGuid(IntermediateSession session) {
    return isNewGuid(session.getGuid(), session.getStartTimestamp());
  }

  public static boolean isHomePage(IntermediateSession session) {
    return session.getValidPageCnt() == session.getHomepageCnt();
  }

  public static boolean isFamilyVi(IntermediateSession session) {
    return session.getValidPageCnt() == session.getFamilyViCnt();
  }

  public static boolean isSignIn(IntermediateSession session) {
    return session.getValidPageCnt() == session.getSigninPageCnt();
  }

  public static boolean isNoUid(IntermediateSession session) {
    return session.getFirstUserId() == null;
  }

  public static boolean isSps(IntermediateSession session) {
    return session.getValidPageCnt() == SINGLE_PAGE_SESSION;
  }

  public static boolean isDirect(IntermediateSession session) {
    return session.getTrafficSrcId() == DIRECT_SESSION_SRC;
  }

  public static boolean isMktg(IntermediateSession session) {
    return UbiLookups.getInstance().getMktgTraficSrcIds().contains(session.getTrafficSrcId());
  }

  public static boolean isSite(IntermediateSession session) {
    return UbiLookups.getInstance().getNonbrowserCobrands().contains(session.getCobrand());
  }

  public static boolean isAgentDeclarative(IntermediateSession session)
      throws IOException, InterruptedException {
    return StringUtils.isNotBlank(session.getAgentString())
        && UbiLookups.getInstance().getAgentMatcher().match(session.getAgentString());
  }

  public static boolean isAgentDeclarative(AgentAttribute agentAttribute)
      throws IOException, InterruptedException {
    String userAgent =
        String.valueOf(
            TransformUtil.recoveryMD5(agentAttribute.getAgent().getAgentHash1().longValue(),
                agentAttribute.getAgent().getAgentHash2().longValue()));
    return StringUtils.isNotBlank(userAgent)
        && UbiLookups.getInstance().getAgentMatcher().match(userAgent);
  }

  public static boolean isNonIframRdtCountZero(Object session) {

    if (session instanceof SessionCore) {
      SessionCore intermediateSession = (SessionCore) session;
      return SessionCoreHelper.isNonIframRdtCountZero(intermediateSession);
    } else {
      UbiSession ubiSession = (UbiSession) session;
      return ubiSession.getNonIframeRdtEventCnt() == 0;
    }

  }


  public static boolean isAgentStringDiff(IntermediateSession session) {
    return !MiscUtil.objEquals(session.getUserAgent(), session.getAgentString());
  }

  public static String getAgentString(IntermediateSession session) {
    if (isAgentStringDiff(session)) {
      return session.getAgentString();
    } else {
      return session.getUserAgent();
    }
  }

  public static boolean isExInternalIpDiff(IntermediateSession session) {
    String eiipTrimed = null;
    if (session.getExInternalIp() != null) {
      eiipTrimed = session.getExInternalIp().trim();
    }
    return !MiscUtil.objEquals(session.getIp(), eiipTrimed);
  }

  public static boolean isExInternalIpNonTrimDiff(IntermediateSession session) {
    return !MiscUtil.objEquals(session.getIp(), session.getExInternalIp());
  }

  public static boolean isAgentStringAfterBase64Diff(IntermediateSession session)
      throws UnsupportedEncodingException {
    String agentBase64 = Base64Ebay.encode(session.getAgentString().getBytes());
    String agentStrAfterBase64 = Base64Ebay.decodeUTF8(agentBase64);
    return !MiscUtil.objEquals(session.getUserAgent(), agentStrAfterBase64);
  }

  public static String getExInternalIp(IntermediateSession session) {
    if (isExInternalIpDiff(session)) {
      return session.getExInternalIp().trim();
    } else {
      return session.getIp();
    }
  }

  public static String getExInternalIpNonTrim(IntermediateSession session) {
    if (isExInternalIpNonTrimDiff(session)) {
      return session.getExInternalIp();
    } else {
      return session.getIp();
    }
  }

  public static String getAgentStringAfterBase64(IntermediateSession session)
      throws UnsupportedEncodingException {
    if (isAgentStringAfterBase64Diff(session)) {
      String agentBase64 = Base64Ebay.encode(session.getAgentString().getBytes());
      String agentStrAfterBase64 = Base64Ebay.decodeUTF8(agentBase64);
      return agentStrAfterBase64;
    } else {
      return session.getUserAgent();
    }
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

  public static void main(String[] args) {
    String agent =
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534+ (KHTML, like Gecko) BingPreview/1"
            + ".0b";
    ;
    if (StringUtils.isNotBlank(agent)) {
      for (String iabAgentReg : LkpManager.getInstance().getIabAgentRegs()) {
        if (agent.toLowerCase().contains(iabAgentReg)) {
          System.out.println(true);
          ;
        }
      }
    }
  }

  public boolean isIabAgent(UbiSession session) throws InterruptedException {
    if (session.getNonIframeRdtEventCnt() > 0 && (session.getUserAgent() != null
        || session.getAgentInfo() != null)) {
      String userAgent =
          session.getUserAgent() == null ? session.getAgentInfo() : session.getUserAgent();
      Boolean whether = iabCache.get(userAgent);
      if (whether == null) {
        whether = checkIabAgent(userAgent);
        if (iabCache.size() < IAB_MAX_CAPACITY) {
          iabCache.put(userAgent, whether);
        }
      }
      return whether;
    }

    return false;
  }

  protected boolean checkIabAgent(String agent) {
    iabAgentRegs = LkpManager.getInstance().getIabAgentRegs();
    if (StringUtils.isNotBlank(agent)) {
      for (String iabAgentReg : iabAgentRegs) {
        if (agent.toLowerCase().contains(iabAgentReg)) {
          return true;
        }
      }
    }
    return false;
  }

}
