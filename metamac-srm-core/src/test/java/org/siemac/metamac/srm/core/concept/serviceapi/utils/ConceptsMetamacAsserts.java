package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsExternalItemDto;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsAsserts;

public class ConceptsMetamacAsserts extends ConceptsAsserts {

    public static void assertEqualsConceptScheme(ConceptSchemeVersionMetamac expected, ConceptSchemeVersionMetamac actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEqualsExternalItem(expected.getRelatedOperation(), actual.getRelatedOperation());
        ConceptsAsserts.assertEqualsConceptScheme(expected, actual);
    }

    public static void assertEqualsConcept(ConceptMetamac expected, ConceptMetamac actual) {
        ConceptsAsserts.assertEqualsInternationalString(expected.getPluralName(), actual.getPluralName());
        ConceptsAsserts.assertEqualsInternationalString(expected.getAcronym(), actual.getAcronym());
        ConceptsAsserts.assertEqualsInternationalString(expected.getDescriptionSource(), actual.getDescriptionSource());
        ConceptsAsserts.assertEqualsInternationalString(expected.getContext(), actual.getContext());
        ConceptsAsserts.assertEqualsInternationalString(expected.getDocMethod(), actual.getDocMethod());
        assertEquals(expected.getSdmxRelatedArtefact(), actual.getSdmxRelatedArtefact());
        // TODO type
        ConceptsAsserts.assertEqualsInternationalString(expected.getDerivation(), actual.getDerivation());
        ConceptsAsserts.assertEqualsInternationalString(expected.getLegalActs(), actual.getLegalActs());
        
        ConceptsAsserts.assertEqualsConcept(expected, actual);
    }
    
    public static void assertEqualsConceptSchemeMetamacDto(ConceptSchemeMetamacDto expected, ConceptSchemeMetamacDto actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEqualsExternalItemDto(expected.getRelatedOperation(), actual.getRelatedOperation());
        ConceptsAsserts.assertEqualsConceptSchemeDto(expected, actual);
    }

}
