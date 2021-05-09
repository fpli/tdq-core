package com.ebay.tdq.svc;

import com.ebay.tdq.dto.TdqResult;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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
}
