package com.ebay.sojourner.rt.operator.session;

import com.ebay.sojourner.common.model.AgentHash;
import com.ebay.sojourner.common.model.Guid;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.Base64Ebay;
import com.ebay.sojourner.common.util.BitUtils;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.common.util.GUID2Date;
import com.ebay.sojourner.common.util.LkpManager;
import com.ebay.sojourner.common.util.MiscUtil;
import com.ebay.sojourner.common.util.SessionCoreHelper;
import com.ebay.sojourner.common.util.SessionFlags;
import com.ebay.sojourner.common.util.SojTimestamp;
import com.ebay.sojourner.common.util.TransformUtil;
import com.ebay.sojourner.common.util.UbiLookups;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

public class UbiSessionToSessionCoreMapFunction extends RichMapFunction<UbiSession, SessionCore> {

  protected static boolean checkIabAgent(String agent) {
    if (StringUtils.isNotBlank(agent)) {
      for (String iabAgentReg : LkpManager.getInstance().getIabAgentRegs()) {
        if (agent.toLowerCase().contains(iabAgentReg)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public SessionCore map(UbiSession ubiSession) throws Exception {
    SessionCore sessionCore = transToSessionCore(ubiSession);
    return sessionCore;
  }

  private SessionCore transToSessionCore(UbiSession session) throws Exception {
    SessionCore core = new SessionCore();
    core.setAbsEventCnt(session.getAbsEventCnt());

    if (!StringUtils.isBlank(session.getUserAgent())) {
      long[] long4AgentHash = TransformUtil.md522Long(TransformUtil.getMD5(session.getUserAgent()));
      AgentHash agentHash = new AgentHash();
      agentHash.setAgentHash1(long4AgentHash[0]);
      agentHash.setAgentHash2(long4AgentHash[1]);
      core.setUserAgent(agentHash);
    } else {
      AgentHash agentHash = new AgentHash();
      agentHash.setAgentHash1(0L);
      agentHash.setAgentHash2(0L);
      core.setUserAgent(agentHash);
    }
    core.setIp(TransformUtil.ipToInt(session.getIp()) == null ? 0
        : TransformUtil.ipToInt(session.getIp()));
    core.setBotFlag(session.getBotFlag());
    if (session.getFirstCguid() != null) {
      long[] long4Cguid = TransformUtil.md522Long(session.getFirstCguid());
      Guid cguid = new Guid();
      cguid.setGuid1(long4Cguid[0]);
      cguid.setGuid2(long4Cguid[1]);
      core.setCguid(cguid);
    } else {
      Guid cguid = new Guid();
      cguid.setGuid1(0L);
      cguid.setGuid2(0L);
      core.setCguid(cguid);
    }

    if (session.getGuid() != null) {
      long[] long4Cguid = TransformUtil.md522Long(session.getGuid());
      Guid guid = new Guid();
      guid.setGuid1(long4Cguid[0]);
      guid.setGuid2(long4Cguid[1]);
      core.setGuid(guid);
    } else {
      Guid cguid = new Guid();
      cguid.setGuid1(0L);
      cguid.setGuid2(0L);
      core.setGuid(cguid);
    }

    core.setAppId(session.getFirstAppId());
    core.setFlags(getFlags(session));
    core.setValidPageCnt(session.getValidPageCnt());
    core.setSessionStartDt(session.getSessionStartDt());
    core.setAbsEndTimestamp(session.getAbsEndTimestamp());
    core.setAbsStartTimestamp(session.getAbsStartTimestamp());
    // handle IAB
    if (session.getNonIframeRdtEventCnt() > 0 && core.getUserAgent() != null) {
      //      Boolean whether = iabCache.get(core.getUserAgent());
      //      if (whether == null) {
      //        whether = checkIabAgent(session.getUserAgent());
      //        iabCache.put(core.getUserAgent(), whether);
      //      }
      Boolean whether = checkIabAgent(session.getUserAgent());

      if (whether) {
        core.setFlags(BitUtils.setBit(core.getFlags(), SessionFlags.IAB_AGENT));
      }
    }

    if (BitUtils.isBitSet(core.getFlags(), SessionFlags.AGENT_STRING_DIFF) && !StringUtils
        .isBlank(session.getAgentString())) {
      long[] long4AgentHash =
          TransformUtil.md522Long(TransformUtil.getMD5(session.getAgentString()));
      AgentHash agentHash = new AgentHash();
      agentHash.setAgentHash1(long4AgentHash[0]);
      agentHash.setAgentHash2(long4AgentHash[1]);
      core.setAgentString(agentHash);

    } else if (StringUtils.isBlank(session.getAgentString())) {
      AgentHash agentHash = new AgentHash();
      agentHash.setAgentHash1(0L);
      agentHash.setAgentHash2(0L);
      core.setAgentString(agentHash);
    }

    if (!MiscUtil.objEquals(session.getIp(), session.getExInternalIp())) {
      core.setFlags(BitUtils.setBit(core.getFlags(), SessionFlags.EXINTERNALIP_NONTRIMED_DIFF));
      core.setExInternalIpNonTrim(TransformUtil.ipToInt(session.getExInternalIp()));
    }

    String eiipTrimed = null;
    if (session.getExInternalIp() != null) {
      eiipTrimed = session.getExInternalIp().trim();
    }
    if (!MiscUtil.objEquals(session.getIp(), eiipTrimed)) {
      core.setFlags(BitUtils.setBit(core.getFlags(), SessionFlags.EXINTERNALIP_DIFF));
      core.setExInternalIp(TransformUtil.ipToInt(eiipTrimed));
    }

    // TODO to match the incorrect old logic , just for 'data quality'
    AgentHash agentString = SessionCoreHelper.getAgentString(core);
    if (agentString.getAgentHash1() != 0L && agentString.getAgentHash2() != 0L) {
      //      Boolean equal = base64Cache.get(agentString);
      //      String agentStrAfterBase64 = null;
      //      if (equal == null) {
      //        String agentBase64 = Base64Ebay.encode(session.getAgentString().getBytes());
      //        agentStrAfterBase64 = Base64Ebay.decodeUTF8(agentBase64);
      //        equal = MiscUtil.objEquals(agentString, agentStrAfterBase64);
      //        base64Cache.put(agentString, equal);
      //      }
      String agentBase64 = Base64Ebay.encode(session.getAgentString().getBytes());
      String agentStrAfterBase64 = Base64Ebay.decodeUTF8(agentBase64);
      Boolean equal = MiscUtil.objEquals(agentString, agentStrAfterBase64);
      AgentHash agentAfterBase64 = agentString;
      if (!equal) {
        if (agentAfterBase64.getAgentHash1() == 0L && agentAfterBase64.getAgentHash2() == 0L) {
          agentBase64 = Base64Ebay.encode(session.getAgentString().getBytes());
          agentStrAfterBase64 = Base64Ebay.decodeUTF8(agentBase64);
        }

        long[] long4AgentHash = TransformUtil.md522Long(TransformUtil.getMD5(agentStrAfterBase64));
        agentAfterBase64 = new AgentHash();
        agentAfterBase64.setAgentHash1(long4AgentHash[0]);
        agentAfterBase64.setAgentHash2(long4AgentHash[1]);
      }

      if (!MiscUtil.objEquals(core.getUserAgent(), agentAfterBase64)) {
        core.setFlags(BitUtils.setBit(core.getFlags(),
            SessionFlags.AGENT_STRING_AFTER_BASE64_DIFF));
        core.setAgentStringAfterBase64(agentAfterBase64);
      }
    }

    return core;
  }

  public int getFlags(UbiSession session) throws Exception {
    Integer DIRECT_SESSION_SRC = 1;
    Integer SINGLE_PAGE_SESSION = 1;
    int flags = 0;
    if (session.getSingleClickSessionFlag() == null) {
      flags = BitUtils.setBit(flags, SessionFlags.SINGLE_CLICK_NULL_POS);
    }
    if (session.getSingleClickSessionFlag() != null && session.getSingleClickSessionFlag()) {
      flags = BitUtils.setBit(flags, SessionFlags.SINGLE_CLICK_FLAGS_POS);
    }
    if (session.getBidBinConfirmFlag() != null && session.getBidBinConfirmFlag()) {
      flags = BitUtils.setBit(flags, SessionFlags.BID_BIN_CONFIRM_FLAGS_POS);
    }
    if (session.getAgentCnt() > 1) {
      flags = BitUtils.setBit(flags, SessionFlags.AGENT_HOPER_FLAGS_POS);
    }
    if (isNewGuid(session.getGuid(), session.getStartTimestamp())) {
      flags = BitUtils.setBit(flags, SessionFlags.NEW_GUID_FLAGS_POS);
    }
    if (session.getValidPageCnt() == session.getHomepageCnt()) {
      flags = BitUtils.setBit(flags, SessionFlags.HOME_PAGE_FLAGS_POS);
    }
    if (session.getValidPageCnt() == session.getFamilyViCnt()) {
      flags = BitUtils.setBit(flags, SessionFlags.FAMILY_VI_FLAGS_POS);
    }
    if (session.getValidPageCnt() == session.getSigninPageCnt()) {
      flags = BitUtils.setBit(flags, SessionFlags.SIGN_IN_FLAGS_POS);
    }
    if (session.getFirstUserId() == null) {
      flags = BitUtils.setBit(flags, SessionFlags.NO_UID_FLAGS_POS);
    }
    if (session.getTrafficSrcId() == DIRECT_SESSION_SRC) {
      flags = BitUtils.setBit(flags, SessionFlags.DIRECT_FLAGS_POS);
    }
    if (UbiLookups.getInstance().getMktgTraficSrcIds().contains(session.getTrafficSrcId())) {
      flags = BitUtils.setBit(flags, SessionFlags.MKTG_FLAGS_POS);
    }
    if (UbiLookups.getInstance().getNonbrowserCobrands().contains(session.getCobrand())) {
      flags = BitUtils.setBit(flags, SessionFlags.SITE_FLAGS_POS);
    }

    if (!StringUtils.isBlank(session.getAgentString())) {
      if (UbiLookups.getInstance().getAgentMatcher().match(session.getAgentString())) {
        flags = BitUtils.setBit(flags, SessionFlags.DECLARATIVE_AGENT_FLAGS_POS);
      }
    }

    if (SINGLE_PAGE_SESSION.equals(session.getValidPageCnt())) {
      flags = BitUtils.setBit(flags, SessionFlags.SPS_SESSION_POS);
    }

    if (session.getNonIframeRdtEventCnt() == 0) {
      flags = BitUtils.setBit(flags, SessionFlags.ZERO_NON_IFRAME_RDT);
    }

    if (!MiscUtil.objEquals(session.getUserAgent(), session.getAgentString())) {
      flags = BitUtils.setBit(flags, SessionFlags.AGENT_STRING_DIFF);
    }
    return flags;
  }

  private boolean isNewGuid(String guid, Long startTimestamp) {
    try {
      if (startTimestamp != null) {
        long guidTimestamp = GUID2Date.getTimestamp(guid);
        long startTimestampInUnix = SojTimestamp.getUnixTimestamp(startTimestamp);
        long minTimestamp = startTimestampInUnix - Constants.MINUS_GUID_MIN_MS;
        long maxTimestamp = startTimestampInUnix + Constants.PLUS_GUID_MAX_MS;
        if (guidTimestamp >= minTimestamp && guidTimestamp <= maxTimestamp) {
          return true;
        }
      }
    } catch (RuntimeException e) {
      return false;
    }
    return false;
  }

  @Override
  public void open(Configuration configuration) throws Exception {
    super.open(configuration);

  }

}
