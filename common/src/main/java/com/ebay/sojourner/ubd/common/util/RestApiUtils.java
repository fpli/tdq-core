package com.ebay.sojourner.ubd.common.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Slf4j
public class RestApiUtils {

  public static OkHttpClient getRestClient() {
    return new OkHttpClient();
  }

  public static Request buildRequest(String url) {
    return new Request.Builder()
        .url(url)
        .addHeader(Constants.AUTH_USERNAME_KEY, Constants.AUTH_USERNAME_VALUE)
        .addHeader(Constants.AUTH_TOKEN_KEY, Constants.AUTH_TOKEN_VALUE)
        .build();
  }
}
