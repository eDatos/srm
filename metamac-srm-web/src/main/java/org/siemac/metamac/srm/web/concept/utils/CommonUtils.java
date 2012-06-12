package org.siemac.metamac.srm.web.concept.utils;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.*;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

public class CommonUtils {

    public static String getConceptSchemeProcStatus(ConceptSchemeDto conceptSchemeDto) {
        String procStatus = getCoreMessages().getString(getCoreMessages().conceptSchemeProcStatusEnum() + conceptSchemeDto.getProcStatus().getName());
        /*if (!StringUtils.isBlank(conceptSchemeDto.getPublishedVersion())) {
            procStatus += getMessages().conceptSchemePreviousVersion(conceptSchemeDto.getPublishedVersion());
        }*/
        return procStatus;
    }

}
