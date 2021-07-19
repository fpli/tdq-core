package com.ebay.tdq.svc;

import com.ebay.tdq.dto.*;
import com.ebay.tdq.utils.ProntoUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import java.text.ParseException;
import java.util.*;

/**
 * @author juntzhang
 */
@Slf4j
public class FetchMetricsMsMtcServiceTest  {
  EmbeddedElasticsearch elasticsearchResource =null;
  private HashMap<String, Object> getMap(String event_time, String page_family,String site_id,
                                         long tag_cnt, long total_cnt,String tag_name) throws ParseException {
    HashMap<String, Object> json = Maps.newHashMap();
    System.out.println(DateUtils.parseDate(event_time, new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime());
    json.put("metric_name", "Glabal_Mandotory_Tag_Rate");
    json.put("metric_type", "tag_miss_cnt");
    json.put("event_time", DateUtils.parseDate(event_time, new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime());
    json.put("tag_cnt", tag_cnt);
    json.put("total_cnt", total_cnt);
    json.put("site_id", site_id);
    json.put("page_family", page_family);
    json.put("tag_name", tag_name);
    json.put("event_time_fmt", DateUtils.parseDate(event_time, new String[]{"yyyy-MM-dd HH:mm:ss"}));
    json.put("process_time", DateUtils.parseDate(event_time, new String[]{"yyyy-MM-dd HH:mm:ss"}));
    return json;
  }

  @BeforeEach
  public void createData() throws Exception {
    elasticsearchResource = new EmbeddedElasticsearch();
    elasticsearchResource.start("es-test");
    Client client=elasticsearchResource.getClient();
    val index = ProntoUtils.INDEX_PREFIX + "tag_miss_cnt_glabal_mandotory_tag_rate";

    PutIndexTemplateRequest request = new PutIndexTemplateRequest("tdq-metrics-pronto");
    request.patterns(Lists.newArrayList(ProntoUtils.INDEX_PREFIX  + "*"));
    String source = IOUtils.toString(this.getClass().getResourceAsStream("/tdq-metrics-template2.json"));
    request.source(source, XContentType.JSON);
    client.admin().indices().putTemplate(request).get();

    client.index(Requests.indexRequest().index(index).source(
            getMap("2021-07-16 00:05:00", "UNWTCH", "77",
                    1,2,"u"
            ))).get();
    client.index(Requests.indexRequest().index(index).source(
            getMap("2021-07-16 00:05:00", "VI", "16",
                    947,948,"u"
            ))).get();
    client.index(Requests.indexRequest().index(index).source(
            getMap("2021-07-16 01:05:00", " VI", "16",
                    947,948,"u"))).get();
    client.index(Requests.indexRequest().index(index).source(
            getMap("2021-07-16 01:05:00", " VI", "16",
                    2,3,"u"))).get();
    Thread.sleep(3000);
  }

  @Test
  public void testQuery() throws Exception {
    val elasticsearchResource = new EmbeddedElasticsearch();
    elasticsearchResource.start("es-test");
    createData();
    Map<String, Set<String>> dimensions = new HashMap<>();
    dimensions.put("page_id", Sets.newHashSet("711", "1677718"));

    TdqMtrcQryParam param = TdqMtrcQryParam.builder().pageFmy("VI")
             .tagMetrics(Arrays.asList("tag_cnt","total_cnt")).tags( Arrays.asList("u"))
            .siteId("16").metricName("Glabal_Mandotory_Tag_Rate")
            .metricType("tag_miss_cnt").from(1626364800000L)
            .to(1626368400000L).precision(2)
            .aggMethod(AggMethod.SUM)
            .metricsMethod(MetricsMethod.PERCENTAGE)
            .build();

    TdqMtrcQryRs result = (TdqMtrcQryRs)ServiceFactory.getFetchMetricsService().fetchMetrics(param);
    System.out.println(result.getTagMetrics());
    Assert.assertEquals(1, result.getTagMetrics().size());
//    Map<Long, Double> m =
//        result.getRecords().stream().collect(Collectors.toMap(QueryProfilerResult.Record::getTimestamp,
//            QueryProfilerResult.Record::getValue));
//    double a1 = m.get(DateUtils.parseDate("2021-05-29 12:02:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime());
//    double a2 = m.get(DateUtils.parseDate("2021-05-29 12:04:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime());
//    Assert.assertEquals(5d / 9d, a1, 0.0001);
//    Assert.assertEquals(2d / 3d, a2, 0.0001);
  }

  @AfterAll
  public void close(){
    if(elasticsearchResource!=null) {
      try {
        ServiceFactory.close();
        elasticsearchResource.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
