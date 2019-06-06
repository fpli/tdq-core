package com.ebay.sojourner.ubd.rt.operators.signature;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import org.apache.flink.streaming.api.datastream.DataStream;

public class CouchbaseSignatureStore implements SignatureStore {
    @Override
    public void save(DataStream<IpSignature> signatureStream) {

    }

    @Override
    public IpSignature load(String ip) {
        return null;
    }
}
