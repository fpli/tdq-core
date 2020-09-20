package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule4Test {

  IcfRule4 icfRule4;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule4 = new IcfRule4();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("8"));
    int botFlag = icfRule4.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(804);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule4.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}