package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class IpAttributeAccumulator {
    private IpAttribute ipAttribute;
    private UbiSession ubiSession;

    public IpAttributeAccumulator(){
        this.ipAttribute = new IpAttribute();
        this.ubiSession = new UbiSession();
    }
}
