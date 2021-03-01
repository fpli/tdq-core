package com.ebay.sojourner.tdq.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum MetricType {
    TAG_MISS_CNT(1, "Tag miss count"),
    TAG_SUM(2, "Tag sum"),
    PAGE_CNT(3, "Page count"),
    TRANSFORM_ERROR(4, "Transform error");

    private final int id;
    private final String desc;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static MetricType of(int metricTypeId) {
        for (MetricType metricType : values()) {
            if (metricType.getId() == metricTypeId) {
                return MetricType.valueOf(metricType.getDesc());
            }
        }
        return null;
    }

    public static MetricType of(String metricType) {
        return MetricType.valueOf(metricType.toUpperCase());
    }

}
