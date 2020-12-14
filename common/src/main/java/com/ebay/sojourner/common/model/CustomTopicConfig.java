package com.ebay.sojourner.common.model;

import java.util.List;
import lombok.Data;

@Data
public class CustomTopicConfig {
  private List<PageIdTopicMapping> mappings;
}
