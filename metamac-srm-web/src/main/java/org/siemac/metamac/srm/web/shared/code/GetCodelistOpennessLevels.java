package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistOpennessVisualisationDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCodelistOpennessLevels {

    @In(1)
    String                                 codelistUrn;

    @Out(1)
    List<CodelistOpennessVisualisationDto> codelistOpennessVisualisationDtos;
}
