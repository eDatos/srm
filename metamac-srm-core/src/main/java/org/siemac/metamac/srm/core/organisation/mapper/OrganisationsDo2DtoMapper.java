package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

public interface OrganisationsDo2DtoMapper {

    // Organisation schemes
    public OrganisationSchemeMetamacDto organisationSchemeMetamacDoToDto(OrganisationSchemeVersionMetamac source);
    public List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDoListToDtoList(List<OrganisationSchemeVersionMetamac> sources);

    // Organisations
    public OrganisationMetamacDto organisationMetamacDoToDto(OrganisationMetamac source);
    public List<ItemHierarchyDto> organisationMetamacDoListToItemHierarchyDtoList(List<OrganisationMetamac> sources);
}
