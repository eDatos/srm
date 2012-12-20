package org.siemac.metamac.srm.web.concept.utils;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;

public class CommonUtils {

    private static LinkedHashMap<String, String> conceptSchemeTypeHashMap     = null;
    private static LinkedHashMap<String, String> conceptRoleHashMap           = null;
    private static LinkedHashMap<String, String> conceptFacetValueTypeHashMap = null;

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

    public static LinkedHashMap<String, String> getConceptFacetValueTypeHashMap() {
        if (conceptFacetValueTypeHashMap == null) {
            conceptFacetValueTypeHashMap = new LinkedHashMap<String, String>();
            conceptFacetValueTypeHashMap.put(new String(), new String());
            for (FacetValueTypeEnum f : FacetFormUtils.getConceptFacetValueTypeEnums()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().facetValueTypeEnum() + f.getName());
                conceptFacetValueTypeHashMap.put(f.toString(), value);
            }
        }
        return conceptFacetValueTypeHashMap;
    }

    public static String getRelatedOperationCode(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        return conceptSchemeMetamacDto.getRelatedOperation() != null ? conceptSchemeMetamacDto.getRelatedOperation().getCode() : null;
    }

    /**
     * METADATA VISIBILITY
     */

    public static boolean isMetadataSdmxRelatedArtefactVisible(ConceptSchemeTypeEnum type) {
        return ConceptSchemeTypeEnum.OPERATION.equals(type) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(type) || ConceptSchemeTypeEnum.MEASURE.equals(type);
    }

    public static boolean isMetadataRolesVisible(ConceptSchemeTypeEnum type) {
        return ConceptSchemeTypeEnum.OPERATION.equals(type) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(type) || ConceptSchemeTypeEnum.MEASURE.equals(type);
    }

    public static boolean isMetadataExtendsVisible(ConceptSchemeTypeEnum type) {
        return !ConceptSchemeTypeEnum.ROLE.equals(type);
    }
}
