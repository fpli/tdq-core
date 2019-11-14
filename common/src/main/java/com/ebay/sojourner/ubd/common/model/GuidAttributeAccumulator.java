package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class GuidAttributeAccumulator  {
    private GuidAttribute attribute;


    public GuidAttributeAccumulator() {
        this.attribute= new GuidAttribute();
    }
}
