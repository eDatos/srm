package org.siemac.metamac.srm.core.base.serviceapi.utils;

import org.siemac.metamac.srm.core.base.domain.ItemScheme;
import org.siemac.metamac.srm.core.utils.SrmAsserts;

import static org.junit.Assert.assertEquals;

public class BaseAsserts {

    public static void assetEqualsItemScheme(ItemScheme expected, ItemScheme actual) {

        // IdentifiableArtefact
        assertEquals(expected.getIdLogic(), actual.getIdLogic());
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getUrn(), actual.getUrn());

        // NameableArtefact
        SrmAsserts.assertEqualsInternationalString(expected.getName(), actual.getName());
        SrmAsserts.assertEqualsInternationalString(expected.getDescription(), actual.getDescription());

        // MaintainableArtefact
        assertEquals(expected.getVersionLogic(), actual.getVersionLogic());
        assertEquals(expected.getValidFrom(), actual.getValidFrom());
        assertEquals(expected.getValidTo(), actual.getValidTo());
        assertEquals(expected.getFinalLogic(), actual.getFinalLogic());
        assertEquals(expected.getIsExternalReference(), actual.getIsExternalReference());
        assertEquals(expected.getStructureURL(), actual.getStructureURL());
        assertEquals(expected.getServiceURL(), actual.getServiceURL());

        SrmAsserts.assertExternalItemBt(expected.getMaintainer(), actual.getMaintainer());
    }

}
