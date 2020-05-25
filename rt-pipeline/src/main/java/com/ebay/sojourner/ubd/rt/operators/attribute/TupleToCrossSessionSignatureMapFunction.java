package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.CrossSessionSignature;
import java.util.Set;
import org.apache.flink.api.common.functions.RichMapFunction;
import scala.Tuple4;

public class TupleToCrossSessionSignatureMapFunction extends
    RichMapFunction<Tuple4<String, Boolean, Set<Integer>, Long>, CrossSessionSignature> {

  @Override
  public CrossSessionSignature map(Tuple4<String, Boolean, Set<Integer>, Long> signatures) {
    CrossSessionSignature crossSessionSignature = new CrossSessionSignature();
    crossSessionSignature.setBotFlags(signatures._3());
    crossSessionSignature.setExpirationTime(signatures._4());
    crossSessionSignature.setIsGenerate(signatures._2());
    crossSessionSignature.setSignatureId(signatures._1());
    return crossSessionSignature;
  }
}
