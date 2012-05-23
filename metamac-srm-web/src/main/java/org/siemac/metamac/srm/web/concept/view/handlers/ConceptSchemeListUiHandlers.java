package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptSchemeListUiHandlers extends UiHandlers {

    void goToConceptScheme(String idLogic);

    void createConceptScheme(ConceptSchemeDto conceptSchemeDto);

    void deleteConceptSchemes(List<String> uuidsFromSelected);

    void onResultSetFirstButtonClicked();

    void onResultSetPreviousButtonClicked();

    void onResultSetNextButtonClicked();

    void onResultSetLastButtonClicked();
}
