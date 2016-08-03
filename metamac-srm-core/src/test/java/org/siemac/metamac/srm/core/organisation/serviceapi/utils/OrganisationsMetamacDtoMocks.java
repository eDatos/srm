package org.siemac.metamac.srm.core.organisation.serviceapi.utils;

import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.utils.OrganisationsDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

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

    public static RelatedResourceDto mockMaintainerDto(String code, String urn) {
        RelatedResourceDto target = new RelatedResourceDto();
        target.setCode(code);
        target.setUrn(urn);
        target.setType(RelatedResourceTypeEnum.AGENCY);
        return target;

    }
}
