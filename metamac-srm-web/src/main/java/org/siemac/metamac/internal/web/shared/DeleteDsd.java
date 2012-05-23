package org.siemac.metamac.internal.web.shared;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteDsd {

    @In(1)
    DataStructureDefinitionDto dataStructureDefinitionDto;

}
