package com.ebay.sojourner.rt.common.util;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.metrics.Counter;

public class SignatureUtils {

  public static Set<Integer> generateNewSignature(Map<Integer, Integer> signatureStates) {

    LinkedHashSet<Integer> newGenerateSignatures = new LinkedHashSet<>();

    if (signatureStates.size() > 0) {
      for (Map.Entry<Integer, Integer> signatureState : signatureStates.entrySet()) {
        if (signatureState.getValue() == 1) {
          newGenerateSignatures.add(signatureState.getKey());
        }
      }
    }

    return newGenerateSignatures;
  }

  public static Set<Integer> setBotFlags(Set<Integer> sourceSet, Set<Integer> targetSet) {

    if (CollectionUtils.isNotEmpty(sourceSet) || targetSet != null) {
      targetSet.addAll(sourceSet);
    }

    return targetSet;
  }

  public static void signatureMetricsCollection(Map<String, Counter> signatureCounterNameMap,
      String signatureId, Boolean isGeneration) {

    if (signatureCounterNameMap.get(signatureId + Constants.GENERATION_PREFFIX) != null
        || signatureCounterNameMap.get(signatureId + Constants.EXPIRATION_PREFFIX) != null) {
      if (isGeneration) {
        Counter gCounter = signatureCounterNameMap.get(signatureId + Constants.GENERATION_PREFFIX);
        gCounter.inc();
      } else {
        Counter eCounter = signatureCounterNameMap.get(signatureId + Constants.EXPIRATION_PREFFIX);
        eCounter.inc();
      }
    }
  }
}
