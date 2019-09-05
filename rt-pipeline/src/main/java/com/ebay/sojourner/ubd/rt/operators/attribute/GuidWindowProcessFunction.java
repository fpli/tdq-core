package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.ebay.sojourner.ubd.common.model.GuidAttribute;
import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseManager;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.GuidSignatureBotDetector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

import java.util.Set;

public class GuidWindowProcessFunction
        extends ProcessWindowFunction<GuidAttributeAccumulator, GuidAttribute, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(GuidWindowProcessFunction.class);
    //    private IpSignature ipSignature;
    private GuidSignatureBotDetector guidSignatureBotDetector;
    private CouchBaseManager couchBaseManager;
    private static final String BUCKET_NAME = "botsignature";
    private static final String USER_NAME = "Administrator";
    private static final String USER_PASS = "111111";

    @Override
    public void process(Tuple tuple, Context context, Iterable<GuidAttributeAccumulator> elements,
                        Collector<GuidAttribute> out) throws Exception {

        GuidAttributeAccumulator guidAttributeAccumulator = elements.iterator().next();

            Set<Integer> botFlagList = guidSignatureBotDetector.getBotFlagList(guidAttributeAccumulator.getAttribute());

//            if (botFlagList != null && botFlagList.size() > 0) {
//                JsonObject guidSignature = JsonObject.create()
//                        .put("guid", guidAttributeAccumulator.getAttribute().getGuid())
//                        .put("botFlag", JsonArray.from(botFlagList.toArray()));
//                couchBaseManager.upsert(guidSignature, guidAttributeAccumulator.getAttribute().getGuid());
//            }


    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
        guidSignatureBotDetector = GuidSignatureBotDetector.getInstance();
//        couchBaseManager = CouchBaseManager.getInstance();
    }

    @Override
    public void clear(Context context) throws Exception {
        super.clear(context);
//        couchBaseManager.close();
    }
}
