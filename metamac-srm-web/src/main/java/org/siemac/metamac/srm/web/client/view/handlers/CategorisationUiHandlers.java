package org.siemac.metamac.srm.web.client.view.handlers;

import java.util.List;

public interface CategorisationUiHandlers extends BaseUiHandlers {

    void retrieveCategorisations(String artefactCategorisedUrn);
    void createCategorisations(List<String> categoryUrns);
    void deleteCategorisations(List<String> urns);

    void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria);
    void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn);
}
