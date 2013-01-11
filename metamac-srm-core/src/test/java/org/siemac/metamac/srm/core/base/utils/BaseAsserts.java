package org.siemac.metamac.srm.core.base.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;

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
}
