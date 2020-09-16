package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.business.ubd.util.CsvUtils;
import com.ebay.sojourner.business.ubd.util.GenerateClientData;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrafficSourceIdMetricsFromCsv {

  private List<UbiEvent> ubiEventList;
  private SessionAccumulator sessionAccumulator;
  private SessionMetrics sessionMetrics;
  Iterable<CSVRecord> records;

  public static final String FILE_NAME = "trafficsourceid_testcase.csv";
  public static final String[] FILE_HEADER = {"guid", "clickid", "siteid", "version", "pageid",
      "pagename", "eventtimestamp", "urlquerystring", "clientdata", "applicationpayload",
      "webserver", "userid",
      "itemid", "flags", "sqr", "currentimprid", "iframe", "agentinfo", "clientIP", "appid",
      "sid", "requestCorrelationId", "remoteIP", "eventFamily", "eventAction", "rlogid"};

  @BeforeEach
  public void setup() throws Exception {

    sessionAccumulator = new SessionAccumulator();
    sessionMetrics = SessionMetrics.getInstance();
    ubiEventList = new ArrayList<>();
    records = CsvUtils.readCsvFile(FILE_NAME, FILE_HEADER);

  }

  @Test
  public void test_TrafficSourceMetric() throws Exception {

    for (CSVRecord record : records) {
      UbiEvent ubiEvent = new UbiEvent();

      ubiEvent.setGuid(record.get("guid"));
      ubiEvent.setClickId(Integer.parseInt(record.get("clickid")));
      ubiEvent.setSiteId(Integer.parseInt(record.get("siteid")));
      ubiEvent.setVersion(Integer.parseInt(record.get("version")));
      ubiEvent.setPageId(Integer.parseInt(record.get("pageid")));
      ubiEvent.setPageName(record.get("pagename"));
      ubiEvent.setUrlQueryString(record.get("urlquerystring"));
      ubiEvent.setClientData(GenerateClientData.constructClientData(record.get("clientdata")));
      ubiEvent.setApplicationPayload(record.get("applicationpayload"));
      ubiEvent.setWebServer(record.get("webserver"));

      // appid
      if (record.get("appid") == null) {
        ubiEvent.setAppId(null);
      } else {
        ubiEvent.setAppId(Integer.parseInt(record.get("appid")));
      }

      ubiEvent.setRefererHash(null);
      ubiEvent.setEventTimestamp(
          Long.parseLong(SojTimestamp.getSojTimestamp(
              record.get("eventtimestamp").substring(0, 10) + " " + record.get("eventtimestamp")
                  .substring(11))));
      ubiEvent.setCookies(null);
      ubiEvent.setReferrer(null);
      ubiEvent.setUserId(record.get("userid"));

      // itemid
      if (record.get("itemid") == null) {
        ubiEvent.setItemId(null);
      } else {
        ubiEvent.setItemId(Long.parseLong(record.get("itemid")));
      }

      ubiEvent.setFlags(record.get("flags"));
      ubiEvent.setRdt(false);
      ubiEvent.setRegu(0);
      ubiEvent.setSqr(record.get("sqr"));
      ubiEvent.setStaticPageType(0);
      ubiEvent.setReservedForFuture(0);
      ubiEvent.setEventAttr(null);
      ubiEvent.setCurrentImprId(Long.parseLong(record.get("currentimprid")));
      ubiEvent.setSourceImprId(null);
      ubiEvent.setCobrand(6);

      // iframe
      if (Integer.parseInt(record.get("iframe")) == 0) {
        ubiEvent.setIframe(false);
      } else {
        ubiEvent.setIframe(true);
      }

      ubiEvent.setAgentInfo(record.get("agentinfo"));
      ubiEvent.setForwardedFor(null);
      ubiEvent.setClientIP(record.get("clientIP"));
      ubiEvent.setPartialValidPage(true);
      ubiEvent.setSid(record.get("sid"));
      ubiEvent.setRequestCorrelationId(record.get("requestCorrelationId"));
      ubiEvent.setRemoteIP(record.get("remoteIP"));
      ubiEvent.setRlogid(record.get("rlogid"));
      ubiEvent.setEventAction(record.get("eventAction"));
      ubiEvent.setEventFamily(record.get("eventFamily"));

      ubiEventList.add(ubiEvent);
    }

    sessionMetrics.init();
    sessionMetrics.start(sessionAccumulator);
    for (UbiEvent event : ubiEventList) {
      sessionMetrics.feed(event, sessionAccumulator);
    }
    sessionMetrics.end(sessionAccumulator);
    Assert.assertEquals(14, sessionAccumulator.getUbiSession().getTrafficSrcId());

  }
}
