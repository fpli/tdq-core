package com.ebay.sojourner.ubd.common.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;
import lombok.Getter;

@Data
public class GuidAttribute implements Attribute<SessionCore>, Serializable {

  private long guid1;
  private long guid2;
  private Set<Integer> botFlagList = new LinkedHashSet<>();
  @Getter
  private int absEventCount = 0;

  public GuidAttribute() {
  }

  public void feed(SessionCore intermediateSession, int botFlag, boolean isNeeded) {
    if (isNeeded) {
      absEventCount += intermediateSession.getAbsEventCnt();
    }
  }

  @Override
  public void revert(SessionCore intermediateSession, int botFlag) {
  }

  public void clear() {
    guid1 = 0;
    guid2 = 0;
    absEventCount = 0;
  }

  @Override
  public void clear(int botFlag) {
    guid1 = 0;
    guid2 = 0;
    absEventCount = 0;
  }
}
