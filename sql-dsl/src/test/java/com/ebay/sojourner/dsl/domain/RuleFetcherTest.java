package com.ebay.sojourner.dsl.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.RestClient;
import com.ebay.sojourner.dsl.domain.rule.RuleDefinition;
import java.util.List;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EnvironmentUtils.class, RuleFetcher.class})
public class RuleFetcherTest {

  RuleFetcher ruleFetcher;
  RestClient mockRestClient = mock(RestClient.class);
  Response mockResponse = mock(Response.class);
  ResponseBody mockResponseBody = mock(ResponseBody.class);
  String json = "[\n" +
      "    {\n" +
      "        \"id\": 1019,\n" +
      "        \"createdBy\": null,\n" +
      "        \"updatedBy\": null,\n" +
      "        \"createTime\": \"2020-08-19T08:04:11\",\n" +
      "        \"updateTime\": \"2020-08-24T06:40:41\",\n" +
      "        \"bizId\": 1,\n" +
      "        \"name\": \"BOT_RULE_1_CRAWLER_BOT\",\n" +
      "        \"content\": \"SELECT 1 as bot FROM soj.idl_session WHERE agentInfo NOT LIKE '%CUBOT%' AND agentInfo SIMILAR TO '.*bot[^a-z0-9\\\\_-].*|.*bot|.*spider.*|.*crawl.*|.*ktxn.*' ESCAPE '\\\\'\",\n" +
      "        \"description\": \"Flagged based on User Agent containing \\\"spider\\\", \\\"crawl\\\", \\\"bot\\\", etc regular expressions.\",\n" +
      "        \"version\": 2,\n" +
      "        \"isLatest\": true,\n" +
      "        \"status\": \"PUBLISHED\",\n" +
      "        \"category\": \"SESSION\"\n" +
      "    },\n" +
      "    {\n" +
      "        \"id\": 1020,\n" +
      "        \"createdBy\": null,\n" +
      "        \"updatedBy\": null,\n" +
      "        \"createTime\": \"2020-08-19T08:04:33\",\n" +
      "        \"updateTime\": \"2020-08-24T06:40:41\",\n" +
      "        \"bizId\": 9,\n" +
      "        \"name\": \"BOT_RULE_9_MANY_SEARCH_VIEW_BOT\",\n" +
      "        \"content\": \"SELECT 9 as bot FROM soj.idl_session WHERE (searchCnt > 400 AND viewCnt > 0) OR (viewCnt > 400 AND searchCnt > 0)\",\n" +
      "        \"description\": \"400+ VI or searches, with no Bid/BIN/Watch\",\n" +
      "        \"version\": 2,\n" +
      "        \"isLatest\": true,\n" +
      "        \"status\": \"PUBLISHED\",\n" +
      "        \"category\": \"SESSION\"\n" +
      "    },\n" +
      "    {\n" +
      "        \"id\": 1021,\n" +
      "        \"createdBy\": null,\n" +
      "        \"updatedBy\": null,\n" +
      "        \"createTime\": \"2020-08-19T08:04:51\",\n" +
      "        \"updateTime\": \"2020-08-24T06:40:41\",\n" +
      "        \"bizId\": 15,\n" +
      "        \"name\": \"BOT_RULE_15_MANY_EVENTS_BOT\",\n" +
      "        \"content\": \"SELECT 15 as bot FROM soj.idl_session WHERE absEventCnt >= 10000\",\n" +
      "        \"description\": \"Sessions having more than 10,000 events which includes iframe and direct events.\",\n" +
      "        \"version\": 3,\n" +
      "        \"isLatest\": true,\n" +
      "        \"status\": \"PUBLISHED\",\n" +
      "        \"category\": \"SESSION\"\n" +
      "    }\n" +
      "]";

  @Before
  public void setUp() throws Exception {
    mockStatic(EnvironmentUtils.class);
    when(EnvironmentUtils.get(Property.REST_BASE_URL)).thenAnswer(invocation -> "http://localhost");
    whenNew(RestClient.class).withAnyArguments().thenReturn(mockRestClient);
    when(mockRestClient.get("/api/rule/list/published")).thenReturn(mockResponse);
    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(json);

    ruleFetcher = new RuleFetcher();
  }

  @Test
  public void fetchAllRules() throws Exception {
    List<RuleDefinition> result = ruleFetcher.fetchAllRules();
    assertThat(result.size()).isEqualTo(3);
  }
}