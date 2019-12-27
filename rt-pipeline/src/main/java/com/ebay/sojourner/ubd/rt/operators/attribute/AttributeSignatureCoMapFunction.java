package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpSignature;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

import java.util.HashMap;
import java.util.Map;

public class AttributeSignatureCoMapFunction implements CoMapFunction<AgentIpSignature, Map<String, Object>, Map<String,Object>> {
    Map<String,Object> attributeSignature = new HashMap<>();
    @Override
    public Map<String, Object> map1(AgentIpSignature agentIpSignature) throws Exception {
        attributeSignature.put("agentAndIp", agentIpSignature);
        return attributeSignature;
    }

    @Override
    public Map<String, Object> map2(Map<String, Object> map) throws Exception {
        attributeSignature.putAll(map);
        return attributeSignature;
    }
}
