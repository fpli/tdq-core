package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RawSojEventWrapper {
  private String guid;
  private int pageId;
  private byte[] payload;
}
