package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.RestClientUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class RuleFetcher {

  private final OkHttpClient client;
  private final ObjectMapper objectMapper;
  private static final String FETCH_ALL_RULES_URL =
      EnvironmentUtils.get(Property.REST_SERVER) + "/api/rule/list/published";
  private static final String FETCH_RULE_URL_PREFIX =
      EnvironmentUtils.get(Property.REST_SERVER) + "/api/rule/";

  public RuleFetcher() {
    this.client = RestClientUtils.getRestClient();
    this.objectMapper = new ObjectMapper();
  }

  public List<RuleDefinition> fetchAllRules() {
    List<RuleDefinition> ruleDefinitionList = Lists.newArrayList();

    try {
      Request request = RestClientUtils
          .buildRequest(FETCH_ALL_RULES_URL);
      log.info("Fetching rules, url: {}", FETCH_ALL_RULES_URL);
      Response response = client.newCall(request).execute();
      ruleDefinitionList = objectMapper
          .reader()
          .forType(new TypeReference<List<RuleDefinition>>() {
          })
          .readValue(response.body().string());
      log.info("Fetched rule count: {}", ruleDefinitionList.size());
    } catch (IOException e) {
      log.error("Failed to fetch published rules.", e);
    }

    return ruleDefinitionList;
  }

}
