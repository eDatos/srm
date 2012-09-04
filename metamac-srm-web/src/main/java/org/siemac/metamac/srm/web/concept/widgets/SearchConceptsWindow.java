package org.siemac.metamac.srm.web.concept.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.BaseSearchWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class SearchConceptsWindow extends BaseSearchWindow {

    private SearchConceptsPaginatedItem conceptsListGridItem;

    public SearchConceptsWindow(String title, int maxResults, PaginatedAction action) {
        super(title, maxResults, action);

        conceptsListGridItem = new SearchConceptsPaginatedItem("list", "title", FORM_ITEM_CUSTOM_WIDTH, maxResults, action);
        conceptsListGridItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        conceptsListGridItem.setShowTitle(false);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());

        form.setFields(conceptsListGridItem, saveItem);
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public void setConcepts(List<ConceptMetamacDto> conceptMetamacDtos) {
        conceptsListGridItem.setConcepts(conceptMetamacDtos);
    }

    public ConceptMetamacDto getSelectedConcept() {
        return conceptsListGridItem.getSelectedConcept();
    }

    public void refreshSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        conceptsListGridItem.refreshPaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public SearchConceptsPaginatedItem getConceptsListGridItem() {
        return conceptsListGridItem;
    }

    public BaseCustomListGrid getListGrid() {
        return conceptsListGridItem.getListGrid();
    }

}
