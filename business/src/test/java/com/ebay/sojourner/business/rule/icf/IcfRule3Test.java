package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule3Test {

  IcfRule3 icfRule3;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule3 = new IcfRule3();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("4"));
    int botFlag = icfRule3.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(803);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule3.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}