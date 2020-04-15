package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.adapter.enumerable.EnumerableInterpretable;
import org.apache.calcite.adapter.enumerable.EnumerableRel;
import org.apache.calcite.adapter.enumerable.EnumerableRel.Prefer;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.runtime.Bindable;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.tools.Planner;

public class SqlCompilerEventRule extends SqlEventRule {

  private Bindable bindable;

  public SqlCompilerEventRule(String sql) {
    super(sql);
  }

  public SqlCompilerEventRule(String sql, long ruleId, int version, String category) {
    super(sql, ruleId, version, category);
  }

  @Override
  protected void prepareSql(String sql) {
    try {
      Planner planner = dataContext.getPlanner();
      SqlNode parseTree = planner.parse(sql);
      SqlNode validated = planner.validate(parseTree);
      RelNode relTree = planner.rel(validated).rel;
      RelTraitSet traitSet = planner.getEmptyTraitSet().replace(EnumerableConvention.INSTANCE);
      RelNode optimized = planner.transform(0, traitSet, relTree);

      Map<String, Object> internalParameters =
          new LinkedHashMap<>();
      Prefer prefer = EnumerableRel.Prefer.ARRAY;
      // prefer = EnumerableRel.Prefer.CUSTOM;
      bindable = EnumerableInterpretable
          .toBindable(internalParameters, null, (EnumerableRel) optimized, prefer);
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
      Enumerable enumerable = bindable.bind(dataContext);
      if (enumerable.any()) {
        botFlag = (int) enumerable.first();
      }
    } catch (Exception e) {
      botFlag = 0;
      LOGGER.warn("Fail to get bot flag.", e);
    }
    return botFlag;
  }
}
