package com.ebay.sojourner.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "pageId")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageIdTopicMapping {
  private Long id;
  private Integer pageId;
  private List<String> topics;
  protected String createTime;
  protected String updateTime;
}
