package com.ebay.sojourner.rt.common.state;

import java.util.Map;
import java.util.Set;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class MapStateDesc {

  public static final MapStateDescriptor<String, Set<Integer>> ipSignatureDesc =
      new MapStateDescriptor<>(
          "broadcast-ipSignature-state",
          BasicTypeInfo.STRING_TYPE_INFO,
          TypeInformation.of(new TypeHint<Set<Integer>>() {
          }));

  public static final MapStateDescriptor<String, Set<Integer>> agentSignatureDesc =
      new MapStateDescriptor<>(
          "broadcast-agentSignature-state",
          BasicTypeInfo.STRING_TYPE_INFO,
          TypeInformation.of(new TypeHint<Set<Integer>>() {
          }));

  public static final MapStateDescriptor<String, Set<Integer>> agentIpSignatureDesc =
      new MapStateDescriptor<>(
          "broadcast-agentIpSignature-state",
          BasicTypeInfo.STRING_TYPE_INFO,
          TypeInformation.of(new TypeHint<Set<Integer>>() {
          }));

  public static final MapStateDescriptor<String, Map<Integer, Long>> attributeSignatureDesc =
      new MapStateDescriptor<>(
          "broadcast-attributeSignature-state",
          BasicTypeInfo.STRING_TYPE_INFO,
          TypeInformation.of(new TypeHint<Map<Integer, Long>>() {
          }));

  public static final MapStateDescriptor<String, Set<Integer>> guidSignatureDesc =
      new MapStateDescriptor<>(
          "broadcast-guidSignature-state",
          BasicTypeInfo.STRING_TYPE_INFO,
          TypeInformation.of(new TypeHint<Set<Integer>>() {
          }));
}
