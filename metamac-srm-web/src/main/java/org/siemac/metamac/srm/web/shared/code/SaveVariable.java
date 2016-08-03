package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.srm.core.code.dto.VariableDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveVariable {

    @In(1)
    VariableDto variableDto;

    @Out(1)
    VariableDto savedVariableDto;
}
