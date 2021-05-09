package com.ebay.tdq.svc;

import com.ebay.tdq.service.ProfilerService;
import com.ebay.tdq.service.RuleEngineService;

/**
 * @author juntzhang
 */
public class ServiceFactory {
  private static final RuleEngineService ruleEngineService = RuleEngineServiceImpl.instance;
  private static final ProfilerService profilerService = ProfilerServiceImpl.instance;

  public static RuleEngineService getRuleEngine() {
    return ruleEngineService;
  }

  public static ProfilerService getProfiler() {
    return profilerService;
  }
}
