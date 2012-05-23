package org.siemac.metamac.internal.web.client.utils;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

public class CommonUtils {

    public static String getUuidString(String uuid) {
        return StringUtils.isBlank(uuid) ? null : uuid;
    }

    // TODO: proc status
    public static String getConceptSchemeProcStatus(ConceptSchemeDto conceptSchemeDto) {
        /*
         * String procStatus = getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + conceptSchemeDto.getProcStatus().getName());
         * if (!StringUtils.isBlank(indicatorDto.getPublishedVersion())) {
         * procStatus += IndicatorsWeb.getMessages().indicatorPreviousVersion(indicatorDto.getPublishedVersion());
         * }
         * return procStatus;
         */
        return "DRAFT";
    }

}
