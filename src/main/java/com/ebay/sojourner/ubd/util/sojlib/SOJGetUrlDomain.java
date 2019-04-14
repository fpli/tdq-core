package com.ebay.sojourner.ubd.util.sojlib;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;

public class SOJGetUrlDomain {

    /*
     * this function is to get domain/host from a url string
     */
    public static String getUrlDomain(String urlString) {
        if (StringUtils.isBlank(urlString))
            return "";

        URL url;

        try {
            url = new URL(urlString);
        } catch (IOException e) {
            return "";
        }

        return url.getHost();
    }
}
