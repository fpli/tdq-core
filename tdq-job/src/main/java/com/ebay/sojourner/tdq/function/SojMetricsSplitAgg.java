package com.ebay.sojourner.tdq.function;

import com.ebay.sojourner.common.model.PageCntMetrics;
import com.ebay.sojourner.common.model.SojMetrics;
import com.ebay.sojourner.common.model.TagMissingCntMetrics;
import com.ebay.sojourner.common.model.TagSumMetrics;
import com.ebay.sojourner.common.model.TotalCntMetrics;
import com.ebay.sojourner.common.model.TransformErrorMetrics;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import static java.util.Map.Entry;

@Slf4j
public class SojMetricsSplitAgg implements AggregateFunction<SojMetrics, SojMetrics, SojMetrics> {

    @Override
    public SojMetrics createAccumulator() {
        SojMetrics sojMetrics = new SojMetrics();
        return sojMetrics;
    }

    @Override
    public SojMetrics add(SojMetrics inputSojMetrics,
            SojMetrics sojMetrics) {
        addTagMissingCntMetrics(inputSojMetrics, sojMetrics);
        addTagSumMetrics(inputSojMetrics, sojMetrics);
        addTransformErrorCntMetrics(inputSojMetrics, sojMetrics);
        addPageCntMetrics(inputSojMetrics, sojMetrics);
        addTotalCntMetrics(inputSojMetrics, sojMetrics);
        sojMetrics.setEventTime(inputSojMetrics.getEventTime());
        return sojMetrics;
    }

    @Override
    public SojMetrics getResult(SojMetrics sojMetrics) {
        return sojMetrics;
    }

    @Override
    public SojMetrics merge(SojMetrics a, SojMetrics b) {
        return null;
    }

    private void addTagMissingCntMetrics(SojMetrics inputSojMetrics,
            SojMetrics sojMetrics) {
        for (Entry<String, TagMissingCntMetrics> entry : inputSojMetrics
                .getTagMissingCntMetricsMap().entrySet()) {
            String               metricKey            = entry.getKey();
            TagMissingCntMetrics tagMissingCntMetrics = entry.getValue();
            if (sojMetrics.getTagMissingCntMetricsMap().containsKey(metricKey)) {
                sojMetrics.getTagMissingCntMetricsMap().get(metricKey).getPageFamilySet()
                        .addAll(tagMissingCntMetrics.getPageFamilySet());
                Map<String, Map<String, Long>> domainTagCntMap
                        = tagMissingCntMetrics.getTagCntMap();
                Map<String, Map<String, Long>> sojDomainTagCntMap
                        = sojMetrics.getTagMissingCntMetricsMap()
                        .get(metricKey).getTagCntMap();
                for (Entry<String, Map<String, Long>> domainTagCnt : domainTagCntMap.entrySet()) {
                    String            domain           = domainTagCnt.getKey();
                    Map<String, Long> tagMissingCntMap = domainTagCnt.getValue();
                    if (sojDomainTagCntMap.containsKey(domain)) {
                        Map<String, Long> sojTagCntMap = sojDomainTagCntMap.get(domain);
                        for (Entry<String, Long> tagCntEntry : tagMissingCntMap.entrySet()) {
                            String tagName = tagCntEntry.getKey();
                            Long   count   = tagCntEntry.getValue();
                            if (sojTagCntMap.containsKey(tagName)) {
                                sojTagCntMap.put(tagName, sojTagCntMap.get(tagName) + count);
                            } else {
                                sojTagCntMap.put(tagName, count);
                            }
                        }
                    } else {
                        sojDomainTagCntMap.put(domain, tagMissingCntMap);
                    }
                }
            } else {
                sojMetrics.getTagMissingCntMetricsMap().put(metricKey, tagMissingCntMetrics);
            }
        }
    }

    private void addTagSumMetrics(SojMetrics inputSojMetrics, SojMetrics sojMetrics) {
        for (Entry<String, TagSumMetrics> entry
                : inputSojMetrics.getTagSumMetricsMap().entrySet()) {
            String        metricKey     = entry.getKey();
            TagSumMetrics tagSumMetrics = entry.getValue();
            if (sojMetrics.getTagSumMetricsMap().containsKey(metricKey)) {
                Map<String, Map<String, Double>> domainTagSumMap = tagSumMetrics.getTagSumMap();
                Map<String, Map<String, Double>> sojDomainTagSumMap
                        = sojMetrics.getTagSumMetricsMap()
                        .get(metricKey).getTagSumMap();
                for (Entry<String, Map<String, Double>> tagCntEntry
                        : domainTagSumMap.entrySet()) {
                    String domain = tagCntEntry.getKey();
                    if (sojDomainTagSumMap.containsKey(domain)) {
                        Map<String, Double> tagSum       = tagCntEntry.getValue();
                        Map<String, Double> sojTagSumMap = sojDomainTagSumMap.get(domain);
                        for (Entry<String, Double> tagSumEntry : tagSum.entrySet()) {
                            String tagName  = tagSumEntry.getKey();
                            Double tagValue = tagSumEntry.getValue();
                            if (sojTagSumMap.containsKey(tagName)) {
                                sojTagSumMap.put(tagName, sojTagSumMap.get(tagName) + tagValue);
                            } else {
                                sojTagSumMap.put(tagName, tagValue);
                            }
                        }
                    } else {
                        sojDomainTagSumMap.put(domain, tagCntEntry.getValue());
                    }
                }
            } else {
                sojMetrics.getTagSumMetricsMap().put(metricKey, tagSumMetrics);
            }
        }
    }

    private void addPageCntMetrics(SojMetrics inputSojMetrics, SojMetrics sojMetrics) {
        for (Entry<String, PageCntMetrics> entry
                : inputSojMetrics.getPageCntMetricsMap().entrySet()) {
            String         metricKey      = entry.getKey();
            PageCntMetrics pageCntMetrics = entry.getValue();
            if (sojMetrics.getPageCntMetricsMap().containsKey(metricKey)) {
                Map<String, Map<Integer, Long>> sojDomainPageCntMap
                        = sojMetrics.getPageCntMetricsMap()
                        .get(metricKey).getPageCntMap();
                Map<String, Map<Integer, Long>> domainPageCntMap = pageCntMetrics.getPageCntMap();
                for (Entry<String, Map<Integer, Long>> domainPageCntEntry
                        : domainPageCntMap.entrySet()) {
                    String domain = domainPageCntEntry.getKey();
                    if (sojDomainPageCntMap.containsKey(domain)) {
                        Map<Integer, Long> pageCntMap    = domainPageCntEntry.getValue();
                        Map<Integer, Long> sojPageCntMap = sojDomainPageCntMap.get(domain);
                        for (Entry<Integer, Long> pageCntEntry : pageCntMap.entrySet()) {
                            Integer pageId = pageCntEntry.getKey();
                            Long    count  = pageCntEntry.getValue();
                            if (sojPageCntMap.containsKey(pageId)) {
                                sojPageCntMap.put(pageId, sojPageCntMap.get(pageId) + count);
                            } else {
                                sojPageCntMap.put(pageId, count);
                            }
                        }
                    } else {
                        sojDomainPageCntMap.put(domain, domainPageCntEntry.getValue());
                    }
                }
            } else {
                sojMetrics.getPageCntMetricsMap().put(metricKey, pageCntMetrics);
            }
        }
    }

    private void addTransformErrorCntMetrics(SojMetrics inputSojMetrics,
            SojMetrics sojMetrics) {
        for (Entry<String, TransformErrorMetrics> entry
                : inputSojMetrics.getTransformErrorMetricsMap().entrySet()) {
            String                metricKey             = entry.getKey();
            TransformErrorMetrics transformErrorMetrics = entry.getValue();
            if (sojMetrics.getTransformErrorMetricsMap().containsKey(metricKey)) {

                Map<String, Map<String, Long>> domainTagErrorCntMap
                        = transformErrorMetrics.getTagErrorCntMap();
                Map<String, Map<String, Long>> sojDomainTagErrorCntMap
                        = sojMetrics.getTransformErrorMetricsMap()
                        .get(metricKey).getTagErrorCntMap();
                for (Entry<String, Map<String, Long>> domainTagErrorCntEntry
                        : domainTagErrorCntMap.entrySet()) {
                    String domain = domainTagErrorCntEntry.getKey();
                    if (sojDomainTagErrorCntMap.containsKey(domain)) {
                        Map<String, Long> tagErrorCntMap    = domainTagErrorCntEntry.getValue();
                        Map<String, Long> sojTagErrorCntMap = sojDomainTagErrorCntMap.get(domain);
                        for (Entry<String, Long> tagCntEntry : tagErrorCntMap.entrySet()) {
                            String tagName = tagCntEntry.getKey();
                            Long   count   = tagCntEntry.getValue();
                            if (sojTagErrorCntMap.containsKey(tagName)) {
                                sojTagErrorCntMap.put(tagName,
                                        sojTagErrorCntMap.get(tagName) + count);
                            } else {
                                sojTagErrorCntMap.put(tagName, count);
                            }
                        }
                    } else {
                        sojDomainTagErrorCntMap.put(domain, domainTagErrorCntEntry.getValue());
                    }
                }
            } else {
                sojMetrics.getTransformErrorMetricsMap().put(metricKey, transformErrorMetrics);
            }
        }
    }

    private void addTotalCntMetrics(SojMetrics inputSojMetrics, SojMetrics sojMetrics) {
        for (Entry<String, TotalCntMetrics> entry
                : inputSojMetrics.getTotalCntMetricsMap().entrySet()) {
            String          metricKey       = entry.getKey();
            TotalCntMetrics totalCntMetrics = entry.getValue();
            if (sojMetrics.getTotalCntMetricsMap().containsKey(metricKey)) {
                Map<String, Long> sojDomainPageCntMap
                        = sojMetrics.getTotalCntMetricsMap()
                        .get(metricKey).getTotalCntMap();
                Map<String, Long> domainTotalCntMap = totalCntMetrics.getTotalCntMap();
                for (Entry<String, Long> domainTotalCntEntry
                        : domainTotalCntMap.entrySet()) {
                    String domain = domainTotalCntEntry.getKey();
                    if (sojDomainPageCntMap.containsKey(domain)) {
                        Long totalCnt    = domainTotalCntEntry.getValue();
                        Long sojTotalCnt = sojDomainPageCntMap.get(domain);
                        sojDomainPageCntMap.put(domain, totalCnt + sojTotalCnt);
                    } else {
                        sojDomainPageCntMap.put(domain, domainTotalCntEntry.getValue());
                    }
                }
                Map<String, Long> sojDomainPageCntItmMap
                        = sojMetrics.getTotalCntMetricsMap()
                        .get(metricKey).getTotalCntItmMap();
                Map<String, Long> domainTotalCntItmMap = totalCntMetrics.getTotalCntItmMap();
                for (Entry<String, Long> domainTotalCntItmEntry
                        : domainTotalCntItmMap.entrySet()) {
                    String domain = domainTotalCntItmEntry.getKey();
                    if (sojDomainPageCntItmMap.containsKey(domain)) {
                        Long totalCnt    = domainTotalCntItmEntry.getValue();
                        Long sojTotalCnt = sojDomainPageCntItmMap.get(domain);
                        sojDomainPageCntItmMap.put(domain, totalCnt + sojTotalCnt);
                    } else {
                        sojDomainPageCntItmMap.put(domain, domainTotalCntItmEntry.getValue());
                    }
                }
            } else {
                sojMetrics.getTotalCntMetricsMap().put(metricKey, totalCntMetrics);
            }
        }
    }
}
