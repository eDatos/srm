package org.siemac.metamac.srm.web.shared.organisation;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetOrganisationSchemeVersions {

    @In(1)
    String                                  urn;

    @Out(1)
    List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDtos;
}
