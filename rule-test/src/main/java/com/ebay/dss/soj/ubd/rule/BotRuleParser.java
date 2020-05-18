package com.ebay.dss.soj.ubd.rule;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNumericLiteral;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

public class BotRuleParser {

  //This map should fetched from Zookeeper in future.
  private static Map<String, String> TABLE_NAME_MAP = ImmutableMap.of(
      "SOJ.IDL_EVENT", "DEFAULT.UBI_EVENT", "SOJ.IDL_SESSION", "DEFAULT.UBI_SESSION");
  private static Map<Integer, Integer> BOT_FLAG_MAP = ImmutableMap.of(1, 1001, 2, 1002);
  private SqlSelect sqlSelect;

  public BotRuleParser(String sql) {
    SqlParser parser = SqlParser.create(sql);
    try {
      this.sqlSelect = (SqlSelect) parser.parseQuery();
    } catch (SqlParseException e) {
      e.printStackTrace();
    }
  }

  public int getBotFlag() {
    for (SqlNode sqlNode : sqlSelect.getSelectList()) {
      SqlKind sqlKind = sqlNode.getKind();
      switch (sqlKind) {
        case LITERAL:
          return Integer.parseInt(((SqlNumericLiteral) sqlNode).toValue());
        case AS:
          return Integer.parseInt(((SqlBasicCall) sqlNode).getOperandList().get(0).toString());
      }
    }
    return 0;
  }

  public String getSourceTable() {
    SqlIdentifier sqlIdentifier = (SqlIdentifier) sqlSelect.getFrom();
    return TABLE_NAME_MAP.get(sqlIdentifier.toString());
  }
}
