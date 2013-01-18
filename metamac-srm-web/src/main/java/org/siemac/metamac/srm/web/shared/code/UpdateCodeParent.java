package org.siemac.metamac.srm.web.shared.code;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class UpdateCodeParent {

    @In(1)
    String codeUrn;

    @In(2)
    String newParentUrn;
}
