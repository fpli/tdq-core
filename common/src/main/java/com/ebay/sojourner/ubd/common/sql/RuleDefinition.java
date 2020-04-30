package com.ebay.sojourner.ubd.common.sql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleDefinition {

  private long id;
  private long bizId;
  private String category;
  private String name;
  private String content;
  private String description;
  private int version;
  private String status;
  protected String createdBy;
  protected String updatedBy;
  protected String createTime;
  protected String updateTime;
  private Boolean isActive;
}
