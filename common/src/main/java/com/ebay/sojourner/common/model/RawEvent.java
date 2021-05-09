package com.ebay.sojourner.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RawEvent implements Serializable {

  private RheosHeader rheosHeader;
  private Map<String, String> sojA;
  private Map<String, String> sojK;
  private Map<String, String> sojC;
  private ClientData clientData;
  private long ingestTime;
  private Long eventTimestamp;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
