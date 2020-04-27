package com.ebay.sojourner.ubd.common.util;

import java.util.Collection;
import java.util.Set;
import lombok.Getter;

public class IntermediateLkp {

  private static volatile IntermediateLkp intermediateLkp= null;
  @Getter private Set<Integer> mobilePageSet = null;
  @Getter private Set<Integer> agentExcludePageSet = null;
  @Getter private Set<Integer> notifyCLickPageSet = null;
  @Getter private Set<Integer> notifyViewPageSet = null;
  @Getter  private Set<Integer> roverPageSet = null;
  @Getter private Set<Integer> scPageSet1 = null;
  @Getter private Set<Integer> scPageSet2 = null;
  @Getter private Collection<String> tags = null;
  private IntermediateLkp(){
    roverPageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.ROVER_PAGES), Property.PROPERTY_DELIMITER);
    scPageSet1 =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.SCEVENT_EXCLUDE_PAGES1), Property.PROPERTY_DELIMITER);
    scPageSet2 =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.SCEVENT_EXCLUDE_PAGES2), Property.PROPERTY_DELIMITER);
    agentExcludePageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.AGENT_EXCLUDE_PAGES), Property.PROPERTY_DELIMITER);
    notifyCLickPageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.NOTIFY_CLICK_PAGES), Property.PROPERTY_DELIMITER);
    notifyViewPageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.NOTIFY_VIEW_PAGES), Property.PROPERTY_DELIMITER);
    mobilePageSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.MOBILE_PAGES), Property.PROPERTY_DELIMITER);
    tags =
        PropertyUtils.parseProperty(
            UBIConfig.getString(Property.PRELOAD_PAYLOAD_TAGS), Property.PROPERTY_DELIMITER);
  }

  public static IntermediateLkp getInstance(){
    if(intermediateLkp==null) {
      synchronized (IntermediateLkp.class){
        if(intermediateLkp==null){
          intermediateLkp = new IntermediateLkp();
        }
      }
    }
    return intermediateLkp;
  }

}
