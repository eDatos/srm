package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class CommonUtils {

    public static String getConceptSchemeProcStatus(ConceptSchemeMetamacDto conceptSchemeDto) {
        return getCoreMessages().getString(getCoreMessages().itemSchemeMetamacProcStatusEnum() + conceptSchemeDto.getProcStatus().getName());
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
