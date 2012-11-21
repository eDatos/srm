package org.siemac.metamac.srm.core.code.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
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

    public static void checkRetrieveCodelistByCodeUrn(String codeUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codeUrn, ServiceExceptionParameters.URN, exceptions);

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

    public static void checkRetrieveCodelistFamilyByIdentifier(String identifier, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(identifier, ServiceExceptionParameters.IDENTIFIER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

}