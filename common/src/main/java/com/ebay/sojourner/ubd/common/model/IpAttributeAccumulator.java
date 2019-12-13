package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class IpAttributeAccumulator {
    private IpAttribute ipAttribute;


    public IpAttributeAccumulator(){
        this.ipAttribute = new IpAttribute();

    }
}
