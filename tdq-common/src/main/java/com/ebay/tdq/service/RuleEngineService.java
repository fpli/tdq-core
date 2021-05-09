package com.ebay.tdq.service;

import com.ebay.tdq.dto.IDoMetricConfig;
import com.ebay.tdq.dto.TdqDataResult;
import com.ebay.tdq.dto.TdqResult;

/**
 * @author juntzhang
 */
public interface RuleEngineService {
  TdqDataResult<String> translateConfig(IDoMetricConfig iDoMetricConfig);

  TdqResult verifyExpression(String code);

  TdqResult verifyTdqConfig(String tdqConfig);
}
