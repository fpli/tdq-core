package com.ebay.sojourner.rt.operator.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ebay.sojourner.business.detector.EventBotDetector;
import com.ebay.sojourner.business.parser.EventParser;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.google.common.collect.Sets;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.functions.util.FunctionUtils;
import org.apache.flink.configuration.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

class EventMapFunctionTest {

  EventMapFunction mapFunction;
  RuntimeContext mockRuntimeContext = mock(RuntimeContext.class);
  EventParser mockEventParser = mock(EventParser.class);
  EventBotDetector mockEventBotDetector = mock(EventBotDetector.class);

  @BeforeEach
  void setUp() {
    mapFunction = new EventMapFunction();
    FunctionUtils.setFunctionRuntimeContext(mapFunction, mockRuntimeContext);
    doNothing().when(mockRuntimeContext).addAccumulator(anyString(), any());
  }

  @Test
  void open() throws Exception {
    mapFunction.open(new Configuration());
    EventParser parser = Whitebox.getInternalState(mapFunction, "parser");
    EventBotDetector eventBotDetector = Whitebox.getInternalState(mapFunction, "eventBotDetector");
    assertThat(parser).isNotNull();
    assertThat(eventBotDetector).isNotNull();
  }

  @Test
  void map() throws Exception {
    RawEvent rawEvent = new RawEvent();
    Whitebox.setInternalState(mapFunction, "parser", mockEventParser);
    Whitebox.setInternalState(mapFunction, "eventBotDetector", mockEventBotDetector);
    doNothing().when(mockEventParser).parse(any(), any());
    when(mockEventBotDetector.getBotFlagList(any())).thenReturn(Sets.newHashSet(1, 2, 3));
    UbiEvent ubiEvent = mapFunction.map(rawEvent);
    assertThat(ubiEvent.getBotFlags().size()).isEqualTo(3);
    assertThat(ubiEvent.getBotFlags()).contains(1,2,3);
  }
}