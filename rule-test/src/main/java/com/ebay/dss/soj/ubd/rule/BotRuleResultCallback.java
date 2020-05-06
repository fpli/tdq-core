package com.ebay.dss.soj.ubd.rule;

public interface BotRuleResultCallback {

  void onSuccess(BotRuleResult result);

  void onFailure(Throwable t);
}
