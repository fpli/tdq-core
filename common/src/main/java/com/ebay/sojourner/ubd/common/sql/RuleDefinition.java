package com.ebay.sojourner.ubd.common.sql;

import lombok.Data;

@Data
public class RuleDefinition {
  private long id;
  private long ruleId;
  private String name;
  private String content;
  private String description;
  private int version;
  private String status;
  protected String createdBy;
  protected String updatedBy;
  protected String createTime;
  protected String updateTime;
}
