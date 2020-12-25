package com.ebay.sojourner.distributor.function;

import com.ebay.sojourner.common.model.PageIdTopicMapping;
import com.ebay.sojourner.common.util.RestClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

@Slf4j
public class MappingSourceFunction extends RichSourceFunction<PageIdTopicMapping> {

  private RestClient restClient;
  private ObjectMapper objectMapper;
  private final String baseURL;
  private final Long interval;

  public MappingSourceFunction(String baseURL, Long interval) {
    this.baseURL = baseURL;
    this.interval = interval;
  }

  @Override
  public void run(SourceContext<PageIdTopicMapping> ctx) throws Exception {
    while (true) {
      try {
        Response response = restClient.get("/api/custom_topic_config");
        List<PageIdTopicMapping> configs =
            objectMapper
            .reader()
            .forType(new TypeReference<List<PageIdTopicMapping>>() {})
            .readValue(response.body().string());

        for (PageIdTopicMapping config : configs) {
          ctx.collect(config);
        }
      } catch (Exception e) {
        log.error("Error when calling rest api");
      }

      Thread.sleep(interval);
    }
  }

  @Override
  public void cancel() {
    log.info("MappingSourceFunction cancelled");
    restClient = null;
    objectMapper = null;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    this.restClient = new RestClient(baseURL);
    this.objectMapper = new ObjectMapper();
  }
}
