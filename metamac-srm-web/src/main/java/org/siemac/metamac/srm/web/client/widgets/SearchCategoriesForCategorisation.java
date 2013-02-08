package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

public class SearchCategoriesForCategorisation extends SearchMultipleRelatedResourcePaginatedWithRelatedResourceFilterWindow {

    public SearchCategoriesForCategorisation(int maxResults, PaginatedAction filterListAction, PaginatedAction selectionListAction) {
        super(getConstants().categorisationCreate(), getConstants().categorySchemeFilter(), getConstants().categoriesSelection(), maxResults, filterListAction, selectionListAction);

        // Set the selection list required
        selectionListItem.setRequired(true);
    }

    public boolean validateSelectionListForm() {
        return selectionListItem.getForm().validate(false);
    }
}
