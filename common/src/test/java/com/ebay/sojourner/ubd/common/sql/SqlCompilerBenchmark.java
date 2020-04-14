package com.ebay.sojourner.ubd.common.sql;

import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_10_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_11_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_12_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_13_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_1_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_2_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_3_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_4_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_56_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_5_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_6_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.ICF_RULE_7_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.Rules.RULE_1_COMPILER;
import static com.ebay.sojourner.ubd.common.sql.TestUtils.benchmark;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * === Benchmark: Compiler - REGEXP === Avg rule exec time: 25230 ns === Benchmark: Compiler - UDF
 * === Avg rule exec time: 3592 ns
 */
public class SqlCompilerBenchmark {

  public static void benchmarkRegexp() {
    // Prepare rules and events
    List<SqlEventRule> rules = new ArrayList<SqlEventRule>();
    rules.add(RULE_1_COMPILER);

    List<UbiEvent> events = new ArrayList<UbiEvent>();
    events.add(new UbiEventBuilder().agentInfo("googlebot").build());
    events.add(new UbiEventBuilder().agentInfo("crawler").build());
    events.add(new UbiEventBuilder().agentInfo("chrome").build());

    // Execute rules against events
    benchmark("Compiler - REGEXP", rules, events);
  }

  public static void benchmarkUdf() {
    // Prepare rules and events
    List<SqlEventRule> rules = new ArrayList<SqlEventRule>();
    rules.add(ICF_RULE_1_COMPILER);
    rules.add(ICF_RULE_2_COMPILER);
    rules.add(ICF_RULE_3_COMPILER);
    rules.add(ICF_RULE_4_COMPILER);
    rules.add(ICF_RULE_5_COMPILER);
    rules.add(ICF_RULE_6_COMPILER);
    rules.add(ICF_RULE_7_COMPILER);
    rules.add(ICF_RULE_10_COMPILER);
    rules.add(ICF_RULE_11_COMPILER);
    rules.add(ICF_RULE_12_COMPILER);
    rules.add(ICF_RULE_13_COMPILER);
    rules.add(ICF_RULE_56_COMPILER);

    long icfBinary = 0b0001001000000000; // 0x1200
    UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
    List<UbiEvent> events = new ArrayList<>();
    events.add(ubiEvent);

    // Execute rules against events
    benchmark("Compiler - UDF", rules, events);
  }

  public static void main(String[] args) {
    benchmarkRegexp();
    benchmarkUdf();
  }
}
