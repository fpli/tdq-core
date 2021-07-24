package com.ebay.tdq.svc;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.tdq.common.env.ProntoEnv;
import com.ebay.tdq.service.FetchMetricsService;
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

  private static volatile ServiceFactory serviceFactory;
  private ProntoEnv prontoEnv;
  private RestHighLevelClient restHighLevelClient;
  private RuleEngineService ruleEngineService;
  private ProfilerService profilerService;
  private FetchMetricsService fetchMetricsService;

  private ServiceFactory() {
    initialize();
  }

  private void initialize() {
    if (restHighLevelClient != null) {
      try {
        restHighLevelClient.close();
      } catch (IOException e) {
        log.info(e.getMessage(), e);
      }
    }

    String profile = System.getenv(EnvironmentUtils.PROFILE);
    if (StringUtils.isNotBlank(profile)) {
      EnvironmentUtils.activateProfile(profile);
    }
    prontoEnv = new ProntoEnv();
    restHighLevelClient = restHighLevelClient();
    ruleEngineService = new RuleEngineServiceImpl();
    profilerService = new ProfilerServiceImpl(restHighLevelClient);
    fetchMetricsService = new FetchMetricsMsMtcServiceImpl(restHighLevelClient);
  }

  protected RestHighLevelClient restHighLevelClient() {
    RestClientBuilder builder = RestClient.builder(new HttpHost(
        prontoEnv.getHostname(), prontoEnv.getPort(), prontoEnv.getSchema()));
    if (StringUtils.isNotBlank(prontoEnv.getHostname())) {
      final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
      credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
          prontoEnv.getUsername(), prontoEnv.getPassword()));
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


  private static ServiceFactory getInstance() {
    if (serviceFactory == null) {
      synchronized (ServiceFactory.class) {
        if (serviceFactory == null) {
          serviceFactory = new ServiceFactory();
        }
      }
    }
    return serviceFactory;
  }

  public static RuleEngineService getRuleEngine() {
    return getInstance().ruleEngineService;
  }

  public static ProfilerService getProfiler() {
    return getInstance().profilerService;
  }

  @Deprecated
  public static FetchMetricsService getFetchMetricsService() {
    return getInstance().fetchMetricsService;
  }

  public static ProntoEnv getProntoEnv() {
    return getInstance().prontoEnv;
  }
}
