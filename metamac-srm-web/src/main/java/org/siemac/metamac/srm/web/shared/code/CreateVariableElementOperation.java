package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CreateVariableElementOperation {

    @In(1)
    VariableElementOperationTypeEnum operationType;

    @In(2)
    List<String>                     variableElementUrns;

    @In(3)
    String                           variableElementUrn;

    @Out(1)
    VariableElementOperationDto      variableElementOperationDto;
}
