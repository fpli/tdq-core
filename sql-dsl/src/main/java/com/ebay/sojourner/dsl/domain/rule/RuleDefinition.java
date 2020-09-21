package com.ebay.sojourner.dsl.domain.rule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = {"bizId", "version"})
public class RuleDefinition implements Serializable {

  private long id;
  private long bizId;
  private String category;
  private String name;
  private String content;
  private String description;
  private int version;
  private String status;
  private String createdBy;
  private String updatedBy;
  private String createTime;
  private String updateTime;
  private Boolean isLatest;
}
