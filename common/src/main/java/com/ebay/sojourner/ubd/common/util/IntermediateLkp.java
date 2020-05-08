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
  @Getter private Set<Integer> roverPageSet = null;
  @Getter private Set<Integer> scPageSet1 = null;
  @Getter private Set<Integer> scPageSet2 = null;
  @Getter private Collection<String> tags = null;
  @Getter private String imgMpxChnlSet1 = null;
  @Getter private String imgMpxChnlSet6 = null;
  private StringBuilder stingBuilder = new StringBuilder();
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
    stingBuilder.setLength(0);
    stingBuilder
        .append(".*/0/(")
        .append("4908/89969|")
        .append("710/(82157|63920|89230)|")
        .append("711/(87621|88535)|")
        .append("709/(18254|4989)|")
        .append("707/(52222|1174)|")
        .append("706/(83004|87751)|")
        .append("1185/18912|")
        .append("5221/29898|")
        .append("5222/36186")
        .append("){1}/.*");
    imgMpxChnlSet1 = stingBuilder.toString();

    stingBuilder.setLength(0);
    stingBuilder
        .append(".*/0/(")
        .append("705/53470|")
        .append("706/53473|")
        .append("707/53477|")
        .append("709/53476|")
        .append("710/(53481|5232)|")
        .append("711/(53200|1751)|")
        .append("724/53478|")
        .append("1185/53479|")
        .append("1346/(53482|53482)|")
        .append("1553/53471|")
        .append("4686/53472|")
        .append("5221/53469|")
        .append("5222/53480|")
        .append("5282/53468|")
        .append("){1}/.*");
    imgMpxChnlSet6 = stingBuilder.toString();
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
