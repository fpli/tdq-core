package com.ebay.tdq.svc;

import com.ebay.tdq.dto.QueryProfilerParam;
import com.ebay.tdq.dto.QueryProfilerResult;
import com.ebay.tdq.service.ProfilerService;

/**
 * @author juntzhang
 */
public class ProfilerServiceImpl implements ProfilerService {
  public static ProfilerService instance = new ProfilerServiceImpl();

  @Override
  public QueryProfilerResult query(QueryProfilerParam param) {
    return null;
  }
}
