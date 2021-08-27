package com.ebay.tdq.svc;

import java.io.File;
import java.util.Collections;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.transport.Netty4Plugin;
import org.junit.rules.TemporaryFolder;

/**
 * @author juntzhang
 */
public class EmbeddedElasticsearch {
  private final TemporaryFolder tempFolder = new TemporaryFolder();
  private Node node;

  public void start(String clusterName) throws Exception {
    this.tempFolder.create();
    File tmpDataFolder = this.tempFolder.newFolder();
    if (node == null) {
      Settings settings = Settings.builder()
          .put("cluster.name", clusterName)
          .put("http.cors.enabled", true)
          .put("path.home", tmpDataFolder.getParent())
          .put("path.data", tmpDataFolder.getAbsolutePath())
          .build();

      node = new PluginNode(settings);
      node.start();
    }
  }

  public void close() throws Exception {
    if (node != null && !node.isClosed()) {
      node.close();
      this.tempFolder.delete();
      node = null;
    }
  }

  public Client getClient() {
    if (node != null && !node.isClosed()) {
      return node.client();
    } else {
      return null;
    }
  }

  private static class PluginNode extends Node {
    public PluginNode(Settings settings) {
      super(InternalSettingsPreparer.prepareEnvironment(settings, Collections.emptyMap(), null, () -> "node1"),
          Collections.singletonList(Netty4Plugin.class), true);
    }
  }
}
