package com.ebay.sojourner.ubd.common.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class AttributeAccumulator<T extends Attribute> implements Serializable{

    private static final long serialVersionUID = 5642072395614341391L;

    private T attribute;
    private UbiSession ubiSession;

    public AttributeAccumulator()
    {
        this.ubiSession = new UbiSession();
    }
}

