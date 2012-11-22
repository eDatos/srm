package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Urns;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;

public class Asserts extends MetamacRestAsserts {


    public static void assertEqualsInternationalStringNotNull(org.siemac.metamac.core.common.ent.domain.InternationalString expecteds,
            org.siemac.metamac.rest.common.v1_0.domain.InternationalString actuals) {
        assertNotNull(expecteds);
        assertEqualsInternationalString(expecteds, actuals);
    }

    public static void assertEqualsInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString expecteds, org.siemac.metamac.rest.common.v1_0.domain.InternationalString actuals) {
        assertEqualsNullability(expecteds, actuals);
        if (expecteds == null) {
            return;
        }
        assertEquals(expecteds.getTexts().size(), actuals.getTexts().size());
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString expected : expecteds.getTexts()) {
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

    public static void assertEqualsUrnsNotNull(List<ConceptMetamac> expecteds, Urns actuals) {
        assertTrue(expecteds.size() > 0);
        assertEquals(expecteds.size(), actuals.getTotal().intValue());
        for (int i = 0; i < expecteds.size(); i++) {
            assertEquals(expecteds.get(i).getNameableArtefact().getUrn(), actuals.getUrns().get(i));
        }
    }

    public static void assertEqualsResource(ItemSchemeVersion expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEquals(expectedKind, actual.getKind());
        assertEquals(expected.getMaintainableArtefact().getCode(), actual.getId());
        assertEquals(expected.getMaintainableArtefact().getUrn(), actual.getUrn());
        assertEquals(expectedKind, actual.getSelfLink().getKind());
        assertEquals(expectedSelfLink, actual.getSelfLink().getHref());
        assertEqualsInternationalString(expected.getMaintainableArtefact().getName(), actual.getTitle());
    }

    public static void assertEqualsResource(Item expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEquals(expectedKind, actual.getKind());
        assertEquals(expected.getNameableArtefact().getCode(), actual.getId());
        assertEquals(expected.getNameableArtefact().getUrn(), actual.getUrn());
        assertEquals(expectedKind, actual.getSelfLink().getKind());
        assertEquals(expectedSelfLink, actual.getSelfLink().getHref());
        assertEqualsInternationalString(expected.getNameableArtefact().getName(), actual.getTitle());
    }
}