package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.ebay.sojourner.ubd.common.model.AttributeSignature;
import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IpSignature;

import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseManager;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.IpSignatureBotDetector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

import java.util.Set;

public class IpWindowProcessFunction
        extends ProcessWindowFunction<IpAttributeAccumulator, AttributeSignature, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(IpWindowProcessFunction.class);
    private AttributeSignature ipSignature;
    private IpSignatureBotDetector ipSignatureBotDetector;
//    private CouchBaseManager couchBaseManager;
//    private static final String BUCKET_NAME = "botsignature";
//    private static final String USER_NAME = "Administrator";
//    private static final String USER_PASS = "111111";

    @Override
    public void process(Tuple tuple, Context context, Iterable<IpAttributeAccumulator> elements,
                        Collector<AttributeSignature> out) throws Exception {

        IpAttributeAccumulator ipAttributeAccumulator = elements.iterator().next();
        if (ipAttributeAccumulator.getIpAttribute().getClientIp() != null) {
            Set<Integer> botFlagList = ipSignatureBotDetector.getBotFlagList(ipAttributeAccumulator.getIpAttribute());

            if (botFlagList != null && botFlagList.size() > 0) {
//                JsonObject ipSignature = JsonObject.create()
//                        .put("ip", ipAttr.getAttribute().getClientIp())
//                        .put("botFlag", JsonArray.from(botFlagList.toArray()));
//                couchBaseManager.upsert(ipSignature, ipAttr.getAttribute().getClientIp());
                ipSignature.getAttributeSignature().put("ip" + ipAttributeAccumulator.getIpAttribute().getClientIp(),botFlagList);
                out.collect(ipSignature);
            }
        }


    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
        ipSignatureBotDetector = IpSignatureBotDetector.getInstance();
        ipSignature = new AttributeSignature();
//        couchBaseManager = CouchBaseManager.getInstance();
    }

    @Override
    public void clear(Context context) throws Exception {
        super.clear(context);
//        couchBaseManager.close();
    }
}
