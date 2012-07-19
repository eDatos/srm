package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveDsd {

    @In(1)
    DataStructureDefinitionDto dsd;

    @Out(1)
    DataStructureDefinitionDto dsdSaved;
}
