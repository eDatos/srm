package org.siemac.metamac.srm.core.base.utils;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.common.SrmConstantsTest;

public class BaseDtoMocks extends com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseDtoMocks {

    /**
     * Mock an ExternalItemDto type Operation
     */
    public static ExternalItemDto mockExternalItemStatisticalOperationDto(String code) {
        ExternalItemDto target = new ExternalItemDto();
        target.setType(TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
        target.setTitle(null); // statistical operations has not title in dto
        target.setUrn("urn:siemac:org.siemac.metamac.infomodel.statisticaloperations.Operation=" + code);
        target.setCode(code);
        target.setUri(SrmConstantsTest.ENDPOINT_INTERNAL_API_STATISTICAL_OPERATIONS + "/operations/" + target.getCode());
        target.setManagementAppUrl(SrmConstantsTest.INTERNAL_WEB_APPLICATION_STATISTICAL_OPERATIONS + "/#operations;id=" + target.getCode());
        return target;
    }
}
