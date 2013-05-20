package org.siemac.metamac.srm.web.organisation.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationContactWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;

public interface OrganisationUiHandlers extends BaseOrganisationUiHandlers {

    void retrieveOrganisation(String organisationUrn);
    void updateOrganisation(OrganisationMetamacDto organisationDto);
    void deleteOrganisation(OrganisationMetamacDto organisationMetamacDto);
    void retrieveOrganisationListByScheme(String organisationSchemeUrn);
    void retrieveContacts(OrganisationContactWebCriteria criteria);
    void updateContacts(List<ContactDto> contacts, Long contactToUpdateId);
}
