package org.siemac.metamac.srm.web.shared;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class FindCodeLists {

    @In(1)
    String                uriConcept;

    @Out(1)
    List<ExternalItemDto> codeLists;
}
