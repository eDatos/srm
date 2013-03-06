package org.siemac.metamac.srm.core.common.service.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

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
        List<ProcStatusEnum> procStatus = new ArrayList<ProcStatusEnum>(procStatusArray.length);
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

    public static Boolean isAlphabeticalOrderVisualisation(CodelistOrderVisualisation orderVisualisation) {
        return SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(orderVisualisation.getNameableArtefact().getCode());
    }

    public static void setCodeOrder(CodeMetamac code, int columnIndex, Integer codeIndex) {
        try {
            MethodUtils.invokeExactMethod(code, "setOrder" + columnIndex, codeIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getCodeOrder(CodeMetamac code, int columnIndex) {
        try {
            return (Integer) MethodUtils.invokeExactMethod(code, "getOrder" + columnIndex, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}