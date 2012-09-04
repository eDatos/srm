package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;

public class CommonUtils {

    private static LinkedHashMap<String, String> conceptSchemeTypeHashMap = null;
    private static LinkedHashMap<String, String> conceptRoleHashMap       = null;

    public static String getConceptSchemeProcStatus(ConceptSchemeMetamacDto conceptSchemeDto) {
        return getCoreMessages().getString(getCoreMessages().itemSchemeMetamacProcStatusEnum() + conceptSchemeDto.getProcStatus().getName());
    }

    public static LinkedHashMap<String, String> getConceptTypeHashMap(List<ConceptTypeDto> conceptTypeDtos) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put(new String(), new String());
        for (ConceptTypeDto conceptTypeDto : conceptTypeDtos) {
            hashMap.put(conceptTypeDto.getIdentifier(), CommonWebUtils.getElementName(conceptTypeDto.getIdentifier(), conceptTypeDto.getDescription()));
        }
        return hashMap;
    }

    public static LinkedHashMap<String, String> getConceptSchemeTypeHashMap() {
        if (conceptSchemeTypeHashMap == null) {
            conceptSchemeTypeHashMap = new LinkedHashMap<String, String>();
            conceptSchemeTypeHashMap.put(new String(), new String());
            for (ConceptSchemeTypeEnum c : ConceptSchemeTypeEnum.values()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptSchemeTypeEnum() + c.getName());
                conceptSchemeTypeHashMap.put(c.toString(), value);
            }
        }
        return conceptSchemeTypeHashMap;
    }

    public static String getConceptSchemeTypeName(ConceptSchemeTypeEnum conceptSchemeTypeEnum) {
        return conceptSchemeTypeEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptSchemeTypeEnum() + conceptSchemeTypeEnum.name()) : null;
    }

    public static LinkedHashMap<String, String> getConceptRoleHashMap() {
        if (conceptRoleHashMap == null) {
            conceptRoleHashMap = new LinkedHashMap<String, String>();
            conceptRoleHashMap.put(new String(), new String());
            for (ConceptRoleEnum c : ConceptRoleEnum.values()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptRoleEnum() + c.getName());
                conceptRoleHashMap.put(c.toString(), value);
            }
        }
        return conceptRoleHashMap;
    }

    public static String getConceptRoleName(ConceptRoleEnum conceptRoleEnum) {
        return conceptRoleEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptRoleEnum() + conceptRoleEnum.name()) : null;
    }

}
