package org.siemac.metamac.srm.core.task.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseInvocationValidator;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;

public class TasksMetamacInvocationValidator extends BaseInvocationValidator {

    public static void checkImportConceptsTsvInBackground(String conceptSchemeUrn, File file, String fileName, boolean updateAlreadyExisting, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(conceptSchemeUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(file, ServiceExceptionParameters.STREAM, exceptions);
        ValidationUtils.checkParameterRequired(fileName, ServiceExceptionParameters.FILE_NAME, exceptions);
        ValidationUtils.checkParameterRequired(updateAlreadyExisting, ServiceExceptionParameters.IMPORTATION_TSV_UPDATE_ALREADY_EXISTING, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkImportCodesTsvInBackground(String codelistUrn, File file, String fileName, boolean updateAlreadyExisting, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codelistUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(file, ServiceExceptionParameters.STREAM, exceptions);
        ValidationUtils.checkParameterRequired(fileName, ServiceExceptionParameters.FILE_NAME, exceptions);
        ValidationUtils.checkParameterRequired(updateAlreadyExisting, ServiceExceptionParameters.IMPORTATION_TSV_UPDATE_ALREADY_EXISTING, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkImportCodeOrdersTsvInBackground(String codelistUrn, File file, String fileName, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codelistUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(file, ServiceExceptionParameters.STREAM, exceptions);
        ValidationUtils.checkParameterRequired(fileName, ServiceExceptionParameters.FILE_NAME, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkImportVariableElementsTsvInBackground(String variableUrn, File file, String fileName, boolean updateAlreadyExisting, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(variableUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(file, ServiceExceptionParameters.STREAM, exceptions);
        ValidationUtils.checkParameterRequired(fileName, ServiceExceptionParameters.FILE_NAME, exceptions);
        ValidationUtils.checkParameterRequired(updateAlreadyExisting, ServiceExceptionParameters.IMPORTATION_TSV_UPDATE_ALREADY_EXISTING, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
}