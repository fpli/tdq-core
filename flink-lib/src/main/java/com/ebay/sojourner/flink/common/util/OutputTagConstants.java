package com.ebay.sojourner.flink.common.util;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.OutputTag;

public class OutputTagConstants {

  public static OutputTag<UbiSession> sessionOutputTag =
      new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));

  public static OutputTag<UbiEvent> lateEventOutputTag =
      new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));

  public static OutputTag<UbiEvent> mappedEventOutputTag =
      new OutputTag<>("mapped-event-output-tag", TypeInformation.of(UbiEvent.class));

  public static OutputTag<RawEvent> dataSkewOutputTag =
      new OutputTag<>("skew-raw-event-output-tag", TypeInformation.of(RawEvent.class));

  public static OutputTag<SojEvent> botEventOutputTag =
      new OutputTag<>("bot-event-output-tag", TypeInformation.of(SojEvent.class));

  public static OutputTag<SojSession> botSessionOutputTag =
      new OutputTag<>("bot-session-output-tag", TypeInformation.of(SojSession.class));

  public static OutputTag<SojSession> crossDaySessionOutputTag =
      new OutputTag<>("cross-day-session-output-tag", TypeInformation.of(SojSession.class));

  public static OutputTag<SojSession> openSessionOutputTag =
      new OutputTag<>("open-session-output-tag", TypeInformation.of(SojSession.class));
}
