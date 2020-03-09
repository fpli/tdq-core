package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.RheosHeader;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

@Slf4j
public class RawEventDeserializationSchema implements DeserializationSchema<RawEvent> {

  @Override
  public RawEvent deserialize(byte[] message) throws IOException {
    long ingestTime = System.nanoTime();
    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);

    // Generate RheosHeader
    RheosHeader rheosHeader =
        new RheosHeader(
            rheosEvent.getEventCreateTimestamp(),
            rheosEvent.getEventSentTimestamp(),
            rheosEvent.getSchemaId(),
            rheosEvent.getEventId(),
            rheosEvent.getProducerId());

    // Generate sojA, sojK, sojC
    Map<Utf8, Utf8> sojA = (Map<Utf8, Utf8>) genericRecord.get("sojA");
    Map<Utf8, Utf8> sojK = (Map<Utf8, Utf8>) genericRecord.get("sojK");
    Map<Utf8, Utf8> sojC = (Map<Utf8, Utf8>) genericRecord.get("sojC");
    Map<String, String> sojAMap = new HashMap<>();
    if (sojA != null) {
      for (Map.Entry<Utf8, Utf8> entry : sojA.entrySet()) {
        sojAMap.put(entry.getKey().toString(), entry.getValue().toString());
      }
    }
    Map<String, String> sojKMap = new HashMap<>();
    if (sojK != null) {
      for (Map.Entry<Utf8, Utf8> entry : sojK.entrySet()) {
        sojKMap.put(entry.getKey().toString(), entry.getValue().toString());
      }
    }

    Map<String, String> sojCMap = new HashMap<>();
    if (sojC != null) {
      for (Map.Entry<Utf8, Utf8> entry : sojC.entrySet()) {
        sojCMap.put(entry.getKey().toString(), entry.getValue().toString());
      }
    }

    // Generate ClientData
    // If clientData is not of type GenericRecord, just skip this message.
    if (!(genericRecord.get("clientData") instanceof GenericRecord)) {
      log.info("clientData is not of type GenericRecord. " + genericRecord.get("clientData"));
      return null;
    }

    GenericRecord genericClientData = (GenericRecord) genericRecord.get("clientData");
    ClientData clientData = new ClientData();
    clientData.setForwardFor(getString(genericClientData.get("ForwardFor")));
    clientData.setScript(getString(genericClientData.get("Script")));
    clientData.setServer(getString(genericClientData.get("Server")));
    clientData.setTMachine(getString(genericClientData.get("TMachine")));
    clientData.setTStamp((Long) genericClientData.get("TStamp"));
    clientData.setTName(getString(genericClientData.get("TName")));
    clientData.setTPayload(getString(genericClientData.get("TPayload")));
    clientData.setColo(getString(genericClientData.get("colo")));
    clientData.setPool(getString(genericClientData.get("pool")));
    clientData.setAgent(getString(genericClientData.get("agent")));
    clientData.setRemoteIP(getString(genericClientData.get("remoteIP")));
    clientData.setTType(getString(genericClientData.get("TType")));
    clientData.setTStatus(getString(genericClientData.get("TStatus")));
    clientData.setCorrId(getString(genericClientData.get("corrId")));
    clientData.setContentLength((Integer) genericClientData.get("contentLength"));
    clientData.setNodeId(getString(genericClientData.get("nodeId")));
    clientData.setRequestGuid(getString(genericClientData.get("requestGuid")));
    clientData.setUrlQueryString(getString(genericClientData.get("urlQueryString")));
    clientData.setReferrer(getString(genericClientData.get("referrer")));
    clientData.setRlogid(getString(genericClientData.get("rlogid")));
    clientData.setAcceptEncoding(getString(genericClientData.get("acceptEncoding")));
    clientData.setTDuration((Long) genericClientData.get("TDuration"));

    return new RawEvent(rheosHeader, sojAMap, sojKMap, sojCMap, clientData, ingestTime);
  }

  private String getString(Object o) {
    return (o != null) ? o.toString() : null;
  }

  @Override
  public boolean isEndOfStream(RawEvent nextElement) {
    return false;
  }

  @Override
  public TypeInformation<RawEvent> getProducedType() {
    return TypeInformation.of(RawEvent.class);
  }
}
