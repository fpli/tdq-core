package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.CrossSessionSignature;
import org.apache.flink.api.common.functions.RichMapFunction;

public class TupleToCrossSessionSignatureMapFunction extends
    RichMapFunction<BotSignature, CrossSessionSignature> {

  @Override
  public CrossSessionSignature map(BotSignature signatures) {
    CrossSessionSignature crossSessionSignature = new CrossSessionSignature();
    //    crossSessionSignature.setBotFlags(new HashSet<>(signatures.getBotFlags()));
    //    crossSessionSignature.setExpirationTime(signatures.getExpirationTime());
    //    crossSessionSignature.setIsGenerate(signatures.getIsGeneration());
    //    crossSessionSignature.setSignatureId(signatures.);
    return crossSessionSignature;
  }
}
