package com.ebay.sojourner.ubd.rt.operators.event;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.SlidingWindowReservoir;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class EventDiscardingSink extends RichSinkFunction<UbiEvent> {

  private transient DropwizardHistogramWrapper siteToSourceHistogram;
  private transient DropwizardHistogramWrapper siteToSinkHistogram;
  private transient DropwizardHistogramWrapper sourceToSinkHistogram;

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    Histogram dropwizardHistogram =
        new Histogram(new SlidingWindowReservoir(500));
    siteToSourceHistogram = getRuntimeContext().getMetricGroup()
        .addGroup("sojourner-ubd")
        .histogram("site to source", new DropwizardHistogramWrapper(dropwizardHistogram));
    siteToSinkHistogram = getRuntimeContext().getMetricGroup()
        .addGroup("sojourner-ubd")
        .histogram("site to sink", new DropwizardHistogramWrapper(dropwizardHistogram));
    sourceToSinkHistogram = getRuntimeContext().getMetricGroup()
        .addGroup("sojourner-ubd")
        .histogram("source to sink", new DropwizardHistogramWrapper(dropwizardHistogram));
  }

  @Override
  public void invoke(UbiEvent value, Context context) throws Exception {
    long end = System.nanoTime();
    long siteToSource = value.getIngestTime() - value.getEventTimestamp();
    long siteToSink = end - value.getEventTimestamp();
    long sourceToSink = end - value.getIngestTime();
    siteToSinkHistogram.update(siteToSink);
    siteToSourceHistogram.update(siteToSource);
    sourceToSinkHistogram.update(sourceToSink);
  }
}
