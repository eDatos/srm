package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CancelCodelistValidity {

    @In(1)
    List<String>             urns;

    @Out(1)
    List<CodelistMetamacDto> codelistMetamacDtos;

}
