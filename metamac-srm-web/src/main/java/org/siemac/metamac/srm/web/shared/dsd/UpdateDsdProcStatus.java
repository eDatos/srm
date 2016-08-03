package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateDsdProcStatus {

    @In(1)
    DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDtoToUpdateStatus;

    @In(2)
    ProcStatusEnum                    nextProcStatus;

    @In(3)
    Boolean                           forceLatestFinal;

    @Out(1)
    DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;

    @Optional
    @Out(2)
    MetamacWebException               notificationException;
}
