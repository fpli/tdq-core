package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.RheosHeader;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.log4j.Logger;

public class RawEventDeserializationSchemaForPA implements DeserializationSchema<GenericRecord> {

  private static final Logger logger = Logger.getLogger(RawEventDeserializationSchemaForPA.class);

  @Override
  public GenericRecord deserialize(byte[] message) throws IOException {
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

    genericRecord.put("rheosHeader", rheosHeader);
    //
    //        // Generate sojA, sojK, sojC
    //        Map<Utf8, Utf8> sojA = (Map<Utf8, Utf8>) genericRecord.get("sojA");
    //        Map<Utf8, Utf8> sojK = (Map<Utf8, Utf8>) genericRecord.get("sojK");
    //        Map<Utf8, Utf8> sojC = (Map<Utf8, Utf8>) genericRecord.get("sojC");
    //        Map<String, String> sojAMap = new HashMap<>();
    //        if (sojA != null) {
    //            for (Map.Entry<Utf8, Utf8> entry : sojA.entrySet()) {
    //                sojAMap.put(entry.getKey().toString(), entry.getValue().toString());
    //            }
    //        }
    //        Map<String, String> sojKMap = new HashMap<>();
    //        if (sojK != null) {
    //            for (Map.Entry<Utf8, Utf8> entry : sojK.entrySet()) {
    //                sojKMap.put(entry.getKey().toString(), entry.getValue().toString());
    //            }
    //        }
    //
    //        Map<String, String> sojCMap = new HashMap<>();
    //        if (sojC != null) {
    //            for (Map.Entry<Utf8, Utf8> entry : sojC.entrySet()) {
    //                sojCMap.put(entry.getKey().toString(), entry.getValue().toString());
    //            }
    //        }
    //
    //        // Generate ClientData
    //        // If clientData is not of type GenericRecord, just skip this message.
    //        if (!(genericRecord.get("clientData") instanceof GenericRecord)) {
    //            logger.info("clientData is not of type GenericRecord. "
    //                    + genericRecord.get("clientData"));
    //            return null;
    //        }
    //
    //        GenericRecord genericClientData = (GenericRecord) genericRecord.get("clientData");
    //        ClientData clientData = new ClientData();
    //        clientData.setForwardFor(getString(genericClientData.get("ForwardFor")));
    //        clientData.setScript(getString(genericClientData.get("Script")));
    //        clientData.setServer(getString(genericClientData.get("Server")));
    //        clientData.setTMachine(getString(genericClientData.get("TMachine")));
    //
    //        clientData.setTStamp((Long) genericClientData.get("TStamp"));
    //        clientData.setTName(getString(genericClientData.get("TName")));
    //        clientData.setTPayload(getString(genericClientData.get("TPayload")));
    //        clientData.setColo(null);
    //        clientData.setPool(null);
    //
    //        clientData.setTType(null);
    //        clientData.setTStatus(getString(genericClientData.get("TStatus")));
    //        clientData.setCorrId(null);
    //        clientData.setContentLength((Integer) genericClientData.get("contentLength"));
    //        clientData.setNodeId(null);
    //        clientData.setRequestGuid(null);
    //
    //        clientData.setReferrer(null);
    //
    //        clientData.setAcceptEncoding(null);
    //        clientData.setTDuration((Long) genericClientData.get("TDuration"));
    //
    //        clientData.setAgent(getString(genericRecord.get("agentInfo")));
    //        clientData.setRemoteIP(getString(genericRecord.get("remoteIP")));
    //        clientData.setRlogid(getString(genericRecord.get("rlogid")));
    //        clientData.setUrlQueryString(getString(genericRecord.get("urlQueryString")));
    //
    //         return new RawEvent(rheosHeader, sojAMap, sojKMap, sojCMap, clientData);
    return genericRecord;
  }

  private String getString(Object o) {
    return (o != null) ? o.toString() : null;
  }

  @Override
  public boolean isEndOfStream(GenericRecord nextElement) {
    return false;
  }

  @Override
  public TypeInformation<GenericRecord> getProducedType() {
    return TypeInformation.of(GenericRecord.class);
  }
}
