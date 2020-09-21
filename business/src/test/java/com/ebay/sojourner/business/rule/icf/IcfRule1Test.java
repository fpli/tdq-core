package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule1Test {

  IcfRule1 icfRule1;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule1 = new IcfRule1();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("1"));
    int botFlag = icfRule1.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(801);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule1.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}