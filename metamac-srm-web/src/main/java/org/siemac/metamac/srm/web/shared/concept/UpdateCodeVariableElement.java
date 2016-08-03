package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateCodeVariableElement {

    @In(1)
    String         codeUrn;

    @In(2)
    String         variableElementUrn;

    @Out(1)
    CodeMetamacDto codeMetamacDto;
}
