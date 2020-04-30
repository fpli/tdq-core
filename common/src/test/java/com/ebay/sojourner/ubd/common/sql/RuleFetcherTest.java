package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.util.Constants;
import com.ebay.sojourner.ubd.common.util.RestApiUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RuleFetcherTest {

  private OkHttpClient client;
  private Request request;
  private Response response;
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    client = new OkHttpClient();
  }

  @Test
  public void test_fetchRules() throws Exception {
    request = RestApiUtils.buildRequest(
        Constants.REST_SERVER + Constants.API_RULE_LIST_PUBLISHED);
    response = client.newCall(request).execute();
    objectMapper = new ObjectMapper();
    List<RuleDefinition> responseBodyContent = objectMapper
        .reader()
        .forType(new TypeReference<List<RuleDefinition>>() {
        })
        .readValue(response.body().string());

    Assert.assertEquals(1, responseBodyContent.size());
  }

  @Test
  public void test_fetchRuleById() throws Exception {
    request = RestApiUtils.buildRequest(
        Constants.REST_SERVER + Constants.API_SPECIFIED_RULE_PREFIX + 1000);
    response = client.newCall(request).execute();
    objectMapper = new ObjectMapper();
    RuleDefinition responseBodyContent = objectMapper
        .readValue(response.body().string(), RuleDefinition.class);
    System.out.println("rule=" + responseBodyContent);

    Assert.assertEquals(1000, responseBodyContent.getId());
  }

}
