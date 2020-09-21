package com.ebay.sojourner.business.rule.icf;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcfRule13Test {

  IcfRule13 icfRule13;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    icfRule13 = new IcfRule13();
    ubiEvent = new UbiEvent();
  }

  @Test
  void getBotFlag() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("1000"));
    int botFlag = icfRule13.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(813);
  }

  @Test
  void getBotFlag_nonBot() {
    ubiEvent.setIcfBinary(NumberUtils.hexToDec("0"));
    int botFlag = icfRule13.getBotFlag(ubiEvent);
    assertThat(botFlag).isEqualTo(0);
  }
}