package com.ebay.sojourner.ubd.common.sharedlib.util;


import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

public class MobileEventsIdentifier {

    private String mobileStartPattern;
    private String mobileIndexPattern;
    private String mobileMatchPattern;
    private String[] startPatternList;
    private String[] indexPatternList;
    public String[] matchPatternList;
    public String[][] multiMatchPatternArray;

    public MobileEventsIdentifier() {
        mobileStartPattern = UBIConfig.getString(Property.MOBILE_AGENT_START);
        mobileIndexPattern = UBIConfig.getString(Property.MOBILE_AGENT_INDEX);
        mobileMatchPattern = UBIConfig.getString(Property.MOBILE_AGENT_OTHER);
        if (!UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false)) {
            if (mobileIndexPattern == null || mobileMatchPattern == null || mobileStartPattern == null) {
                throw new RuntimeException();
            }
            startPatternList = mobileStartPattern.split(Property.PROPERTY_DELIMITER);
            indexPatternList = mobileIndexPattern.split(Property.PROPERTY_DELIMITER);
            matchPatternList = mobileMatchPattern.split(Property.PROPERTY_DELIMITER);
            multiMatchPatternArray = new String[matchPatternList.length][];
            for (int i = 0; i < matchPatternList.length; i++) {
                multiMatchPatternArray[i] = matchPatternList[i].split(Property.MOBILE_AGENT_DELIMITER);
            }
        }
    }
}
