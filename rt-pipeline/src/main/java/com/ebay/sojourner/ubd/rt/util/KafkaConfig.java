package com.ebay.sojourner.ubd.rt.util;

import lombok.Data;

import java.util.List;

@Data
public class KafkaConfig {
    private List<String> bootstrapServers;
    private String groupId;
    private String topic;
}
