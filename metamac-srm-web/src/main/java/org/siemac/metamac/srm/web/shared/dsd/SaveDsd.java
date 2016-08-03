package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveDsd {

    @In(1)
    DataStructureDefinitionMetamacDto dsd;

    @Out(1)
    DataStructureDefinitionMetamacDto dsdSaved;
}
