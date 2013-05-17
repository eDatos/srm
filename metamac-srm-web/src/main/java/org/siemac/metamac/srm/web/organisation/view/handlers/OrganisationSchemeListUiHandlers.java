package org.siemac.metamac.srm.web.organisation.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public interface OrganisationSchemeListUiHandlers extends BaseUiHandlers {

    void goToOrganisationScheme(String urn, OrganisationSchemeTypeEnum type);
    void createOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto);
    void deleteOrganisationSchemes(List<String> urns);
    void retrieveOrganisationSchemes(int firstResult, int maxResults, OrganisationSchemeWebCriteria organisationSchemeWebCriteria);
    void cancelValidity(List<String> urn);
}
