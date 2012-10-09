package org.siemac.metamac.srm.web.shared.organisation;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.OrganisationDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveOrganisation {

    @In(1)
    OrganisationDto organisationToSave;

    @Out(1)
    OrganisationDto organisationSaved;

}
