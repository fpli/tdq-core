package com.ebay.sojourner.ubd.sharedlib.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;


public class GUID2Date {
    private static final int[] hexlookup = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, // 0-15
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 16-31
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 32-47
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, // 48-63
			-1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 64-79
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 80-95
			-1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 96-111
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 // 112-127
	};

	private static long power(int base, int n) {
		long i, p;

		if (n == 0) {
			return 1;
		}

		p = 1;
		for (i = 1; i <= n; ++i) {
			p = p * base;
		}
		return p;
	}

	private static long Hex2Long(String in) {
		int len = in.length();
		long out = 0;
		int dec = 0;
		long val = 0;

		for (int i = 0; i < len; i++) {
			dec = hexlookup[in.charAt(i)];
			val = power(16, len - i - 1);
			out += val * dec;
		}
		return out;
	}

	public static Date getDate(String guid) {
		long millisec = getTimestamp(guid);
		return new Date(millisec);
	}
	
	public static long getTimestamp(String guid) {
	       // use SimpleDateFormat to format the output
        // set TimeZone to "GMT" align with TD udf
        //String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
        //SimpleDateFormat timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        //timestampFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (StringUtils.isBlank(guid) || guid.trim().length() != 32) {
            throw new RuntimeException("Can't convert guid to timestamp");
        }

        String s = guid.substring(8, 11) + guid.substring(0, 8);
        return Hex2Long(s);
	}
}
