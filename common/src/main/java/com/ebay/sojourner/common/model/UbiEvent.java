/**
 * Autogenerated by Avro
 *
 * <p>DO NOT EDIT DIRECTLY
 */
package com.ebay.sojourner.common.model;

import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

@Data
public class UbiEvent implements Serializable {

  private String guid;
  private String sessionId = Constants.NO_SESSION_ID;// for jetstream
  private long sessionSkey;
  private int seqNum;
  private Long sessionStartDt;
  private Long sojDataDt;
  private int clickId;
  private int siteId = -1;
  private int version;
  private int pageId = -1;
  private String pageName;
  private Long refererHash; // String in jetstream
  private Long eventTimestamp;
  private String urlQueryString;
  private ClientData clientData;
  private String cookies;
  private String applicationPayload;
  private String webServer;
  private String referrer;
  private String userId;
  private Long itemId;
  private String flags;
  private boolean rdt; //int in jetstream
  private int regu;
  private String sqr;
  private int staticPageType;
  private int reservedForFuture;
  private String eventAttr;
  private Long currentImprId;//ciid in jetstream
  private Long sourceImprId;//siid in jetstream
  private int cobrand;
  private boolean iframe;
  private String agentInfo;
  private String forwardedFor;
  private String clientIP;
  private int bitVal;
  private Integer appId;
  private Long oldSessionSkey;
  private int hashCode;
  private boolean partialValidPage = true;
  private long sessionStartTime;
  private long sessionEndTime;
  private Set<Integer> botFlags = new LinkedHashSet<>(); // bot in jetstream int
  private long icfBinary;
  // collect some metrics for monitor and validation
  private long ingestTime;
  private long generateTime;
  @Getter
  private long eventCnt;

  //metric for monitor
  private String dataCenter;

  //new columns from jetstream
  private String sid;
  private long eventCaptureTime;//didn't find logic
  private String requestCorrelationId;
  private String pageFamily; // need to lookup and diff from batch ignore currently
  private String remoteIP;
  private String appVersion;
  private String eventFamily;
  private String eventAction;
  private String trafficSource;
  private String osVersion;
  private String deviceFamily;
  private String deviceType;
  private String browserVersion;
  private String browserFamily;
  private String osFamily;
  private String enrichedOsVersion;
  private String rlogid;
  private String city;
  private String region;
  private String country;
  private String continent;
  private String lineSpeed;
  private boolean isReturningVisitor;
  private String streamId;
  private String buserId;

  //  private Map<String, Object> counters;

  public static void main(String[] args) {

    System.out.println(new UbiEvent().concatTimestamp("0174145e1540a5ed28910e02ffa7abf5",
        3801167554921000L));
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("0174145e1540a5ed28910e02ffa7abf5");
    ubiEvent.setEventTimestamp(3801167554921000L);
    ubiEvent.updateSessionId();
    System.out.println(ubiEvent.getSessionId());
    System.out.println(1 << 4);
  }

  public void setIsReturningVisitor(boolean returningVisitor) {
    isReturningVisitor = returningVisitor;
  }

  public void updateSessionId() {
    //old version
    //    int charPos = Constants.HEX_DIGITS.length;
    //    int mask = (1 << 4) - 1;
    //    long decimal = eventTimestamp == null ? 0L : eventTimestamp;
    //    char[] out = new char[Constants.HEX_DIGITS.length];
    //
    //    Arrays.fill(out, '0');
    //
    //    do {
    //      out[--charPos] = Constants.HEX_DIGITS[(int) (decimal & mask)];
    //      decimal = decimal >>> 4;
    //    } while (decimal != 0);

    // this.sessionId = guid + new String(out, 0, out.length);
    this.sessionId = concatTimestamp(this.guid, this.eventTimestamp);
  }

  public void updateSessionSkey() {
    this.sessionSkey = this.eventTimestamp / Constants.SESSION_KEY_DIVISION;
  }

  private String concatTimestamp(String prefix, long timestamp) {
    long unixTimestamp = SojTimestamp.getSojTimestampToUnixTimestamp(timestamp);
    int prefixLen = 0;
    if (!StringUtils.isBlank(prefix)) {
      prefixLen = prefix.length();
    }else{
      prefix="";
    }
    StringBuilder builder = new StringBuilder(prefixLen + 16);
    builder.append(prefix);
    String x = Long.toHexString(unixTimestamp);
    for (int i = 16 - x.length(); i > 0; i--) {
      builder.append('0');
    }
    builder.append(x);
    return builder.toString();
  }

  public void eventCountIncrementByOne() {
    eventCnt++;
  }

  public boolean isNewSession() {
    return Constants.NO_SESSION_ID.equals(sessionId);
  }

  //  public Object get(String key) {
  //    if (counters == null) {
  //      synchronized (this) {
  //        if (counters == null) {
  //          counters = new ConcurrentHashMap<>();
  //        }
  //      }
  //    }
  //
  //    return counters.get(key);
  //  }
  //
  //  public void put(String key, Object value) {
  //    if (counters == null) {
  //      synchronized (this) {
  //        if (counters == null) {
  //          counters = new ConcurrentHashMap<>();
  //        }
  //      }
  //    }
  //
  //    counters.put(key, value);
  //  }

  public boolean hasSessionEndTime() {
    return Constants.NO_TIMESTAMP != sessionEndTime;
  }


}
