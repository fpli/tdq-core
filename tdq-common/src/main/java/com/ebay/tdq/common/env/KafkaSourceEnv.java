package com.ebay.tdq.common.env;

import static com.ebay.sojourner.common.env.EnvironmentUtils.get;

import com.ebay.tdq.utils.DateUtils;
import java.io.Serializable;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author juntzhang
 */
@Data
public class KafkaSourceEnv implements Serializable {

  private String startupMode;  // TIMESTAMP,LATEST,EARLIEST
  private Long fromTimestamp = 0L;
  private Long endTimestamp = 0L;
  private final Long outOfOrderless;
  private final Long idleSourceTimeout;

  public KafkaSourceEnv() {
    outOfOrderless = DateUtils.toSeconds(get("flink.app.advance.watermark.out-of-orderless"));
    idleSourceTimeout = DateUtils.toSeconds(get("flink.app.advance.watermark.idle-source-timeout"));
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

    String endTimestampStr = get("flink.app.source.end-timestamp");
    if (StringUtils.isNotBlank(endTimestampStr)) {
      endTimestamp = Long.valueOf(endTimestampStr);
    }
  }

  // ignore event before fromTimestamp
  public boolean isTimestampBefore(long t) {
    return startupMode.equals("TIMESTAMP") && this.fromTimestamp > 0 && t < this.fromTimestamp;
  }


  public boolean isTimestampEnded(long t) {
    return this.endTimestamp > 0 && t > (this.endTimestamp + this.outOfOrderless);
  }

  public boolean isBackFill() {
    return this.endTimestamp > 0;
  }
}
