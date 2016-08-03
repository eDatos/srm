package org.siemac.metamac.srm.web.shared.organisation;

import java.util.List;

import org.siemac.metamac.srm.web.shared.criteria.OrganisationContactWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetOrganisationContacts {

    @In(1)
    OrganisationContactWebCriteria criteria;

    @Out(1)
    List<ContactDto>               contactDtos;
}
