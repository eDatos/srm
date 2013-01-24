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

    private SearchConceptsPaginatedItem conceptsPaginatedItem;

    public SearchConceptsWindow(String title, int maxResults, PaginatedAction action) {
        super(title);

        conceptsPaginatedItem = new SearchConceptsPaginatedItem("list", "title", FORM_ITEM_CUSTOM_WIDTH, maxResults, action);
        conceptsPaginatedItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        conceptsPaginatedItem.setShowTitle(false);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());

        form.setFields(conceptsPaginatedItem, saveItem);
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public void setConcepts(List<ConceptMetamacDto> conceptMetamacDtos) {
        conceptsPaginatedItem.setConcepts(conceptMetamacDtos);
    }

    public List<ConceptMetamacDto> getSelectedConcepts() {
        return conceptsPaginatedItem.getSelectedConcepts();
    }

    public void refreshSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        conceptsPaginatedItem.refreshPaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public SearchConceptsPaginatedItem getConceptsListGridItem() {
        return conceptsPaginatedItem;
    }

    public BaseCustomListGrid getListGrid() {
        return conceptsPaginatedItem.getListGrid();
    }
}
