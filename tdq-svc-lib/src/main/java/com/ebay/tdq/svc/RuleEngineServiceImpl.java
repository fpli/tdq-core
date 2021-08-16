package com.ebay.tdq.svc;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.common.model.TdqEvent;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.config.ExpressionConfig;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.config.TransformationConfig;
import com.ebay.tdq.dto.IDoMetricConfig;
import com.ebay.tdq.dto.IDoMetricConfigSchema;
import com.ebay.tdq.dto.TdqDataResult;
import com.ebay.tdq.dto.TdqResult;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.ProfilingSqlParser;
import com.ebay.tdq.service.RuleEngineService;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.JsonUtils;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author juntzhang
 */
@Slf4j
public class RuleEngineServiceImpl implements RuleEngineService {

  private final ExecutorService executor;
  private final List<RawEvent> sample;

  RuleEngineServiceImpl() {
    try {
      executor = new ThreadPoolExecutor(
          5, 50, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
      sample = new ArrayList<>();
      for (String json : getSampleData()) {
        RawEvent event = JsonUtils.parseObject(json, RawEvent.class);
        event.setEventTimestamp(System.currentTimeMillis());
        sample.add(event);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static List<String> getSampleData() throws IOException {
    try (InputStream is = ServiceFactory.class.getResourceAsStream("/pathfinder_raw_event.txt")) {
      return IOUtils.readLines(is);
    }
  }

  public static String getIDoConfigSchema() throws IOException {
    try (InputStream is = ServiceFactory.class.getResourceAsStream("/iDo_config_schema.json")) {
      return IOUtils.toString(is);
    }
  }

  // replace field placeholder
  private String replaceFieldPattern(IDoMetricConfigSchema schema, IDoMetricConfig iDoMetricConfig, String field,
      List<TransformationConfig> transformations) throws IllegalAccessException {
    String[] arr = field.split("\\.");
    if (arr.length == 1) {
      TransformationConfig c = schema.getTransformations().get(arr[0]);
      if (c == null) {
        throw new IllegalAccessException(field + " is not define in schema!");
      }
      addTransformations(transformations, c);
      return arr[0];
    } else if (arr.length == 2) {
      String pattern = schema.getFieldPatterns().get(arr[0]);
      if (StringUtils.isBlank(pattern)) {
        throw new IllegalAccessException(field + " is illegal!");
      }
      return iDoMetricConfig.cast(pattern.replace("__TDQ_PLACEHOLDER", arr[1]), field);
    } else {
      throw new IllegalAccessException(field + " is illegal!");
    }
  }

  private void addTransformations(List<TransformationConfig> transformations, TransformationConfig transformation) {
    if (transformations.stream().noneMatch(t -> t.getAlias().equals(transformation.getAlias()))) {
      transformations.add(transformation);
    }
  }

  @Override
  public TdqDataResult<String> translateConfig(IDoMetricConfig iDoMetricConfig) {
    TdqDataResult<String> result = new TdqDataResult<>();
    try {
      IDoMetricConfigSchema schema = JsonUtils.parseObject(getIDoConfigSchema(), IDoMetricConfigSchema.class);
      IDoMetricConfigSchema.Aggregate aggregate = schema.getAggregates().get(iDoMetricConfig.getOperator());
      if (aggregate == null) {
        throw new IllegalAccessException("iDoMetricConfig.getOperator() is not defined!");
      }
      List<TransformationConfig> transformations = new ArrayList<>();

      // transfer defined fields
      String filter = iDoMetricConfig.getFilter();
      if (StringUtils.isNotBlank(filter)) {
        for (SqlIdentifier identifier : ProfilingSqlParser.getFilterIdentifiers(iDoMetricConfig.getFilter())) {
          String fieldName = identifier.toString();
          String definedField = replaceFieldPattern(schema, iDoMetricConfig, fieldName, transformations);
          filter = filter.replace(fieldName, definedField);
        }
      }

      if (CollectionUtils.isNotEmpty(iDoMetricConfig.getDimensions())) {
        for (String d : iDoMetricConfig.getDimensions()) {
          TransformationConfig c = schema.getTransformations().get(d);
          if (c == null) {
            throw new IllegalAccessException(d + " is not define in schema!");
          }
          addTransformations(transformations, c);
        }
      }

      for (int i = 0; i < iDoMetricConfig.getExpressions().size(); i++) {
        String expr = iDoMetricConfig.getExpressions().get(i);
        for (SqlIdentifier identifier : ProfilingSqlParser.getExprIdentifiers(expr)) {
          String fieldName = identifier.toString();
          String definedField = replaceFieldPattern(schema, iDoMetricConfig, fieldName, transformations);
          expr = expr.replace(fieldName, definedField);
        }

        for (TransformationConfig c : aggregate.getParams()) {
          String originalTest = ((String) c.getExpression().getConfig().get("text"));
          if (StringUtils.isNotBlank(originalTest)) {
            String text = originalTest.replace("__TDQ_PLACEHOLDER_" + (i + 1), "(" + expr + ")");
            c.getExpression().getConfig().put("text", text);
          } else {
            String originalArg0 = ((String) c.getExpression().getConfig().get("arg0"));
            if (StringUtils.isNotBlank(originalArg0)) {
              String arg0 = originalArg0.replace("__TDQ_PLACEHOLDER_" + (i + 1), "(" + expr + ")");
              c.getExpression().getConfig().put("arg0", arg0);
            }
          }
          addTransformations(transformations, c);
        }
      }

      ProfilerConfig profilerConfig = new ProfilerConfig(
          iDoMetricConfig.getMetricName(),
          null, ExpressionConfig.expr(aggregate.getExpr()),
          filter,
          transformations,
          iDoMetricConfig.getDimensions(),
          iDoMetricConfig.getComment(),
          null
      );

      Map<String, Object> config = new HashMap<>();
      config.put("window", iDoMetricConfig.getWindow());
      String id = RandomStringUtils.randomAlphabetic(1) + RandomStringUtils.randomAlphanumeric(9);

      RuleConfig ruleConfig = RuleConfig.builder()
          .name("r_" + id)
          .profiler(profilerConfig)
          .type("realtime.rheos.profiler")
          .config(config)
          .build();

      TdqConfig tdqConfig = TdqConfig.builder()
          .id(id)
          .name("cfg_" + id)
          .rule(ruleConfig)
          .build();
      result.setData(JsonUtils.toJSONString(tdqConfig));
      return result;
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
      result.exception(e).failed();
      return result;
    }
  }

  @Override
  public TdqResult verifyExpression(String code) {
    try {
      SqlNode node = ProfilingSqlParser.getExpr(code);
      log.info("verifyExpression[code={},node={}] success!", code, node);
    } catch (Exception e) {
      return TdqResult.builder().msg(e.getMessage()).exception(e).build().failed();
    }
    return TdqResult.builder().msg("ok").build();
  }

  @Override
  public TdqResult verifyTdqConfig(String tdqConfig) {
    final TdqDataResult<Long> result = new TdqDataResult<>();
    TdqConfig config;
    try {
      config = JsonUtils.parseObject(tdqConfig, TdqConfig.class);
    } catch (IOException e) {
      result.exception(e);
      return result;
    }
    result.setData(0L);
    for (RuleConfig ruleConfig : config.getRules()) {
      for (ProfilerConfig profilerConfig : ruleConfig.getProfilers()) {
        try {
          ProfilingSqlParser parser = new ProfilingSqlParser(
              profilerConfig,
              DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString()),
              null,
              null);
          final PhysicalPlan plan = parser.parsePlan();
          plan.validatePlan();
          process(result, plan);
          if (!result.isOk()) {
            return result;
          }
        } catch (Exception e) {
          log.warn("profilerConfig[" + profilerConfig + "] validate exception:" + e.getMessage(), e);
          result.exception(e);
          return result;
        }
      }
    }
    result.setMsg("ok");
    return result;
  }

  private void process(TdqDataResult<Long> result, PhysicalPlan plan) {
    Future<Long> future = executor.submit(() -> {
      long s = System.currentTimeMillis();
      Map<String, InternalMetric> map = Maps.newHashMap();
      try {
        int i = 1;
        while (i > 0) {
          for (RawEvent event : sample) {
            InternalMetric newMetric = plan.process(new TdqEvent(event));
            if (newMetric != null) {
              map.compute(newMetric.getMetricId(), (key, old) -> {
                if (old != null) {
                  return plan.merge(newMetric, old);
                } else {
                  return newMetric;
                }
              });
            }
          }
          i--;
        }
        map.values().forEach(System.out::println);
      } catch (Exception e) {
        log.warn(e.getMessage(), e);
        result.exception(e);
      }
      log.warn(plan.metricKey() + " cost time {}ms", (System.currentTimeMillis() - s));
      return System.currentTimeMillis() - s;
    });

    try {
      result.setData(result.getData() + future.get(3, TimeUnit.SECONDS));
      //result.setData(result.getData() + future.get());
    } catch (TimeoutException e) {
      result.timeout();
      result.setException(e);
      result.setMsg("pls check rule maybe have some performance issue!");
    } catch (ExecutionException e) {
      log.warn(e.getMessage(), e);
      result.exception(e);
      result.setMsg("execute failed");
    } catch (InterruptedException e) {
      log.warn(e.getMessage(), e);
      result.exception(e);
      result.setMsg("interrupted");
    } finally {
      future.cancel(true);
    }
  }

}
