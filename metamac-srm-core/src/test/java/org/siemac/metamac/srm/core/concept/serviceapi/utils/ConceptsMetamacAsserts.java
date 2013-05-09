package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import static org.junit.Assert.assertEquals;

import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsAsserts;

public class ConceptsMetamacAsserts extends ConceptsAsserts {

    public static void assertEqualsConceptScheme(ConceptSchemeVersionMetamac expected, ConceptSchemeVersionMetamac actual) {

        // Metamac
        assertEquals(expected.getType(), actual.getType());
        assertEqualsExternalItem(expected.getRelatedOperation(), actual.getRelatedOperation());

        // Sdmx
        ConceptsAsserts.assertEqualsConceptScheme(expected, actual);
    }

    public static void assertEqualsConceptSchemeMetamacDto(ConceptSchemeMetamacDto expected, ConceptSchemeMetamacDto actual) {

        // Metamac
        assertEquals(expected.getType(), actual.getType());
        assertEqualsExternalItemDto(expected.getRelatedOperation(), actual.getRelatedOperation());

        // Sdmx
        ConceptsAsserts.assertEqualsConceptSchemeDto(expected, actual);
    }

    public static void assertEqualsConceptScheme(ConceptSchemeVersionMetamac expected, ConceptSchemeMetamacBasicDto actual) {
        BaseAsserts.assertEqualsItemSchemeBasicDto(expected, expected.getLifeCycleMetadata(), actual);
    }

    public static void assertEqualsConceptScheme(ConceptSchemeVersionMetamac expected, ConceptSchemeMetamacDto actual) {
        assertEqualsConceptScheme(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsConceptScheme(ConceptSchemeMetamacDto expected, ConceptSchemeVersionMetamac actual) {
        assertEqualsConceptScheme(actual, expected, MapperEnum.DTO2DO);
    }

    public static void assertEqualsConcept(ConceptMetamac expected, ConceptMetamac actual) {

        // Metamac
        assertEqualsInternationalString(expected.getPluralName(), actual.getPluralName());
        assertEqualsInternationalString(expected.getAcronym(), actual.getAcronym());
        assertEqualsInternationalString(expected.getDescriptionSource(), actual.getDescriptionSource());
        assertEqualsInternationalString(expected.getContext(), actual.getContext());
        assertEqualsInternationalString(expected.getDocMethod(), actual.getDocMethod());
        assertEquals(expected.getSdmxRelatedArtefact(), actual.getSdmxRelatedArtefact());
        assertEqualsConceptType(expected.getConceptType(), actual.getConceptType());
        assertEqualsInternationalString(expected.getDerivation(), actual.getDerivation());
        assertEqualsInternationalString(expected.getLegalActs(), actual.getLegalActs());
        assertEqualsConceptExtends(expected.getConceptExtends(), actual.getConceptExtends());
        CodesMetamacAsserts.assertEqualsVariable(expected.getVariable(), actual.getVariable());

        // Sdmx
        ConceptsAsserts.assertEqualsConcept(expected, actual);
    }

    public static void assertEqualsConceptDto(ConceptMetamacDto expected, ConceptMetamacDto actual) {

        // Metamac
        assertEqualsInternationalStringDto(expected.getPluralName(), actual.getPluralName());
        assertEqualsInternationalStringDto(expected.getAcronym(), actual.getAcronym());
        assertEqualsInternationalStringDto(expected.getDescriptionSource(), actual.getDescriptionSource());
        assertEqualsInternationalStringDto(expected.getContext(), actual.getContext());
        assertEqualsInternationalStringDto(expected.getDocMethod(), actual.getDocMethod());
        assertEquals(expected.getSdmxRelatedArtefact(), actual.getSdmxRelatedArtefact());
        assertEqualsConceptType(expected.getConceptType(), actual.getConceptType());
        assertEqualsInternationalStringDto(expected.getDerivation(), actual.getDerivation());
        assertEqualsInternationalStringDto(expected.getLegalActs(), actual.getLegalActs());
        BaseAsserts.assertEqualsRelatedResourceDto(expected.getConceptExtends(), actual.getConceptExtends());
        BaseAsserts.assertEqualsRelatedResourceDto(expected.getVariable(), actual.getVariable());

        // Sdmx
        ConceptsAsserts.assertEqualsConceptDto(expected, actual);
    }

    public static void assertEqualsConcept(ConceptMetamac expected, ConceptMetamacDto actual) {
        assertEqualsConcept(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsConcept(ConceptMetamacDto expected, ConceptMetamac actual) {
        assertEqualsConcept(actual, expected, MapperEnum.DTO2DO);
    }

    public static void assertEqualsConcept(ConceptMetamac expected, ConceptMetamacBasicDto actual) {
        BaseAsserts.assertEqualsItemBasicDto(expected, actual);
    }

    public static void assertEqualsConceptType(ConceptType expected, ConceptTypeDto actual) {
        assertEqualsNullability(expected, actual);
        if (expected != null) {
            assertEquals(expected.getIdentifier(), actual.getIdentifier());
            assertEqualsInternationalString(expected.getDescription(), actual.getDescription());
        }
    }

    private static void assertEqualsConceptType(ConceptType expected, ConceptType actual) {
        assertEqualsNullability(expected, actual);
        if (expected != null) {
            assertEquals(expected.getIdentifier(), actual.getIdentifier());
        }
    }

    private static void assertEqualsConceptType(ConceptTypeDto expected, ConceptTypeDto actual) {
        assertEqualsNullability(expected, actual);
        if (expected != null) {
            assertEquals(expected.getIdentifier(), actual.getIdentifier());
            assertEqualsInternationalStringDto(expected.getDescription(), actual.getDescription());
        }
    }

    private static void assertEqualsConceptExtends(ConceptMetamac expected, ConceptMetamac actual) {
        assertEqualsNullability(expected, actual);
        if (expected != null) {
            assertEquals(expected.getNameableArtefact().getUrn(), actual.getNameableArtefact().getUrn());
        }
    }

    private static void assertEqualsConceptScheme(ConceptSchemeVersionMetamac entity, ConceptSchemeMetamacDto dto, MapperEnum mapperEnum) {

        // Metamac
        assertEquals(entity.getType(), dto.getType());
        assertEqualsExternalItem(entity.getRelatedOperation(), dto.getRelatedOperation());

        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            // generated by service
            BaseAsserts.assertEqualsLifeCycle(entity.getLifeCycleMetadata(), dto.getLifeCycle());
        }

        // Sdmx
        ConceptsAsserts.assertEqualsConceptScheme(entity, dto, mapperEnum);
    }

    private static void assertEqualsConcept(ConceptMetamac entity, ConceptMetamacDto dto, MapperEnum mapperEnum) {

        // Metamac
        assertEqualsInternationalString(entity.getPluralName(), dto.getPluralName());
        assertEqualsInternationalString(entity.getAcronym(), dto.getAcronym());
        assertEqualsInternationalString(entity.getDescriptionSource(), dto.getDescriptionSource());
        assertEqualsInternationalString(entity.getContext(), dto.getContext());
        assertEqualsInternationalString(entity.getDocMethod(), dto.getDocMethod());
        assertEquals(entity.getSdmxRelatedArtefact(), dto.getSdmxRelatedArtefact());
        assertEquals(entity.getConceptType().getIdentifier(), dto.getConceptType().getIdentifier());
        assertEqualsInternationalString(entity.getDerivation(), dto.getDerivation());
        assertEqualsInternationalString(entity.getLegalActs(), dto.getLegalActs());
        assertEqualsNullability(entity.getConceptExtends(), dto.getConceptExtends());
        if (entity.getConceptExtends() != null) {
            assertEquals(entity.getConceptExtends().getNameableArtefact().getUrn(), dto.getConceptExtends().getUrn());
        }
        CodesMetamacAsserts.assertEqualsVariableRelatedResourceDto(entity.getVariable(), dto.getVariable(), mapperEnum);

        // Sdmx
        ConceptsAsserts.assertEqualsConcept(entity, dto, mapperEnum);
    }
}
