package com.ebay.sojourner.ubd.sharedlib.util;

import org.apache.commons.lang3.StringUtils;

public class SOJStrBetweenEndlist {
    //only char is supported
	public static String getStringBetweenEndList(String url, char startchar, char endchar) {
	    if (StringUtils.isBlank(url))
	        return null;
	    
	    int startPos = url.indexOf(startchar);
	    
	    if (startPos < 0)
	        startPos = 0;
	    else
	        startPos += 1;
	    
        int endPos = url.indexOf(endchar,startPos);
        
	    if (endPos < 0)
	        endPos = url.length();
	    
	    return url.substring(startPos,endPos);
	}
	
    public static void main(String[] args) throws Exception {

        String url1="http://example.com:80/docs/books/tutorial/index.html?111?name=networking#%E6%B5%8B%E8%AF%95";
        System.out.println("protocol = " + getStringBetweenEndList(url1, 'z', 'z'));
    }
}
