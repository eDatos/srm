package org.siemac.metamac.srm.core.base.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.IdentifiableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.ItemMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.ItemSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.base.dto.MaintainableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.NameableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.StructureMetamacBasicDto;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class BaseAsserts extends com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts {

    // LIFE CYCLE

    public static void assertEqualsLifeCycleDto(LifeCycleDto expected, LifeCycleDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getProcStatus(), actual.getProcStatus());
        assertEquals(expected.getProductionValidationDate(), actual.getProductionValidationDate());
        assertEquals(expected.getProductionValidationUser(), actual.getProductionValidationUser());
        assertEquals(expected.getDiffusionValidationDate(), actual.getDiffusionValidationDate());
        assertEquals(expected.getDiffusionValidationUser(), actual.getDiffusionValidationUser());
        assertEquals(expected.getInternalPublicationDate(), actual.getInternalPublicationDate());
        assertEquals(expected.getInternalPublicationUser(), actual.getInternalPublicationUser());
        assertEquals(expected.getExternalPublicationDate(), actual.getExternalPublicationDate());
        assertEquals(expected.getExternalPublicationUser(), actual.getExternalPublicationUser());
    }

    public static void assertEqualsLifeCycle(SrmLifeCycleMetadata entity, LifeCycleDto dto) {
        assertNotNull(entity);
        assertNotNull(dto);
        assertEquals(entity.getProcStatus(), dto.getProcStatus());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(entity.getProductionValidationDate()), dto.getProductionValidationDate());
        assertEquals(entity.getProductionValidationUser(), dto.getProductionValidationUser());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(entity.getDiffusionValidationDate()), dto.getDiffusionValidationDate());
        assertEquals(entity.getDiffusionValidationUser(), dto.getDiffusionValidationUser());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(entity.getInternalPublicationDate()), dto.getInternalPublicationDate());
        assertEquals(entity.getInternalPublicationUser(), dto.getInternalPublicationUser());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(entity.getExternalPublicationDate()), dto.getExternalPublicationDate());
        assertEquals(entity.getExternalPublicationUser(), dto.getExternalPublicationUser());
    }

    public static void assertEqualsLifeCycle(SrmLifeCycleMetadata expected, SrmLifeCycleMetadata actual) {
        assertEquals(expected.getProcStatus(), actual.getProcStatus());
        assertEquals(expected.getProductionValidationDate(), actual.getProductionValidationDate());
        assertEquals(expected.getProductionValidationUser(), actual.getProductionValidationUser());
        assertEquals(expected.getDiffusionValidationDate(), actual.getDiffusionValidationDate());
        assertEquals(expected.getDiffusionValidationUser(), actual.getDiffusionValidationUser());
        assertEquals(expected.getInternalPublicationDate(), actual.getInternalPublicationDate());
        assertEquals(expected.getInternalPublicationUser(), actual.getInternalPublicationUser());
        assertEquals(expected.getExternalPublicationDate(), actual.getExternalPublicationDate());
        assertEquals(expected.getExternalPublicationUser(), actual.getExternalPublicationUser());
    }

    public static void assertEqualsRelatedResourceDto(RelatedResourceDto expected, RelatedResourceDto actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.getUrn(), actual.getUrn());
    }

    public static void assertEqualsRelatedResourcesDto(List<RelatedResourceDto> expecteds, List<RelatedResourceDto> actuals) {
        assertEquals(expecteds.size(), actuals.size());
        for (int i = 0; i < expecteds.size(); i++) {
            assertEqualsRelatedResourceDto(expecteds.get(i), actuals.get(i));
        }
    }

    public static void assertEqualsStructureBasicDto(StructureVersion expected, SrmLifeCycleMetadata expectedLifecycle, StructureMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getStructure().getId(), actual.getStructureId());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expected.getStructure().getResourceCreatedDate()), actual.getResourceCreatedDate());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expected.getStructure().getResourceLastUpdated()), actual.getResourceLastUpdated());

        assertEqualsMaintainableArtefactBasicDto(expected.getMaintainableArtefact(), expectedLifecycle, actual);
    }

    public static void assertEqualsItemSchemeBasicDto(ItemSchemeVersion expected, SrmLifeCycleMetadata expectedLifecycle, ItemSchemeMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getItemScheme().getIsTaskInBackground(), actual.getIsTaskInBackground());
        assertEquals(expected.getItemScheme().getId(), actual.getItemSchemeId());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expected.getItemScheme().getResourceCreatedDate()), actual.getResourceCreatedDate());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expected.getItemScheme().getResourceLastUpdated()), actual.getResourceLastUpdated());

        assertEqualsMaintainableArtefactBasicDto(expected.getMaintainableArtefact(), expectedLifecycle, actual);
    }

    public static void assertEqualsItemBasicDto(Item expected, ItemMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getItemSchemeVersion().getMaintainableArtefact().getUrn(), actual.getItemSchemeVersionUrn());

        assertEqualsNameableArtefactBasicDto(expected.getNameableArtefact(), actual);
    }

    public static void assertEqualsMaintainableArtefactBasicDto(MaintainableArtefact expected, SrmLifeCycleMetadata expectedLifecycle, MaintainableArtefactMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expected.getValidTo()), actual.getValidTo());
        assertEquals(expected.getVersionLogic(), actual.getVersionLogic());
        assertEquals(expectedLifecycle.getProcStatus(), actual.getProcStatus());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expectedLifecycle.getInternalPublicationDate()), actual.getInternalPublicationDate());
        assertEquals(expectedLifecycle.getInternalPublicationUser(), actual.getInternalPublicationUser());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expectedLifecycle.getExternalPublicationDate()), actual.getExternalPublicationDate());
        assertEquals(expectedLifecycle.getExternalPublicationUser(), actual.getExternalPublicationUser());

        assertEqualsNameableArtefactBasicDto(expected, actual);
    }

    public static void assertEqualsNameableArtefactBasicDto(NameableArtefact expected, NameableArtefactMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getUrn(), actual.getUrn());
        assertEquals(expected.getUrnProvider(), actual.getUrnProvider());

        assertEqualsIdentifiableArtefactBasicDto(expected, actual);
    }

    public static void assertEqualsIdentifiableArtefactBasicDto(IdentifiableArtefact expected, IdentifiableArtefactMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getUrn(), actual.getUrn());
        assertEquals(expected.getUrnProvider(), actual.getUrnProvider());
    }
}
