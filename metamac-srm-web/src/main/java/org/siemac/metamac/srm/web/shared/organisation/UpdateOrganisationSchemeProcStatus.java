package org.siemac.metamac.srm.web.shared.organisation;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateOrganisationSchemeProcStatus {

    @In(1)
    String                       urn;

    @In(2)
    ProcStatusEnum               nextProcStatus;

    @In(3)
    ProcStatusEnum               currentProcStatus;

    @Out(1)
    OrganisationSchemeMetamacDto organisationSchemeDto;

}
