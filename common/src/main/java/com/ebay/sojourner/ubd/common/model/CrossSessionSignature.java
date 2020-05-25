package com.ebay.sojourner.ubd.common.model;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Data
public class CrossSessionSignature implements Serializable {

  private String signatureId;
  private Boolean isGenerate;
  private Long expirationTime;
  private Set<Integer> botFlags;
}
