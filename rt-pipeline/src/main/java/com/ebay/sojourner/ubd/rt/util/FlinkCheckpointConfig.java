package com.ebay.sojourner.ubd.rt.util;

import lombok.Data;

@Data
public class FlinkCheckpointConfig {
    private String dataDir;
    private Long interval;
    private Long timeout;
    private Long minPauseBetween;
    private Integer maxConcurrent;
}
