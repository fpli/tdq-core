package com.ebay.dss.soj.ubd.rule;

public interface BotRuleResultCallback {

  void onBeforeSuccess(BotRuleResult result);

  void onBeforeFailure(Throwable t);

  void onAfterSuccess(BotRuleResult result);

  void onAfterFailure(Throwable t);
}
