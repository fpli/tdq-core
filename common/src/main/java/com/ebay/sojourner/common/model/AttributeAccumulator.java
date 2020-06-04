package com.ebay.sojourner.common.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class AttributeAccumulator<T extends Attribute> implements Serializable {

  private static final long serialVersionUID = 5642072395614341391L;

  private T attribute;
  private UbiSession ubiSession;

  public AttributeAccumulator() {
    this.ubiSession = new UbiSession();
  }
}