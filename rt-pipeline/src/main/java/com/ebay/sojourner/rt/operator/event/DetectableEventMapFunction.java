package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.types.Either;

@Slf4j
public class DetectableEventMapFunction
    extends RichMapFunction<UbiEvent, Either<UbiEvent, UbiSession>> {

  @Override
  public Either<UbiEvent, UbiSession> map(UbiEvent value) throws Exception {
    return Either.Left(value);
  }
}
