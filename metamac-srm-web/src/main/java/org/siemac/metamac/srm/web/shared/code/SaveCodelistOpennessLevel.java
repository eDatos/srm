package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.srm.core.code.dto.CodelistOpennessVisualisationDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveCodelistOpennessLevel {

    @In(1)
    String                           codelistUrn;

    @In(2)
    CodelistOpennessVisualisationDto codelistOpennessVisualisationDto;

    @Out(1)
    CodelistOpennessVisualisationDto codelistOpennessVisualisationSaved;
}
