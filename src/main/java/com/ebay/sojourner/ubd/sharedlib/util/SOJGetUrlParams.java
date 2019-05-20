package com.ebay.sojourner.ubd.sharedlib.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

public class SOJGetUrlParams {
    /**
     * 
     * @param urlString
     * @return 
     * @throws IOException
     * replace with getUrlParams
     */
    @Deprecated
    public static String getUrlParams1(String urlString) throws IOException {
        if (StringUtils.isBlank(urlString))
            return "";

        URL url = new URL(urlString);
        StringBuilder sb = new StringBuilder();
        
        if (url.getQuery() != null)
            sb.append(url.getQuery());
        else
            return "";
        
        if (url.getRef() != null)
            sb.append("#").append(url.getRef());

        return sb.toString();
    }
    
    public static String getUrlParams(String url) {
        if (StringUtils.isBlank(url))
            return "";

        //test valid url
        try{
            URL aURL = new URL(url);
        }catch (IOException e){
            //if not valid url, return null
            return "";
        }

        //find char "?"
        int param_start_pos = url.indexOf("?");
        if (param_start_pos > 0){
            return url.substring(++param_start_pos);
        }else{
            return "";
        }
    }

    public static void main(String[] args) throws Exception {

        String url1="http://example.com:80/docs/books/tutorial/index.html?111?name=networking#%E6%B5%8B%E8%AF%95";
        URL aURL = new URL(URLDecoder.decode(url1, "UTF-8"));
        System.out.println("protocol = " + aURL.getProtocol());
        System.out.println("authority = " + aURL.getAuthority());
        System.out.println("host = " + aURL.getHost());
        System.out.println("port = " + aURL.getPort());
        System.out.println("path = " + aURL.getPath());
        System.out.println("query = " + aURL.getQuery());
        System.out.println("filename = " + aURL.getFile());
        System.out.println("ref = " + aURL.getRef());
        System.out.println("filename = " + aURL.getFile());
        System.out.println("params = " + getUrlParams(aURL.toString()));
    }
}
