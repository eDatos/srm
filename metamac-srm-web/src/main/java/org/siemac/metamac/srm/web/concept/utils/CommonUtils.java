package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;

public class CommonUtils {

    public static String getConceptSchemeProcStatus(MetamacConceptSchemeDto conceptSchemeDto) {
        return getCoreMessages().getString(getCoreMessages().conceptSchemeProcStatusEnum() + conceptSchemeDto.getProcStatus().getName());
    }

}
