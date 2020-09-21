package com.ebay.sojourner.dsl.sql;

import com.ebay.sojourner.dsl.domain.rule.RuleDefinition;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class Rules {

  private static Map<String, String> rules =
      new Yaml().loadAs(Rules.class.getResourceAsStream("/icf-rules.yaml"), HashMap.class);


  public static final String ICF_RULE_1_STR = rules.get("ICF_RULE_1_STR");
  public static final SQLEventRule ICF_RULE_1 = new SQLEventRule(getRuleDef(ICF_RULE_1_STR));

  public static final String ICF_RULE_2_STR = rules.get("ICF_RULE_2_STR");
  public static final SQLEventRule ICF_RULE_2 = new SQLEventRule(getRuleDef(ICF_RULE_2_STR));

  public static final String ICF_RULE_3_STR = rules.get("ICF_RULE_3_STR");
  public static final SQLEventRule ICF_RULE_3 = new SQLEventRule(getRuleDef(ICF_RULE_3_STR));

  public static final String ICF_RULE_4_STR = rules.get("ICF_RULE_4_STR");
  public static final SQLEventRule ICF_RULE_4 = new SQLEventRule(getRuleDef(ICF_RULE_4_STR));

  public static final String ICF_RULE_5_STR = rules.get("ICF_RULE_5_STR");
  public static final SQLEventRule ICF_RULE_5 = new SQLEventRule(getRuleDef(ICF_RULE_5_STR));

  public static final String ICF_RULE_6_STR = rules.get("ICF_RULE_6_STR");
  public static final SQLEventRule ICF_RULE_6 = new SQLEventRule(getRuleDef(ICF_RULE_6_STR));

  public static final String ICF_RULE_7_STR = rules.get("ICF_RULE_7_STR");
  public static final SQLEventRule ICF_RULE_7 = new SQLEventRule(getRuleDef(ICF_RULE_7_STR));

  public static final String ICF_RULE_10_STR = rules.get("ICF_RULE_10_STR");
  public static final SQLEventRule ICF_RULE_10 = new SQLEventRule(getRuleDef(ICF_RULE_10_STR));

  public static final String ICF_RULE_11_STR = rules.get("ICF_RULE_11_STR");
  public static final SQLEventRule ICF_RULE_11 = new SQLEventRule(getRuleDef(ICF_RULE_11_STR));

  public static final String ICF_RULE_12_STR = rules.get("ICF_RULE_12_STR");
  public static final SQLEventRule ICF_RULE_12 = new SQLEventRule(getRuleDef(ICF_RULE_12_STR));

  public static final String ICF_RULE_13_STR = rules.get("ICF_RULE_13_STR");
  public static final SQLEventRule ICF_RULE_13 = new SQLEventRule(getRuleDef(ICF_RULE_13_STR));

  public static final String ICF_RULE_56_STR = rules.get("ICF_RULE_56_STR");
  public static final SQLEventRule ICF_RULE_56 = new SQLEventRule(getRuleDef(ICF_RULE_56_STR));


  public static RuleDefinition getRuleDef(String sql) {
    RuleDefinition ruleDefinition = new RuleDefinition();
    ruleDefinition.setBizId(1L);
    ruleDefinition.setCategory("EVENT");
    ruleDefinition.setContent(sql);
    return ruleDefinition;
  }
}
