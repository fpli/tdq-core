package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PulsarEvents {

  private Long eventCreateTimestamp;
  private String content;
}
