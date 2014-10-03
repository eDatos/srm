package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateConceptSchemeProcStatus {

    @In(1)
    ConceptSchemeMetamacDto conceptSchemeMetamacDto;

    @In(2)
    ProcStatusEnum          nextProcStatus;

    @In(3)
    Boolean                 forceLatestFinal;       // Only suitable for internal publication

    @Out(1)
    ConceptSchemeMetamacDto conceptSchemeDto;

    @Optional
    @Out(2)
    MetamacWebException     notificationException;
}
