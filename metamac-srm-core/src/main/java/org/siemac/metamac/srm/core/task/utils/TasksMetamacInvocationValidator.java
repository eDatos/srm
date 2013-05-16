package org.siemac.metamac.srm.core.task.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseInvocationValidator;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;

public class TasksMetamacInvocationValidator extends BaseInvocationValidator {

    public static void checkImportCodesTsvInBackground(String codelistUrn, InputStream tsvStream, boolean updateAlreadyExisting, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codelistUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(tsvStream, ServiceExceptionParameters.STREAM, exceptions);
        ValidationUtils.checkParameterRequired(updateAlreadyExisting, ServiceExceptionParameters.IMPORTATION_TSV_UPDATE_ALREADY_EXISTING, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkImportCodeOrdersTsvInBackground(String codelistUrn, InputStream tsvStream, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codelistUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(tsvStream, ServiceExceptionParameters.STREAM, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkImportVariableElementsTsvInBackground(String variableUrn, InputStream tsvStream, boolean updateAlreadyExisting, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(variableUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(tsvStream, ServiceExceptionParameters.STREAM, exceptions);
        ValidationUtils.checkParameterRequired(updateAlreadyExisting, ServiceExceptionParameters.IMPORTATION_TSV_UPDATE_ALREADY_EXISTING, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
}