package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseManager;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.AgentSignatureBotDetector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

import java.util.Set;

public class AgentWindowProcessFunction
        extends ProcessWindowFunction<AgentAttributeAccumulator, AttributeSignature, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(AgentWindowProcessFunction.class);
    private AttributeSignature agentSignature;
    private AgentSignatureBotDetector agentSignatureBotDetector;
//    private CouchBaseManager couchBaseManager;
//    private static final String BUCKET_NAME = "botsignature";
//    private static final String USER_NAME = "Administrator";
//    private static final String USER_PASS = "111111";

    @Override
    public void process(Tuple tuple, Context context, Iterable<AgentAttributeAccumulator> elements,
                        Collector<AttributeSignature> out) throws Exception {

        AgentAttributeAccumulator agentAttributeAccumulator = elements.iterator().next();
//        System.out.println(agentAttributeAccumulator.getAgentAttribute());
        Set<Integer> botFlagList = agentSignatureBotDetector.getBotFlagList(agentAttributeAccumulator.getAgentAttribute());

        if (botFlagList != null && botFlagList.size() > 0) {
//            JsonObject agentSignature = JsonObject.create()
//                    .put("agent", agentAttributeAccumulator.getAgentAttribute().getAgent())
//                    .put("botFlag", JsonArray.from(botFlagList.toArray()));
//            couchBaseManager.upsert(agentSignature, agentAttributeAccumulator.getAttribute().getAgent());
//            out.collect(ipSignature);
            agentSignature.getAttributeSignature().put("agent" + agentAttributeAccumulator.getAgentAttribute().getAgent(),botFlagList);
            out.collect(agentSignature);
        }


    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
        agentSignatureBotDetector = AgentSignatureBotDetector.getInstance();
        agentSignature = new AttributeSignature();
//        couchBaseManager = CouchBaseManager.getInstance();
    }

    @Override
    public void clear(Context context) throws Exception {
        super.clear(context);
//        couchBaseManager.close();
    }
}
