package org.apache.flink.streaming.connectors.elasticsearch;

import org.apache.flink.streaming.connectors.elasticsearch.testutils.ElasticsearchResource;

/**
 * @author juntzhang
 */
public class TdqElasticsearchResource extends ElasticsearchResource {
  public TdqElasticsearchResource(String clusterName) {
    super(clusterName);
  }

  public void start() throws Throwable {
    before();
  }

  public void stop() {
    super.after();
  }
}
