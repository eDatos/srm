package org.siemac.metamac.srm.core.base.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.IdentifiableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.ItemMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.ItemSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.LifeCycleBasicDto;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.base.dto.MaintainableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.NameableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.StructureMetamacBasicDto;
import org.siemac.metamac.srm.core.common.SrmConstantsTest;

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
        assertEqualsNotNull(expected.getStructure().getId(), actual.getStructureId());
        assertEqualsNotNull(CoreCommonUtil.transformDateTimeToDate(expected.getStructure().getResourceCreatedDate()), actual.getResourceCreatedDate());
        assertEqualsNotNull(CoreCommonUtil.transformDateTimeToDate(expected.getStructure().getResourceLastUpdated()), actual.getResourceLastUpdated());
        assertEqualsNotNull(CoreCommonUtil.transformDateTimeToDate(expected.getCreatedDate()), actual.getCreatedDate());

        assertEqualsLifeCycleBasicDto(expectedLifecycle, actual.getLifeCycle());
        assertEqualsMaintainableArtefactBasicDto(expected.getMaintainableArtefact(), expectedLifecycle, actual);
    }

    public static void assertEqualsItemSchemeBasicDto(ItemSchemeVersion expected, SrmLifeCycleMetadata expectedLifecycle, ItemSchemeMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEqualsNotNull(expected.getItemScheme().getIsTaskInBackground(), actual.getIsTaskInBackground());
        assertEqualsNotNull(expected.getItemScheme().getId(), actual.getItemSchemeId());
        assertEqualsNotNull(CoreCommonUtil.transformDateTimeToDate(expected.getItemScheme().getResourceCreatedDate()), actual.getResourceCreatedDate());
        assertEqualsNotNull(CoreCommonUtil.transformDateTimeToDate(expected.getItemScheme().getResourceLastUpdated()), actual.getResourceLastUpdated());
        assertEqualsNotNull(CoreCommonUtil.transformDateTimeToDate(expected.getCreatedDate()), actual.getCreatedDate());

        assertEqualsLifeCycleBasicDto(expectedLifecycle, actual.getLifeCycle());
        assertEqualsMaintainableArtefactBasicDto(expected.getMaintainableArtefact(), expectedLifecycle, actual);
    }

    public static void assertEqualsItemBasicDto(Item expected, ItemMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEqualsNotNull(expected.getItemSchemeVersion().getMaintainableArtefact().getUrn(), actual.getItemSchemeVersionUrn());
        assertEqualsNotNull(CoreCommonUtil.transformDateTimeToDate(expected.getCreatedDate()), actual.getCreatedDate());

        assertEqualsNameableArtefactBasicDto(expected.getNameableArtefact(), actual);
    }

    public static void assertEqualsMaintainableArtefactBasicDto(MaintainableArtefact expected, SrmLifeCycleMetadata expectedLifecycle, MaintainableArtefactMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        BaseAsserts.assertEqualsRelatedResource(expected.getMaintainer().getNameableArtefact(), actual.getMaintainer());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expected.getValidTo()), actual.getValidTo());
        assertEqualsNotNull(expected.getVersionLogic(), actual.getVersionLogic());

        assertEqualsNameableArtefactBasicDto(expected, actual);
    }

    public static void assertEqualsNameableArtefactBasicDto(NameableArtefact expected, NameableArtefactMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        assertEqualsIdentifiableArtefactBasicDto(expected, actual);
    }

    public static void assertEqualsIdentifiableArtefactBasicDto(IdentifiableArtefact expected, IdentifiableArtefactMetamacBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEqualsNotNull(expected.getCode(), actual.getCode());
        assertEqualsNotNull(expected.getUrn(), actual.getUrn());
        assertEqualsNotNull(expected.getUrnProvider(), actual.getUrnProvider());
    }

    public static void assertEqualsLifeCycleBasicDto(SrmLifeCycleMetadata expected, LifeCycleBasicDto actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getProcStatus(), actual.getProcStatus());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expected.getInternalPublicationDate()), actual.getInternalPublicationDate());
        assertEquals(expected.getInternalPublicationUser(), actual.getInternalPublicationUser());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(expected.getExternalPublicationDate()), actual.getExternalPublicationDate());
        assertEquals(expected.getExternalPublicationUser(), actual.getExternalPublicationUser());
    }

    public static void assertEqualsNotNull(Object expected, Object actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    public static void assertEqualsExternalItemStatisticalOperations(ExternalItem entity, ExternalItemDto dto, MapperEnum mapperEnum) {
        assertEqualsExternalItem(entity, dto, SrmConstantsTest.ENDPOINT_INTERNAL_API_STATISTICAL_OPERATIONS, SrmConstantsTest.INTERNAL_WEB_APPLICATION_STATISTICAL_OPERATIONS, mapperEnum);
    }

    private static void assertEqualsExternalItem(ExternalItem entity, ExternalItemDto dto, String baseApi, String baseWebApplication, MapperEnum mapperEnum) {
        assertEqualsExternalItem(entity, dto, mapperEnum);

        assertEqualsNullability(entity.getUri(), dto.getUri());
        if (entity.getUri() != null) {
            if (MapperEnum.DO2DTO.equals(mapperEnum)) {
                assertEquals(baseApi + entity.getUri(), dto.getUri());
            } else if (MapperEnum.DTO2DO.equals(mapperEnum)) {
                assertEquals(dto.getUri().replaceFirst(baseApi, StringUtils.EMPTY), entity.getUri());
            } else {
                fail("Mapper unexpected: " + mapperEnum);
            }
        }

        assertEqualsNullability(entity.getManagementAppUrl(), dto.getManagementAppUrl());
        if (entity.getManagementAppUrl() != null) {
            if (MapperEnum.DO2DTO.equals(mapperEnum)) {
                assertEquals(baseWebApplication + entity.getManagementAppUrl(), dto.getManagementAppUrl());
            } else if (MapperEnum.DTO2DO.equals(mapperEnum)) {
                assertEquals(dto.getManagementAppUrl().replaceFirst(baseWebApplication, StringUtils.EMPTY), entity.getManagementAppUrl());
            } else {
                fail("Mapper unexpected: " + mapperEnum);
            }
        }
    }

}
