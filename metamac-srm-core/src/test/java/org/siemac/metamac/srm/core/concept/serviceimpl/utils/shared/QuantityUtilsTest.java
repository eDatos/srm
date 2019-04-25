package org.siemac.metamac.srm.core.concept.serviceimpl.utils.shared;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QuantityUtilsTest {

    @Test
    public void isPowerOfTenTest() {
        assertTrue(QuantityUtils.isPowerOfTen(1));
        assertTrue(QuantityUtils.isPowerOfTen(10));
        assertTrue(QuantityUtils.isPowerOfTen(100));
        assertTrue(QuantityUtils.isPowerOfTen(1000));
        assertTrue(QuantityUtils.isPowerOfTen(10000));
        assertTrue(QuantityUtils.isPowerOfTen(100000));
        assertTrue(QuantityUtils.isPowerOfTen(1000000));
        assertTrue(QuantityUtils.isPowerOfTen(10000000));
        assertTrue(QuantityUtils.isPowerOfTen(100000000));
        assertTrue(QuantityUtils.isPowerOfTen(1000000000));

        assertFalse(QuantityUtils.isPowerOfTen(0));
        assertFalse(QuantityUtils.isPowerOfTen(2));
        assertFalse(QuantityUtils.isPowerOfTen(3));
        assertFalse(QuantityUtils.isPowerOfTen(4));
        assertFalse(QuantityUtils.isPowerOfTen(5));
        assertFalse(QuantityUtils.isPowerOfTen(6));
        assertFalse(QuantityUtils.isPowerOfTen(7));
        assertFalse(QuantityUtils.isPowerOfTen(8));
        assertFalse(QuantityUtils.isPowerOfTen(9));

        assertFalse(QuantityUtils.isPowerOfTen(-1));
        assertFalse(QuantityUtils.isPowerOfTen(-10));
        assertFalse(QuantityUtils.isPowerOfTen(-100));
        assertFalse(QuantityUtils.isPowerOfTen(-1000));
        assertFalse(QuantityUtils.isPowerOfTen(-10000));
        assertFalse(QuantityUtils.isPowerOfTen(-100000));
        assertFalse(QuantityUtils.isPowerOfTen(-1000000));
        assertFalse(QuantityUtils.isPowerOfTen(-10000000));
        assertFalse(QuantityUtils.isPowerOfTen(-100000000));
        assertFalse(QuantityUtils.isPowerOfTen(-1000000000));

        assertFalse(QuantityUtils.isPowerOfTen(11));

        assertFalse(QuantityUtils.isPowerOfTen(101));
        assertFalse(QuantityUtils.isPowerOfTen(110));
        assertFalse(QuantityUtils.isPowerOfTen(111));

        assertFalse(QuantityUtils.isPowerOfTen(1001));
        assertFalse(QuantityUtils.isPowerOfTen(1010));
        assertFalse(QuantityUtils.isPowerOfTen(1011));
        assertFalse(QuantityUtils.isPowerOfTen(1100));
        assertFalse(QuantityUtils.isPowerOfTen(1101));
        assertFalse(QuantityUtils.isPowerOfTen(1110));
        assertFalse(QuantityUtils.isPowerOfTen(1111));

        assertFalse(QuantityUtils.isPowerOfTen(null));
        assertFalse(QuantityUtils.isPowerOfTen(""));
        assertFalse(QuantityUtils.isPowerOfTen("prueba"));
    }

}
