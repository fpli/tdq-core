package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.env.EnvironmentUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Slf4j
public class RestClientUtils {

  public static OkHttpClient getRestClient() {
    return new OkHttpClient();
  }

  public static Request buildRequest(String url) {
    return new Request.Builder()
        .url(url)
        .addHeader(EnvironmentUtils.get(Constants.REST_AUTH_USERNAME_KEY),
            EnvironmentUtils.get(Constants.REST_AUTH_USERNAME_VALUE))
        .addHeader(EnvironmentUtils.get(Constants.REST_AUTH_TOKEN_KEY),
            EnvironmentUtils.get(Constants.REST_AUTH_TOKEN_VALUE))
        .build();
  }
}
