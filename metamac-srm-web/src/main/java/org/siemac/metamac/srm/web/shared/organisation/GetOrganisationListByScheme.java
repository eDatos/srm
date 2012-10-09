package org.siemac.metamac.srm.web.shared.organisation;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.OrganisationDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetOrganisationListByScheme {

    @In(1)
    String                schemeUrn;

    @Out(1)
    List<OrganisationDto> organisationDtos;

}
