package org.siemac.metamac.srm.core.code.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;

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
            checkCodelist(codelistVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCodelist(CodelistVersionMetamac codelistVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(codelistVersion, ServiceExceptionParameters.CODELIST, exceptions);
        if (codelistVersion != null) {
            checkCodelist(codelistVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkCodelist(CodelistVersionMetamac codelistVersion, List<MetamacExceptionItem> exceptions) {
        if (codelistVersion.getMaintainableArtefact() != null && BooleanUtils.isTrue(codelistVersion.getMaintainableArtefact().getIsExternalReference())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE));
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
            checkCode(codelistVersion, code, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCode(CodelistVersionMetamac codelistVersionMetamac, CodeMetamac code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);
        if (code != null) {
            checkCode(codelistVersionMetamac, code, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkCode(CodelistVersionMetamac codelistVersion, CodeMetamac code, List<MetamacExceptionItem> exceptions) {
        // common metadata in sdmx are checked in Sdmx module
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

        checkVariableFamily(variableFamily, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkVariableFamily(VariableFamily variableFamily, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(variableFamily, ServiceExceptionParameters.VARIABLE_FAMILY, exceptions);
        if (variableFamily == null) {
            return;
        }
        checkNameableArtefact(variableFamily.getNameableArtefact(), exceptions);
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
        ValidationUtils.checkMetadataRequired(variable.getShortName(), ServiceExceptionParameters.VARIABLE_SHORT_NAME, exceptions);

        // Check dates: validFrom value must be lower than validTo value
        ValidationUtils.checkDateTimeBeforeDateTime(variable.getValidFrom(), variable.getValidTo(), ServiceExceptionParameters.VARIABLE_VALID_TO, exceptions);
    }
}