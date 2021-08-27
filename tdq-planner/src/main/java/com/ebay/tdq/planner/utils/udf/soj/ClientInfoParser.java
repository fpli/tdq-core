package com.ebay.tdq.planner.utils.udf.soj;

import com.ebay.tdq.planner.utils.udf.stringsearch.StringSearcherExt;
import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import org.neosearch.stringsearcher.Emit;

public final class ClientInfoParser implements Serializable {

  StringSearcherExt<?> stringSearcher = StringSearcherExt.builder()
      .addSearchString("&ForwardedFor=")
      .addSearchString("&RemoteIP=")
      .addSearchString("&Referer=")
      .addSearchString("&ContentLength=")
      .addSearchString("&Script=")
      .addSearchString("&Server=")
      .addSearchString("&Agent=")
      .addSearchString("&Encoding=")
      .addSearchString("&TPool=")
      .addSearchString("&TStamp=")
      .addSearchString("&TType=")
      .addSearchString("&TName=")
      .addSearchString("&TStatus=")
      .addSearchString("&TDuration=")
      .addSearchString("&TPayload=")
      .addSearchString("&TMachine=")
      .addSearchString("&corrId=")
      .addSearchString("&nodeId=")
      .build();

  private static int isValidCIname(String key) {
    String candidates = "ForwardedFor|RemoteIP|Referer|ContentLength|Script|Server|Agent|Encoding"
        + "|TPool|TStamp|TType|TName|TStatus|TDuration|TPayload|TMachine|corrId|nodeId";
    return candidates.contains(key) ? 1 : 0;
  }

  public String evaluate(final String clientInfo, String clientField) {
    if (clientInfo == null || clientField == null) {
      return null;
    }

    return extWoCopy(clientInfo, clientField);
  }

  public String extWoCopy(String clientinfo, String key) {
    if (StringUtils.isBlank(clientinfo)) {
      return null;
    } else if (!StringUtils.isBlank(key) && isValidCIname(key) != 0) {
      int valueStartPos;
      int tmpPos = -1;
      if (clientinfo.startsWith(key + '=')) {
        valueStartPos = (key + '=').length();
        Emit e = stringSearcher.firstMatch(clientinfo, valueStartPos);
        if (e == null) {
          return clientinfo.substring(valueStartPos);
        } else {
          tmpPos = e.getStart();
          return clientinfo.substring(valueStartPos, tmpPos);
        }
      } else if (!clientinfo.contains("&" + key + '=')) {
        return null;
      } else {
        int p = clientinfo.indexOf("&" + key + '=');
        valueStartPos = p + ("&" + key + '=').length();
        Emit e = stringSearcher.firstMatch(clientinfo, valueStartPos);
        if (e == null) {
          return clientinfo.substring(valueStartPos);
        } else {
          tmpPos = e.getStart();
          return clientinfo.substring(valueStartPos, tmpPos);
        }
      }
    } else {
      return null;
    }
  }


}