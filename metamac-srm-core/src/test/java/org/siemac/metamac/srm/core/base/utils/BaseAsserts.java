package org.siemac.metamac.srm.core.base.utils;

import static org.junit.Assert.assertEquals;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;

public class BaseAsserts extends com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts{

    // LIFE CYCLE

    public static void assertEqualsLifeCycleDto(LifeCycleDto expected, LifeCycleDto actual) {
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

}
