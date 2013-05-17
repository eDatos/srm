package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

public class ItemsPaginatedListGrid extends NavigablePaginatedListGrid {

    public ItemsPaginatedListGrid(int maxResults, PaginatedAction action) {
        super(maxResults, action);
    }

    public void setUiHandlers(BaseUiHandlers uiHandlers) {
        ((NavigableListGrid) getListGrid()).setUiHandlers(uiHandlers);
    }
}
