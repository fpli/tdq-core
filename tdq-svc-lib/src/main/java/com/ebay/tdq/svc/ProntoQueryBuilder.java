package com.ebay.tdq.svc;

import java.util.ArrayList;
import java.util.List;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlCharStringLiteral;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlNumericLiteral;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author juntzhang
 */
public class ProntoQueryBuilder {

  public BoolQueryBuilder rootBuilder;
  public SqlNode sqlNode;

  public ProntoQueryBuilder(BoolQueryBuilder rootBuilder, SqlNode sqlNode) {
    this.rootBuilder = rootBuilder;
    this.sqlNode = sqlNode;
  }

  public void set(String operatorName, List<Object> operands) {
    switch (operatorName.toUpperCase()) {
      case ">":
        rootBuilder.must(QueryBuilders.rangeQuery((String) operands.get(0)).gt(operands.get(1)));
        break;
      case ">=":
        rootBuilder.must(QueryBuilders.rangeQuery((String) operands.get(0)).gte(operands.get(1)));
        break;
      case "<":
        rootBuilder.must(QueryBuilders.rangeQuery((String) operands.get(0)).lt(operands.get(1)));
        break;
      case "<=":
        rootBuilder.must(QueryBuilders.rangeQuery((String) operands.get(0)).lte(operands.get(1)));
        break;
      case "IN":
        rootBuilder.must(QueryBuilders.termsQuery((String) operands.get(0), operands.subList(1, operands.size())));
        break;
      case "NOT IN":
        rootBuilder.mustNot(QueryBuilders.termsQuery((String) operands.get(0), operands.subList(1, operands.size())));
        break;
      case "AND":
        for (Object operand : operands) {
          parseSqlBasicCall((SqlBasicCall) operand);
        }
        break;
      default:
        throw new IllegalStateException("Unexpected operator: " + operatorName);
    }
  }

  public void submit() {
    if (sqlNode == null) {
      return;
    }
    parseSqlBasicCall((SqlBasicCall) sqlNode);
  }

  public void parseSqlBasicCall(SqlBasicCall call) {
    String operator = call.getOperator().getName();
    set(operator, transformOperands(call.getOperands()));
  }

  private List<Object> transformOperands(SqlNode[] operands) {
    List<Object> ans = new ArrayList<>();
    for (SqlNode node : operands) {
      if (node instanceof SqlBasicCall) {
        parseSqlBasicCall((SqlBasicCall) node);
      } else if (node instanceof SqlLiteral) {
        ans.add(transformLiteral((SqlLiteral) node));
      } else if (node instanceof SqlNodeList) {
        for (SqlNode n : ((SqlNodeList) node).getList()) {
          ans.add(transformLiteral((SqlLiteral) n));
        }
      } else if (node instanceof SqlIdentifier) {
        ans.add(transformSqlIdentifier((SqlIdentifier) node));
      } else {
        throw new IllegalStateException("Unexpected SqlCall: " + node);
      }
    }
    return ans;
  }

  private Object transformSqlIdentifier(SqlIdentifier identifier) {
    return identifier.toString();
  }

  private Object transformLiteral(SqlLiteral literal) {
    if (literal instanceof SqlNumericLiteral) {
      return literal.getValueAs(Number.class);
    } else if (literal instanceof SqlCharStringLiteral) {
      return literal.getValueAs(String.class);
    } else {
      throw new IllegalStateException("Unexpected literal: " + literal);
    }
  }
}
