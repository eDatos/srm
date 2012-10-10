package org.siemac.metamac.srm.core.organisation.serviceapi.utils;

import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.utils.OrganisationsDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationsMetamacDtoMocks extends OrganisationsDtoMocks {

    // -----------------------------------------------------------------------------------
    // ORGANISATIONS
    // -----------------------------------------------------------------------------------

    public static OrganisationSchemeMetamacDto mockOrganisationScheme(OrganisationSchemeTypeEnum type) {
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = new OrganisationSchemeMetamacDto();
        organisationSchemeMetamacDto.setLifeCycle(new LifeCycleDto());
        mockOrganisationSchemeDto(organisationSchemeMetamacDto, type);
        return organisationSchemeMetamacDto;
    }
    // -----------------------------------------------------------------------------------
    // ORGANISATIONS
    // -----------------------------------------------------------------------------------

    public static OrganisationMetamacDto mockOrganisationDto(OrganisationTypeEnum type) {
        OrganisationMetamacDto organisationMetamacDto = new OrganisationMetamacDto();
        OrganisationsDtoMocks.mockOrganisationDto(organisationMetamacDto, type);
        return organisationMetamacDto;
    }
}
