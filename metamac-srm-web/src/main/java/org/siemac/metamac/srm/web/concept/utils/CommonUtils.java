package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;

public class CommonUtils {

    public static String getConceptSchemeProcStatus(ConceptSchemeDto conceptSchemeDto) {
        return getCoreMessages().getString(getCoreMessages().conceptSchemeProcStatusEnum() + conceptSchemeDto.getProcStatus().getName());
    }

}
