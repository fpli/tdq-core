package com.ebay.tdq.config;

import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntzhang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvConfig implements Serializable {

  private Map<String, Object> config;
}
