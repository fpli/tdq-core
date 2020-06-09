package com.ebay.sojourner.common.util;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Slf4j
public class RestClientUtils {

  public static final String X_AUTH_USERNAME_HEADER = "X-Auth-Username";
  public static final String X_AUTH_TOKEN_HEADER = "X-Auth-Token";

  public static OkHttpClient getRestClient() {
    return new OkHttpClient();
  }

  public static Request buildRequest(String url) {
    return new Request.Builder()
        .url(url)
        .addHeader(X_AUTH_USERNAME_HEADER,
            EnvironmentUtils.get(Property.REST_AUTH_USERNAME))
        .addHeader(X_AUTH_TOKEN_HEADER,
            EnvironmentUtils.get(Property.REST_AUTH_TOKEN))
        .build();
  }
}
