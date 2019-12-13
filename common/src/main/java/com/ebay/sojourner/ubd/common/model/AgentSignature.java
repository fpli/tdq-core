package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class AgentSignature implements Signature, Serializable {

    private Map<String, Set<Integer>> agentBotSignature = new HashMap<>();

    public AgentSignature() {
    }
}
