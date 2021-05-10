package com.ebay.tdq.svc;

import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.dto.QueryProfilerParam;
import com.ebay.tdq.dto.QueryProfilerResult;
import com.ebay.tdq.expressions.aggregate.Max;
import com.ebay.tdq.expressions.aggregate.Min;
import com.ebay.tdq.rules.AggrPhysicalPlan;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.ProfilingSqlParser;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.service.ProfilerService;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.JsonUtils;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
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

import static com.ebay.tdq.svc.ServiceFactory.INDEX_PREFIX;
import static com.ebay.tdq.svc.ServiceFactory.LATENCY_INDEX_PREFIX;
import static com.ebay.tdq.utils.DateUtils.calculateIndexDate;

/**
 * @author juntzhang
 */
@Slf4j
public class ProfilerServiceImpl implements ProfilerService {
  final RestHighLevelClient client;

  ProfilerServiceImpl(RestHighLevelClient client) {
    this.client = client;
  }

  @Override
  public QueryProfilerResult query(QueryProfilerParam param) {
    final QueryProfilerResult.QueryProfilerResultBuilder<?, ?> resultBuilder =
        QueryProfilerResult.builder().param(param);
    ProfilerConfig profilerConfig;
    int window;
    try {
      Validate.isTrue(param.getFrom() <= param.getTo(), "from must be earlier than to");

      TdqConfig config = JsonUtils.parseObject(param.getTdqConfig(), TdqConfig.class);
      Validate.isTrue(config.getRules().size() == 1, "currently only support one rule.");

      RuleConfig ruleConfig = config.getRules().get(0);
      Validate.isTrue(ruleConfig.getProfilers().size() == 1, "currently only support one profiler.");

      profilerConfig = ruleConfig.getProfilers().get(0);
      Validate.isTrue(StringUtils.isNotEmpty(profilerConfig.getMetricName()), "metric name is required.");
      window = (int) DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString());

      PhysicalPlan physicalPlan = new ProfilingSqlParser(profilerConfig, window).parsePlan();

      SearchSourceBuilder builder = new SearchSourceBuilder();
      BoolQueryBuilder rootBuilder = QueryBuilders.boolQuery();

      rootBuilder.must(QueryBuilders.rangeQuery("event_time").gte(param.getFrom()).lte(param.getTo()));
      rootBuilder.must(QueryBuilders.termQuery("metric_key", profilerConfig.getMetricName()));

      if (MapUtils.isNotEmpty(param.getDimensions())) {
        param.getDimensions().forEach((k, v) -> {
          rootBuilder.must(QueryBuilders.termsQuery("tags." + k, v));
        });
      }
      builder.query(rootBuilder);

      AggregationBuilder aggregation = AggregationBuilders.dateHistogram("agg").field("event_time")
          .fixedInterval(DateHistogramInterval.seconds(window));

      for (AggrPhysicalPlan aggPlan : physicalPlan.aggregations()) {
        if (aggPlan.evaluation() instanceof Max) {
          aggregation.subAggregation(AggregationBuilders.max(aggPlan.name()).field("expr." + aggPlan.name()));
        } else if (aggPlan.evaluation() instanceof Min) {
          aggregation.subAggregation(AggregationBuilders.min(aggPlan.name()).field("expr." + aggPlan.name()));
        } else {
          aggregation.subAggregation(AggregationBuilders.sum(aggPlan.name()).field("expr." + aggPlan.name()));
        }
      }

      builder.aggregation(aggregation);
      builder.size(0);
      log.info("search request {}", builder);
      SearchRequest searchRequest = new SearchRequest(calculateIndexes(param.getFrom(), param.getTo()), builder);
      searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());


      SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
      Histogram agg = searchResponse.getAggregations().get("agg");
      for (Histogram.Bucket entry : agg.getBuckets()) {
        long eventTime = Long.parseLong(entry.getKeyAsString());
        TdqMetric tdqMetric = new TdqMetric(physicalPlan.metricKey(), eventTime);
        for (AggrPhysicalPlan aggPlan : physicalPlan.aggregations()) {
          tdqMetric.putExpr(aggPlan.name(),
              ((NumericMetricsAggregation.SingleValue) entry.getAggregations().get(aggPlan.name())).value());
        }
        physicalPlan.evaluate(tdqMetric);
        resultBuilder.record(new QueryProfilerResult.Record(eventTime, tdqMetric.getValue()));
      }
      return resultBuilder.build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultBuilder.exception(e);
      return resultBuilder.build();
    }
  }

  private String[] calculateIndexes(long from, long end) {
    Set<String> results = new HashSet<>();
    long next = from;
    while (end >= next) {
      results.add(INDEX_PREFIX + calculateIndexDate(next));
      results.add(LATENCY_INDEX_PREFIX + calculateIndexDate(next));
      next = next + 86400 * 1000;
    }
    results.add(INDEX_PREFIX + calculateIndexDate(end));
    results.add(LATENCY_INDEX_PREFIX + calculateIndexDate(end));
    log.info("search request indexes=>{}", StringUtils.join(results, ","));
    return results.toArray(new String[0]);
  }
}
