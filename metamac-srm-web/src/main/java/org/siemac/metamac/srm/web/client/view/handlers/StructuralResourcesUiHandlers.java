package org.siemac.metamac.srm.web.client.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;

public interface StructuralResourcesUiHandlers extends UiHandlers {

    void goToDsd(String urn);

    void goToConceptScheme(String id);

}
