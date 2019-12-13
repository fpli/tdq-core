package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class AgentIpSignature implements Signature, Serializable {
    private Map<String, Set<Integer>> agentIpBotSignature = new HashMap<>();

    public AgentIpSignature() {
    }
}
