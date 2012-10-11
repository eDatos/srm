package org.siemac.metamac.srm.web.shared.organisation;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveOrganisation {

    @In(1)
    OrganisationMetamacDto organisationToSave;

    @Out(1)
    OrganisationMetamacDto organisationSaved;

}
