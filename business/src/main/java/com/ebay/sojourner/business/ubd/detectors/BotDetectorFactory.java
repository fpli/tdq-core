package com.ebay.sojourner.business.ubd.detectors;

import com.ebay.sojourner.business.ubd.rule.RuleManager;

public class BotDetectorFactory {

  private BotDetectorFactory() {
  }

  public static AbstractBotDetector get(Type type, RuleManager ruleManager) {
    switch (type) {
      case EVENT:
        return new EventBotDetector(ruleManager);
      default:
        throw new RuntimeException("Unknown BotDetector Type");
    }
  }

  public enum Type {
    EVENT
  }
}
