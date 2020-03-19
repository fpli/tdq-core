package com.ebay.sojourner.ubd.common.sql;

public class Rules {

  public static final String ICF_RULE_1_STR =
      "SELECT 801 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 1)";
  public static final SqlEventRule ICF_RULE_1_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_1_STR);
  public static final SqlEventRule ICF_RULE_1_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_1_STR);
  public static final String ICF_RULE_2_STR =
      "SELECT 802 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 2)";
  public static final SqlEventRule ICF_RULE_2_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_2_STR);
  public static final SqlEventRule ICF_RULE_2_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_2_STR);
  public static final String ICF_RULE_3_STR =
      "SELECT 803 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 3)";
  public static final SqlEventRule ICF_RULE_3_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_3_STR);
  public static final SqlEventRule ICF_RULE_3_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_3_STR);
  public static final String ICF_RULE_4_STR =
      "SELECT 804 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 4)";
  public static final SqlEventRule ICF_RULE_4_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_4_STR);
  public static final SqlEventRule ICF_RULE_4_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_4_STR);
  public static final String ICF_RULE_5_STR =
      "SELECT 805 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 5)";
  public static final SqlEventRule ICF_RULE_5_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_5_STR);
  public static final SqlEventRule ICF_RULE_5_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_5_STR);
  public static final String ICF_RULE_6_STR =
      "SELECT 806 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 6)";
  public static final SqlEventRule ICF_RULE_6_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_6_STR);
  public static final SqlEventRule ICF_RULE_6_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_6_STR);
  public static final String ICF_RULE_7_STR =
      "SELECT 807 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 7)";
  public static final SqlEventRule ICF_RULE_7_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_7_STR);
  public static final SqlEventRule ICF_RULE_7_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_7_STR);
  public static final String ICF_RULE_10_STR =
      "SELECT 808 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 10)";
  public static final SqlEventRule ICF_RULE_10_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_10_STR);
  public static final SqlEventRule ICF_RULE_10_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_10_STR);
  public static final String ICF_RULE_11_STR =
      "SELECT 809 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 11)";
  public static final SqlEventRule ICF_RULE_11_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_11_STR);
  public static final SqlEventRule ICF_RULE_11_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_11_STR);
  public static final String ICF_RULE_12_STR =
      "SELECT 810 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 12)";
  public static final SqlEventRule ICF_RULE_12_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_12_STR);
  public static final SqlEventRule ICF_RULE_12_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_12_STR);
  public static final String ICF_RULE_13_STR =
      "SELECT 811 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 13)";
  public static final SqlEventRule ICF_RULE_13_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_13_STR);
  public static final SqlEventRule ICF_RULE_13_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_13_STR);
  public static final String ICF_RULE_56_STR =
      "SELECT 812 as bot_flag "
          + "FROM \"soj\".\"ubiEvents\""
          + "WHERE \"checkBit\"(\"icfBinary\", 56)";
  public static final SqlEventRule ICF_RULE_56_INTERPRETER =
      new SqlInterpreterEventRule(ICF_RULE_56_STR);
  public static final SqlEventRule ICF_RULE_56_COMPILER =
      new SqlCompilerEventRule(ICF_RULE_56_STR);
  private static final String RULE_1_STR =
      "SELECT 1 as bot_1 "
          + "FROM \"soj\".\"ubiEvents\" "
          + "WHERE \"agentInfo\" SIMILAR TO '.*bot[^a-z0-9\u0000_-].*"
          + "|.*bot|.*spider.*|.*crawl.*|.*ktxn.*'";
  public static final SqlEventRule RULE_1_INTERPRETER =
      new SqlInterpreterEventRule(RULE_1_STR);
  public static final SqlEventRule RULE_1_COMPILER =
      new SqlCompilerEventRule(RULE_1_STR);
}
