package com.ebay.sojourner.dsl.domain;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.sojourner.dsl.domain.rule.RuleDefinition;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.RestClientUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  public RuleFetcher() {
    this.client = RestClientUtils.getRestClient();
    this.objectMapper = new ObjectMapper();
  }

  public List<RuleDefinition> fetchAllRules() throws Exception {
    Request request = RestClientUtils
        .buildRequest(FETCH_ALL_RULES_URL);
    log.info("Fetching rules, url: {}", FETCH_ALL_RULES_URL);
    Response response = client.newCall(request).execute();
    List<RuleDefinition> ruleDefinitionList = objectMapper
        .reader()
        .forType(new TypeReference<List<RuleDefinition>>() {})
        .readValue(response.body().string());
    log.info("Fetched rule count: {}", ruleDefinitionList.size());
    return ruleDefinitionList;
  }

}
