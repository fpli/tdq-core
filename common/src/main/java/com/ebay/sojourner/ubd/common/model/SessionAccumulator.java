package com.ebay.sojourner.ubd.common.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionAccumulator implements Serializable {

  private UbiSession ubiSession;

  public SessionAccumulator() {
    this.ubiSession = new UbiSession();
  }
}
