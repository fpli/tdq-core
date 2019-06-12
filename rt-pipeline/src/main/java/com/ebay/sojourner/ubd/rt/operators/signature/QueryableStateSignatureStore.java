package com.ebay.sojourner.ubd.rt.operators.signature;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;

public class QueryableStateSignatureStore implements SignatureStore {
    @Override
    public void save(DataStream<IpSignature> signatureStream) {
        signatureStream
                .keyBy("clientIp")
                .asQueryableState("bot7",
                        new ValueStateDescriptor<>("", TypeInformation.of(
                                new TypeHint<IpSignature>() {
                                }
                        ))
                );
    }

    @Override
    public IpSignature load(String ip) {
        return null;
    }
}
