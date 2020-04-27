package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.sharedlib.util.IsValidIPv4;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJTS2Date;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.util.Set;

public class TimestampMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  public static final String SHOCKWAVE_FLASH_AGENT = "Shockwave Flash";
  private PageIndicator indicator;
  //  private EventListenerContainer eventListenerContainer;
  private Set<Integer> agentExcludeSet;

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setAbsStartTimestamp(null);
    sessionAccumulator.getUbiSession().setAbsEndTimestamp(null);
    sessionAccumulator.getUbiSession().setStartTimestamp(null);
    sessionAccumulator.getUbiSession().setEndTimestamp(null);

  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (!event.isIframe()) {
      if (!event.isRdt() || indicator.isCorrespondingPageEvent(event)) {
        if (sessionAccumulator.getUbiSession().getStartTimestamp() == null) {
          sessionAccumulator.getUbiSession().setStartTimestamp(event.getEventTimestamp());
        } else if (event.getEventTimestamp() != null
            && sessionAccumulator.getUbiSession().getStartTimestamp() > event.getEventTimestamp()) {
          sessionAccumulator.getUbiSession().setStartTimestamp(event.getEventTimestamp());

        }
        if (sessionAccumulator.getUbiSession().getEndTimestamp() == null) {
          sessionAccumulator.getUbiSession().setEndTimestamp(event.getEventTimestamp());
        } else if (event.getEventTimestamp() != null
            && sessionAccumulator.getUbiSession().getEndTimestamp() < event.getEventTimestamp()) {
          sessionAccumulator.getUbiSession().setEndTimestamp(event.getEventTimestamp());
        }
      }
      if (!event.isRdt()) {
        if (sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT() == null) {
          sessionAccumulator.getUbiSession()
              .setStartTimestampNOIFRAMERDT(event.getEventTimestamp());
        } else if (event.getEventTimestamp() != null
            && sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT() > event
            .getEventTimestamp()) {
          sessionAccumulator.getUbiSession()
              .setStartTimestampNOIFRAMERDT(event.getEventTimestamp());

        }
        if (sessionAccumulator.getUbiSession().getEndTimestampNOIFRAMERDT() == null) {
          sessionAccumulator.getUbiSession().setEndTimestampNOIFRAMERDT(event.getEventTimestamp());
        } else if (event.getEventTimestamp() != null
            && sessionAccumulator.getUbiSession().getEndTimestampNOIFRAMERDT() < event
            .getEventTimestamp()) {
          sessionAccumulator.getUbiSession().setEndTimestampNOIFRAMERDT(event.getEventTimestamp());
        }
        String agentInfo = event.getAgentInfo();
        if (!agentExcludeSet.contains(event.getPageId()) && agentInfo != null
            && !agentInfo.equals(SHOCKWAVE_FLASH_AGENT) && !IsValidIPv4.isValidIP(agentInfo)) {
          if (sessionAccumulator.getUbiSession().getStartTimestampForAgentString() == null) {
            sessionAccumulator.getUbiSession()
                .setStartTimestampForAgentString(event.getEventTimestamp());
          } else if (event.getEventTimestamp() != null
              && sessionAccumulator.getUbiSession().getStartTimestampForAgentString() > event
              .getEventTimestamp()) {
            sessionAccumulator.getUbiSession()
                .setStartTimestampForAgentString(event.getEventTimestamp());

          }
        }
      }
      if (sessionAccumulator.getUbiSession().getStartTimestampNOIFRAME() == null) {
        sessionAccumulator.getUbiSession().setStartTimestampNOIFRAME(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getStartTimestampNOIFRAME() > event
          .getEventTimestamp()) {
        sessionAccumulator.getUbiSession().setStartTimestampNOIFRAME(event.getEventTimestamp());

      }

    }
    if (sessionAccumulator.getUbiSession().getAbsStartTimestamp() == null) {
      sessionAccumulator.getUbiSession().setAbsStartTimestamp(event.getEventTimestamp());
    } else if (event.getEventTimestamp() != null
        && sessionAccumulator.getUbiSession().getAbsStartTimestamp() > event.getEventTimestamp()) {
      sessionAccumulator.getUbiSession().setAbsStartTimestamp(event.getEventTimestamp());

      //      e.onEarlyEventChange(event,sessionAccumulator.getUbiSession());
    }
    if (sessionAccumulator.getUbiSession().getAbsEndTimestamp() == null) {
      sessionAccumulator.getUbiSession().setAbsEndTimestamp(event.getEventTimestamp());
    } else if (event.getEventTimestamp() != null
        && sessionAccumulator.getUbiSession().getAbsEndTimestamp() < event.getEventTimestamp()) {
      sessionAccumulator.getUbiSession().setAbsEndTimestamp(event.getEventTimestamp());
      //      eventListenerContainer.onLateEventChange(event,sessionAccumulator.getUbiSession());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    sessionAccumulator
        .getUbiSession()
        .setSojDataDt(sessionAccumulator.getUbiSession().getAbsEndTimestamp() == null ? null :
            SOJTS2Date.castSojTimestampToDate(
                sessionAccumulator.getUbiSession().getAbsEndTimestamp()));
    // Fix bug HDMIT-3732 to avoid integer result overflow
    int durationSec =
        (sessionAccumulator.getUbiSession().getStartTimestamp() == null
            || sessionAccumulator.getUbiSession().getEndTimestamp() == null)
            ? 0
            : (int)
                ((sessionAccumulator.getUbiSession().getEndTimestamp()
                    - sessionAccumulator.getUbiSession().getStartTimestamp())
                    / 1000000);
    int absDuration =
        (int)
            (sessionAccumulator.getUbiSession().getAbsEndTimestamp() == null
                || sessionAccumulator.getUbiSession().getAbsStartTimestamp() == null
                ? 0 : (sessionAccumulator.getUbiSession().getAbsEndTimestamp()
                - sessionAccumulator.getUbiSession().getAbsStartTimestamp())
                / 1000000);
    sessionAccumulator.getUbiSession().setDurationSec(durationSec);
    sessionAccumulator.getUbiSession().setAbsDuration(absDuration);
  }

  @Override
  public void init() throws Exception {
    setPageIndicator(new PageIndicator(UBIConfig.getString(Property.SEARCH_VIEW_PAGES)));
    agentExcludeSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.AGENT_EXCLUDE_PAGES), Property.PROPERTY_DELIMITER);
    //    eventListenerContainer = EventListenerContainer.getInstance();
  }

  void setPageIndicator(PageIndicator indicator) {
    this.indicator = indicator;
  }
}
