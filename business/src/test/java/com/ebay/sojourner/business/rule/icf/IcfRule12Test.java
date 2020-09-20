package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule12Test {

  IcfRule12 icfRule12;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule12 = new IcfRule12();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("800"));
    int botFlag = icfRule12.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(812);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule12.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}