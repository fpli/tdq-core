package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule10Test {

  IcfRule10 icfRule10;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule10 = new IcfRule10();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("200"));
    int botFlag = icfRule10.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(810);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule10.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}