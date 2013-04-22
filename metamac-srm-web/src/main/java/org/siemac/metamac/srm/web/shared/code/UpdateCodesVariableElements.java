package org.siemac.metamac.srm.web.shared.code;

import java.util.Map;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class UpdateCodesVariableElements {

    @In(1)
    Map<Long, Long> variableElementsIdByCodeId;
}
