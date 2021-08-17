package com.ebay.tdq.svc;

import static com.ebay.sojourner.common.env.EnvironmentUtils.getStringWithPattern;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.tdq.common.env.ProntoEnv;
import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.service.ProfilerService;
import com.ebay.tdq.service.RuleEngineService;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
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
  private TdqEnv tdqEnv;
  private ProntoEnv prontoEnv;
  private String indexPattern;
  private RestHighLevelClient restHighLevelClient;
  private RuleEngineService ruleEngineService;
  private ProfilerService profilerService;

  private ServiceFactory() {
    initialize();
  }

  private void initialize() {
    close0();
    String profile = System.getenv(EnvironmentUtils.PROFILE);
    if (StringUtils.isNotBlank(profile)) {
      EnvironmentUtils.activateProfile(profile);
    }
    tdqEnv = new TdqEnv();
    prontoEnv = tdqEnv.getProntoEnv();
    indexPattern = getStringWithPattern("pronto.index-pattern");
    restHighLevelClient = restHighLevelClient();
    ruleEngineService = new RuleEngineServiceImpl();
    profilerService = new ProfilerServiceImpl(restHighLevelClient);
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

  private String getIndexDateSuffix(long ts, TimeZone timeZone) {
    return FastDateFormat.getInstance("yyyy-MM-dd", timeZone).format(ts);
  }

  private String getNormalMetricIndex0(Long eventTime) {
    return indexPattern + getIndexDateSuffix(eventTime, tdqEnv.getTimeZone());
  }

  private void close0() {
    if (restHighLevelClient != null) {
      try {
        restHighLevelClient.close();
      } catch (IOException e) {
        log.info(e.getMessage(), e);
      }
    }
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

  public static TdqEnv getTdqEnv() {
    return getInstance().tdqEnv;
  }

  @VisibleForTesting
  public static String getIndexPattern() {
    return getInstance().indexPattern;
  }

  public static String getNormalMetricIndex(long t) {
    return getInstance().getNormalMetricIndex0(t);
  }

  // close when jvm is shutdown
  public static void close() {
    getInstance().close0();
  }

  // reconnect when es client is closed
  public static synchronized void refresh() {
    serviceFactory = new ServiceFactory();
  }
}
