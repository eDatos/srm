package org.siemac.metamac.srm.web.client.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CategorisationUiHandlers extends UiHandlers {

    void retrieveCategorisations();
    void createCategorisations(List<String> categoryUrns);
    void deleteCategorisations(List<String> urns);

    void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria);
    void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn);
}
