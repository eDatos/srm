package org.siemac.metamac.srm.web.shared.dsd;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveComponentForDsd {

    @In(1)
    String            dsdUrn;

    @In(2)
    ComponentDto      componentDto;

    @In(3)
    TypeComponentList typeComponentList;

    @Out(1)
    ComponentDto      componentDtoSaved;
}
