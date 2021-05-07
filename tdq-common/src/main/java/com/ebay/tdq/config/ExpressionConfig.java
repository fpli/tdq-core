package com.ebay.tdq.config;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressionConfig implements Serializable {
    private String              operator;
    private Map<String, Object> config;

    public static ExpressionConfig udf(String sql) {
        ExpressionConfig config = new ExpressionConfig();
        config.setOperator("UDF");
        config.setConfig(Maps.newHashMap());
        config.put("text",sql);
        return config;
    }

    public static ExpressionConfig operator(String operator) {
        ExpressionConfig config = new ExpressionConfig();
        config.setOperator(operator);
        config.setConfig(Maps.newHashMap());
        return config;
    }

    public static ExpressionConfig expr(String sql) {
        ExpressionConfig config = new ExpressionConfig();
        config.setOperator("Expr");
        config.setConfig(Maps.newHashMap());
        config.put("text", sql);
        return config;
    }

    public ExpressionConfig put(String k, Object v) {
        config.put(k, v);
        return this;
    }
}
