package org.siemac.metamac.srm.web.shared.organisation;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetOrganisations {

    @In(1)
    int                               firstResult;

    @In(2)
    int                               maxResults;

    @In(3)
    OrganisationWebCriteria           criteria;

    @Out(1)
    List<OrganisationMetamacBasicDto> organisations;

    @Out(2)
    Integer                           firstResultOut;

    @Out(3)
    Integer                           totalResults;
}
