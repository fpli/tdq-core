package com.ebay.sojourner.ubd.common.sql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = {"bizId", "version"})
public class RuleDefinition {

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
