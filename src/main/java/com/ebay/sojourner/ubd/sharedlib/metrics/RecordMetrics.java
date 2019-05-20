package com.ebay.sojourner.ubd.sharedlib.metrics;


import java.util.LinkedHashSet;

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

    public void feed(Source source, Target target) throws Exception {
        for (FieldMetrics<Source, Target> metrics : fieldMetrics) {
            metrics.feed(source, target);
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
