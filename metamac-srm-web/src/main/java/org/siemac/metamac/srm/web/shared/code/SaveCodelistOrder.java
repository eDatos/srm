package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveCodelistOrder {

    @In(1)
    String                        codelistUrn;

    @In(2)
    CodelistOrderVisualisationDto codelistOrderVisualisationDto;

    @Out(1)
    CodelistOrderVisualisationDto codelistOrderSaved;
}
