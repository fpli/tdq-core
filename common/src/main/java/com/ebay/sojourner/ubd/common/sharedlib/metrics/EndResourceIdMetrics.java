package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;

/**
 *                     select p as _exitPage, timestamp as _exitTime
 *                     from SOJEvent(p is not null and rdt = 0 and _ifrm = false and (session.string('_exitPage') is null or (session.long('_exitTime') is not null and timestamp - session.long('_exitTime') > 0)));
 *                     
 * select p as _exitPage from SOJEvent(p is not null and rdt = 0 and _ifrm = false);
 *
 * select ColonFieldUtil.extractField(r1, 1) as _exitPage, _ct as _exitTime
 * from PULSAREvent(et is not null and (et.startsWith('VIEW_') or et.startsWith('SERV_'))
 * and r1 is not null and (session.string('_exitPage') is null
 * or (session.long('_exitTime') is not null and _ct - session.long('_exitTime') > 0)));
 *
 *
 */
public class EndResourceIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void init() throws Exception {

  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {

  }

  @Override
  public void feed(UbiEvent ubiEvent, SessionAccumulator sessionAccumulator) throws Exception {

  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {

  }
}
