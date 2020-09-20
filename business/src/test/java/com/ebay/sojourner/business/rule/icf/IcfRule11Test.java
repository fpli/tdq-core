package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule11Test {

  IcfRule11 icfRule11;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule11 = new IcfRule11();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("400"));
    int botFlag = icfRule11.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(811);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule11.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}