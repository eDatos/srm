package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsExternalItemDto;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsInternationalStringDto;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsAsserts;

public class ConceptsMetamacAsserts extends ConceptsAsserts {

    public static void assertEqualsConceptScheme(ConceptSchemeVersionMetamac expected, ConceptSchemeVersionMetamac actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEqualsExternalItem(expected.getRelatedOperation(), actual.getRelatedOperation());
        ConceptsAsserts.assertEqualsConceptScheme(expected, actual);
    }

    public static void assertEqualsConcept(ConceptMetamac expected, ConceptMetamac actual) {
        assertEqualsInternationalString(expected.getPluralName(), actual.getPluralName());
        assertEqualsInternationalString(expected.getAcronym(), actual.getAcronym());
        assertEqualsInternationalString(expected.getDescriptionSource(), actual.getDescriptionSource());
        assertEqualsInternationalString(expected.getContext(), actual.getContext());
        assertEqualsInternationalString(expected.getDocMethod(), actual.getDocMethod());
        assertEquals(expected.getSdmxRelatedArtefact(), actual.getSdmxRelatedArtefact());
        assertEqualsConceptType(expected.getType(), actual.getType());
        assertEqualsInternationalString(expected.getDerivation(), actual.getDerivation());
        assertEqualsInternationalString(expected.getLegalActs(), actual.getLegalActs());

        ConceptsAsserts.assertEqualsConcept(expected, actual);
    }

    public static void assertEqualsConceptSchemeMetamacDto(ConceptSchemeMetamacDto expected, ConceptSchemeMetamacDto actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEqualsExternalItemDto(expected.getRelatedOperation(), actual.getRelatedOperation());
        ConceptsAsserts.assertEqualsConceptSchemeDto(expected, actual);
    }

    public static void assertEqualsConceptDto(ConceptMetamacDto expected, ConceptMetamacDto actual) {
        assertEqualsInternationalStringDto(expected.getPluralName(), actual.getPluralName());
        assertEqualsInternationalStringDto(expected.getAcronym(), actual.getAcronym());
        assertEqualsInternationalStringDto(expected.getDescriptionSource(), actual.getDescriptionSource());
        assertEqualsInternationalStringDto(expected.getContext(), actual.getContext());
        assertEqualsInternationalStringDto(expected.getDocMethod(), actual.getDocMethod());
        assertEquals(expected.getSdmxRelatedArtefact(), actual.getSdmxRelatedArtefact());
        // TODO type
        assertEqualsInternationalStringDto(expected.getDerivation(), actual.getDerivation());
        assertEqualsInternationalStringDto(expected.getLegalActs(), actual.getLegalActs());

        ConceptsAsserts.assertEqualsConceptDto(expected, actual);
    }
    
    private static void assertEqualsConceptType(ConceptType expected, ConceptType actual) {
        if (expected == null && actual == null) {
            return;
        } else if (expected != null && actual != null) {
            assertEquals(expected.getIdentifier(), actual.getIdentifier());
        } else {
            fail("ConceptType are different");
        }
    }
}
