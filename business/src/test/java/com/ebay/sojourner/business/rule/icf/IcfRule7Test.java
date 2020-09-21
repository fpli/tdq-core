package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule7Test {

  IcfRule7 icfRule7;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule7 = new IcfRule7();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("64"));
    int botFlag = icfRule7.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(807);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule7.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}