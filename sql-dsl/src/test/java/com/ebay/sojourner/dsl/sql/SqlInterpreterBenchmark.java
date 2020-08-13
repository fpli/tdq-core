//package sql;
//
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_10_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_11_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_12_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_13_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_1_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_2_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_3_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_4_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_56_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_5_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_6_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_7_INTERPRETER;
//import static com.ebay.sojourner.dsl.sql.Rules.RULE_1_INTERPRETER;
//
//import com.ebay.sojourner.common.model.UbiEvent;
//import com.ebay.sojourner.dsl.sql.SqlEventRule;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * === Benchmark: Interpreter - REGEXP === Avg rule exec time: 3445047 ns === Benchmark: Interpreter
// * - UDF === Avg rule exec time: 2204262 ns
// */
//public class SqlInterpreterBenchmark {
//
//  public static void benchmarkRegexp() {
//    // Prepare rules and events
//    List<SqlEventRule> rules = new ArrayList<>();
//    rules.add(RULE_1_INTERPRETER);
//
//    List<UbiEvent> events = new ArrayList<>();
//    events.add(new UbiEventBuilder().agentInfo("googlebot").build());
//    events.add(new UbiEventBuilder().agentInfo("crawler").build());
//    events.add(new UbiEventBuilder().agentInfo("chrome").build());
//
//    // Execute rules against events
//    TestUtils.benchmark("Interpreter - REGEXP", rules, events);
//  }
//
//  public static void benchmarkUdf() {
//    // Prepare Rules and Events
//    List<SqlEventRule> rules = new ArrayList<SqlEventRule>();
//    rules.add(ICF_RULE_1_INTERPRETER);
//    rules.add(ICF_RULE_2_INTERPRETER);
//    rules.add(ICF_RULE_3_INTERPRETER);
//    rules.add(ICF_RULE_4_INTERPRETER);
//    rules.add(ICF_RULE_5_INTERPRETER);
//    rules.add(ICF_RULE_6_INTERPRETER);
//    rules.add(ICF_RULE_7_INTERPRETER);
//    rules.add(ICF_RULE_10_INTERPRETER);
//    rules.add(ICF_RULE_11_INTERPRETER);
//    rules.add(ICF_RULE_12_INTERPRETER);
//    rules.add(ICF_RULE_13_INTERPRETER);
//    rules.add(ICF_RULE_56_INTERPRETER);
//
//    long icfBinary = 0b0001001000000000; // 0x1200
//    UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
//    List<UbiEvent> events = new ArrayList<>();
//    events.add(ubiEvent);
//
//    // Execute rules against events
//    TestUtils.benchmark("Interpreter - UDF", rules, events);
//  }
//
//  public static void main(String[] args) {
//    benchmarkRegexp();
//    benchmarkUdf();
//  }
//}
