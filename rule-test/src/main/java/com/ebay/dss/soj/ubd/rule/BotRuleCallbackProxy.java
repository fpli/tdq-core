package com.ebay.dss.soj.ubd.rule;

import com.google.common.util.concurrent.FutureCallback;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BotRuleCallbackProxy implements FutureCallback<BotRuleResult> {

  private BotRuleResultCallback resultCallback;

  public BotRuleCallbackProxy(BotRuleResultCallback resultCallback) {
    this.resultCallback = resultCallback;
  }

  public void onSuccess(@Nullable BotRuleResult result) {
    resultCallback.onSuccess(result);
  }

  public void onFailure(Throwable t) {
    resultCallback.onFailure(t);
  }
}
