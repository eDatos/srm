package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateDsdProcStatus {

    @In(1)
    Long                               id;

    @In(2)
    ItemSchemeMetamacProcStatusEnum procStatus;

    @Out(1)
    DataStructureDefinitionDto         dataStructureDefinitionDto;

}
