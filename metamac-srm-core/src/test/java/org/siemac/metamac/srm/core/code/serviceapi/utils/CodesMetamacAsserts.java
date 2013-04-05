package org.siemac.metamac.srm.core.code.serviceapi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.DateUtils;
import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementOperation;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;

import com.arte.statistic.sdmx.srm.core.code.serviceapi.utils.CodesAsserts;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CodesMetamacAsserts extends CodesAsserts {

    // ------------------------------------------------------------------------------------
    // CODELIST
    // ------------------------------------------------------------------------------------

    public static void assertEqualsCodelist(CodelistVersionMetamac expected, CodelistVersionMetamac actual) {
        BaseAsserts.assertEqualsLifeCycle(expected.getLifeCycleMetadata(), actual.getLifeCycleMetadata());
        assertEqualsCodelistWithoutLifeCycleMetadata(expected, actual);
    }

    public static void assertEqualsCodelistWithoutLifeCycleMetadata(CodelistVersionMetamac expected, CodelistVersionMetamac actual) {
        // Metamac
        assertEqualsInternationalString(expected.getShortName(), actual.getShortName());
        assertEqualsInternationalString(expected.getDescriptionSource(), actual.getDescriptionSource());
        assertEquals(expected.getIsRecommended(), actual.getIsRecommended());
        assertEquals(expected.getAccessType(), actual.getAccessType());
        assertEqualsCodelistFamily(expected.getFamily(), actual.getFamily());
        assertEqualsVariable(expected.getVariable(), actual.getVariable());

        assertEqualsNullability(expected.getReplacedByCodelist(), actual.getReplacedByCodelist());
        if (expected.getReplacedByCodelist() != null) {
            assertEquals(expected.getReplacedByCodelist().getMaintainableArtefact().getUrn(), actual.getReplacedByCodelist().getMaintainableArtefact().getUrn());
        }
        // cannot check here due to flush restrictions in create method. we must remove relations before save
        // assertEquals(expected.getReplaceToCodelists().size(), actual.getReplaceToCodelists().size());
        // for (int i = 0; i < expected.getReplaceToCodelists().size(); i++) {
        // assertEquals(expected.getReplaceToCodelists().get(i).getMaintainableArtefact().getUrn(), actual.getReplaceToCodelists().get(i).getMaintainableArtefact().getUrn());
        // }

        // Sdmx
        CodesAsserts.assertEqualsCodelist(expected, actual);
    }

    public static void assertEqualsCodelistMetamacDto(CodelistMetamacDto expected, CodelistMetamacDto actual) {
        // Metamac
        assertEqualsInternationalStringDto(expected.getShortName(), actual.getShortName());
        assertEqualsInternationalStringDto(expected.getDescriptionSource(), actual.getDescriptionSource());
        assertEquals(expected.getIsRecommended(), actual.getIsRecommended());
        assertEquals(expected.getAccessType(), actual.getAccessType());
        BaseAsserts.assertEqualsRelatedResourceDto(expected.getReplacedByCodelist(), actual.getReplacedByCodelist());
        BaseAsserts.assertEqualsRelatedResourcesDto(expected.getReplaceToCodelists(), actual.getReplaceToCodelists());
        BaseAsserts.assertEqualsRelatedResourceDto(expected.getFamily(), actual.getFamily());
        BaseAsserts.assertEqualsRelatedResourceDto(expected.getVariable(), actual.getVariable());
        // SDMX
        CodesAsserts.assertEqualsCodelistDto(expected, actual);
    }
    public static void assertEqualsCodelist(CodelistVersionMetamac expected, CodelistMetamacDto actual) {
        assertEqualsCodelist(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsCodelist(CodelistMetamacDto expected, CodelistVersionMetamac actual) {
        assertEqualsCodelist(actual, expected, MapperEnum.DTO2DO);
    }

    private static void assertEqualsCodelist(CodelistVersionMetamac entity, CodelistMetamacDto dto, MapperEnum mapperEnum) {
        // Metamac
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            // generated by service
            BaseAsserts.assertEqualsLifeCycle(entity.getLifeCycleMetadata(), dto.getLifeCycle());
        }
        assertEqualsInternationalString(entity.getShortName(), dto.getShortName());
        assertEqualsInternationalString(entity.getDescriptionSource(), dto.getDescriptionSource());
        assertEquals(entity.getIsRecommended(), dto.getIsRecommended());
        assertEquals(entity.getAccessType(), dto.getAccessType());
        assertEqualsNullability(entity.getReplacedByCodelist(), dto.getReplacedByCodelist());
        if (entity.getReplacedByCodelist() != null) {
            assertEquals(entity.getReplacedByCodelist().getMaintainableArtefact().getUrn(), dto.getReplacedByCodelist().getUrn());
        }
        assertEquals(entity.getReplaceToCodelists().size(), dto.getReplaceToCodelists().size());
        for (int i = 0; i < entity.getReplaceToCodelists().size(); i++) {
            assertEquals(entity.getReplaceToCodelists().get(i).getMaintainableArtefact().getUrn(), dto.getReplaceToCodelists().get(i).getUrn());
        }
        assertEqualsCodelistFamilyRelatedResourceDto(entity.getFamily(), dto.getFamily(), mapperEnum);
        assertEqualsVariableRelatedResourceDto(entity.getVariable(), dto.getVariable(), mapperEnum);
        assertEqualsCodelistOpennessVisualisationResourceDto(entity.getDefaultOpennessVisualisation(), dto.getDefaultOpennessVisualisation(), mapperEnum);

        // SDMX
        CodesAsserts.assertEqualsCodelist(entity, dto, mapperEnum);
    }
    // ------------------------------------------------------------------------------------
    // CODE
    // ------------------------------------------------------------------------------------

    public static void assertEqualsCode(CodeMetamac expected, CodeMetamac actual) {
        // Metamac
        assertEqualsNullability(expected.getVariableElement(), actual.getVariableElement());
        if (expected.getVariableElement() != null) {
            assertEquals(expected.getVariableElement().getIdentifiableArtefact().getUrn(), actual.getVariableElement().getIdentifiableArtefact().getUrn());
        }
        assertEqualsInternationalString(expected.getShortName(), actual.getShortName());

        // SDMX
        CodesAsserts.assertEqualsCode(expected, actual);
    }

    public static void assertEqualsCodeDto(CodeMetamacDto expected, CodeMetamacDto actual) {
        // Metamac
        BaseAsserts.assertEqualsRelatedResourceDto(expected.getVariableElement(), actual.getVariableElement());
        assertEqualsInternationalStringDto(expected.getShortName(), actual.getShortName());

        // SDMX
        CodesAsserts.assertEqualsCodeDto(expected, actual);
    }

    public static void assertEqualsCode(CodeMetamac expected, CodeMetamacDto actual) {
        assertEqualsCode(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsCode(CodeMetamacDto expected, CodeMetamac actual) {
        assertEqualsCode(actual, expected, MapperEnum.DTO2DO);
    }

    private static void assertEqualsCode(CodeMetamac entity, CodeMetamacDto dto, MapperEnum mapperEnum) {
        // Metamac
        assertEqualsNullability(entity.getVariableElement(), dto.getVariableElement());
        if (entity.getVariableElement() != null) {
            assertEquals(entity.getVariableElement().getIdentifiableArtefact().getUrn(), dto.getVariableElement().getUrn());
        }
        assertEqualsInternationalString(entity.getShortName(), dto.getShortName());

        // SDMX
        CodesAsserts.assertEqualsCode(entity, dto, mapperEnum);
    }

    // ------------------------------------------------------------------------------------
    // CODELIST FAMILIES
    // ------------------------------------------------------------------------------------

    public static void assertEqualsCodelistFamily(CodelistFamily expected, CodelistFamily actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        // other artefacts
        assertEqualsNameableArtefact(expected.getNameableArtefact(), actual.getNameableArtefact());
    }

    public static void assertEqualsCodelistFamilyDto(CodelistFamilyDto expected, CodelistFamilyDto actual) {
        CodesAsserts.assertEqualsNameableArtefactDto(expected, actual);
    }

    public static void assertEqualsCodelistFamily(CodelistFamily expected, CodelistFamilyDto actual) {
        assertEqualsCodelistFamily(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsCodelistFamily(CodelistFamilyDto actual, CodelistFamily expected) {
        assertEqualsCodelistFamily(expected, actual, MapperEnum.DTO2DO);
    }

    private static void assertEqualsCodelistFamily(CodelistFamily entity, CodelistFamilyDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getUuid());
            assertEquals(entity.getUuid(), dto.getUuid());

            assertNotNull(entity.getCreatedBy());
            assertEquals(entity.getCreatedBy(), dto.getCreatedBy());

            assertNotNull(entity.getCreatedDate());
            assertTrue(DateUtils.isSameInstant(entity.getCreatedDate().toDate(), dto.getCreatedDate()));

            assertNotNull(entity.getLastUpdatedBy());
            assertEquals(entity.getLastUpdatedBy(), dto.getLastUpdatedBy());

            assertNotNull(entity.getLastUpdated());
            assertTrue(DateUtils.isSameInstant(entity.getLastUpdated().toDate(), dto.getLastUpdated()));

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());
            assertEquals(entity.getVersion(), dto.getVersionOptimisticLocking());
        }

        // other artefacts
        assertEqualsNameableArtefact(entity.getNameableArtefact(), dto, mapperEnum);
    }

    private static void assertEqualsCodelistFamilyRelatedResourceDto(CodelistFamily entity, RelatedResourceDto dto, MapperEnum mapperEnum) {
        assertEqualsNullability(entity, dto);
        if (entity == null) {
            return;
        }
        assertEquals(entity.getNameableArtefact().getCode(), dto.getCode());
        assertEquals(entity.getNameableArtefact().getUrn(), dto.getUrn());
        assertEquals(entity.getNameableArtefact().getUrnProvider(), dto.getUrnProvider());
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ------------------------------------------------------------------------------------

    public static void assertEqualsVariableFamily(VariableFamily expected, VariableFamily actual) {
        // other artefacts
        assertEqualsNameableArtefact(expected.getNameableArtefact(), actual.getNameableArtefact());
    }

    public static void assertEqualsVariableFamilyDto(VariableFamilyDto expected, VariableFamilyDto actual) {
        CodesAsserts.assertEqualsNameableArtefactDto(expected, actual);
    }

    public static void assertEqualsVariableFamily(VariableFamily expected, VariableFamilyDto actual) {
        assertEqualsVariableFamily(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsVariableFamily(VariableFamilyDto actual, VariableFamily expected) {
        assertEqualsVariableFamily(expected, actual, MapperEnum.DTO2DO);
    }

    private static void assertEqualsVariableFamily(VariableFamily entity, VariableFamilyDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getUuid());
            assertEquals(entity.getUuid(), dto.getUuid());

            assertNotNull(entity.getCreatedBy());
            assertEquals(entity.getCreatedBy(), dto.getCreatedBy());

            assertNotNull(entity.getCreatedDate());
            assertTrue(DateUtils.isSameInstant(entity.getCreatedDate().toDate(), dto.getCreatedDate()));

            assertNotNull(entity.getLastUpdatedBy());
            assertEquals(entity.getLastUpdatedBy(), dto.getLastUpdatedBy());

            assertNotNull(entity.getLastUpdated());
            assertTrue(DateUtils.isSameInstant(entity.getLastUpdated().toDate(), dto.getLastUpdated()));

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());
            assertEquals(entity.getVersion(), dto.getVersionOptimisticLocking());
        }

        // other artefacts
        assertEqualsNameableArtefact(entity.getNameableArtefact(), dto, mapperEnum);
    }

    // ------------------------------------------------------------------------------------
    // VARIABLES
    // ------------------------------------------------------------------------------------

    public static void assertEqualsVariable(Variable expected, Variable actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEqualsInternationalString(expected.getShortName(), actual.getShortName());
        assertEqualsDate(expected.getValidFrom(), actual.getValidFrom());
        assertEqualsDate(expected.getValidTo(), actual.getValidTo());
        assertEquals(expected.getFamilies().size(), actual.getFamilies().size());
        for (int i = 0; i < expected.getFamilies().size(); i++) {
            assertEquals(expected.getFamilies().get(i).getNameableArtefact().getUrn(), actual.getFamilies().get(i).getNameableArtefact().getUrn());
        }
        assertEqualsNullability(expected.getReplacedByVariable(), actual.getReplacedByVariable());
        if (expected.getReplacedByVariable() != null) {
            assertEquals(expected.getReplacedByVariable().getNameableArtefact().getUrn(), actual.getReplacedByVariable().getNameableArtefact().getUrn());
        }
        // cannot check here due to flush restrictions in create method. we must remove relations before save
        // assertEquals(expected.getReplaceToVariables().size(), actual.getReplaceToVariables().size());
        // for (int i = 0; i < expected.getReplaceToVariables().size(); i++) {
        // assertEquals(expected.getReplaceToVariables().get(i).getNameableArtefact().getUrn(), actual.getReplaceToVariables().get(i).getNameableArtefact().getUrn());
        // }

        // other artefacts
        assertEqualsNameableArtefact(expected.getNameableArtefact(), actual.getNameableArtefact());
    }

    public static void assertEqualsVariableDto(VariableDto expected, VariableDto actual) {
        assertEqualsInternationalStringDto(expected.getShortName(), actual.getShortName());
        assertEquals(expected.getValidFrom(), actual.getValidFrom());
        assertEquals(expected.getValidTo(), actual.getValidTo());
        BaseAsserts.assertEqualsRelatedResourcesDto(expected.getFamilies(), actual.getFamilies());
        BaseAsserts.assertEqualsRelatedResourceDto(expected.getReplacedByVariable(), actual.getReplacedByVariable());
        BaseAsserts.assertEqualsRelatedResourcesDto(expected.getReplaceToVariables(), actual.getReplaceToVariables());

        // other artefacts
        CodesAsserts.assertEqualsNameableArtefactDto(expected, actual);
    }

    public static void assertEqualsVariable(Variable expected, VariableDto actual) {
        assertEqualsVariable(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsVariable(VariableDto actual, Variable expected) {
        assertEqualsVariable(expected, actual, MapperEnum.DTO2DO);
    }

    private static void assertEqualsVariable(Variable entity, VariableDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getUuid());
            assertEquals(entity.getUuid(), dto.getUuid());

            assertNotNull(entity.getCreatedBy());
            assertEquals(entity.getCreatedBy(), dto.getCreatedBy());

            assertNotNull(entity.getCreatedDate());
            assertTrue(DateUtils.isSameInstant(entity.getCreatedDate().toDate(), dto.getCreatedDate()));

            assertNotNull(entity.getLastUpdatedBy());
            assertEquals(entity.getLastUpdatedBy(), dto.getLastUpdatedBy());

            assertNotNull(entity.getLastUpdated());
            assertTrue(DateUtils.isSameInstant(entity.getLastUpdated().toDate(), dto.getLastUpdated()));

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());
            assertEquals(entity.getVersion(), dto.getVersionOptimisticLocking());
        }

        assertEqualsInternationalString(entity.getShortName(), dto.getShortName());
        assertEqualsDate(entity.getValidFrom(), dto.getValidFrom());
        assertEqualsDate(entity.getValidTo(), dto.getValidTo());
        assertEquals(entity.getFamilies().size(), dto.getFamilies().size());
        for (int i = 0; i < entity.getFamilies().size(); i++) {
            assertEquals(entity.getFamilies().get(i).getNameableArtefact().getUrn(), dto.getFamilies().get(i).getUrn());
        }
        assertEqualsNullability(entity.getReplacedByVariable(), dto.getReplacedByVariable());
        if (entity.getReplacedByVariable() != null) {
            assertEquals(entity.getReplacedByVariable().getNameableArtefact().getUrn(), dto.getReplacedByVariable().getUrn());
        }
        assertEquals(entity.getReplaceToVariables().size(), dto.getReplaceToVariables().size());
        for (int i = 0; i < entity.getReplaceToVariables().size(); i++) {
            assertEquals(entity.getReplaceToVariables().get(i).getNameableArtefact().getUrn(), dto.getReplaceToVariables().get(i).getUrn());
        }

        // other artefacts
        assertEqualsNameableArtefact(entity.getNameableArtefact(), dto, mapperEnum);
    }

    public static void assertEqualsVariableRelatedResourceDto(Variable entity, RelatedResourceDto dto, MapperEnum mapperEnum) {
        assertEqualsNullability(entity, dto);
        if (entity == null) {
            return;
        }
        assertEquals(entity.getNameableArtefact().getUrn(), dto.getUrn());
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ------------------------------------------------------------------------------------

    public static void assertEqualsVariableElement(VariableElement expected, VariableElement actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEqualsInternationalString(expected.getShortName(), actual.getShortName());
        assertEqualsDate(expected.getValidFrom(), actual.getValidFrom());
        assertEqualsDate(expected.getValidTo(), actual.getValidTo());
        assertEquals(expected.getVariable().getNameableArtefact().getUrn(), actual.getVariable().getNameableArtefact().getUrn());

        assertEqualsNullability(expected.getReplacedByVariableElement(), actual.getReplacedByVariableElement());
        if (expected.getReplacedByVariableElement() != null) {
            assertEquals(expected.getReplacedByVariableElement().getIdentifiableArtefact().getUrn(), actual.getReplacedByVariableElement().getIdentifiableArtefact().getUrn());
        }
        // cannot check here due to flush restrictions in create method. we must remove relations before save
        // assertEquals(expected.getReplaceToVariableElements().size(), actual.getReplaceToVariableElements().size());
        // for (int i = 0; i < expected.getReplaceToVariableElements().size(); i++) {
        // assertEquals(expected.getReplaceToVariableElements().get(i).getNameableArtefact().getUrn(), actual.getReplaceToVariableElements().get(i).getNameableArtefact().getUrn());
        // }

        // other artefacts
        assertEqualsIdentifiableArtefact(expected.getIdentifiableArtefact(), actual.getIdentifiableArtefact());
    }

    public static void assertEqualsVariableElementDto(VariableElementDto expected, VariableElementDto actual) {
        assertEqualsInternationalStringDto(expected.getShortName(), actual.getShortName());
        assertEquals(expected.getValidFrom(), actual.getValidFrom());
        assertEquals(expected.getValidTo(), actual.getValidTo());
        BaseAsserts.assertEqualsRelatedResourceDto(expected.getVariable(), actual.getVariable());
        BaseAsserts.assertEqualsRelatedResourceDto(expected.getReplacedByVariableElement(), actual.getReplacedByVariableElement());
        BaseAsserts.assertEqualsRelatedResourcesDto(expected.getReplaceToVariableElements(), actual.getReplaceToVariableElements());

        // other artefacts
        CodesAsserts.assertEqualsIdentifiableArtefactDto(expected, actual);
    }

    public static void assertEqualsVariableElement(VariableElement expected, VariableElementDto actual) {
        assertEqualsVariableElement(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsVariableElement(VariableElementDto actual, VariableElement expected) {
        assertEqualsVariableElement(expected, actual, MapperEnum.DTO2DO);
    }

    private static void assertEqualsVariableElement(VariableElement entity, VariableElementDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getUuid());
            assertEquals(entity.getUuid(), dto.getUuid());

            assertNotNull(entity.getCreatedBy());
            assertEquals(entity.getCreatedBy(), dto.getCreatedBy());

            assertNotNull(entity.getCreatedDate());
            assertTrue(DateUtils.isSameInstant(entity.getCreatedDate().toDate(), dto.getCreatedDate()));

            assertNotNull(entity.getLastUpdatedBy());
            assertEquals(entity.getLastUpdatedBy(), dto.getLastUpdatedBy());

            assertNotNull(entity.getLastUpdated());
            assertTrue(DateUtils.isSameInstant(entity.getLastUpdated().toDate(), dto.getLastUpdated()));

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());
            assertEquals(entity.getVersion(), dto.getVersionOptimisticLocking());
        }

        assertEqualsInternationalString(entity.getShortName(), dto.getShortName());
        assertEqualsDate(entity.getValidFrom(), dto.getValidFrom());
        assertEqualsDate(entity.getValidTo(), dto.getValidTo());
        assertEquals(entity.getVariable().getNameableArtefact().getUrn(), dto.getVariable().getUrn());
        assertEqualsNullability(entity.getReplacedByVariableElement(), dto.getReplacedByVariableElement());
        if (entity.getReplacedByVariableElement() != null) {
            assertEquals(entity.getReplacedByVariableElement().getIdentifiableArtefact().getUrn(), dto.getReplacedByVariableElement().getUrn());
        }
        assertEquals(entity.getReplaceToVariableElements().size(), dto.getReplaceToVariableElements().size());
        for (int i = 0; i < entity.getReplaceToVariableElements().size(); i++) {
            assertEquals(entity.getReplaceToVariableElements().get(i).getIdentifiableArtefact().getUrn(), dto.getReplaceToVariableElements().get(i).getUrn());
        }

        // other artefacts
        assertEqualsIdentifiableArtefact(entity.getIdentifiableArtefact(), dto, mapperEnum);
    }

    public static void assertEqualsVariableElementRelatedResourceDto(VariableElement entity, RelatedResourceDto dto, MapperEnum mapperEnum) {
        assertEqualsNullability(entity, dto);
        if (entity == null) {
            return;
        }
        assertEquals(entity.getIdentifiableArtefact().getUrn(), dto.getUrn());
    }

    public static void assertEqualsVariableElementOperation(VariableElementOperation expected, VariableElementOperationDto actual) {
        assertEqualsVariableElementOperation(expected, actual, MapperEnum.DO2DTO);
    }

    private static void assertEqualsVariableElementOperation(VariableElementOperation entity, VariableElementOperationDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getUuid());
            assertEquals(entity.getUuid(), dto.getUuid());

            assertNotNull(entity.getCreatedBy());
            assertEquals(entity.getCreatedBy(), dto.getCreatedBy());

            assertNotNull(entity.getCreatedDate());
            assertTrue(DateUtils.isSameInstant(entity.getCreatedDate().toDate(), dto.getCreatedDate()));

            assertNotNull(entity.getLastUpdatedBy());
            assertEquals(entity.getLastUpdatedBy(), dto.getLastUpdatedBy());

            assertNotNull(entity.getLastUpdated());
            assertTrue(DateUtils.isSameInstant(entity.getLastUpdated().toDate(), dto.getLastUpdated()));

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());
            assertEquals(entity.getVersion(), dto.getVersionOptimisticLocking());
        }

        assertEquals(entity.getCode(), dto.getCode());
        assertEquals(entity.getOperationType(), dto.getOperationType());
        assertEquals(entity.getVariable().getNameableArtefact().getUrn(), dto.getVariable().getUrn());
        assertEquals(entity.getSources().size(), dto.getSources().size());
        for (int i = 0; i < entity.getSources().size(); i++) {
            assertEquals(entity.getSources().get(i).getIdentifiableArtefact().getUrn(), dto.getSources().get(i).getUrn());
        }
        assertEquals(entity.getTargets().size(), dto.getTargets().size());
        for (int i = 0; i < entity.getTargets().size(); i++) {
            assertEquals(entity.getTargets().get(i).getIdentifiableArtefact().getUrn(), dto.getTargets().get(i).getUrn());
        }
    }

    // ------------------------------------------------------------------------------------
    // ORDER VISUALISATIONS
    // ------------------------------------------------------------------------------------

    public static void assertEqualsCodelistOrderVisualisation(CodelistOrderVisualisation expected, CodelistOrderVisualisation actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        // other artefacts
        assertEqualsNameableArtefact(expected.getNameableArtefact(), actual.getNameableArtefact());
    }

    public static void assertEqualsCodelistOrderVisualisation(CodelistOrderVisualisation expected, CodelistVisualisationDto actual) {
        assertEqualsCodelistOrderVisualisation(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsCodelistOrderVisualisation(CodelistVisualisationDto expected, CodelistOrderVisualisation actual) {
        assertEqualsCodelistOrderVisualisation(actual, expected, MapperEnum.DTO2DO);
    }

    public static void assertEqualsCodelistOrderVisualisationDto(CodelistVisualisationDto expected, CodelistVisualisationDto actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        // other artefacts
        CodesAsserts.assertEqualsNameableArtefactDto(expected, actual);
    }

    private static void assertEqualsCodelistOrderVisualisation(CodelistOrderVisualisation entity, CodelistVisualisationDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getUuid());
            assertEquals(entity.getUuid(), dto.getUuid());

            assertNotNull(entity.getCreatedBy());
            assertEquals(entity.getCreatedBy(), dto.getCreatedBy());

            assertNotNull(entity.getCreatedDate());
            assertTrue(DateUtils.isSameInstant(entity.getCreatedDate().toDate(), dto.getCreatedDate()));

            assertNotNull(entity.getLastUpdatedBy());
            assertEquals(entity.getLastUpdatedBy(), dto.getLastUpdatedBy());

            assertNotNull(entity.getLastUpdated());
            assertTrue(DateUtils.isSameInstant(entity.getLastUpdated().toDate(), dto.getLastUpdated()));

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());
            assertEquals(entity.getVersion(), dto.getVersionOptimisticLocking());
        }

        // other artefacts
        assertEqualsNameableArtefact(entity.getNameableArtefact(), dto, mapperEnum);
    }

    public static void assertEqualsCodelistOrderVisualisationResourceDto(CodelistOrderVisualisation entity, RelatedResourceDto dto, MapperEnum mapperEnum) {
        assertEqualsNullability(entity, dto);
        if (entity == null) {
            return;
        }
        assertEquals(entity.getNameableArtefact().getUrn(), dto.getUrn());
    }

    // ------------------------------------------------------------------------------------
    // OPENNESS VISUALISATIONS
    // ------------------------------------------------------------------------------------

    public static void assertEqualsCodelistOpennessVisualisation(CodelistOpennessVisualisation expected, CodelistOpennessVisualisation actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        // other artefacts
        assertEqualsNameableArtefact(expected.getNameableArtefact(), actual.getNameableArtefact());
    }

    public static void assertEqualsCodelistOpennessVisualisation(CodelistOpennessVisualisation expected, CodelistVisualisationDto actual) {
        assertEqualsCodelistOpennessVisualisation(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsCodelistOpennessVisualisation(CodelistVisualisationDto expected, CodelistOpennessVisualisation actual) {
        assertEqualsCodelistOpennessVisualisation(actual, expected, MapperEnum.DTO2DO);
    }

    public static void assertEqualsCodelistOpennessVisualisationDto(CodelistVisualisationDto expected, CodelistVisualisationDto actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        // other artefacts
        CodesAsserts.assertEqualsNameableArtefactDto(expected, actual);
    }

    private static void assertEqualsCodelistOpennessVisualisation(CodelistOpennessVisualisation entity, CodelistVisualisationDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getUuid());
            assertEquals(entity.getUuid(), dto.getUuid());

            assertNotNull(entity.getCreatedBy());
            assertEquals(entity.getCreatedBy(), dto.getCreatedBy());

            assertNotNull(entity.getCreatedDate());
            assertTrue(DateUtils.isSameInstant(entity.getCreatedDate().toDate(), dto.getCreatedDate()));

            assertNotNull(entity.getLastUpdatedBy());
            assertEquals(entity.getLastUpdatedBy(), dto.getLastUpdatedBy());

            assertNotNull(entity.getLastUpdated());
            assertTrue(DateUtils.isSameInstant(entity.getLastUpdated().toDate(), dto.getLastUpdated()));

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());
            assertEquals(entity.getVersion(), dto.getVersionOptimisticLocking());
        }

        // other artefacts
        assertEqualsNameableArtefact(entity.getNameableArtefact(), dto, mapperEnum);
    }

    public static void assertEqualsCodelistOpennessVisualisationResourceDto(CodelistOpennessVisualisation entity, RelatedResourceDto dto, MapperEnum mapperEnum) {
        assertEqualsNullability(entity, dto);
        if (entity == null) {
            return;
        }
        assertEquals(entity.getNameableArtefact().getUrn(), dto.getUrn());
    }
}
