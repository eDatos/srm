package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCodesByCodelist {

    @In(1)
    String                               codelistUrn;

    @In(2)
    String                               codelistOrderUrn;

    @In(3)
    String                               codelistOpennessLevelUrn;

    @In(4)
    String                               locale;

    @Out(1)
    List<CodeMetamacVisualisationResult> codes;

    @Out(2)
    CodelistVisualisationDto             codelistOrderVisualisationDto;

    @Out(3)
    CodelistVisualisationDto             codelistOpennessVisualisationDto;
}
