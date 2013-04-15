package org.siemac.metamac.srm.core.common.service.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
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
            if (StringUtils.equals(variableElementUrn, variableElement.getIdentifiableArtefact().getUrn())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isAlphabeticalOrderVisualisation(CodelistOrderVisualisation orderVisualisation) {
        return SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(orderVisualisation.getNameableArtefact().getCode());
    }

    public static Boolean isAllExpandedOpennessVisualisation(CodelistOpennessVisualisation opennessVisualisation) {
        return SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE.equals(opennessVisualisation.getNameableArtefact().getCode());
    }

    /**
     * Get code order.
     * Note: can not use reflection because set to null is necessary
     */
    public static void setCodeOrder(CodeMetamac code, int columnIndex, Integer codeIndex) {
        switch (columnIndex) {
            case 1:
                code.setOrder1(codeIndex);
                break;
            case 2:
                code.setOrder2(codeIndex);
                break;
            case 3:
                code.setOrder3(codeIndex);
                break;
            case 4:
                code.setOrder4(codeIndex);
                break;
            case 5:
                code.setOrder5(codeIndex);
                break;
            case 6:
                code.setOrder6(codeIndex);
                break;
            case 7:
                code.setOrder7(codeIndex);
                break;
            case 8:
                code.setOrder8(codeIndex);
                break;
            case 9:
                code.setOrder9(codeIndex);
                break;
            case 10:
                code.setOrder10(codeIndex);
                break;
            case 11:
                code.setOrder11(codeIndex);
                break;
            case 12:
                code.setOrder12(codeIndex);
                break;
            case 13:
                code.setOrder13(codeIndex);
                break;
            case 14:
                code.setOrder14(codeIndex);
                break;
            case 15:
                code.setOrder15(codeIndex);
                break;
            case 16:
                code.setOrder16(codeIndex);
                break;
            case 17:
                code.setOrder17(codeIndex);
                break;
            case 18:
                code.setOrder18(codeIndex);
                break;
            case 19:
                code.setOrder19(codeIndex);
                break;
            case 20:
                code.setOrder20(codeIndex);
                break;
            default:
                throw new IllegalArgumentException("Order not supported: " + columnIndex);
        }
    }

    /**
     * Clear all order columns to put at the end of new level
     */
    public static void clearCodeOrders(CodeMetamac code) {
        for (int i = 0; i < SrmConstants.CODELIST_OPENNESS_VISUALISATION_MAXIMUM_NUMBER; i++) {
            setCodeOrder(code, i + 1, null);
        }
    }

    public static Integer getCodeOrder(CodeMetamac code, int columnIndex) {
        try {
            return (Integer) MethodUtils.invokeExactMethod(code, "getOrder" + columnIndex, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get code openness.
     * Note: can not use reflection because set to null is necessary
     */
    public static void setCodeOpenness(CodeMetamac code, int columnIndex, Boolean codeIndex) {
        switch (columnIndex) {
            case 1:
                code.setOpenness1(codeIndex);
                break;
            case 2:
                code.setOpenness2(codeIndex);
                break;
            case 3:
                code.setOpenness3(codeIndex);
                break;
            case 4:
                code.setOpenness4(codeIndex);
                break;
            case 5:
                code.setOpenness5(codeIndex);
                break;
            case 6:
                code.setOpenness6(codeIndex);
                break;
            case 7:
                code.setOpenness7(codeIndex);
                break;
            case 8:
                code.setOpenness8(codeIndex);
                break;
            case 9:
                code.setOpenness9(codeIndex);
                break;
            case 10:
                code.setOpenness10(codeIndex);
                break;
            case 11:
                code.setOpenness11(codeIndex);
                break;
            case 12:
                code.setOpenness12(codeIndex);
                break;
            case 13:
                code.setOpenness13(codeIndex);
                break;
            case 14:
                code.setOpenness14(codeIndex);
                break;
            case 15:
                code.setOpenness15(codeIndex);
                break;
            case 16:
                code.setOpenness16(codeIndex);
                break;
            case 17:
                code.setOpenness17(codeIndex);
                break;
            case 18:
                code.setOpenness18(codeIndex);
                break;
            case 19:
                code.setOpenness19(codeIndex);
                break;
            case 20:
                code.setOpenness20(codeIndex);
                break;
            default:
                throw new IllegalArgumentException("Openness not supported: " + columnIndex);
        }
    }

    public static Boolean getCodeOpenness(CodeMetamac code, int columnIndex) {
        try {
            return (Boolean) MethodUtils.invokeExactMethod(code, "getOpenness" + columnIndex, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static PagedResult pagedResultZeroResults(PagingParameter pagingParameter) {
        return new PagedResult(new ArrayList(), pagingParameter.getStartRow(), pagingParameter.getRowCount(), pagingParameter.getPageSize(), 0, 0);
    }
}