package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.business.parser.PageIndicator;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.IntermediateLkp;
import com.ebay.sojourner.common.util.IsValidIPv4;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.PropertyUtils;
import com.ebay.sojourner.common.util.SojUtils;
import com.ebay.sojourner.common.util.UBIConfig;
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

        // for ScEvent
        Integer pageId = event.getPageId() == Integer.MIN_VALUE ? -99 : event.getPageId();
        if (!IntermediateLkp.getInstance().getScPageSet1().contains(pageId)
            && !IntermediateLkp.getInstance().getScPageSet2().contains(pageId)) {
          if (sessionAccumulator.getUbiSession().getStartTimestampForScEvent() == null) {
            sessionAccumulator.getUbiSession()
                .setStartTimestampForScEvent(event.getEventTimestamp());
          } else if (event.getEventTimestamp() != null
              && sessionAccumulator.getUbiSession().getStartTimestampForScEvent() > event
              .getEventTimestamp()) {
            sessionAccumulator.getUbiSession()
                .setStartTimestampForScEvent(event.getEventTimestamp());

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
    if (event.getReferrer() != null) {
      if (sessionAccumulator.getUbiSession().getStartTimestampForReferrer() == null) {
        sessionAccumulator.getUbiSession().setStartTimestampForReferrer(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getStartTimestampForReferrer() > event
          .getEventTimestamp()) {
        sessionAccumulator.getUbiSession().setStartTimestampForReferrer(event.getEventTimestamp());
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
    // for roverclick
    if (SojUtils.isRoverClick(event)) {
      if (sessionAccumulator.getUbiSession().getAbsStartTimestampForRoverClick() == null) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForRoverClick(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getAbsStartTimestampForRoverClick() > event
          .getEventTimestamp()) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForRoverClick(event.getEventTimestamp());
      }
    }

    // for rover3084
    if (SojUtils.isRover3084Click(event)) {
      if (sessionAccumulator.getUbiSession().getAbsStartTimestampForRover3084() == null) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForRover3084(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getAbsStartTimestampForRover3084() > event
          .getEventTimestamp()) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForRover3084(event.getEventTimestamp());
      }
    }
    // for rover3085
    if (SojUtils.isRover3085Click(event)) {
      if (sessionAccumulator.getUbiSession().getAbsStartTimestampForRover3085() == null) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForRover3085(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getAbsStartTimestampForRover3085() > event
          .getEventTimestamp()) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForRover3085(event.getEventTimestamp());
      }
    }

    // for rover3962
    if (SojUtils.isRover3962Click(event)) {
      if (sessionAccumulator.getUbiSession().getAbsStartTimestampForRover3962() == null) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForRover3962(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getAbsStartTimestampForRover3962() > event
          .getEventTimestamp()) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForRover3962(event.getEventTimestamp());
      }
    }

    // for NotifyClick
    if (IntermediateLkp.getInstance()
        .getNotifyCLickPageSet()
        .contains(event.getPageId())) {
      if (sessionAccumulator.getUbiSession().getAbsStartTimestampForNotifyClick() == null) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForNotifyClick(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getAbsStartTimestampForNotifyClick() > event
          .getEventTimestamp()) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForNotifyClick(event.getEventTimestamp());
      }
    }

    // for NotifyView
    if (IntermediateLkp.getInstance()
        .getNotifyViewPageSet()
        .contains(event.getPageId())) {
      if (sessionAccumulator.getUbiSession().getAbsStartTimestampForNotifyView() == null) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForNotifyView(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getAbsStartTimestampForNotifyView() > event
          .getEventTimestamp()) {
        sessionAccumulator.getUbiSession()
            .setAbsStartTimestampForNotifyView(event.getEventTimestamp());
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {

    //allign with jetstream
    //    sessionAccumulator
    //        .getUbiSession()
    //        .setSojDataDt(sessionAccumulator.getUbiSession().getAbsEndTimestamp() == null ? null :
    //            SOJTS2Date.castSojTimestampToDate(
    //                sessionAccumulator.getUbiSession().getAbsEndTimestamp()));
    sessionAccumulator
        .getUbiSession()
        .setSojDataDt(sessionAccumulator.getUbiSession().getAbsEndTimestamp());
    // Fix bug HDMIT-3732 to avoid integer result overflow
    int durationSec =
        (sessionAccumulator.getUbiSession().getStartTimestamp() == null
            || sessionAccumulator.getUbiSession().getEndTimestamp() == null)
            ? 0
            : (int)
                ((sessionAccumulator.getUbiSession().getEndTimestamp()
                    - sessionAccumulator.getUbiSession().getStartTimestamp())
                    / 1000000);
    //allign with jetstream 0810
    //change to long
    //    int absDuration =
    //        (int)
    //            (sessionAccumulator.getUbiSession().getAbsEndTimestamp() == null
    //                || sessionAccumulator.getUbiSession().getAbsStartTimestamp() == null
    //                ? 0 : (sessionAccumulator.getUbiSession().getAbsEndTimestamp()
    //                - sessionAccumulator.getUbiSession().getAbsStartTimestamp())
    //                / 1000000);


    int absDuration =
        (int)
            (sessionAccumulator.getUbiSession().getAbsEndTimestamp() == null
                || sessionAccumulator.getUbiSession().getAbsStartTimestamp() == null
                ? 0 : (sessionAccumulator.getUbiSession().getAbsEndTimestamp()
                - sessionAccumulator.getUbiSession().getAbsStartTimestamp()>Integer.MAX_VALUE?
                Integer.MAX_VALUE:(sessionAccumulator.getUbiSession().getAbsEndTimestamp()
                - sessionAccumulator.getUbiSession().getAbsStartTimestamp()))
                );
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
