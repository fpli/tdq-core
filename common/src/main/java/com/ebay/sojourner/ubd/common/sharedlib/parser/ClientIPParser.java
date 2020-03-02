package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.IsValidIPv4;
import com.ebay.sojourner.ubd.common.sharedlib.util.IsValidPrivateIPv4;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJListGetValueByIndex;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJListLastElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ClientIPParser implements FieldParser<RawEvent, UbiEvent> {
  public static final String REMOTE_IP = "RemoteIP";
  public static final String FORWARDED_FOR = "ForwardedFor";
  public static final String AGENT = "Agent";
  private static final Logger log = Logger.getLogger(ClientIPParser.class);

  @Override
  public void init() throws Exception {}

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    ClientData clientData = rawEvent.getClientData();
    try {
      String remoteIP = clientData.getRemoteIP();
      String forwardedFor = clientData.getForwardFor();

      if (StringUtils.isNotBlank(remoteIP)) {
        if (IsValidIPv4.isValidIP(remoteIP) && !IsValidPrivateIPv4.isValidIP(remoteIP)) {
          ubiEvent.setClientIP(remoteIP);
        } else {
          String forwardValueByIndex =
              SOJListGetValueByIndex.getValueByIndex(
                  SOJListLastElement.getLastElement(forwardedFor, ":"), ",", 1);
          if (IsValidIPv4.isValidIP(forwardValueByIndex)
              && !IsValidPrivateIPv4.isValidIP(forwardValueByIndex)) {
            ubiEvent.setClientIP(forwardValueByIndex);
          } else if (IsValidIPv4.isValidIP(remoteIP)) {
            ubiEvent.setClientIP(remoteIP);
          }
        }
      }
    } catch (Exception e) {
      log.debug("Parsing ClientIP failed, format incorrect: " + clientData);
    }
  }
}
