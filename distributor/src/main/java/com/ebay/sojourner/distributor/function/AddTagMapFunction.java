package com.ebay.sojourner.distributor.function;

import com.ebay.sojourner.common.model.RawSojEventWrapper;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.flink.connector.kafka.AvroKafkaDeserializer;
import com.ebay.sojourner.flink.connector.kafka.AvroKafkaSerializer;
import com.ebay.sojourner.flink.connector.kafka.KafkaDeserializer;
import com.ebay.sojourner.flink.connector.kafka.KafkaSerializer;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

public class AddTagMapFunction extends RichMapFunction<RawSojEventWrapper, RawSojEventWrapper> {

  private transient KafkaDeserializer<SojEvent> deserializer;
  private transient KafkaSerializer<SojEvent> serializer;
  private static final String AGENT_TAG = "Agent";
  private static final String EVENT_TS_TAG = "EventTS";
  private static final String FORWARDED_FOR_TAG = "ForwardedFor";
  private static final String PAYLOAD_TAG = "Payload";
  private static final String REFERER_TAG = "Referer";
  private static final String REMOTE_IP_TAG = "RemoteIP";
  private static final String TPOOL_TAG = "TPool";
  private static final String TTYPE_TAG = "TType";
  private static final String BOTT_TAG = "bott";
  private static final String CBRND_TAG = "cbrnd";
  private static final String CLIENT_IP_TAG = "clientIP";
  private static final String CORRID_TAG = "corrId";
  private static final String CURRENT_IMPR_ID_TAG = "currentImprId";
  private static final String DD_BF_TAG = "dd_bf";
  private static final String DD_BV_TAG = "dd_bv";
  private static final String DD_D_TAG = "dd_d";
  private static final String DD_DC_TAG = "dd_dc";
  private static final String DD_OS_TAG = "dd_os";
  private static final String DD_OSV_TAG = "dd_osv";
  private static final String IFRM_TAG = "ifrm";
  private static final String JS_EV_MAK_TAG = "js_ev_mak";
  private static final String NODE_ID_TAG = "nodeId";
  private static final String PAGE_NAME_TAG = "pageName";
  private static final String RDT_TAG = "rdt";
  private static final String REQUEST_GUID_TAG = "requestGuid";
  private static final String RHEOS_UPSTREAM_CREATE_TS_TAG = "rheosUpstreamCreateTs";
  private static final String RHEOS_UPSTREAM_SEND_TS_TAG = "rheosUpstreamSendTs";
  private static final String RLOGID_TAG = "rlogid";
  private static final String RV_TAG = "rv";
  private static final String TIMESTAMP_TAG = "timestamp";
  private static final String URL_QUERY_STRING_TAG = "urlQueryString";

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    deserializer = new AvroKafkaDeserializer<>(SojEvent.class);
    serializer = new AvroKafkaSerializer<>(SojEvent.getClassSchema());
  }

  @Override
  public RawSojEventWrapper map(RawSojEventWrapper wrapper) throws Exception {
    // add JetStream backward compatible tags in 'applicationPayload' field
    try {
      SojEvent event = deserializer.decodeValue(wrapper.getPayload());
      Map<String, String> applicationPayload = event.getApplicationPayload();
      // add tags
      if (event.getClientData().containsKey("TStamp")) {
        applicationPayload.put(EVENT_TS_TAG, event.getClientData().get("TStamp"));
      }
      if (event.getClientData().containsKey("ForwardedFor")) {
        applicationPayload.put(FORWARDED_FOR_TAG, event.getClientData().get("ForwardedFor"));
      }
      if (event.getClientData().containsKey("TPool")) {
        applicationPayload.put(TPOOL_TAG, event.getClientData().get("TPool"));
      }
      if (event.getClientData().containsKey("TType")) {
        applicationPayload.put(TTYPE_TAG, event.getClientData().get("TType"));
      }
      if (event.getClientData().containsKey("corrId")) {
        applicationPayload.put(CORRID_TAG, event.getClientData().get("corrId"));
      }
      if (event.getClientData().containsKey("nodeId")) {
        applicationPayload.put(NODE_ID_TAG, event.getClientData().get("nodeId"));
      }
      if (event.getClientData().containsKey("reqeustGuid")) {
        applicationPayload.put(REQUEST_GUID_TAG, event.getClientData().get("reqeustGuid"));
      }

      // Referer tag is always there
      applicationPayload.put(REFERER_TAG, event.getClientData().getOrDefault("Referer", ""));

      if (StringUtils.isNotBlank(event.getAgentInfo())) {
        applicationPayload.put(AGENT_TAG, event.getAgentInfo());
      }
      if (StringUtils.isNotBlank(event.getUrlQueryString())) {
        applicationPayload.put(PAYLOAD_TAG, event.getUrlQueryString());
        applicationPayload.put(URL_QUERY_STRING_TAG, event.getUrlQueryString());
      }
      if (StringUtils.isNotBlank(event.getRemoteIP())) {
        applicationPayload.put(REMOTE_IP_TAG, event.getRemoteIP());
      }
      if (event.getBot() != null) {
        applicationPayload.put(BOTT_TAG, String.valueOf(event.getBot()));
      }
      if (StringUtils.isNotBlank(event.getCobrand())) {
        applicationPayload.put(CBRND_TAG, event.getCobrand());
      }
      if (StringUtils.isNotBlank(event.getClientIP())) {
        applicationPayload.put(CLIENT_IP_TAG, event.getClientIP());
      }
      if (event.getCurrentImprId() != null) {
        applicationPayload.put(CURRENT_IMPR_ID_TAG, String.valueOf(event.getCurrentImprId()));
      }
      if (StringUtils.isNotBlank(event.getBrowserFamily())) {
        applicationPayload.put(DD_BF_TAG, event.getBrowserFamily());
      }
      if (StringUtils.isNotBlank(event.getBrowserVersion())) {
        applicationPayload.put(DD_BV_TAG, event.getBrowserVersion());
      }
      if (StringUtils.isNotBlank(event.getDeviceFamily())) {
        applicationPayload.put(DD_D_TAG, event.getDeviceFamily());
      }
      if (StringUtils.isNotBlank(event.getDeviceType())) {
        applicationPayload.put(DD_DC_TAG, event.getDeviceType());
      }
      if (StringUtils.isNotBlank(event.getOsFamily())) {
        applicationPayload.put(DD_OS_TAG, event.getOsFamily());
      }
      if (StringUtils.isNotBlank(event.getEnrichedOsVersion())) {
        applicationPayload.put(DD_OSV_TAG, event.getEnrichedOsVersion());
      }
      if (event.getIframe() != null) {
        applicationPayload.put(IFRM_TAG, String.valueOf(event.getIframe()));
      }
      if (StringUtils.isNotBlank(event.getGuid())) {
        applicationPayload.put(JS_EV_MAK_TAG, event.getGuid());
      }
      if (StringUtils.isNotBlank(event.getPageName())) {
        applicationPayload.put(PAGE_NAME_TAG, event.getPageName());
      }
      if (event.getRdt() != null) {
        applicationPayload.put(RDT_TAG, String.valueOf(event.getRdt()));
      }
      if (StringUtils.isNotBlank(event.getRlogid())) {
        applicationPayload.put(RLOGID_TAG, event.getRlogid());
      }
      if (event.getRv() != null) {
        applicationPayload.put(RV_TAG, String.valueOf(event.getRv()));
      }
      if (event.getEventTimestamp() != null) {
        applicationPayload.put(TIMESTAMP_TAG, String.valueOf(event.getEventTimestamp()));
      }
      if (event.getRheosHeader().getEventCreateTimestamp() != null) {
        applicationPayload.put(RHEOS_UPSTREAM_CREATE_TS_TAG,
                               String.valueOf(event.getEventTimestamp()));
      }
      if (event.getRheosHeader().getEventSentTimestamp() != null) {
        applicationPayload.put(RHEOS_UPSTREAM_SEND_TS_TAG,
                               String.valueOf(System.currentTimeMillis()));
      }

      wrapper.setPayload(serializer.encodeValue(event));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return wrapper;
  }
}
