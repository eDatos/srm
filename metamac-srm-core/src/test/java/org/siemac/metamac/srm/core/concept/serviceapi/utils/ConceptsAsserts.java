package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import org.siemac.metamac.srm.core.base.serviceapi.utils.BaseAsserts;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;


public class ConceptsAsserts {

    public static void assertEqualsConceptScheme(ConceptScheme expected, ConceptScheme actual) {
        BaseAsserts.assetEqualsItemScheme(expected.getItemScheme(), actual.getItemScheme());
        
        // TODO: Check that has the same items (concepts)
    }
}
