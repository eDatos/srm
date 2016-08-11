package org.siemac.metamac.srm.core.base.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;

public class MiscMetamacInvocationValidator {

    // ---------------------------------------------------------------------------
    // MISC VALUES
    // ---------------------------------------------------------------------------

    public static void checkCreateOrUpdateMiscValue(String name, Object value, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(name, ServiceExceptionParameters.MISC_VALUE_NAME, exceptions);
        if (ValidationUtils.isEmpty(value)) {
            exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.MISC_VALUE_VALUE));
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindOneMiscValueByName(String name, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(name, ServiceExceptionParameters.MISC_VALUE_NAME, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

}