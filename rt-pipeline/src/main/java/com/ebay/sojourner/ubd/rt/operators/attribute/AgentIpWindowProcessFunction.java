package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseManager;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.AgentIpSignatureBotDetector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

import java.util.Set;

public class AgentIpWindowProcessFunction
        extends ProcessWindowFunction<AgentIpAttributeAccumulator, AgentIpAttribute, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(AgentIpWindowProcessFunction.class);
    //    private IpSignature ipSignature;
    private AgentIpSignatureBotDetector agentIpSignatureBotDetector;
    private CouchBaseManager couchBaseManager;
    private static final String BUCKET_NAME = "botsignature";
    private static final String USER_NAME = "Administrator";
    private static final String USER_PASS = "111111";

    @Override
    public void process(Tuple tuple, Context context, Iterable<AgentIpAttributeAccumulator> elements,
                        Collector<AgentIpAttribute> out) throws Exception {

        AgentIpAttributeAccumulator agentIpAttributeAccumulator = elements.iterator().next();

        Set<Integer> botFlagList = agentIpSignatureBotDetector.getBotFlagList(agentIpAttributeAccumulator.getAttribute());

        if (botFlagList != null && botFlagList.size() > 0) {
            JsonObject ipSignature = JsonObject.create()
                    .put("agentIp", agentIpAttributeAccumulator.getAttribute().getAgent()+agentIpAttributeAccumulator.getAttribute().getClientIp())
                    .put("botFlag", JsonArray.from(botFlagList.toArray()));
            couchBaseManager.upsert(ipSignature, agentIpAttributeAccumulator.getAttribute().getAgent()+agentIpAttributeAccumulator.getAttribute().getClientIp());
        }

        out.collect(agentIpAttributeAccumulator.getAttribute());


    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
        agentIpSignatureBotDetector = AgentIpSignatureBotDetector.getInstance();
        couchBaseManager = CouchBaseManager.getInstance();
    }

    @Override
    public void clear(Context context) throws Exception {
        super.clear(context);
        couchBaseManager.close();
    }
}
