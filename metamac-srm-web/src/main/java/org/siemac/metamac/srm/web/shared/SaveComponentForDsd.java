package org.siemac.metamac.srm.web.shared;

import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveComponentForDsd {

    @In(1)
    Long              idDsd;

    @In(2)
    ComponentDto      componentDto;

    @In(3)
    TypeComponentList typeComponentList;

    @Out(1)
    ComponentDto      componentDtoSaved;
}
