package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.joda.time.DateTime;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.LifeCycle;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;

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

    public static void assertEqualsResource(MaintainableArtefact expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEquals(expectedKind, actual.getKind());
        assertEquals(expected.getCode(), actual.getId());
        assertEquals(expected.getUrn(), actual.getUrn());
        assertEquals(expectedKind, actual.getSelfLink().getKind());
        assertEquals(expectedSelfLink, actual.getSelfLink().getHref());
        assertEqualsInternationalString(expected.getName(), actual.getTitle());
    }

    public static void assertEqualsResource(ItemSchemeVersion expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEqualsResource(expected.getMaintainableArtefact(), expectedKind, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(NameableArtefact expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEquals(expectedKind, actual.getKind());
        assertEquals(expected.getCode(), actual.getId());
        assertEquals(expected.getUrn(), actual.getUrn());
        assertEquals(expectedKind, actual.getSelfLink().getKind());
        assertEquals(expectedSelfLink, actual.getSelfLink().getHref());
        assertEqualsInternationalString(expected.getName(), actual.getTitle());
    }

    public static void assertEqualsResource(Item expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEqualsResource(expected.getNameableArtefact(), expectedKind, expectedSelfLink, actual);
    }

    public static void assertUriProviderExpected(MaintainableArtefact maintainableArtefact, String uriActual) {
        if (maintainableArtefact.getIsImported()) {
            assertTrue(uriActual.startsWith(RestTestConstants.URI_MARK_TO_RECOGNISE_IS_IN_DATABASE));
        } else {
            assertTrue(uriActual.startsWith("http:"));
        }
    }

    public static void assertEqualsProcStatus(ProcStatusEnum expected, ProcStatus actual) {
        assertEquals(expected.getName(), actual.name());
    }

    public static void assertEqualsDate(DateTime expected, Date actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.toDate(), actual);
    }

    public static void assertEqualsLifeCycle(SrmLifeCycleMetadata expected, LifeCycle actual) {
        assertEqualsProcStatus(expected.getProcStatus(), actual.getProcStatus());
        assertEqualsDate(expected.getProductionValidationDate(), actual.getProductionValidationDate());
        assertEquals(expected.getProductionValidationUser(), actual.getProductionValidationUser());
        assertEqualsDate(expected.getDiffusionValidationDate(), actual.getDiffusionValidationDate());
        assertEquals(expected.getDiffusionValidationUser(), actual.getDiffusionValidationUser());
        assertEqualsDate(expected.getInternalPublicationDate(), actual.getInternalPublicationDate());
        assertEquals(expected.getInternalPublicationUser(), actual.getInternalPublicationUser());
        assertEqualsDate(expected.getExternalPublicationDate(), actual.getExternalPublicationDate());
        assertEquals(expected.getExternalPublicationUser(), actual.getExternalPublicationUser());
        assertEquals(expected.getIsExternalPublicationFailed(), actual.isIsExternalPublicationFailed());
        assertEqualsDate(expected.getExternalPublicationFailedDate(), actual.getExternalPublicationFailedDate());
    }
}