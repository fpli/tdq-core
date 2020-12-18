package com.ebay.sojourner.common.util;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.apache.commons.io.Charsets;

@Slf4j
public class RestClient {

  public static final String X_AUTH_USERNAME_HEADER = "X-Auth-Username";
  public static final String X_AUTH_TOKEN_HEADER = "X-Auth-Token";

  private final String baseURL;
  private final OkHttpClient okHttpClient;

  public RestClient(String baseURL) {
    this.baseURL = baseURL;
    okHttpClient = new OkHttpClient.Builder().build();
  }

  public Response get(String api) throws IOException {
    String url = baseURL + api;

    Request request = new Builder()
        .url(url)
        .addHeader(X_AUTH_USERNAME_HEADER,
                   EnvironmentUtils.get(Property.REST_USERNAME))
        .addHeader(X_AUTH_TOKEN_HEADER,
                   Credentials.basic("sojourner", "sojourner", Charsets.UTF_8))
        .build();

    try {
      return okHttpClient.newCall(request).execute();
    } catch (IOException e) {
      log.error("Error when calling rest api");
      throw new IOException(e);
    }
  }

}
