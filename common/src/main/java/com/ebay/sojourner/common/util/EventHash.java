package com.ebay.sojourner.common.util;

import com.ebay.sojourner.common.model.UbiEvent;

public class EventHash {

  /**
   * hashrow (  a.click_id,a.site_id,a.page_id,a.page_name,a.referrer_hash,a.url_query_string,a
   * .client_data ,a.cookies,a.application_payload,a.web_server,a.referrer,a.user_id,a.item_id,a
   * .flags,a .rdt,a.regu,a.sqr ,a.staticpagetype,a.ciid,a.siid) as KEY_HASH
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

  public static void main(String[] args) {
    System.out.println(hash(0,
        "nqc=AA**&c=1&g=2f87f0671730a9bd36b07de7ff8a8f6e&h=67&px=4249&chnl=9&uc=1&nqt=AA**&p"
            + "=2317508&uaid=2f87f13a1730aade5df39bf0f783f93cS0&bs=0&t=0&cflgs=wA**&ul=en-US&plmt"
            + "=MxEAAB%252BLCAAAAAAAAADtV9tuGzcQ%252FRc"
            + "%252BKxLvF78VaVG06ENrNXkJgoKXocV6vVrsrmK7gf"
            + "%252B9w5VUGxaiykBSyEAhQEsOySHPzJwZ8jPprsgFp0oLzmakK"
            +
            "%252BTiw2dSErlgFH9iRmKp48xSxyUT3DI1Iz3KiJDKZCMZdzZI4Y1NXgaTKPCsQUpKZgRGVPcRv36rFpe"
            + "1m6aZEV8VKKUo0zjNf9rLb1COx7i5Ixd0RtZx%252Bvhh%252B1lPYwOe7TMZwPdx9dsG%252BnvURB4"
            + "ecJc4ThMiTqAPsyco%252BHMUTFK%252BQ2G9MYLHIET00hoeUo7CMOtzikZm%252BXVQsNNQ4OSMe"
            + "5BcmhH6S%252FDDuiUfj2JTz7EpIewOm8uRCWl8sABSROu1VM7lbMFKm4w4Hw%252FJ5ygko%252Fs"
            + "4i1pq7pO20nkppfaG8SCYDlJm0NmeCQqq3KMvNIIyrPJlj4IbkJCEk9SCNDq7BIIJEBQ0aEPV%252Ba"
            + "CQByg4czsUxkbhWcoyoc940E4pEwxQG3MOkrrzQSEOUCA1dihAewGOm6S5Rn8475D%252B1jPpQlY5hGM"
            + "o1ltBh2pW96HH%252FXAj7NSQxebY1ZP6OryMvk2%252BqeO%252BaiOMSVWzItcUj4QNpgyvCYYxpSTB"
            + "3TbDpAlF67G2sNH1NXjq7Ltu32r%252FaW1ehqZ0dWVdWGLsd82r1PXvBth3u%252BZmrFYkWzNWV8UKsL"
            + "RjTVDdBHAof0H9rqBcrVA5IpqR25LGFZpfoydIC%252BPtur%252F%252BqWrSXNb49uldW8ZJAsHfz%25"
            + "2BvfZphv3bq4hntckIblL5f1FJvQlGH1%252B32H%252B2Cubn13Xdpfn0mHrYl%252FXk5n2nWG99APBR"
            + "Mn2mjO5qxaeNM32F0sSj%252FtOox%252BLHEe1zeL2C8%252BLSJbLPeq3mwXzW9KO%252F9zqJAr2G"
            + "Hy%252Bpfgoml7yD0Mq%252B%252Bh8RidgtI6ZSd8W%252BOBYb"
            + "%252B07wvcXm6l5GLsN4DC4cfl8ofWhwbSXgYt0gN9v06bOFYsGFklXlc"
            +
            "%252FnuLvAbF8N1n7FFsPZYTqFTzgVZlcz7M1mWrDjKCoPgkdqElgsHR4i9V9y71vVf6OFDyMSH5AbMr1"
            + "jtg6miy0UFSFgHcRaaXPVgnQyWVBzdEke37EfgGalxObf3ti382Hpl%252F8kSD7TTP%252BT%252BpjpD7"
            + "B118k9YGdXxeh6TNCC6TRvlJ7gXeNSPFqzoI0MXqjI%252FDgqOQp6Hj07nd%252BhH4BmpcTmv7nlfry9R"
            + "Xq2eMQV49DyOynQ09X1aFTMgH%252FWpnghCA5tbxfvrbqbvju2v5m%252F%252BDQKhthtE6Az0DBouOg"
            + "ZVKYGz0kCPCvDw7nHHL4AAE99uA4OCH2w2Rs9ZY8%252FA1x%252BxdjMxEAAA%253D%253D&pn=2&rq=0"
            + "ba3d58e335dd897&pagename=cos__mfe&po=%5B%28pg%3A2056321+pid%3A101013%29%28pg%3A2056"
            + "321+pid%3A101012%29%28pg%3A2056321+pid%3A101015%29%28pg%3A2056321+pid%3A10"
            + "1014%29%28pg%3A2056321+pid%3A100595%29%28pg%3A2056321+pid%3A100594%29%28pg%3A2"
            + "056321+pid%3A100593%29%28pg%3A2056321+pid%3A100592%29%28pg%3A2056321+pid%3A1005"
            + "90%29%28pg%3A2056321+pid%3A100723%29%5D&ciid=h%2FE65d8*"));
    System.out.println(hash(0,
        "saucxgdpry=false&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid"
            + "=6c7f363505bb4b7484af853e6d9f3075&g=2f87f0671730a9bd36b07de7ff8a8f6e&saebaypid"
            + "=100592&sapcxkw=&h=67&schemaversion=3&salv=5&ciid=h%2FE77V0*&p=2367355&sapcxcat"
            + "=11450%2C260010%2C15724%2C11554&t=0&saiid=6a21d7fb-1ce8-493f-937a-76d2829b13c1"
            + "&cflgs=gA**&samslid=&eactn=AUCT&pn=2&rq=4874bb063ab5a395&pagename=SandPage&ciid=h"
            + "%2FE77V0*"));
    System.out.println(hash(0,
        "saucxgdpry=false&flgs=AA**&efam=SAND&ac=&saucxgdprct=false&saty=1&sameid"
            + "=a3df4c0ca41b47cca76ce2b9042db6c8&g=2f87f0671730a9bd36b07de7ff8a8f6e&saebaypid"
            + "=100590&sapcxkw=&h=67&schemaversion=3&salv=5&ciid=h%2FE87V0*&p=2367355&sapcxcat"
            + "=11450%2C260010%2C15724%2C11554&t=0&saiid=0926337a-3b13-4b9c-a36f-9bfe71f399d6"
            + "&cflgs=gA**&samslid=&eactn=AUCT&pn=2&rq=4874bb063ab5a395&pagename=SandPage&ciid=h"
            + "%2FE87V0*"));
  }
}
