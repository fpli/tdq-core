package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentSignature;
import com.ebay.sojourner.ubd.common.model.IpSignature;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

import java.util.HashMap;
import java.util.Map;

public class AgentIpSignatureCoMapFunction implements CoMapFunction<AgentSignature, IpSignature, Map<String,Object>> {
    Map<String,Object> agentAndIpSignature = new HashMap<>();
    @Override
    public Map<String, Object> map1(AgentSignature agentSignature) throws Exception {
        agentAndIpSignature.put("agentSignature",agentSignature);
        return agentAndIpSignature;
    }

    @Override
    public Map<String, Object> map2(IpSignature ipSignature) throws Exception {
        agentAndIpSignature.put("ipSignature",ipSignature);
        return agentAndIpSignature;
    }
}
