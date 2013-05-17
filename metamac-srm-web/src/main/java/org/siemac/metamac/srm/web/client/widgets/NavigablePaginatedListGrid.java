package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.PaginatedListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

public class NavigablePaginatedListGrid extends PaginatedListGrid {

    public NavigablePaginatedListGrid(int maxResults, PaginatedAction action) {
        super(new NavigableListGrid(), maxResults, action);
    }

    public void setUiHandlers(BaseUiHandlers uiHandlers) {
        ((NavigableListGrid) getListGrid()).setUiHandlers(uiHandlers);
    }
}
