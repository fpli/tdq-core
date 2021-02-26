package com.ebay.sojourner.tdq.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MetricType {
    TAGMISSCNT(1, "TAGMISSCNT"),
    TAGSUM(2, "TAGSUM"),
    PAGECNT(3, "PAGECNT"),
    TRANSFORMERROR(4, "TRANSFORMERROR");

    private final int id;
    private final String desc;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static MetricType of(int metricTypeId) {
        for (MetricType metricType : MetricType.values()) {
            if (metricType.getId() == metricTypeId) {
                return MetricType.valueOf(metricType.getDesc());
            }
        }
        return null;
    }

    public static MetricType of(String desc) {
        return MetricType.valueOf(desc.toUpperCase());
    }

}
