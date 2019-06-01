package com.ebay.sojourner.ubd.rt.operators.attrubite;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.model.IpSignature;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class IpWindowProcessFunction
        extends ProcessWindowFunction<IpAttribute, IpSignature, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(IpWindowProcessFunction.class);
    private IpSignature ipSignature;

    @Override
    public void process(Tuple tuple, Context context, Iterable<IpAttribute> elements,
                        Collector<IpSignature> out) throws Exception {

        IpAttribute ipAttr = elements.iterator().next();
        Set<Integer> ipBotFlags = new HashSet<Integer>();
        ipSignature = new IpSignature();
        if (ipAttr.getSingleClickSessionCount() > 20) {
            ipSignature.setClientIp(ipAttr.getClientIp());
            ipBotFlags.add(7);
        }
        ipSignature.setBotFlag(ipBotFlags);
        out.collect(ipSignature);
    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);

    }
}
