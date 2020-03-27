package com.ebay.sojourner.ubd.common.sql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;

public class RuleFetcher {

  protected static final Logger LOGGER = Logger.getLogger(RuleManager.class);
  public static final String REST_SERVER = "http://10.169.116.50:9001";
  public static final String API_RULE_LIST_PUBLISHED = "/api/rule/list/published";

  private ScheduledExecutorService scheduledExecutorService;
  private OkHttpClient client;
  private Request request;
  private RuleManager ruleManager;

  public RuleFetcher(RuleManager ruleManager) {
    client = new OkHttpClient();
    request = new Request.Builder()
        .url(REST_SERVER + API_RULE_LIST_PUBLISHED)
        .build();
    this.ruleManager = ruleManager;
  }

  public void fetchRulesPeriodically() {
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    System.out.println("Fetch rules periodically");
    scheduledExecutorService.scheduleAtFixedRate(() -> {
      fetchRules();
    }, 0, 60, TimeUnit.SECONDS);
  }

  public synchronized void fetchRules() {
    try {
      try (Response response = client.newCall(request).execute()) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<RuleDefinition> responseBodyContent = objectMapper.reader()
            .forType(new TypeReference<List<RuleDefinition>>() {
            }).readValue(response.body().bytes());
        System.out.println("Fetch rules at " + new Date());
        System.out.println("Rules = " + responseBodyContent);
        if (ruleManager != null) {
          ruleManager.updateRules(responseBodyContent);
        }
      }
    } catch (Exception e) {
      LOGGER.warn(e);
    }
  }

  public static void example1() throws Exception {
    RuleFetcher fetcher = new RuleFetcher(null);
    Response response = fetcher.client.newCall(fetcher.request).execute();
    ObjectMapper objectMapper = new ObjectMapper();
    List<RuleDefinition> responseBodyContent = objectMapper.reader()
        .forType(new TypeReference<List<RuleDefinition>>() {
        }).readValue(response.body().bytes());
    System.out.println(responseBodyContent.toString());
  }

  public static void example2() {
    RuleFetcher fetcher = new RuleFetcher(null);
    fetcher.fetchRulesPeriodically();
  }

  public static void main(String[] args) {
    example2();
  }

}
