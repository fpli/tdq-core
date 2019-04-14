package com.ebay.sojourner.ubd.util.sojlib;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SOJNVL {
	public static final String KV_DELIMITER[] = { "&", "&_", "&!" };
	public static final String BLANK_STRING="";

	public static String getTagValue(String value, String key) {
	    if (StringUtils.isBlank(value) ||
	    		StringUtils.isBlank(key)) {
	        return null;
	    }
	    
	    //ensure string starts with &
	    value = "&" + value;
	    
		Pattern p1, p2;
		Matcher m1, m2;
		String kvSet = "", keySet = "";
		// set search keys
		for (int i = 0; i < KV_DELIMITER.length; i++) {
			keySet += KV_DELIMITER[i] + key + "=|";
			kvSet += KV_DELIMITER[i] + "|";
		}

		keySet = keySet.substring(0, keySet.length() - 1);
		kvSet = kvSet.substring(0, kvSet.length() - 1);

		p1 = Pattern.compile(keySet);
		p2 = Pattern.compile(kvSet);
		m1 = p1.matcher(value);
		m2 = p2.matcher(value);

		if (m1.find()) {
			int startpos;
			int endpos;
			startpos = m1.start();
			int tmppos = m1.end();
			if (m2.find(tmppos)) {
				endpos = m2.start();
			}else{
				//if not next delimiter, get the max position
				endpos = value.length();
			}
				if (endpos < 0)
					endpos = value.length() - 1;
				String kvPair[] = value.substring(startpos, endpos).split("=",2);
				return BLANK_STRING.equals(kvPair[1]) ? null : kvPair[1];
			
		}
		return null;
	}
}
