package com.ebay.sojourner.business.ubd.detectors;

public class BotDetectorFactory {

  private BotDetectorFactory() {
  }

  public static AbstractBotDetector get(Type type) {
    switch (type) {
      case EVENT:
        return new EventBotDetector();
      default:
        throw new RuntimeException("Unknown BotDetector Type");
    }
  }

  public enum Type {
    EVENT
  }
}
