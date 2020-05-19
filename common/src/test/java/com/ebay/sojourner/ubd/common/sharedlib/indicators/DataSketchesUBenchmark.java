package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.datasketches.hll.HllSketch;
import org.apache.datasketches.hll.TgtHllType;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
import org.apache.flink.core.memory.DataOutputSerializer;

public class DataSketchesUBenchmark {

  public static void main(String[] args) throws Exception {
    BufferedReader guidReader = new BufferedReader(new InputStreamReader(
        DataSketchesUBenchmark.class.getResourceAsStream("/guid_set_all.txt")));
    HllSketch guidHll = new HllSketch(12, TgtHllType.HLL_4);

    String guid = null;
    byte[] hllBytes = null;
    int n = 1000000;
    for (int i = 1; i <= n; i++) {
      guid = guidReader.readLine();
      guidHll.update(guid);
      if (i % (n / 10) == 0) {
        System.out.println("=====================");
        long nativeStart = System.nanoTime();
        byte[] bytes = guidHll.toCompactByteArray();
        long nativeEnd = System.nanoTime();
        System.out.printf(
            "estimate error = %.3f",
            Math.abs((guidHll.getEstimate() - i) / i));
        System.out.println();
        System.out.printf(
            "hll native duration = %d us, hll native serialized size = %d B",
            (nativeEnd - nativeStart) / 1000,
            bytes.length);
        // guidHll = HllSketch.heapify(bytes);
        KryoSerializer kryo = new KryoSerializer(HllSketch.class, new ExecutionConfig());
        DataOutputSerializer output = new DataOutputSerializer(10);
        long kryoStart = System.nanoTime();
        kryo.serialize(guidHll, output);
        long kryoEnd = System.nanoTime();
        System.out.println();
        System.out.printf("kryo duration = %d us, kryo serialized size = %d B",
            (kryoEnd - kryoStart) / 1000, output.length());
        System.out.println();
      }
    }
  }
}
