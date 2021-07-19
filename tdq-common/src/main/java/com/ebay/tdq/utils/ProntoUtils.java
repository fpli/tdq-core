package com.ebay.tdq.utils;

import com.ebay.sojourner.common.util.Constants;
import com.ebay.tdq.dto.TdqMtrcQryParam;
import com.ebay.tdq.dto.TdqQryParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaoding
 * @since 2021/7/14 1:34 AM
 */
@Deprecated
public class ProntoUtils {

  public static String INDEX_PREFIX = "tdq-metrics-pronto-";

  public static String[] calculateIndexes(TdqQryParam tdqQryParam) {
    return new String[]{INDEX_PREFIX + getMetricsKey(tdqQryParam)};
  }

  private static String getMetricsKey(TdqQryParam tdqQryParam) {
    String metricType = tdqQryParam.getMetricType();
    StringBuilder metricDesc = new StringBuilder();
    if (metricType != null) {
      metricDesc.append(metricType);
    }
    return StringUtils.lowerCase(metricDesc.append(Constants.METRIC_DEL)
        .append(tdqQryParam.getMetricName()).toString());
  }

  public static String constructDomain(TdqMtrcQryParam param) {
    if (StringUtils.isNotEmpty(param.getSiteId()) && StringUtils.isNotEmpty(param.getPageFmy())) {
      return param.getPageFmy() + Constants.FIELD_DELIM + param.getSiteId();
    }
    return "total";

  }

  public static String constructTags(TdqMtrcQryParam param) {
    if (CollectionUtils.isNotEmpty(param.getTags())) {
      StringBuilder sb = new StringBuilder();
      param.getTags().forEach((e) -> sb.append(e).append(Constants.METRIC_DEL));
      return sb.toString();
    }
    return "null";

  }
}
