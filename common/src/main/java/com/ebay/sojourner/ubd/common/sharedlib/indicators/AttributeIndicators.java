package com.ebay.sojourner.ubd.common.sharedlib.indicators;


import com.ebay.sojourner.ubd.common.sharedlib.metrics.Aggregator;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.FieldMetrics;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.RecordMetrics;

import java.util.LinkedHashSet;

public abstract class AttributeIndicators<Source, Target> implements Aggregator<Source, Target> {
    
    protected LinkedHashSet<Indicator<Source, Target>> indicators = new LinkedHashSet<Indicator<Source, Target>>();

    /**
     * Initialize the field metrics for being used in aggregator operations.
     */
    public abstract void initIndicators();

    public void init() throws Exception {
        for (Indicator<Source, Target> indicator : indicators) {
            indicator.init();
        }
    }
    
    public void start(Target target) throws Exception {
        for (Indicator<Source, Target> indicator : indicators) {
            indicator.start(target);
        }
    }

    @Override
    public void feed( Source ubiSession, Target agentIpAttributeAccumulator ) throws Exception {

    }

    public void feed( Source source, Target target ,boolean isNeeded) throws Exception {
        for (Indicator<Source, Target> indicator : indicators) {
            if (!indicator.filter(source, target)) {
                if (isNeeded) {
                    indicator.feed(source, target, isNeeded);
                    isNeeded=false;
                } else {
                    indicator.feed(source, target, isNeeded);
                }
            }
        }
    }
    
    public void end(Target target) throws Exception {
        for (Indicator<Source, Target> indicator : indicators) {
            indicator.end(target);
        }
    }
    
    public void addIndicators(Indicator<Source, Target> indicator) {
        if (!indicators.contains(indicator)) {
            indicators.add(indicator);
        } else {
            throw new RuntimeException("Duplicate Metrics!!  ");
        }
    }
}
