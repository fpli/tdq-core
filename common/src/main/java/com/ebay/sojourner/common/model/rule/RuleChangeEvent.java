package com.ebay.sojourner.common.model.rule;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class RuleChangeEvent {
  private Set<RuleDefinition> rules;
  private RuleChangeEventType type;
  private LocalDateTime localDateTime;
}
