package org.siemac.metamac.srm.web.client.widgets.search;

import static org.siemac.metamac.web.common.client.MetamacWebCommon.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public abstract class SearchMultiRelatedResourceSimpleItem extends RelatedResourceListItem {

    private SearchMultipleRelatedResourcePaginatedWindow window;

    public SearchMultiRelatedResourceSimpleItem(String name, String title, int maxResults, ListRecordNavigationClickHandler listRecordNavigationClickHandler) {
        super(name, title, true, listRecordNavigationClickHandler);
        appendWindow(maxResults);
    }

    private void appendWindow(final int maxResults) {
        getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                window = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().resourceSelection(), maxResults, new SearchPaginatedAction<MetamacWebCriteria>() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                        retrieveResources(firstResult, maxResults, webCriteria);
                    }
                });

                addInformationLabel();

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

            private void addInformationLabel() {
                String informationLabelContents = getInformationLabelContents();
                if (!StringUtils.isBlank(informationLabelContents)) {
                    InformationLabel informationLabel = new InformationLabel();
                    informationLabel.setWidth(window.getWidth());
                    informationLabel.setMargin(10);
                    informationLabel.setContents(informationLabelContents);
                    window.getSearchWindowLayout().addMember(informationLabel, 0);
                }
            }
        });
    }

    public void setResources(List<RelatedResourceDto> items, int firstResult, int totalResults) {
        if (window != null) {
            window.setResources(items);
            window.refreshSourcePaginationInfo(firstResult, items.size(), totalResults);
        }
    }

    public String getInformationLabelContents() {
        // By default, no information label is set. This method must be override to set an information label on the top of the search window.
        return null;
    }

    protected abstract void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria);
}
