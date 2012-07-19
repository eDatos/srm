package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteDsd {

    @In(1)
    DataStructureDefinitionDto dataStructureDefinitionDto;

}
