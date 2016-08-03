package org.siemac.metamac.srm.web.client.view.handlers;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public interface StructuralResourcesUiHandlers extends BaseUiHandlers {

    void goToDsd(String urn);
    void goToConceptScheme(String urn);
    void goToOrganisationScheme(String urn, OrganisationSchemeTypeEnum type);
    void goToCategoryScheme(String urn);
    void goToCodelist(String urn);
}
