package com.ebay.sojourner.ubd.common.model;

public class IpAttributeAccumulator {
    private IpAttribute ipAttribute;
    private UbiSession ubiSession;


    public IpAttributeAccumulator() {
        this.ubiSession = new UbiSession();
        this.ipAttribute= new IpAttribute();
    }

}
