package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AgentAttribute implements Attribute,Serializable {
    private String clientIp;
    private int singleClickSessionCount;
    public void increase()
    {
        singleClickSessionCount++;
    }
}
