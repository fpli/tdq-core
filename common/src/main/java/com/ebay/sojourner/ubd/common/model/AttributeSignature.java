package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class AttributeSignature {
    private Map<String, Set<Integer>> attributeSignature = new HashMap<>();

    public AttributeSignature() {
    }
}
