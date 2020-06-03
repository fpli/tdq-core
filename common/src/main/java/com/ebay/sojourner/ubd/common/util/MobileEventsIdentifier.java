package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.commons.lang3.StringUtils;

public class MobileEventsIdentifier {

  public String[] matchPatternList;
  public String[][] multiMatchPatternArray;
  private String mobileStartPattern;
  private String mobileIndexPattern;
  private String mobileMatchPattern;
  private String[] startPatternList;
  private String[] indexPatternList;

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

  public boolean isMobileEvent(UbiEvent event) {
    String agent = event.getAgentInfo();
    if (StringUtils.isNotBlank(agent)) {
      for (String startPattern : startPatternList) {
        if (agent.startsWith(startPattern)) {
          return true;
        }
      }

      for (String indexPattern : indexPatternList) {
        if (agent.indexOf(indexPattern) != -1) {
          return true;
        }
      }

      for (int i = 0; i < multiMatchPatternArray.length; i++) {
        boolean mobileFlag = false;
        int index = -1;
        if (multiMatchPatternArray[i][0].startsWith(Property.START_IDENTIFIER)) {
          if (agent.startsWith(multiMatchPatternArray[i][0].substring(1))) {
            mobileFlag = true;
          }
        } else {
          index = agent.indexOf(multiMatchPatternArray[i][0]);
          if (index != -1) {
            mobileFlag = true;
          }
        }
        if (mobileFlag) {
          for (int j = 1; j < multiMatchPatternArray[i].length; j++) {
            index = agent.indexOf(multiMatchPatternArray[i][j], index);
            if (index == -1) {
              mobileFlag = false;
              break;
            }
          }
        }
        if (mobileFlag) {
          return true;
        }
      }
    }
    return false;
  }
}
