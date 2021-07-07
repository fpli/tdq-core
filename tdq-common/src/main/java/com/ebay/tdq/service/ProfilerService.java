package com.ebay.tdq.service;

import com.ebay.tdq.dto.QueryDropdownParam;
import com.ebay.tdq.dto.QueryDropdownResult;
import com.ebay.tdq.dto.QueryProfilerParam;
import com.ebay.tdq.dto.QueryProfilerResult;

public interface ProfilerService {
  QueryProfilerResult query(QueryProfilerParam param);

  QueryDropdownResult dropdown(QueryDropdownParam param);
}
