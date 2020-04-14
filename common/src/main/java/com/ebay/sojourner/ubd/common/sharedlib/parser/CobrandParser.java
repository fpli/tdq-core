package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.MobileEventsIdentifier;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import com.ebay.sojourner.ubd.common.util.Constants;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CobrandParser implements FieldParser<RawEvent, UbiEvent> {

  public static final String PARTNER = "pn";
  private static PageIndicator halfPageIndicator;
  private static PageIndicator clssfctnPageIndicator;
  private static PageIndicator coreSitePageIndicator;
  private static AppIdCategory mobileAppIdCategory;
  private static AppIdCategory desktopAppIdCategory;
  private static AppIdCategory eimAppIdCategory;
  private static String halfSite;
  private static String expressSite;
  private static String expressPartner;
  private static String halfPartner;
  private static String shoppingPartner;
  private static String artisanPartner;
  private static MobileEventsIdentifier mobileIdentifier;

  @Override
  public void init() throws Exception {
    setHalfPageIndicator(new PageIndicator(UBIConfig.getString(Property.HALF_PAGES)));
    setClssfctnPageIndicator(new PageIndicator(UBIConfig.getString(Property.CLASSIFIED_PAGES)));
    setCoreSitePageIndicator(new PageIndicator(UBIConfig.getString(Property.CORESITE_PAGES)));
    setMobileAppIdCategory(new AppIdCategory(UBIConfig.getString(Property.MOBILE_APP)));
    setDesktopAppIdCategory(new AppIdCategory(UBIConfig.getString(Property.DESKTOP_APP)));
    setEimAppIdCategory(new AppIdCategory(UBIConfig.getString(Property.EIM_APP)));
    setMobileEventIdentifier(new MobileEventsIdentifier());
    halfSite = UBIConfig.getString(Property.HALF_SITE);
    expressSite = UBIConfig.getString(Property.EXPRESS_SITE);
    expressPartner = UBIConfig.getString(Property.EXPRESS_PARTNER);
    halfPartner = UBIConfig.getString(Property.HALF_PARTNER);
    shoppingPartner = UBIConfig.getString(Property.SHOPPING_PARTNER);
    artisanPartner = UBIConfig.getString(Property.ARTISAN_PARTNER);
    if (!UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false)) {
      if (halfSite == null
          || expressSite == null
          || expressPartner == null
          || halfPartner == null
          || shoppingPartner == null
          || artisanPartner == null) {
        log.error("Error! Cobrand Properties Parse Error in config file!");
        throw new RuntimeException();
      }
    }
  }

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    Map<Integer, String[]> pageFmlyNameMap = LkpManager.getInstance().getPageFmlyMaps();
    Integer pageId = ubiEvent.getPageId();
    ubiEvent.setCobrand(Constants.DEFAULT_CORE_SITE_COBRAND);

    if (mobileAppIdCategory != null && mobileAppIdCategory.isCorrespondingAppId(ubiEvent)) {
      ubiEvent.setCobrand(Constants.MOBILE_APP_COBRAND);
      return;
    } else if (desktopAppIdCategory != null && desktopAppIdCategory
        .isCorrespondingAppId(ubiEvent)) {
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
      try {
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
      } catch (NullPointerException e) {
        System.out.println(new Date() + "=====cobrandParser nullpoint====");
        System.out.println(new Date() + "pageId:" + pageId);
        System.out.println(new Date() + "expressSite:" + expressSite);
        if (pageFmlyNameMap != null) {
          System.out.println(new Date() + "pageFmlyNameMap is not null:" + pageFmlyNameMap.size());
          System.out.println(new Date() + "pageFmlyNameMap contains pageId:" + pageId);
        }

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

  void setMobileEventIdentifier(MobileEventsIdentifier identifier) {
    this.mobileIdentifier = identifier;
  }

}
