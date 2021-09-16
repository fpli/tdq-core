package com.ebay.tdq.svc;

import com.ebay.tdq.dto.QueryDropdownParam;
import com.ebay.tdq.dto.QueryDropdownResult;
import com.ebay.tdq.dto.QueryProfilerParam;
import com.ebay.tdq.dto.QueryProfilerResult;
import com.ebay.tdq.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
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
public class ProfilerServiceQueryProfilersTest {

  TimeZone zone = ServiceFactory.getTdqEnv().getTimeZone();

  public HashMap<String, Object> getMap(
      String metric_key,
      String event_time, String pageId, String siteId, Double itm_valid_cnt,
      Double itm_cnt) throws ParseException {
    HashMap<String, Object> json = Maps.newHashMap();
    Map<String, Double> expr = Maps.newHashMap();
    Map<String, String> tags = Maps.newHashMap();

    expr.put("itm_cnt", itm_cnt);
    expr.put("itm_valid_cnt", itm_valid_cnt);
    if (metric_key.equals("global_mandatory_tag_item_rate1")) {
      tags.put("page_id", pageId);
    } else {
      tags.put("page", pageId);
    }
    tags.put("site", siteId);

    long t = DateUtils.parseDateTime(event_time, zone);
    json.put("metric_key", metric_key);
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

    client.index(Requests.indexRequest().index(index)
        .source(getMap("global_mandatory_tag_item_rate1", "2021-05-29 12:02:00", "711", "1", 4d, 7d))).get();
    client.index(Requests.indexRequest().index(index)
        .source(getMap("global_mandatory_tag_item_rate1", "2021-05-29 12:02:00", "711", "1", 1d, 2d))).get();
    client.index(Requests.indexRequest().index(index)
        .source(getMap("global_mandatory_tag_item_rate1", "2021-05-29 12:04:00", "711", "2", 1d, 1d))).get();
    client.index(Requests.indexRequest().index(index)
        .source(getMap("global_mandatory_tag_item_rate1", "2021-05-29 12:04:00", "1677718", "1", 1d, 2d))).get();

    client.index(Requests.indexRequest().index(index)
        .source(getMap("global_mandatory_tag_item_rate2", "2021-05-29 12:02:00", "711", "1", 1d, 2d))).get();
    client.index(Requests.indexRequest().index(index)
        .source(getMap("global_mandatory_tag_item_rate2", "2021-05-29 12:04:00", "1677718", "2", 1d, 1d))).get();
    client.index(Requests.indexRequest().index(index)
        .source(getMap("global_mandatory_tag_item_rate2", "2021-05-29 12:04:00", "1677718", "1", 1d, 2d))).get();

    Thread.sleep(3000);
  }

  @Test
  public void testQuery() throws Exception {
    val elasticsearchResource = new EmbeddedElasticsearch();
    elasticsearchResource.start("es-test");
    createData(elasticsearchResource.getClient());

    Map<String, Set<String>> dimensions = new HashMap<>();
    dimensions.put("global_mandatory_tag_item_rate1.page_id", Sets.newHashSet("711", "1677718"));
    dimensions.put("global_mandatory_tag_item_rate2.page", Sets.newHashSet("711", "1677718"));
    QueryProfilerParam param = new QueryProfilerParam(
        RuleEngineServiceTest.get("global_mandatory_tag_item_rate3"),
        DateUtils
            .parseDateTime("2021-05-29 12:02:00", zone),
        DateUtils
            .parseDateTime("2021-05-29 12:04:00", zone),
        dimensions
    );

    QueryProfilerResult result = ServiceFactory.getProfiler().query(param);
    Assert.assertEquals(2, result.getRecords().size());
    Map<Long, Double> m =
        result.getRecords().stream().collect(Collectors.toMap(QueryProfilerResult.Record::getTimestamp,
            QueryProfilerResult.Record::getValue));
    double a1 = m.get(DateUtils.parseDateTime("2021-05-29 12:02:00", zone));
    double a2 = m.get(DateUtils.parseDateTime("2021-05-29 12:04:00", zone));
    Assert.assertEquals(5d - 1d, a1, 0.0001);
    Assert.assertEquals(2d - 2d, a2, 0.0001);

    Map<Long, Double> detail1 = result.getDetails().get("1").stream().collect(Collectors.toMap(QueryProfilerResult.Record::getTimestamp,
        QueryProfilerResult.Record::getValue));
    Assert.assertEquals(5d, detail1.get(DateUtils.parseDateTime("2021-05-29 12:02:00", zone)), 0.0001);
    Assert.assertEquals(2d, detail1.get(DateUtils.parseDateTime("2021-05-29 12:04:00", zone)), 0.0001);

    Map<Long, Double> detail2 = result.getDetails().get("2").stream().collect(Collectors.toMap(QueryProfilerResult.Record::getTimestamp,
        QueryProfilerResult.Record::getValue));
    Assert.assertEquals(1d, detail2.get(DateUtils.parseDateTime("2021-05-29 12:02:00", zone)), 0.0001);
    Assert.assertEquals(2d, detail2.get(DateUtils.parseDateTime("2021-05-29 12:04:00", zone)), 0.0001);

    Map<Long, Double> detail3 = result.getDetails().get("3").stream().collect(Collectors.toMap(QueryProfilerResult.Record::getTimestamp,
        QueryProfilerResult.Record::getValue));
    Assert.assertEquals(5d / 1d, detail3.get(DateUtils.parseDateTime("2021-05-29 12:02:00", zone)), 0.0001);
    Assert.assertEquals(2d / 2d, detail3.get(DateUtils.parseDateTime("2021-05-29 12:04:00", zone)), 0.0001);

    dimensions = new HashMap<>();
    dimensions.put("global_mandatory_tag_item_rate1.page_id", Sets.newHashSet("711"));
    dimensions.put("global_mandatory_tag_item_rate2.page", Sets.newHashSet("1677718"));
    param = new QueryProfilerParam(
        RuleEngineServiceTest.get("global_mandatory_tag_item_rate3"),
        DateUtils
            .parseDateTime("2021-05-29 12:02:00", zone),
        DateUtils
            .parseDateTime("2021-05-29 12:04:00", zone),
        dimensions
    );

    result = ServiceFactory.getProfiler().query(param);
    Assert.assertEquals(2, result.getRecords().size());
    m =
        result.getRecords().stream().collect(Collectors.toMap(QueryProfilerResult.Record::getTimestamp,
            QueryProfilerResult.Record::getValue));
    a1 = m.get(DateUtils.parseDateTime("2021-05-29 12:02:00", zone));
    a2 = m.get(DateUtils.parseDateTime("2021-05-29 12:04:00", zone));
    Assert.assertEquals(5d - 0d, a1, 0.0001);
    Assert.assertEquals(1d - 2d, a2, 0.0001);

    createData(elasticsearchResource.getClient());
    QueryDropdownParam dropdownParam = new QueryDropdownParam(
        RuleEngineServiceTest.get("global_mandatory_tag_item_rate3"),
        DateUtils.parseDateTime("2021-05-29 12:02:00", ServiceFactory.getTdqEnv().getTimeZone()),
        DateUtils.parseDateTime("2021-05-29 12:04:00", ServiceFactory.getTdqEnv().getTimeZone())
    );
    QueryDropdownResult dropdown = ServiceFactory.getProfiler().dropdown(dropdownParam);
    System.out.println(dropdown);
    Assert.assertEquals(2, dropdown.getRecords().size());

    elasticsearchResource.close();


  }
}
