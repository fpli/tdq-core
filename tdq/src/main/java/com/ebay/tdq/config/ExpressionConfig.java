package com.ebay.tdq.config;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ExpressionConfig implements Serializable {
    private String              operator;
    private Map<String, Object> config;

    public ExpressionConfig put(String k, Object v) {
        config.put(k, v);
        return this;
    }

    public static ExpressionConfig udf(String sql) {
        return ExpressionConfig.builder().operator("UDF").config(Maps.newHashMap()).build().put("text", sql);
    }

    public static ExpressionConfig operator(String operator) {
        return ExpressionConfig.builder().operator(operator).config(Maps.newHashMap()).build();
    }

    public static ExpressionConfig expr(String sql) {
        return ExpressionConfig.builder().operator("Expr").config(Maps.newHashMap()).build().put("text", sql);
    }
}
