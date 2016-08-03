package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CopyDsd {

    @In(1)
    String                            urn;

    @In(2)
    String                            code;

    @Out(2)
    DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;
}
