package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.MobileEventsIdentifier;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CobrandParser implements FieldParser<RawEvent, UbiEvent> {

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
    Map<Integer, String[]> pageFmlyNameMap = LkpFetcher.getInstance().getPageFmlyMaps();
    Integer pageId = ubiEvent.getPageId();
    // TODO(Haibo): check buiness logic later????
    Integer cobrand;
    if (rawEvent.getSojA() == null || rawEvent.getSojA().get("cobrand") == null) {
      cobrand = -1;
    } else {
      cobrand = Integer.valueOf(rawEvent.getSojA().get("cobrand"));
    }
    ubiEvent.setCobrand(cobrand);
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
