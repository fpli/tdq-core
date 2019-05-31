package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.util.Constants;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Map;

public class CobrandParser implements FieldParser<RawEvent, UbiEvent> {
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
    public void init() throws Exception {

        setHalfPageIndicator(new PageIndicator(UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.HALF_PAGES)));
        setClssfctnPageIndicator(new PageIndicator(UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.CLASSIFIED_PAGES)));
        setCoreSitePageIndicator(new PageIndicator(UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.CORESITE_PAGES)));
        setMobileAppIdCategory(new AppIdCategory(UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.MOBILE_APP)));
        setDesktopAppIdCategory(new AppIdCategory(UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.DESKTOP_APP)));
        setEimAppIdCategory(new AppIdCategory(UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.EIM_APP)));
        setMobileEventIdentifier(new MobileEventsIdentifier());
        halfSite = UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.HALF_SITE);
        expressSite = UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.EXPRESS_SITE);
        expressPartner = UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.EXPRESS_PARTNER);
        halfPartner = UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.HALF_PARTNER);
        shoppingPartner = UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.SHOPPING_PARTNER);
        artisanPartner = UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.ARTISAN_PARTNER);
        if (!UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getBoolean(Property.IS_TEST_ENABLE, false)) {
            if (halfSite == null || expressSite == null || expressPartner == null || halfPartner == null || shoppingPartner == null
                    || artisanPartner == null) {
                log.info("Error! Cobrand Properties Parse Error in config file!");
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
        Map<Integer, String[]> pageFmlyNameMap = LkpFetcher.getInstance().getPageFmlyMaps();
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
