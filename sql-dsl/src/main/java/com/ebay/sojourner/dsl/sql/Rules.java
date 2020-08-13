//package com.ebay.sojourner.dsl.sql;
//
//import java.util.HashMap;
//import java.util.Map;
//import org.yaml.snakeyaml.Yaml;
//
//public class Rules {
//
//  private static Map<String, String> rules =
//      new Yaml().loadAs(Rules.class.getResourceAsStream("/sql/rules.yaml"), HashMap.class);
//
//  private static final String RULE_1_STR = rules.get("RULE_1_STR");
//  public static final SqlEventRule RULE_1_INTERPRETER =
//      new SqlInterpreterEventRule(RULE_1_STR);
//  public static final SqlEventRule RULE_1_COMPILER =
//      new SqlCompilerEventRule(RULE_1_STR);
//
//  public static final String ICF_RULE_1_STR = rules.get("ICF_RULE_1_STR");
//  public static final SqlEventRule ICF_RULE_1_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_1_STR);
//  public static final SqlEventRule ICF_RULE_1_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_1_STR);
//
//  public static final String ICF_RULE_2_STR = rules.get("ICF_RULE_2_STR");
//  public static final SqlEventRule ICF_RULE_2_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_2_STR);
//  public static final SqlEventRule ICF_RULE_2_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_2_STR);
//
//  public static final String ICF_RULE_3_STR = rules.get("ICF_RULE_3_STR");
//  public static final SqlEventRule ICF_RULE_3_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_3_STR);
//  public static final SqlEventRule ICF_RULE_3_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_3_STR);
//
//  public static final String ICF_RULE_4_STR = rules.get("ICF_RULE_4_STR");
//  public static final SqlEventRule ICF_RULE_4_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_4_STR);
//  public static final SqlEventRule ICF_RULE_4_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_4_STR);
//
//  public static final String ICF_RULE_5_STR = rules.get("ICF_RULE_5_STR");
//  public static final SqlEventRule ICF_RULE_5_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_5_STR);
//  public static final SqlEventRule ICF_RULE_5_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_5_STR);
//
//  public static final String ICF_RULE_6_STR = rules.get("ICF_RULE_6_STR");
//  public static final SqlEventRule ICF_RULE_6_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_6_STR);
//  public static final SqlEventRule ICF_RULE_6_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_6_STR);
//
//  public static final String ICF_RULE_7_STR = rules.get("ICF_RULE_7_STR");
//  public static final SqlEventRule ICF_RULE_7_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_7_STR);
//  public static final SqlEventRule ICF_RULE_7_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_7_STR);
//
//  public static final String ICF_RULE_10_STR = rules.get("ICF_RULE_10_STR");
//  public static final SqlEventRule ICF_RULE_10_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_10_STR);
//  public static final SqlEventRule ICF_RULE_10_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_10_STR);
//
//  public static final String ICF_RULE_11_STR = rules.get("ICF_RULE_11_STR");
//  public static final SqlEventRule ICF_RULE_11_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_11_STR);
//  public static final SqlEventRule ICF_RULE_11_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_11_STR);
//
//  public static final String ICF_RULE_12_STR = rules.get("ICF_RULE_12_STR");
//  public static final SqlEventRule ICF_RULE_12_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_12_STR);
//  public static final SqlEventRule ICF_RULE_12_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_12_STR);
//
//  public static final String ICF_RULE_13_STR = rules.get("ICF_RULE_13_STR");
//  public static final SqlEventRule ICF_RULE_13_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_13_STR);
//  public static final SqlEventRule ICF_RULE_13_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_13_STR);
//
//  public static final String ICF_RULE_56_STR = rules.get("ICF_RULE_56_STR");
//  public static final SqlEventRule ICF_RULE_56_INTERPRETER =
//      new SqlInterpreterEventRule(ICF_RULE_56_STR);
//  public static final SqlEventRule ICF_RULE_56_COMPILER =
//      new SqlCompilerEventRule(ICF_RULE_56_STR);
//}
