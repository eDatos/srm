package org.siemac.metamac.srm.web.shared.organisation;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetOrganisation {

    @In(1)
    String                       urn;

    @Out(1)
    OrganisationMetamacDto       organisationDto;

    @Out(2)
    OrganisationSchemeMetamacDto organisationSchemeMetamacDto;
}
