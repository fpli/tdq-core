package com.ebay.sojourner.ubd.rt.operators.sessionizer;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

import java.util.Iterator;


public class IPWindowProcessFunction
        extends ProcessWindowFunction<UbiSession, IpSignature, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(IPWindowProcessFunction.class);
    private IpSignature ipSignature;

    @Override
    public void process(Tuple tuple, Context context, Iterable<UbiSession> elements,
                        Collector<IpSignature> out) throws Exception {

        int singleClickSessionCount = 0;

        String clientIp = null;
        if (elements != null) {
            Iterator<UbiSession> ubiSessionIterator = elements.iterator();

            while (ubiSessionIterator.hasNext()) {

                UbiSession ubiSession = ubiSessionIterator.next();
                if (clientIp == null) {
                    clientIp = ubiSession.getClientIp();
                }
                if (Boolean.TRUE.equals(ubiSession.getSingleClickSessionFlag())) {
                    singleClickSessionCount++;
                }
            }

        }
        if (singleClickSessionCount > 20) {
            ipSignature = new IpSignature();
            ipSignature.setClientIp(clientIp);
            ipSignature.setBotFlag(7);
            out.collect(ipSignature);
        }

    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);

    }
}
