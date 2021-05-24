package com.ebay.tdq.svc;

import com.ebay.tdq.dto.QueryProfilerParam;
import com.ebay.tdq.dto.QueryProfilerResult;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author juntzhang
 */
@Slf4j
public class ProfilerServiceTest {

  public HashMap<String, Object> getMap(String event_time, String pageId, Double itm_valid_cnt, Double itm_cnt) throws ParseException {
    HashMap<String, Object> json = Maps.newHashMap();
    Map<String, Double> expr = Maps.newHashMap();
    Map<String, String> tags = Maps.newHashMap();

    expr.put("itm_cnt", itm_cnt);
    expr.put("itm_valid_cnt", itm_valid_cnt);

    tags.put("page_id", pageId);

    json.put("metric_key", "global_mandatory_tag_item_rate1");
    json.put("event_time", DateUtils.parseDate(event_time, new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime());
    json.put("tags", tags);
    json.put("expr", expr);
    return json;
  }

  public void createData(Client client) throws Exception {
    val index = ServiceFactory.INDEX_PREFIX + "2021.05.28";
    val latencyIndex = ServiceFactory.LATENCY_INDEX_PREFIX + "2021.05.28";
    client.index(Requests.indexRequest().index(index).source(getMap("2021-05-29 12:02:00", "711", 4d, 7d))).get();
    client.index(Requests.indexRequest().index(index).source(getMap("2021-05-29 12:04:00", "711", 1d, 1d))).get();
    client.index(Requests.indexRequest().index(index).source(getMap("2021-05-29 12:04:00", "1677718", 1d, 2d))).get();
    client.index(Requests.indexRequest().index(latencyIndex).source(getMap("2021-05-29 12:02:00", "711", 1d, 2d))).get();
    Thread.sleep(3000);
  }

  @Test
  public void testQuery() throws Exception {
    val elasticsearchResource = new EmbeddedElasticsearch();
    elasticsearchResource.start("es-test");
    createData(elasticsearchResource.getClient());

    Map<String, Set<String>> dimensions = new HashMap<>();
    dimensions.put("page_id", Sets.newHashSet("711", "1677718"));
    QueryProfilerParam param = new QueryProfilerParam(
        RuleEngineServiceTest.get("global_mandatory_tag_item_rate1"),
        DateUtils.parseDate("2021-05-29 12:02:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime(),
        DateUtils.parseDate("2021-05-29 12:04:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime(),
        dimensions
    );

    QueryProfilerResult result = ServiceFactory.getProfiler().query(param);
    Assert.assertEquals(2, result.getRecords().size());
    Map<Long, Double> m =
        result.getRecords().stream().collect(Collectors.toMap(QueryProfilerResult.Record::getTimestamp,
            QueryProfilerResult.Record::getValue));
    double a1 = m.get(DateUtils.parseDate("2021-05-29 12:02:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime());
    double a2 = m.get(DateUtils.parseDate("2021-05-29 12:04:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime());
    Assert.assertEquals(5d / 9d, a1, 0.0001);
    Assert.assertEquals(2d / 3d, a2, 0.0001);
    //Thread.sleep(10000000);

    ServiceFactory.close();
    elasticsearchResource.close();
  }
}
