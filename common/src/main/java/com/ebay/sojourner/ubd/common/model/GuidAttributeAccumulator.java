package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class GuidAttributeAccumulator {
     private GuidAttribute guidAttribute;


     public GuidAttributeAccumulator(){
         this.guidAttribute = new GuidAttribute();

     }
}
