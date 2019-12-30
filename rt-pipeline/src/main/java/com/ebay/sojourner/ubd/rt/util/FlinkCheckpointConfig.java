package com.ebay.sojourner.ubd.rt.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.Duration;

@Data
public class FlinkCheckpointConfig {
    @JsonProperty("data-dir")
    private String dataDir;

    @JsonDeserialize(using = CustomDurationDeserializer.class)
    private Duration interval;

    @JsonDeserialize(using = CustomDurationDeserializer.class)
    private Duration timeout;

    @JsonProperty("min-pause-between")
    @JsonDeserialize(using = CustomDurationDeserializer.class)
    private Duration minPauseBetween;

    @JsonProperty("max-concurrent")
    private Integer maxConcurrent;
}
