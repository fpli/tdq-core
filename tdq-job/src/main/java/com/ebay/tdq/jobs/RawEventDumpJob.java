package com.ebay.tdq.jobs;

import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.planner.LkpManager;
import com.ebay.tdq.sources.RhsKafkaSourceBuilder;
import com.ebay.tdq.utils.TdqContext;

/**
 * @author juntzhang
 */
public class RawEventDumpJob {

  protected TdqContext tdqCxt;

  public static void main(String[] args) throws Exception {
    new RawEventDumpJob().submit(args);
  }

  public void submit(String[] args) throws Exception {
    tdqCxt = new TdqContext(args);

    TdqConfig tdqConfig = LkpManager.getInstance(
        tdqCxt.getTdqEnv().getJdbcEnv()).findTdqConfig(tdqCxt.getTdqEnv().getJobName());

    RhsKafkaSourceBuilder.dump(tdqConfig, tdqCxt);

    tdqCxt.getRhsEnv().execute(tdqCxt.getTdqEnv().getJobName());
  }

}
