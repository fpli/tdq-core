package com.ebay.sojourner.ubd.common.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class SessionForGuidEnhancement implements Serializable, Cloneable {

  private long guid1;
  private long guid2;
  private int absEventCnt;

}
