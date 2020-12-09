package com.ebay.sojourner.distributor;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

import com.ebay.sojourner.common.model.PageIdTopicMapping;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.RestClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.collections.MapUtils;

@Slf4j
public class SojEventDispatcher {
  public static final ConcurrentHashMap<Integer, List<String>> mappings = new ConcurrentHashMap<>();
  private static final RestClient restClient = new RestClient(getString(Property.REST_BASE_URL));
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static ScheduledExecutorService scheduledExecutorService;

  static {
    loadMappings();

    if (MapUtils.isEmpty(mappings)) {
      throw new IllegalStateException("PageId topic mapping config is empty");
    }

    log.info("Loaded {} mappings: {}", mappings.size(), mappings);

    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    scheduledExecutorService.scheduleWithFixedDelay(() -> {
      log.info("Scheduled to update pageId topic mappings configs.");
      loadMappings();
    }, 30, 30, TimeUnit.SECONDS);
  }

  private static void loadMappings() {
    try {
      Response response = restClient.get("/api/custom_topic_config");
      List<PageIdTopicMapping> configs = objectMapper
          .reader()
          .forType(new TypeReference<List<PageIdTopicMapping>>() {})
          .readValue(response.body().string());

      for (PageIdTopicMapping config : configs) {
        mappings.put(config.getPageId(), config.getTopics());
      }

    } catch (IOException e) {
      log.error("Error when calling rest api");
    }
  }

  public static void close() {
    scheduledExecutorService.shutdown();
    // scheduledExecutorService = null;
  }
}
