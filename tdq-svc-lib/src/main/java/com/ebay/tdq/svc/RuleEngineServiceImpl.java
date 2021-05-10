package com.ebay.tdq.svc;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.dto.IDoMetricConfig;
import com.ebay.tdq.dto.TdqDataResult;
import com.ebay.tdq.dto.TdqResult;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.ProfilingSqlParser;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.service.RuleEngineService;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.JsonUtils;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.io.IOUtils;

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
      sample   = new ArrayList<>();
      for (String json :
          IOUtils.readLines(RuleEngineServiceImpl.class.getResourceAsStream("/pathfinder_raw_event.txt"))) {
        sample.add(JsonUtils.parseObject(json, RawEvent.class));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public TdqDataResult<String> translateConfig(IDoMetricConfig iDoMetricConfig) {
    return null;
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
              DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString())
          );
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
      Map<String, TdqMetric> map = Maps.newHashMap();
      try {
        int i = 10000;
        while (i > 0) {
          for (RawEvent event : sample) {
            TdqMetric newMetric = plan.process(event);
            map.compute(newMetric.getUid(), (key, old) -> {
              if (old != null) {
                return plan.merge(newMetric, old);
              } else {
                return newMetric;
              }
            });
          }
          i--;
        }
      } catch (Exception e) {
        result.exception(e);
      }
      return System.currentTimeMillis() - s;
    });

    try {
      result.setData(result.getData() + future.get(1, TimeUnit.SECONDS));
    } catch (TimeoutException e) {
      result.timeout();
      result.setException(e);
    } catch (ExecutionException e) {
      result.exception(e);
      result.setMsg("execute failed");
    } catch (InterruptedException e) {
      result.exception(e);
      result.setMsg("interrupted");
    } finally {
      future.cancel(true);
    }
  }

}
