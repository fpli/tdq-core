package com.ebay.sojourner.ubd.common.sharedlib.util;

import org.apache.commons.lang3.StringUtils;

public class SOJGuidMod {
    public static int sojGUIDMod(String guid, int mod_value) {
		// Null indicator -1 NULL , 0 Not NULL

		// checking NULL value for parameter:guid
		if (StringUtils.isBlank(guid)) {
			return 0;
		}

		// checking NULL value for parameter:mod_value
		if (mod_value < 0) {
			return 0;
		}

		guid = guid.trim();

		int guid_len = guid.length();

		// Error if length is not 32
		if (guid_len != 32) {
			return 0;
		}

		int multiplier = 1;
		int hash = 0;
		int i = 0;

		for (i = 31; i >= 0; i--) {
			hash += (guid.charAt(i) * multiplier);
			multiplier = (multiplier << 5) - multiplier;
		}

		return Math.abs((hash % mod_value));
	}
}
