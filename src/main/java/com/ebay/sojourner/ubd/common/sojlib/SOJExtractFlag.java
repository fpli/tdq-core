package com.ebay.sojourner.ubd.common.sojlib;


public class SOJExtractFlag {

    private final static int ascii_to_base64[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62,
			-1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1,
			-1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1,
			26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42,
			43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1 };

    public static int extractFlag(String soj_flag, int bit_pos) {
		// Null indicator -1 NULL , 0 Not NULL
		int str_len, mybytepos;

		if (bit_pos < 0) {
			return 0;
		}

		// checking NULL value for parameter:soj_flag
		if (soj_flag == null
				|| soj_flag.equals(""))
			return 0;

		str_len = soj_flag.length();

		mybytepos = bit_pos / 6;
		// Error if byte required to get the bit is greater than input string
		// length
		
		if (mybytepos >= str_len)
			return 0;
		// Error if input char is
		char charAt = soj_flag.charAt(mybytepos);
		if (charAt < 0 || charAt >= ascii_to_base64.length) {
		    return 0;
		}
		
        if (ascii_to_base64[charAt] < 0)
			return 0;

		return ((ascii_to_base64[charAt] >> (5 - (bit_pos % 6))) & 1);
	}
}
