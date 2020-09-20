package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule2Test {

  IcfRule2 icfRule2;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule2 = new IcfRule2();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("2"));
    int botFlag = icfRule2.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(802);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule2.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}