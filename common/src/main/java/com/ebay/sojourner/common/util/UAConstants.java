package com.ebay.sojourner.common.util;

import java.util.HashMap;
import java.util.Map;

public class UAConstants {

  public static final String PAGE = "p";
  public static final String GUID = "g";
  public static final String AGENT = "Agent";
  public static final String DEVICE_TAG = "dn";

  public static final String BROWSERVESION = "dd_bv";
  public static final String BROWSERFAMILY = "dd_bf";
  public static final String OSVESION = "dd_osv";
  public static final String OSFAMILY = "dd_os";
  public static final String DEVICETYPE = "dd_dc";
  public static final String DEVICEFAMILY = "dd_d";

  public static final String PULSAR_RREFIX = "_";

  public static final String E_BAYI_PHONE = "eBayiPhone";
  public static final String E_BAY_ANDROID = "eBayAndroid";
  public static final String E_BAYI_PAD = "eBayiPad";

  public static final String DN_IPHONE = "iPhone";
  public static final String DN_IPAD = "iPad";

  public static final String DEVICE_MOBILE = "Mobile";
  public static final String DEVICE_TABLET = "Tablet";
  public static final String DEVICE_DESKTOP = "Desktop";
  public static final String DEVICE_TOUCH_SCREEN = "TouchScreen";

  public static final String OS_ANDROID = "Android";
  public static final String OS_IOS = "iOS";

  public static final String SAFARI_WEB_KIT = "Safari WebKit";
  public static final String CHROME = "Chrome";

  public static final String OTHER = "Other";

  public static Map<String, String> OTHERRESULT = new HashMap<String, String>();
  public static Map<String, String> OTHERRESULT_PULSAR = new HashMap<String, String>();

  static {
    OTHERRESULT.put(OSFAMILY, OTHER);
    OTHERRESULT.put(DEVICEFAMILY, OTHER);
    OTHERRESULT.put(BROWSERFAMILY, OTHER);
  }

  static {
    OTHERRESULT_PULSAR.put(PULSAR_RREFIX + OSFAMILY, OTHER);
    OTHERRESULT_PULSAR.put(PULSAR_RREFIX + DEVICEFAMILY, OTHER);
    OTHERRESULT_PULSAR.put(PULSAR_RREFIX + BROWSERFAMILY, OTHER);
  }
}
