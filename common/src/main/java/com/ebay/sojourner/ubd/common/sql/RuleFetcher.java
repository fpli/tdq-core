package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.util.Constants;
import com.ebay.sojourner.ubd.common.util.RestClientUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.compress.utils.Lists;

@Slf4j
public class RuleFetcher {

  private final OkHttpClient client;
  private final ObjectMapper objectMapper;

  public RuleFetcher() {
    this.client = RestClientUtils.getRestClient();
    this.objectMapper = new ObjectMapper();
  }

  public List<RuleDefinition> fetchAllRules() {
    List<RuleDefinition> ruleDefinitionList = Lists.newArrayList();

    try {
      Request request = RestClientUtils
          .buildRequest(Constants.REST_SERVER + Constants.API_RULE_LIST_PUBLISHED);
      Response response = client.newCall(request).execute();
      ruleDefinitionList = objectMapper
          .reader()
          .forType(new TypeReference<List<RuleDefinition>>() {
          })
          .readValue(response.body().string());
      log.info("Fetching rules at {}", LocalDateTime.now());
      log.info("Rules = " + ruleDefinitionList);
    } catch (IOException e) {
      log.error("fetch all published rule failed", e);
    }

    return ruleDefinitionList;
  }

  public RuleDefinition fetchRuleById(String id) {
    RuleDefinition ruleDefinition = null;
    try {
      Request request = RestClientUtils
          .buildRequest(Constants.REST_SERVER + Constants.API_SPECIFIED_RULE_PREFIX + id);
      Response response = client.newCall(request).execute();
      String responseBody = response.body().string();
      ruleDefinition = objectMapper.readValue(responseBody, RuleDefinition.class);
      log.info("Fetching rule {} at {}", id, LocalDateTime.now());
      log.info("Rule = " + ruleDefinition);
    } catch (IOException e) {
      log.error("fetch specified rule failed", e);
    }

    return ruleDefinition;
  }
}
