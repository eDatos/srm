package org.siemac.metamac.srm.web.client.widgets.search;

import static org.siemac.metamac.web.common.client.MetamacWebCommon.getConstants;

import java.util.List;

import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceLinkItem;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceItemWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceWebCriteria;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public abstract class SearchSrmItemLinkItemWithSchemeFilterItem extends SearchRelatedResourceLinkItem {

    private int                                                maxResults;
    private SearchSingleSrmItemWithSchemeFilterPaginatedWindow window;

    public SearchSrmItemLinkItemWithSchemeFilterItem(String name, String title, int maxResults, CustomLinkItemNavigationClickHandler clickHandler) {
        super(name, title, clickHandler);
        this.maxResults = maxResults;

        searchIcon.addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                onSearch();

            }
        });
    }

    private void onSearch() {
        SearchPaginatedAction<RelatedResourceWebCriteria> filterSearchAction = new SearchPaginatedAction<RelatedResourceWebCriteria>() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, RelatedResourceWebCriteria webCriteria) {
                webCriteria.setOnlyLastVersion(window.getFilter().getSearchCriteria().isItemSchemeLastVersion());
                retrieveItemSchemes(firstResult, maxResults, webCriteria);
            }
        };

        window = new SearchSingleSrmItemWithSchemeFilterPaginatedWindow(getConstants().resourceSelection(), maxResults, filterSearchAction,
                new SearchPaginatedAction<RelatedResourceItemWebCriteria>() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, RelatedResourceItemWebCriteria webCriteria) {
                        retrieveItems(firstResult, maxResults, webCriteria);
                    }
                });

        window.retrieveItems();

        window.setSaveAction(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                RelatedResourceDto selectedResource = window.getSelectedResource();
                SearchSrmItemLinkItemWithSchemeFilterItem.this.setRelatedResource(selectedResource);
                window.markForDestroy();
            }
        });
    }

    protected abstract void retrieveItemSchemes(int firstResult, int maxResults, RelatedResourceWebCriteria webCriteria);

    protected abstract void retrieveItems(int firstResult, int maxResults, RelatedResourceItemWebCriteria webCriteria);

    public void setFilterResources(List<RelatedResourceDto> relatedResourceDtos, int firstResult, int elementsInPage, int totalResults) {
        if (window != null) {
            window.setFilterResources(relatedResourceDtos);
            window.refreshFilterSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }

    public void setResources(List<RelatedResourceDto> relatedResourceDtos, int firstResult, int elementsInPage, int totalResults) {
        if (window != null) {
            window.setResources(relatedResourceDtos);
            window.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }
}
