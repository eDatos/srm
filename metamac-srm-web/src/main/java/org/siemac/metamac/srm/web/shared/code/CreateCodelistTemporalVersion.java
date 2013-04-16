package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CreateCodelistTemporalVersion {

    @In(1)
    String             urn;

    @Out(1)
    Boolean            isPlannedInBackground;

    @Out(2)
    CodelistMetamacDto codelistMetamacDto;
}
