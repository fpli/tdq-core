package com.ebay.sojourner.ubd.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import org.junit.jupiter.api.Test;

public class SetTest {

  @Test
  public void testEmptySet() {
    assertEquals(Collections.emptySet().size(), 0);
  }
}
