package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.util.Constants;
import com.ebay.sojourner.ubd.common.util.RestApiUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
    client = RestApiUtils.getRestClient();
    this.ruleManager = ruleManager;
  }

  protected void fetchRulesPeriodically() {
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    log.info("Fetch rules periodically");
    scheduledExecutorService
        .scheduleAtFixedRate(this::fetchRules, 60, 15, TimeUnit.SECONDS);
  }

  public synchronized void fetchRules() {
    try {
      request = RestApiUtils
          .buildRequest(Constants.REST_SERVER + Constants.API_RULE_LIST_PUBLISHED);
      Response response = client.newCall(request).execute();
      ObjectMapper objectMapper = new ObjectMapper();
      List<RuleDefinition> responseBodyContent = objectMapper
          .reader()
          .forType(new TypeReference<List<RuleDefinition>>() {
          })
          .readValue(response.body().string());
      log.info("Fetch rules at " + new Date());
      log.info("Rules = " + responseBodyContent);

      if (ruleManager != null) {
        ruleManager.updateRules(responseBodyContent);
      }

    } catch (IOException e) {
      log.warn("fetch all published rule failed", e);
    }
  }

  public synchronized void fetchRulesById(Long ruleId, String id) {

    try {
      request = RestApiUtils.buildRequest(
          Constants.REST_SERVER + Constants.API_SPECIFIED_RULE_PREFIX + id);
      Response response = client.newCall(request).execute();
      ObjectMapper objectMapper = new ObjectMapper();
      String responseBody = response.body().string();
      RuleDefinition responseBodyContent = objectMapper
          .readValue(responseBody, RuleDefinition.class);
      log.info("Fetch rules at " + new Date());
      log.info("Rules = " + responseBodyContent);
      if (ruleManager != null) {
        ruleManager.updateRulesById(responseBodyContent, ruleId);
      }
    } catch (IOException e) {
      log.warn("fetch specified rule failed", e);
    }
  }
}
