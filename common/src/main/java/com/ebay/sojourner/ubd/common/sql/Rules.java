package com.ebay.sojourner.ubd.common.sql;

public class Rules {
  public static final SqlEventRule RULE_1 =
      new SqlEventRule(
          "SELECT 1 as bot_1 "
              + "FROM \"soj\".\"ubiEvents\" "
              + "WHERE \"agentInfo\" SIMILAR TO '.*bot[^a-z0-9\u0000_-].*"
              + "|.*bot|.*spider.*|.*crawl.*|.*ktxn.*'");

  public static final SqlEventRule ICF_RULE_1 =
      new SqlEventRule(
          "SELECT 801 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 1)");

  public static final SqlEventRule ICF_RULE_2 =
      new SqlEventRule(
          "SELECT 802 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 2)");

  public static final SqlEventRule ICF_RULE_3 =
      new SqlEventRule(
          "SELECT 803 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 3)");

  public static final SqlEventRule ICF_RULE_4 =
      new SqlEventRule(
          "SELECT 804 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 4)");

  public static final SqlEventRule ICF_RULE_5 =
      new SqlEventRule(
          "SELECT 805 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 5)");

  public static final SqlEventRule ICF_RULE_6 =
      new SqlEventRule(
          "SELECT 806 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 6)");

  public static final SqlEventRule ICF_RULE_7 =
      new SqlEventRule(
          "SELECT 807 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 7)");

  public static final SqlEventRule ICF_RULE_10 =
      new SqlEventRule(
          "SELECT 808 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 10)");

  public static final SqlEventRule ICF_RULE_11 =
      new SqlEventRule(
          "SELECT 809 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 11)");

  public static final SqlEventRule ICF_RULE_12 =
      new SqlEventRule(
          "SELECT 810 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 12)");

  public static final SqlEventRule ICF_RULE_13 =
      new SqlEventRule(
          "SELECT 811 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 13)");

  public static final SqlEventRule ICF_RULE_56 =
      new SqlEventRule(
          "SELECT 812 as bot_flag "
              + "FROM \"soj\".\"ubiEvents\""
              + "WHERE \"checkBit\"(\"icfBinary\", 56)");
}
