package com.ebay.sojourner.ubd.rt.operators.signature;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import org.apache.flink.streaming.api.datastream.DataStream;

public interface SignatureStore {

    void save(DataStream<IpSignature> signatureStream);

    IpSignature load(String ip);

}
