package com.ebay.tdq.common.env;

import static com.ebay.sojourner.common.env.EnvironmentUtils.get;

import com.ebay.sojourner.common.env.EnvironmentUtils;
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

    String endTimestampStr = EnvironmentUtils.getStringOrDefault("flink.app.source.end-timestamp", null);
    if (StringUtils.isNotBlank(endTimestampStr)) {
      endTimestamp = Long.valueOf(endTimestampStr);
    }
  }

  // ignore event before fromTimestamp
  private boolean isTimestampBefore(long t) {
    return startupMode.equals("TIMESTAMP") && this.fromTimestamp > 0 && t < this.fromTimestamp;
  }

  private boolean isTimestampEnded(long t) {
    return this.endTimestamp > 0 && t > this.endTimestamp;
  }

  public boolean isProcessElement(long t) {
    return !isTimestampBefore(t) && !isTimestampEnded(t);
  }

  public boolean isNotProcessElement(long t) {
    return !isProcessElement(t);
  }

  public boolean isEndOfStream(long t) {
    return this.endTimestamp > 0 && t > (this.endTimestamp + this.outOfOrderless * 1000);
  }

  public boolean isBackFill() {
    return this.endTimestamp > 0;
  }

}
