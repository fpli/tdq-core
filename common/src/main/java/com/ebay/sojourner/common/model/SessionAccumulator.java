package com.ebay.sojourner.common.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionAccumulator implements Serializable {

  private UbiSession ubiSession;
  private UbiSession ubiSessionSplit;

  public SessionAccumulator() {
    this.ubiSession = new UbiSession();
    ubiSessionSplit=null;
  }
  public SessionAccumulator(UbiSession ubiSession) {
    this.ubiSession = ubiSession;
    ubiSessionSplit=null;
  }
}
