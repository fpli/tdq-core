package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class GuidAttributeAccumulator {
     private GuidAttribute guidAttribute;
    private HashMap<Integer,Integer> botFlagStatus = new HashMap<>();


     public GuidAttributeAccumulator(){
         this.guidAttribute = new GuidAttribute();
         botFlagStatus.put(15,0);
     }
}
