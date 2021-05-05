package com.ebay.tdq

import org.junit.Test

/**
 * @author juntzhang
 */
class ProntoSinkJobTest extends SingleRuleSqlJobTest {
  @Test
  def test_sum_by_page_id_sink2pronto(): Unit = {
    sum_by_page_id("test_sum_by_page_id_sink2pronto").submit2Pronto()
  }
}
