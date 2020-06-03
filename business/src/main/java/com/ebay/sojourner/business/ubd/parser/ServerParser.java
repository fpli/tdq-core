package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.ClientData;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import org.apache.commons.lang3.StringUtils;

public class ServerParser implements FieldParser<RawEvent, UbiEvent> {

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    ClientData clientData = rawEvent.getClientData();
    if (clientData != null) {
      String serverInfo = clientData.getServer();
      if (StringUtils.isNotBlank(serverInfo)) {
        ubiEvent.setWebServer(serverInfo);
      }
    }
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
