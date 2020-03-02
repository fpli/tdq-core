package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.RheosHeader;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

@Slf4j
public class SojEventDeserializationSchema implements DeserializationSchema<RawEvent> {

  private static Map<String, String> fieldMap = new ConcurrentHashMap<>();

  static {
    fieldMap.put("appId", "app");
    fieldMap.put("itemId", "itm");
    fieldMap.put("refererHash", "r");
    fieldMap.put("regu", "regU");
    fieldMap.put("siid", "siid");
    fieldMap.put("siteId", "t");
    fieldMap.put("timestamp", "mtsts");
    fieldMap.put("userId", "u");
    fieldMap.put("sqr", "sQr");
    fieldMap.put("cookies", "cookies");
    fieldMap.put("cobrand", "cobrand");
  }

  @Override
  public RawEvent deserialize(byte[] message) throws IOException {

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

    //        genericRecord.put("rheosHeader",rheosHeader);

    // Generate sojA, sojK, sojC
    //        Map<Utf8, Utf8> sojA = (Map<Utf8, Utf8>) genericRecord.get("sojA");
    //        Map<Utf8, Utf8> sojK = (Map<Utf8, Utf8>) genericRecord.get("sojK");
    //        Map<Utf8, Utf8> sojC = (Map<Utf8, Utf8>) genericRecord.get("sojC");
    Map<String, String> sojAMap = new HashMap<>();
    //        if (sojA != null) {
    //            for (Map.Entry<Utf8, Utf8> entry : sojA.entrySet()) {
    //                sojAMap.put(entry.getKey().toString(), entry.getValue().toString());
    //            }
    //        }
    Map<String, String> sojKMap = new HashMap<>();
    //        if (sojK != null) {
    //            for (Map.Entry<Utf8, Utf8> entry : sojK.entrySet()) {
    //                sojKMap.put(entry.getKey().toString(), entry.getValue().toString());
    //            }
    //        }

    Map<String, String> sojCMap = new HashMap<>();
    //        if (sojC != null) {
    //            for (Map.Entry<Utf8, Utf8> entry : sojC.entrySet()) {
    //                sojCMap.put(entry.getKey().toString(), entry.getValue().toString());
    //            }
    //        }

    // Generate ClientData
    // If clientData is not of type GenericRecord, just skip this message.
    if (!(genericRecord.get("clientData") instanceof Map)) {
      log.info("clientData is not of type Map. " + genericRecord.get("clientData"));
      return null;
    }
    //        String appid=getString2(genericRecord.get("appId"));

    //        GenericRecord applicationPayload =  genericRecord.get("applicationPayload");
    Map<Utf8, Utf8> applicationPayloadUtf8 =
        (Map<Utf8, Utf8>) genericRecord.get("applicationPayload");
    Map<String, String> applicationPayload = new HashMap<>();
    if (applicationPayload != null) {
      for (Map.Entry<Utf8, Utf8> entry : applicationPayloadUtf8.entrySet()) {
        applicationPayload.put(getString(entry.getKey()), getString(entry.getValue().toString()));
      }
    }
    for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      String valueMid = getString(genericRecord.get(key));
      applicationPayload.put(value, valueMid);
    }
    sojAMap.putAll(applicationPayload);
    String agentFromAP = getString2(applicationPayload.get("Agent"));
    String agent = getString2(genericRecord.get("agentInfo"));
    String agentCLI = agent.length() > agentFromAP.length() ? agent : agentFromAP;
    Map<Utf8, Utf8> clientDataMapUtf8 = (Map<Utf8, Utf8>) genericRecord.get("clientData");
    Map<String, String> clientDataMap = new HashMap<>();
    if (clientDataMap != null) {
      for (Map.Entry<Utf8, Utf8> entry : clientDataMapUtf8.entrySet()) {
        clientDataMap.put(getString(entry.getKey()), getString(entry.getValue().toString()));
      }
    }

    ClientData clientData = new ClientData();
    clientData.setForwardFor(getString(clientDataMap.get("ForwardFor")));
    clientData.setScript(getString(clientDataMap.get("Script")));
    clientData.setServer(getString(clientDataMap.get("Server")));
    clientData.setTMachine(getString(clientDataMap.get("TMachine")));

    clientData.setTStamp(
        StringUtils.isEmpty(applicationPayload.get("timestamp"))
            ? null
            : Long.valueOf(getString(applicationPayload.get("timestamp"))));
    clientData.setTName(getString(clientDataMap.get("TName")));
    clientData.setTPayload(getString(clientDataMap.get("TPayload")));
    clientData.setColo(getString(applicationPayload.get("colo")));
    clientData.setPool(getString(applicationPayload.get("TPool")));

    clientData.setTType(getString(applicationPayload.get("TType")));
    clientData.setTStatus(getString(clientDataMap.get("TStatus")));
    clientData.setCorrId(getString(applicationPayload.get("corrId")));
    clientData.setContentLength(
        StringUtils.isEmpty(clientDataMap.get("ContentLength"))
            ? null
            : Integer.valueOf(getString(clientDataMap.get("ContentLength"))));
    clientData.setNodeId(getString(applicationPayload.get("nodeId")));
    clientData.setRequestGuid(getString(applicationPayload.get("requestGuid")));

    clientData.setReferrer(getString(applicationPayload.get("Referer")));

    clientData.setAcceptEncoding(getString(clientDataMap.get("Encoding")));
    clientData.setTDuration(
        StringUtils.isEmpty(clientDataMap.get("TDuration"))
            ? null
            : Long.valueOf(clientDataMap.get("TDuration")));

    clientData.setAgent(agentCLI);
    clientData.setRemoteIP(getString(genericRecord.get("remoteIP")));
    clientData.setRlogid(getString(genericRecord.get("rlogid")));
    clientData.setUrlQueryString(getString(applicationPayload.get("urlQueryString")));

    return new RawEvent(rheosHeader, sojAMap, sojKMap, sojCMap, clientData);
    //        return genericRecord;
  }

  private String getString(Object o) {
    return (o != null) ? o.toString() : null;
  }

  private String getString2(Object o) {
    return (o != null) ? o.toString() : "";
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
