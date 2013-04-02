package org.siemac.metamac.srm.core.importation.serviceimpl.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseInvocationValidator;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;

public class ImportationMetamacInvocationValidator extends BaseInvocationValidator {

    public static void checkImportVariableElementsCsvInBackground(String variableUrn, InputStream csvStream, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(variableUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(csvStream, ServiceExceptionParameters.STREAM, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

}