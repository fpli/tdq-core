package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class GuidAttributeAccumulator  {
    private GuidAttribute attribute;
    private UbiSession ubiSession;


    public GuidAttributeAccumulator() {
        this.ubiSession = new UbiSession();
        this.attribute= new GuidAttribute();
    }
}
