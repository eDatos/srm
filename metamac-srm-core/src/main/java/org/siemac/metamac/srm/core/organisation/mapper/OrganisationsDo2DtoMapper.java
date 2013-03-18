package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

public interface OrganisationsDo2DtoMapper {

    // Organisation schemes
    public OrganisationSchemeMetamacDto organisationSchemeMetamacDoToDto(OrganisationSchemeVersionMetamac source);
    public List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDoListToDtoList(List<OrganisationSchemeVersionMetamac> sources);

    // Organisations
    public OrganisationMetamacDto organisationMetamacDoToDto(OrganisationMetamac source);
    public RelatedResourceDto organisationMetamacDoToRelatedResourceDto(OrganisationMetamac source) throws MetamacException;
    public List<ItemHierarchyDto> organisationMetamacDoListToItemHierarchyDtoList(List<OrganisationMetamac> sources);

    // Contacts
    public ContactDto contactDoToDto(Contact source);
}
