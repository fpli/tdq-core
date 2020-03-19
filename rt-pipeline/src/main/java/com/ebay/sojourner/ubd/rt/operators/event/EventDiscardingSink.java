package com.ebay.sojourner.ubd.rt.operators.event;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.SlidingWindowReservoir;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import java.util.Date;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class EventDiscardingSink extends RichSinkFunction<UbiEvent> {

  private transient DropwizardHistogramWrapper siteToSourceWrapper;
  private transient DropwizardHistogramWrapper siteToSinkWrapper;
  private transient DropwizardHistogramWrapper sourceToSinkWrapper;

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    Histogram siteToSourceHistogram =
        new Histogram(new SlidingWindowReservoir(500));
    Histogram siteToSinkHistogram =
        new Histogram(new SlidingWindowReservoir(500));
    Histogram sourceToSinkHistogram =
        new Histogram(new SlidingWindowReservoir(500));
    siteToSourceWrapper = getRuntimeContext().getMetricGroup()
        .addGroup("sojourner-ubd")
        .histogram("site to source", new DropwizardHistogramWrapper(siteToSourceHistogram));
    siteToSinkWrapper = getRuntimeContext().getMetricGroup()
        .addGroup("sojourner-ubd")
        .histogram("site to sink", new DropwizardHistogramWrapper(siteToSinkHistogram));
    sourceToSinkWrapper = getRuntimeContext().getMetricGroup()
        .addGroup("sojourner-ubd")
        .histogram("source to sink", new DropwizardHistogramWrapper(sourceToSinkHistogram));
  }

  @Override
  public void invoke(UbiEvent value, Context context) throws Exception {
    long end = new Date().getTime();
    long siteToSource = value.getIngestTime() - value.getGenerateTime();
    long siteToSink = end - value.getGenerateTime();
    long sourceToSink = (end - value.getIngestTime());
    siteToSourceWrapper.update(siteToSource);
    siteToSinkWrapper.update(siteToSink);
    sourceToSinkWrapper.update(sourceToSink);
  }
}
