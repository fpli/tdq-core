package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.CrossSessionSignature;
import java.util.Set;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple5;

public class TupleToSignatureMapFunction extends
    RichMapFunction<Tuple5<String, String, Boolean, Set<Integer>, Long>, CrossSessionSignature> {

  @Override
  public CrossSessionSignature map(Tuple5<String, String, Boolean, Set<Integer>, Long> signatures) {
    CrossSessionSignature crossSessionSignature = new CrossSessionSignature();
    crossSessionSignature.setSignatureValue(signatures.f1);
    crossSessionSignature.setBotFlags(signatures.f3);
    crossSessionSignature.setExpirationTime(signatures.f4);
    crossSessionSignature.setIsGenerate(signatures.f2);
    crossSessionSignature.setSignatureId(signatures.f0);
    return crossSessionSignature;
  }
}
