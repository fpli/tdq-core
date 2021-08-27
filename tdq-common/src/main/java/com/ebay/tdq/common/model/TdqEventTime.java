package com.ebay.tdq.common.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.avro.Schema;

/**
 * @author juntzhang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TdqEventTime implements Serializable {

  private String expression;
  private Schema.Type type;
}
