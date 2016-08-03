package org.siemac.metamac.srm.web.shared.organisation;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDefaultMaintainer {

    @Out(1)
    OrganisationMetamacDto organisationMetamacDto;
}
