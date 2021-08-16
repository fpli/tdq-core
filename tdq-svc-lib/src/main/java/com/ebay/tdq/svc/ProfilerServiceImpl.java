package com.ebay.tdq.svc;

import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.dto.QueryDropdownParam;
import com.ebay.tdq.dto.QueryDropdownResult;
import com.ebay.tdq.dto.QueryProfilerParam;
import com.ebay.tdq.dto.QueryProfilerResult;
import com.ebay.tdq.dto.TdqResult;
import com.ebay.tdq.expressions.Expression;
import com.ebay.tdq.expressions.aggregate.Max;
import com.ebay.tdq.expressions.aggregate.Min;
import com.ebay.tdq.rules.ExpressionParser;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.ProfilingSqlParser;
import com.ebay.tdq.rules.Transformation;
import com.ebay.tdq.service.ProfilerService;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.JsonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.avro.Schema;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
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
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author juntzhang
 */
@Slf4j
public class ProfilerServiceImpl implements ProfilerService {

  final RestHighLevelClient client;

  ProfilerServiceImpl(RestHighLevelClient client) {
    this.client = client;
  }

  /**
   * realtime.rheos.profilers
   */
  @Override
  public QueryProfilerResult query(QueryProfilerParam param) {
    try {
      Validate.isTrue(StringUtils.isNotBlank(param.getTdqConfig()), "TDQ config is empty");

      TdqConfig config = JsonUtils.parseObject(param.getTdqConfig(), TdqConfig.class);
      Validate.isTrue(config.getRules().size() == 1, "currently only support one rule.");

      RuleConfig ruleConfig = config.getRules().get(0);
      int window = (int) DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString());
      if (ruleConfig.getType().equals("realtime.rheos.profiler")) {
        return query(param, ruleConfig.getProfilers().get(0), window);
      } else if (ruleConfig.getType().equals("realtime.rheos.profilers")) {
        return query(param, ruleConfig.getProfilers(), window, (String) ruleConfig.getConfig().get("profilers-expr"));
      } else {
        throw new IllegalStateException("Unexpected operator: " + ruleConfig.getType());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return QueryProfilerResult.builder().param(param).exception(e).code(TdqResult.Code.FAILED).build();
    }
  }

  private QueryProfilerResult query(QueryProfilerParam param, ProfilerConfig profilerConfig, int window)
      throws IOException, ParseException {
    final QueryProfilerResult.QueryProfilerResultBuilder<?, ?> resultBuilder =
        QueryProfilerResult.builder().param(param);
    for (InternalMetric metric : query0(param, profilerConfig, window)) {
      resultBuilder.record(new QueryProfilerResult.Record(metric.getEventTime(), metric.getValue()));
    }
    return resultBuilder.build();
  }

  private QueryProfilerResult query(QueryProfilerParam param, List<ProfilerConfig> profilerConfigs, int window,
      String evaluation)
      throws IOException, ParseException {
    final QueryProfilerResult.QueryProfilerResultBuilder<?, ?> resultBuilder =
        QueryProfilerResult.builder().param(param);

    Map<Long, Map<String, Object>> params = Maps.newHashMap();

    Map<String, Set<String>> dimensions = param.getDimensions();
    for (ProfilerConfig pc : profilerConfigs) {
      Map<String, Set<String>> newDimensions = new HashMap<>();
      for (val e : dimensions.entrySet()) {
        if (e.getKey().startsWith(pc.getMetricName())) {
          newDimensions.put(e.getKey().split("\\.")[1], e.getValue());
        }
      }
      for (InternalMetric metric : query0(
          new QueryProfilerParam(param.getTdqConfig(), param.getFrom(), param.getTo(), newDimensions), pc, window)) {
        params.compute(metric.getEventTime(), (key, values) -> {
          if (values == null) {
            values = new HashMap<>();
          }
          Map<String, Object> finalValues = values;
          metric.getValues().forEach((k, v) -> finalValues.put(metric.getMetricName() + "." + k, v));
          return finalValues;
        });
      }
    }
    Expression expression = ExpressionParser.expr(evaluation, ServiceFactory.getTdqEnv(), Schema.Type.DOUBLE);
    params.forEach((k, v) -> {
      final Object t = PhysicalPlan.eval(expression, v);
      resultBuilder.record(new QueryProfilerResult.Record(k, t == null ? 0d : (double) t));
    });
    return resultBuilder.build();
  }

  private List<InternalMetric> query0(QueryProfilerParam param, ProfilerConfig profilerConfig, int window)
      throws ParseException, IOException {
    Validate.isTrue(StringUtils.isNotEmpty(profilerConfig.getMetricName()), "metric name is required.");

    Long from = getFrom(profilerConfig, param.getFrom());

    PhysicalPlan physicalPlan = new ProfilingSqlParser(profilerConfig, window, null, null).parsePlan();

    SearchSourceBuilder builder = new SearchSourceBuilder();
    BoolQueryBuilder rootBuilder = QueryBuilders.boolQuery();

    rootBuilder.must(QueryBuilders.rangeQuery("event_time").gte(from).lte(param.getTo()));
    rootBuilder.must(QueryBuilders.termQuery("metric_key", profilerConfig.getMetricName()));

    if (MapUtils.isNotEmpty(param.getDimensions())) {
      param.getDimensions().forEach((k, v) -> {
        rootBuilder.must(QueryBuilders.termsQuery("tags." + k + ".raw", v));
      });
    }
    ProfilingSqlParser parser = new ProfilingSqlParser(profilerConfig, window, null, null);
    new ProntoQueryBuilder(rootBuilder, parser.parseProntoFilterExpr()).submit();

    builder.query(rootBuilder);

    AggregationBuilder aggregation = AggregationBuilders.dateHistogram("agg").field("event_time")
        .fixedInterval(DateHistogramInterval.seconds(window));

    for (Transformation aggPlan : physicalPlan.aggregations()) {
      if (aggPlan.expr() instanceof Max) {
        aggregation.subAggregation(AggregationBuilders.max(aggPlan.name()).field("expr." + aggPlan.name()));
      } else if (aggPlan.expr() instanceof Min) {
        aggregation.subAggregation(AggregationBuilders.min(aggPlan.name()).field("expr." + aggPlan.name()));
      } else {
        aggregation.subAggregation(AggregationBuilders.sum(aggPlan.name()).field("expr." + aggPlan.name()));
      }
    }

    builder.aggregation(aggregation);
    builder.size(0);
    log.info("search request {}", builder);
    SearchRequest searchRequest = new SearchRequest(calculateIndexes(from, param.getTo()), builder);
    searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    Histogram agg = searchResponse.getAggregations().get("agg");
    List<InternalMetric> result = Lists.newArrayList();
    for (Histogram.Bucket entry : agg.getBuckets()) {
      long eventTime = Long.parseLong(entry.getKeyAsString());
      InternalMetric tdqMetric = new InternalMetric(physicalPlan.metricKey(), eventTime);
      for (Transformation aggPlan : physicalPlan.aggregations()) {
        tdqMetric.putExpr(aggPlan.name(),
            ((NumericMetricsAggregation.SingleValue) entry.getAggregations().get(aggPlan.name())).value());
      }
      physicalPlan.evaluate(tdqMetric);
      result.add(tdqMetric);
    }
    return result;
  }

  @Override
  public QueryDropdownResult dropdown(QueryDropdownParam param) {
    final QueryDropdownResult.QueryDropdownResultBuilder<?, ?> resultBuilder =
        QueryDropdownResult.builder().param(param);
    try {
      Validate.isTrue(StringUtils.isNotBlank(param.getTdqConfig()), "TDQ config is empty");

      TdqConfig config = JsonUtils.parseObject(param.getTdqConfig(), TdqConfig.class);
      Validate.isTrue(config.getRules().size() == 1, "currently only support one rule.");

      RuleConfig ruleConfig = config.getRules().get(0);

      int window = (int) DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString());

      for (ProfilerConfig profilerConfig : ruleConfig.getProfilers()) {
        Validate.isTrue(StringUtils.isNotEmpty(profilerConfig.getMetricName()), "metric name is required.");
        Long from = getFrom(profilerConfig, param.getFrom());

        ProfilingSqlParser parser = new ProfilingSqlParser(profilerConfig, window, null, null);
        PhysicalPlan physicalPlan = parser.parsePlan();
        if (ArrayUtils.isEmpty(physicalPlan.dimensions())) {
          return resultBuilder.build();
        }

        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder rootBuilder = QueryBuilders.boolQuery();

        rootBuilder.must(QueryBuilders.rangeQuery("event_time").gte(from).lte(param.getTo()));
        rootBuilder.must(QueryBuilders.termQuery("metric_key", profilerConfig.getMetricName()));

        new ProntoQueryBuilder(rootBuilder, parser.parseProntoDropdownExpr()).submit();

        builder.query(rootBuilder);
        for (Transformation t : physicalPlan.dimensions()) {
          String dimension = t.name();
          AggregationBuilder aggregation = AggregationBuilders
              .terms("agg_" + dimension)
              .field("tags." + dimension + ".raw")
              .size(1000)
              .order(BucketOrder.key(true));

          builder.aggregation(aggregation);
          builder.size(0);
        }
        log.info("search request {}", builder);
        SearchRequest searchRequest = new SearchRequest(calculateIndexes(from, param.getTo()), builder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (Transformation t : physicalPlan.dimensions()) {
          String dimension = t.name();
          ParsedStringTerms agg = searchResponse.getAggregations().get("agg_" + dimension);
          final QueryDropdownResult.Record.RecordBuilder record = QueryDropdownResult.Record
              .builder()
              .metricName(profilerConfig.getMetricName())
              .name(dimension);
          for (val entry : agg.getBuckets()) {
            record.item(entry.getKeyAsString());
          }
          resultBuilder.record(record.build());
        }
      }

      return resultBuilder.build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultBuilder.exception(e).code(TdqResult.Code.FAILED);
      return resultBuilder.build();
    }
  }

  private String[] calculateIndexes(long from, long end) {
    Set<String> results = new HashSet<>();
    long next = from;
    while (end >= next) {

      results.add(ServiceFactory.getTdqEnv().getSinkEnv().getNormalMetricIndex(next));
      next = next + 86400 * 1000;
    }
    results.add(ServiceFactory.getTdqEnv().getSinkEnv().getNormalMetricIndex(end));
    log.info("search request indexes=>{}", StringUtils.join(results, ","));
    return results.toArray(new String[0]);
  }

  private Long getFrom(ProfilerConfig config, long from) throws ParseException {
    Long birthday = config.getMetricBirthday();
    if (birthday == null) {
      return from;
    } else {
      return Math.max(birthday, from);
    }
  }
}
