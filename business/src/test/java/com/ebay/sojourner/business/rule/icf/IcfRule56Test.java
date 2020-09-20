package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule56Test {

  IcfRule56 icfRule56;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule56 = new IcfRule56();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("80000000000000"));
    int botFlag = icfRule56.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(856);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule56.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}