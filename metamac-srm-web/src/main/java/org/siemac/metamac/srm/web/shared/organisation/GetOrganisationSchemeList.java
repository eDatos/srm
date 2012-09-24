package org.siemac.metamac.srm.web.shared.organisation;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetOrganisationSchemeList {

    @In(1)
    int                                firstResult;

    @In(2)
    int                                maxResults;

    @In(3)
    String                             organisationScheme;

    @Out(1)
    List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos;

    @Out(2)
    Integer                            pageNumber;

    @Out(3)
    Integer                            totalResults;

}
