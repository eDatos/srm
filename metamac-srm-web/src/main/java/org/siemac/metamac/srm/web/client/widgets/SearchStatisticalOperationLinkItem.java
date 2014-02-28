package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.filters.SimpleFilterForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.utils.FormUtils;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchSingleExternalItemPaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public abstract class SearchStatisticalOperationLinkItem extends SearchExternalItemLinkItem {

    private SearchSingleExternalItemPaginatedWindow<MetamacWebCriteria> searchOperationWindow;

    public SearchStatisticalOperationLinkItem(String name, String title) {
        super(name, title);
    }

    @Override
    protected void onSearch() {
        final int OPERATION_FIRST_RESULT = 0;
        final int OPERATION_MAX_RESULTS = 16;

        searchOperationWindow = new SearchSingleExternalItemPaginatedWindow<MetamacWebCriteria>(MetamacWebCommon.getConstants().resourceSelection(), OPERATION_MAX_RESULTS, new SimpleFilterForm(),
                new SearchPaginatedAction<MetamacWebCriteria>() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                        retrieveStatisticalOperations(firstResult, maxResults, webCriteria);
                    };
                });

        retrieveStatisticalOperations(OPERATION_FIRST_RESULT, OPERATION_MAX_RESULTS, searchOperationWindow.getSearchCriteria());

        searchOperationWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                ExternalItemDto statisticalOperation = searchOperationWindow.getSelectedResource();
                if (getForm() != null) {
                    FormUtils.setValue(getForm(), getName(), statisticalOperation);
                }
                searchOperationWindow.destroy();
            }
        });
    }

    public void setOperations(List<ExternalItemDto> externalItemsDtos, int firstResult, int totalResults) {
        if (searchOperationWindow != null) {
            searchOperationWindow.setResources(externalItemsDtos);
            searchOperationWindow.refreshSourcePaginationInfo(firstResult, externalItemsDtos.size(), totalResults);
        }
    }

    protected abstract void retrieveStatisticalOperations(int firstResult, int maxResults, MetamacWebCriteria webCriteria);
}
