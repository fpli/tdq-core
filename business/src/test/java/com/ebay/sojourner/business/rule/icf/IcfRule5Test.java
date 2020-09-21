package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule5Test {

  IcfRule5 icfRule5;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule5 = new IcfRule5();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("16"));
    int botFlag = icfRule5.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(805);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule5.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}