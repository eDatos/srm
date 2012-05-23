package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

public class CommonUtils {

    public static String getConceptSchemeProcStatus(ConceptSchemeDto conceptSchemeDto) {
        return "DRAFT";
        // TODO: Finish when dto has procstatus
        /*
         * String procStatus = getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + conceptSchemeDto.getProcStatus().getName());
         * if (!StringUtils.isBlank(conceptSchemeDto.getPublishedVersion())) {
         * procStatus += IndicatorsWeb.getMessages().indicatorPreviousVersion(conceptSchemeDto.getPublishedVersion());
         * }
         * return procStatus;
         */
    }

}
