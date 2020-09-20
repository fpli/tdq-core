package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule6Test {

  IcfRule6 icfRule6;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule6 = new IcfRule6();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("32"));
    int botFlag = icfRule6.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(806);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule6.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}