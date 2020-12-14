package com.ebay.sojourner.dsl.domain;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.RestClient;
import com.ebay.sojourner.dsl.domain.rule.RuleDefinition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

@Slf4j
public final class RuleFetcher {

  private final RestClient client = new RestClient(EnvironmentUtils.get(Property.REST_BASE_URL));
  private final ObjectMapper objectMapper = new ObjectMapper();
  private static final String FETCH_ALL_RULES_URL = "/api/rule/list/published";

  public List<RuleDefinition> fetchAllRules() throws Exception {
    log.info("Fetching rules, url: {}", FETCH_ALL_RULES_URL);
    Response response = client.get(FETCH_ALL_RULES_URL);
    List<RuleDefinition> ruleDefinitionList = objectMapper
        .reader()
        .forType(new TypeReference<List<RuleDefinition>>() {})
        .readValue(response.body().string());
    log.info("Fetched rule count: {}", ruleDefinitionList.size());
    return ruleDefinitionList;
  }

}
