package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class GuidAttributeAccumulator {
     private GuidAttribute guidAttribute;
     private UbiSession ubiSession;

     public GuidAttributeAccumulator(){
         this.guidAttribute = new GuidAttribute();
         this.ubiSession = new UbiSession();
     }
}
