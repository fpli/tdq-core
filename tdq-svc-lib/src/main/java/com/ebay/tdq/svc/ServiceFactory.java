package com.ebay.tdq.svc;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.tdq.config.ProntoConfig;
import com.ebay.tdq.service.ProfilerService;
import com.ebay.tdq.service.RuleEngineService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * env: tdq-profile=tdq-pre-prod|tdq-prod [default is test]
 *
 * @author juntzhang
 */
@Slf4j
public class ServiceFactory {
  protected static ProntoConfig prontoConfig;

  static {
    synchronized (ServiceFactory.class) {
      String profile = System.getenv(EnvironmentUtils.PROFILE);
      if (StringUtils.isNotBlank(profile)) {
        EnvironmentUtils.activateProfile(profile);
      }
      prontoConfig = new ProntoConfig();
    }
  }

  private static final RuleEngineService ruleEngineService = new RuleEngineServiceImpl();

  private static final ProfilerService profilerService = new ProfilerServiceImpl(restHighLevelClient());

  public static RuleEngineService getRuleEngine() {
    return ruleEngineService;
  }

  public static ProfilerService getProfiler() {
    return profilerService;
  }

  public static void close() throws IOException {
    ((ProfilerServiceImpl) profilerService).client.close();
  }

  protected static RestHighLevelClient restHighLevelClient() {
    RestClientBuilder builder = RestClient.builder(new HttpHost(
        prontoConfig.getHostname(), prontoConfig.getPort(), prontoConfig.getSchema()));
    if (StringUtils.isNotBlank(prontoConfig.getHostname())) {
      final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
      credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
          prontoConfig.getUsername(), prontoConfig.getPassword()));
      builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
        @Override
        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
          return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }
      });
    }

    RestHighLevelClient client = new RestHighLevelClient(builder);
    log.info("restHighLevelClient is initialized.");
    return client;
  }
}
