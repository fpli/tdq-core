package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import java.util.List;

public class TestUtils {

  public static final int DEFAULT_ITERATIONS = 1000;
  private static final String UDF_1_STR =
<<<<<<< HEAD
      "SELECT square(2) FROM soj.idl_event";

=======
      "SELECT square(2) FROM soj.ubiEvents";
>>>>>>> sojourner-performance
  public static final SqlEventRule UDF_1_INTERPRETER =
      new SqlInterpreterEventRule(UDF_1_STR);
  public static final SqlEventRule UDF_1_COMPILER =
      new SqlCompilerEventRule(UDF_1_STR);

  public static void benchmark(String name, List<SqlEventRule> rules, List<UbiEvent> events,
      int iterations) {
    System.out.println("=== Benchmark: " + name + " ===");
    long start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      for (SqlEventRule rule : rules) {
        for (UbiEvent event : events) {
          rule.getBotFlag(event);
        }
      }
    }
    long end = System.nanoTime();
    long avg = (end - start) / iterations / rules.size() / events.size();
    System.out.println("Avg rule exec time: " + avg + " ns");
  }

  public static void benchmark(String name, List<SqlEventRule> rules, List<UbiEvent> events) {
    benchmark(name, rules, events, DEFAULT_ITERATIONS);
  }
}
