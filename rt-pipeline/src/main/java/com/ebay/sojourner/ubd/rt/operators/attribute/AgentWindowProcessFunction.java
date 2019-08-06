package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.model.AgentAttributeAccumulator;
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
        extends ProcessWindowFunction<AgentAttributeAccumulator, AgentAttribute, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(AgentWindowProcessFunction.class);
    //    private IpSignature ipSignature;
    private AgentSignatureBotDetector agentSignatureBotDetector;
    private CouchBaseManager couchBaseManager;
    private static final String BUCKET_NAME = "botsignature";
    private static final String USER_NAME = "Administrator";
    private static final String USER_PASS = "111111";

    @Override
    public void process(Tuple tuple, Context context, Iterable<AgentAttributeAccumulator> elements,
                        Collector<AgentAttribute> out) throws Exception {

        AgentAttributeAccumulator agentAttributeAccumulator = elements.iterator().next();

        Set<Integer> botFlagList = agentSignatureBotDetector.getBotFlagList(agentAttributeAccumulator.getAttribute());

        if (botFlagList != null && botFlagList.size() > 0) {
            JsonObject agentSignature = JsonObject.create()
                    .put("agent", agentAttributeAccumulator.getAttribute().getAgent())
                    .put("botFlag", JsonArray.from(botFlagList.toArray()));
            couchBaseManager.upsert(agentSignature, agentAttributeAccumulator.getAttribute().getAgent());
//            out.collect(ipSignature);
        }


    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
        agentSignatureBotDetector = AgentSignatureBotDetector.getInstance();
        couchBaseManager = CouchBaseManager.getInstance();
    }

    @Override
    public void clear(Context context) throws Exception {
        super.clear(context);
        couchBaseManager.close();
    }
}
