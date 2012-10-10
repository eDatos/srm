package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

public interface OrganisationsDo2DtoMapper {

    // Organisation schemes
    public OrganisationSchemeMetamacDto organisationSchemeMetamacDoToDto(OrganisationSchemeVersionMetamac source);
    public List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDoListToDtoList(List<OrganisationSchemeVersionMetamac> sources);
    
    // Organisations
    public OrganisationMetamacDto organisationMetamacDoToDto(OrganisationMetamac source);


}
