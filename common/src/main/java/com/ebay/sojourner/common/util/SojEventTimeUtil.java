package com.ebay.sojourner.common.util;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;

public class SojEventTimeUtil {

  public static boolean isEarlyEvent(Long eventimestamp, Long absEventimestamp) {
    if (eventimestamp == null) {
      return false;
    } else if (absEventimestamp == null) {
      return true;
    }
    return eventimestamp < absEventimestamp;
  }

  public static boolean isEarlyByMultiCOls(UbiEvent ubiEvent, UbiSession ubiSession) {
    if (ubiEvent.getEventTimestamp() == null) {
      return false;
    } else {
      if (ubiSession.getAbsStartTimestamp() == null) {
        return true;
      }
      if (ubiEvent.getEventTimestamp() < ubiSession.getAbsStartTimestamp()) {
        return true;
      } else if (ubiEvent.getEventTimestamp() == ubiSession.getAbsStartTimestamp()) {
        if (ubiEvent.getClickId() < ubiSession.getClickId()) {
          return true;
        } else if (ubiEvent.getClickId() == ubiSession.getClickId()) {
          if (ubiEvent.getPageId() < ubiSession.getPageId()) {
            return true;
          } else if (ubiEvent.getPageId() == ubiSession.getPageId()) {
            if (ubiEvent.getHashCode() < ubiSession.getHashCode()) {
              return true;
            } else {
              return false;
            }
          } else {
            return false;
          }
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

  }

  public static boolean isLateEvent(Long eventimestamp, Long absEventimestamp) {
    if (eventimestamp == null) {
      return false;
    } else if (absEventimestamp == null) {
      return true;
    }
    return eventimestamp > absEventimestamp;
  }
}
