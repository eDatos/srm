package org.siemac.metamac.srm.soap.external.v1_0.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.joda.time.DateTime;
import org.siemac.metamac.soap.common.v1_0.domain.InternationalString;
import org.siemac.metamac.soap.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.soap.common.v1_0.domain.Resource;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;

public class Asserts {

    public static void assertEqualsInternationalStringNotNull(com.arte.statistic.sdmx.srm.core.common.domain.InternationalString expecteds,
            org.siemac.metamac.soap.common.v1_0.domain.InternationalString actuals) {
        assertNotNull(expecteds);
        assertEqualsInternationalString(expecteds, actuals);
    }

    public static void assertEqualsInternationalString(com.arte.statistic.sdmx.srm.core.common.domain.InternationalString expecteds,
            org.siemac.metamac.soap.common.v1_0.domain.InternationalString actuals) {
        assertEqualsNullability(expecteds, actuals);
        if (expecteds == null) {
            return;
        }
        assertEquals(expecteds.getTexts().size(), actuals.getTexts().size());
        for (com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString expected : expecteds.getTexts()) {
            boolean existsItem = false;
            for (LocalisedString actual : actuals.getTexts()) {
                if (expected.getLocale().equals(actual.getLang())) {
                    assertEquals(expected.getLabel(), actual.getValue());
                    existsItem = true;
                }
            }
            assertTrue(existsItem);
        }
    }

    public static void assertEqualsInternationalString(String locale1, String label1, String locale2, String label2, InternationalString actual) {
        int count = 0;
        for (LocalisedString localisedString : actual.getTexts()) {
            if (localisedString.getLang().equals(locale1)) {
                assertEquals(label1, localisedString.getValue());
                count++;
            } else if (localisedString.getLang().equals(locale2)) {
                assertEquals(label2, localisedString.getValue());
                count++;
            }
        }
        assertEquals(count, actual.getTexts().size());
    }

    public static void assertEqualsResource(ItemSchemeVersion expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEquals(expected.getMaintainableArtefact().getCode(), actual.getId());
        assertEquals(expected.getMaintainableArtefact().getUrn(), actual.getUrn());
        assertEqualsInternationalString(expected.getMaintainableArtefact().getName(), actual.getName());
    }

    public static void assertEqualsResource(Item expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEquals(expected.getNameableArtefact().getCode(), actual.getId());
        assertEquals(expected.getNameableArtefact().getUrn(), actual.getUrn());
        assertEqualsInternationalString(expected.getNameableArtefact().getName(), actual.getName());
    }

    public static void assertEqualsDate(DateTime expected, Date actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.toDate(), actual);
    }

    public static void assertEqualsNullability(Object expected, Object actual) {
        if (actual == null && expected == null) {
            return;
        } else if ((actual != null && expected == null) || (actual == null && expected != null)) {
            fail();
        }
    }
}