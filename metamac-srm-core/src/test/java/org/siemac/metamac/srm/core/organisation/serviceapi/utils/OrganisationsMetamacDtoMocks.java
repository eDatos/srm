package org.siemac.metamac.srm.core.organisation.serviceapi.utils;

import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.utils.OrganisationsDtoMocks;

public class OrganisationsMetamacDtoMocks extends OrganisationsDtoMocks {

    public static OrganisationSchemeMetamacDto mockOrganisationScheme() {
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = new OrganisationSchemeMetamacDto();
        organisationSchemeMetamacDto.setLifeCycle(new LifeCycleDto());
        mockOrganisationSchemeDto(organisationSchemeMetamacDto);
        return organisationSchemeMetamacDto;
    }

}
