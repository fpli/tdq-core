package com.ebay.tdq.jobs;

import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.sources.RhsKafkaSourceFactory;
import com.ebay.tdq.utils.TdqConfigManager;
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

    TdqConfig tdqConfig = TdqConfigManager.getInstance(
        tdqCxt.getTdqEnv()).findTdqConfig(tdqCxt.getTdqEnv().getJobName());

    RhsKafkaSourceFactory.dump(tdqConfig, tdqCxt);

    tdqCxt.getRhsEnv().execute(tdqCxt.getTdqEnv().getJobName());
  }

}
