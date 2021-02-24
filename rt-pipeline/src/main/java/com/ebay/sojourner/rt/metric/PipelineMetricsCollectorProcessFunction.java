package com.ebay.sojourner.rt.metric;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.SlidingWindowReservoir;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class PipelineMetricsCollectorProcessFunction extends ProcessFunction<UbiEvent, UbiEvent> {

  private static final String siteToSource = "site_to_source";
  private static final String siteToSink = "site_to_sink";
  private static final String sourceToSink = "source_to_sink";
  private static final String latency = "source_to_sink";
  private transient DropwizardHistogramWrapper siteToSourceWrapper;
  private transient DropwizardHistogramWrapper siteToSinkWrapper;
  private transient DropwizardHistogramWrapper sourceToSinkWrapper;
  private static final Map<String, DropwizardHistogramWrapper> domainWrapperMap = new HashMap<>();
  private static final Histogram latencyHistogram = new Histogram(new SlidingWindowReservoir(500));

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
    long siteToSource =
        value.getIngestTime() - SojTimestamp
            .getSojTimestampToUnixTimestamp(value.getEventTimestamp());
    long siteToSink = end - SojTimestamp
        .getSojTimestampToUnixTimestamp(value.getEventTimestamp());
    long sourceToSink = (end - value.getIngestTime());
    siteToSourceWrapper.update(siteToSource);
    siteToSinkWrapper.update(siteToSink);
    sourceToSinkWrapper.update(sourceToSink);

    if (value.getPageFamily() == null || value.getPageFamily().equals("")) {
      value.setPageFamily("null");
    }

    String pageFamilyAndSiteId = value.getPageFamily() + "," + value.getSiteId();
    if (domainWrapperMap.containsKey(pageFamilyAndSiteId)
        && domainWrapperMap.get(pageFamilyAndSiteId) != null) {
      domainWrapperMap.get(pageFamilyAndSiteId).update(sourceToSink);
    } else {
      DropwizardHistogramWrapper domainWrapper = getRuntimeContext().getMetricGroup()
          .addGroup(Constants.SOJ_METRICS_GROUP)
          .addGroup("siteId", String.valueOf(value.getSiteId()))
          .addGroup("pageFamily", String.valueOf(value.getPageFamily()))
          .histogram(latency, new DropwizardHistogramWrapper(latencyHistogram));
      domainWrapperMap.put(pageFamilyAndSiteId, domainWrapper);
      domainWrapper.update(sourceToSink);
    }
    out.collect(null);
  }
}
