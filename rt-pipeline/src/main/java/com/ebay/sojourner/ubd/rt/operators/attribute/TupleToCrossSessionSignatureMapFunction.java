package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.CrossSessionSignature;
import java.util.Set;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple4;

public class TupleToCrossSessionSignatureMapFunction extends
    RichMapFunction<Tuple4<String, Boolean, Set<Integer>, Long>, CrossSessionSignature> {

  @Override
  public CrossSessionSignature map(Tuple4<String, Boolean, Set<Integer>, Long> signatures) {
    CrossSessionSignature crossSessionSignature = new CrossSessionSignature();
    crossSessionSignature.setBotFlags(signatures.f2);
    crossSessionSignature.setExpirationTime(signatures.f3);
    crossSessionSignature.setIsGenerate(signatures.f1);
    crossSessionSignature.setSignatureId(signatures.f0);
    return crossSessionSignature;
  }
}
