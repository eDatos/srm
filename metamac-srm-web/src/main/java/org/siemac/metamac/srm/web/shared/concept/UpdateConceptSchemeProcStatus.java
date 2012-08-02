package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;
import org.siemac.metamac.srm.core.enume.domain.MaintainableArtefactProcStatusEnum;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateConceptSchemeProcStatus {

    @In(1)
    Long                               id;

    @In(2)
    MaintainableArtefactProcStatusEnum procStatus;

    @Out(1)
    MetamacConceptSchemeDto            conceptSchemeDto;

}
