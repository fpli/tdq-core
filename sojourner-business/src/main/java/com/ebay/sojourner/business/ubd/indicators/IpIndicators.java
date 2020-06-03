package com.ebay.sojourner.business.ubd.indicators;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.UbiBotFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpIndicators extends AttributeIndicators<AgentIpAttribute, IpAttributeAccumulator> {

  private static volatile IpIndicators ipIndicators;
  private BotFilter botFilter;

  public IpIndicators() {
    botFilter = new UbiBotFilter();
    initIndicators();
    try {
      init();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public static IpIndicators getInstance() {
    if (ipIndicators == null) {
      synchronized (IpIndicators.class) {
        if (ipIndicators == null) {
          ipIndicators = new IpIndicators();
        }
      }
    }
    return ipIndicators;
  }

  @Override
  public void initIndicators() {
    addIndicators(new ScsCntForBot7IPIndicator<>(botFilter));
    addIndicators(new SuspectIPIPIndicator<>(botFilter));
  }
}
