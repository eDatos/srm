package org.siemac.metamac.srm.core.base.serviceapi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;

public class BaseAsserts {

    // -----------------------------------------------------------------
    // ITEM SCHEME
    // -----------------------------------------------------------------

    public static void assetEqualsItemScheme(ItemScheme expected, ItemScheme actual) {

        // IdentifiableArtefact
        assertEquals(expected.getIdLogic(), actual.getIdLogic());
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getUrn(), actual.getUrn());

        // NameableArtefact
        assertEqualsInternationalString(expected.getName(), actual.getName());
        assertEqualsInternationalString(expected.getDescription(), actual.getDescription());

        // MaintainableArtefact
        assertEquals(expected.getVersionLogic(), actual.getVersionLogic());
        assertEquals(expected.getValidFrom(), actual.getValidFrom());
        assertEquals(expected.getValidTo(), actual.getValidTo());
        assertEquals(expected.getFinalLogic(), actual.getFinalLogic());
        assertEquals(expected.getIsExternalReference(), actual.getIsExternalReference());
        assertEquals(expected.getStructureURL(), actual.getStructureURL());
        assertEquals(expected.getServiceURL(), actual.getServiceURL());

        assertEqualsExternalItem(expected.getMaintainer(), actual.getMaintainer());
    }

    // -----------------------------------------------------------------
    // INTERNATIONAL STRING
    // -----------------------------------------------------------------

    public static void assertEqualsInternationalString(InternationalString expected, InternationalString actual) {
        if (actual == null && expected == null) {
            return;
        } else if ((actual != null && expected == null) || (actual == null && expected != null)) {
            fail();
        }
        assertEquals(expected.getTexts().size(), actual.getTexts().size());
        for (LocalisedString localisedStringExpected : expected.getTexts()) {
            assertEquals(localisedStringExpected.getLabel(), actual.getLocalisedLabel(localisedStringExpected.getLocale()));
        }
    }

    public static void assertEqualsInternationalString(InternationalString internationalString, String locale1, String label1, String locale2, String label2) {
        int count = 0;
        if (locale1 != null) {
            assertEquals(label1, internationalString.getLocalisedLabel(locale1));
            count++;
        }
        if (locale2 != null) {
            assertEquals(label2, internationalString.getLocalisedLabel(locale2));
            count++;
        }
        assertEquals(count, internationalString.getTexts().size());
    }

    // -----------------------------------------------------------------
    // EXTERNAL ITEMS
    // -----------------------------------------------------------------

    public static void assertEqualsExternalItem(ExternalItem expected, ExternalItem actual) {
        //TODO assertEqualsExternalItem
    }

}
