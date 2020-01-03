package com.ebay.sojourner.ubd.common.util;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SetTest {

    @Test
    public void testEmptySet() {
        assertEquals(Collections.emptySet().size(), 0);
    }
}
