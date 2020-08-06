package com.ebay.sojourner.common.util;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractUAParser implements UAParser {

  protected AtomicLong cachehit = new AtomicLong();
  protected AtomicLong cachemiss = new AtomicLong();

  protected void tuneParseResult(String agent, String dn, Map<String, String> uaResult,
      boolean isPulsar) {
    if (UAConstants.OTHER.equals(uaResult.get(UAConstants.DEVICEFAMILY))) {
      if (agent.startsWith(UAConstants.E_BAYI_PHONE)) {
        uaResult.put(UAConstants.DEVICEFAMILY, UAConstants.DEVICE_MOBILE);
      } else if (agent.startsWith(UAConstants.E_BAY_ANDROID)) {
        uaResult.put(UAConstants.DEVICEFAMILY, UAConstants.DEVICE_MOBILE);
      } else if (agent.startsWith(UAConstants.E_BAYI_PAD)) {
        uaResult.put(UAConstants.DEVICEFAMILY, UAConstants.DEVICE_TABLET);
      }
    }
    if (UAConstants.OTHER.equals(uaResult.get(UAConstants.DEVICEFAMILY))) {
      if (dn != null) {
        if (dn.startsWith(UAConstants.DN_IPHONE)) {
          uaResult.put(UAConstants.DEVICEFAMILY, UAConstants.DEVICE_MOBILE);
        } else if (dn.startsWith(UAConstants.DN_IPAD)) {
          uaResult.put(UAConstants.DEVICEFAMILY, UAConstants.DEVICE_TABLET);
        }
      }
    }
    if (UAConstants.DEVICE_MOBILE.equals(uaResult.get(UAConstants.DEVICEFAMILY))) {
      if (uaResult.get(UAConstants.DEVICETYPE) != null && uaResult.get(UAConstants.DEVICETYPE)
          .toLowerCase().contains("ipad")) {
        uaResult.put(UAConstants.DEVICEFAMILY, UAConstants.DEVICE_TABLET);
      }
    }
    // Tune the OS result of the Agent Parser
    if (uaResult.get(UAConstants.OSFAMILY) != null) {
      if (UAConstants.OTHER.equals(uaResult.get(UAConstants.OSFAMILY))) {
        if (agent.startsWith(UAConstants.E_BAYI_PHONE)) {
          uaResult.put(UAConstants.OSFAMILY, UAConstants.OS_IOS);
        } else if (agent.startsWith(UAConstants.E_BAY_ANDROID)) {
          uaResult.put(UAConstants.OSFAMILY, UAConstants.OS_ANDROID);
        } else if (agent.startsWith(UAConstants.E_BAYI_PAD)) {
          uaResult.put(UAConstants.OSFAMILY, UAConstants.OS_IOS);
        }
      }
      if ("ANDROID".equals(uaResult.get(UAConstants.OSFAMILY))) {
        uaResult.put(UAConstants.OSFAMILY, UAConstants.OS_ANDROID);
      } else if ("OTHER".equals(uaResult.get(UAConstants.OSFAMILY))) {
        uaResult.put(UAConstants.OSFAMILY, UAConstants.OTHER);
      }
    }

    // Tune the Browser result of the Agent Parser
    if (uaResult.get(UAConstants.BROWSERFAMILY) != null) {
      if (UAConstants.OTHER.equals(uaResult.get(UAConstants.BROWSERFAMILY))) {
        if (agent.startsWith(UAConstants.E_BAYI_PHONE)) {
          uaResult.put(UAConstants.BROWSERFAMILY, UAConstants.SAFARI_WEB_KIT);
        } else if (agent.startsWith(UAConstants.E_BAY_ANDROID)) {
          uaResult.put(UAConstants.BROWSERFAMILY, UAConstants.CHROME);
        } else if (agent.startsWith(UAConstants.E_BAYI_PAD)) {
          uaResult.put(UAConstants.BROWSERFAMILY, UAConstants.SAFARI_WEB_KIT);
        }
      }
    }

    if (isPulsar) {
      uaResult.put(UAConstants.PULSAR_RREFIX + UAConstants.DEVICEFAMILY,
          uaResult.get(UAConstants.DEVICEFAMILY));
      uaResult.put(UAConstants.PULSAR_RREFIX + UAConstants.DEVICETYPE,
          uaResult.get(UAConstants.DEVICETYPE));
      uaResult.put(UAConstants.PULSAR_RREFIX + UAConstants.OSFAMILY,
          uaResult.get(UAConstants.OSFAMILY));
      uaResult.put(UAConstants.PULSAR_RREFIX + UAConstants.OSVESION,
          uaResult.get(UAConstants.OSVESION));
      uaResult.put(UAConstants.PULSAR_RREFIX + UAConstants.BROWSERFAMILY,
          uaResult.get(UAConstants.BROWSERFAMILY));
      uaResult.put(UAConstants.PULSAR_RREFIX + UAConstants.BROWSERVESION,
          uaResult.get(UAConstants.BROWSERVESION));

      uaResult.remove(UAConstants.DEVICEFAMILY);
      uaResult.remove(UAConstants.DEVICETYPE);
      uaResult.remove(UAConstants.OSFAMILY);
      uaResult.remove(UAConstants.OSVESION);
      uaResult.remove(UAConstants.BROWSERFAMILY);
      uaResult.remove(UAConstants.BROWSERVESION);
    }
  }

  public long getCachehit() {
    return cachehit.get();
  }

  public long getCachemiss() {
    return cachemiss.get();
  }
}
