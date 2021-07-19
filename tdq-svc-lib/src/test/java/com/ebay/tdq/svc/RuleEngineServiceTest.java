package com.ebay.tdq.svc;

import com.ebay.tdq.dto.IDoMetricConfig;
import com.ebay.tdq.dto.TdqDataResult;
import com.ebay.tdq.dto.TdqResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author juntzhang
 */
@Slf4j
public class RuleEngineServiceTest {

  @Test
  public void testVerifyExpression() {
    TdqResult result = ServiceFactory.getRuleEngine()
        .verifyExpression("case when item is not null then 1 else 0 end");
    log.info("success=>{}", result);

    Assert.assertTrue(result.isOk());

    result = ServiceFactory.getRuleEngine()
        .verifyExpression("case when item is not null then 1 else 0");
    log.info("failed=>{}", result);
    Assert.assertEquals(result.getCode(), TdqResult.Code.FAILED);
    Assert.assertNotNull(result.getException());
  }

  @Test
  public void testVerifyTdqConfigs() throws IOException {
    TdqResult result = ServiceFactory.getRuleEngine().verifyTdqConfig(get("tdq_rules"));
    log.info("SUCCESS=>{}", result);
    if(!result.isOk()){
      result.getException().printStackTrace();
    }
    Assert.assertTrue(result.isOk());
  }

  @Test
  public void testVerifyTdqConfig() throws IOException {
    TdqResult result = ServiceFactory.getRuleEngine().verifyTdqConfig(get("testVerifyTdqConfig_success"));
    log.info("SUCCESS=>{}", result);
    Assert.assertTrue(result.isOk());

    result = ServiceFactory.getRuleEngine().verifyTdqConfig(get("testVerifyTdqConfig_syntax_illegal"));
    log.info("FAILED=>{}", result);
    Assert.assertNotNull(result.getException());

    result = ServiceFactory.getRuleEngine().verifyTdqConfig(get("testVerifyTdqConfig_process_failed"));
    log.info("FAILED=>{}", result);
    Assert.assertNotNull(result.getException());
  }

  public static String get(String name) throws IOException {
    return IOUtils.toString(RuleEngineServiceTest.class.getResourceAsStream("/" + name + ".json"));
  }

  @Test
  public void testTranslateConfig() {
    IDoMetricConfig metricConfig = new IDoMetricConfig();
    metricConfig.setMetricName("test");
    metricConfig.setWindow("2min");
    metricConfig.setOperator("avg");
    metricConfig.setExpressions(Lists.newArrayList("case when LENGTH( REGEXP_EXTRACT( SOJ_NVL('itm|itmid|itm_id|itmlist|litm'), '^(\\d+(%2C)?)+$', 1) ) > 0 then 1 else 0 end"));
    //    metricConfig.setExpressions(Lists.newArrayList("case when item is not null then 1 else 0 end"));
    //    metricConfig.setExpressions(Lists.newArrayList("client_data_tag.contentLength"));
    //    metricConfig.setDimensions(Lists.newArrayList("page_id"));
    //    metricConfig.setFilter("soj_tag.TDuration > 30.0 AND client_data_tag.contentLength > 100");
    metricConfig.setFilter(" SOJ_PAGE_FAMILY(CAST( SOJ_NVL('p') AS INTEGER)) in ('BID','VI')");
    Map<String, IDoMetricConfig.FieldType> dataTypes = new HashMap<>();
    dataTypes.put("soj_tag.TDuration", IDoMetricConfig.FieldType.DOUBLE);
    dataTypes.put("client_data_tag.contentLength", IDoMetricConfig.FieldType.INTEGER);
    metricConfig.setDataTypes(dataTypes);
    TdqDataResult<String> result = ServiceFactory.getRuleEngine().translateConfig(
        metricConfig
    );
    String json = result.getData();
    System.out.println(json);
    TdqResult tdqResult = ServiceFactory.getRuleEngine().verifyTdqConfig(json);
    System.out.println(tdqResult);
    Assert.assertTrue(result.isOk());
  }

  @Test
  public void testTranslateDivideConfig() {
    IDoMetricConfig metricConfig = new IDoMetricConfig();
    metricConfig.setMetricName("test");
    metricConfig.setWindow("2min");
    metricConfig.setOperator("divide");
    metricConfig.setExpressions(Lists.newArrayList(
        "case when item is not null then 1 else 0 end",
        "case when client_data_tag.contentLength > 150 then 1 else 0 end"
    ));
    metricConfig.setDimensions(Lists.newArrayList("page_id"));
    metricConfig.setFilter("soj_tag.TDuration > 30.0 AND client_data_tag.contentLength > 100");
    Map<String, IDoMetricConfig.FieldType> dataTypes = new HashMap<>();
    dataTypes.put("soj_tag.TDuration", IDoMetricConfig.FieldType.DOUBLE);
    dataTypes.put("client_data_tag.contentLength", IDoMetricConfig.FieldType.INTEGER);
    metricConfig.setDataTypes(dataTypes);
    TdqDataResult<String> result = ServiceFactory.getRuleEngine().translateConfig(
        metricConfig
    );
    System.out.println(result.getData());

    TdqResult tdqResult = ServiceFactory.getRuleEngine().verifyTdqConfig(result.getData());
    System.out.println(tdqResult);
    Assert.assertTrue(result.isOk());
  }
}
