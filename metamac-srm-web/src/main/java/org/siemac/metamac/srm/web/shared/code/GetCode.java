package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCode {

    @In(1)
    String         urn;

    @Out(1)
    CodeMetamacDto codeDto;
}
