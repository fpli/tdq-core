package com.ebay.sojourner.common.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "pageId")
@AllArgsConstructor
@Data
public class PageIdTopicMapping {
  private Integer pageId;
  private List<String> topics;
}
