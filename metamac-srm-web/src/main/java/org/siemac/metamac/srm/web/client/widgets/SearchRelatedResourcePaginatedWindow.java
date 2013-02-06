package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.BaseSearchWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class SearchRelatedResourcePaginatedWindow extends BaseSearchWindow {

    private InformationLabel                   infoMessageLabel;
    private SearchRelatedResourcePaginatedItem listGridItem;
    private FormItem                           initialSelectionItem;

    public SearchRelatedResourcePaginatedWindow(String title, int maxResults, PaginatedAction action) {
        super(title);
        common(title, maxResults, null, action);
    }

    public SearchRelatedResourcePaginatedWindow(String title, int maxResults, FormItem initialSelectionItem, PaginatedAction action) {
        super(title);
        this.initialSelectionItem = initialSelectionItem;
        this.initialSelectionItem.setWidth("100%");
        common(title, maxResults, initialSelectionItem, action);
    }

    private void common(String title, int maxResults, FormItem initialSelectionItem, PaginatedAction action) {
        listGridItem = new SearchRelatedResourcePaginatedItem("list", "title", FORM_ITEM_CUSTOM_WIDTH, maxResults, action);
        listGridItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        listGridItem.setShowTitle(false);

        getListGrid().setSelectionType(SelectionStyle.SINGLE);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());

        if (initialSelectionItem == null) {
            form.setFields(listGridItem, saveItem);
        } else {
            listGridItem.setColSpan(2);
            form.setFields(initialSelectionItem, listGridItem, saveItem);
        }
    }

    public void setInfoMessage(String text) {
        infoMessageLabel = new InformationLabel(text);
        infoMessageLabel.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        infoMessageLabel.setMargin(10);
        infoMessageLabel.setVisibility(Visibility.HIDDEN);

        getSearchWindowLayout().addMember(infoMessageLabel, 0);
    }

    public void showInfoMessage() {
        infoMessageLabel.show();
    }

    public void hideInfoMessage() {
        infoMessageLabel.hide();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public void setRelatedResources(List<RelatedResourceDto> relatedResources) {
        listGridItem.setRelatedResources(relatedResources);
    }

    public RelatedResourceDto getSelectedRelatedResource() {
        return listGridItem.getSelectedRelatedResource();
    }

    public void refreshSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        listGridItem.refreshPaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public void setSearchAction(SearchPaginatedAction action) {
        listGridItem.setSearchAction(action);
    }

    public SearchRelatedResourcePaginatedItem getListGridItem() {
        return listGridItem;
    }

    public BaseCustomListGrid getListGrid() {
        return listGridItem.getListGrid();
    }

    public String getRelatedResourceCriteria() {
        return listGridItem.getRelatedResourceCriteria();
    }

    public FormItem getInitialSelectionItem() {
        return initialSelectionItem;
    }

    public String getInitialSelectionValue() {
        return initialSelectionItem.getValue() != null && StringUtils.isNotEmpty(String.valueOf(initialSelectionItem.getValue())) ? String.valueOf(initialSelectionItem.getValue()) : null;
    }
}
