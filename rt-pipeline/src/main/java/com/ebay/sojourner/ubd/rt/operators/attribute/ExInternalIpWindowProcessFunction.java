package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseManager;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.IpSignatureBotDetector;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

public class ExInternalIpWindowProcessFunction
    extends ProcessWindowFunction<IpAttributeAccumulator, IpSignature, Tuple, TimeWindow> {
  private static final Logger logger = Logger.getLogger(ExInternalIpWindowProcessFunction.class);
  private static final String BUCKET_NAME = "botsignature";
  private static final String USER_NAME = "Administrator";
  private static final String USER_PASS = "111111";
  //    private IpSignature ipSignature;
  private IpSignatureBotDetector ipSignatureBotDetector;
  private CouchBaseManager couchBaseManager;

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<IpAttributeAccumulator> elements,
      Collector<IpSignature> out)
      throws Exception {

    IpAttributeAccumulator ipAttr = elements.iterator().next();
    if (ipAttr.getIpAttribute().getClientIp() != null) {
      Set<Integer> botFlagList = ipSignatureBotDetector.getBotFlagList(ipAttr.getIpAttribute());

      if (botFlagList != null && botFlagList.size() > 0) {
        //                ipSignature.setClientIp(ipAttr.getAttribute().getClientIp());
        //                ipSignature.setBotFlag(botFlagList);
        JsonObject ipSignature =
            JsonObject.create()
                .put("ip", ipAttr.getIpAttribute().getClientIp())
                .put("botFlag", JsonArray.from(botFlagList.toArray()));
        couchBaseManager.upsert(ipSignature, ipAttr.getIpAttribute().getClientIp());
        //            out.collect(ipSignature);
      }
    }
  }

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    ipSignatureBotDetector = IpSignatureBotDetector.getInstance();
    //        couchBaseManager = CouchBaseManager.getInstance();
  }

  @Override
  public void clear(Context context) throws Exception {
    super.clear(context);
    //        couchBaseManager.close();
  }
}
