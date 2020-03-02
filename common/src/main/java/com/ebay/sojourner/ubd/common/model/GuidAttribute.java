package com.ebay.sojourner.ubd.common.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;
import lombok.Getter;

@Data
public class GuidAttribute implements Attribute<UbiSession>, Serializable {

  private String guid;
  private Set<Integer> botFlagList = new LinkedHashSet<>();
  @Getter private int absEventCount = 0;

  public void feed(UbiSession ubiSession, int botFlag, boolean isNeeded) {
    if (isNeeded) absEventCount += ubiSession.getAbsEventCnt();
  }

  public GuidAttribute() {}

  @Override
  public void revert(UbiSession ubiSession, int botFlag) {}

  public void clear() {
    guid = null;
    absEventCount = 0;
  }

  @Override
  public void clear(int botFlag) {
    guid = null;
    absEventCount = 0;
  }
}
