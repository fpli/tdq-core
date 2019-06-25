package com.ebay.sojourner.ubd.rt.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Collections;

public class SetTest {

    @Test
    public void testEmptySet() {
        assertEquals(Collections.emptySet().size(), 0);
    }
}
