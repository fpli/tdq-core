package com.ebay.sojourner.dsl.sql;

import com.ebay.sojourner.common.model.UbiEvent;
import org.apache.calcite.interpreter.Interpreter;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.tools.Planner;

public class SqlInterpreterEventRule extends SqlEventRule {

  private RelNode relTree;
  private Interpreter interpreter;

  public SqlInterpreterEventRule(String sql) {
    super(sql);
  }

  protected void prepareSql(String sql) {
    try {
      Planner planner = dataContext.getPlanner();
      SqlNode parseTree = planner.parse(sql);
      SqlNode validated = planner.validate(parseTree);
      RelNode relTree = planner.rel(validated).rel;
      this.relTree = relTree;
      this.interpreter = new Interpreter(dataContext, relTree);
    } catch (Exception e) {
      throw new RuntimeException("Failed to prepare SQL: " + sql, e);
    }
  }

  @Override
  public int getBotFlag(UbiEvent ubiEvent) {
    int botFlag = 0;
    try {
      dataSource.updateData(
          new SojReflectiveEvent.Builder()
              .agentInfo(ubiEvent.getAgentInfo())
              .icfBinary(ubiEvent.getIcfBinary())
              .build());
      interpreter = new Interpreter(dataContext, relTree);
      if (interpreter.any()) {
        botFlag = (int) interpreter.first()[0];
      }
    } catch (Exception e) {
      botFlag = 0;
      LOGGER.warn("Fail to get bot flag.", e);
    }
    return botFlag;
  }
}
