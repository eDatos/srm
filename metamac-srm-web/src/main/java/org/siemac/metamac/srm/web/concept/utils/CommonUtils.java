package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

public class CommonUtils {

    public static String getConceptSchemeProcStatus(ConceptSchemeDto conceptSchemeDto) {
        return getCoreMessages().getString(getCoreMessages().conceptSchemeProcStatusEnum() + conceptSchemeDto.getProcStatus().getName());
    }

}
