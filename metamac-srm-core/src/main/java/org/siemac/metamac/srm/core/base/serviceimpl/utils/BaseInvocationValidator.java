package org.siemac.metamac.srm.core.base.serviceimpl.utils;

import java.util.List;

import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;

public class BaseInvocationValidator {

    // ------------------------------------------------------------------------------------
    // ITEM SHCEME
    // ------------------------------------------------------------------------------------

    public static void checkItemScheme(ItemScheme itemScheme, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(itemScheme, ServiceExceptionParameters.ITEM_SCHEME, exceptions);
        ValidationUtils.checkMetadataRequired(itemScheme.getIdLogic(), ServiceExceptionParameters.ITEM_SCHEME_ID_LOGIC, exceptions);
        ValidationUtils.checkMetadataRequired(itemScheme.getVersionLogic(), ServiceExceptionParameters.ITEM_SCHEME_VERSION_LOGIC, exceptions);
        ValidationUtils.checkMetadataRequired(itemScheme.getName(), ServiceExceptionParameters.ITEM_SCHEME_NAME, exceptions);
        ValidationUtils.checkMetadataRequired(itemScheme.getFinalLogic(), ServiceExceptionParameters.ITEM_SCHEM_FINAL_LOGIC, exceptions);
        checkMantainer(itemScheme.getMaintainer(), exceptions);
    }
    

    // ------------------------------------------------------------------------------------
    // MANTAINER
    // ------------------------------------------------------------------------------------

    private static void checkMantainer(ExternalItemBt maintainer, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(maintainer.getCodeId(), ServiceExceptionParameters.MANTAINER_CODE_ID, exceptions);
        ValidationUtils.checkMetadataRequired(maintainer.getType(), ServiceExceptionParameters.MANTAINER_TYPE, exceptions);
        ValidationUtils.checkMetadataRequired(maintainer.getUriInt(), ServiceExceptionParameters.MANTAINER_URI_INT, exceptions);
    }

}
