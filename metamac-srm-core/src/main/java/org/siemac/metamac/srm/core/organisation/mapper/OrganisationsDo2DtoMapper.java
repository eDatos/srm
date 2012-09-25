package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

public interface OrganisationsDo2DtoMapper {

    public OrganisationSchemeMetamacDto organisationSchemeMetamacDoToDto(OrganisationSchemeVersionMetamac source);
    public List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDoListToDtoList(List<OrganisationSchemeVersionMetamac> sources);

}
