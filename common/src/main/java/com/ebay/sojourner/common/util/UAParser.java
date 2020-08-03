package com.ebay.sojourner.common.util;

import java.util.Map;

public interface UAParser {
    Map<String, String> processUA(String agent, String dn, boolean isPulsar);
}