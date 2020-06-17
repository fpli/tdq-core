package com.ebay.sojourner.rt.common.util;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.kafka.KafkaConnectorFactory;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.datastream.DataStream;

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

    if (CollectionUtils.isNotEmpty(sourceSet) && CollectionUtils.isNotEmpty(targetSet)) {
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

  public static void buildSignatureKafkaSink(DataStream<BotSignature> dataStream, String topic,
      String signatureId, String slotGroup, String messageKey) {

    dataStream
        .addSink(KafkaConnectorFactory.createKafkaProducer(
            topic,
            FlinkEnvUtils.getListString(Property.BEHAVIOR_TOTAL_NEW_BOOTSTRAP_SERVERS_DEFAULT),
            BotSignature.class,
            messageKey))
        .setParallelism(FlinkEnvUtils.getInteger(Property.DEFAULT_PARALLELISM))
        .slotSharingGroup(slotGroup)
        .name(String.format("%s Signature", signatureId))
        .uid(String.format("signature-%s-sink-id", signatureId));

  }

  public static void updateSignatureStatus(Map<Integer, Integer> signatureStatus,
      Set<Integer> botFlags) {

    for (Integer botFlag : botFlags) {
      if (signatureStatus.containsKey(botFlag)) {
        switch (signatureStatus.get(botFlag)) {
          case 0:
            signatureStatus.put(botFlag, 1);
            break;
          case 1:
            signatureStatus.put(botFlag, 2);
            break;
        }
      }
    }
  }
}
