package com.ebay.tdq.svc;

import com.ebay.tdq.dto.TdqMtrcQryParam;
import com.ebay.tdq.dto.TdqMtrcQryRs;
import com.ebay.tdq.service.FetchMetricsService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static com.ebay.tdq.utils.ProntoUtils.*;

/**
 * @author xiaoding
 * @since 2021/7/14 1:17 AM
 */
@Slf4j
public class FetchMetricsMsMtcServiceImpl implements
        FetchMetricsService<TdqMtrcQryParam, TdqMtrcQryRs> {
    final RestHighLevelClient client;
    BigDecimal bg;

    FetchMetricsMsMtcServiceImpl(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public TdqMtrcQryRs fetchMetrics(TdqMtrcQryParam param) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder rootBuilder = QueryBuilders.boolQuery();
        rootBuilder.must(QueryBuilders.rangeQuery("event_time").gte(param.getFrom()).lte(param.getTo()));
        rootBuilder.must(QueryBuilders.termQuery("metric_name.raw", param.getMetricName()));
        rootBuilder.must(QueryBuilders.termQuery("metric_type.raw", param.getMetricType()));
        Validate.noNullElements(param.getTags(), "tags cannot be null, pls set value");
        if (StringUtils.isNotEmpty(param.getPageFmy()) && StringUtils.isNotEmpty(param.getSiteId())) {
            rootBuilder.must(QueryBuilders.termsQuery("site_id.raw", param.getSiteId()));
            rootBuilder.must(QueryBuilders.termsQuery("page_family.raw", param.getPageFmy()));
        }
        if (CollectionUtils.isNotEmpty(param.getExcludePgFmys())) {
            rootBuilder.mustNot(QueryBuilders.termsQuery("page_family.raw", param.getExcludePgFmys()));
        }
        if (CollectionUtils.isNotEmpty(param.getTags())) {
            param.getTags().forEach((e) -> rootBuilder.should(QueryBuilders
                    .termsQuery("tag_name", e)));
        }
        builder.query(rootBuilder);
        AggregationBuilder aggregation = AggregationBuilders.dateHistogram("agg").field("event_time")
                .fixedInterval(DateHistogramInterval.seconds(300));
        if (CollectionUtils.isNotEmpty(param.getTagMetrics())) {
            for (String tagMetric : param.getTagMetrics()) {
                switch (param.getAggMethod()) {
                    case SUM:
                        aggregation.subAggregation(AggregationBuilders.sum(tagMetric).field(tagMetric));
                        break;
                    case AVG:
                        aggregation.subAggregation(AggregationBuilders.avg(tagMetric).field(tagMetric));
                        break;
                    case COUNT:
                        aggregation.subAggregation(AggregationBuilders.count(tagMetric).field(tagMetric));
                        break;
                    default:
                        break;
                }

            }
        }
        builder.aggregation(aggregation);
        builder.size(0);
        log.info("search request {}", builder);
        SearchRequest searchRequest = new SearchRequest(calculateIndexes(param), builder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Histogram agg = searchResponse.getAggregations().get("agg");
        TdqMtrcQryRs.TdqMtrcQryRsBuilder tdqMsMtcRsBuilder = TdqMtrcQryRs.builder();
        tdqMsMtcRsBuilder.metricName(param.getMetricName());
        tdqMsMtcRsBuilder.metricType(param.getMetricType());
        tdqMsMtcRsBuilder.from(param.getFrom());
        tdqMsMtcRsBuilder.to(param.getTo());
        tdqMsMtcRsBuilder.aggMethod(param.getAggMethod());

        switch (param.getMetricsMethod()) {
            case PERCENTAGE: {
                Double value1 = 0.0;
                Double value2 = 0.0;
                for (Histogram.Bucket entry : agg.getBuckets()) {
                    long eventTime = Long.parseLong(entry.getKeyAsString());
                    value1 += ((NumericMetricsAggregation.SingleValue) entry.getAggregations()
                            .get(param.getTagMetrics().get(0))).value();
                    value2 += ((NumericMetricsAggregation.SingleValue) entry.getAggregations()
                            .get(param.getTagMetrics().get(1))).value();
                }
                double percnt = value1 / value2 * 100;
                bg = new BigDecimal(percnt);
                percnt = bg.setScale(param.getPrecision(), BigDecimal.ROUND_HALF_UP).doubleValue();
                Map<String, Map<String, Double>> metricsMap = Maps.newHashMap();
                Map<String, Double> tagValueMap = Maps.newHashMap();
                tagValueMap.put(constructTags(param), percnt);
                metricsMap.put(constructDomain(param), tagValueMap);
                tdqMsMtcRsBuilder.tagMetrics(metricsMap);
                break;
            }
            case COUNT: {
                Long value = 0L;
                for (Histogram.Bucket entry : agg.getBuckets()) {
                    long eventTime = Long.parseLong(entry.getKeyAsString());
                    value += (long) ((NumericMetricsAggregation.SingleValue) entry.getAggregations()
                            .get(param.getTagMetrics().get(0))).value();
                }
                Map<String, Map<String, Long>> metricsMap = Maps.newHashMap();
                Map<String, Long> tagValueMap = Maps.newHashMap();
                tagValueMap.put(constructTags(param), value);
                metricsMap.put(constructDomain(param), tagValueMap);
            }
        }
        return tdqMsMtcRsBuilder.build();
    }
}
