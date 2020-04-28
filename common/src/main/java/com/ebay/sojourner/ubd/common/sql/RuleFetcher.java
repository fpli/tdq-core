package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.util.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class RuleFetcher {

  private ScheduledExecutorService scheduledExecutorService;
  private OkHttpClient client;
  private Request request;
  private RuleManager ruleManager;

  public RuleFetcher(RuleManager ruleManager) {
    client = new OkHttpClient();
    this.ruleManager = ruleManager;
  }

  protected void fetchRulesPeriodically() {
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    log.info("Fetch rules periodically");
    scheduledExecutorService
        .scheduleAtFixedRate(this::fetchRules, 0, 10, TimeUnit.SECONDS);
  }

  protected synchronized void fetchRules() {
    try {
      request = bulidHttpRequest(Constants.REST_SERVER + Constants.API_RULE_LIST_PUBLISHED);
      try (Response response = client.newCall(request).execute()) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<RuleDefinition> responseBodyContent = objectMapper.reader()
            .forType(new TypeReference<List<RuleDefinition>>() {
            }).readValue(response.body().bytes());
        log.info("Fetch rules at " + new Date());
        log.info("Rules = " + responseBodyContent);
        if (ruleManager != null) {
          ruleManager.updateRules(responseBodyContent);
        }
      }
    } catch (Exception e) {
      log.warn("fetch all published rule failed", e);
    }
  }

  public synchronized void fetchRulesById(Long ruleId) {
    try {
      request = bulidHttpRequest(
          Constants.REST_SERVER + Constants.API_SPECIFIED_RULE_PREFIX + ruleId);
      try (Response response = client.newCall(request).execute()) {
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = response.body().string();
        RuleDefinition ruleDefinition = objectMapper.readValue(responseBody, RuleDefinition.class);
        log.info("Fetch rules at " + new Date());
        log.info("Rules = " + ruleDefinition);
        if (ruleManager != null) {
          ruleManager.updateRulesById(ruleDefinition, ruleId);
        }
      }
    } catch (Exception e) {
      log.warn("fetch specified rule failed", e);
    }
  }

  private Request bulidHttpRequest(String url) {
    return new Request.Builder()
        .url(url)
        .addHeader("X-Auth-Username", "soj-flink-app")
        .addHeader("X-Auth-Token", "B65613BAE51443268AEBCAEF26C30ABE")
        .build();
  }

  public static void example1() throws Exception {
    RuleFetcher fetcher = new RuleFetcher(null);
    Response response = fetcher.client.newCall(fetcher.request).execute();
    ObjectMapper objectMapper = new ObjectMapper();
    List<RuleDefinition> responseBodyContent = objectMapper.reader()
        .forType(new TypeReference<List<RuleDefinition>>() {
        }).readValue(response.body().bytes());
    log.info(responseBodyContent.toString());
  }

  public static void example2() {
    RuleFetcher fetcher = new RuleFetcher(null);
    fetcher.fetchRulesPeriodically();
  }

  public static void main(String[] args) {
    example2();
  }

}
