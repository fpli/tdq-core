package com.ebay.sojourner.distributor;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SojEventDispatcher {
  public static final ConcurrentHashMap<Integer, List<String>> mappings = new ConcurrentHashMap<>();
  public static ScheduledExecutorService scheduledExecutorService;

  static {
    mappings.put(
        3962,
        // Lists.newArrayList("behavior.trafficjam.custimized.3962"));
        Lists.newArrayList("behavior.total.new.customized.page.3962"));

    // mappings.put(
    //     3086,
    //     Lists.newArrayList("behavior.total.new.customized.rover.tracking.event"));
    //
    // mappings.put(SojournerCustomTopicJob
    //     3085,
    //     Lists.newArrayList("behavior.total.new.customized.rover.tracking.event"));
    //
    // mappings.put(
    //     3084,
    //     Lists.newArrayList("behavior.total.new.customized.rover.tracking.event"));

    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    scheduledExecutorService.scheduleWithFixedDelay(() -> {
      log.warn("calling rest api...");
    }, 10, 10, TimeUnit.SECONDS);
  }

  public static void close() {
    scheduledExecutorService.shutdown();
    // scheduledExecutorService = null;
  }
}
