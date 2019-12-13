package com.ebay.sojourner.ubd.common.sharedlib.metrics;


import org.apache.flink.api.common.accumulators.AverageAccumulator;

import java.util.LinkedHashSet;
import java.util.Map;

public abstract class RecordMetrics<Source, Target> implements Aggregator<Source, Target> {
    
    protected LinkedHashSet<FieldMetrics<Source, Target>> fieldMetrics = new LinkedHashSet<FieldMetrics<Source, Target>>();

    /**
     * Initialize the field metrics for being used in aggregator operations.
     */
    public abstract void initFieldMetrics();

    public void init() throws Exception {
        for (FieldMetrics<Source, Target> metrics : fieldMetrics) {
            metrics.init();
        }
    }
    
    public void start(Target target) throws Exception {
        for (FieldMetrics<Source, Target> metrics : fieldMetrics) {
            metrics.start(target);
        }
    }
    public void feed(Source source, Target target) throws Exception{

    }

    public void feed(Source source, Target target, Map<String, AverageAccumulator> map) throws Exception {
        for (FieldMetrics<Source, Target> metrics : fieldMetrics) {
            long start = System.nanoTime();
            metrics.feed(source, target);
            long end = System.nanoTime();
            map.get(metrics.getClass().getSimpleName()).add(end - start);
        }
    }
    
    public void end(Target target) throws Exception {
        for (FieldMetrics<Source, Target> metrics : fieldMetrics) {
            metrics.end(target);
        }
    }
    
    public void addFieldMetrics(FieldMetrics<Source, Target> metrics) {
        if (!fieldMetrics.contains(metrics)) {
            fieldMetrics.add(metrics);
        } else {
            throw new RuntimeException("Duplicate Metrics!!  ");
        }
    }
}
