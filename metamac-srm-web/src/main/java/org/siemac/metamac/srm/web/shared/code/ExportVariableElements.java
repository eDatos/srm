package org.siemac.metamac.srm.web.shared.code;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ExportVariableElements {

    @In(1)
    String variableUrn;

    @Out(1)
    String fileName;
}
