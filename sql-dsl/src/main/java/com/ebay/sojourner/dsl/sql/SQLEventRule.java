package com.ebay.sojourner.dsl.sql;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.dsl.domain.rule.RuleDefinition;

public class SQLEventRule extends AbstractSQLRule<UbiEvent, ReflectiveUbiEvent, Integer> {

  public SQLEventRule(RuleDefinition ruleDefinition) {
    super(ruleDefinition);
  }

  @Override
  protected SingleRecordDataSource<ReflectiveUbiEvent> getDataSource() {
    return new ReflectiveUbiEventDataSource();
  }

  @Override
  public Integer execute(UbiEvent ubiEvent) {
    ReflectiveUbiEvent reflectiveUbiEvent = new ReflectiveUbiEvent(ubiEvent);
    dataSource.update(reflectiveUbiEvent);

    int flag = 0;
    if (enumerable.any()) {
      Integer botFlag = enumerable.first();
      if (botFlag > 0) {
        flag = botFlag;
      }
    }

    return flag;
  }

  public class ReflectiveUbiEventDataSource implements SingleRecordDataSource<ReflectiveUbiEvent> {

    public ReflectiveUbiEvent[] idl_event = new ReflectiveUbiEvent[1];

    @Override
    public void update(ReflectiveUbiEvent reflectiveUbiEvent) {
      idl_event[0] = reflectiveUbiEvent;
    }
  }

}
