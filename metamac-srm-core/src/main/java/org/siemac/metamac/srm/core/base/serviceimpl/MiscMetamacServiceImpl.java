package org.siemac.metamac.srm.core.base.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.MiscValue;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.MiscMetamacInvocationValidator;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.springframework.stereotype.Service;

/**
 * Implementation of MiscMetamacService.
 */
@Service("miscMetamacService")
public class MiscMetamacServiceImpl extends MiscMetamacServiceImplBase {

    public MiscMetamacServiceImpl() {
    }

    @Override
    public MiscValue createOrUpdateMiscValue(ServiceContext ctx, String name, Object value) throws MetamacException {

        // Validation
        MiscMetamacInvocationValidator.checkCreateOrUpdateMiscValue(name, value, null);

        // Retrieve possible existing
        MiscValue miscValue = getMiscValueRepository().findByName(name);
        if (miscValue == null) {
            miscValue = new MiscValue();
            miscValue.setName(name);
        } else {
            miscValue.clearAllValues();
        }
        if (value instanceof String) {
            miscValue.setStringValue((String) value);
        } else if (value instanceof DateTime) {
            miscValue.setDateValue((DateTime) value);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Value type unsupported: " + value.getClass().getCanonicalName());
        }

        // Save
        return getMiscValueRepository().save(miscValue);
    }

    @Override
    public MiscValue findOneMiscValueByName(ServiceContext ctx, String name) throws MetamacException {
        // Validation
        MiscMetamacInvocationValidator.checkFindOneMiscValueByName(name, null);

        // Retrieve
        MiscValue miscValue = getMiscValueRepository().findByName(name);
        return miscValue; // note: can not exist
    }
}
