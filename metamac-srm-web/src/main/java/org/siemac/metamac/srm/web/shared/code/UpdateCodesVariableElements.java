package org.siemac.metamac.srm.web.shared.code;

import java.util.Map;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class UpdateCodesVariableElements {

    @In(1)
    String          codelistUrn;

    @In(2)
    Map<Long, Long> variableElementsIdByCodeId;
}
