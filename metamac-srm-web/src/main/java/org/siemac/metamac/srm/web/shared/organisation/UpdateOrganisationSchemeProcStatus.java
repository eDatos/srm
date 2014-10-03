package org.siemac.metamac.srm.web.shared.organisation;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateOrganisationSchemeProcStatus {

    @In(1)
    String                       urn;

    @In(2)
    OrganisationSchemeTypeEnum   organisationSchemeType;

    @In(3)
    ProcStatusEnum               nextProcStatus;

    @In(4)
    ProcStatusEnum               currentProcStatus;

    @In(5)
    Boolean                      forceLastestFinal;     // Only suitable for internal publication

    @Out(1)
    OrganisationSchemeMetamacDto organisationSchemeDto;

    @Optional
    @Out(2)
    MetamacWebException          notificationException;
}
