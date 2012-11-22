package org.siemac.metamac.srm.rest.internal.v1_0.concepts.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptType;

public class ConceptsAsserts extends Asserts {
    

    public static void assertEqualsConceptScheme(ConceptSchemeVersionMetamac source, ConceptScheme target) {
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes";
        String selfLink = parentLink + "/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + source.getMaintainableArtefact().getCode() + "/"
                + source.getMaintainableArtefact().getVersionLogic();
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertEquals(source.getType().toString(), target.getType().toString());
        MetamacAsserts.assertEqualsNullability(source.getRelatedOperation(), target.getRelatedOperation());
        if (source.getRelatedOperation() != null) {
            assertEquals(source.getRelatedOperation().getUrn(), target.getRelatedOperation().getUrn());
        }
        assertEquals(source.getMaintainableArtefact().getReplaceToVersion(), target.getReplaceToVersion());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/concepts", target.getChildLinks().getChildLinks().get(0).getHref());

        // Concepts (SDMX type)
        assertEquals(source.getItems().size(), target.getConcepts().size());
        for (int i = 0; i < source.getItems().size(); i++) {
            assertTrue(target.getConcepts().get(i) instanceof ConceptType);
            assertFalse(target.getConcepts().get(i) instanceof Concept);

            assertEqualsConceptSdmx((ConceptMetamac) source.getItems().get(i), target.getConcepts().get(i));
        }
    }
    public static void assertEqualsConceptSdmx(ConceptMetamac source, ConceptType target) {
        // Only test some metadata because SDMX metadata is tested in SDMX project
        // Test something...
        assertEquals(source.getNameableArtefact().getCode(), target.getId());
        assertEquals(source.getNameableArtefact().getUrn(), target.getUrn());
        assertEqualsNullability(source.getParent(), target.getParent());
        if (source.getParent() != null) {
            assertEquals(source.getParent().getNameableArtefact().getCode(), target.getParent().getRef().getId());
        }
    }

    public static void assertEqualsConcept(ConceptMetamac source, Concept target) {

        assertEquals(RestInternalConstants.KIND_CONCEPT, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes" + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/"
                + source.getItemSchemeVersion().getMaintainableArtefact().getCode() + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getVersionLogic() + "/concepts";
        String selfLink = parentLink + "/" + source.getNameableArtefact().getCode();
        assertEquals(RestInternalConstants.KIND_CONCEPT, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        assertEqualsInternationalStringNotNull(source.getPluralName(), target.getPluralName());
        assertEqualsInternationalStringNotNull(source.getAcronym(), target.getAcronym());
        assertEqualsInternationalStringNotNull(source.getDescriptionSource(), target.getDescriptionSource());
        assertEqualsInternationalStringNotNull(source.getContext(), target.getContext());
        assertEqualsInternationalStringNotNull(source.getDocMethod(), target.getDocMethod());
        assertEqualsInternationalStringNotNull(source.getDerivation(), target.getDerivation());
        assertEqualsInternationalStringNotNull(source.getLegalActs(), target.getLegalActs());

        assertEquals(source.getType().getIdentifier(), target.getType().getId());
        assertEqualsInternationalStringNotNull(source.getType().getDescription(), target.getType().getTitle());

        assertEqualsUrnsNotNull(source.getRoleConcepts(), target.getRoles());
        assertEqualsUrnsNotNull(source.getRelatedConcepts(), target.getRelatedConcepts());
        assertEquals(source.getConceptExtends().getNameableArtefact().getUrn(), target.getExtends());

        // Sdmx
        assertEqualsConceptSdmx(source, target);
    }

    public static void assertEqualsResource(ConceptSchemeVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEqualsResource(expected, RestInternalConstants.KIND_CONCEPT_SCHEME, expectedSelfLink, actual);
    }
    
    public static void assertEqualsResource(ConceptMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic() + "/concepts/" + expected.getNameableArtefact().getCode();
        assertEqualsResource(expected, RestInternalConstants.KIND_CONCEPT, expectedSelfLink, actual);
    }
}