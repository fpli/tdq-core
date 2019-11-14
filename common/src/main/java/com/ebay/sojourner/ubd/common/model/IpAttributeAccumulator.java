package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class IpAttributeAccumulator {
    private IpAttribute attribute;


    public IpAttributeAccumulator() {
        this.attribute= new IpAttribute();
    }

}
