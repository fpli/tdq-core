package com.ebay.tdq.common.env;

import com.ebay.tdq.config.ExpressionConfig;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.config.TransformationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author juntzhang
 */
public class TdqConfigTest {

  public static ProfilerConfig createProfilerExample1() {
    return ProfilerConfig.builder()
        .metricName("global_mandatory_tag_item_rate1")
        .comment("Global Mandatory Tag - Item Rate")
        .expression(ExpressionConfig.expr("itm_valid_cnt / itm_no_missing_cnt"))
        .transformation(TransformationConfig.builder()
            .alias("page_family")
            .expression(ExpressionConfig.udf(" SOJ_PAGE_FAMILY(__TDQ_EVENT)"))
            .build()
        )
        .transformation(TransformationConfig.builder()
            .alias("site_id").expression(ExpressionConfig.udf("SITE_ID(__TDQ_EVENT)"))
            .build()
        )
        .transformation(TransformationConfig.builder()
            .alias("item")
            .expression(ExpressionConfig.udf(" SOJ_NVL(__TDQ_EVENT, 'itm|itmid|itm_id|itmlist|litm')"))
            .build()
        )
        .transformation(TransformationConfig.builder()
            .alias("itm_no_missing_cnt")
            .expression(ExpressionConfig.operator("Count"))
            .filter("length(item) > 0")
            .build()
        )
        .transformation(TransformationConfig.builder()
            .alias("itm_valid_cnt")
            .expression(ExpressionConfig.operator("Count"))
            .filter("LENGTH( REGEXP_EXTRACT(item, '^(\\d+(%2C)?)+$', 1) ) > 0")
            .build()
        )
        .dimension("page_family")
        .dimension("site_id")
        .build();
  }

  public static ProfilerConfig createProfilerExample2() {
    return ProfilerConfig.builder()
        .metricName("global_mandatory_tag_item_rate2")
        .comment("Global Mandatory Tag - Item Rate")
        .expression(ExpressionConfig.expr("itm_valid_cnt / itm_cnt"))
        .transformation(TransformationConfig.builder().alias("page_family")
            .expression(ExpressionConfig.udf(" SOJ_PAGE_FAMILY(__TDQ_EVENT)"))
            .build()
        )
        .transformation(TransformationConfig.builder()
            .alias("item")
            .expression(ExpressionConfig.udf(" SOJ_NVL(__TDQ_EVENT, 'itm|itmid|itm_id|itmlist|litm')"))
            .build()
        )
        .transformation(TransformationConfig.builder()
            .alias("itm_valid_ind")
            .expression(ExpressionConfig.expr(
                "case when LENGTH( REGEXP_EXTRACT(item, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1 else 0 end"))
            .build()
        )
        .transformation(TransformationConfig.builder()
            .alias("itm_cnt")
            .expression(ExpressionConfig.operator("Count"))
            .build()
        )
        .transformation(TransformationConfig.builder().alias("itm_valid_cnt")
            .expression(ExpressionConfig.operator("Sum").put("arg0", "itm_valid_ind"))
            .build()
        )
        .dimension("page_family")
        .build();
  }

  public static ProfilerConfig createProfilerExample3() {
    return ProfilerConfig.builder()
        .metricName("event_capture_publish_latency")
        .comment("Event Capture Publish Latency")
        .expression(ExpressionConfig.expr("t_duration_sum"))
        .transformation(TransformationConfig.builder()
            .alias("page_id")
            .expression(ExpressionConfig.udf(" SOJ_NVL(__TDQ_EVENT, 'p')"))
            .build()
        )
        .transformation(TransformationConfig.builder()
            .alias("t_duration")
            .expression(ExpressionConfig.expr("CAST( SOJ_NVL(__TDQ_EVENT, 'TDuration') AS DOUBLE)"))
            .build()
        )
        .transformation(TransformationConfig.builder()
            .alias("t_duration_sum")
            .expression(ExpressionConfig.operator("SUM").put("arg0", "t_duration"))
            .build()
        )
        .filter("page_id in ('1702898', '1677718') and CAST(clientData.contentLength AS INTEGER) > 30")//
        // todo if page_id is int how to
        // deal with string set?
        .dimension("page_id")
        .build();
  }

  public static void main(String[] args) throws Exception {
    TdqConfig config = TdqConfig.builder()
        .id("1")
        .rule(RuleConfig.builder()
            .profiler(createProfilerExample1())
            .profiler(createProfilerExample2())
            .profiler(createProfilerExample3())
            .build()
        )
        .build();
    ObjectMapper objectMapper = new ObjectMapper();
    System.out.println(objectMapper.writeValueAsString(config));
  }
}
