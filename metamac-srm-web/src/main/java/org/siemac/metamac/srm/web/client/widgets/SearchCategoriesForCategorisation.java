package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

public class SearchCategoriesForCategorisation extends SearchMultipleRelatedResourcePaginatedWithRelatedResourceFilterWindow {

    protected static final String FIELD_IS_LAST_VERSION = "is-last-version";

    public SearchCategoriesForCategorisation(int maxResults, PaginatedAction filterListAction, PaginatedAction selectionListAction) {
        super(getConstants().categorisationCreate(), getConstants().categorySchemeFilter(), getConstants().categorySchemeSelected(), getConstants().categoriesSelection(), maxResults,
                filterListAction, selectionListAction);

        CustomCheckboxItem isLastVersionItem = new CustomCheckboxItem(FIELD_IS_LAST_VERSION, MetamacWebCommon.getConstants().resourceOnlyLastVersion());
        isLastVersionItem.setVisible(false);
        isLastVersionItem.setTitleStyle("staticFormItemTitle");
        isLastVersionItem.setValue(true);

        getInitialFilterForm().addFields(isLastVersionItem);
    }

    public void showIsLastVersionItem() {
        getInitialFilterForm().getItem(FIELD_IS_LAST_VERSION).show();
        getInitialFilterForm().getItem(FIELD_IS_LAST_VERSION).setValue(true); // by default, isLastVersion is selected
    }

    public boolean getIsLastVersionValue() {
        return ((CustomCheckboxItem) getInitialFilterForm().getItem(FIELD_IS_LAST_VERSION)).getValueAsBoolean();
    }

    public CustomCheckboxItem getIsLastVersionItem() {
        return (CustomCheckboxItem) getInitialFilterForm().getItem(FIELD_IS_LAST_VERSION);
    }
}
