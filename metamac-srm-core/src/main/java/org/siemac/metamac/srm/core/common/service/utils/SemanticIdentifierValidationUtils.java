package org.siemac.metamac.srm.core.common.service.utils;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;

import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParameters;

public class SemanticIdentifierValidationUtils extends com.arte.statistic.sdmx.srm.core.common.service.utils.SemanticIdentifierValidationUtils {

    public static void checkCodelistFamilySemanticIdentifier(CodelistFamily codelistFamily, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkSemanticIdentifierAsMetamacID(codelistFamily.getNameableArtefact().getCode(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, exceptions);
    }

    public static void checkVariableFamilySemanticIdentifier(VariableFamily variableFamily, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkSemanticIdentifierAsMetamacID(variableFamily.getNameableArtefact().getCode(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, exceptions);
    }

    public static void checkVariableSemanticIdentifier(Variable variable, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkSemanticIdentifierAsMetamacID(variable.getNameableArtefact().getCode(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, exceptions);
    }

    public static void checkVariableElementSemanticIdentifier(VariableElement variableElement, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkSemanticIdentifierAsMetamacID(variableElement.getIdentifiableArtefact().getCode(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, exceptions);
    }

    public static boolean isVariableElementSemanticIdentifier(String code) {
        return CoreCommonUtil.matchMetamacID(code);
    }

    public static void checkCodelistOrderVisualisationSemanticIdentifier(CodelistOrderVisualisation codelistOrderVisualisation, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkSemanticIdentifierAsMetamacID(codelistOrderVisualisation.getNameableArtefact().getCode(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, exceptions);
    }

    public static void checkCodelistOpennessVisualisationSemanticIdentifier(CodelistOpennessVisualisation codelistOpennessVisualisation, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkSemanticIdentifierAsMetamacID(codelistOpennessVisualisation.getNameableArtefact().getCode(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, exceptions);
    }

}