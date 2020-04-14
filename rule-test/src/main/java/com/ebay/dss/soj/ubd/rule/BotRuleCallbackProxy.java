package com.ebay.dss.soj.ubd.rule;

import com.google.common.util.concurrent.FutureCallback;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BotRuleCallbackProxy implements FutureCallback<BotRuleResult> {

  private boolean before;
  private BotRuleResultCallback resultCallback;

  public BotRuleCallbackProxy(boolean before, BotRuleResultCallback resultCallback) {
    this.before = before;
    this.resultCallback = resultCallback;
  }

  public void onSuccess(@Nullable BotRuleResult result) {
    if (before) {
      resultCallback.onBeforeSuccess(result);
    } else {
      resultCallback.onAfterSuccess(result);
    }
  }

  public void onFailure(Throwable t) {
    if (before) {
      resultCallback.onBeforeFailure(t);
    } else {
      resultCallback.onAfterFailure(t);
    }
  }
}
