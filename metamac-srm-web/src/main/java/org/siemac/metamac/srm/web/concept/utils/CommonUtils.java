package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;

import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.arte.statistic.sdmx.v2_1.domain.enume.concept.domain.ConceptSchemeTypeEnum;

public class CommonUtils {

    public static String getConceptSchemeProcStatus(MetamacConceptSchemeDto conceptSchemeDto) {
        return getCoreMessages().getString(getCoreMessages().maintainableArtefactProcStatusEnum() + conceptSchemeDto.getProcStatus().getName());
    }

    public static LinkedHashMap<String, String> getConceptSchemeTypeHashMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (ConceptSchemeTypeEnum c : ConceptSchemeTypeEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptSchemeTypeEnum() + c.getName());
            valueMap.put(c.toString(), value);
        }
        return valueMap;
    }

}
