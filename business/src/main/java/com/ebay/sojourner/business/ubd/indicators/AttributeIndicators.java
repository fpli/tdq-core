package com.ebay.sojourner.business.ubd.indicators;

import com.ebay.sojourner.business.ubd.metrics.Aggregator;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AttributeIndicators<Source, Target> implements Aggregator<Source, Target> {

  protected Set<AbstractIndicator<Source, Target>> indicators = new LinkedHashSet<>();

  /**
   * Initialize the field metrics for being used in aggregator operations.
   */
  public abstract void initIndicators();

  @Override
  public void init() throws Exception {
    for (AbstractIndicator<Source, Target> indicator : indicators) {
      indicator.init();
    }
  }

  public void start(Target target) throws Exception {
    for (AbstractIndicator<Source, Target> indicator : indicators) {
      indicator.start(target);
    }
  }

  @Override
  public void feed(Source source, Target target)
      throws Exception {
    for (AbstractIndicator<Source, Target> indicator : indicators) {
      if (!indicator.filter(source, target)) {
        indicator.feed(source, target);
      }
    }
  }

  public void end(Target target) throws Exception {
    for (AbstractIndicator<Source, Target> indicator : indicators) {
      indicator.end(target);
    }
  }

  public void addIndicators(AbstractIndicator<Source, Target> indicator) {
    if (!indicators.contains(indicator)) {
      indicators.add(indicator);
    } else {
      throw new RuntimeException("Duplicate Metrics!!  ");
    }
  }
}
