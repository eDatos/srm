package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CopyCodelist {

    @In(1)
    String             codelistUrn;

    @In(2)
    String             code;

    @Out(1)
    CodelistMetamacDto codelistCopied;

    @Out(2)
    Boolean            isPlannedInBackground;
}
