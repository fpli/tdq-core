package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJParseClientInfo;

public class GenerateClientData {

  public static ClientData constructClientData(String clientDatastr) {
    ClientData clientData = new ClientData();
    clientData.getClass().getDeclaredFields();
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TPayload") != null) {
      clientData.setTPayload(SOJParseClientInfo.getClientInfo(clientDatastr, "TPayload"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TPool") != null) {
      clientData.setTPool(SOJParseClientInfo.getClientInfo(clientDatastr, "TPool"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TDuration") != null) {
      clientData.setTDuration(SOJParseClientInfo.getClientInfo(clientDatastr, "TDuration"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TStatus") != null) {
      clientData.setTStatus(SOJParseClientInfo.getClientInfo(clientDatastr, "TStatus"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TType") != null) {
      clientData.setTType(SOJParseClientInfo.getClientInfo(clientDatastr, "TType"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "ContentLength") != null) {
      clientData.setContentLength(SOJParseClientInfo
          .getClientInfo(clientDatastr, "ContentLength"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "ForwardedFor") != null) {
      clientData.setForwardFor(SOJParseClientInfo.getClientInfo(clientDatastr, "ForwardedFor"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Script") != null) {
      clientData.setScript(SOJParseClientInfo.getClientInfo(clientDatastr, "Script"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Server") != null) {
      clientData.setServer(SOJParseClientInfo.getClientInfo(clientDatastr, "Server"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TMachine") != null) {
      clientData.setTMachine(SOJParseClientInfo.getClientInfo(clientDatastr, "TMachine"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TStamp") != null) {
      clientData.setTStamp(SOJParseClientInfo.getClientInfo(clientDatastr, "TStamp"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "TName") != null) {
      clientData.setTName(SOJParseClientInfo.getClientInfo(clientDatastr, "TName"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Agent") != null) {
      clientData.setAgent(SOJParseClientInfo.getClientInfo(clientDatastr, "Agent"));
    }
    if (SOJParseClientInfo.getClientInfo(clientDatastr, "RemoteIP") != null) {
      clientData.setRemoteIP(SOJParseClientInfo.getClientInfo(clientDatastr, "RemoteIP"));
    }

    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Encoding") != null) {
      clientData.setEncoding(SOJParseClientInfo.getClientInfo(clientDatastr, "Encoding"));
    }

    if (SOJParseClientInfo.getClientInfo(clientDatastr, "Referer") != null) {
      clientData.setReferrer(SOJParseClientInfo.getClientInfo(clientDatastr, "Referer"));
    }

    return clientData;
  }

}
