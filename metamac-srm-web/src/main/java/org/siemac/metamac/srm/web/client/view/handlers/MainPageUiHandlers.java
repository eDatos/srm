package org.siemac.metamac.srm.web.client.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;

public interface MainPageUiHandlers extends UiHandlers {

    void openHelpUrl();
    void closeSession();

    void onNavigationPaneSectionHeaderClicked(String name);
    void onNavigationPaneSectionClicked(String name);

    void goToConcepts();
    void goToCodelists();
    void goToDsds();
    void goToOrganisations();
    void goToCategories();

    // Importation
    void sDMXResourceImportationFailed(String errorMessage);
    void sDMXResourceImportationSucceed(String fileName);
}
