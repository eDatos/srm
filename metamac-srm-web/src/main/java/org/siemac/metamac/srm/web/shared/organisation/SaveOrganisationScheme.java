package org.siemac.metamac.srm.web.shared.organisation;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveOrganisationScheme {

    @In(1)
    OrganisationSchemeMetamacDto organisationSchemeToSave;

    @Out(1)
    OrganisationSchemeMetamacDto organisationSchemeSaved;

}
