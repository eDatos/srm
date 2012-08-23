package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsExternalItemDto;

import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsAsserts;

public class ConceptsMetamacAsserts extends ConceptsAsserts {

    public static void assertEqualsConceptScheme(ConceptSchemeVersionMetamac expected, ConceptSchemeVersionMetamac actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEqualsExternalItem(expected.getRelatedOperation(), actual.getRelatedOperation());
        ConceptsAsserts.assertEqualsConceptScheme(expected, actual);
    }

    public static void assertEqualsConceptSchemeMetamacDto(ConceptSchemeMetamacDto expected, ConceptSchemeMetamacDto actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEqualsExternalItemDto(expected.getRelatedOperation(), actual.getRelatedOperation());
        ConceptsAsserts.assertEqualsConceptSchemeDto(expected, actual);
    }

}
