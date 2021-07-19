package com.ebay.tdq.common.env;

import static com.ebay.sojourner.common.env.EnvironmentUtils.get;

import java.io.Serializable;
import lombok.Data;

/**
 * @author juntzhang
 */
@Data
public class KafkaSourceEnv implements Serializable {

  private String startupMode;  // TIMESTAMP,LATEST,EARLIEST
  private Long fromTimestamp = 0L;

  public KafkaSourceEnv() {
    String fromTimestamp = get("flink.app.source.from-timestamp");
    if (fromTimestamp.equalsIgnoreCase("earliest")) {
      startupMode = "EARLIEST";
    } else if (Long.parseLong(fromTimestamp) == 0) {
      startupMode = "LATEST";
    } else if (Long.parseLong(fromTimestamp) > 0) {
      startupMode = "TIMESTAMP";
      this.fromTimestamp = Long.parseLong(fromTimestamp);
    } else {
      throw new IllegalArgumentException("Cannot parse fromTimestamp value");
    }
  }

  // ignore event before fromTimestamp
  public boolean isTimestampBefore(long t) {
    return startupMode.equals("TIMESTAMP") && this.fromTimestamp > 0 && t < this.fromTimestamp;
  }
}
