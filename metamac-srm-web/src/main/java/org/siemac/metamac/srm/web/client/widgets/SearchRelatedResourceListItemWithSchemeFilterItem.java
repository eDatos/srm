package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceItemWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceWebCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public abstract class SearchRelatedResourceListItemWithSchemeFilterItem extends RelatedResourceListItem {

    private SearchMultipleRelatedResourceItemWithSchemeFilterPaginatedWindow window;

    public SearchRelatedResourceListItemWithSchemeFilterItem(String name, String title, int maxResults, ListRecordNavigationClickHandler listRecordNavigationClickHandler) {
        super(name, title, true, listRecordNavigationClickHandler);
        appendWindow(maxResults);
    }

    private void appendWindow(final int maxResults) {
        getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                SearchPaginatedAction<RelatedResourceWebCriteria> filterSearchAction = new SearchPaginatedAction<RelatedResourceWebCriteria>() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, RelatedResourceWebCriteria webCriteria) {
                        webCriteria.setOnlyLastVersion(window.getFilter().getSearchCriteria().isItemSchemeLastVersion());
                        retrieveItemSchemes(firstResult, maxResults, webCriteria);
                    }
                };

                window = new SearchMultipleRelatedResourceItemWithSchemeFilterPaginatedWindow(MetamacWebCommon.getConstants().resourceSelection(), maxResults, filterSearchAction,
                        new SearchPaginatedAction<RelatedResourceItemWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, RelatedResourceItemWebCriteria webCriteria) {
                                retrieveItems(firstResult, maxResults, webCriteria);
                            }
                        });

                window.getFilter().getItemSchemeFilterFacet().getFormItem().setVisible(isItemSchemeFilterFacetVisible());

                window.retrieveItems();

                window.setSelectedResources(getSelectedRelatedResources());

                window.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        setRelatedResources(window.getSelectedResources());
                        window.markForDestroy();
                    }
                });
            }
        });
    }

    protected abstract void retrieveItemSchemes(int firstResult, int maxResults, RelatedResourceWebCriteria webCriteria);

    protected abstract void retrieveItems(int firstResult, int maxResults, RelatedResourceItemWebCriteria webCriteria);

    protected boolean isItemSchemeFilterFacetVisible() {
        return true;
    }

    public void setFilterResources(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (window != null) {
            window.setFilterResources(resources);
            window.refreshFilterSourcePaginationInfo(firstResult, resources.size(), totalResults);
        }
    }

    public void setResources(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (window != null) {
            window.setResources(resources);
            window.refreshSourcePaginationInfo(firstResult, resources.size(), totalResults);
        }
    }
}
