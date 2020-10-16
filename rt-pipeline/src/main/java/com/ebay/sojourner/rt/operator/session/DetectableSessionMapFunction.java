package com.ebay.sojourner.rt.operator.session;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.types.Either;

public class DetectableSessionMapFunction
    extends RichMapFunction<UbiSession, Either<UbiEvent, UbiSession>> {

  @Override
  public Either<UbiEvent, UbiSession> map(UbiSession value) throws Exception {
    return Either.Right(value);
  }
}
