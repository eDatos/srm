package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import static org.junit.Assert.assertEquals;

import org.siemac.metamac.srm.core.base.serviceapi.utils.BaseAsserts;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersion;

public class ConceptsAsserts {

    public static void assertEqualsConceptScheme(ConceptSchemeVersion expected, ConceptSchemeVersion actual) {
        assertEquals(expected.getType(), actual.getType());
        BaseAsserts.assertEqualsExternalItem(expected.getRelatedOperation(), actual.getRelatedOperation());

        BaseAsserts.assertEqualsItemScheme(expected, actual);
    }
}
