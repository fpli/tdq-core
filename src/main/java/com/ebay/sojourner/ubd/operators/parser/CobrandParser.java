package com.ebay.sojourner.ubd.operators.parser;


import com.ebay.sojourner.ubd.util.Constants;
import com.ebay.sojourner.ubd.util.Property;
import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sojlib.SOJNVL;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.Map;

public class CobrandParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
  private static final Logger log = Logger.getLogger(CobrandParser.class);

  public static final String PARTNER = "pn";
  private PageIndicator halfPageIndicator;
  private PageIndicator clssfctnPageIndicator;
  private PageIndicator coreSitePageIndicator;
  private AppIdCategory mobileAppIdCategory;
  private AppIdCategory desktopAppIdCategory;
  private AppIdCategory eimAppIdCategory;
  private String halfSite;
  private String expressSite;
  private String expressPartner;
  private String halfPartner;
  private String shoppingPartner;
  private String artisanPartner;
  private MobileEventsIdentifier mobileIdentifier;

  @Override
  public void init(Configuration conf, RuntimeContext runtimeContext) throws Exception {
    setHalfPageIndicator(new PageIndicator(conf.getString(Property.HALF_PAGES,null)));
    setClssfctnPageIndicator(new PageIndicator(conf.getString(Property.CLASSIFIED_PAGES,null)));
    setCoreSitePageIndicator(new PageIndicator(conf.getString(Property.CORESITE_PAGES,null)));
    setMobileAppIdCategory(new AppIdCategory(conf.getString(Property.MOBILE_APP,null)));
    setDesktopAppIdCategory(new AppIdCategory(conf.getString(Property.DESKTOP_APP,null)));
    setEimAppIdCategory(new AppIdCategory(conf.getString(Property.EIM_APP,null)));
    setMobileEventIdentifier(new MobileEventsIdentifier(conf));
    halfSite = conf.getString(Property.HALF_SITE,null);
    expressSite = conf.getString(Property.EXPRESS_SITE,null);
    expressPartner = conf.getString(Property.EXPRESS_PARTNER,null);
    halfPartner = conf.getString(Property.HALF_PARTNER,null);
    shoppingPartner = conf.getString(Property.SHOPPING_PARTNER,null);
    artisanPartner = conf.getString(Property.ARTISAN_PARTNER,null);
        if (!conf.getBoolean(Property.IS_TEST_ENABLE, false)) {
            if (halfSite == null || expressSite == null || expressPartner == null || halfPartner == null || shoppingPartner == null
                    || artisanPartner == null) {
                log.info("Error! Cobrand Properties Parse Error in config file!");
                throw new RuntimeException();
            }
        }
    initLkpTable(conf,runtimeContext);
  }

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    Map<Integer, String[]> pageFmlyNameMap = LkpFetcher.getPageFmlyMaps();
    Integer pageId = ubiEvent.getPageId();
    ubiEvent.setCobrand(Constants.DEFAULT_CORE_SITE_COBRAND);

    if (mobileAppIdCategory != null && mobileAppIdCategory.isCorrespondingAppId(ubiEvent)) {
      ubiEvent.setCobrand(Constants.MOBILE_APP_COBRAND);
      return;
    } else if (desktopAppIdCategory != null && desktopAppIdCategory.isCorrespondingAppId(ubiEvent)) {
      ubiEvent.setCobrand(Constants.DESKTOP_APP_COBRAND);
      return;
    } else if (eimAppIdCategory != null && eimAppIdCategory.isCorrespondingAppId(ubiEvent)) {
      ubiEvent.setCobrand(Constants.EIM_APP_COBRAND);
      return;
    } else if (clssfctnPageIndicator != null
        && clssfctnPageIndicator.isCorrespondingPageEvent(ubiEvent)) {
      if (mobileIdentifier.isMobileEvent(ubiEvent)) {
        ubiEvent.setCobrand(Constants.MOBILE_CLASSIFIED_COBRAND);
        return;
      }
      ubiEvent.setCobrand(Constants.CLASSIFIED_SITE_COBRAND);
      return;
    } else if (halfPageIndicator != null && halfPageIndicator.isCorrespondingPageEvent(ubiEvent)) {
      if (mobileIdentifier.isMobileEvent(ubiEvent)) {
        ubiEvent.setCobrand(Constants.MOBILE_HALF_COBRAND);
        return;
      }
      ubiEvent.setCobrand(Constants.HALF_SITE_COBRAND);
      return;
    } else if (coreSitePageIndicator != null
        && coreSitePageIndicator.isCorrespondingPageEvent(ubiEvent)) {
      if (mobileIdentifier.isMobileEvent(ubiEvent)) {
        ubiEvent.setCobrand(Constants.MOBILE_CORE_SITE_COBRAND);
        return;
      }
      ubiEvent.setCobrand(Constants.DEFAULT_CORE_SITE_COBRAND);
      return;
    }
    if (pageFmlyNameMap.containsKey(pageId)) {
      if (expressSite.equals(pageFmlyNameMap.get(pageId)[0])) {
        ubiEvent.setCobrand(Constants.EBAYEXPRESS_SITE_COBRAND);
        return;
      }
      if (halfSite.equals(pageFmlyNameMap.get(pageId)[0])) {
        if (mobileIdentifier.isMobileEvent(ubiEvent)) {
          ubiEvent.setCobrand(Constants.MOBILE_HALF_COBRAND);
          return;
        }
        ubiEvent.setCobrand(Constants.HALF_SITE_COBRAND);
        return;
      }
    }
    String pn = SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), PARTNER);
    if (StringUtils.isNotBlank(pn) && pn.matches("-?\\d+")) {
      if (pn.equals(expressPartner)) {
        ubiEvent.setCobrand(Constants.EBAYEXPRESS_SITE_COBRAND);
        return;
      }
      if (pn.equals(shoppingPartner)) {
        ubiEvent.setCobrand(Constants.SHOPPING_SITE_COBRAND);
        return;
      }
      if (pn.equals(halfPartner)) {
        if (mobileIdentifier.isMobileEvent(ubiEvent)) {
          ubiEvent.setCobrand(Constants.MOBILE_HALF_COBRAND);
          return;
        }
        ubiEvent.setCobrand(Constants.HALF_SITE_COBRAND);
        return;
      }
      if (pn.equals(artisanPartner)) {
        ubiEvent.setCobrand(Constants.ARTISAN_COBRAND);
        return;
      }
    }

    if (mobileIdentifier.isMobileEvent(ubiEvent)) {
      ubiEvent.setCobrand(Constants.MOBILE_CORE_SITE_COBRAND);
    }
  }
  public void initLkpTable(Configuration conf,RuntimeContext runtimeContext) throws Exception {
    LkpFetcher.loadPageFmlys(conf,runtimeContext);
  }

  void setHalfPageIndicator(PageIndicator indicator) {
    this.halfPageIndicator = indicator;
  }

  void setClssfctnPageIndicator(PageIndicator indicator) {
    this.clssfctnPageIndicator = indicator;
  }

  void setCoreSitePageIndicator(PageIndicator indicator) {
    this.coreSitePageIndicator = indicator;
  }

  void setMobileAppIdCategory(AppIdCategory category) {
    this.mobileAppIdCategory = category;
  }

  void setDesktopAppIdCategory(AppIdCategory category) {
    this.desktopAppIdCategory = category;
  }

  void setEimAppIdCategory(AppIdCategory category) {
    this.eimAppIdCategory = category;
  }

  void setHalfSite(String value) {
    this.halfSite = value;
  }

  void setExpressSite(String value) {
    this.expressSite = value;
  }

  void setExpressPartner(String value) {
    this.expressPartner = value;
  }

  void setHalfPartner(String value) {
    this.halfPartner = value;
  }

  void setShoppingPartner(String value) {
    this.shoppingPartner = value;
  }

  void setArtisanPartner(String value) {
    this.artisanPartner = value;
  }
  
  void setMobileEventIdentifier(MobileEventsIdentifier identifier) {
      this.mobileIdentifier = identifier;
  } 
}
