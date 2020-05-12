package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class Guid {
  private long guid1;
  private long guid2;

  @Override
  public int hashCode() {
    return 1;
  }

  //比较的是域名
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }
    final Guid agentHash = (Guid) obj;
    return agentHash.guid1==this.guid1&&agentHash.guid2==this.guid2;
  }
}
