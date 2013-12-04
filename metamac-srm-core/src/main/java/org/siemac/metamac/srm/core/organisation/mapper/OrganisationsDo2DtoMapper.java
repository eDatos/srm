package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;

public interface OrganisationsDo2DtoMapper {

    // Organisation schemes
    public OrganisationSchemeMetamacDto organisationSchemeMetamacDoToDto(OrganisationSchemeVersionMetamac source);
    public OrganisationSchemeMetamacBasicDto organisationSchemeMetamacDoToBasicDto(OrganisationSchemeVersionMetamac source);
    public List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDoListToDtoList(List<OrganisationSchemeVersionMetamac> sources);
    public RelatedResourceDto organisationSchemeMetamacDoToRelatedResourceDto(OrganisationSchemeVersionMetamac source);

    // Organisations
    public OrganisationMetamacDto organisationMetamacDoToDto(OrganisationMetamac source);
    public OrganisationMetamacBasicDto organisationMetamacDoToBasicDto(OrganisationMetamac source);
    public RelatedResourceDto organisationMetamacDoToRelatedResourceDto(OrganisationMetamac source) throws MetamacException;

    // Contacts
    public ContactDto contactDoToDto(Contact source);
}
