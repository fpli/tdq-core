package com.ebay.sojourner.business.detector;

public class BotDetectors {

  public static ThreadLocal<NewSessionBotDetector> sessionBotDetector = new ThreadLocal<>();

  public static NewSessionBotDetector getSessionBotDetector() {
    if (sessionBotDetector.get() == null) {
      sessionBotDetector.set(new NewSessionBotDetector());
    }

    return sessionBotDetector.get();
  }

}
