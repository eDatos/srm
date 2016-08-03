package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

public class VersionableResourcePaginatedCheckListGrid extends PaginatedCheckListGrid {

    public VersionableResourcePaginatedCheckListGrid(int maxResults, PaginatedAction action) {
        super(maxResults, new VersionableResourceCustomListGrid(), action);
        ListGridUtils.setCheckBoxSelectionType(getListGrid());
    }

    public void setUiHandlers(BaseUiHandlers uiHandlers) {
        ((VersionableResourceCustomListGrid) getListGrid()).setUiHandlers(uiHandlers);
    }
}
