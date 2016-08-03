package org.siemac.metamac.srm.web.client.view.handlers;

import java.util.Date;
import java.util.List;

import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface CategorisationUiHandlers extends BaseUiHandlers {

    void retrieveCategorisations(String artefactCategorisedUrn);
    void createCategorisations(List<String> categoryUrns);
    void deleteCategorisations(List<String> urns);
    void exportCategorisations(List<String> urns, ExportDetailEnum detail, ExportReferencesEnum references);
    void cancelCategorisationValidity(List<String> urn, Date validTo);

    void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, CategorySchemeWebCriteria categorySchemeWebCriteria);
    void retrieveCategoriesForCategorisations(int firstResult, int maxResults, CategoryWebCriteria categoryWebCriteria);
}
