package com.ebay.sojourner.dsl.sql;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.model.rule.RuleDefinition;

public class SQLSessionRule extends AbstractSQLRule<UbiSession, ReflectiveUbiSession, Integer> {

  public SQLSessionRule(RuleDefinition ruleDefinition) {
    super(ruleDefinition);
  }

  @Override
  public Integer execute(UbiSession ubiSession) {

    ReflectiveUbiSession reflectiveUbiSession = new ReflectiveUbiSession(ubiSession);
    dataSource.update(reflectiveUbiSession);

    int flag = 0;
    if (enumerable.any()) {
      Integer botFlag = enumerable.first();
      if (botFlag > 0) {
        flag = botFlag;
      }
    }

    return flag;
  }

  @Override
  protected SingleRecordDataSource<ReflectiveUbiSession> getDataSource() {
    return new ReflectiveUbiSessionDataSource();
  }

  public class ReflectiveUbiSessionDataSource implements
      SingleRecordDataSource<ReflectiveUbiSession> {

    public ReflectiveUbiSession[] idl_session = new ReflectiveUbiSession[1];

    @Override
    public void update(ReflectiveUbiSession reflectiveUbiSession) {
      idl_session[0] = reflectiveUbiSession;
    }
  }
}
