package org.siemac.metamac.srm.web.client.view.handlers;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.gwtplatform.mvp.client.UiHandlers;

public interface StructuralResourcesUiHandlers extends UiHandlers {

    void goToDsd(String urn);
    void goToConceptScheme(String urn);
    void goToOrganisationScheme(String urn, OrganisationSchemeTypeEnum type);
    void goToCategoryScheme(String urn);

}
