package com.ebay.sojourner.ubd.rt.operators.attrubite;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.apache.flink.api.common.functions.AggregateFunction;

public class IpAttributeAgg implements AggregateFunction<UbiSession, IpAttribute, IpAttribute> {
    @Override
    public IpAttribute createAccumulator() {
        return new IpAttribute();
    }

    @Override
    public IpAttribute add(UbiSession session, IpAttribute ipAttr) {
        if (ipAttr.getClientIp() == null) {
            ipAttr.setClientIp(session.getClientIp());
        }
        if (Boolean.TRUE.equals(session.getSingleClickSessionFlag())) {
            ipAttr.setSingleClickSessionCount(ipAttr.getSingleClickSessionCount() + 1);
        }
        return ipAttr;
    }

    @Override
    public IpAttribute getResult(IpAttribute ipAttr) {
        return ipAttr;
    }

    @Override
    public IpAttribute merge(IpAttribute a, IpAttribute b) {
        return null;
    }
}
