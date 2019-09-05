package com.ebay.sojourner.ubd.common.model;

import lombok.Data;
import org.apache.flink.api.java.typeutils.TypeExtractor;

import java.io.Serializable;

@Data
public class AttributeAccumulator<T extends Attribute> implements Serializable{
    private T attribute;
    private UbiSession ubiSession;

    public AttributeAccumulator()
    {

        this.ubiSession = new UbiSession();

    }

}