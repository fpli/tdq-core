package com.ebay.sojourner.common.util;

import com.ebay.sojourner.common.model.UbiEvent;

public class EventHash {

  /**
   * hashrow (  a.click_id,a.site_id,a.page_id,a.page_name,a.referrer_hash,a.url_query_string,a
   * .client_data
   * ,a.cookies,a.application_payload,a.web_server,a.referrer,a.user_id,a.item_id,a.flags,a
   * .rdt,a.regu,a.sqr
   * ,a.staticpagetype,a.ciid,a.siid) as KEY_HASH
   */
  public static int hashCode(UbiEvent event) {
    // No need to include click ID and page Id coz the sorting already contain them
    int hashCode = hash(0, event.getApplicationPayload());
    //hashCode = hash(hashCode, event.getClickId());
    hashCode = hash(hashCode, event.getClientData());
    hashCode = hash(hashCode, event.getCookies());
    hashCode = hash(hashCode, event.getFlags());
    hashCode = hash(hashCode, event.getItemId());
    //hashCode = hash(hashCode, event.getPageId());
    hashCode = hash(hashCode, event.getPageName());
    hashCode = hash(hashCode, event.isRdt() ? 1 : 0);
    hashCode = hash(hashCode, event.getRefererHash());
    hashCode = hash(hashCode, event.getReferrer());
    hashCode = hash(hashCode, event.getRegu());
    hashCode = hash(hashCode, event.getSessionStartDt());
    hashCode = hash(hashCode, event.getSiteId());
    hashCode = hash(hashCode, event.getSqr());
    hashCode = hash(hashCode, event.getStaticPageType());
    hashCode = hash(hashCode, event.getUrlQueryString());
    hashCode = hash(hashCode, event.getUserId());
    hashCode = hash(hashCode, event.getWebServer());
    hashCode = hash(hashCode, event.getCurrentImprId());
    hashCode = hash(hashCode, event.getSourceImprId());
    return hashCode;
  }

  private static int hash(int hashCode, Object obj) {
    if (obj != null) {
      hashCode += 37 * obj.hashCode();
    }
    return hashCode;
  }
}
