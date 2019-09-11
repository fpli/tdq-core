package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class IpAttributeAccumulator {
    private IpAttribute attribute;
    private UbiSession ubiSession;


    public IpAttributeAccumulator() {
        this.ubiSession = new UbiSession();
        this.attribute= new IpAttribute();
    }

}
