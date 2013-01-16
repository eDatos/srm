package org.siemac.metamac.srm.core.common.service.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class SrmServiceUtils {

    public static String[] procStatusEnumToString(ProcStatusEnum... procStatus) {
        String[] procStatusString = new String[procStatus.length];
        for (int i = 0; i < procStatus.length; i++) {
            procStatusString[i] = procStatus[i].name();
        }
        return procStatusString;
    }

    public static List<ProcStatusEnum> procStatusEnumToList(ProcStatusEnum[] procStatusArray) {
        List<ProcStatusEnum> procStatus = new ArrayList<ProcStatusEnum>();
        for (int i = 0; i < procStatusArray.length; i++) {
            procStatus.add(procStatusArray[i]);
        }
        return procStatus;
    }

    public static Boolean isItemSchemeFirstVersion(ItemSchemeVersion itemSchemeVersion) {
        return itemSchemeVersion.getMaintainableArtefact().getReplaceToVersion() == null;
    }

    public static RelatedResourceDto getRelatedResource(String urn, List<RelatedResourceDto> relatedResources) {
        for (RelatedResourceDto relatedResourceDto : relatedResources) {
            if (StringUtils.equals(urn, relatedResourceDto.getUrn())) {
                return relatedResourceDto;
            }
        }
        return null;
    }

    /**
     * Returns TRUE if the variable with the urn variableUrn is in the variable list
     */
    public static boolean isVariableInList(String variableUrn, List<Variable> variables) {
        for (Variable variable : variables) {
            if (StringUtils.equals(variableUrn, variable.getNameableArtefact().getUrn())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns TRUE if the family with the urn familyUrn is in the family list
     */
    public static boolean isFamilyInList(String familyUrn, List<VariableFamily> families) {
        for (VariableFamily family : families) {
            if (StringUtils.equals(familyUrn, family.getNameableArtefact().getUrn())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns TRUE if the codelist with the URN codelistUrn is in the codelist list
     */
    public static boolean isCodelistInList(String codelistUrn, List<CodelistVersionMetamac> codelists) {
        for (CodelistVersionMetamac codelist : codelists) {
            if (StringUtils.equals(codelistUrn, codelist.getMaintainableArtefact().getUrn())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns TRUE if the variable element with the URN variableElementUrn is in the variable elements list
     */
    public static boolean isVariableElementInList(String variableElementUrn, List<VariableElement> variableElements) {
        for (VariableElement variableElement : variableElements) {
            if (StringUtils.equals(variableElementUrn, variableElement.getNameableArtefact().getUrn())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter list of orders to get only the codes requested
     */
    @SuppressWarnings("rawtypes")
    public static List<CodeOrderVisualisation> filterCodeOrderVisualisationsByCodes(CodelistOrderVisualisation codelistOrderVisualisation, List codes) {
        List<CodeOrderVisualisation> codeOrderVisualisationsFiltered = new ArrayList<CodeOrderVisualisation>();
        for (int i = 0; i < codes.size(); i++) {
            Item code = (Item) codes.get(i);
            CodeOrderVisualisation codeOrderVisualisation = filterCodeOrderVisualisationsByCode(codelistOrderVisualisation.getCodes(), code.getNameableArtefact().getUrn());
            if (codeOrderVisualisation != null) {
                codeOrderVisualisationsFiltered.add(codeOrderVisualisation);
            }
        }
        return codeOrderVisualisationsFiltered;
    }

    /**
     * Filter list of orders to get only the orders related to codes in same level of code
     */
    public static List<CodeOrderVisualisation> filterCodeOrderVisualisationsOfCodeInSameLevel(CodelistOrderVisualisation codelistOrderVisualisation, CodeMetamac code) {

        // Items in same level
        List<Item> codesInSameLevel = null;
        if (code.getParent() != null) {
            codesInSameLevel = code.getParent().getChildren();
        } else {
            codesInSameLevel = code.getItemSchemeVersionFirstLevel().getItemsFirstLevel();
        }

        // Filter visualisations
        return filterCodeOrderVisualisationsByCodes(codelistOrderVisualisation, codesInSameLevel);
    }

    /**
     * Get from list the order visualisation of code related to code
     */
    public static CodeOrderVisualisation filterCodeOrderVisualisationsByCode(List<CodeOrderVisualisation> codeOrderVisualisations, String codeUrn) {
        for (CodeOrderVisualisation codeOrderVisualisation : codeOrderVisualisations) {
            if (codeOrderVisualisation.getCode().getNameableArtefact().getUrn().equals(codeUrn)) {
                return codeOrderVisualisation;
            }
        }
        return null;
    }

}