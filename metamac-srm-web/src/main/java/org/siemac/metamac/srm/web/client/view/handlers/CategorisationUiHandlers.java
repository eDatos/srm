package org.siemac.metamac.srm.web.client.view.handlers;

import java.util.Date;
import java.util.List;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface CategorisationUiHandlers extends BaseUiHandlers {

    void retrieveCategorisations(String artefactCategorisedUrn);
    void createCategorisations(List<String> categoryUrns);
    void deleteCategorisations(List<String> urns);
    void cancelCategorisationValidity(List<String> urn, Date validTo);

    void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria);
    void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn);
}
