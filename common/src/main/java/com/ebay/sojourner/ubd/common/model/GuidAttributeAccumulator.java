package com.ebay.sojourner.ubd.common.model;

public class GuidAttributeAccumulator extends AttributeAccumulator<GuidAttribute> {
    public GuidAttributeAccumulator() {
        super();
        this.setAttribute(new GuidAttribute());
    }
}
