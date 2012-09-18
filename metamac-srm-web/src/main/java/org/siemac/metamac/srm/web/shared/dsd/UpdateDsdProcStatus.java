package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateDsdProcStatus {

    @In(1)
    String                            urn;

    @In(2)
    ItemSchemeMetamacProcStatusEnum   nextProcStatus;

    @In(3)
    ItemSchemeMetamacProcStatusEnum   currentProcStatus;

    @Out(1)
    DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;

}
