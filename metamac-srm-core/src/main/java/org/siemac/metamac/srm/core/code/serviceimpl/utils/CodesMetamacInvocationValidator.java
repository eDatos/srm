package org.siemac.metamac.srm.core.code.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SemanticIdentifierValidationUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.code.serviceimpl.utils.CodesInvocationValidator;

public class CodesMetamacInvocationValidator extends CodesInvocationValidator {

    // ---------------------------------------------------------------------------
    // CODELISTS
    // ---------------------------------------------------------------------------

    public static void checkCreateCodelist(CodelistVersionMetamac codelistVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(codelistVersion, ServiceExceptionParameters.CODELIST, exceptions);
        if (codelistVersion != null) {
            checkCodelist(codelistVersion, true, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCodelist(CodelistVersionMetamac codelistVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(codelistVersion, ServiceExceptionParameters.CODELIST, exceptions);
        if (codelistVersion != null) {
            checkCodelist(codelistVersion, false, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCodelist(CodelistVersionMetamac codelistVersion, boolean creating, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataOptionalIsValid(codelistVersion.getShortName(), ServiceExceptionParameters.CODELIST_SHORT_NAME, exceptions);
        if (codelistVersion.getMaintainableArtefact() != null && BooleanUtils.isTrue(codelistVersion.getMaintainableArtefact().getIsExternalReference())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE));
        }
        if (SrmValidationUtils.mustValidateMetadataRequired(codelistVersion, creating)) {
            ValidationUtils.checkMetadataRequired(codelistVersion.getVariable(), ServiceExceptionParameters.CODELIST_VARIABLE, exceptions);
        }
        if (codelistVersion.getId() != null) {
            ValidationUtils.checkMetadataRequired(codelistVersion.getIsVariableUpdated(), ServiceExceptionParameters.CODELIST_IS_VARIABLE_UPDATED, exceptions);
        }
    }

    // ---------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------

    public static void checkCreateCode(CodelistVersionMetamac codelistVersion, CodeMetamac code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codelistVersion, ServiceExceptionParameters.CODELIST, exceptions);
        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);
        if (code != null && codelistVersion != null) {
            checkCode(codelistVersion, code, true, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCode(CodelistVersionMetamac codelistVersionMetamac, CodeMetamac code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);
        if (code != null) {
            checkCode(codelistVersionMetamac, code, false, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCodeParent(String codeUrn, String newParentUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codeUrn, ServiceExceptionParameters.URN, exceptions);
        // newParentUrn is optional. When it is null, move to first level

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCodeInOrderVisualisation(String codeUrn, String codelistOrderVisualisationUrn, Long newCodeIndex, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codeUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(codelistOrderVisualisationUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(newCodeIndex, ServiceExceptionParameters.CODE_ORDER_VISUALISATION_INDEX, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkCode(CodelistVersionMetamac codelistVersion, CodeMetamac code, boolean creating, List<MetamacExceptionItem> exceptions) {
        if (SrmValidationUtils.mustValidateMetadataRequired(codelistVersion, creating)) {
            ValidationUtils.checkMetadataRequired(codelistVersion.getVariable(), ServiceExceptionParameters.CODELIST_VARIABLE, exceptions);
        }
        if (code.getVariableElement() != null) {
            ValidationUtils.checkMetadataEmpty(code.getShortName(), ServiceExceptionParameters.CODE_SHORT_NAME, exceptions);
        } else {
            ValidationUtils.checkMetadataOptionalIsValid(code.getShortName(), ServiceExceptionParameters.CODE_SHORT_NAME, exceptions);
        }

        // common metadata in sdmx are checked in Sdmx module
    }

    // ---------------------------------------------------------------------------
    // CODELIST VISUALISATIONS
    // ---------------------------------------------------------------------------

    public static void checkCreateCodelistOrderVisualisation(String codelistUrn, CodelistOrderVisualisation codelistOrderVisualisation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(codelistUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(codelistOrderVisualisation, ServiceExceptionParameters.CODELIST_ORDER_VISUALISATION, exceptions);
        if (codelistOrderVisualisation != null) {
            checkCodelistOrderVisualisation(codelistOrderVisualisation, exceptions);
            ValidationUtils.checkMetadataEmpty(codelistOrderVisualisation.getCodes(), ServiceExceptionParameters.CODELIST_ORDER_VISUALISATION_CODES, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCodelistOrderVisualisation(CodelistOrderVisualisation codelistOrderVisualisation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        checkCodelistOrderVisualisation(codelistOrderVisualisation, exceptions);
        ValidationUtils.checkParameterRequired(codelistOrderVisualisation.getCodelistVersion(), ServiceExceptionParameters.CODELIST, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkCodelistOrderVisualisation(CodelistOrderVisualisation codelistOrderVisualisation, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(codelistOrderVisualisation, ServiceExceptionParameters.CODELIST_ORDER_VISUALISATION, exceptions);
        if (codelistOrderVisualisation == null) {
            return;
        }
        checkNameableArtefact(codelistOrderVisualisation.getNameableArtefact(), exceptions);
        if (codelistOrderVisualisation.getNameableArtefact() != null) {
            SemanticIdentifierValidationUtils.checkCodelistOrderVisualisationSemanticIdentifier(codelistOrderVisualisation, exceptions);
        }
    }

    // ---------------------------------------------------------------------------
    // CODELIST FAMILIES
    // ---------------------------------------------------------------------------

    public static void checkCreateCodelistFamily(CodelistFamily codelistFamily, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkCodelistFamily(codelistFamily, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCodelistFamily(CodelistFamily codelistFamily, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        if (codelistFamily != null) {
            ValidationUtils.checkMetadataRequired(codelistFamily.getNameableArtefact().getUrn(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_URN, exceptions);
        }
        checkCodelistFamily(codelistFamily, exceptions);
        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkAddCodelistsToCodelistFamily(List<String> codelistUrns, String codelistFamilyUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codelistUrns, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(codelistFamilyUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRemoveCodelistFromCodelistFamily(String codelistUrn, String codelistFamilyUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codelistUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(codelistFamilyUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkCodelistFamily(CodelistFamily codelistFamily, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(codelistFamily, ServiceExceptionParameters.CODELIST_FAMILY, exceptions);
        if (codelistFamily == null) {
            return;
        }
        checkNameableArtefact(codelistFamily.getNameableArtefact(), exceptions);
        if (codelistFamily.getNameableArtefact() != null) {
            SemanticIdentifierValidationUtils.checkCodelistFamilySemanticIdentifier(codelistFamily, exceptions);
        }
    }

    // ---------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ---------------------------------------------------------------------------

    public static void checkCreateVariableFamily(VariableFamily variableFamily, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkVariableFamily(variableFamily, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateVariableFamily(VariableFamily variableFamily, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        if (variableFamily != null) {
            ValidationUtils.checkMetadataRequired(variableFamily.getNameableArtefact().getUrn(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_URN, exceptions);
        }
        checkVariableFamily(variableFamily, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkVariableFamily(VariableFamily variableFamily, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(variableFamily, ServiceExceptionParameters.VARIABLE_FAMILY, exceptions);
        if (variableFamily == null) {
            return;
        }
        checkNameableArtefact(variableFamily.getNameableArtefact(), exceptions);
        if (variableFamily.getNameableArtefact() != null) {
            SemanticIdentifierValidationUtils.checkVariableFamilySemanticIdentifier(variableFamily, exceptions);
        }
    }

    // ---------------------------------------------------------------------------
    // VARIABLES
    // ---------------------------------------------------------------------------

    public static void checkCreateVariable(Variable variable, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkVariable(variable, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateVariable(Variable variable, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        if (variable != null) {
            ValidationUtils.checkMetadataRequired(variable.getNameableArtefact().getUrn(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_URN, exceptions);
        }
        checkVariable(variable, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkAddVariablesToFamily(List<String> variablesUrn, String familyUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(variablesUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(familyUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRemoveVariableFromFamily(String variableUrn, String familyUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(variableUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(familyUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkVariable(Variable variable, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(variable, ServiceExceptionParameters.VARIABLE, exceptions);
        if (variable == null) {
            return;
        }

        // Check required metadata
        ValidationUtils.checkMetadataRequired(variable.getFamilies(), ServiceExceptionParameters.VARIABLE_FAMILY, exceptions);
        checkNameableArtefact(variable.getNameableArtefact(), exceptions);
        if (variable.getNameableArtefact() != null) {
            SemanticIdentifierValidationUtils.checkVariableSemanticIdentifier(variable, exceptions);
        }

        ValidationUtils.checkMetadataRequired(variable.getShortName(), ServiceExceptionParameters.VARIABLE_SHORT_NAME, exceptions);

        // Check dates: validFrom value must be lower than validTo value
        ValidationUtils.checkDateTimeBeforeDateTime(variable.getValidFrom(), variable.getValidTo(), ServiceExceptionParameters.VARIABLE_VALID_TO, exceptions);
    }

    // ---------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ---------------------------------------------------------------------------

    public static void checkCreateVariableElement(VariableElement variableElement, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkVariableElement(variableElement, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateVariableElement(VariableElement variableElement, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        if (variableElement != null) {
            ValidationUtils.checkMetadataRequired(variableElement.getNameableArtefact().getUrn(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_URN, exceptions);
        }
        checkVariableElement(variableElement, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkAddVariableElementsToVariable(List<String> variableElementsUrn, String variableUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(variableElementsUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(variableUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRemoveVariableElementFromVariable(String variableElementUrn, String variableUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(variableElementUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(variableUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkAddVariableElementOperation(List<String> sources, List<String> targets, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(sources, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(targets, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveVariableElementOperationByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteVariableElementOperation(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCodeVariableElement(String codeUrn, String variableElementUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codeUrn, ServiceExceptionParameters.URN, exceptions);
        // variableElementUrn is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkVariableElement(VariableElement variableElement, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(variableElement, ServiceExceptionParameters.VARIABLE_ELEMENT, exceptions);
        if (variableElement == null) {
            return;
        }

        // Check required metadata
        ValidationUtils.checkMetadataRequired(variableElement.getVariable(), ServiceExceptionParameters.VARIABLE_ELEMENT_VARIABLE, exceptions);
        checkNameableArtefact(variableElement.getNameableArtefact(), exceptions);
        if (variableElement.getNameableArtefact() != null) {
            SemanticIdentifierValidationUtils.checkVariableElementSemanticIdentifier(variableElement, exceptions);
        }
        ValidationUtils.checkMetadataRequired(variableElement.getShortName(), ServiceExceptionParameters.VARIABLE_ELEMENT_SHORT_NAME, exceptions);

        // Check dates: validFrom value must be lower than validTo value
        ValidationUtils.checkDateTimeBeforeDateTime(variableElement.getValidFrom(), variableElement.getValidTo(), ServiceExceptionParameters.VARIABLE_ELEMENT_VALID_TO, exceptions);
    }
}