package org.siemac.metamac.srm.web.shared.organisation;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CancelOrganisationSchemeValidity {

    @In(1)
    List<String>                       urns;

    @Out(1)
    List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos;

}
