package com.ebay.sojourner.batch.connector.common.session;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class SplitSessionProcessFunction extends ProcessFunction<SojSession, SojSession> {

  private static final String DATE_FORMAT = "yyyyMMdd";
  private static final String DEFAULT_DATE = "19700101";
  private DateTimeFormatter dateTimeFormatter;
  private OutputTag outputTag;

  public SplitSessionProcessFunction(OutputTag outputTag) {
    this.outputTag = outputTag;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    dateTimeFormatter = dateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZoneId.systemDefault());
  }

  @Override
  public void processElement(SojSession sojSession, Context context, Collector<SojSession> out)
      throws Exception {

    Long sessionEndTimestamp = SojTimestamp
        .getSojTimestampToUnixTimestamp(sojSession.getAbsEndTimestamp());
    Long sessionStartDt = sojSession.getSessionStartDt();

    String sessionEndTimeString = transferLongToDateString(sessionEndTimestamp);
    String sessionStartTimeString = transferLongToDateString(sessionStartDt);

    if (sessionStartTimeString.equals(sessionEndTimeString)) {
      out.collect(sojSession);
    } else {
      context.output(outputTag, sojSession);
    }
  }

  private String transferLongToDateString(Long time) {

    if (time > 0) {
      String defaultTsStr = dateTimeFormatter.format(Instant.ofEpochMilli(time));
      return defaultTsStr.substring(0, 8);
    } else {
      return DEFAULT_DATE;
    }
  }

  @Override
  public void close() throws Exception {
    super.close();
  }
}
