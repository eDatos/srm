package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetVariableElementOperationsByVariableElement {

    @In(1)
    String                            variableElementUrn;

    @Out(1)
    List<VariableElementOperationDto> operations;
}
