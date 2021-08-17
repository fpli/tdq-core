package com.ebay.tdq.svc;

import com.ebay.tdq.dto.QueryDropdownParam;
import com.ebay.tdq.dto.QueryDropdownResult;
import com.ebay.tdq.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author juntzhang
 */
@Slf4j
public class ProfilerServiceDropdownTest {

  public HashMap<String, Object> getMap(String event_time, String pageId, String siteId, Double itm_valid_cnt,
      Double itm_cnt) throws ParseException {
    HashMap<String, Object> json = Maps.newHashMap();
    Map<String, Double> expr = Maps.newHashMap();
    Map<String, String> tags = Maps.newHashMap();

    expr.put("itm_cnt", itm_cnt);
    expr.put("itm_valid_cnt", itm_valid_cnt);

    tags.put("page_id", pageId);
    switch (pageId) {
      case "1450":
        tags.put("domain", "MYEBAY");//1450
        break;
      case "711":
        tags.put("domain", "ASQ");// 711
        break;
      case "1677718":
        tags.put("domain", "VI");// 1677718
        break;
      default:
        tags.put("domain", "TEST");
        break;
    }

    tags.put("site", siteId);

    long t = DateUtils.parseDateTime(event_time, ServiceFactory.getTdqEnv().getTimeZone());
    json.put("metric_key", "global_mandatory_tag_item_rate2");
    json.put("event_time", t);
    json.put("tags", tags);
    json.put("expr", expr);
    return json;
  }

  public void createData(Client client) throws Exception {
    val pattern = ServiceFactory.getIndexPattern();
    val index = pattern + "2021-05-29";
    PutIndexTemplateRequest request = new PutIndexTemplateRequest("tdq-metrics");
    request.patterns(Lists.newArrayList(pattern + "*"));
    String source = IOUtils.toString(this.getClass().getResourceAsStream("/tdq-metrics-template.json"));
    request.source(source, XContentType.JSON);
    client.admin().indices().putTemplate(request).get();

    client.index(Requests.indexRequest().index(index).source(getMap("2021-05-29 12:02:00", "1450", "1", 4d, 7d))).get();
    client.index(Requests.indexRequest().index(index).source(getMap("2021-05-29 12:02:00", "711", "1", 4d, 7d))).get();
    client.index(Requests.indexRequest().index(index).source(getMap("2021-05-29 12:04:00", "711", "1", 1d, 1d))).get();
    client.index(Requests.indexRequest().index(index).source(getMap("2021-05-29 12:04:00", "1677718", "1", 1d, 2d)))
        .get();
    client.index(Requests.indexRequest().index(index).source(getMap("2021-05-29 12:02:00", "711", "2", 0d, 0d))).get();
    Thread.sleep(3000);
  }

  @Test
  public void testDropdown() throws Exception {
    val elasticsearchResource = new EmbeddedElasticsearch();
    elasticsearchResource.start("es-test");
    createData(elasticsearchResource.getClient());
    QueryDropdownParam param = new QueryDropdownParam(
        RuleEngineServiceTest.get("global_mandatory_tag_item_rate2"),
        DateUtils.parseDateTime("2021-05-29 12:02:00", ServiceFactory.getTdqEnv().getTimeZone()),
        DateUtils.parseDateTime("2021-05-29 12:04:00", ServiceFactory.getTdqEnv().getTimeZone())
    );

    QueryDropdownResult result = ServiceFactory.getProfiler().dropdown(param);
    List<QueryDropdownResult.Record> records = result.getRecords();
    System.out.println(records);
    Assert.assertEquals(2, records.size());
    Assert.assertEquals(2, records.get(0).getItems().size());
    Assert.assertEquals(1, records.get(1).getItems().size());
    elasticsearchResource.close();
  }
}
