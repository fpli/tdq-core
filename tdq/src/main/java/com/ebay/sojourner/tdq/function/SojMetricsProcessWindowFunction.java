package com.ebay.sojourner.tdq.function;

import com.ebay.sojourner.common.model.*;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.tdq.metrics.SojMetricsGauge;
import org.apache.commons.collections.MapUtils;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Map.Entry;

public class SojMetricsProcessWindowFunction extends
        ProcessWindowFunction<SojMetrics, SojMetrics, Tuple, TimeWindow> {
    private static final Map<String, Gauge> domainMetricGuageMap = new ConcurrentHashMap<>();
    private static final Map<String, Object> domainMetricValueMap = new ConcurrentHashMap<>();

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void process(Tuple tuple, Context context, Iterable<SojMetrics> elements,
                        Collector<SojMetrics> out) {
        SojMetrics sojMetrics = elements.iterator().next();
        collectSojMetrics(sojMetrics, context.window().getEnd());
        out.collect(null);
    }

    private void collectSojMetrics(SojMetrics sojMetrics, Long eventTime) {
        collectPageCntMetrics(sojMetrics, eventTime);
        collectTagMissingCntMetrics(sojMetrics, eventTime);
        collectTagSumMetrics(sojMetrics, eventTime);
        collectTransformErrorMetrics(sojMetrics, eventTime);
    }

    private void collectTagMissingCntMetrics(SojMetrics sojMetrics, Long evenTime) {
        if (sojMetrics != null && MapUtils.isNotEmpty(sojMetrics.getTagMissingCntMetricsMap())) {
            for (Entry<String, TagMissingCntMetrics> tagMissingCntMetricsEntry
                    : sojMetrics.getTagMissingCntMetricsMap().entrySet()) {
                String metricKey = tagMissingCntMetricsEntry.getKey();
                TagMissingCntMetrics tagMissingCntMetrics = tagMissingCntMetricsEntry.getValue();
                Map<String, Map<String, Long>> domainTagMissingCntMap
                        = tagMissingCntMetrics.getTagCntMap();
                for (Entry<String, Map<String, Long>> domainTagMissingCntEntry
                        : domainTagMissingCntMap.entrySet()) {
                    String domain = domainTagMissingCntEntry.getKey();
                    String[] domainList = domain.split(Constants.DOMAIN_DEL);
                    Map<String, Long> tagMissingCntMap = domainTagMissingCntEntry.getValue();
                    for (Entry<String, Long> tagMissingCntEntry : tagMissingCntMap.entrySet()) {
                        String tagName = tagMissingCntEntry.getKey();
                        Long tagCount = tagMissingCntEntry.getValue();
                        StringBuilder sb = new StringBuilder(Constants.SOJ_METRICS_GROUP)
                                .append(metricKey).append(domain).append(evenTime).append(tagName);
                        if (!domainMetricValueMap.containsKey(sb.toString())) {
                            domainMetricValueMap.put(sb.toString(), tagCount);
                            Gauge gauge = getRuntimeContext()
                                    .getMetricGroup().addGroup(Constants.SOJ_METRICS_GROUP)
                                    .addGroup(Constants.SOJ_METRIC_TYPE,
                                            tagMissingCntMetrics.getMetricType().toString())
                                    .addGroup(Constants.SOJ_METRIC_NAME,
                                            tagMissingCntMetrics.getMetricName())
                                    .addGroup(Constants.SOJ_PGAE_FAMILY, domainList[0])
                                    .addGroup(Constants.SOJ_SITE_ID, domainList[1])
                                    .addGroup(Constants.SOJ_EVENT_TIME, String.valueOf(evenTime))
                                    .addGroup(Constants.SOJ_TAG_NAME, tagName.replace("|", "-"))
                                    .gauge(Constants.TAG_MISSING_CNT_METRICS,
                                            new SojMetricsGauge(domainMetricValueMap,
                                                    sb.toString()));
                            domainMetricGuageMap.put(sb.toString(), gauge);
                        } else {
                            domainMetricValueMap.put(sb.toString(), tagCount);
                        }
                    }

                }
            }
        }
    }

    private void collectTagSumMetrics(SojMetrics sojMetrics, Long evenTime) {
        if (sojMetrics != null && MapUtils.isNotEmpty(sojMetrics.getTagSumMetricsMap())) {
            for (Entry<String, TagSumMetrics> tagSumMetricsEntry
                    : sojMetrics.getTagSumMetricsMap().entrySet()) {
                String metricKey = tagSumMetricsEntry.getKey();
                TagSumMetrics tagSumMetrics = tagSumMetricsEntry.getValue();
                Map<String, Map<String, Double>> domainTagSumMap = tagSumMetrics.getTagSumMap();
                for (Entry<String, Map<String, Double>> domainTagSumEntry
                        : domainTagSumMap.entrySet()) {
                    String domain = domainTagSumEntry.getKey();
                    String[] domainList = domain.split(Constants.DOMAIN_DEL);
                    Map<String, Double> tagSumMap = domainTagSumEntry.getValue();
                    for (Entry<String, Double> tagSumEntry : tagSumMap.entrySet()) {
                        String tagName = tagSumEntry.getKey();
                        Double tagCount = tagSumEntry.getValue();
                        StringBuilder sb = new StringBuilder(Constants.SOJ_METRICS_GROUP)
                                .append(metricKey).append(domain).append(evenTime).append(tagName);
                        if (!domainMetricValueMap.containsKey(sb.toString())) {
                            domainMetricValueMap.put(sb.toString(), tagCount);
                            Gauge gauge = getRuntimeContext()
                                    .getMetricGroup().addGroup(Constants.SOJ_METRICS_GROUP)
                                    .addGroup(Constants.SOJ_METRIC_TYPE,
                                            tagSumMetrics.getMetricType().toString())
                                    .addGroup(Constants.SOJ_METRIC_NAME,
                                            tagSumMetrics.getMetricName())
                                    .addGroup(Constants.SOJ_PGAE_FAMILY, domainList[0])
                                    .addGroup(Constants.SOJ_SITE_ID, domainList[1])
                                    .addGroup(Constants.SOJ_EVENT_TIME, String.valueOf(evenTime))
                                    .addGroup(Constants.SOJ_TAG_NAME, tagName)
                                    .gauge(Constants.TAG_SUM_METRICS,
                                            new SojMetricsGauge(domainMetricValueMap,
                                                    sb.toString()));
                            domainMetricGuageMap.put(sb.toString(), gauge);
                        } else {
                            domainMetricValueMap.put(sb.toString(), tagCount);
                        }
                    }

                }
            }
        }
    }

    private void collectPageCntMetrics(SojMetrics sojMetrics, Long evenTime) {
        if (sojMetrics != null && MapUtils.isNotEmpty(sojMetrics.getPageCntMetricsMap())) {
            for (Entry<String, PageCntMetrics> pageCntMetricsEntry
                    : sojMetrics.getPageCntMetricsMap().entrySet()) {
                String metricKey = pageCntMetricsEntry.getKey();
                PageCntMetrics pageCntMetrics = pageCntMetricsEntry.getValue();
                Map<String, Map<Integer, Long>> domainPageCntMap = pageCntMetrics.getPageCntMap();
                for (Entry<String, Map<Integer, Long>> domainPageCntEntry :
                        domainPageCntMap.entrySet()) {
                    String domain = domainPageCntEntry.getKey();
                    String[] domainList = domain.split(Constants.DOMAIN_DEL);
                    Map<Integer, Long> pageCntMap = domainPageCntEntry.getValue();
                    for (Entry<Integer, Long> pageCntEntry : pageCntMap.entrySet()) {
                        Integer pageId = pageCntEntry.getKey();
                        Long pageCnt = pageCntEntry.getValue();
                        StringBuilder sb = new StringBuilder(Constants.SOJ_METRICS_GROUP)
                                .append(metricKey).append(domain).append(evenTime).append(pageId);
                        if (!domainMetricValueMap.containsKey(sb.toString())) {
                            domainMetricValueMap.put(sb.toString(), pageCnt);
                            Gauge gauge = getRuntimeContext()
                                    .getMetricGroup().addGroup(Constants.SOJ_METRICS_GROUP)
                                    .addGroup(Constants.SOJ_METRIC_TYPE,
                                            pageCntMetrics.getMetricType().toString())
                                    .addGroup(Constants.SOJ_METRIC_NAME,
                                            pageCntMetrics.getMetricName())
                                    .addGroup(Constants.SOJ_PGAE_FAMILY, domainList[0])
                                    .addGroup(Constants.SOJ_SITE_ID, domainList[1])
                                    .addGroup(Constants.SOJ_EVENT_TIME, String.valueOf(evenTime))
                                    .addGroup(Constants.SOJ_PAGE_ID, String.valueOf(pageId))
                                    .gauge(Constants.PAGE_CNT_METRICS,
                                            new SojMetricsGauge(domainMetricValueMap,
                                                    sb.toString()));
                            domainMetricGuageMap.put(sb.toString(), gauge);
                        } else {
                            domainMetricValueMap.put(sb.toString(), pageCnt);
                        }
                    }

                }
            }
        }
    }

    private void collectTransformErrorMetrics(SojMetrics sojMetrics, Long evenTime) {
        if (sojMetrics != null && MapUtils.isNotEmpty(sojMetrics.getTransformErrorMetricsMap())) {
            for (Entry<String, TransformErrorMetrics> transformErrorMetricsEntry
                    : sojMetrics.getTransformErrorMetricsMap().entrySet()) {
                String metricKey = transformErrorMetricsEntry.getKey();
                TransformErrorMetrics transformErrorMetrics = transformErrorMetricsEntry.getValue();
                Map<String, Map<String, Long>> domainTransformErrorMap
                        = transformErrorMetrics.getTagErrorCntMap();
                for (Entry<String, Map<String, Long>> domainTransformErrorEntry :
                        domainTransformErrorMap.entrySet()) {
                    String domain = domainTransformErrorEntry.getKey();
                    String[] domainList = domain.split(Constants.DOMAIN_DEL);
                    Map<String, Long> transformErrorMap = domainTransformErrorEntry.getValue();
                    for (Entry<String, Long> transformErrorEntry : transformErrorMap.entrySet()) {
                        String tagName = transformErrorEntry.getKey();
                        Long tagErrorCnt = transformErrorEntry.getValue();
                        StringBuilder sb = new StringBuilder(Constants.SOJ_METRICS_GROUP)
                                .append(metricKey).append(domain).append(evenTime).append(tagName);
                        if (!domainMetricValueMap.containsKey(sb.toString())) {
                            domainMetricValueMap.put(sb.toString(), tagErrorCnt);
                            Gauge gauge = getRuntimeContext()
                                    .getMetricGroup().addGroup(Constants.SOJ_METRICS_GROUP)
                                    .addGroup(Constants.SOJ_METRIC_TYPE,
                                            transformErrorMetrics.getMetricType().toString())
                                    .addGroup(Constants.SOJ_METRIC_NAME,
                                            transformErrorMetrics.getMetricName())
                                    .addGroup(Constants.SOJ_PGAE_FAMILY, domainList[0])
                                    .addGroup(Constants.SOJ_SITE_ID, domainList[1])
                                    .addGroup(Constants.SOJ_EVENT_TIME, String.valueOf(evenTime))
                                    .addGroup(Constants.SOJ_TAG_NAME, String.valueOf(tagName
                                            .replace("|", "-")))
                                    .gauge(Constants.TRANSFORM_ERROR_METRICS,
                                            new SojMetricsGauge(domainMetricValueMap,
                                                    sb.toString()));
                            domainMetricGuageMap.put(sb.toString(), gauge);
                        } else {
                            domainMetricValueMap.put(sb.toString(), tagErrorCnt);
                        }
                    }

                }
            }
        }
    }
}
