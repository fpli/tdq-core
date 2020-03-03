/**
 * Autogenerated by Avro
 *
 * <p>DO NOT EDIT DIRECTLY
 */
package com.ebay.sojourner.ubd.common.model;

import com.ebay.sojourner.ubd.common.util.Constants;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;
import lombok.Getter;

@Data
public class UbiEvent implements Serializable {
  private String guid;
  private String sessionId = Constants.NO_SESSION_ID;
  private int seqNum;
  private Long sessionStartDt;
  private Long sojDataDt;
  private int clickId;
  private int siteId = -1;
  private int version;
  private int pageId = -1;
  private String pageName;
  private Long refererHash;
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
  private boolean rdt;
  private int regu;
  private String sqr;
  private int staticPageType;
  private int reservedForFuture;
  private String eventAttr;
  private Long currentImprId;
  private Long sourceImprId;
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
  private Set<Integer> botFlags = new LinkedHashSet<>();
  private long icfBinary;
  @Getter private long eventCnt;
  //  private Map<String, Object> counters;

  public void updateSessionId() {
    int charPos = Constants.HEX_DIGITS.length;
    int mask = (1 << 4) - 1;
    long decimal = sessionStartTime;
    char[] out = new char[Constants.HEX_DIGITS.length];

    Arrays.fill(out, '0');

    do {
      out[--charPos] = Constants.HEX_DIGITS[(int) (decimal & mask)];
      decimal = decimal >>> 4;
    } while (decimal != 0);

    this.sessionId = guid + new String(out, 0, out.length);
  }

  public void eventCountIncrementByOne() {
    eventCnt++;
  }

  public boolean isNewSession() {
    return Constants.NO_SESSION_ID.equals(sessionId);
  }

  public boolean hasSessionEndTime() {
    return Constants.NO_TIMESTAMP != sessionEndTime;
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
}
