package com.ebay.sojourner.rt.common.metrics;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.SlidingWindowReservoir;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.Constants;
import java.util.Date;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class PipelineMetricsCollectorProcessFunction extends ProcessFunction<UbiEvent,UbiEvent> {

  private transient DropwizardHistogramWrapper siteToSourceWrapper;
  private transient DropwizardHistogramWrapper siteToSinkWrapper;
  private transient DropwizardHistogramWrapper sourceToSinkWrapper;
  private static final String siteToSource = "site_to_source";
  private static final String siteToSink = "site_to_sink";
  private static final String sourceToSink = "source_to_sink";

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    Histogram siteToSourceHistogram =
        new Histogram(new SlidingWindowReservoir(500));
    Histogram siteToSinkHistogram =
        new Histogram(new SlidingWindowReservoir(500));
    Histogram sourceToSinkHistogram =
        new Histogram(new SlidingWindowReservoir(500));
    siteToSourceWrapper = getRuntimeContext().getMetricGroup()
        .addGroup(Constants.SOJ_METRICS_GROUP)
        .histogram(siteToSource, new DropwizardHistogramWrapper(siteToSourceHistogram));
    siteToSinkWrapper = getRuntimeContext().getMetricGroup()
        .addGroup(Constants.SOJ_METRICS_GROUP)
        .histogram(siteToSink, new DropwizardHistogramWrapper(siteToSinkHistogram));
    sourceToSinkWrapper = getRuntimeContext().getMetricGroup()
        .addGroup(Constants.SOJ_METRICS_GROUP)
        .histogram(sourceToSink, new DropwizardHistogramWrapper(sourceToSinkHistogram));
  }

  @Override
  public void processElement(UbiEvent value, Context ctx, Collector<UbiEvent> out) {
    long end = new Date().getTime();
    long siteToSource = value.getIngestTime() - value.getGenerateTime();
    long siteToSink = end - value.getGenerateTime();
    long sourceToSink = (end - value.getIngestTime());
    siteToSourceWrapper.update(siteToSource);
    siteToSinkWrapper.update(siteToSink);
    sourceToSinkWrapper.update(sourceToSink);
    out.collect(null);
  }
}
