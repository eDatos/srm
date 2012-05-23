package org.siemac.metamac.internal.web.shared;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsd {

    @In(1)
    Long                       idDsd;

    @Out(1)
    DataStructureDefinitionDto dsd;
}
